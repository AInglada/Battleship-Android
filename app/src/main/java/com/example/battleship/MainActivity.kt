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
    var board by remember { mutableStateOf(generateBoard()) }
    val hits = remember { mutableStateOf(Array(8) { Array(8) { false } }) }

    Column(modifier = Modifier.padding(16.dp)) {
        for (rowIndex in board.indices) {
            Row {
                for (colIndex in board[rowIndex].indices) {
                    val cell = board[rowIndex][colIndex]
                    val hit = hits.value[rowIndex][colIndex]
                    val color = when {
                        hit && cell -> Color.Red  // Impact
                        hit -> Color.White       // Miss
                        else -> Color.Blue       // Water
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color, RoundedCornerShape(4.dp))
                            .clickable {
                                hits.value[rowIndex][colIndex] = true
                                board = board.copyOf() // Force recomposition
                            }
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
