package com.example.battleship

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameMode = intent.getStringExtra("GAME_MODE") ?: "PvP"
        setContent {
            val viewModel: GameViewModel = viewModel()
            viewModel.setGameMode(gameMode) // <-- Nuevo
            BattleshipGame(viewModel = viewModel, onExit = { finish() })
        }
    }
}

@Composable
fun BattleshipGame(viewModel: GameViewModel, onExit: () -> Unit) {
    val currentPlayer by viewModel.currentPlayer
    val gameOver by viewModel.gameOver
    val winner by viewModel.winner

    Box(modifier = Modifier.fillMaxSize()) {
        if (gameOver) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Player $winner wins!",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Text("Player $currentPlayer's turn", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                BoardGrid(viewModel = viewModel)
            }
        }
        Button(
            onClick = onExit,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Exit")
        }
    }
}

@Composable
fun BoardGrid(viewModel: GameViewModel) {
    val (currentBoard, currentHits) = if (viewModel.currentPlayer.value == 1) {
        viewModel.player2Board.value to viewModel.player1Hits.value
    } else {
        viewModel.player1Board.value to viewModel.player2Hits.value
    }

    Column {
        currentBoard.indices.forEach { row ->
            Row {
                currentBoard[row].indices.forEach { col ->
                    val cell = currentBoard[row][col]
                    val hit = currentHits[row][col]
                    val color = when {
                        hit && cell -> Color.Red
                        hit -> Color.White
                        else -> Color.Blue
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color, RoundedCornerShape(4.dp))
                            .clickable(enabled = !hit) {
                                viewModel.handleCellClick(row, col)
                            }
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}