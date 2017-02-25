package windows

import gui._

/**
 * Window class.
 */
abstract class Window(windowTitle: String) extends Frame(UIParent) {
  setAllPoints()
  setFrameStrata(Fullscreen)

  protected val actualWindow: Frame = new Frame(this)
  actualWindow.setPoint(Center)
  actualWindow.setSize(300)
  actualWindow.registerForDrag(actualWindow)
  actualWindow.setClampedToFrame(this)

  private val bg: Texture = actualWindow.createTexture()
  bg.setAllPoints()
  bg.setVertexColor(89.0 / 255, 157.0 / 255, 220.0 / 255)

  private val title: FontString = actualWindow.createFontString()
  title.setSize(actualWindow.width, 30)
  title.setPoint(Top)
  title.setText(windowTitle)
  title.setTextColor(0,0,0)

  def setTitle(t: String): Unit =
    title.setText(t)

  private object CloseButton extends Button(this) {
    setSize(75, 30)
    setText("Close")

    private val bg = createTexture()
    bg.setAllPoints()
    bg.setVertexColor(0.5, 0.5, 0.5)

    setScript(ScriptKind.OnMouseReleased)((_: Frame, _: Double, _: Double, button: Int) => {
      if (button == 0) {
        parent.get.hide()
      }
    })
  }

  CloseButton.setPoint(TopRight, actualWindow, BottomRight)

  actualWindow.setFrameLevel(CloseButton.frameLevel + 1)

  private val border: Texture = actualWindow.createTexture("", Overlay)
  border.lineWidth = 5
  border.setMode(LineMode)
  border.setAllPoints()
  border.setVertexColor(0,0,0)

  private var lastColorChanged: Double = 0
  private var changed: Int = 1

  private def shake(): Unit = {
    border.setVertexColor(1, 69.0 / 255, 0)
    lastColorChanged = 0
    changed = 1
    setScript(ScriptKind.OnUpdate)((_: Frame, dt: Double) => {
      lastColorChanged += dt
      if (lastColorChanged > 100) {
        lastColorChanged -= 100
        if (changed % 2 == 0) {
          border.setVertexColor(1, 69.0 / 255, 0)
        } else {
          border.setVertexColor(0,0,0)
        }
        changed += 1
        if (changed > 5) {
          border.setVertexColor(0,0,0)
          setScript(ScriptKind.OnUpdate)((_: Frame, _: Double) => {})
        }
      }
    })
  }

  setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, _: Int) => shake())
}
