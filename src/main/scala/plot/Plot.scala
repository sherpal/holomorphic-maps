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

import gui._

import scala.collection.mutable


class Plot(name: String = "") extends Frame {

  val id: Int = Plot.giveId()

  // constructor
  private val behind = new Frame("", Some(PlotWindowsArea))
  behind.setPoint(Center)
  behind.setSize(Plot.defaultPlotSize._1, Plot.defaultPlotSize._2 + 30)

  this.setParent(behind)
  this.setPoint(TopLeft, behind, TopLeft, 0, -30)
  this.setSize(Plot.defaultPlotSize._1, Plot.defaultPlotSize._2)
  setTopLevel(enable = true)
  putOnTop()

  this.registerEvent(Plot.OnPlotGotFocus)((f: Frame, focused: Plot, _: Option[Plot]) => {
    Plot.focusedPlot = Some(focused)
    if (f == focused) {
      f.asInstanceOf[Plot].setDesignColor(Plot.withFocusColor)
    } else {
      f.asInstanceOf[Plot].setDesignColor(Plot.withoutFocusColor)
    }
  })

  private val bg = createTexture()
  bg.setAllPoints()

  private val header = new Frame("", Some(this))
  header.setPoint(BottomRight, this, TopRight)
  header.setPoint(BottomLeft, this, TopLeft)
  header.setHeight(30)
  private val headerTex = header.createTexture()
  headerTex.setAllPoints()

  private val border = header.createTexture()
  border.setDrawLayer(Overlay)
  border.setPoint(TopLeft, header, TopLeft)
  border.setPoint(BottomRight, this, BottomRight)
  border.setVertexColor(0,0,0)
  border.setMode(LineMode)

  behind.registerForDrag(header)
  behind.setClampedToFrame(PlotWindowsArea)

  private val titleFS: FontString = header.createFontString()
  titleFS.setAllPoints()
  titleFS.setJustifyV()
  titleFS.setJustifyH()

