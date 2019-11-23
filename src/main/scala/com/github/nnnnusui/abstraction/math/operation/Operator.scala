package com.github.nnnnusui.abstraction.math.operation

class Operator[Lhs, Out](lhs: Lhs){
  def +[Rhs](rhs: Rhs)(implicit toLhs: Rhs => Lhs, has: Plus[Lhs, Out]): Out
    = has.plus(lhs, rhs)
  def +(rhs: Lhs)(implicit has: Plus[Lhs, Out]): Out
    = has.plus(lhs, rhs)
  def -[Rhs](rhs: Rhs)(implicit toLhs: Rhs => Lhs, has: Minus[Lhs, Out]): Out
    = has.minus(lhs, rhs)
  def -(rhs: Lhs)(implicit has: Minus[Lhs, Out]): Out
    = has.minus(lhs, rhs)
}
