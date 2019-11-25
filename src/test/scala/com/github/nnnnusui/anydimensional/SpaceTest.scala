package com.github.nnnnusui.anydimensional

import org.scalatest.FunSuite

class SpaceTest extends FunSuite {
//  sealed trait Axis
//  object Axis{
//    case object X extends Axis
//    case object Y extends Axis
//    case object Z extends Axis
//  }
  implicit val axis: Seq[String] = Seq("x", "y", "z")
  val space = Space()
  println(space)
  val map = space.updated(Coordinates(1, 2, 3), 5)
  println(Space.from(map))
}
