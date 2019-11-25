package com.github.nnnnusui.anydimensional

import com.github.nnnnusui.abstraction.Is

case class Space[A](value: Map[Coordinates, A]) extends Is[Map[Coordinates, A]]{
  override def toString: String = s"${this.getClass.getSimpleName}(${value.mkString(", ")})"
}
object Space{
  implicit def to[A](that: Space[A]): Map[Coordinates, A] = that.value
  implicit def from[A](value: Map[Coordinates, A]): Space[A] = Space(value)

  def apply[A](dimensions: (Coordinates, A)*): Space[A] = {
    val map = dimensions.toMap
    new Space(map)
  }
}
