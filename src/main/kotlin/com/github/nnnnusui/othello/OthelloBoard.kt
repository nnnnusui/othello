package com.github.nnnnusui.othello

import com.github.nnnnusui.anydimensional.Coordinates
import com.github.nnnnusui.anydimensional.Space
import java.lang.Exception
import java.util.*

class OthelloBoard(
        maxLengths: IntArray
       ,val onFlip: (color: String, coordinates: Coordinates) -> Unit = {color, coordinates -> }
){
    val INITIAL_DISC_COLOR = "none "
    val initDisc = Disc(INITIAL_DISC_COLOR)

    val board = Space<Disc>(Coordinates(*maxLengths), { initDisc })
    var isGameSet = false

    init { println(" maxLengths: ${maxLengths.map { it }.joinToString(",")}") }

    fun initDrop(color: String, coordinates: Coordinates) {
        board[coordinates] = Disc(color)
        onFlip(color, coordinates)
    }
    fun drop(color: String, coordinates: Coordinates): Boolean {
        search(color, coordinates)
        val dropSquare = board[coordinates]
        if (dropSquare.color != color) return false
        return true
    }
    fun search(color: String, coordinates: Coordinates) {
        if (board[coordinates].color != INITIAL_DISC_COLOR) return
        board.getDirectionList().forEachIndexed { index, direction ->
            println("index: ${index}")
            var scanningCoordinates = coordinates.clone()
            scanningCoordinates += direction
            var scanCount = 0
            while (board.isIncluding(scanningCoordinates)){
                   println("search. ${color}: ${scanningCoordinates().map { it + 1 }.joinToString(",")}")
                val square = board[scanningCoordinates]
                if (square.color == INITIAL_DISC_COLOR) break
                if (square.color == color) { //flip
                    if (scanCount == 0) break
                    println("found. ${color}: ${scanningCoordinates().map { it + 1 }.joinToString(",")}")
                    for (i in scanCount downTo 0) {
                        scanningCoordinates -= direction
                        board[scanningCoordinates] = Disc(color)
                    }
                    break
                }
                scanningCoordinates += direction
                scanCount += 1
            }
        }
    }

    override fun toString(): String {
        return board.toString().replace("^Space".toRegex(), "OthelloBoard")
    }
}

class OthelloPlayer(val color: String) {

    fun tryRequestCoordinates(dimension: Int): Optional<Coordinates> {
        return try {
            val coordinates = Coordinates(*readLine()!!.split(',').map { it.toInt()-1 }.reversed().toIntArray())
            if (coordinates.dimension != dimension)
                Optional.empty()
            else
                Optional.of(coordinates)
        } catch (e: Exception) {
            Optional.empty()
        }
    }
}

class OthelloGameLoop(val board: OthelloBoard, val playerArray: Array<OthelloPlayer>){

    fun initDrop(color: String, vararg ints: Int) {
        board.initDrop(color, Coordinates(*ints.map { it-1 }.toIntArray()))
    }
    tailrec fun start() {
            println(board)
        playerArray.forEach { turn(it) }
        if (board.isGameSet) return
        start()
    }
    tailrec fun turn(player: OthelloPlayer) {
        println("turn: ${player.color}")
        val optCoord = player.tryRequestCoordinates(board.board.dimension)
        val isDropped =
            if (optCoord.isPresent)
                board.drop(player.color, optCoord.get())
            else false
        if (isDropped) return
        println("drop failure. try again.")
        turn(player)
    }
}

fun main(args: Array<String>) {
    val white = "white"
    val black = "black"
    val board = OthelloBoard(arrayOf(4, 4, 4).toIntArray(), {str, coord -> })
    val othello = OthelloGameLoop(board, arrayOf(OthelloPlayer(white), OthelloPlayer(black)))
    othello.initDrop(black, 2, 2, 2)
    othello.initDrop(black, 3, 3, 2)
    othello.initDrop(white, 2, 3, 2)
    othello.initDrop(white, 3, 2, 2)

    othello.initDrop(white, 2, 2, 3)
    othello.initDrop(white, 3, 3, 3)
    othello.initDrop(black, 2, 3, 3)
    othello.initDrop(black, 3, 2, 3)
    othello.start()
}
fun getCoord(vararg ints: Int): Coordinates
    = Coordinates(*ints)