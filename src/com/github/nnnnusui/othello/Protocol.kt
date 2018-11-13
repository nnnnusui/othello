package com.github.nnnnusui.othello

enum class Protocol(val message: String){
    CONNECT("connect.")
    ,JOIN("join")
    ,JOIN_HOST("join_host")
    ,START("game_start")
    ,END("game_set")
    ,FLIP("flip")
    ,TURN("your turn")
}
