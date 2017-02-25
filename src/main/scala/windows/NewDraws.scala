package windows

import complex.Complex
import gui._
import plot._

/**
 * This window will allow the user to add lines or shapes by specifying characteristics numerically
 */
object NewDraws extends Window("New Draw") {

  actualWindow.setSize(500, 400)

  private def addBackground(frame: Frame): Texture = {
    val bg = frame.createTexture()
    bg.setAllPoints()
    bg.setVertexColor(0.7, 0.7, 0.7)
    bg
  }

  private object colorPicker extends Frame(actualWindow) {
    setPoint(BottomRight, actualWindow, BottomRight, -20, 20)
    setPoint(TopLeft, actualWindow, BottomLeft, 20, 75)

    private val fs: FontString = createFontString()
    fs.setPoint(TopLeft)
    fs.setSize(100,30)
    fs.setJustifyH(JustifyLeft)
    fs.setText("Select color{FF0000:R}color{00FF00:G}color{0000FF:B} color (0-255 range):")

    private var r: Int = 0
    private var g: Int = 0
    private var b: Int = 0

    private def setColor(): Unit = {
      DrawingOptions.setColor(r, g, b)
    }

    private val red = new IntEditBox(this, (_: Option[Int]) => {})
    red.setPoint(TopLeft, fs, BottomLeft)
    red.setSize(50, 25)
    red.setTextInsets(5,-5,0,0)
    addBackground(red)
    red.setMaxLetterNumber(3)

    private val green = new IntEditBox(this, (_: Option[Int]) => {})
    green.setPoint(Left, red, Right, 10)
    green.setSize(50, 25)
    green.setTextInsets(5,-5,0,0)
    addBackground(green)
    green.setMaxLetterNumber(3)

    private val blue = new IntEditBox(this, (_: Option[Int]) => {})
    blue.setPoint(Left, green, Right, 10)
    blue.setSize(50, 25)
    blue.setTextInsets(5,-5,0,0)
    addBackground(blue)
    blue.setMaxLetterNumber(3)

    private def onLoseFocus(self: Focusable, other: Option[Focusable]): Unit = {
      r = red.parser.get
      g = green.parser.get
      b = blue.parser.get
      setColor()
    }
    red.setScript(ScriptKind.OnLoseFocus)(onLoseFocus)
    green.setScript(ScriptKind.OnLoseFocus)(onLoseFocus)
    blue.setScript(ScriptKind.OnLoseFocus)(onLoseFocus)
  }

  private abstract class SubWindows extends Frame(actualWindow) {
    def addDraw(): Unit

    private val addButton: Button = new Button(this)
    addButton.setSize(100, 25)
    addButton.setPoint(BottomRight, 0, 30)
    addButton.setNormalTexture(addBackground(addButton))
    addButton.normalTexture.get.setVertexColor(0.3, 0.3, 0.3)
    addButton.setPushedTexture(Button.makeSimplePushedTexture(addButton))
    addButton.setScript(ScriptKind.OnMouseReleased)((_: Frame, _: Double, _: Double, button: Int) => {
      if (button == 0) {
        addDraw()
      }
    })
    addButton.setText("Draw")

    setPoint(TopLeft, actualWindow, TopLeft, 20, -40)
    setPoint(BottomRight, colorPicker, TopRight, 0, 10)

    protected val title: FontString = createFontString()
    title.setSize(120, 30)
    title.setPoint(TopLeft)
    title.setJustifyH(JustifyLeft)
  }

  private object NewLine extends SubWindows {
    private var from: Complex = Complex(0,0)
    private var to: Complex = Complex(1,1)

    title.setText("Line properties:")

    private val edge1FS: FontString = createFontString()
    edge1FS.setPoint(TopLeft, title, BottomLeft)
    edge1FS.setSize(120, 20)
    edge1FS.setJustifyH(JustifyLeft)
    edge1FS.setText("Edge 1:")

