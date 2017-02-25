package windows

import gui._

/**
 * Template and common functions for NumericEditBox
 */
abstract class NumericEditBox[A](parent: Frame, enterCallback: (Option[A]) => Unit) extends EditBox(parent) {

  protected val legalDigits: String

  protected def isDigit(s: String): Boolean = legalDigits.exists(_.toString == s)

  setScript(ScriptKind.OnClick)((_: Frame, x: Double, y: Double, button: Int) => {
    if (button == 0) {
      setCursorPosition(findMouseCursor(x, y))
      setFocus()
    }
  })

  def parser: Option[A]

  setScript(ScriptKind.OnKeyPressed)((_: Frame, key: String, _: Boolean) => {
    key match {
      case "Enter" =>
        enterCallback(parser)
        clearFocus()
      case "Escape" =>
        clearFocus()
      case "Backspace" =>
        backspace()
      case "Delete" =>
        delChar()
      case "ArrowLeft" =>
        moveCursor(-1)
      case "ArrowRight" =>
        moveCursor(1)
      case d if isDigit(d) =>
        insert(d)
      case _ =>
    }
  })


}
