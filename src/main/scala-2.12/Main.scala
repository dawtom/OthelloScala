import java.awt.{BasicStroke, Color, Graphics2D}

object Main {
  def main(args: Array[String]) {
    val board = new Board
    val ui = new UI(board)
    ui.visible = true
  }
}