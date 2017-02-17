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


/**
 * MenuBand is a wide rectangle at the top of the screen that contains menu buttons.
 */
object MenuBand extends Frame("Menu", Some(UIParent)) {
  setPoint(TopLeft)
  setPoint(TopRight)
  setHeight(30)

  private val bg = createTexture()
  bg.setAllPoints()
  bg.setVertexColor(200.0 / 255, 200.0 / 255, 200.0 / 255)

  private val newPlotBut: Button = new Button("", Some(this))
  newPlotBut.setPoint(Left, this, Left, 10)
  newPlotBut.setSize(100, 25)

  newPlotBut.setText("New Plot")
  newPlotBut.setTextColor(0,0,0)

  newPlotBut.setNormalTexture()
  newPlotBut.normalTexture.get.setAllPoints()
  newPlotBut.normalTexture.get.setVertexColor(170.0 / 255, 170.0 / 255, 170.0 / 255)

  newPlotBut.setPushedTexture(Button.makeSimplePushedTexture(newPlotBut))

  newPlotBut.setHighlightTexture()
  newPlotBut.highlightTexture.get.setAllPoints()
  newPlotBut.highlightTexture.get.setVertexColor(89.0 / 255, 157.0 / 255, 220.0 / 255, 0.5)

  newPlotBut.setScript(ScriptKind.OnMouseReleased)((_: Frame, x: Double, y: Double, button: Int) => {
    if (button == 0 && newPlotBut.isMouseOver(x, y)) {
      new Plot()
    }
  })


}
