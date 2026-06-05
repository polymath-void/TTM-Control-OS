package com.ttm.controlos.core.resolver

/**
 * Represents a single installed application in the system registry.
 *
 * This model is used by:
 * - PackageResolver
 * - CommandParser (future alias matching)
 * - Executor (app launch / uninstall)
 *
 * Design goal:
 * Normalize all app-related operations into structured data
 * instead of raw string matching.
 */
data class AppInfo(

    /**
     * Human-readable app name.
     * Example: "WhatsApp", "Chrome"
     */
    val appName: String,

    /**
     * System package name.
     * Example: "com.whatsapp"
     */
    val packageName: String,

    /**
     * Optional list of aliases for command matching.
     * Example: ["wa", "whatsapp messenger", "chat app"]
     */
    val aliases: List<String> = emptyList(),

    /**
     * Optional flag for system apps.
     * Used later for permission restrictions.
     */
    val isSystemApp: Boolean = false
)
