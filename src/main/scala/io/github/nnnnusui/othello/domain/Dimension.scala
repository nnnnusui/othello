package io.github.nnnnusui.othello.domain

opaque type Dimension <: Int = Int
object Dimension:
  def apply(value: Int): Dimension = value
  extension (it: Dimension)
    def value: Int                           = it
    def multiply[A](elem: => A): Iterable[A] = Iterable.fill(value)(elem)
    def directions: Set[Coordinates] =
      Coordinates
        .manyByProduction(multiply(Iterable(0, 1, -1)))
        .drop(1) // remove [0, 0, ..., 0]
        .toSet
