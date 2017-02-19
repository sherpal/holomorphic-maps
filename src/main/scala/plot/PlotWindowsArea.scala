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


object PlotWindowsArea extends Frame("PlotWindowsArea", Some(UIParent)) {
  setPoint(TopLeft, MenuBand, BottomLeft)
  setPoint(TopRight, MenuBand, BottomRight, -300)
  setPoint(BottomLeft, UIParent, BottomLeft)
  setFrameStrata(Low)

  private val border = createTexture()
  border.setAllPoints()
  border.setVertexColor(1, 20.0 / 255, 147.0 / 255)
  border.setMode(LineMode)



  private var _drawMode: DrawAreaMode = DrawAreaLine
  def drawMode: DrawAreaMode = _drawMode
  def setDrawMode(mode: DrawAreaMode): Unit = {
    _drawMode = mode
    Plot.allPlots.foreach(_.drawArea.drawMode = mode)
  }

  private var _drawColor: (Double, Double, Double) = (0,0,0)
  def drawColor: (Double, Double, Double) = _drawColor
  def setDrawColor(r: Double, g: Double, b: Double): Unit = {
    _drawColor = (r,g,b)
    Plot.allPlots.foreach(_.drawArea.drawColors = _drawColor)
  }

  var mapFunction: HolomorphicMap = HolomorphicMap("Identity", "", z => z)


}




abstract sealed class DrawAreaMode
case object DrawAreaLine extends DrawAreaMode
case object DrawAreaCircle extends DrawAreaMode
case object DrawAreaEllipse extends DrawAreaMode
case object DrawAreaRectangle extends DrawAreaMode
case object DrawAreaFillEllipse extends DrawAreaMode
case object DrawAreaFillCircle extends DrawAreaMode
