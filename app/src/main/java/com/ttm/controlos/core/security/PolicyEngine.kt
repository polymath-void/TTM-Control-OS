package com.ttm.controlos.core.security

import com.ttm.controlos.core.intent.TTMIntent

/**
 * PolicyEngine
 *
 * Central authorization system for all command execution.
 *
 * Responsibilities:
 * 1. Convert TTMIntent → required Capability
 * 2. Evaluate system policy rules
 *
 * This is the authoritative decision layer of the OS.
 */
object PolicyEngine {

    /**
     * Check if an intent is allowed to execute.
     */
    fun isAllowed(intent: TTMIntent): Boolean {
        val capability = mapIntentToCapability(intent)

        return evaluatePolicy(intent, capability)
    }

    /**
     * Map intents to required capabilities.
     */
    private fun mapIntentToCapability(intent: TTMIntent): Capability {
        return when (intent) {

            is TTMIntent.OpenApp ->
                Capability.OPEN_APP

            is TTMIntent.UninstallApp ->
                Capability.UNINSTALL_APP

            TTMIntent.ListApps ->
                Capability.READ_INSTALLED_APPS

            TTMIntent.ShowNotificationApps ->
                Capability.READ_NOTIFICATIONS

            is TTMIntent.Unknown ->
                Capability.UNKNOWN
        }
    }

    /**
     * Core policy evaluation logic.
     */
    private fun evaluatePolicy(
        intent: TTMIntent,
        capability: Capability
    ): Boolean {

        return when (capability) {

            Capability.OPEN_APP -> true

            Capability.READ_INSTALLED_APPS -> true

            Capability.READ_NOTIFICATIONS -> true

            Capability.UNINSTALL_APP -> {
                // Safety rule: block system apps uninstall by default
                if (intent is TTMIntent.UninstallApp) {
                    !isProtectedApp(intent.appName)
                } else {
                    false
                }
            }

            Capability.MODIFY_SYSTEM_SETTINGS -> false
            Capability.CONTROL_NETWORK -> false
            Capability.CONTROL_AUDIO -> false

            Capability.UNKNOWN -> false
        }
    }

    /**
     * System protection rules
     */
    private fun isProtectedApp(appName: String): Boolean {
        val protected = setOf(
            "system",
            "settings",
            "launcher",
            "phone",
            "messages",
            "android"
        )

        return protected.any {
            appName.lowercase().contains(it)
        }
    }

    /**
     * Optional debug reason (for UI layer later)
     */
    fun getDeniedReason(intent: TTMIntent): String {
        return when (intent) {
            is TTMIntent.UninstallApp ->
                "Uninstall blocked: system or protected application"

            is TTMIntent.Unknown ->
                "Unknown command"

            else ->
                "Operation not permitted by policy"
        }
    }
}
