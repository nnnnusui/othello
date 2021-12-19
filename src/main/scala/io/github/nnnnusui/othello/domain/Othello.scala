package io.github.nnnnusui.othello.domain

object Othello:
  def apply(boardExpandLengths: Coordinates, players: Set[Player]) =
    new Othello(
      Board.initializedFromExpandLength(boardExpandLengths, players),
      LazyList.continually(players).flatten,
      Option.empty,
      players,
    )

class Othello[Disc] private (
    val board: Board[Disc],
    val playerTurnStream: LazyList[Disc],
    playerStartedThisLapPass: Option[Disc],
    players: Set[Disc],
):
  val player: Disc                           = playerTurnStream.head
  val nextPlayerStream: LazyList[Disc]       = playerTurnStream.drop(1)
  lazy val moves: Map[Action, Othello[Disc]] = complementPassingMove(dropDiscMoves).toMap
  lazy val isEnd: Boolean                    = moves.keys.toSeq.isEmpty
  private val passedAround: Boolean          = playerStartedThisLapPass.contains(player)

  lazy val counts: Map[Disc, Int] =
    val grouped =
      board.grouped.map((key, value) => (key, value.length))
    players.map(it => (it, grouped.getOrElse(it, 0))).toMap

  type Move = (Action, Othello[Disc])
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
          players,
        ),
      ),
    )
  private def dropDiscMoves =
    for
      (coordinates, _) <- board.toSeq.to(LazyList)
      dropped          <- board.droppedOption(player, coordinates)
    yield (
      Action.Drop(coordinates),
      new Othello(
        dropped,
        nextPlayerStream,
        Option.empty,
        players,
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
