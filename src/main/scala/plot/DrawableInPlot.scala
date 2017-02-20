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

import gui.Texture
import webglgraphics.Canvas2D

/**
 * Unites methods of Line and Shape.
 */
trait DrawableInPlot {
  val plot: Plot

  /**
   * We attach to the Line a texture child to the [[DrawArea]] of its [[Plot]].
   * This texture will be used to actually draw the line on the [[DrawArea]].
   */
  val attachedCanvas: Texture = plot.drawArea.createTexture()
  attachedCanvas.setAllPoints()
  attachedCanvas.setTexture(new Canvas2D())
  attachedCanvas.canvas.get.setSize(plot.drawArea.width.toInt, plot.drawArea.height.toInt)


  private var _isShown: Boolean = true

  def hide(): Unit = {
    _isShown = false
    attachedCanvas.hide()
  }

  def isShown: Boolean = _isShown

  def show(): Unit = {
    _isShown = true
    attachedCanvas.show()
    draw()
  }

  def draw(): Unit

  plot.addChild(this)
}
