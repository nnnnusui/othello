package com.github.nnnnusui

package object anydimensional {
  sealed trait Three
  object Three{
    case object X extends Three
    case object Y extends Three
    case object Z extends Three
    val values: Set[Three] = Set(X, Y, Z)
  }
}
