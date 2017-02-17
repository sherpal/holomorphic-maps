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
 * An Ellipse is a particular type of [[Line]] whose primary goal is to draw ellipses, as the name suggests.
 */
class Ellipse(plot: Plot, colors: (Double, Double, Double),
              var center: Complex, var xRadius: Double, var yRadius: Double)
  extends Line(plot, colors, Line.ellipse(center, xRadius, yRadius)) {

  override def cycle: Boolean = true

  private var lastCenter = center
  private var lastXRadius = xRadius
  private var lastYRadius = yRadius

  override def draw(): Unit = {
    if (lastCenter != center || lastXRadius != xRadius || lastYRadius != yRadius) {
      lastCenter = center
      lastXRadius = xRadius
      lastYRadius = yRadius
      stepPoints = Line.ellipse(center, xRadius, yRadius, 1000)
    }
    super.draw()
  }
}

