package com.github.nnnnusui.othello

import com.github.nnnnusui.anydimensional.Coordinates
//import com.github.nnnnusui.server.TcpServer
//import com.github.nnnnusui.server.WorkerThread
import java.net.Socket
import kotlin.concurrent.thread


class Disc(
  val color: String
  ){
    override fun toString(): String {
        return color
    }
}

//open class OthelloPlayer(
//  val color: String
// ,val socket: Socket
//  ){
//    val output = socket.getOutputStream()
//    val input = socket.getInputStream()
//    val newLine get() = input.bufferedReader().readLine()
//
//    fun requestDropCoord(): Coordinates {
//        Protocol.TURN.message.forEach { output.write(it.toInt()) }
//        return Coordinates(*newLine.split(',').map { it.toInt() }.toIntArray())
//    }
//    fun noticeFlip(color: String, coordinates: Coordinates) {
//        "${color}_${coordinates}".forEach { output.write(it.toInt()) }
//    }
//    fun newDisc(): Disc {
//        return Disc(color)
//    }
//}
//class OthelloHost(
//  color: String
// ,socket: Socket
// ,val server: TcpServer
//  ): OthelloPlayer(color, socket) {
//
//    fun waitOrder() {
//        (readLine()+'\n').forEach { output.write(it.toInt()) }
////        println(method.message)
////        hostPlayer.gameStart {
////        }
//    }
//
//    inline fun gameStart(action: () -> Unit) {
//        action()
//    }
//}

//class OthelloGameServer(){
//    val server = TcpServer(8080)
//    val playerList = mutableListOf<OthelloPlayer>()
//
//    fun requestParser(request: String) {
//        val header = request.substringBefore(':')
//        val message = request.substringAfter(':')
//        val order = Protocol.valueOf(header)
//        when(order) {
//            Protocol.JOIN_HOST -> { joinHost() }
//            Protocol.JOIN -> { join() }
//            Protocol.START -> {  }
//        }
//    }
//    fun joinHost() {
//        val boardSize = arrayOf(8, 8)
//        val board = OthelloBoard({ color, coordinates ->
//            playerList.forEach { it.noticeFlip(color, coordinates) }
//        }, *boardSize.toIntArray())
//
////        board.initDrop("white", 3, 3)
////        board.initDrop("white", 4, 4)
////        board.initDrop("black", 3, 4)
////        board.initDrop("black", 4, 3)
//
//    }
//    fun join() {
//
//    }
//}

/*
 * Server
 * Client -> Server [ join(color) ]
 * HostClient -> Server [ start ]
 * Server.get(OthelloBoard)
 * Server -> Client [ board.space ]
 * loop {
 *   Server -> Client [ dropRequest ]
 *   Client -> Server [ drop(coordinates) ]
 *   Server -> Clients [ flip(color, coordinates) ]
 * }
 * Server -> Clients [ gameSet ]
 * close()
 */

//class OthelloGameLoop(
//  ints: Array<Int>
// ,val othelloPlayerList: Array<OthelloPlayer>
//  ){
//    val board = OthelloBoard({ color, coordinates ->
//        othelloPlayerList.forEach { it.noticeFlip(color, coordinates) }
//    }, *ints.toIntArray())
//
//    fun initDrop(color: String, vararg lengths: Int) {
//        board.initDrop(color, Coordinates(*lengths))
//    }
//    tailrec fun start() {
//        debugPrintln(board.toString())
//        othelloPlayerList.forEach { turn(it) }
//        debugPrintln(board.toString())
//        if (board.isGameSet) return
//        start()
//    }
//
//    tailrec fun turn(player: OthelloPlayer) {
//        debugPrintln("turn: ${player.color}")
//        val request = player.requestDropCoord()
//        val isDropped = board.drop(player.color, request)
//        debugPrintln(board.toString())
//        if (isDropped) return
//
//        turn(player)
//    }
//
//    fun debugPrintln(string: String) { println(string) }
//}




//class OthelloServerWorkerThread: WorkerThread {
//    override fun run(socket: Socket) {}
//}

fun main(args: Array<String>) {
    val port = 8080
    val boardSize = arrayOf(8, 8)

    val client = OthelloClient("localhost", port)
    client.run()
    return

    val playerList = mutableListOf<OthelloPlayer>()

//    val server = TcpServer(port)
//    thread {
//        server.start {
//            val socket = it
//            val request = socket.getInputStream().bufferedReader().readLine()
//            val header = request.substringBefore(':')
//            val method = Protocol.valueOf(header)
//            val message = request.substringAfter(':')
//            when (method) {
//                Protocol.JOIN_HOST -> {
//                }
//                Protocol.JOIN -> {
//                    playerList.add(OthelloPlayer(message, socket))
//                    playerList.forEach { println("${it.color}") }
//                }
//                Protocol.START -> {
//                    socket.close()
//                }
//            }
//        }
//    }
//    val color = "white"
//    val hostSocket = Socket("localhost", port)
//    val hostPlayer = OthelloHost(color, hostSocket, server)
//    playerList.add(hostPlayer)
//    hostPlayer.waitOrder()
//    return


//    val othello = OthelloGameServer(
//             boardSize
//            , playerList.toTypedArray()
//            , { color, coordinates -> println() }
//    )
//    othello.initDrop("white", 3, 3)
//    othello.initDrop("white", 4, 4)
//    othello.initDrop("black", 3, 4)
//    othello.initDrop("black", 4, 3)
//    othello.start()
}
