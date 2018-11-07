package com.github.nnnnusui.othello

import com.github.nnnnusui.anydimensional.Coordinates
import com.github.nnnnusui.anydimensional.Space
import kotlin.math.pow

class OthelloBoard(
  val onFlip: (color: String, coordinates: Coordinates) -> Unit
 ,vararg maxLengths: Int
  ){
    val INITIAL_DISC_COLOR = "none "
    val maxLengths = maxLengths
    val quantityOfAxis = maxLengths.size
    val initDisc = Disc(INITIAL_DISC_COLOR)
    val board = Space<Disc>(Coordinates(*maxLengths), { initDisc })
    var isGameSet = false

    init { println(" maxLengths: ${maxLengths.map { it }.joinToString(",")}") }

    fun initDrop(color: String, coordinates: Coordinates) {
        board[coordinates] = Disc(color)
        onFlip(color, coordinates)
    }
    fun drop(color: String, vararg lengths: Int): Boolean {
        return drop(color, Coordinates(*lengths))
    }
    fun drop(color: String, coordinates: Coordinates): Boolean {
        search(color, coordinates)
        val dropSquare = board[coordinates]
        if (dropSquare.color != color) return false
        return true
    }
    fun search(color: String, coordinates: Coordinates) {
        val dropSquare = board[coordinates]
        if (dropSquare.color != INITIAL_DISC_COLOR) return

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
                val pos = coordinates.clone()
                println("set. ${color}: ${pos().map { it + 1 }.joinToString(",")}")
                val moveNum = 1
                var moveCount = 0
                do {
                    var dirFlagCount = 0
                    for (axis in 0..totalDirectionFlags.length - 1)
                        if (totalDirectionFlags[axis] == '1') {
                            if (scanningDirectionFlags[dirFlagCount] == '1') pos()[axis] += moveNum
                            else                                             pos()[axis] -= moveNum
                            dirFlagCount += 1
                        }

                    try {
                        val square = board[pos]
                        println("search. ${color}: ${pos().map { it + 1 }.joinToString(",")}")
                        if (square.color == INITIAL_DISC_COLOR) break
                        if (square.color == color) { //flip
                            if (moveCount == 0) break
                            println("found. ${color}: ${pos().map { it + 1 }.joinToString(",")}")

                            for (i in moveCount downTo 0) {
                                var dirFlagCount = 0
                                for (axis in 0..totalDirectionFlags.length - 1)
                                    if (totalDirectionFlags[axis] == '1') {
                                        if (scanningDirectionFlags[dirFlagCount] == '1') pos()[axis] -= moveNum
                                        else                                             pos()[axis] += moveNum
                                        dirFlagCount += 1
                                    }
                                board[pos] = Disc(color)
                                onFlip(color, pos)
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

    override fun toString(): String {
        val sBuilder = StringBuilder()

        for (i in board.indices) {
            sBuilder.append("${board[i]}, ")
            var mag = 1
            for (len in maxLengths) {
                mag *= len
                if ((i + 1) % mag == 0) sBuilder.append("\n")
            }
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

class OthelloGameServer(
  ints: Array<Int>
 ,colors: Array<String>
 ,onFlip: (color: String, coordinates: Coordinates) -> Unit
 ,val onTurn: (player: OthelloPlayer) -> Coordinates = { Coordinates(*readLine()!!.split(",").map { it.toInt()-1 }.toIntArray()) }
){
    val board = OthelloBoard(onFlip, *ints.toIntArray())
    val players = colors.map { OthelloPlayer(it) }.toTypedArray()

    fun initDrop(color: String, vararg lengths: Int) {
        board.initDrop(color, Coordinates(*lengths))
    }
    tailrec fun start() {
        debugPrintln(board.toString())
        players.forEach { turn(it) }
        if (board.isGameSet) return
        debugPrintln(board.toString())
        start()
    }

    tailrec fun turn(player: OthelloPlayer) {
        debugPrintln("turn: ${player.color}")
        val input = onTurn(player)
        val isDropped = board.drop(player.color, input)
        debugPrintln(board.toString())
        if (isDropped) return
        turn(player)
    }

    fun debugPrintln(string: String) {
        println(string)
    }
}

fun main(args: Array<String>) {

    val othello = OthelloGameServer(
            arrayOf(8, 8)
           ,arrayOf("white", "black")
           ,{ color, coordinates -> println() }
    )
    othello.initDrop("white", 3, 3)
    othello.initDrop("white", 4, 4)
    othello.initDrop("black", 3, 4)
    othello.initDrop("black", 4, 3)
    othello.start()
}