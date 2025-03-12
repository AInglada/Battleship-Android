package com.example.battleship
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val board = remember { generateBoard() }
    Column(modifier = Modifier.padding(16.dp)) {
        for (row in board) {
            Row {
                for (cell in row) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(if (cell) Color.Gray else Color.Blue, RoundedCornerShape(4.dp))
                            .padding(2.dp)
                    )
                }
            }
        }
    }
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
    while (!placed) {
        val x = Random.nextInt(size)
        val y = Random.nextInt(size)
        val horizontal = Random.nextBoolean()
        if (canPlaceShip(board, x, y, length, horizontal)) {
            for (i in 0 until length) {
                if (horizontal) board[x + i][y] = true else board[x][y + i] = true
            }
            placed = true
        }
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

        for (dx in -1..1) {
            for (dy in -1..1) {
                val nx = xi + dx
                val ny = yi + dy
                if (nx in 0 until size && ny in 0 until size && board[nx][ny]) {
                    return false
                }
            }
        }
    }
    return true
}
