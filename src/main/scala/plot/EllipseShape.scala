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

/**
 * Disk shape to draw on plots.
 */
class EllipseShape(val plot: Plot, var colors: (Double, Double, Double),
                   var center: Complex, var xRadius: Double, var yRadius: Double) extends Shape {

  /**
   * Returns a list of Triangles that nearly cover the Ellipse.
   *
   * The algorithm is as follows:
   * - start with two triangles whose vertices have angle 0, pi/2 and pi, and pi, 3pi/2 and 2pi
   * - as long as the difference between two angles is too big for the angleThreshold, repeat the following:
   *   for each pair of adjacent angles a1 and a2, form a new [[Triangle]] whose vertices have angles a1, a2 and
   *   (a1 + a2) / 2 and a new List of two pairs of angles (a1, (a1 + a2) / 2) and ((a1 + a2) / 2, a2).
   * This procedure has the advantage that the closer you get to the boundary of the ellipse, the smaller the triangles
   * are.
   */
  def drawTriangles: List[Triangle] = {
    val angleThreshold = math.Pi / 40

    def drawTrianglesAcc(acc: List[Triangle], angles: List[(Double, Double)]): List[Triangle] = {
      if (math.abs(angles.head._1 - angles.head._2) < angleThreshold) {
        acc
      } else {
        val (newTriangles, newAngles): (List[Triangle], List[List[(Double, Double)]]) =
          (for (deltaAngle <- angles) yield {
          (
            new Triangle(
              this,
              center + Complex(xRadius * math.cos(deltaAngle._1), yRadius * math.sin(deltaAngle._1)),
              center + Complex(xRadius * math.cos(deltaAngle._2), yRadius * math.sin(deltaAngle._2)),
              center + Complex(xRadius * math.cos((deltaAngle._1 + deltaAngle._2) / 2),
                yRadius * math.sin((deltaAngle._1 + deltaAngle._2) / 2))
            ), List(
            (deltaAngle._1, (deltaAngle._1 + deltaAngle._2) / 2), ((deltaAngle._1 + deltaAngle._2) / 2, deltaAngle._2)
          )
          )
        }).unzip
        drawTrianglesAcc(newTriangles ++ acc, newAngles.flatten)
      }
    }

    drawTrianglesAcc(List[Triangle](), List((0, math.Pi), (math.Pi, 2 * math.Pi)))
  }

  draw()
}
