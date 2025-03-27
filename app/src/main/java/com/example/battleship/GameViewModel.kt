package com.example.battleship

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*

class GameViewModel : ViewModel() {
    private val gameManager = GameManager()

    val player1Board = mutableStateOf(gameManager.generateBoard())
    val player2Board = mutableStateOf(gameManager.generateBoard())
    val player1Hits = mutableStateOf(gameManager.initializeHits())
    val player2Hits = mutableStateOf(gameManager.initializeHits())
    val currentPlayer = mutableStateOf(1)
    val gameOver = mutableStateOf(false)
    val winner = mutableStateOf(0)
    private var gameMode = "PvP"
    private val aiPlayer = AIPlayer()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun setGameMode(mode: String) {
        gameMode = mode
    }

    fun handleCellClick(row: Int, col: Int) {
        val (currentBoard, currentHits) = getCurrentBoardAndHits()
        if (!currentHits[row][col]) {
            currentHits[row][col] = true
            if (gameManager.isAllShipsSunk(currentBoard, currentHits)) {
                gameOver.value = true
                winner.value = currentPlayer.value
            } else {
                // Only AI if is Player vs AI
                if (currentPlayer.value == 1) {
                    currentPlayer.value = 2
                    scope.launch {
                        delay(1000)
                        makeAIMove()
                    }
                }
            }
        }
    }

    private fun makeAIMove() {
        val (currentBoard, currentHits) = getCurrentBoardAndHits()
        val (row, col) = aiPlayer.generateMove(currentHits)
        currentHits[row][col] = true

        if (gameManager.isAllShipsSunk(currentBoard, currentHits)) {
            gameOver.value = true
            winner.value = currentPlayer.value
        } else {
            currentPlayer.value = 1 // Turns to Player
        }
    }

    private fun getCurrentBoardAndHits(): Pair<Array<Array<Boolean>>, Array<Array<Boolean>>> {
        return if (currentPlayer.value == 1) {
            player2Board.value to player1Hits.value
        } else {
            player1Board.value to player2Hits.value
        }
    }
}