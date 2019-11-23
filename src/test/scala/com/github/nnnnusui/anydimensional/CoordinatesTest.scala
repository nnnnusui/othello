package com.github.nnnnusui.anydimensional

import org.scalatest.FunSuite

class CoordinatesTest extends FunSuite {
  {
    println("=> example =>")
    val coord1 = Coordinates(10, 20, 30)
    println(s"coord1: $coord1")
    println(s"keys: ${coord1.keys}")
    println("<=\n")
  }

  {
    println("=> implicit code example =>")
    implicit val axis: Seq[String] = Seq("x", "y", "z")
    println(s"axis: $axis")
    val coord1 = Coordinates(10, 20, 30)
    println(s"coord1: $coord1")
    val longCoord = Coordinates(10, 20, 30, 40, 50)
    println(s"long: $longCoord\t//round down")
    val shortCoord = Coordinates(10, 20)
    println(s"short: $shortCoord\t//fill in ${Coordinates.identityElement}")
    println("<=\n")


    println("=> calculation example =>")
    import Coordinates.operator
    println(s"coord1 + coord1: \n = ${coord1 + coord1}")
    println(s"coord1 - coord1: \n = ${coord1 - coord1}")
    println(s"coord1 - shortCoord: \n = ${coord1 - shortCoord}")
    println(s"Coordinates(2, 4, 8) - Coordinates(1024, -512, 258): \n = ${Coordinates(2, 4, 8) - Coordinates(1024, -512, 258)}")
    println("<=\n")
  }
}
