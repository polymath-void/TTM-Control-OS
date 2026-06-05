package com.ttm.controlos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.ttm.controlos.core.executor.Executor
import com.ttm.controlos.core.parser.CommandParser
import com.ttm.controlos.core.system.SystemOutput

class MainActivity : ComponentActivity() {

    private lateinit var outputView: TextView
    private lateinit var inputBox: EditText
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        outputView = TextView(this).apply {
            text = "TTM Control OS Ready\n"
            textSize = 14f
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

        SystemOutput.callback = { msg ->
            runOnUiThread {
                outputView.append("\n$msg")
            }
        }

        sendButton.setOnClickListener {
            val input = inputBox.text.toString()
            handleCommand(input)
            inputBox.text.clear()
        }
    }

    private fun handleCommand(input: String) {

        val intent = CommandParser.parse(input)

        if (intent == null) {
            SystemOutput.send("Invalid command: $input")
            return
        }

        Executor.execute(this, intent)
    }
}
