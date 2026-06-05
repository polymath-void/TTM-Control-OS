package com.ttm.controlos.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.ttm.controlos.core.executor.Executor
import com.ttm.controlos.core.parser.CommandParser

/**
 * CommandScreen
 *
 * Primary UI layer for entering system commands.
 *
 * Flow:
 * User Input → CommandParser → TTMIntent → Executor
 *
 * This UI is intentionally minimal because:
 * - Vue animation layer may replace visuals later
 * - Kotlin handles all execution logic
 */
@Composable
fun CommandScreen(context: Context) {

    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "TTM Control OS",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter command...") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val intent = CommandParser.parse(input)

                Executor.execute(context, intent)

                output = "Executed: $intent"
                input = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Run Command")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = output,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
