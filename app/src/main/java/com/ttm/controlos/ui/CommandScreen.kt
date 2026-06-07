package com.ttm.controlos.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ttm.controlos.core.engine.CommandEngine
import com.ttm.controlos.core.system.SystemOutput

@Composable
fun CommandScreen() {
    val context = LocalContext.current
    // Instantiate engine once for the lifecycle of the screen
    val engine = remember { CommandEngine(context) }
    val systemOutput by engine.systemOutput.collectAsState()
    
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "TTM Control OS (Root Enabled)", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter command (e.g., 'uninstall <pkg>')") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                engine.processCommand(input)
                input = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Run Root Command")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Observe reactive states
        when (val state = systemOutput) {
            is SystemOutput.Idle -> Text("Ready.")
            is SystemOutput.Processing -> Text("Status: ${state.status}", color = MaterialTheme.colorScheme.primary)
            is SystemOutput.Success -> Text("Result: ${state.message}", color = MaterialTheme.colorScheme.tertiary)
            is SystemOutput.Error -> Text("Error: ${state.reason}", color = MaterialTheme.colorScheme.error)
        }
    }
}
