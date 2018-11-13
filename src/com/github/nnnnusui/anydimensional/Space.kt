package com.github.nnnnusui.anydimensional

import kotlin.math.pow

class Space<T>(
    val maxCoordinates: Coordinates
   ,init: (Int) -> T
  ){
    val dimension = maxCoordinates.dimension
    val space = Array(maxCoordinates().reduce{ acc, int -> acc * int}, {init(it) as Any}) as Array<T>
    val indices = space.indices
    val size    = space.size

    fun toOneDimensional(coordinates: Coordinates): Int {
        var mag = 1
        var sum = coordinates[0]
        for (i in 1..maxCoordinates.size-1) {
            mag *= maxCoordinates[i-1]
            sum += mag * (coordinates[i])
        }
        return sum
    }

    fun isIncluding(coordinates: Coordinates): Boolean
        = coordinates.coordinates.filter { it < 0 }.isEmpty()
       && (maxCoordinates - coordinates).coordinates.filter { it <= 0 }.isEmpty()

    fun getDirectionList(): Array<Coordinates> {
        val coordList = mutableListOf<Coordinates>()
        val totalDimension = 2F.pow(dimension).toInt() -1
        for (scanningAxis in totalDimension downTo 1) { // 次元列挙
            val selectedAxisFlags = Integer.toBinaryString(scanningAxis)
                                           .padStart(dimension, '0')
            val selectedAxisCount = selectedAxisFlags.count { it == '1' }
            val tortalDirection = 2F.pow(selectedAxisCount).toInt() -1
            for (scanningDirection in tortalDirection downTo 0) { // 方位列挙
                val scanningDirectionFlags = Integer.toBinaryString(scanningDirection)
                                                    .padStart(selectedAxisCount, '0')
                var flagCounter = 0
                coordList.add( Coordinates(
                    *selectedAxisFlags.map {
                        if (it == '1') {
                            flagCounter++
                            if (scanningDirectionFlags[flagCounter-1] == '0') -1
                            else                                             1
                        } else { 0 }
                    }.toIntArray())
                )
            }
        }
        return coordList.toTypedArray()
    }

    operator fun get(index: Int): T        = space[index]
    operator fun set(index: Int, value: T) { space[index] = value }
    operator fun get(coordinates: Coordinates): T        = space[toOneDimensional(coordinates)]
    operator fun set(coordinates: Coordinates, value: T) { space[toOneDimensional(coordinates)] = value }

    inline fun forEach(action: (T) -> Unit) {
        for (element in space) action(element)
    }
    inline fun forEachIndexed(action: (index: Int, T) -> Unit) {
        var index = 0
        for (item in space) action(index++, item)
    }

    override fun toString(): String {
        val sBuilder = StringBuilder("")
        space.forEachIndexed { index, disc ->
            var str = ", ${disc}"
            maxCoordinates().reduce { acc, i ->
                if ((index) % acc == 0) {
                    str = "\t${disc}"
                    if (index == 0) return@reduce 0
                    else            sBuilder.appendln()
                }
                acc * i
            }
             sBuilder.append(str)
        }
        return "Space _ size: ${maxCoordinates().joinToString("x ")} [\n${sBuilder}\n]"
    }
}

fun main(args: Array<String>) {
    val space = Space<Int>(
            Coordinates(8,8,8)
           ,{ 0 }
    )
    println(space)
    space.getDirectionList().forEach {
        println(it)
    }
}