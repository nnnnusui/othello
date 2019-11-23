package com.github.nnnnusui.anydimensional

import org.scalatest.FunSuite

class SpaceTest extends FunSuite {
  implicit val axis: Seq[String] = Seq("x", "y", "z")
  val space = Space(Coordinates(3, 3, 3))
  println(space)
}
