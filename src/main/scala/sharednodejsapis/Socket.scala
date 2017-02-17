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


package sharednodejsapis

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, JSName, ScalaJSDefined}
import scala.scalajs.js.|

@ScalaJSDefined
trait ErrorEvent extends js.Object {
  val stack: String
}

@ScalaJSDefined
trait RInfo extends js.Object {
  val address: String
  val port: Int
  val family: String
}

@ScalaJSDefined
trait Address extends js.Object {
  val address: String
  val port: Int
}

@js.native
@JSName("dgram.Socket")
abstract class Socket extends EventEmitter {
  def address(): Address = js.native

  def bind(port: Int, address: String = js.native): Unit = js.native

  def close(): Unit = js.native

  def send(msg: String | Buffer | js.Array[Buffer], port: Int, address: String): Unit = js.native
  def send(msg: String | Buffer | js.Array[Buffer], port: Int, address: String, callback: js.Function): Unit = js.native
  def send(msg: String | Buffer | js.Array[Buffer], offset: Int, length: Int, port: Int, address: String): Unit = js.native
  def send(msg: String | Buffer | js.Array[Buffer], offset: Int, length: Int,
           port: Int, address: String, callback: js.Function): Unit = js.native
}

@js.native
@JSImport("dgram", JSImport.Namespace)
object DgramModule extends js.Object {
  def createSocket(t: String): Socket = js.native
}
