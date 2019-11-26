package com.github.nnnnusui.anydimensional

import com.github.nnnnusui.abstraction.Is
import com.github.nnnnusui.abstraction.math.Group
import com.github.nnnnusui.abstraction.math.operation.Operator

case class Coordinates(value: Map[String, Int]) extends Is[Map[String, Int]]{
  override def toString: String = s"${this.getClass.getSimpleName}(${value.mkString(", ")})"
}
object Coordinates{
  val identityElement = 0 //Plus.identityElement
  implicit def operator(lhs: Coordinates): Operator[Coordinates, Coordinates] = new Operator[Coordinates, Coordinates](lhs)
  implicit object CoordinatesIsGroup extends CoordinatesIsGroup

  implicit def to(that: Coordinates): Map[String, Int] = that.value
  implicit def from(value: Map[String, Int]): Coordinates = Coordinates(value)
  def apply(values: Int*)(implicit axis: Set[String] = Range(0, values.size).map(_.toString).toSet): Coordinates ={
    val map = axis.zipWithIndex
      .map{ case(key, index)=> (key, values.lift(index).getOrElse(identityElement))}
      .toMap
    Coordinates(map)
  }
}
trait CoordinatesIsGroup extends Group[Coordinates, Coordinates]{
  override def plus(_1: Coordinates, _2: Coordinates): Coordinates  = operationResult(_1, _2, (_1, _2)=> _1 + _2)
  override def minus(_1: Coordinates, _2: Coordinates): Coordinates = operationResult(_1, _2, (_1, _2)=> _1 - _2)

  def operationResult(_1: Coordinates, _2: Coordinates, calculate: (Int, Int) => Int): Coordinates ={
    val keySet = _1.keySet.union(_2.keySet)
    val calculatedMap = keySet.map(key=> (key, calculate(_1.getOrElse(key, 0), _2.getOrElse(key, 0))) )
                              .toMap
    Coordinates(calculatedMap)
  }
}
