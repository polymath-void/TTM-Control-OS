package com.ttm.controlos.core.security

import com.ttm.controlos.core.intent.TTMIntent

/**
 * PermissionGate
 *
 * Central safety firewall for command execution.
 */
object PermissionGate {

    fun isAllowed(intent: TTMIntent): Boolean {
        return when (intent) {

            /**
             * SAFE READ OPERATIONS
             */
            TTMIntent.ListApps,
            TTMIntent.ShowNotificationApps -> true

            /**
             * APP OPERATIONS
             */
            is TTMIntent.OpenApp -> true

            is TTMIntent.UninstallApp -> {
                isAllowedToUninstall(intent.appName)
            }

            /**
             * DEVICE CONTROL
             */
            is TTMIntent.SetBrightness -> {
                isAllowedBrightness(intent.value)
            }

            is TTMIntent.SetVolume -> {
                isAllowedVolume(intent.value)
            }

            /**
             * UNKNOWN COMMANDS
             */
            is TTMIntent.Unknown -> false
        }
    }

    /**
     * Protect system-critical apps
     */
    private fun isAllowedToUninstall(appName: String): Boolean {

        val protectedApps = setOf(
            "system",
            "settings",
            "launcher",
            "phone",
            "messages"
        )

        return protectedApps.none {
            appName.lowercase().contains(it)
        }
    }

    /**
     * Brightness safety boundary
     */
    private fun isAllowedBrightness(value: Int): Boolean {
        return value in 0..255
    }

    /**
     * Volume safety boundary
     */
    private fun isAllowedVolume(value: Int): Boolean {
        return value in 0..100
    }

    /**
     * Optional UI feedback layer
     */
    fun getRejectionReason(intent: TTMIntent): String? {
        return when (intent) {

            is TTMIntent.UninstallApp ->
                "This app is protected from uninstall operations"

            is TTMIntent.Unknown ->
                "Unknown command"

            is TTMIntent.SetBrightness ->
                "Brightness value out of safe range (0–255)"

            is TTMIntent.SetVolume ->
                "Volume value out of safe range (0–100)"

            else -> null
        }
    }
}
