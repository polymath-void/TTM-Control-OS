package com.ttm.controlos.core.parser

import com.ttm.controlos.core.intent.TTMIntent

/**
 * CommandParser (V1 Final)
 *
 * Converts raw user text into TTMIntent.
 *
 * Design goal:
 * - Minimal
 * - Stable
 * - No experimental logic
 */
object CommandParser {

    fun parse(input: String): TTMIntent {
        val text = input.lowercase().trim()

        return when {

            // -------------------------
            // APP CONTROL
            // -------------------------
            text.startsWith("open ") -> {
                TTMIntent.OpenApp(
                    appName = text.removePrefix("open ").trim()
                )
            }

            text.startsWith("uninstall ") ||
            text.startsWith("remove ") ||
            text.startsWith("delete ") -> {
                TTMIntent.UninstallApp(
                    appName = text
                        .replace("uninstall", "")
                        .replace("remove", "")
                        .replace("delete", "")
                        .trim()
                )
            }

            text == "list apps" || text == "apps" -> {
                TTMIntent.ListApps
            }

            text == "show notifications" || text == "notifications" -> {
                TTMIntent.ShowNotificationApps
            }

            // -------------------------
            // DEVICE CONTROL
            // -------------------------
            text.startsWith("set brightness ") -> {
                val value = text.removePrefix("set brightness ").trim().toIntOrNull()
                if (value != null) {
                    TTMIntent.SetBrightness(value)
                } else {
                    TTMIntent.Unknown(input)
                }
            }

            text.startsWith("set volume ") -> {
                val value = text.removePrefix("set volume ").trim().toIntOrNull()
                if (value != null) {
                    TTMIntent.SetVolume(value)
                } else {
                    TTMIntent.Unknown(input)
                }
            }

            // -------------------------
            // UNKNOWN
            // -------------------------
            else -> TTMIntent.Unknown(input)
        }
    }
}
