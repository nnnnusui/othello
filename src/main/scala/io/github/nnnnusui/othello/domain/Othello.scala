package io.github.nnnnusui.othello.domain

/* # 用語集
 * Othello: ゲームの名称
 * Board: ゲーム盤
 * Disc: ボード上に置く石
 * // Color: Discの種類
 */
object Othello:
  def apply[Disc](boardExpandLengths: Coordinates, discKinds: Set[Disc]) =
    new Othello(
      Board.initializedFromExpandLength(boardExpandLengths, discKinds),
      LazyList.continually(discKinds).flatten,
      Option.empty,
      discKinds,
    )

class Othello[Disc] private (
    val board: Board[Disc],
    discKindStream: LazyList[Disc],
    discKindStartedThisLapPass: Option[Disc],
    discKinds: Set[Disc],
):
  val discKindOfTheTurn: Disc                    = discKindStream.head
  lazy val moves: Map[Action, Othello[Disc]]     = complementPassingMove(dropDiscMoves).toMap
  lazy val isEnd: Boolean                        = moves.keys.toSeq.isEmpty
  private val nextDiscKindStream: LazyList[Disc] = discKindStream.drop(1)
  private val passedAround: Boolean = discKindStartedThisLapPass.contains(discKindOfTheTurn)

  lazy val counts: Map[Disc, Int] =
    val grouped =
      board.grouped.map((key, value) => (key, value.size))
    discKinds.map(it => (it, grouped.getOrElse(it, 0))).toMap

  type Move = (Action, Othello[Disc])
  private def complementPassingMove(moves: Seq[Move]): Seq[Move] =
    if moves.nonEmpty then return moves
    if passedAround then return LazyList.empty
    LazyList(
      (
        Action.Pass,
        new Othello(
          board,
          nextDiscKindStream,
          Option(discKindStartedThisLapPass.getOrElse(discKindOfTheTurn)),
          discKinds,
        ),
      ),
    )
  private def dropDiscMoves =
    for
      (coordinates, _) <- board.toSeq.to(LazyList)
      dropped          <- board.droppedOption(discKindOfTheTurn, coordinates)
    yield (
      Action.Drop(coordinates),
      new Othello(
        dropped,
        nextDiscKindStream,
        Option.empty,
        discKinds,
      ),
    )

  override def toString: String =
    val actions =
      moves.keys.toSeq.zipWithIndex
        .map((it, index) => s"$index: $it")
        .mkString(",\n")
    s"""${this.getClass.getSimpleName}(
       |  board = ${board.toString.indent(2).trim},
       |  discKindOfTheTurn = $discKindOfTheTurn,
       |  actions = Actions(
       |    ${actions.indent(4).trim}
       |  )
       |)""".stripMargin
