package com.example.battleship

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleshipGame()
        }
    }
}

@Composable
fun BattleshipGame() {
    var player1Board by remember { mutableStateOf(generateBoard()) }
    var player2Board by remember { mutableStateOf(generateBoard()) }
    var player1Hits by remember { mutableStateOf(initializeHits()) }
    var player2Hits by remember { mutableStateOf(initializeHits()) }
    var currentPlayer by remember { mutableStateOf(1) }
    var gameOver by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf(0) }

    val (currentBoard, currentHits) = getCurrentBoardAndHits(currentPlayer, player1Board, player2Board, player1Hits, player2Hits)

    if (gameOver) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(id = R.string.player_wins, winner), style = MaterialTheme.typography.headlineLarge)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(id = R.string.player_turn, currentPlayer), style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                if (isAllShipsSunk(currentBoard, currentHits)) {
                    gameOver = true
                    winner = currentPlayer
                } else {
                    for (row in currentBoard.indices) {
                        Row {
                            for (col in currentBoard[row].indices) {
                                val cell = currentBoard[row][col]
                                val hit = currentHits[row][col]
                                val color = when {
                                    hit && cell -> Color.Red // Hit
                                    hit -> Color.White       // Miss
                                    else -> Color.Blue       // Water
                                }

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(color, RoundedCornerShape(4.dp))
                                        .clickable(enabled = !hit) {
                                            currentHits[row][col] = true
                                            currentPlayer = if (currentPlayer == 1) 2 else 1
                                        }
                                        .padding(2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getCurrentBoardAndHits(
    currentPlayer: Int,
    player1Board: Array<Array<Boolean>>, player2Board: Array<Array<Boolean>>,
    player1Hits: Array<Array<Boolean>>, player2Hits: Array<Array<Boolean>>
): Pair<Array<Array<Boolean>>, Array<Array<Boolean>>> {
    return if (currentPlayer == 1) player2Board to player1Hits else player1Board to player2Hits
}

fun initializeHits(): Array<Array<Boolean>> {
    return Array(8) { Array(8) { false } }
}

fun generateBoard(): Array<Array<Boolean>> {
    val size = 8
    val board = Array(size) { Array(size) { false } }
    val ships = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)

    for (ship in ships) {
        placeShip(board, ship)
    }
    return board
}

fun placeShip(board: Array<Array<Boolean>>, length: Int) {
    val size = board.size
    var placed = false
    var attempts = 0
    while (!placed && attempts < 100) {
        val x = Random.nextInt(size)
        val y = Random.nextInt(size)
        val horizontal = Random.nextBoolean()
        if (canPlaceShip(board, x, y, length, horizontal)) {
            for (i in 0 until length) {
                if (horizontal) board[x + i][y] = true else board[x][y + i] = true
            }
            placed = true
        }
        attempts++
    }
}

fun canPlaceShip(board: Array<Array<Boolean>>, x: Int, y: Int, length: Int, horizontal: Boolean): Boolean {
    val size = board.size
    if (horizontal && x + length > size) return false
    if (!horizontal && y + length > size) return false

    for (i in 0 until length) {
        val xi = if (horizontal) x + i else x
        val yi = if (horizontal) y else y + i
        if (board[xi][yi]) return false

        if (!isSpaceFree(board, xi, yi, size)) return false
    }
    return true
}

fun isSpaceFree(board: Array<Array<Boolean>>, x: Int, y: Int, size: Int): Boolean {
    for (dx in -1..1) {
        for (dy in -1..1) {
            val nx = x + dx
            val ny = y + dy
            if (nx in 0 until size && ny in 0 until size && board[nx][ny]) {
                return false
            }
        }
    }
    return true
}

fun isAllShipsSunk(board: Array<Array<Boolean>>, hits: Array<Array<Boolean>>): Boolean {
    return board.indices.all { row ->
        board[row].indices.all { col ->
            !board[row][col] || hits[row][col]
        }
    }
}