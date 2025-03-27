package com.example.battleship

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class ConfigurationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConfigurationScreen(
                onStartGame = { gameMode ->
                    val intent = Intent(this, GameActivity::class.java).apply {
                        putExtra("GAME_MODE", gameMode)
                    }
                    startActivity(intent)
                    finish() // Deletes this activity from back stack
                }
            )
        }
    }
}

@Composable
fun ConfigurationScreen(onStartGame: (String) -> Unit) {
    var gameMode by remember { mutableStateOf("PvP") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(R.string.game_settings), style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        RadioGroup(
            options = listOf(stringResource(R.string.player_vs_player), stringResource(R.string.player_vs_ai)),
            selectedOption = gameMode,
            onOptionSelected = { gameMode = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onStartGame(gameMode) }) {
            Text(stringResource(R.string.start_game))
        }
    }
}

@Composable
fun RadioGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onOptionSelected(option) }
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Text(text = option)
            }
        }
    }
}