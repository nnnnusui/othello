package io.github.nnnnusui.othello.domain

type Coordinates = Seq[Int]
object Coordinates:
  def apply(values: Int*): Coordinates = values.toSeq
extension (it: Coordinates)
  def +(rhs: Coordinates): Coordinates =
    it.zipAll(rhs, 0, 0).map(_ + _)
