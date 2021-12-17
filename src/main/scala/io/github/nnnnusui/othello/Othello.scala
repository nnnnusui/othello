package io.github.nnnnusui.othello

object Othello:
  def apply(board: Board, players: Seq[Player]) =
    new Othello(board, LazyList.continually(players).flatten, Option.empty)

  type Color       = Char
  type Coordinates = Seq[Int]
  type Move        = (Coordinates, Color)
  extension (it: Coordinates)
    def +(rhs: Coordinates): Coordinates =
      it.zipAll(rhs, 0, 0).map(_ + _)
  object Coordinates:
    def apply(values: Int*): Coordinates = values.toSeq

  case class Player(color: Color)
  sealed trait Action
  object Action:
    case class Drop(coordinates: Coordinates) extends Action
    case object Pass                          extends Action

  object Board:
    def initializedFromExpandLength(expandLengths: Coordinates, colors: Seq[Color]): Board =
      val upperBounds = expandLengths.map(_ * 2 + colors.length)
      val dimension   = expandLengths.length
      val empty       = Board(space = Map.empty, upperBounds)

      val colorStream = LazyList
        .continually(colors)
        .flatten
        .sliding(colors.length)
        .flatten
      val initPlaceCoordinates =
        Seq
          .fill(dimension)(colors.indices)
          .foldLeft(Seq(Seq.empty[Int])) { (sum, it) =>
            for
              upper   <- sum
              current <- it
            yield current +: upper
          }
          .map(_ + expandLengths)
      initPlaceCoordinates
        .zip(colorStream)
        .foldLeft(empty) { case (board, (coordinates, color)) =>
          board.updated(color, coordinates)
        }

  case class Board private (space: Map[Coordinates, Color], upperBounds: Coordinates):
    val dimension: Int = upperBounds.length
    val length: Int    = upperBounds.product
    def apply(coordinates: Coordinates): Color =
      space.getOrElse(coordinates, '_')
    def updated(color: Color, coordinates: Coordinates): Board =
      if !isIncluding(coordinates) then return this
      copy(space = space.updated(coordinates, color))
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
      upperBounds.zip(coordinates).forall(_ > _)
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

    def toSeq: Seq[Move] =
      upperBounds
        .foldLeft(Seq(Seq.empty[Int])) { (sum, it) =>
          for
            current <- Range(0, it) // The lower axis has higher iteration priority
            higherAxis <- sum
          yield higherAxis :+ current
        }
        .map(it => (it, space.getOrElse(it, '_')))
    override def toString: String =
      val colors =
        upperBounds.zipWithIndex
          .foldLeft(toSeq.map(_._2.toString)) { case (sum, (it, index)) =>
            sum.grouped(it).toSeq.map { it =>
              index match
                case 0 => it.mkString(", ")
                case 1 => it.mkString(",\n")
                case _ => it.map(it => s"  ${it.indent(2).trim}").mkString("(\n", "\n),(\n", "\n)")
            }
          }
      s"""Board(
         |  ${colors.mkString("\n").indent(2).trim}
         |)""".stripMargin

import Othello.*
class Othello private (
    val board: Board,
    val playerTurnStream: LazyList[Player],
    playerStartedThisLapPass: Option[Player],
):
  val player: Player                     = playerTurnStream.head
  val nextPlayerStream: LazyList[Player] = playerTurnStream.drop(1)
  lazy val moves: Map[Action, Othello]   = complementPassingMove(dropDiscMoves).toMap
  lazy val isEnd: Boolean                = moves.keys.toSeq.isEmpty
  private val passedAround: Boolean      = playerStartedThisLapPass.contains(player)

  type Self = Othello
  type Move = (Action, Othello)
  private def complementPassingMove(moves: Seq[Move]): Seq[Move] =
    if moves.nonEmpty then return moves
    if passedAround then return LazyList.empty
    LazyList(
      (
        Action.Pass,
        new Othello(
          board,
          nextPlayerStream,
          Option(playerStartedThisLapPass.getOrElse(player)),
        ),
      ),
    )
  private def dropDiscMoves =
    for
      (coordinates, _) <- board.toSeq.to(LazyList)
      dropped          <- board.droppedOption(player.color, coordinates)
    yield (
      Action.Drop(coordinates),
      new Othello(
        dropped,
        nextPlayerStream,
        Option.empty,
      ),
    )

  override def toString: String =
    val actions =
      moves.keys.toSeq.zipWithIndex
        .map((it, index) => s"$index: $it")
        .mkString(",\n")
    s"""${this.getClass.getSimpleName}(
       |  ${board.toString.indent(2).trim},
       |  $player,
       |  Actions(
       |    ${actions.indent(4).trim}
       |  )
       |)""".stripMargin
