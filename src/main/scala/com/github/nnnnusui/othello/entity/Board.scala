package com.github.nnnnusui.othello.entity

import com.github.nnnnusui.anydimensional.{Coordinates, Space}

object Board {
  private var space = Space[Disc]()
  def put(disc: Disc, coordinates: Coordinates): Unit ={
    space = space.updated(coordinates, disc)
  }
}
