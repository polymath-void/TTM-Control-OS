package com.ttm.controlos

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.ttm.controlos.core.executor.Executor
import com.ttm.controlos.core.intent.TTMIntent
import com.ttm.controlos.core.system.SystemOutput

class MainActivity : ComponentActivity() {

    private lateinit var outputView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        outputView = TextView(this).apply {
            textSize = 16f
            text = "TTM Control OS Ready\n"
        }

        setContentView(outputView)

        // CONNECT OUTPUT PIPELINE
        SystemOutput.callback = { msg ->
            runOnUiThread {
                outputView.append("\n$msg")
            }
        }

        // TEST COMMANDS (MVP DEMO)
        runDemo()
    }

    private fun runDemo() {

        Executor.execute(this, TTMIntent.ListApps)

        Executor.execute(this, TTMIntent.SetVolume(50))

        Executor.execute(this, TTMIntent.SetBrightness(120))

        Executor.execute(this, TTMIntent.OpenApp("whatsapp"))
    }
}
