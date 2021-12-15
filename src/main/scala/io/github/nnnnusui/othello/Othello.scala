package io.github.nnnnusui.othello

object Othello:
  def apply(board: Board, players: Seq[Player]) =
    new Othello(board, LazyList.continually(players).flatten, false)

  type Color = Char
  object Coordinates:
    def fromSeq(values: Seq[Int]): Option[Coordinates] =
      if values.length < 2 then return Option.empty
      Some(Coordinates(values.head, values(1)))
  case class Coordinates(x: Int, y: Int):
    def +(rhs: Coordinates): Coordinates = Coordinates(x + rhs.x, y + rhs.y)

  case class Player(color: Color)
  sealed trait Action
  object Action:
    case class Drop(coordinates: Coordinates) extends Action
    case object Pass                          extends Action

  object Board:
    def initializedFromLength(length: Int): Board =
      val (x, y) = (length, length)
      val empty  = Board(Seq.fill(x)(Seq.fill(y)('_')))

      val half = length / 2
      empty
        .updated('W', Coordinates(half - 1, half - 1))
        .updated('B', Coordinates(half - 1, half))
        .updated('B', Coordinates(half, half - 1))
        .updated('W', Coordinates(half, half))

  case class Board(space: Seq[Seq[Color]]):
    val dimension = 2
    def apply(coordinates: Coordinates): Color =
      val Coordinates(x, y) = coordinates
      space(y)(x)
    def updated(color: Color, coordinates: Coordinates): Board =
      if !isIncluding(coordinates) then return this
      val Coordinates(x, y) = coordinates
      Board(space.updated(y, space(y).updated(x, color)))
    //  def dropped(color: Color, coordinates: Coordinates): Board =
    //    droppedOption(color, coordinates).getOrElse(this)
    def droppedOption(color: Color, coordinates: Coordinates): Option[Board] =
      val replaceTargets = replaceTargetsOnDrop(color, coordinates)
      if replaceTargets.isEmpty then return Option.empty
      Some(
        replaceTargets
          .foldLeft(this) { (board, it) => board.updated(color, it) },
      )
    def replaceTargetsOnDrop(color: Color, coordinates: Coordinates): Seq[Coordinates] =
      if !isIncluding(coordinates) then return Seq.empty
      if apply(coordinates) != '_' then return Seq.empty
      val replaceTargets =
        (
          for direction <- directionList
          yield
            def scanning(
                current: Coordinates,
                result: Seq[Coordinates],
            ): Seq[Coordinates] =
              if !isIncluding(current) then return Seq.empty
              this.apply(current) match
                case '_' => Seq.empty
                case it if it == color =>
                  result
                case _ =>
                  scanning(current + direction, result :+ current)
            scanning(coordinates + direction, Seq())
        ).flatten
      if replaceTargets.isEmpty then return Seq.empty
      replaceTargets :+ coordinates

    def isIncluding(coordinates: Coordinates): Boolean =
      val Coordinates(x, y) = coordinates
      space.length > x && x >= 0 && space(x).length > y && y >= 0
    def directionList: Seq[Coordinates] =
      Seq
        .fill(dimension)(Seq(0, 1, -1))
        .foldLeft(Seq(Seq.empty[Int])) { (sum, it) =>
          for
            upper   <- sum
            current <- it
          yield upper :+ current
        }
        .drop(1) // remove [0, 0, ..., 0]
        .map(it => Coordinates.fromSeq(it).get)

    def toSeq: Seq[(Color, Coordinates)] =
      for
        (row, y)   <- space.zipWithIndex
        (color, x) <- row.zipWithIndex
      yield (color, Coordinates(x, y))
    override def toString: String =
      s"""Board(
         |  ${space.map(_.mkString(", ")).mkString("\n").indent(2).trim}
         |)""".stripMargin

import Othello.*
class Othello private (
    val board: Board,
    val playerTurnStream: LazyList[Player],
    wasPassed: Boolean,
):
  val player: Player                     = playerTurnStream.head
  val nextPlayerStream: LazyList[Player] = playerTurnStream.drop(1)
  lazy val moves: Map[Action, Othello]   = complementPassingMove(dropDiscMoves).toMap
  lazy val isEnd: Boolean                = moves.keys.toSeq.isEmpty

  type Self = Othello
  type Move = (Action, Othello)
  private def complementPassingMove(moves: Seq[Move]): Seq[Move] =
    if moves.nonEmpty then return moves
    if wasPassed then return LazyList.empty
    LazyList(
      (
        Action.Pass,
        new Othello(
          board,
          nextPlayerStream,
          true,
        ),
      ),
    )
  private def dropDiscMoves =
    for
      (_, coordinates) <- board.toSeq.to(LazyList)
      dropped          <- board.droppedOption(player.color, coordinates)
    yield (
      Action.Drop(coordinates),
      new Othello(
        dropped,
        nextPlayerStream,
        false,
      ),
    )

  override def toString: String =
    val actions =
      moves.keys.toSeq.zipWithIndex
        .map((it, index) => s"$index: $it")
        .mkString(",\n")
    s"""${this.getClass.getSimpleName}(
       |  ${board.toString.indent(2).trim}
       |  $player,
       |  Actions(
       |    ${actions.indent(4).trim}
       |  )
       |)""".stripMargin
