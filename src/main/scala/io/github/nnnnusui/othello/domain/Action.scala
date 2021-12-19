package io.github.nnnnusui.othello.domain

sealed trait Action
object Action:
  case class Drop(coordinates: Coordinates) extends Action
  case object Pass                          extends Action
