package com.github.nnnnusui.othello.entity

import com.github.nnnnusui.anydimensional.Coordinates
import org.scalatest.FunSuite

class GameTest extends FunSuite {
  implicit val axis: Seq[String] = Seq("x", "y")
  val testPlayer = Player()
  Game.initPut(testPlayer, Coordinates(3, 4))
  println(Game.boardState)
}
