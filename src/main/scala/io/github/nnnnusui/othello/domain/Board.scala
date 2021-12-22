package io.github.nnnnusui.othello.domain

object Board:
  def initializedFromExpandLength[Disc](
      expandLengths: Coordinates,
      discKinds: Set[Disc],
  ): Board[Disc] =
    val upperBounds = expandLengths.map(_ * 2 + discKinds.size)
    val dimension   = expandLengths.dimension
    val empty       = Board[Disc](Space(), upperBounds)

    val discStream =
      dimension
        .multiply(discKinds.size)
        .scanLeft((1, 1)) { case ((before, _), it) => (before * it, before) }
        .foldLeft(LazyList.continually(discKinds).flatten) { case (stream, (it, before)) =>
          LazyList.continually(stream.sliding(it, before).flatten).flatten
        }
    Coordinates
      .manyByProduction(dimension.multiply(Range(0, discKinds.size)))
      .map(_ + expandLengths) // init place coordinates
      .zip(discStream)
      .foldLeft(empty) { case (board, (coordinates, disc)) =>
        board.updated(coordinates, disc)
      } // initialized board

case class Board[Disc] private (space: Space[Disc], upperBounds: Coordinates):
  val dimension: Dimension = upperBounds.dimension
  val length: Int          = upperBounds.product

  def apply(coordinates: Coordinates): Option[Disc] = space.get(coordinates)
  def updated(coordinates: Coordinates, disc: Disc): Board[Disc] =
    if !isIncluding(coordinates) then return this
    copy(space = Space(space.updated(coordinates, disc)))
  def droppedOption(coordinates: Coordinates, disc: Disc): Option[Board[Disc]] =
    replaceTargetsOnDrop(coordinates, disc) match
      case it if it.isEmpty => Option.empty
      case it               => Some(it.foldLeft(this) { (board, it) => board.updated(it, disc) })

  // bounded space's methods ->
  def isIncluding(coordinates: Coordinates): Boolean =
    upperBounds.zip(coordinates).forall(_ > _) &&
      Iterable.empty.zipAll(coordinates, 0, 0).forall(_ <= _)
  def iterable: Iterable[(Coordinates, Option[Disc])] =
    Coordinates
      .manyByProduction(upperBounds.map(Range(0, _)))
      .map(it => (it, space.get(it)))
  // <-
  def groupedByValue: Map[Disc, Iterable[Coordinates]] = space.groupedByValue

  private def replaceTargetsOnDrop(origin: Coordinates, discKind: Disc): Seq[Coordinates] =
    if !isIncluding(origin) then return Seq.empty
    if apply(origin).isDefined then return Seq.empty
    dimension.directions.flatMap { direction =>
      space
        .scanLine(origin, direction)(Seq.empty[Coordinates]) {
          case (stack, (coordinates, mayBeDisc)) =>
            mayBeDisc match
              case None                       => (false, Seq.empty)     // cancel
              case Some(it) if it == discKind => (false, stack.reverse) // result
              case _                          => (true, coordinates +: stack)
        }
    } match
      case it if it.isEmpty => Seq.empty
      case it               => origin +: it.toSeq

  override def toString: String =
    val discTexts = iterable.map {
      case (_, Some(value)) => value.toString.head.toString
      case (_, None)        => "_"
    }
    val colors =
      upperBounds.zipWithIndex
        .foldLeft(discTexts.iterator) { case (sum, (axisBound, index)) =>
          sum.grouped(axisBound).map { it =>
            index match
              case 0 => it.mkString(", ")
              case 1 => it.mkString(",\n")
              case _ => it.map(it => s"  ${it.indent(2).trim}").mkString("(\n", "\n),(\n", "\n)")
          }
        }
    s"""Board(
       |  ${colors.mkString("\n").indent(2).trim}
       |)""".stripMargin
