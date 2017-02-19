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
