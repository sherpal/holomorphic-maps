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

/**
 * A Triangle will be a small piece of a [[Shape]] to draw on the Complex plane.
 */
class Triangle(val shape: Shape, var colors: (Double, Double, Double), v1: Complex, v2: Complex, v3: Complex) {

  val triangleVertices = List(v1, v2, v3, v1)

  def draw(): Unit = {
    val plot = shape.plot
    val (realAxis, imaginaryAxis) = plot.axes
    val xUnit = plot.drawArea.width / (realAxis._2 - realAxis._1)
    val yUnit = plot.drawArea.height / (imaginaryAxis._2 - imaginaryAxis._1)

    val vertices = triangleVertices
      .map(z => Complex(z.re * xUnit + (realAxis._2 + realAxis._1) / 2,
        z.im * yUnit + (imaginaryAxis._1 + imaginaryAxis._2) / 2))

    Engine.painter.withCanvases(shape.attachedCanvas.canvas.get)({
      Engine.painter.withColor(colors._1, colors._2, colors._3, 0.9)({
        Engine.painter.drawVertices(vertices, lineWidth = 0)
      })
    })
  }

  /**
   * Subdivide the triangle in the four triangles formed by the vertices of this triangles, together with the
   * centers of the edges of this triangle.
   *
   * This method is recursive but not tail recursive. This should not be a problem as the pixel area is divided by 4
   * each iteration.
   *
   * @param threshold pixel area limit under which triangle should not divide.
   * @return          the four triangles as a List of Triangles.
   */
  def subDivide(threshold: Double): List[Triangle] = {
    if (shouldIDivide(threshold)) {
      val middles = triangleVertices.tail.zip(triangleVertices).map({ case (v1: Complex, v2: Complex) => (v1 + v2) / 2 })

      List(
        (middles.head, middles.tail.head, middles.last),
        (triangleVertices.head, middles.head, middles.last),
        (triangleVertices.tail.head, middles.tail.head, middles.head),
        (triangleVertices.tail.tail.head, middles.tail.tail.head, middles.tail.head)
      )
        .map({ case (v1: Complex, v2: Complex, v3: Complex) => new Triangle(shape, colors, v1, v2, v3) })
        .flatMap((t: Triangle) => t.subDivide(threshold))
    } else {
      List(this)
    }
  }

  /**
   * Returns the area of the triangle, in pixels squared.
   */
  private def pixelArea: Double = {
    val plot = shape.plot
    val (realAxis, imaginaryAxis) = plot.axes
    val xUnit = plot.drawArea.width / (realAxis._2 - realAxis._1)
    val yUnit = plot.drawArea.height / (imaginaryAxis._2 - imaginaryAxis._1)

    val vertices = triangleVertices
      .map(z => Complex(z.re * xUnit + (realAxis._2 + realAxis._1) / 2,
        z.im * yUnit + (imaginaryAxis._1 + imaginaryAxis._2) / 2))

    math.abs((vertices.tail.head - vertices.head).crossProduct(vertices.tail.tail.head - vertices.tail.head)) / 2
  }

  private val boundingBox: (Double, Double, Double, Double) =
    (
      triangleVertices.map(_.im).max, // top
      triangleVertices.map(_.re).min, // left
      triangleVertices.map(_.im).min, // bottom
      triangleVertices.map(_.re).max  // right
    )

  /**
   * Returns whether the pixel area of the Triangle is bigger than the wished threshold, and the triangles bounding
   * box intersect with the visible part of the Complex plane.
   */
  def shouldIDivide(threshold: Double): Boolean =
    threshold < pixelArea && {
      val plot = shape.plot
      val (realAxis, imaginaryAxis) = plot.axes
      math.max(realAxis._1, boundingBox._2) <= math.min(realAxis._2, boundingBox._4) &&
      math.max(imaginaryAxis._1, boundingBox._3) <= math.min(imaginaryAxis._2, boundingBox._1)
    }

  def contains(z: Complex): Boolean = contains(z.re, z.im)

  def contains(x: Double, y: Double): Boolean = {
    val coef1 = (v3.im - v1.im) * (x - v1.re) - (v3.re - v1.re) * (y - v1.im)
    val coef2 = (v2.re - v1.re) * (y - v1.im) - (v2.im - v1.im) * (x - v1.re)

    val det = (v2 - v1) crossProduct (v3 - v1)

    if (det > 0) {
      coef1 >= 0 && coef2 >= 0 && coef1 + coef2 <= det
    } else {
      coef1 <= 0 && coef2 <= 0 && coef1 + coef2 >= det
    }
  }
}