  private def focusScript = (_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0)
      ScriptObject.firesEvent[Plot, Option[Plot], Unit](Plot.OnPlotGotFocus)(this, Plot.focusedPlot)
  }
  header.setScript(ScriptKind.OnClick)(focusScript)
  this.setScript(ScriptKind.OnClick)(focusScript)

  private val closeButton = new Button("", Some(header))
  closeButton.setPoint(Center, header, TopRight, -15, -15)
  closeButton.setRadius(10)
  closeButton.setNormalTexture()
  closeButton.normalTexture.get.setAllPoints()
  closeButton.normalTexture.get.setVertexColor(200.0 / 255, 200.0 / 255, 200.0 / 255)
  closeButton.setText("X")
  closeButton.setTextColor(0,0,0)
  closeButton.setPushedTexture(Button.makeSimplePushedTexture(closeButton))
  closeButton.setScript(ScriptKind.OnMouseReleased)((_: Frame, _: Double, _: Double, but: Int) => {
    if (but == 0) this.close()
  })

  /// End of constructor


  val lineChildren: mutable.Set[Line] = mutable.Set()
  val shapeChildren: mutable.Set[Shape] = mutable.Set()

  def childrenLines: Set[Line] = lineChildren.toSet

  def childrenShapes: Set[Shape] = shapeChildren.toSet

  def removeAllChildren(): Unit = {
    clear()
    lineChildren.clear()
    shapeChildren.clear()
  }

  def removeChild(child: DrawableInPlot): Unit = child match {
    case line: Line =>
      line.hide()
      lineChildren -= line
    case shape: Shape =>
      shape.hide()
      shapeChildren -= shape
  }

  def addChild(child: DrawableInPlot): Unit = child match {
    case line: Line =>
      lineChildren += line
    case shape: Shape =>
      shapeChildren += shape
  }

  def clearLine(line: Line): Unit =
    line.hide()

  def clearShape(shape: Shape): Unit =
    shape.hide()

  def clear(): Unit = {
    lineChildren.foreach(_.hide())
    shapeChildren.foreach(_.hide())
  }


  private var realAxisMin: Double = -2
  private var realAxisMax: Double = 2
  private var imaginaryAxisMin: Double = -2
  private var imaginaryAxisMax: Double = 2
  def axes: ((Double, Double), (Double, Double)) = ((realAxisMin,realAxisMax), (imaginaryAxisMin, imaginaryAxisMax))
  def setAxes(tag: AxisTag, min: Double, max: Double): Unit = {
    tag match {
      case RealTag =>
        realAxisMin = min
        realAxisMax = max
      case ImaginaryTag =>
        imaginaryAxisMin = min
        imaginaryAxisMax = max
    }

    drawArea.drawAxes()
    childrenLines.filter(_.isShown).foreach(_.draw())
  }

  def setTitle(title: String): Unit =
    titleFS.setText(if (title == "") "Plot " + id else title)
  def title: String = titleFS.text


  def setDesignColor(colors: Array[Double]): Unit = {
    bg.setVertexColor(colors(0), colors(1), colors(2), 0.5)
    headerTex.setVertexColor(colors(3), colors(4), colors(5))
    border.setVertexColor(colors(3), colors(4), colors(5))
  }

  val drawArea: DrawArea = new DrawArea(this)


  /**
   * Maps every line and shape through the map holomorphicMap.
   * If the poles of the map are referenced correctly, it will not map a Triangle that contains a pole by the map.
   */
  def map(holomorphicMap: HolomorphicMap): Plot = {
    val newPlot = new Plot(holomorphicMap.name)
    val newPlotBehind = newPlot.behind

    newPlotBehind.clearAllPoints()
    newPlotBehind.setPoint(TopLeft, this.behind, TopRight)
    if (newPlotBehind.right > PlotWindowsArea.right) {
      newPlotBehind.clearAllPoints()
      newPlotBehind.setPoint(TopRight, this.behind, TopLeft)
    }

    newPlot.setAxes(RealTag, this.axes._1._1, this.axes._1._2)
    newPlot.setAxes(ImaginaryTag, this.axes._2._1, this.axes._2._2)

    lineChildren.filter(_.isShown).foreach(line => {
      new Line(newPlot, line.colors, line.stepPoints.map(holomorphicMap.f(_)), isCycle = line.cycle)
    })

    shapeChildren.filter(_.isShown).foreach(shape => {
      val newTriangles = shape.triangles
        .filter((t: Triangle) => holomorphicMap.singularities.forall(!t.contains(_)))
        .map((t: Triangle) => {
        val newVertices = t.triangleVertices.tail.map(holomorphicMap.f(_))
        (newVertices.head, newVertices.tail.head, newVertices.tail.tail.head)
      })
      new RawShape(newPlot, shape.colors, newTriangles)
    })

    newPlot
  }

  def map(): Plot = map(PlotWindowsArea.mapFunction)


  def close(): Unit =
    destroy()

  private def destroy(): Unit = {
    removeAllChildren()
    Plot.freedIds += id
    hide()
    Plot.allPlots -= this
    if (Plot.focusedPlot.isDefined && Plot.focusedPlot.get == this)
      Plot.focusedPlot = None
    // TODO: probably things to add
  }


  // calling initialisation methods
  drawArea.drawAxes()
  focusScript(this, 0, 0, 0)

  Plot.allPlots += this

  setTitle(name)
}



object Plot {
  // some constants
  private val defaultPlotSize: (Double, Double) = (300,300)
  private val withFocusColor = Array[Double](89.0 / 255, 157.0 / 255, 220.0 / 255, 0, 0, 1)
  private val withoutFocusColor = Array[Double](1, 144.0 / 255, 0, 1, 0, 0)

  val OnPlotGotFocus: ScriptKind {type Handler = (Frame, Plot, Option[Plot]) => Unit} =
    ScriptKind.makeEvent[(Frame, Plot, Option[Plot]) => Unit]

  var focusedPlot: Option[Plot] = None

  private val freedIds: mutable.Set[Int] = mutable.Set()
  private var lastID: Int = 0
  private def giveId(): Int = {
    if (freedIds.isEmpty) {
      lastID += 1
      lastID
    } else {
      val id = freedIds.min
      freedIds -= id
      id
    }
  }


  val allPlots: mutable.Set[Plot] = mutable.Set()
}



abstract sealed class AxisTag
case object RealTag extends AxisTag
case object ImaginaryTag extends AxisTag
