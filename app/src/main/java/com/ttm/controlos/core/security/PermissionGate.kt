package com.ttm.controlos.core.security

import com.ttm.controlos.core.intent.TTMIntent

/**
 * PermissionGate
 *
 * Central safety layer that decides whether an intent is allowed
 * to be executed by the system.
 *
 * This is NOT a UI permission system.
 * This is a command execution firewall.
 */
object PermissionGate {

    /**
     * Main authorization check.
     *
     * Returns true if the intent is safe to execute.
     */
    fun isAllowed(intent: TTMIntent): Boolean {
        return when (intent) {

            // Always safe read operations
            TTMIntent.ListApps,
            TTMIntent.ShowNotificationApps -> true

            // App opening is safe
            is TTMIntent.OpenApp -> true

            // Uninstall is restricted (basic protection rule)
            is TTMIntent.UninstallApp -> {
                isAllowedToUninstall(intent.appName)
            }

            // Unknown commands are blocked
            is TTMIntent.Unknown -> false
        }
    }

    /**
     * Restrict uninstall operations.
     *
     * Add system-critical app protection here.
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
     * Optional: reason for denial (for UI feedback later)
     */
    fun getRejectionReason(intent: TTMIntent): String? {
        return when (intent) {
            is TTMIntent.UninstallApp ->
                "This app is protected from uninstall operations"

            is TTMIntent.Unknown ->
                "Unknown command"

            else -> null
        }
    }
}
