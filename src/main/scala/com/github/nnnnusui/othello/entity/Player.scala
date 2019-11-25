package com.github.nnnnusui.othello.entity

import java.util.UUID

case class Player(id: UUID = UUID.randomUUID()){
  def generateDisc = Disc(id)
}
