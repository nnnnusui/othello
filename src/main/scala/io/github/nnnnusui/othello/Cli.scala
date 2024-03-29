package io.github.nnnnusui.othello

import io.github.nnnnusui.othello.domain.*

import java.util.InputMismatchException
import scala.annotation.tailrec
import scala.io.StdIn

trait Cli extends App:
  case class Player(color: Char):
    override def toString: String = color.toString

  println("> build othello game.")
  println("> plz input `boardExpandLengths`. (some number line and END line)")
  println(s"""ex)
      |  input: \"\"\"
      |    ${Seq("3", "1", "END").mkString("\n").indent(4).trim}
      |  \"\"\"
      |  ->
      |    ${Othello(Seq(3, 1), Set('W', 'B').map(Player.apply)).toString.indent(4).trim}
      |""".stripMargin)
  val boardExpandLengths = coordinatesFromStdIn()

  println("> plz input `colors`. (write some letters on the line)")
  val colors = StdIn.readLine().toCharArray.toSet
  val game   = Othello(boardExpandLengths, colors.map(Player.apply))

  println("> game start.")
  val result = run(game)

  println("RESULT:")
  result.counts foreach { (player, value) => println(s"  $player -> DiscCount: $value") }

  def run(game: Othello[Player]): Othello[Player] =
    LazyList
      .continually(true)
      .scanLeft(game) { (game, _) =>
        println(game) // subscribe game state & action
        val action = actionFromStdIn(game.actions.toSeq) // post action
        game(action).getOrElse(game)
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
