package com.github.nnnnusui.abstraction.math.operation

trait Plus[-In, +Out] {
  def plus(_1: In, _2: In): Out
}
