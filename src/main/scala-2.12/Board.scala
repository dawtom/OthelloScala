import scala.collection.mutable.ListBuffer
import scala.swing.Dialog

/**
  * Created by Dawid Tomasiewicz on 11.06.17.
  */
class Board(){
  private var player = 1
  def setPlayer(newValue: Int): Unit ={
    player = newValue
  }
/*  val newGrid = Array.ofDim[BoardStates](8,8)

  def init(): Unit = {
    for (i <- 0.to(7) ; j <- 0.to(7)) {
      newGrid =
    }
  }*/


  private var grid = Array(0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 3, 3, 3, 3, 0, 0,
    0, 0, 3, 1, 2, 3, 0, 0,
    0, 0, 3, 2, 1, 3, 0, 0,
    0, 0, 3, 3, 3, 3, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0)


  def paintOtherPart(x: Int, y: Int, givenPlayer: Int, xChange: Int, yChange: Int): List[Int] = {
    var stopX = x + xChange
    var stopY = y + yChange
    var found = false
    var empty = false
    val otherPlayer = 3 - givenPlayer
    var toPaint = new ListBuffer[Int]

    while (stopX >= 0 && stopX <= 7 && stopY >= 0 && stopY <= 7 && !empty && !found){
      grid(8 * stopY + stopX) match {
        case 0 | 3 =>
          empty = true
          stopX += xChange
          stopY += yChange
        case `givenPlayer` =>
          found = true
        case `otherPlayer` =>
          stopX += xChange
          stopY += yChange
      }
    }
    if (found && !empty) {
      var xTmp = stopX - xChange
      var yTmp = stopY - yChange
      while (xTmp != x || yTmp != y){
        toPaint.+=(8 * yTmp + xTmp)
        xTmp -= xChange
        yTmp -= yChange
      }
    }

    toPaint.toList
  }

  def hasMove(player: Int): Boolean = {
    var result: Boolean = false
    var tmp = List.empty[Int]
    for (i <- 0 to 7; j <- 0 to 7){
      if (grid(8 * j + i) == 3){
        tmp = paintOther(i,j, player)
        if (tmp.nonEmpty) {
          result = true
        }
      }
    }

    result
  }

  def paintOther(x: Int, y: Int, givenPlayer: Int): List[Int] = {
    var result = List.empty[Int]
    var tmp = List.empty[Int]
    for (i <- -1 to 1; j <- -1 to 1){
      if (i != 0 || j != 0){
        tmp = paintOtherPart(x, y, player, i, j)
        result = List.concat(result, tmp)
      }
    }
    result
  }

  def howManyPlayersTiles(player: Int): Int = {
    var count = 0
    for (i <- 0 to 7 ; j <- 0 to 7) {
      if (grid(8 * j + i) == player) {
        count += 1
      }
    }
    count
  }

  def howManyAllTiles(): Int = {
    howManyPlayersTiles(1) + howManyPlayersTiles(2)
  }

  def apply(x: Int, y: Int): Int = grid(8 * y + x)
  def currentPlayer: Int = player
  def play(x: Int, y: Int) {
    var allTiles = 0
    var myTiles = 0
    var hisTiles = 0
    if (this(x, y) == 3) {
      println("This is 3")
      var toPaint = paintOther(x, y, player)
      if (toPaint.nonEmpty) {
        grid(8 * y + x) = player

        for (i <- toPaint) {
          grid(i) = player
        }


        for (i <-  x-1 to x+1 ; j <- y-1 to y + 1) {
          if ((i != x || j != y) && i >= 0 && i <= 7 && j >= 0 && j <= 7) {
            if (grid(8 * j + i) == 0) {
              grid(8 * j + i) = 3
            }
          }
        }
        myTiles = howManyPlayersTiles(player)
        allTiles = howManyAllTiles()
        hisTiles = allTiles - myTiles

        println("My tiles: ", myTiles)
        println("His tiles: ", hisTiles)
        println("All tiles: ", allTiles)

        player = 3 - player

      }

      var winner = 0

      if (allTiles >= 64) {
        player = 3 - player
        if (myTiles > hisTiles) {
          winner = player
        } else if (hisTiles > myTiles){
          winner = 3 - player
        }
        if (winner != 0) {
          Dialog.showMessage(message = "Game finished. Player %d won".format(winner))
        } else {
          Dialog.showMessage(message = "Game finished. Draw")
        }
      }
    }
  }

  def restart() {
    grid = Array(0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 3, 3, 3, 3, 0, 0,
      0, 0, 3, 1, 2, 3, 0, 0,
      0, 0, 3, 2, 1, 3, 0, 0,
      0, 0, 3, 3, 3, 3, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0)
    player = 1
  }
}