    private val edge1EditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        from = z
      case None =>
        ErrorWindow.show("Illegal Complex number syntax.\nEdge 1 set to color{FF0000:0}.")
        from = Complex(0, 0)
    })
    edge1EditBox.clearFocus()
    edge1EditBox.setSize(300, 25)
    edge1EditBox.setPoint(TopLeft, edge1FS, BottomLeft, 0, -5)
    addBackground(edge1EditBox)
    edge1EditBox.setTextInsets(5,-5,0,0)


    private val edge2FS: FontString = createFontString()
    edge2FS.setPoint(TopLeft, edge1EditBox, BottomLeft, 0, -10)
    edge2FS.setSize(120, 20)
    edge2FS.setJustifyH(JustifyLeft)
    edge2FS.setText("Edge 2:")

    private val edge2EditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        to = z
      case None =>
        ErrorWindow.show("Illegal Complex number syntax.\nEdge 2 set to color{FF0000:0}.")
        to = Complex(0, 0)
    })
    edge2EditBox.clearFocus()
    edge2EditBox.setSize(300, 25)
    edge2EditBox.setPoint(TopLeft, edge2FS, BottomLeft, 0, -5)
    addBackground(edge2EditBox)
    edge2EditBox.setTextInsets(5,-5,0,0)

    private val syntaxFS: FontString = createFontString()
    syntaxFS.setPoint(BottomRight)
    syntaxFS.setSize(200, 20)
    syntaxFS.setJustifyH(JustifyRight)
    syntaxFS.setFontSize(15)
    syntaxFS.setText("(Complex number syntax: a+bi or a-bi)")

    def addDraw(): Unit = {
      if ((from - to).modulus > 0.05) {
        val p = Plot.focusedPlot match {
          case Some(plot) => plot
          case None => new Plot("")
        }
        Action.addAction(
          new NewDrawableInPlotAction(new Segment(p, p.drawArea.drawColors, from, to))
        )
        this.parent.get.parent.get.hide()
      } else {
        ErrorWindow.show("Segment length must be bigger than 0.05.")
      }
    }
  }

  private object NewEllipse extends SubWindows {
    private var ellipseCenter: Complex = Complex(0,0)
    private var xRadius: Double = 1
    private var yRadius: Double = 1

    title.setText("Ellipse properties:")

    private val centerFS: FontString = createFontString()
    centerFS.setPoint(TopLeft, title, BottomLeft)
    centerFS.setSize(120, 20)
    centerFS.setJustifyH(JustifyLeft)
    centerFS.setText("Center of ellipse:")

    private val centerEditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        ellipseCenter = z
      case None =>
        ErrorWindow.show("Illegal Complex number syntax.\nCenter set to color{FF0000:0}.")
        ellipseCenter = Complex(0, 0)
    })
    centerEditBox.clearFocus()
    centerEditBox.setSize(300, 25)
    centerEditBox.setPoint(TopLeft, centerFS, BottomLeft, 0, -5)
    addBackground(centerEditBox)
    centerEditBox.setTextInsets(5,-5,0,0)


    private val xRadiusFS: FontString = createFontString()
    xRadiusFS.setPoint(TopLeft, centerEditBox, BottomLeft, 0, -10)
    xRadiusFS.setSize(120, 20)
    xRadiusFS.setJustifyH(JustifyLeft)
    xRadiusFS.setText("X Radius:")

    private val xRadiusEditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        if (z.im != 0 || z.re <= 0) {
          ErrorWindow.show("Radius must be a positive real number.\nRadius set to color{FF0000:1}.")
          xRadius = 1
        } else {
          xRadius = z.re
        }
      case None =>
        ErrorWindow.show("Illegal Double number syntax.\nRadius set to color{FF0000:1}.")
        xRadius = 1
    })
    xRadiusEditBox.clearFocus()
    xRadiusEditBox.setSize(300, 25)
    xRadiusEditBox.setPoint(TopLeft, xRadiusFS, BottomLeft, 0, -5)
    addBackground(xRadiusEditBox)
    xRadiusEditBox.setTextInsets(5,-5,0,0)

    private val yRadiusFS: FontString = createFontString()
    yRadiusFS.setPoint(TopLeft, xRadiusEditBox, BottomLeft, 0, -10)
    yRadiusFS.setSize(120, 20)
    yRadiusFS.setJustifyH(JustifyLeft)
    yRadiusFS.setText("Y Radius:")

    private val yRadiusEditBox = new ComplexNumberEditBox(this, {
        case Some(z) =>
          if (z.im != 0 || z.re <= 0) {
            ErrorWindow.show("Radius must be a positive real number.\nRadius set to color{FF0000:1}.")
            yRadius = 1
          } else {
            yRadius = z.re
          }
        case None =>
          ErrorWindow.show("Illegal Double number syntax.\nRadius set to color{FF0000:1}.")
          yRadius = 1
      })
    yRadiusEditBox.clearFocus()
    yRadiusEditBox.setSize(300, 25)
    yRadiusEditBox.setPoint(TopLeft, yRadiusFS, BottomLeft, 0, -5)
    addBackground(yRadiusEditBox)
    yRadiusEditBox.setTextInsets(5,-5,0,0)

    private val syntaxFS: FontString = createFontString()
    syntaxFS.setPoint(BottomRight)
    syntaxFS.setSize(200, 20)
    syntaxFS.setJustifyH(JustifyRight)
    syntaxFS.setFontSize(15)
    syntaxFS.setText("(Complex number syntax: a+bi or a-bi)")

    def addDraw(): Unit = {
      if (xRadius > 0.05 && yRadius > 0.05) {
        val p = Plot.focusedPlot match {
          case Some(plot) => plot
          case None => new Plot("")
        }
        Action.addAction(
          new NewDrawableInPlotAction(new Ellipse(p, p.drawArea.drawColors, ellipseCenter, xRadius, yRadius))
        )
        this.parent.get.parent.get.hide()
      } else {
        ErrorWindow.show("Radii must be bigger than 0.05.")
      }
    }
  }

  private object NewRectangle extends SubWindows {
    private var topLeft: Complex = Complex(0,0)
    private var bottomRight: Complex = Complex(1,1)

    title.setText("Rectangle properties:")

    private val topLeftFS: FontString = createFontString()
    topLeftFS.setPoint(TopLeft, title, BottomLeft)
    topLeftFS.setSize(120, 20)
    topLeftFS.setJustifyH(JustifyLeft)
    topLeftFS.setText("Top Left:")

    private val topLeftEditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        topLeft = z
      case None =>
        ErrorWindow.show("Illegal Complex number syntax.\nTop Left set to color{FF0000:0}.")
        topLeft = Complex(0, 0)
    })
    topLeftEditBox.clearFocus()
    topLeftEditBox.setSize(300, 25)
    topLeftEditBox.setPoint(TopLeft, topLeftFS, BottomLeft, 0, -5)
    addBackground(topLeftEditBox)
    topLeftEditBox.setTextInsets(5,-5,0,0)


    private val bottomRightFS: FontString = createFontString()
    bottomRightFS.setPoint(TopLeft, topLeftEditBox, BottomLeft, 0, -10)
    bottomRightFS.setSize(120, 20)
    bottomRightFS.setJustifyH(JustifyLeft)
    bottomRightFS.setText("Bottom Right:")

    private val bottomRightEditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        bottomRight = z
      case None =>
        ErrorWindow.show("Illegal Complex number syntax.\nBottom Right set to color{FF0000:0}.")
        bottomRight = Complex(0, 0)
    })
    bottomRightEditBox.clearFocus()
    bottomRightEditBox.setSize(300, 25)
    bottomRightEditBox.setPoint(TopLeft, bottomRightFS, BottomLeft, 0, -5)
    addBackground(bottomRightEditBox)
    bottomRightEditBox.setTextInsets(5,-5,0,0)

    private val syntaxFS: FontString = createFontString()
    syntaxFS.setPoint(BottomRight)
    syntaxFS.setSize(200, 20)
    syntaxFS.setJustifyH(JustifyRight)
    syntaxFS.setFontSize(15)
    syntaxFS.setText("(Complex number syntax: a+bi or a-bi)")

    def addDraw(): Unit = {
      if ((topLeft - bottomRight).modulus < 0.05) {
        ErrorWindow.show("Rectangle sides must be bigger than 0.05.")
      } else if (bottomRight.re < topLeft.re || bottomRight.im > topLeft.im) {
        ErrorWindow.show("Bottom Right is not south east from Top Left.")
      } else {
        val p = Plot.focusedPlot match {
          case Some(plot) => plot
          case None => new Plot("")
        }
        Action.addAction(
          new NewDrawableInPlotAction(new RectangleShape(
            p, p.drawArea.drawColors, topLeft, bottomRight.re - topLeft.re, topLeft.im - bottomRight.im)
          )
        )
        this.parent.get.parent.get.hide()
      }
    }
  }

  private object NewFilledEllipse extends SubWindows {
    private var ellipseCenter: Complex = Complex(0,0)
    private var xRadius: Double = 1
    private var yRadius: Double = 1

    title.setText("Ellipse properties:")

    private val centerFS: FontString = createFontString()
    centerFS.setPoint(TopLeft, title, BottomLeft)
    centerFS.setSize(120, 20)
    centerFS.setJustifyH(JustifyLeft)
    centerFS.setText("Center of ellipse:")

    private val centerEditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        ellipseCenter = z
      case None =>
        ErrorWindow.show("Illegal Complex number syntax.\nCenter set to color{FF0000:0}.")
        ellipseCenter = Complex(0, 0)
    })
    centerEditBox.clearFocus()
    centerEditBox.setSize(300, 25)
    centerEditBox.setPoint(TopLeft, centerFS, BottomLeft, 0, -5)
    addBackground(centerEditBox)
    centerEditBox.setTextInsets(5,-5,0,0)


    private val xRadiusFS: FontString = createFontString()
    xRadiusFS.setPoint(TopLeft, centerEditBox, BottomLeft, 0, -10)
    xRadiusFS.setSize(120, 20)
    xRadiusFS.setJustifyH(JustifyLeft)
    xRadiusFS.setText("X Radius:")

    private val xRadiusEditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        if (z.im != 0 || z.re <= 0) {
          ErrorWindow.show("Radius must be a positive real number.\nRadius set to color{FF0000:1}.")
          xRadius = 1
        } else {
          xRadius = z.re
        }
      case None =>
        ErrorWindow.show("Illegal Double number syntax.\nRadius set to color{FF0000:1}.")
        xRadius = 1
    })
    xRadiusEditBox.clearFocus()
    xRadiusEditBox.setSize(300, 25)
    xRadiusEditBox.setPoint(TopLeft, xRadiusFS, BottomLeft, 0, -5)
    addBackground(xRadiusEditBox)
    xRadiusEditBox.setTextInsets(5,-5,0,0)

    private val yRadiusFS: FontString = createFontString()
    yRadiusFS.setPoint(TopLeft, xRadiusEditBox, BottomLeft, 0, -10)
    yRadiusFS.setSize(120, 20)
    yRadiusFS.setJustifyH(JustifyLeft)
    yRadiusFS.setText("Y Radius:")

    private val yRadiusEditBox = new ComplexNumberEditBox(this, {
      case Some(z) =>
        if (z.im != 0 || z.re <= 0) {
          ErrorWindow.show("Radius must be a positive real number.\nRadius set to color{FF0000:1}.")
          yRadius = 1
        } else {
          yRadius = z.re
        }
      case None =>
        ErrorWindow.show("Illegal Double number syntax.\nRadius set to color{FF0000:1}.")
        yRadius = 1
    })
    yRadiusEditBox.clearFocus()
    yRadiusEditBox.setSize(300, 25)
    yRadiusEditBox.setPoint(TopLeft, yRadiusFS, BottomLeft, 0, -5)
    addBackground(yRadiusEditBox)
    yRadiusEditBox.setTextInsets(5,-5,0,0)

    private val syntaxFS: FontString = createFontString()
    syntaxFS.setPoint(BottomRight)
    syntaxFS.setSize(200, 20)
    syntaxFS.setJustifyH(JustifyRight)
    syntaxFS.setFontSize(15)
    syntaxFS.setText("(Complex number syntax: a+bi or a-bi)")

    def addDraw(): Unit = {
      if (xRadius > 0.05 && yRadius > 0.05) {
        val p = Plot.focusedPlot match {
          case Some(plot) => plot
          case None => new Plot("")
        }
        Action.addAction(
          new NewDrawableInPlotAction(new EllipseShape(p, p.drawArea.drawColors, ellipseCenter, xRadius, yRadius))
        )
        this.parent.get.parent.get.hide()
      } else {
        ErrorWindow.show("Radii must be bigger than 0.05.")
      }
    }
  }

  private val lineButton = new Button(this)
  lineButton.setPoint(TopLeft, actualWindow, BottomLeft)
  lineButton.setSize(30)
  lineButton.setNormalTexture()
  lineButton.normalTexture.get.setTexture(Some(DrawingOptions.lineButton.normalTexture.get.texture.get))
  lineButton.normalTexture.get.setAllPoints()
  lineButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0) {
      NewLine.show()
      NewEllipse.hide()
      NewRectangle.hide()
      NewFilledEllipse.hide()
    }
  })


  private val ellipseButton = new Button(this)
  ellipseButton.setPoint(Left, lineButton, Right)
  ellipseButton.setSize(30)
  ellipseButton.setNormalTexture()
  ellipseButton.normalTexture.get.setTexture(Some(DrawingOptions.ellipseButton.normalTexture.get.texture.get))
  ellipseButton.normalTexture.get.setAllPoints()
  ellipseButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0) {
      NewLine.hide()
      NewEllipse.show()
      NewRectangle.hide()
      NewFilledEllipse.hide()
    }
  })

  private val rectangleButton = new Button(this)
  rectangleButton.setPoint(Left, ellipseButton, Right)
  rectangleButton.setSize(30)
  rectangleButton.setNormalTexture()
  rectangleButton.normalTexture.get.setTexture(Some(DrawingOptions.rectangleButton.normalTexture.get.texture.get))
  rectangleButton.normalTexture.get.setAllPoints()
  rectangleButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0) {
      NewLine.hide()
      NewEllipse.hide()
      NewRectangle.show()
      NewFilledEllipse.hide()
    }
  })

  private val filledEllipseButton = new Button(this)
  filledEllipseButton.setPoint(Left, rectangleButton, Right)
  filledEllipseButton.setSize(30)
  filledEllipseButton.setNormalTexture()
  filledEllipseButton.normalTexture.get.setTexture(
    Some(DrawingOptions.filledEllipseButton.normalTexture.get.texture.get))
  filledEllipseButton.normalTexture.get.setAllPoints()
  filledEllipseButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0) {
      NewLine.hide()
      NewEllipse.hide()
      NewRectangle.hide()
      NewFilledEllipse.show()
    }
  })
}
