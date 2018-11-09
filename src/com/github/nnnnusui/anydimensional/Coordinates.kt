package com.github.nnnnusui.anydimensional

class Coordinates(
        vararg values: Int
    ){
    val dimension = values.size
    val coordinates = values.toTypedArray().toIntArray()
    val indices = coordinates.indices
    val size    = coordinates.size





    fun clone(): Coordinates = Coordinates(*coordinates)

    operator fun invoke(): IntArray
        = coordinates
    operator fun get(i: Int): Int
        = coordinates[i]
    operator fun plus(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this.coordinates[it] + coord[it] }).toIntArray() )
    operator fun minus(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this.coordinates[it] - coord[it] }).toIntArray() )
    operator fun times(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this.coordinates[it] * coord[it] }).toIntArray() )
    operator fun div(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this.coordinates[it] / coord[it] }).toIntArray() )
    operator fun rem(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this.coordinates[it] % coord[it] }).toIntArray() )
}