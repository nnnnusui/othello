package io.github.nnnnusui.othello

import scala.annotation.tailrec
import scala.io.StdIn

trait Cli extends App:
  import Othello.*
  val max           = 8
  val board         = Board.initializedFromLength(max)
  val game: Othello = Othello(board, players = Seq('W', 'B').map(Player.apply))
  run(game)

  def run(game: Othello): Unit =
    LazyList
      .continually(true)
      .scanLeft(game) { (game, _) =>
        println(game) // subscribe game state & action
        val moves  = game.moves
        val action = fromStdIn(moves.keys.toSeq) // post action
        moves(action)
      }
      .dropWhile(!_.isEnd)
      .head

  @tailrec
  private def fromStdIn(actions: Seq[Action]): Action =
    val input = StdIn.readLine()
    {
      input.toIntOption match
        case None =>
          Left(s"$input is invalid.")
        case Some(value) =>
          if value >= actions.length || value < 0 then Left(s"IndexOutOfBoundsException: $value")
          else Right(value)
    } match
      case Left(value) =>
        println(value)
        fromStdIn(actions)
      case Right(value) => actions(value)
