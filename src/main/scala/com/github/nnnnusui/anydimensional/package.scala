package com.github.nnnnusui

package object anydimensional {
  sealed trait Axis
  object Axis{
    case object X extends Axis
    case object Y extends Axis
    case object Z extends Axis
    val values: Set[Axis] = Set(X, Y, Z)
  }
  implicit val axis: Set[Axis] = Axis.values
}
