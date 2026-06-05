package com.ttm.controlos.core.parser

import com.ttm.controlos.core.intent.TTMIntent

object CommandParser {

    fun parse(input: String): CommandResult {

        val cmd = input.lowercase().trim()

        val intent: TTMIntent? = when {

            cmd.startsWith("open ") ->
                TTMIntent.OpenApp(cmd.removePrefix("open ").trim())

            cmd.startsWith("uninstall ") ->
                TTMIntent.UninstallApp(cmd.removePrefix("uninstall ").trim())

            cmd.startsWith("set brightness ") -> {
                val value = cmd.removePrefix("set brightness ").trim().toIntOrNull()
                value?.let { TTMIntent.SetBrightness(it) }
            }

            cmd.startsWith("set volume ") -> {
                val value = cmd.removePrefix("set volume ").trim().toIntOrNull()
                value?.let { TTMIntent.SetVolume(it) }
            }

            cmd == "list apps" ->
                TTMIntent.ListApps

            cmd == "notifications" ->
                TTMIntent.ShowNotificationApps

            else -> null
        }

        return if (intent != null) {
            CommandResult.Success(intent)
        } else {
            CommandResult.Error("Invalid command: $input")
        }
    }
}
