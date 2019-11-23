package com.github.nnnnusui.anydimensional

import com.github.nnnnusui.abstraction.Is

case class EuclideanSpace(value: Map[Coordinates, Int]) extends Is[Map[Coordinates, Int]]
object EuclideanSpace{
  implicit def to(that: EuclideanSpace): Map[Coordinates, Int] = that.value
  implicit def from(value: Map[Coordinates, Int]): EuclideanSpace = EuclideanSpace(value)

  private val identity = Coordinates.identityElement
  def apply(dimensions: Coordinates*): EuclideanSpace = {
    val map = dimensions.map(_ -> identity).toMap
    new EuclideanSpace(map)
  }
//  def apply(keys: Set[String]) = {}
//  def test(identity: Int, _axis: Set[String], _size: Seq[Int]): Map[Set[String], Seq[Int]] ={
//    val axis = _axis.toSeq.reverse.toSet
//    val size = _size.reverse
//    Seq((_axis, Seq.fill(size.head){ identity })).toMap
//  }
//  def nestedInit(identity: Int, _axis: Set[String], _size: Seq[Int]): (Set[String], Seq[_]) ={
//    val axis = _axis.toSeq.reverse.toSet
//    val size = _size.reverse
//    val created = (axis.toSeq.reverse.toSet, Seq.fill(size.head){ identity })
//    nested(created, axis.tail, size.tail)
//  }
//  @scala.annotation.tailrec
//  def nested[A](pair: (Set[String], Seq[A]), axis: Set[String], size: Seq[Int]): (Set[String], Seq[_]) ={
//    if(size.isEmpty) return pair
//    val created = (axis.toSeq.reverse.toSet, Seq.fill(size.head){ pair })
//    nested(created, axis.tail, size.tail)
//  }
}
