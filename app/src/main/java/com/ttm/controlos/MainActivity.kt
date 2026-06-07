package com.ttm.controlos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.ttm.controlos.core.engine.CommandEngine
import com.ttm.controlos.core.resolver.PackageResolver
import com.ttm.controlos.core.system.SystemOutput
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var outputView: TextView
    private lateinit var inputBox: EditText
    private lateinit var sendButton: Button
    
    // Instance of the engine to manage state and logic
    private lateinit var commandEngine: CommandEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize Engine
        commandEngine = CommandEngine(this)

        // 2. Load Resources
        PackageResolver.loadInstalledApps(this)

        // 3. UI Setup
        outputView = TextView(this).apply {
            text = "TTM Control OS Initialized\nApps Cached: ${PackageResolver.getAllApps().size}"
            textSize = 14f
            setPadding(16, 16, 16, 16)
        }

        inputBox = EditText(this).apply {
            hint = "Type command (e.g. set brightness 80)"
        }

        sendButton = Button(this).apply {
            text = "Execute"
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(outputView)
            addView(inputBox)
            addView(sendButton)
        }

        setContentView(layout)

        // 4. Observe Engine Output (Reactive Flow)
        // lifecycleScope ensures this collection stops when the activity is destroyed
        lifecycleScope.launch {
            commandEngine.systemOutput.collect { output ->
                runOnUiThread {
                    outputView.append("\n> $output")
                }
            }
        }

        // 5. Execution Logic
        sendButton.setOnClickListener {
            val input = inputBox.text.toString()
            if (input.isNotBlank()) {
                commandEngine.processCommand(input)
                inputBox.text.clear()
            }
        }
    }
}
