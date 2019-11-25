package com.github.nnnnusui.othello.entity

import com.github.nnnnusui.anydimensional.{Coordinates, Space}

object Game {
  private val board = Board
  def put(player: Player, coordinates: Coordinates): Unit ={
    board.put(player.generateDisc, coordinates)
  }
}
