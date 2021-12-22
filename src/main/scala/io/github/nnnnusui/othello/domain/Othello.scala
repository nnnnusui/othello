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
      discKinds,
      LazyList.continually(discKinds).flatten,
      Option.empty,
    )

class Othello[Disc] private (
    val board: Board[Disc],
    discKinds: Set[Disc],
    discKindStream: LazyList[Disc],
    discKindStartedThisLapPass: Option[Disc],
):
  val discKindOfTheTurn: Disc                        = discKindStream.head
  private lazy val moves: Map[Action, Othello[Disc]] = complementPassingMove(dropDiscMoves).toMap
  private val nextDiscKindStream: LazyList[Disc]     = discKindStream.drop(1)
  private val passedAround: Boolean  = discKindStartedThisLapPass.contains(discKindOfTheTurn)
  lazy val actions: Iterable[Action] = moves.keys
  lazy val isEnd: Boolean            = moves.keys.toSeq.isEmpty
  lazy val counts: Map[Disc, Int] =
    val grouped = board.groupedByValue.map((key, value) => (key, value.size))
    discKinds.map(it => (it, grouped.getOrElse(it, 0))).toMap

  def apply(action: Action): Option[Othello[Disc]] = moves.get(action)
  def droppedOption(coordinates: Coordinates): Option[Othello[Disc]] =
    for dropped <- board.droppedOption(coordinates, discKindOfTheTurn)
    yield updated(board = dropped)
  def passed: Othello[Disc] = updated(discKindStartedThisLapPass = Some(discKindOfTheTurn))

  type Move = (Action, Othello[Disc])
  private def complementPassingMove(moves: Seq[Move]): Seq[Move] =
    if moves.nonEmpty then return moves
    if passedAround then return LazyList.empty
    LazyList(Action.Pass -> passed)
  private def dropDiscMoves =
    for
      (coordinates, _) <- board.iterator.to(LazyList)
      dropped          <- droppedOption(coordinates)
    yield Action.Drop(coordinates) -> dropped

  private def updated(
      board: Board[Disc] = this.board,
      discKindStartedThisLapPass: Option[Disc] = Option.empty,
  ) = new Othello(
    board = board,
    discKinds = this.discKinds,
    discKindStream = nextDiscKindStream,
    discKindStartedThisLapPass = this.discKindStartedThisLapPass.orElse(discKindStartedThisLapPass),
  )

  override def toString: String =
    val actions =
      moves.keys.zipWithIndex
        .map((it, index) => s"$index: $it")
        .mkString(",\n")
    s"""${this.getClass.getSimpleName}(
       |  board = ${board.toString.indent(2).trim},
       |  discKindOfTheTurn = $discKindOfTheTurn,
       |  actions = Actions(
       |    ${actions.indent(4).trim}
       |  )
       |)""".stripMargin
