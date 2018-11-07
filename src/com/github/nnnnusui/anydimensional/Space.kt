package com.github.nnnnusui.anydimensional

import kotlin.math.pow

class Space<T>(
    val maxCoordinates: Coordinates
   ,init: (index: Int) -> T
  ){
    val dimension = maxCoordinates.dimension
    val space = Array<Any>(sizees(maxCoordinates), {init(it) as Any})
    val indices = space.indices
    val size    = space.size

    fun sizees(maxCoordinates: Coordinates): Int {
        return maxCoordinates().reduce{ acc, int -> acc * int}
    }
    fun toOneDimensional(coordinates: Coordinates): Int {
        var mag = 1
        var sum = coordinates[0]
        for (i in 1..maxCoordinates.size-1) {
            mag *= maxCoordinates[i-1]
            sum += mag * (coordinates[i])
        }
        return sum
    }
    operator fun get(i: Int): T
        = space[i] as T
    operator fun set(i: Int, b: T)
        { space[i] = b as Any }
    operator fun get(coordinates: Coordinates): T
        = space[toOneDimensional(coordinates)] as T
    operator fun set(coordinates: Coordinates, b: T)
        { space[toOneDimensional(coordinates)] = b as Any }

    override fun toString(): String {
        val sBuilder = StringBuilder()

        for (i in space.indices) {
            sBuilder.append("${space[i]}, ")
            var mag = 1
            for (len in maxCoordinates()) {
                mag *= len
                if ((i + 1) % mag == 0) sBuilder.append("\n")
            }
        }
        return "${space.size}\n${sBuilder}"
    }
}

fun main(args: Array<String>) {
    val space = Space<Int>(
            Coordinates(8,8)
           ,{ 0 }
    )
    println(space)
}