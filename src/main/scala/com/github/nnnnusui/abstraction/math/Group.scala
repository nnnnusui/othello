package com.github.nnnnusui.abstraction.math

import com.github.nnnnusui.abstraction.math.operation.{Minus, Plus}

trait Group[-In, +Out] extends Plus[In, Out] with Minus[In, Out]
