package com.github.nnnnusui.othello

import java.net.Socket

class OthelloClient(address: String, port: Int){
    private val socket = Socket(address, port)
    val output = socket.getOutputStream()
    val input = socket.getInputStream().bufferedReader()

    fun run() {
        val request = readLine()!!
        val header = request.substringBefore(':')
        val message = request.substringAfter(':')
        while (!socket.isClosed) {
            val protocol = Protocol.valueOf(header)

//            val from = input.readLine()
//            println("fromSERVER(${socket.inetAddress}): ${from}")
//            if (from == Protocol.TURN.message) {
//                val to = readLine()
//                (to + "\n").forEach { output.write(it.toInt()) }
//            }
        }
        println("END")
    }

    fun join(color: String) {
        val mes = "${Protocol.JOIN.message}:${color}"
    }

    fun order(request: String) {
        val header = request.substringBefore(':')
        val message = request.substringAfter(':')
        val protocol = Protocol.valueOf(header)
        when (protocol) {
            Protocol.START -> {

            }
            Protocol.FLIP -> {
                (readLine()+'\n').forEach { output.write(it.toInt()) }
            }
            Protocol.END -> {
                socket.close()
            }
        }
    }
}
