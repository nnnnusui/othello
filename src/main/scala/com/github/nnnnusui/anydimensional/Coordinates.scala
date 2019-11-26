package com.github.nnnnusui.anydimensional

import com.github.nnnnusui.abstraction.Is

case class Coordinates[Key](value: Map[Key, Int]) extends Is[Map[Key, Int]]{
  override def toString: String = s"${this.getClass.getSimpleName}(${value.mkString(", ")})"
}
object Coordinates{
  val identityElement = 0 //Plus.identityElement
//  implicit def operator(lhs: Coordinates): Operator[Coordinates, Coordinates] = new Operator[Coordinates, Coordinates](lhs)
//  implicit object CoordinatesIsGroup extends CoordinatesIsGroup

//  implicit def to(that: Coordinates): Map[String, Int] = that.value
//  implicit def from(value: Map[String, Int]): Coordinates = Coordinates(value)
  def apply[Key](values: Int*)
                (implicit axis: Set[Key] = (0 to values.size).toSet.asInstanceOf[Set[Key]]): Coordinates[Key] ={
    val map = axis.zipWithIndex
                  .map{ case(key, index)=> (key, values.lift(index).getOrElse(identityElement))}
                  .toMap
    new Coordinates(map)
  }
}

//object Axis{
//  def apply(set: Set[Int]): Axis[Int] = {
//    Axis(set.zipWithIndex.toMap)
//  }
//}
//case class Axis[A](value: Map[Int, A]) extends Is[Map[Int, A]]
//trait CoordinatesIsGroup extends Group[Coordinates, Coordinates]{
//  override def plus(_1: Coordinates, _2: Coordinates): Coordinates  = operationResult(_1, _2, (_1, _2)=> _1 + _2)
//  override def minus(_1: Coordinates, _2: Coordinates): Coordinates = operationResult(_1, _2, (_1, _2)=> _1 - _2)
//
//  def operationResult(_1: Coordinates, _2: Coordinates, calculate: (Int, Int) => Int): Coordinates ={
//    val keySet = _1.keySet.union(_2.keySet)
//    val calculatedMap = keySet.map(key=> (key, calculate(_1.getOrElse(key, 0), _2.getOrElse(key, 0))) )
//                              .toMap
//    Coordinates(calculatedMap)
//  }
//}
