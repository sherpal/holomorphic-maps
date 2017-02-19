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


package renderer


import gameengine.{Engine, GameState}
import gui._
import plot._

import scala.scalajs.js.JSApp




object Renderer extends JSApp {
  def main(): Unit = {
    Engine.painter.setBackgroundColor(1,1,1)
    DrawingOptions
    FunctionOptions

    val state = new GameState(draw = () => {
      Frame.drawAllFrames()
    },
      keyPressed = (key: String, isRepeat: Boolean) => {
        Frame.keyPressed(key, isRepeat)
      },
      keyReleased = (key: String) => {
        Frame.keyReleased(key)
      },
      mousePressed = (x: Double, y: Double, button: Int) => {
        Frame.clickHandler(x, y, button)
      },
      mouseMoved = (x: Double, y: Double, dx: Double, dy: Double, button: Int) => {
        Frame.mouseMoved(x, y, dx, dy, button)
      },
      mouseReleased = (x: Double, y: Double, button: Int) => {
        Frame.mouseReleased(x, y, button)
      },
      mouseWheel = (dx: Int, dy: Int, _: Int) => {
        Frame.wheelMoved(dx, dy)
      },
      update = (dt: Double) => {
        Frame.updateHandler(dt)
      })

    Engine.changeGameState(state)
    Engine.startGameLoop()

  }
}
