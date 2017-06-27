import java.awt.{BasicStroke, Color, Dimension, Graphics2D}
import java.awt.geom.{Ellipse2D, Line2D}

import scala.swing.Component
// import scala.swing.Component.mouse
import scala.swing.event.MouseClicked

/**
  * Created by Dawid Tomasiewicz on 11.06.17.
  */
class Canvas(val board: Board) extends Component {
  preferredSize = new Dimension(640, 640)

  listenTo(mouse.clicks)
  reactions += {
    case MouseClicked(_, p, _, _, _) => mouseClick(p.x, p.y)
  }

  // returns squareSide, x0, y0, wid
  private def squareGeometry: (Int, Int, Int, Int) = {
    val d = size
    val squareSide = d.height min d.width
    val x0 = (d.width - squareSide)/2
    val y0 = (d.height - squareSide)/2

    (squareSide, x0, y0, squareSide/8)
  }

  private def mouseClick(x: Int, y: Int) {
    val (squareSide, x0, y0, wid) = squareGeometry
    if (x0 <= x && x < x0 + squareSide &&
      y0 <= y && y < y0 + squareSide) {
      val col = (x - x0) / wid
      val row = (y - y0) / wid
      publish(OthelloEvent(col, row))
    }
  }

  override def paintComponent(g : Graphics2D) {
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
      java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(Color.GREEN)
    val d = size
    g.fillRect(0,0, d.width, d.height)
    val (squareSide, x0, y0, wid) = squareGeometry
    g.setColor(Color.BLACK)
    // vertical lines
    for (x <- 1 to 8)
      g.draw(new Line2D.Double(x0 + x * wid, y0, x0 + x * wid, y0 + squareSide))
    // horizontal lines
    for (y <- 1 to 8)
      g.draw(new Line2D.Double(x0, y0 + y * wid, x0 + squareSide, y0 + y * wid))
    g.setStroke(new BasicStroke(3f))

    for (x <- 0 until 8) {
      for (y <- 0 until 8) {
        board(x, y) match {
          case 1 =>
            g.setColor(Color.BLACK)
            g.fill(new Ellipse2D.Double(x0 + x * wid + 10, y0 + y * wid + 10,
              wid - 20, wid - 20))
          case 2 =>
            g.setColor(Color.WHITE)
            g.fill(new Ellipse2D.Double(x0 + x * wid + 10, y0 + y * wid + 10,
              wid - 20, wid - 20))
          case _ => // draw nothing
        }
      }
    }
  }
}
