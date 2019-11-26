package com.github.nnnnusui.othello.entity

import com.github.nnnnusui.anydimensional.{Coordinates, Space}

object Board {
  private var space = Space[Disc]()
  def put(disc: Disc, coordinates: Coordinates): PutState ={
    if (space.isDefinedAt(coordinates))
      return PutState.DiskAlreadyExists
    space = space.updated(coordinates, disc)
    PutState.Success
  }
  def flip(disc: Disc, coordinates: Coordinates): FlipState ={
    if (!space.isDefinedAt(coordinates))
      return FlipState.DiskDoesNotExists
    space = space.updated(coordinates, disc)
    FlipState.Success
  }
  def state: Space[Disc] = space
}
sealed trait PutState
object PutState{
  case object DiskAlreadyExists extends PutState
  case object Success extends PutState
}
sealed trait FlipState
object FlipState{
  case object DiskDoesNotExists extends FlipState
  case object Success extends FlipState
}