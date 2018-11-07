package com.github.nnnnusui.othello

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class UDP(
  val port: Int
 ,val size: Int
){
    val channel = DatagramChannel.open()
    val byteBuffer = ByteBuffer.allocate(size)
    val address = "127.0.0.1"
    init {
        channel.socket().bind(InetSocketAddress(port))
    }
    fun receive() {
        byteBuffer.clear()
        channel.receive(byteBuffer)
        byteBuffer.flip()
        val getMessage = ByteArray(size)
        println("reveive:" + byteBuffer.get(getMessage))
        getMessage.map { it.toChar() }.forEach { print(it) }
    }
    fun send(message: ByteArray, port: Int) {
        byteBuffer.clear()
        byteBuffer.put(message)
        byteBuffer.flip()
        println("send:" + channel.send(byteBuffer, InetSocketAddress(address, port)))
    }
}


fun main(args: Array<String>) {
    val port = 9998
    val message = "testMessage".toByteArray()
    val one = UDP(port, message.size)
//    one.receive()
    one.send(message, 9999)
}


