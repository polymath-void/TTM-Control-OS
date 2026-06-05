package com.ttm.controlos.core.intent

/**
 * Canonical command intents for TTM Control OS.
 *
 * All user commands MUST be converted into one of these intents
 * before reaching PolicyEngine and Executor.
 */
sealed class TTMIntent {

    /**
     * Open an installed application.
     */
    data class OpenApp(
        val appName: String
    ) : TTMIntent()

    /**
     * Uninstall an application.
     */
    data class UninstallApp(
        val appName: String
    ) : TTMIntent()

    /**
     * List all installed apps.
     */
    object ListApps : TTMIntent()

    /**
     * Show apps that have active notifications.
     */
    object ShowNotificationApps : TTMIntent()

    // -----------------------------
    // DEVICE CONTROL INTENTS (NEW)
    // -----------------------------

    /**
     * Set screen brightness (0–255 or normalized later in parser)
     */
    data class SetBrightness(
        val value: Int
    ) : TTMIntent()

    /**
     * Set media volume (0–100 percentage)
     */
    data class SetVolume(
        val value: Int
    ) : TTMIntent()

    /**
     * Unknown or unsupported command
     */
    data class Unknown(
        val rawInput: String
    ) : TTMIntent()
}
