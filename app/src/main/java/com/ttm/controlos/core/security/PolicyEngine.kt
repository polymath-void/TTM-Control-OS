package com.ttm.controlos.core.security

import com.ttm.controlos.core.intent.TTMIntent

/**
 * PolicyEngine
 *
 * Authority layer for capability-based system control.
 */
object PolicyEngine {

    fun isAllowed(intent: TTMIntent): Boolean {
        val capability = mapIntentToCapability(intent)
        return evaluatePolicy(intent, capability)
    }

    /**
     * Map intents → capabilities
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

            is TTMIntent.SetBrightness ->
                Capability.MODIFY_SYSTEM_SETTINGS

            is TTMIntent.SetVolume ->
                Capability.CONTROL_AUDIO

            is TTMIntent.Unknown ->
                Capability.UNKNOWN
        }
    }

    /**
     * Policy evaluation rules
     */
    private fun evaluatePolicy(
        intent: TTMIntent,
        capability: Capability
    ): Boolean {

        return when (capability) {

            Capability.OPEN_APP ->
                true

            Capability.READ_INSTALLED_APPS ->
                true

            Capability.READ_NOTIFICATIONS ->
                true

            Capability.UNINSTALL_APP -> {
                if (intent is TTMIntent.UninstallApp) {
                    !isProtectedApp(intent.appName)
                } else {
                    false
                }
            }

            Capability.MODIFY_SYSTEM_SETTINGS -> {
                intent is TTMIntent.SetBrightness &&
                        intent.value in 0..255
            }

            Capability.CONTROL_AUDIO -> {
                intent is TTMIntent.SetVolume &&
                        intent.value in 0..100
            }

            Capability.UNKNOWN ->
                false
        }
    }

    /**
     * System app protection
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
     * Debug reasons for UI layer
     */
    fun getDeniedReason(intent: TTMIntent): String {
        return when (intent) {

            is TTMIntent.UninstallApp ->
                "Uninstall blocked: protected system application"

            is TTMIntent.SetBrightness ->
                "Brightness out of allowed range (0–255)"

            is TTMIntent.SetVolume ->
                "Volume out of allowed range (0–100)"

            is TTMIntent.Unknown ->
                "Unknown command"

            else ->
                "Operation denied by policy engine"
        }
    }
}
