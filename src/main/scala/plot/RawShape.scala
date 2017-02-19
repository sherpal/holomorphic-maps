package plot

import complex.Complex

/**
 * RawShapes are created by mapping rectangular of disk shapes via functions.
 */
class RawShape(val plot: Plot, var colors: (Double, Double, Double),
               val rawTriangles: List[(Complex, Complex, Complex)]) extends Shape {

  private val initialTriangles = rawTriangles.map({
    case (v1: Complex, v2: Complex, v3: Complex) => new Triangle(this, colors, v1, v2, v3)
  })

  def drawTriangles: List[Triangle] = initialTriangles

  draw()
}
