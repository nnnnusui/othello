package io.github.nnnnusui.othello.domain

object Board:
  def initializedFromExpandLength[Disc](
      expandLengths: Coordinates,
      discKinds: Set[Disc],
  ): Board[Disc] =
    val upperBounds = expandLengths.map(_ * 2 + discKinds.size)
    val dimension   = expandLengths.length
    val empty       = Board[Disc](space = Space(Map.empty), upperBounds)

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

case class Board[Disc] private (space: Space[Disc], upperBounds: Coordinates):
  val dimension: Dimension = upperBounds.dimension
  val length: Int          = upperBounds.product
  def apply(coordinates: Coordinates): Option[Disc] =
    space.get(coordinates)
  def updated(disc: Disc, coordinates: Coordinates): Board[Disc] = // TODO
    if !isIncluding(coordinates) then return this
    copy(space = Space(space.updated(coordinates, disc)))
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
        for direction <- dimension.directions
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

  def grouped: Map[Disc, Iterable[Coordinates]] = space.reversed // TODO

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

  // bounded space's methods
  def isIncluding(coordinates: Coordinates): Boolean =
    upperBounds.zip(coordinates).forall(_ > _) &&
      Iterable.empty.zipAll(coordinates, 0, 0).forall(_ <= _)
  def toSeq: Seq[(Coordinates, Option[Disc])] =
    Coordinates
      .manyByProduction(upperBounds.map(Range(0, _)))
      .map(it => (it, space.get(it)))
