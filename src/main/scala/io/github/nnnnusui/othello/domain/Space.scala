package io.github.nnnnusui.othello.domain

opaque type Space[A] <: Map[Coordinates, A] = Map[Coordinates, A]
object Space:
  def apply[A](value: Map[Coordinates, A]): Space[A] = value
  extension [A](self: Space[A])
    def groupedByValue: Map[A, Iterable[Coordinates]] = self.groupMap(_._2)(_._1)
    def lineTrace(origin: Coordinates, step: Coordinates): LazyList[(Coordinates, Option[A])] =
      LazyList.continually(step).scanLeft(origin)(_ + _).map(it => (it, self.get(it)))
