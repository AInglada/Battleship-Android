package com.example.battleship

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainMenuScreen(
                onPlayClicked = { startActivity(Intent(this, ConfigurationActivity::class.java)) },
                onHelpClicked = { startActivity(Intent(this, HelpActivity::class.java)) },
                onExitClicked = { finishAffinity() }
            )
        }
    }
}

@Composable
fun MainMenuScreen(
    onPlayClicked: () -> Unit,
    onHelpClicked: () -> Unit,
    onExitClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Battleship", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(48.dp))

        Button(onClick = onPlayClicked, modifier = Modifier.width(200.dp)) {
            Text(stringResource(R.string.play))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onHelpClicked, modifier = Modifier.width(200.dp)) {
            Text(stringResource(R.string.help))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onExitClicked, modifier = Modifier.width(200.dp)) {
            Text(stringResource(R.string.exit))
        }
    }
}