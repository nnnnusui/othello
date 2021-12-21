package io.github.nnnnusui.othello.domain

object Dimension:
  def apply(value: Int): Dimension = value
  extension (it: Dimension)
    def value: Int = it
    def directions: Seq[Coordinates] =
      Coordinates
        .manyByProduction(Seq.fill(value)(Seq(0, 1, -1)))
        .drop(1) // remove [0, 0, ..., 0]

opaque type Dimension = Int
