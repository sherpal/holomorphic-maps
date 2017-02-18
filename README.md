# holomorphic-maps
A small interface for viewing how the complex plane transforms itself under holomorphic maps.

It also serves as an example of use case of the [scalajs-ui](https://github.com/sherpal/scalajs-ui) library.


## How to make it work

holomorphic-maps uses [Electron](http://electron.atom.io/) as engine. You need to have Electron installed on your machine.
[Scala.js](https://www.scala-js.org/) creates javascript files for you to use with Electron. You will need the following files and folder that are compiled:
- 'holomorphic-maps-fastopt.js.map' file
- 'holomorphic-maps-fastopt.js' file
- 'holomorphic-maps-jsdeps.js' file
- 'classes' folder

Once compiled, you should copy-paste them in the 'electron' folder.

### Steps to follow (at least for Windows):
- download [Electron](http://electron.atom.io/)
- download the [scalajs-ui](https://github.com/sherpal/scalajs-ui) library
- compile it using `fastOptJS` or `fullOptJS`
- `publishLocal` it
- download this project
- compile it using `fastOptJS` or `fullOptJS`
- copy-paste the above mentioned files (or their fullOptJS pendant) to electron folder
- use the command `./electron/electron.exe path/to/holomorphic-maps/electron`

## How to use

The software allows you to draw lines, circles and ellipses on the Complex plane restricted to [-2,2] x [-2,2]. You can then chose a function from the list and map all the drawn lines by that function.

You can also press Enter (after the canvas got focus) to see an example of the DebugWindow from the UI.

## Holomorphic maps?

A holomorphic (or analytic) function is a function from a subset of the complex plane to itself, that is C-derivable.
A holomorphic map whose derivative does not vanish is a conformal map, meaning that the angles are preserved. That is, if two lines intersect with a certain angle, the mapped lines will form the same angle.

## (Hopefully) upcoming features

- Map regions like rectangles and disks
- Allow user to create their own functions
- Draw lines with equations
