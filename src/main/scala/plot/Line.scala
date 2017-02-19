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


class Line(val plot: Plot, var colors: (Double, Double, Double), points: Vector[Complex], isCycle: Boolean = false)
extends DrawableInPlot {


  var stepPoints: Vector[Complex] = points

  /** Returns whether the points of the line are close enough to those of a straight line linking the extremities. */
  def isNearSegment: Boolean = {
    stepPoints.isEmpty || stepPoints.tail.isEmpty || stepPoints
      .zip(Line.segment(stepPoints.head, stepPoints.last, stepPoints.length))
      .map({case (z1, z2) => (z1 - z2).modulus2}).max < 0.1
  }

  /** A Line with cycle set to true will link the last point to the first one when drawing. */
  private var _cycle: Boolean = isCycle
  def cycle: Boolean = _cycle
  def cycle(enabled: Boolean = false): Unit =
    _cycle = enabled



  def draw(): Unit = {
    val (realAxis, imaginaryAxis) = plot.axes

    //val startAbscissa = realAxis._1
    val xUnit = plot.drawArea.width / (realAxis._2 - realAxis._1)

    //val startOrdinate = imaginaryAxis._1
    val yUnit = plot.drawArea.height / (imaginaryAxis._2 - imaginaryAxis._1)

    val vertices = (if (cycle) stepPoints.last +: stepPoints else stepPoints)
      .map(z => Complex(z.re * xUnit + (realAxis._2 + realAxis._1) / 2,
        z.im * yUnit + (imaginaryAxis._1 + imaginaryAxis._2) / 2))

    Engine.painter.withCanvases(attachedCanvas.canvas.get)({
      Engine.painter.clear()
      Engine.painter.withColor(colors._1, colors._2, colors._3)({
        Engine.painter.drawLine(vertices, lineWidth = 2)
      })
    })
  }

  draw()
}


object Line {

  /**
   * Returns a [[Vector]] of [[Complex]] numbers on a straight line from "from" to "to".
   *
   * @param from      starting point of the segment.
   * @param to        ending point of the segment.
   * @param numPoints the number of sample points to take (must be bigger than or equal to 2).
   * @return          a vector of all the complex points in the segment.
   */
  def segment(from: Complex, to: Complex, numPoints: Int = 100): Vector[Complex] = {
    val step = (to - from) / (numPoints - 1)
    (for (j <- 0 until numPoints) yield from + j * step).toVector
  }

  def ellipse(center: Complex, xRadius: Double, yRadius: Double, numPoints: Int = 100): Vector[Complex] = {
    (for (j <- 0 until numPoints) yield
      center + xRadius * math.cos(j * 2 * math.Pi / numPoints) + Complex.i * (
          yRadius * math.sin(j * 2 * math.Pi / numPoints)
        )).toVector
  }
}
