/**
 * License
 * =======
 *
 * The MIT License (MIT)
 *
 *
 * Copyright (c) 2017 Antoine DOERAENE @sherpal
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package plot

import complex.Complex
import gameengine.Engine
import gui._
import webglgraphics.Canvas2D


class DrawArea(val plot: Plot) extends Frame {
  setParent(plot)
  setPoint(TopLeft, plot, TopLeft, 10, -10)
  setPoint(BottomRight, plot, BottomRight, -10, 10)

  private val bg = createTexture(layer = BackgroundLayer)
  bg.setAllPoints()
  bg.setVertexColor(200.0 / 255, 200.0 / 255, 200.0 / 255)

  private val axes = createTexture(layer = BackgroundLayer, subLayer = 1)
  axes.setAllPoints()
  axes.setTexture(new Canvas2D())
  axes.canvas.get.setSize(width.toInt, height.toInt)

  private val canvas = createTexture()
  canvas.setAllPoints()
  canvas.setTexture(new Canvas2D())
  canvas.canvas.get.setSize(width.toInt, height.toInt)

  var drawMode: DrawAreaMode = PlotWindowsArea.drawMode

  var drawColors: (Double, Double, Double) = PlotWindowsArea.drawColor

  def drawAxes(): Unit = {
    val (realAxis, imaginaryAxis) = plot.axes
    val xUnit = width / (realAxis._2 - realAxis._1)
    val yUnit = height / (imaginaryAxis._2 - imaginaryAxis._1)

    Engine.painter.withCanvases(axes.canvas.get)({
      Engine.painter.clear()

      if (imaginaryAxis._1 * imaginaryAxis._2 < 0) {
        // 0 \in [imaginaryAxis._1, imaginaryAxis._2]
        Engine.painter.withColor(170.0 / 255, 170.0 / 255, 170.0 / 255)({
          Engine.painter.drawLine(
            List(Complex(-2, 0), Complex(2, 0)).map(z => Complex(z.re * xUnit + (realAxis._2 + realAxis._1) / 2,
              z.im * yUnit + (imaginaryAxis._1 + imaginaryAxis._2) / 2)), lineWidth = 2
          )
        })
      }

      if (realAxis._1 * realAxis._2 < 0) {
        // 0 \in [realAxis._1, realAxis._2]
        Engine.painter.withColor(170.0 / 255, 170.0 / 255, 170.0 / 255)({
          Engine.painter.drawLine(
            List(Complex(0, -2), Complex(0, 2)).map(z => Complex(z.re * xUnit + (realAxis._2 + realAxis._1) / 2,
              z.im * yUnit + (imaginaryAxis._1 + imaginaryAxis._2) / 2)), lineWidth = 2
          )
        })
      }

      Engine.painter.withColor(170.0 / 255, 170.0 / 255, 170.0 / 255)({
        Engine.painter.drawEllipse(0, xUnit, yUnit, segments = 100, fill = false)
      })
    })
  }

  private var dragging: Option[DrawAreaDragging] = None

  setScript(ScriptKind.OnClick)((f: Frame, x: Double, y: Double, button: Int) => if (button == 0) {
    val drawArea = f.asInstanceOf[DrawArea]

    ScriptObject.firesEvent[Plot, Option[Plot], Unit](Plot.OnPlotGotFocus)(drawArea.plot, Plot.focusedPlot)

    dragging = Some(new DrawAreaDragging(x, y, drawArea))
  })

  setScript(ScriptKind.OnMouseReleased)((f: Frame, x: Double, y: Double, button: Int) => if (button == 0) {
    val drawArea = f.asInstanceOf[DrawArea]
    if (drawArea.dragging.isDefined) {
      Action.addAction(() => {
        drawArea.plot.removeChild(drawArea.dragging.get.line)
      })

      if (math.max(math.abs(x - drawArea.dragging.get.startX), math.abs(y - drawArea.dragging.get.startY)) < 5)
        Action.cancelAction()

      drawArea.dragging = None
    }
  })

  setScript(ScriptKind.OnMouseMoved)((f: Frame, x: Double, y: Double, _: Double, _: Double, _: Int) =>
  {
    val drawArea = f.asInstanceOf[DrawArea]
    if (drawArea.dragging.isDefined && (drawArea.dragging.get.startPoint - Complex(x,y)).modulus2 > 5) {
      val info = drawArea.dragging.get

      val currentPoint = DrawArea.fromCoordsToComplex(
        drawArea, x - drawArea.left - drawArea.width / 2, y - drawArea.bottom - drawArea.height / 2
      )

      info.mode match {
        case DrawAreaLine =>
          info.line.asInstanceOf[Segment].to = currentPoint
        case DrawAreaEllipse =>
          val e = info.line.asInstanceOf[Ellipse]
          e.center = (info.startPoint + currentPoint) / 2
          val direction = currentPoint - info.startPoint
          e.xRadius = math.abs(direction.re / 2)
          e.yRadius = math.abs(direction.im / 2)
        case DrawAreaCircle =>
          val c = info.line.asInstanceOf[Ellipse]
          val direction = currentPoint - info.startPoint
          val radius = math.min(math.abs(direction.re / 2), math.abs(direction.im / 2))
          c.xRadius = radius
          c.yRadius = radius

          c.center = info.startPoint
          (direction.re > 0, direction.im > 0) match {
            case (true, true) => c.center += radius + Complex.i * radius
            case (true, false) => c.center += radius - Complex.i * radius
            case (false, true) => c.center += -radius + Complex.i * radius
            case (false, false) => c.center += - radius - Complex.i * radius
          }
      }
      info.line.draw()
    }
  })
}


object DrawArea {
  def fromCoordsToComplex(drawArea: DrawArea, x: Double, y: Double): Complex = {
    val (realAxis, imaginaryAxis) = drawArea.plot.axes

    val xUnit = drawArea.width / (realAxis._2 - realAxis._1)

    val yUnit = drawArea.height / (imaginaryAxis._2 - imaginaryAxis._1)

    Complex(x / xUnit, y / yUnit)
  }
}


class DrawAreaDragging(val startX: Double, val startY: Double, val drawArea: DrawArea) {
  val mode: DrawAreaMode = drawArea.drawMode
  val startPoint: Complex = DrawArea.fromCoordsToComplex(
    drawArea, startX - drawArea.left - drawArea.width / 2, startY - drawArea.bottom - drawArea.height / 2)
  val line: Line = mode match {
    case DrawAreaLine => new Segment(drawArea.plot, drawArea.drawColors, startPoint, startPoint)
    case _ => new Ellipse(drawArea.plot, drawArea.drawColors, startPoint, 1/1000, 1/1000)
  }
}
