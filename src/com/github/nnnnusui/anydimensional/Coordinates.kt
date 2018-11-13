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
            = Coordinates(*Array(dimension, { this[it] + coord[it] }).toIntArray() )
    operator fun minus(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this[it] - coord[it] }).toIntArray() )
    operator fun times(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this[it] * coord[it] }).toIntArray() )
    operator fun div(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this[it] / coord[it] }).toIntArray() )
    operator fun rem(coord: Coordinates): Coordinates
            = Coordinates(*Array(dimension, { this[it] % coord[it] }).toIntArray() )
    operator fun compareTo(coord: Coordinates): Int {
        return (this - coord).coordinates.filter { it == 0 }.firstOrNull()
             ?:(this - coord).coordinates.filter { it <  0 }.firstOrNull()
             ?:(this - coord).coordinates.filter { it >  0 }.first()
    }
//            = (this - coord).coordinates.filter { it == 0 }.firstOrNull()
//            ?:(this - coord).coordinates.filter { it > 0 }.firstOrNull()
//            ?:(this - coord).coordinates.filter { it < 0 }.firstOrNull() ?: 0

    inline fun forEach(action: (Int) -> Unit) {
        for (element in coordinates) action(element)
    }
    inline fun forEachIndexed(action: (index: Int, coordinate: Int) -> Unit) {
        var index = 0
        for (item in coordinates) action(index++, item)
    }


    override fun toString(): String
            = coordinates.joinToString(", ")
}

fun main(args: Array<String>) {
    val coord1 = Coordinates(2, -2)
    val coord2 = Coordinates(3, -3)
    println("${coord1} >  ${coord2}: ${coord1 > coord2}")
    println("${coord1} <  ${coord2}: ${coord1 < coord2}")
    println("${coord1} >= ${coord2}: ${coord1 >= coord2}")
    println("${coord1} <=  ${coord2}: ${coord1 <= coord2}")
}