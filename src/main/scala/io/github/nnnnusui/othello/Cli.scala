package io.github.nnnnusui.othello

import java.util.InputMismatchException
import scala.annotation.tailrec
import scala.io.StdIn

trait Cli extends App:
  import Othello.*
  println("> build othello game.")
  println("> plz input `boardExpandLengths`. (some number line and END line)")
  println(s"""ex)
      |  input: \"\"\"
      |    ${Seq("3", "1", "END").mkString("\n").indent(4).trim}
      |  \"\"\"
      |  ->
      |    ${Board.initializedFromExpandLength(Seq(3, 1)).toString.indent(4).trim}
      |""".stripMargin)
  val boardExpandLengths = coordinatesFromStdIn()
  val board              = Board.initializedFromExpandLength(boardExpandLengths)
  val game: Othello      = Othello(board, players = Seq('W', 'B').map(Player.apply))
  println("> game start.")
  run(game)

  def run(game: Othello): Unit =
    LazyList
      .continually(true)
      .scanLeft(game) { (game, _) =>
        println(game) // subscribe game state & action
        val moves  = game.moves
        val action = actionFromStdIn(moves.keys.toSeq) // post action
        moves(action)
      }
      .dropWhile(!_.isEnd)
      .head

  private def coordinatesFromStdIn(): Coordinates =
    LazyList
      .continually(true)
      .scanLeft((Seq.empty[Int], true)) { case ((sum, _), _) =>
        intFromStdIn() match
          case Left(value)  => (sum, false)
          case Right(value) => (sum :+ value, true)
      }
      .dropWhile(_._2)
      .head
      ._1

  @tailrec
  private def actionFromStdIn(actions: Seq[Action]): Action =
    intFromStdIn() match
      case Left(value) =>
        println(value)
        actionFromStdIn(actions)
      case Right(value) =>
        if value >= actions.length || value < 0 then
          println(IndexOutOfBoundsException(value))
          actionFromStdIn(actions)
        else actions(value)

  private def intFromStdIn(): Either[Exception, Int] =
    val input = StdIn.readLine()
    input.toIntOption match
      case None        => Left(InputMismatchException(input))
      case Some(value) => Right(value)
