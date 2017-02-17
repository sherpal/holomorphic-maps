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


class HolomorphicMap(val name: String, val call: String,
                     val f: (Complex) => Complex,
                     val cut: Option[String] = None,
                     val singularities: List[Complex] = List()) {

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: HolomorphicMap => that.name == this.name || that.call == this.call
    case _ => false
  }

  override def hashCode(): Int = (name + call).hashCode
}


object HolomorphicMap {
  def apply(name: String, call: String,
            f: (Complex) => Complex,
            cut: Option[String] = None,
            singularities: List[Complex] = List()): HolomorphicMap = new HolomorphicMap(
    name, call, f, cut, singularities)
}
