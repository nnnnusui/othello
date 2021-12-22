package io.github.nnnnusui.othello.domain

object Coordinates:
  def manyByProduction(source: Iterable[Iterable[Int]]): Iterable[Coordinates] =
    source
      .foldRight(Iterable(Seq.empty[Int])) { (it, highers) =>
        for
          higherAxis <- highers
          current <- it // The lower axis has higher iteration priority
        yield current +: higherAxis
      }
  def apply(values: Int*): Coordinates = values.toSeq

type Coordinates = Seq[Int]
extension (it: Coordinates)
  def +(rhs: Coordinates): Coordinates = it.zipAll(rhs, 0, 0).map(_ + _)
  def dimension: Dimension             = Dimension(it.size)
