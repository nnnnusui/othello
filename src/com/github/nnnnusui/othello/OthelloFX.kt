package com.github.nnnnusui.othello

import javafx.application.Application
import javafx.beans.property.Property
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

//class OthelloFX: Application() {
//    override fun start(primaryStage: Stage) {
//
//        val dimensionInputArea = TextArea("\",\"で区切ってボードのサイズを指定してね")
//        val playerColorsInputArea = TextArea("\",\"で区切ってプレイヤーの色を列挙してね")
//        val startButton = Button()
//
//        val pane = BorderPane(startButton)
//
//
//        startButton.setOnAction {
//            val playerColors = playerColorsInputArea.text.split(",").toTypedArray()
//            val dims = dimensionInputArea.text.split(",").map { it.toInt() }.toTypedArray()
//            val othelloGame = OthelloGame(playerColors,dims)
//            othelloGame.start()
//        }
//    }
//}


//fun main(args: Array<String>) {
//    Application.launch(OthelloFX::class.java)
//}