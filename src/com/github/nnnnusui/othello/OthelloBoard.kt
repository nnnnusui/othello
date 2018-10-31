package com.github.nnnnusui.othello

import kotlin.math.pow

class OthelloBoard(vararg maxLengths: Int) {

    val maxLengths = maxLengths
    val quantityOfAxis = maxLengths.size
    val board   = Array<Disc>(boardSize(*maxLengths), { Disc("none ") })
    var isGameSet = false

    init { println(" maxLengths: ${maxLengths.map { it }.joinToString(",")}") }

    fun boardSize(vararg lengths: Int): Int {
        var sum = 1
        lengths.forEach { sum *= it }
        return sum
    }

    fun start() {

    }

    fun initDrop(color: String, vararg lengths: Int) {
        board[sum(*lengths)] = Disc(color)
    }
//    fun drop(color: String, coordinate: ) {
//        drop(color, *coordinate.coordinates)
//    }
    fun drop(color: String, vararg lengths: Int): Boolean {
        search(color, *lengths)
        val dropSquare = board[sum(*lengths)]
        if (dropSquare.color != color) return false
        return true
    }
    fun search(color: String, vararg lengths: Int) {
        val dropSquare = board[sum(*lengths)]
        if (dropSquare.color != "none ") return

        val totalDirection = (2F.pow(quantityOfAxis)).toInt() -1
        for (scanningAxis in totalDirection downTo 1) {
            val totalDirectionFlags = Integer.toBinaryString(scanningAxis)
                                             .padStart(quantityOfAxis, '0')
            var quantityOfDirectionFlag = 0
            totalDirectionFlags.forEach {
                if (it == '1')
                    quantityOfDirectionFlag++
            }
            println("totalDirectionFlags: ${totalDirectionFlags}")

            val scanningDirection = (2F.pow(quantityOfDirectionFlag)).toInt() - 1
            for (scanningDirection in scanningDirection downTo 0) { // anyDirectionScan
                val scanningDirectionFlags = Integer.toBinaryString(scanningDirection)
                                                    .padStart(quantityOfDirectionFlag, '0')
                println("scanningDirectionFlags: ${scanningDirectionFlags}")
                val pos = lengths.clone()
                println("set. ${color}: ${pos.map { it + 1 }.joinToString(",")}")
                val moveNum = 1
                var moveCount = 0
                do {
                    var dirFlagCount = 0
                    for (axis in 0..totalDirectionFlags.length - 1)
                        if (totalDirectionFlags[axis] == '1') {
                            if (scanningDirectionFlags[dirFlagCount] == '1') pos[axis] += moveNum
                            else                                             pos[axis] -= moveNum
                            dirFlagCount += 1
                        }

                    try {
                    val square = board[sum(*pos)]
                    println("search. ${color}: ${pos.map { it + 1 }.joinToString(",")}")
                    if (square.color == "none ") break
                    if (square.color == color) { //flip
                        if (moveCount == 0) break
                        println("found. ${color}: ${pos.map { it + 1 }.joinToString(",")}")

                        for (i in moveCount downTo 0) {
                            var dirFlagCount = 0
                            for (axis in 0..totalDirectionFlags.length - 1)
                                if (totalDirectionFlags[axis] == '1') {
                                    if (scanningDirectionFlags[dirFlagCount] == '1') pos[axis] -= moveNum
                                    else                                             pos[axis] += moveNum
                                    dirFlagCount += 1
                                }
                            board[sum(*pos)] = Disc(color)
                        }
                        break
                    }
                    } catch(e: ArrayIndexOutOfBoundsException) {
                        println("catch: Edge of board.")
                        break
                    } finally { moveCount++ }
                } while (true)
            }
        }
    }

    fun sum(vararg ints: Int): Int {
        var mag = 1
        var sum = ints[0]
        for (i in 1..maxLengths.size-1) {
            mag *= maxLengths[i-1]
            sum += mag*(ints[i])
        }
        return sum
    }

    override fun toString(): String {
        val sBuilder = StringBuilder()

        for (i in board.indices) {
            sBuilder.append("${board[i]}, ")
            if ((i+1)% maxLengths[0] == 0) sBuilder.append("\n")
            if ((i+1)%(maxLengths[0]*maxLengths[1]) == 0) sBuilder.append("\n")
        }
        return "${board.size}\n${sBuilder}"
    }
}
class Disc(
  val color: String
  ){
    override fun toString(): String {
        return color
    }
}

class OthelloPlayer(
    val color: String
    ){


    fun newDisc(): Disc {
        return Disc(color)
    }
}

fun main(args: Array<String>) {
    val othello = OthelloBoard(3, 4, 2)
//    othello.initDrop("white", 3, 3)
//    othello.initDrop("white", 4, 4)
//    othello.initDrop("black", 3, 4)
//    othello.initDrop("black", 4, 3)
    othello.initDrop("white", 1, 3, 0)

    println(othello)
    do {
        do {
            println("turn: white")
            val input = readLine()!!.split(",").map { it.toInt()-1 }.toIntArray()
            val isDropped = othello.drop("white", *input)
            println(othello)
        } while (!isDropped)
        do {
            println("turn: black")
            val input2 = readLine()!!.split(",").map { it.toInt()-1 }.toIntArray()
            val isDroped = othello.drop("black", *input2)
            println(othello)
        } while (!isDroped)
    } while (!othello.isGameSet)
}