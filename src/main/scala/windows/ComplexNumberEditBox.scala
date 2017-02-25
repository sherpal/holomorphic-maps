package windows

import complex.Complex
import gui._

/**
 * EditBox whose goal is to contain Complex numbers.
 */
class ComplexNumberEditBox(parent: Frame, enterCallback: (Option[Complex]) => Unit)
  extends NumericEditBox[Complex](parent, enterCallback) {

  protected val legalDigits = " .0123456789+-i"

  // Baby parser until we do something more involved.
  def parser: Option[Complex] = {
    val rawNoSpace = text.replaceAll(" ", "")
    val noSpace = if (rawNoSpace == "") "0" else rawNoSpace
    if (noSpace.contains('+')) {
      val vec = noSpace.split('+')
      val z = try Some(Complex(vec.head.toDouble, vec.last.take(vec.last.length - 1).toDouble)) catch {
        case _: Throwable => None
      }
      z
    } else if (noSpace.tail.contains("-")) { // taking tail to eliminate -a and -bi cases
      if (noSpace.head == '-') {
        val vec = noSpace.tail.split("-")
        val z = try Some(Complex(-vec.head.toDouble, -vec.last.take(vec.last.length - 1).toDouble)) catch {
          case _: Throwable => None
        }
        z
      } else {
        val vec = noSpace.split("-")
        val z = try Some(Complex(vec.head.toDouble, -vec.last.take(vec.last.length - 1).toDouble)) catch {
          case _: Throwable => None
        }
        z
      }
    } else if (noSpace.contains("i")) {
      val z = try Some(Complex(0, noSpace.take(text.length - 1).toDouble)) catch {
        case _: Throwable => None
      }
      z
    } else {
      val z = try Some(Complex(noSpace.toDouble, 0)) catch {
        case _: Throwable => None
      }
      z
    }
  }
}
