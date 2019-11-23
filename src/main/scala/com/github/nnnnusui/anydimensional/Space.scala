package com.github.nnnnusui.anydimensional

import com.github.nnnnusui.abstraction.Is

case class Space(value: Map[Coordinates, Int]) extends Is[Map[Coordinates, Int]]{
  override def toString: String = s"${this.getClass.getSimpleName}(${value.mkString(", ")})"
}
object Space{
  implicit def to(that: Space): Map[Coordinates, Int] = that.value
  implicit def from(value: Map[Coordinates, Int]): Space = Space(value)

  private val identity = Coordinates.identityElement
  def apply(dimensions: Coordinates*): Space = {
    val map = dimensions.map(_ -> identity).toMap
    new Space(map)
  }
}
