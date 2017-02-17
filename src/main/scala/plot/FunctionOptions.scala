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



object FunctionOptions extends Frame("FunctionOptions", Some(UIParent)) {
  setPoint(TopLeft, DrawingOptions, BottomLeft)
  setPoint(BottomRight)

  private val border = createTexture(layer = Overlay)
  border.setAllPoints()
  border.setVertexColor(1, 20.0/255, 147.0 / 255)
  border.setMode(LineMode)

  private val header = createTexture()
  header.setSize(width, 30)
  header.setPoint(Top)
  header.setVertexColor(170.0/255,170.0/255,170.0/255)

  private val headerText = createFontString()
  headerText.setText("Chose function:")
  headerText.setPoint(TopLeft, header, TopLeft, 10)
  headerText.setPoint(BottomRight, header, BottomRight)
  headerText.setJustifyH(JustifyLeft)
  headerText.setVertexColor(0,0,0)


  var holomorphicMaps: List[HolomorphicMap] = List(
    HolomorphicMap("Identity", "id", z => z),
    HolomorphicMap("Square", "sqr", z => z * z),
    HolomorphicMap("Cube", "cube", z => z^3),
    HolomorphicMap("Square root", "sqrt", z => z^0.5, Some("(-Inf,0]")),
    HolomorphicMap("V(z-1)V(z+1)", "doublesqrt", z => (z-1)^0.5 * (z+1)^0.5, Some("[-1,1]")),
    HolomorphicMap("Exponential", "exp", z => Complex.exp(z)),
    HolomorphicMap("Logarithm", "log", z => Complex.log(z), Some("(-Inf,0]")),
    HolomorphicMap("Inverse 1/z", "inverse", z => 1 / z, singularities = List(0)),
    HolomorphicMap("Inverse 1/(z-1)", "inverseshifted", z => 1 / (z - 1), singularities = List(1)),
    HolomorphicMap("Möbius (z-1)/(z+1)", "simplemobius", z => (z - 1) / (z + 1), singularities = List(-1)),
    HolomorphicMap("sin", "sin", z => Complex.sin(z)),
    HolomorphicMap("cos", "cos", z => Complex.cos(z))
  )


  private val scrollFrame = new ScrollFrame("", Some(this))
  scrollFrame.setPoint(TopLeft, header, BottomLeft, 2, -2)
  scrollFrame.setPoint(BottomRight, this, BottomRight, -20, 2)


  private object scrollChild extends Frame("", Some(UIParent)) {
    setHeight(2)
    setWidth(scrollFrame.width)
    setPoint(Center)

    var buttons: List[FunctionButton] = List()
  }

  private class FunctionButton(val holomorphicMap: HolomorphicMap) extends Button("", Some(scrollChild)) {
    setWidth(scrollChild.width - 5)
    setHeight(30)
    scrollChild.setHeight(scrollChild.height + this.height)

    if (scrollChild.buttons.isEmpty) {
      setPoint(Top)
    } else {
      setPoint(Top, scrollChild.buttons.last, Bottom)
    }

    private val funcName = createFontString()
    funcName.setText(holomorphicMap.name)
    funcName.setJustifyH(JustifyLeft)
    funcName.setPoint(TopLeft, this, TopLeft, 10)
    funcName.setPoint(BottomRight, this, BottomRight)
    funcName.setTextColor(0,0,0)

    private val focusedTex = createTexture()
    focusedTex.hide()
    focusedTex.setAllPoints()
    focusedTex.setVertexColor(89.0/255,157.0/255,220.0/255,0.5)

    setScript(ScriptKind.OnClick)((self: Frame, _: Double, _: Double, button: Int) => {
      if (button == 0) {
        focusedButton match {
          case Some(but) =>
            but.focusedTex.hide()
            but.funcName.setTextColor(0,0,0)
          case _ =>
        }

        val thisBut = self.asInstanceOf[FunctionButton]

        focusedButton = Some(thisBut)
        thisBut.focusedTex.show()
        thisBut.funcName.setTextColor()

        PlotWindowsArea.mapFunction = this.holomorphicMap
      }
    })

    scrollFrame.updateScrollChildRect()
  }

  private var focusedButton: Option[FunctionButton] = None

  def changeFocusedButton(up: Int): Unit = {
    focusedButton match {
      case Some(but) =>
        val idx = scrollChild.buttons.indexOf(but)
        if (idx > -1) {
          val newIdx = idx + (if (up > 0) 1 else if (up < 0) -1 else 0)
          val newBut = scrollChild.buttons(
            if (newIdx < 0) scrollChild.buttons.length - 1
            else if (newIdx >= scrollChild.buttons.length) 0
            else newIdx
          )
          val (x, y) = newBut.center
          newBut.click(x, y, 0)
        }
      case None =>
        val (x, y) = scrollChild.buttons.head.center
        scrollChild.buttons.head.click(x, y, 0)
    }
  }

  scrollFrame.setScript(ScriptKind.OnWheelMoved)((self: Frame, _: Int, dy: Int) => {
    val (x, y) = Engine.mousePosition
    if (self.isMouseOver(x, y))
      changeFocusedButton(dy)
  })

  // we need to do this funny for loop because of the anchors of the buttons
  for (map <- holomorphicMaps) {
    scrollChild.buttons = scrollChild.buttons :+ new FunctionButton(map)
  }

  object mapButton extends Button("", Some(this)) {
    setPoint(Right, header, Right, -10)
    setSize(50, 20)
    setText("Map")
    setTextColor(0,0,0)

    setNormalTexture()
    normalTexture.get.setAllPoints()
    normalTexture.get.setVertexColor(200.0/255,200.0/255,200.0/255)

    setHighlightTexture()
    highlightTexture.get.setAllPoints()
    highlightTexture.get.setVertexColor(89.0/255,157.0/255,220.0/255,0.2)

    setScript(ScriptKind.OnMouseReleased)((self: Frame, x: Double, y: Double, button: Int) => {
      ButtonTooltip.hide()
      if (button == 0 && self.isMouseOver(x,y)) {
        Plot.focusedPlot match {
          case Some(plot) => plot.map()
          case _ =>
        }
      }
    })

    setScript(ScriptKind.OnEnter)((self: Frame, _: Frame) => {
      ButtonTooltip.clearLines()
      ButtonTooltip.addLine("Map each drawn line by the selected function", 0, 0, 0, wrap = true, JustifyCenter)
      ButtonTooltip.setOwner(self)
      ButtonTooltip.appearIn()
    })

    setScript(ScriptKind.OnLeave)((_: Frame, _: Frame) => {
      ButtonTooltip.fadeOut()
    })

  }

  mapButton



  scrollFrame.setScrollChild(scrollChild)
}
