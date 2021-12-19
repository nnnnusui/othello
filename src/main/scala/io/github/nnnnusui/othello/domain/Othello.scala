package io.github.nnnnusui.othello.domain

object Othello:
  def apply(boardExpandLengths: Coordinates, players: Seq[Player]) =
    new Othello(
      Board.initializedFromExpandLength(boardExpandLengths, players.map(_.color)),
      LazyList.continually(players).flatten,
      Option.empty,
      players,
    )

class Othello private (
    val board: Board,
    val playerTurnStream: LazyList[Player],
    playerStartedThisLapPass: Option[Player],
    players: Seq[Player],
):
  val player: Player                     = playerTurnStream.head
  val nextPlayerStream: LazyList[Player] = playerTurnStream.drop(1)
  lazy val moves: Map[Action, Othello]   = complementPassingMove(dropDiscMoves).toMap
  lazy val isEnd: Boolean                = moves.keys.toSeq.isEmpty
  private val passedAround: Boolean      = playerStartedThisLapPass.contains(player)

  lazy val counts: Map[Player, Int] =
    val grouped: Map[Color, Int] =
      board.toSeq.groupMap(_._2)(_._1).map((key, value) => (key, value.length))
    players.map(it => (it, grouped.getOrElse(it.color, 0))).toMap

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
          players,
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
