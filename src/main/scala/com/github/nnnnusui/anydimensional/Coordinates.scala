package com.github.nnnnusui.anydimensional

import com.github.nnnnusui.abstraction.Is

case class Coordinates(value: Map[String, Int]) extends Is[Map[String, Int]]
object Coordinates{
  val identityElement = 0 //Plus.identityElement
  implicit def to(that: Coordinates): Map[String, Int] = that.value
  implicit def from(value: Map[String, Int]): Coordinates = Coordinates(value)
  def apply(values: Int*)(implicit keys: Seq[String] = Range(0, values.size).map(_.toString)): Coordinates ={
    val map = keys.zipWithIndex
      .map{ case(key, index)=> (key, values.lift(index).getOrElse(identityElement))}
      .toMap
    Coordinates(map)
  }
}
