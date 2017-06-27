import java.awt.{Color, Dimension}

import scala.swing.{BoxPanel, Button, Component, Dialog, Label, MainFrame, Orientation, Swing}

/**
  * Created by Dawid Tomasiewicz on 11.06.17.
  */
class UI(val board: Board) extends MainFrame {
  private def restrictHeight(s: Component) {
    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
  }


  title = "Othello"

  val canvas = new Canvas(board)
  val newGameButton = Button("New Game") { newGame() }
  val turnLabel = new Label("Player 1's turn")
  turnLabel.foreground = Color.BLUE
  val quitButton = Button("Quit") { sys.exit(0) }
  val buttonLine = new BoxPanel(Orientation.Horizontal) {
    contents += newGameButton
    contents += Swing.HGlue
    contents += turnLabel
    contents += Swing.HGlue
    contents += quitButton
  }

  restrictHeight(buttonLine)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += canvas
    contents += Swing.VStrut(10)
    contents += buttonLine
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  listenTo(canvas)
  reactions += {
    case OthelloEvent(x, y) =>
      board.play(x, y)
      updateLabelAndBoard()
  }

  def updateLabelAndBoard() {
    if (!board.hasMove(board.currentPlayer) && board.howManyAllTiles() < 64) {
      Dialog.showMessage(message = "You have no move")
      board.setPlayer(3 - board.currentPlayer)
    }
    turnLabel.text = "Player %d's turn".format(board.currentPlayer)
    canvas.repaint()
  }

  def newGame() {
    board.restart()
    updateLabelAndBoard()
  }
}

