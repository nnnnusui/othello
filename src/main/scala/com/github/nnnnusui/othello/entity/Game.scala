package com.github.nnnnusui.othello.entity

import com.github.nnnnusui.anydimensional.{Coordinates, Space}

object Game {
  private val board = Board
  def initPut(player: Player, coordinates: Coordinates): Unit ={
    board.put(player.generateDisc, coordinates)
  }
  def put(player: Player, coordinates: Coordinates): Unit ={

  }
  def boardState: Space[Disc] = board.state
}
