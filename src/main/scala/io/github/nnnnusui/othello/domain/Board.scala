package io.github.nnnnusui.othello.domain

object Board:
  def initializedFromExpandLength[Disc](
      expandLengths: Coordinates,
      discKinds: Set[Disc],
  ): Board[Disc] =
    val upperBounds = expandLengths.map(_ * 2 + discKinds.size)
    val dimension   = expandLengths.length
    val empty       = Board[Disc](space = Map.empty, upperBounds)

    val discStream =
      Seq
        .fill(dimension)(discKinds.size)
        .scanLeft((1, 1)) { case ((before, _), it) => (before * it, before) }
        .foldLeft(LazyList.continually(discKinds).flatten) { case (stream, (it, before)) =>
          LazyList.continually(stream.sliding(it, before).flatten).flatten
        }
    Coordinates
      .manyByProduction(Seq.fill(dimension)(Range(0, discKinds.size)))
      .map(_ + expandLengths) // init place coordinates
      .zip(discStream)
      .foldLeft(empty) { case (board, (coordinates, disc)) =>
        board.updated(disc, coordinates)
      } // initialized board

case class Board[Disc] private (space: Map[Coordinates, Disc], upperBounds: Coordinates):
  val dimension: Int = upperBounds.length
  val length: Int    = upperBounds.product
  def apply(coordinates: Coordinates): Option[Disc] =
    space.get(coordinates)
  def updated(disc: Disc, coordinates: Coordinates): Board[Disc] =
    if !isIncluding(coordinates) then return this
    copy(space = space.updated(coordinates, disc))
  def droppedOption(disc: Disc, coordinates: Coordinates): Option[Board[Disc]] =
    val replaceTargets = replaceTargetsOnDrop(disc, coordinates)
    if replaceTargets.isEmpty then return Option.empty
    Some(
      replaceTargets
        .foldLeft(this) { (board, it) => board.updated(disc, it) },
    )
  def replaceTargetsOnDrop(disc: Disc, coordinates: Coordinates): Seq[Coordinates] =
    if !isIncluding(coordinates) then return Seq.empty
    if apply(coordinates).isDefined then return Seq.empty
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
              case None                   => Seq.empty
              case Some(it) if it == disc => result
              case _                      => scanning(current + direction, result :+ current)
          scanning(coordinates + direction, Seq())
      ).flatten
    if replaceTargets.isEmpty then return Seq.empty
    replaceTargets :+ coordinates

  def toSeq: Seq[(Coordinates, Option[Disc])] =
    Coordinates
      .manyByProduction(upperBounds.map(Range(0, _)))
      .map(it => (it, space.get(it)))
  def grouped: Map[Disc, Iterable[Coordinates]] = space.groupMap(_._2)(_._1)

  override def toString: String =
    val discs = toSeq
      .map(_._2)
      .map {
        case Some(value) => value.toString.head
        case None        => '_'
      }
      .map(_.toString)
    val colors =
      upperBounds.zipWithIndex
        .foldLeft(discs) { case (sum, (it, index)) =>
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

  // space's methods
  def isIncluding(coordinates: Coordinates): Boolean =
    upperBounds.zip(coordinates).forall(_ > _)
  def directionList: Seq[Coordinates] =
    Coordinates
      .manyByProduction(Seq.fill(dimension)(Seq(0, 1, -1)))
      .drop(1) // remove [0, 0, ..., 0]
