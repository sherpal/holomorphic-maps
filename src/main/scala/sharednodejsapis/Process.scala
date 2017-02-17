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
import scala.scalajs.js.annotation.{JSImport, ScalaJSDefined}


@js.native
@JSImport("process", JSImport.Namespace)
object NodeProcess extends EventEmitter {
  /**
   * Returns an object giving memory usage statistics about the current process.
   * Note that all statistics are reported in Kilobytes.
   */
  def getProcessMemoryInfo(): ProcessMemoryInfo = js.native

  /**
   * Returns an object giving memory usage statistics about the entire system.
   * Note that all statistics are reported in Kilobytes.
   */
  def getSystemMemoryInfo(): SystemMemoryInfo = js.native

  /**
   * returns an object describing the memory usage of the Node.js process measured in bytes.
   */
  def memoryUsage(): MemoryUsage = js.native
}

@ScalaJSDefined
trait ProcessMemoryInfo extends js.Object {
  /** The amount of memory currently pinned to actual physical RAM. */
  val workingSetSize: Int
  /** The maximum amount of memory that has ever been pinned to actual physical RAM. */
  val peakWorkingSetSize: Int
  /** The amount of memory not shared by other processes, such as JS heap or HTML content. */
  val privateBytes: Int
  /** The amount of memory shared between processes, typically memory consumed by the Electron code itself. */
  val sharedBytes: Int
}


@ScalaJSDefined
trait SystemMemoryInfo extends js.Object {
  /** Total amount of physical memory in Kilobytes available to the system. */
  val total: Int
  /** The total amount of memory not being used by applications or disk cache. */
  val free: Int
  /** The total amount of swap memory in Kilobytes available to the system. (Windows and Linux) */
  val swapTotal: Int
  /** The free amount of swap memory in Kilobytes available to the system. (Windows and Linux) */
  val swapFree: Int
}

@ScalaJSDefined
trait MemoryUsage extends js.Object {
  val rss: Int
  val heapUsed: Int
  val heapTotal: Int
  val heapExternal: Int
}