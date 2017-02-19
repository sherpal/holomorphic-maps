package plot

import gameengine.Engine


trait Shape extends DrawableInPlot {

  var colors: (Double, Double, Double)

  var threshold: Double = 10

  def drawTriangles: List[Triangle]

  def triangles: List[Triangle] = drawTriangles.flatMap(_.subDivide(threshold))

  def draw(): Unit = {
    Engine.painter.withCanvases(attachedCanvas.canvas.get)({
      Engine.painter.clear()
    })
    drawTriangles.foreach(_.draw())
  }
}
