package windows

import gui._

/**
 * EditBox for receiving Ints.
 */
class IntEditBox(frame: Frame, enterCallback: (Option[Int]) => Unit)
  extends NumericEditBox[Int](frame, enterCallback) {

  protected val legalDigits = "0123456789"

  def parser: Option[Int] = Some(if (text == "") 0 else text.toInt) // nothing bad can happen here

}
