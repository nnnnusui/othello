package com.github.nnnnusui.anydimensional

class Coordinates(
        vararg ints: Int
){
    val dimension   = ints.size
    val coordinates = ints.toTypedArray().toIntArray()

    fun clone(): Coordinates
            = Coordinates(*coordinates)
}