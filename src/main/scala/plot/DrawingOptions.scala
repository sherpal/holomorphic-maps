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
import gameengine.Engine
import gui._
import sharednodejsapis.NodeProcess
import webglgraphics.Canvas2D

/**
 * This Frame will contain all the button need to set the draw mode and draw color.
 */
object DrawingOptions extends Frame("DrawingOptions", Some(UIParent)) {
  setPoint(TopLeft, PlotWindowsArea, TopRight)
  setPoint(BottomRight, UIParent, TopRight, 0, -300)

  private val border = createTexture()
  border.setAllPoints()
  border.setVertexColor(1, 20.0 / 255, 147.0 / 255)
  border.setMode(LineMode)

  private val defaultButSize: Int = 30
  private val xOffset: Int = 30
  private val yOffset: Int = 30

  val lineButton: Button = new Button(this)
  lineButton.setPoint(TopLeft, this, TopLeft, xOffset, -yOffset)
  lineButton.setSize(defaultButSize)
  private val lineButtonBG = lineButton.createTexture()
  lineButtonBG.setAllPoints()
  lineButtonBG.setVertexColor(0,0,0)
  lineButtonBG.setMode(LineMode)

  lineButton.setNormalTexture()
  lineButton.normalTexture.get.setAllPoints()
  lineButton.normalTexture.get.setTexture(new Canvas2D())
  lineButton.normalTexture.get.canvas.get.setSize(lineButton.width.toInt, lineButton.height.toInt)
  Engine.painter.withCanvases(lineButton.normalTexture.get.canvas.get)({
    Engine.painter.withColor(200.0 / 255, 200.0 / 255, 200.0 / 255)({
      Engine.painter.drawRectangle(
        -lineButton.width / 2 + lineButton.height / 2 * Complex.i, lineButton.width, lineButton.height
      )
    })

    Engine.painter.withColor(0, 0, 0)({
      Engine.painter.drawLine(List(
        -8 * lineButton.width / 10 + 8 * lineButton.height / 10 * Complex.i,
        lineButton.width * 3 / 10 - lineButton.height * 2 / 10 * Complex.i
      ))
    })
  })

  lineButton.setPushedTexture(Button.makeSimplePushedTexture(lineButton))

  lineButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0)
      PlotWindowsArea.setDrawMode(DrawAreaLine)
  })

  lineButton.setScript(ScriptKind.OnEnter)((self: Region, _: Region) => {
    ButtonTooltip.clearLines()
    ButtonTooltip.addLine("Set draw lines mode (Ctrl+L)", 0, 0, 0, wrap = true, JustifyCenter)
    ButtonTooltip.appearIn()
    ButtonTooltip.setOwner(self.asInstanceOf[Frame])
  })

  lineButton.setScript(ScriptKind.OnLeave)((_: Region, _: Region) => {
    ButtonTooltip.fadeOut()
  })

  val ellipseButton = new Button(this)
  ellipseButton.setPoint(TopLeft, lineButton, TopRight, 10)
  ellipseButton.setSize(defaultButSize)
  private val ellipseButtonBG = ellipseButton.createTexture()
  ellipseButtonBG.setAllPoints()
  ellipseButtonBG.setVertexColor(0,0,0)
  ellipseButtonBG.setMode(LineMode)

  ellipseButton.setNormalTexture()
  ellipseButton.normalTexture.get.setAllPoints()
  ellipseButton.normalTexture.get.setTexture(new Canvas2D())
  ellipseButton.normalTexture.get.canvas.get.setSize(ellipseButton.width.toInt, ellipseButton.height.toInt)
  Engine.painter.withCanvases(ellipseButton.normalTexture.get.canvas.get)({
    Engine.painter.withColor(200.0 / 255, 200.0 / 255, 200.0 / 255)({
      Engine.painter.drawRectangle(
        -ellipseButton.width / 2 + ellipseButton.height / 2 * Complex.i, ellipseButton.width, ellipseButton.height
      )
    })

    Engine.painter.withColor(0, 0, 0)({
      Engine.painter.drawEllipse(0, ellipseButton.width * 8 / 20, ellipseButton.height * 5 / 20, lineWidth = 2)
    })
  })

  ellipseButton.setPushedTexture(Button.makeSimplePushedTexture(ellipseButton))

  ellipseButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0)
      PlotWindowsArea.setDrawMode(DrawAreaEllipse)
  })

  ellipseButton.setScript(ScriptKind.OnEnter)((self: Region, _: Region) => {
    ButtonTooltip.clearLines()
    ButtonTooltip.addLine("Set draw ellipses mode (Ctrl+E)", 0, 0, 0, wrap = true, JustifyCenter)
    ButtonTooltip.appearIn()
    ButtonTooltip.setOwner(self.asInstanceOf[Frame])
  })

  ellipseButton.setScript(ScriptKind.OnLeave)((_: Region, _: Region) => {
    ButtonTooltip.fadeOut()
  })

  val circleButton = new Button(this)
  circleButton.setPoint(TopLeft, ellipseButton, TopRight, 10)
  circleButton.setSize(defaultButSize)
  private val circleButtonBG = circleButton.createTexture()
  circleButtonBG.setAllPoints()
  circleButtonBG.setVertexColor(0,0,0)
  circleButtonBG.setMode(LineMode)

  circleButton.setNormalTexture()
  circleButton.normalTexture.get.setAllPoints()
  circleButton.normalTexture.get.setTexture(new Canvas2D())
  circleButton.normalTexture.get.canvas.get.setSize(circleButton.width.toInt, circleButton.height.toInt)
  Engine.painter.withCanvases(circleButton.normalTexture.get.canvas.get)({
    Engine.painter.withColor(200.0 / 255, 200.0 / 255, 200.0 / 255)({
      Engine.painter.drawRectangle(
        -circleButton.width / 2 + circleButton.height / 2 * Complex.i, circleButton.width, circleButton.height
      )
    })

    Engine.painter.withColor(0, 0, 0)({
      Engine.painter.drawEllipse(0, circleButton.width * 8 / 20, circleButton.height * 8 / 20, lineWidth = 2)
    })
  })

  circleButton.setPushedTexture(Button.makeSimplePushedTexture(circleButton))

  circleButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0)
      PlotWindowsArea.setDrawMode(DrawAreaCircle)
  })

  circleButton.setScript(ScriptKind.OnEnter)((self: Region, _: Region) => {
    ButtonTooltip.clearLines()
    ButtonTooltip.addLine("Set draw circles mode (Ctrl+C)", 0, 0, 0, wrap = true, JustifyCenter)
    ButtonTooltip.appearIn()
    ButtonTooltip.setOwner(self.asInstanceOf[Frame])
  })

  circleButton.setScript(ScriptKind.OnLeave)((_: Region, _: Region) => {
    ButtonTooltip.fadeOut()
  })

  val rectangleButton = new Button(this)
  rectangleButton.setPoint(TopLeft, circleButton, TopRight, 10)
  rectangleButton.setSize(defaultButSize)
  private val rectangleButtonBG = rectangleButton.createTexture()
  rectangleButtonBG.setAllPoints()
  rectangleButtonBG.setVertexColor(0,0,0)
  rectangleButtonBG.setMode(LineMode)

  rectangleButton.setNormalTexture()
  rectangleButton.normalTexture.get.setAllPoints()
  rectangleButton.normalTexture.get.setTexture(new Canvas2D())
  rectangleButton.normalTexture.get.canvas.get.setSize(rectangleButton.width.toInt, rectangleButton.height.toInt)
  Engine.painter.withCanvases(rectangleButton.normalTexture.get.canvas.get)({
    Engine.painter.withColor(200.0 / 255, 200.0 / 255, 200.0 / 255)({
      Engine.painter.drawRectangle(
        -circleButton.width / 2 + circleButton.height / 2 * Complex.i, circleButton.width, circleButton.height
      )
    })

    Engine.painter.withColor(0.1, 0.1, 0.1)({
      Engine.painter.drawRectangle(
        Complex(-rectangleButton.width * 2 / 5, rectangleButton.height / 4),
        circleButton.width * 4 / 5, circleButton.height / 2, lineWidth = 0)
    })
  })

  rectangleButton.setPushedTexture(Button.makeSimplePushedTexture(rectangleButton))

  rectangleButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0)
      PlotWindowsArea.setDrawMode(DrawAreaRectangle)
  })

  rectangleButton.setScript(ScriptKind.OnEnter)((self: Region, _: Region) => {
    ButtonTooltip.clearLines()
    ButtonTooltip.addLine("Set draw rectangle mode", 0, 0, 0, wrap = true, JustifyCenter)
    ButtonTooltip.appearIn()
    ButtonTooltip.setOwner(self.asInstanceOf[Frame])
  })

  rectangleButton.setScript(ScriptKind.OnLeave)((_: Region, _: Region) => {
    ButtonTooltip.fadeOut()
  })


  val filledEllipseButton = new Button(this)
  filledEllipseButton.setPoint(TopLeft, rectangleButton, TopRight, 10)
  filledEllipseButton.setSize(defaultButSize)
  private val filledEllipseButtonBG = filledEllipseButton.createTexture()
  filledEllipseButtonBG.setAllPoints()
  filledEllipseButtonBG.setVertexColor(0,0,0)
  filledEllipseButtonBG.setMode(LineMode)

  filledEllipseButton.setNormalTexture()
  filledEllipseButton.normalTexture.get.setAllPoints()
  filledEllipseButton.normalTexture.get.setTexture(new Canvas2D())
  filledEllipseButton.normalTexture.get.canvas.get.setSize(
    filledEllipseButton.width.toInt, filledEllipseButton.height.toInt)
  Engine.painter.withCanvases(filledEllipseButton.normalTexture.get.canvas.get)({
    Engine.painter.withColor(200.0 / 255, 200.0 / 255, 200.0 / 255)({
      Engine.painter.drawRectangle(
        -filledEllipseButton.width / 2 + filledEllipseButton.height / 2 * Complex.i,
        filledEllipseButton.width, filledEllipseButton.height
      )
    })

    Engine.painter.withColor(0.1, 0.1, 0.1)({
      Engine.painter.drawEllipse(0, filledEllipseButton.width * 8 / 20, filledEllipseButton.height * 5 / 20,
        lineWidth = 0)
    })
  })

  filledEllipseButton.setPushedTexture(Button.makeSimplePushedTexture(filledEllipseButton))

  filledEllipseButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0)
      PlotWindowsArea.setDrawMode(DrawAreaFillEllipse)
  })

  filledEllipseButton.setScript(ScriptKind.OnEnter)((self: Region, _: Region) => {
    ButtonTooltip.clearLines()
    ButtonTooltip.addLine("Set draw filled ellipse", 0, 0, 0, wrap = true, JustifyCenter)
    ButtonTooltip.appearIn()
    ButtonTooltip.setOwner(self.asInstanceOf[Frame])
  })

  filledEllipseButton.setScript(ScriptKind.OnLeave)((_: Region, _: Region) => {
    ButtonTooltip.fadeOut()
  })


  private val diskButton = new Button(this)
  diskButton.setPoint(TopLeft, filledEllipseButton, TopRight, 10)
  diskButton.setSize(defaultButSize)
  private val diskButtonBG = diskButton.createTexture()
  diskButtonBG.setAllPoints()
  diskButtonBG.setVertexColor(0,0,0)
  diskButtonBG.setMode(LineMode)

  diskButton.setNormalTexture()
  diskButton.normalTexture.get.setAllPoints()
  diskButton.normalTexture.get.setTexture(new Canvas2D())
  diskButton.normalTexture.get.canvas.get.setSize(diskButton.width.toInt, diskButton.height.toInt)
  Engine.painter.withCanvases(diskButton.normalTexture.get.canvas.get)({
    Engine.painter.withColor(200.0 / 255, 200.0 / 255, 200.0 / 255)({
      Engine.painter.drawRectangle(
        -diskButton.width / 2 + diskButton.height / 2 * Complex.i, diskButton.width, diskButton.height
      )
    })

    Engine.painter.withColor(0.1, 0.1, 0.1)({
      Engine.painter.drawEllipse(0, diskButton.width * 8 / 20, diskButton.height * 8 / 20, lineWidth = 0)
    })
  })

  diskButton.setPushedTexture(Button.makeSimplePushedTexture(diskButton))

  diskButton.setScript(ScriptKind.OnClick)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0)
      PlotWindowsArea.setDrawMode(DrawAreaFillCircle)
  })

  diskButton.setScript(ScriptKind.OnEnter)((self: Region, _: Region) => {
    ButtonTooltip.clearLines()
    ButtonTooltip.addLine("Set draw disk mode", 0, 0, 0, wrap = true, JustifyCenter)
    ButtonTooltip.appearIn()
    ButtonTooltip.setOwner(self.asInstanceOf[Frame])
  })

  diskButton.setScript(ScriptKind.OnLeave)((_: Region, _: Region) => {
    ButtonTooltip.fadeOut()
  })

  //TODO: cancel button

  private val slidersHeight = 15
  private val slidersSpace = 10

  private val chooseColorFS = createFontString()
  chooseColorFS.setPoint(TopLeft, lineButton, BottomLeft, 0, -defaultButSize)
  chooseColorFS.setSize(this.width - defaultButSize, 20)
  //chooseColorFS.setFontSize(15)
  chooseColorFS.setFont("Quicksand", 20)
  chooseColorFS.setText("Line colors settings:")
  chooseColorFS.setTextColor(0,0,0)
  chooseColorFS.setJustifyH(JustifyLeft)

  private val chosenColor = new Frame("", Some(this))
  chosenColor.setSize(defaultButSize, 3 * slidersHeight + 2 * slidersSpace)
  chosenColor.setPoint(TopLeft, chooseColorFS, BottomLeft, 0, -slidersSpace)

  private val chosenColorBorder = chosenColor.createTexture(layer = Overlay)
  chosenColorBorder.setAllPoints()
  chosenColorBorder.setVertexColor(0,0,0)
  chosenColorBorder.setMode(LineMode)

  private val chosenColorBG = chosenColor.createTexture()
  chosenColorBG.setAllPoints()
  chosenColorBG.setVertexColor(
    PlotWindowsArea.drawColor._1,
    PlotWindowsArea.drawColor._2,
    PlotWindowsArea.drawColor._3
  )

  private val red = new Slider(this)
  red.setPoint(TopLeft, chosenColor, TopRight, 10)
  red.setPoint(TopRight, this, TopRight, -10, chosenColor.top - this.top)
  red.setHeight(slidersHeight)
  private val redBG = red.createTexture()
  redBG.setAllPoints()
  redBG.setVertexColor(200.0/255,200.0/255,200.0/255)

  red.setMinMaxValues(0, 255)
  red.setStep(Some(1))
  red.setThumbLength(15)
  red.thumbTexture.setVertexColor(1,0,0)
  red.setValue((255 * PlotWindowsArea.drawColor._1).toInt)

  red.setScript(ScriptKind.OnValueChanged)((_: ValueBar, value: Double, _: Double) => {
    val (_, g, b, _) = chosenColorBG.vertexColor
    chosenColorBG.setVertexColor(value / 255, g, b)
    PlotWindowsArea.setDrawColor(value / 255, g, b)
  })

  private val green = new Slider(this)
  green.setPoint(TopLeft, red, BottomLeft, 0, -slidersSpace)
  green.setPoint(TopRight, red, BottomRight, 0, -slidersSpace)
  green.setHeight(slidersHeight)
  private val greenBG = green.createTexture()
  greenBG.setAllPoints()
  greenBG.setVertexColor(200.0/255,200.0/255,200.0/255)

  green.setMinMaxValues(0, 255)
  green.setStep(Some(1))
  green.setThumbLength(15)
  green.thumbTexture.setVertexColor(0,1,0)
  green.setValue((255 * PlotWindowsArea.drawColor._2).toInt)

  green.setScript(ScriptKind.OnValueChanged)((_: ValueBar, value: Double, _: Double) => {
    val (r, _, b, _) = chosenColorBG.vertexColor
    chosenColorBG.setVertexColor(r, value / 255, b)
    PlotWindowsArea.setDrawColor(r, value / 255, b)
  })

  private val blue = new Slider(this)
  blue.setPoint(TopLeft, green, BottomLeft, 0, -slidersSpace)
  blue.setPoint(TopRight, green, BottomRight, 0, -slidersSpace)
  blue.setHeight(slidersHeight)
  private val blueBG = blue.createTexture()
  blueBG.setAllPoints()
  blueBG.setVertexColor(200.0/255,200.0/255,200.0/255)

  blue.setMinMaxValues(0, 255)
  blue.setStep(Some(1))
  blue.setThumbLength(15)
  blue.thumbTexture.setVertexColor(0,0)
  blue.setValue((255 * PlotWindowsArea.drawColor._3).toInt)

  blue.setScript(ScriptKind.OnValueChanged)((_: ValueBar, value: Double, _: Double) => {
    val (r, g, _, _) = chosenColorBG.vertexColor
    chosenColorBG.setVertexColor(r, g, value / 255)
    PlotWindowsArea.setDrawColor(r, g, value / 255)
  })

  def setColor(r: Int, g: Int, b: Int): Unit = {
    red.setValue(if (r < 0) 0 else if (r > 255) 255 else r)
    green.setValue(if (g < 0) 0 else if (g > 255) 255 else g)
    blue.setValue(if (b < 0) 0 else if (b > 255) 255 else b)
  }

  private val newDrawsButton = new Button(this)
  newDrawsButton.setSize(defaultButSize)
  newDrawsButton.setPoint(TopLeft, chosenColor, BottomLeft, 0, -20)
  newDrawsButton.setNormalTexture("./pics/numerical_drawing.png")
  newDrawsButton.normalTexture.get.setAllPoints()
  newDrawsButton.setScript(ScriptKind.OnEnter)((_: Frame, _: Frame) => {
    newDrawsButton.setPushedTexture(Button.makeSimplePushedTexture(newDrawsButton))
    newDrawsButton.removeScript(ScriptKind.OnEnter)
  })
  newDrawsButton.setScript(ScriptKind.OnMouseReleased)((_: Frame, _: Double, _: Double, button: Int) => {
    if (button == 0) {
      windows.NewDraws.show()
    }
  })
  newDrawsButton.setScript(ScriptKind.OnEnter)((self: Frame, _: Frame) => {
    ButtonTooltip.clearLines()
    ButtonTooltip.addLine("Draw with numeric values", 0, 0, 0, wrap = true, JustifyCenter)
    ButtonTooltip.appearIn()
    ButtonTooltip.setOwner(self)
  })
  newDrawsButton.setScript(ScriptKind.OnLeave)((_: Frame, _: Frame) => {
    ButtonTooltip.fadeOut()
  })




  DebugWindow.addData((_: Double) => {
    val processInfo = NodeProcess.memoryUsage()
    "Memory usage: " + (processInfo.heapUsed / 1024 / 1024) + " mb"
  })
  DebugWindow.hide()

  setScript(ScriptKind.OnKeyReleased)((_: Frame, key: String) => {
    key match {
      case "l" if Engine.isDown("Control") =>
        val (x, y) = lineButton.center
        lineButton.click(x,y,0)
      case "e" if Engine.isDown("Control") =>
        val (x, y) = ellipseButton.center
        ellipseButton.click(x,y,0)
      case "c" if Engine.isDown("Control") =>
        val (x, y) = circleButton.center
        circleButton.click(x,y,0)
      case "z" if Engine.isDown("Control") =>
        Action.cancelAction()
      case "ArrowUp" =>
        FunctionOptions.changeFocusedButton(-1)
      case "ArrowDown" =>
        FunctionOptions.changeFocusedButton(1)
      case "F1" =>
        if (DebugWindow.isVisible)
          DebugWindow.hide()
        else
          DebugWindow.show()
      case _ =>
    }
  })

}
