package windows


import gui._


/**
 * Small window which purpose is to show error message to user when relevant.
 */
object ErrorWindow extends Window("Error") {

  actualWindow.setSize(300, 150)

  private val errorMessage: FontString = actualWindow.createFontString()
  errorMessage.setPoint(TopLeft, actualWindow, TopLeft, 10, -30)
  errorMessage.setPoint(BottomRight, actualWindow, BottomRight, -10)
  errorMessage.setMultiLine(enable = true)
  errorMessage.setFontSize(15)

  def show(message: String): Unit = {
    errorMessage.setText(message)
    putOnTop()

    show()
  }

  hide()
}
