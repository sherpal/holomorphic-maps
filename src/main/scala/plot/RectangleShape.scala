package plot

import complex.Complex

/**
 * Rectangle shape to draw on plots.
 */
class RectangleShape(val plot: Plot, var colors: (Double, Double, Double),
                     var topLeft: Complex, var width: Double, var height: Double) extends Shape {

  def drawTriangles: List[Triangle] = List[Triangle](
    new Triangle(this, colors, topLeft, topLeft + width, topLeft - height * Complex.i),
    new Triangle(this, colors, topLeft + width, topLeft - height * Complex.i, topLeft + width - height * Complex.i)
  )

  draw()
}
