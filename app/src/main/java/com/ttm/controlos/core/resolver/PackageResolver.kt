package com.ttm.controlos.core.resolver

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

/**
 * PackageResolver
 *
 * Converts user-facing app names into real Android package names.
 *
 * Flow:
 * PackageManager → AppInfo list → search by name/alias → packageName
 */
object PackageResolver {

    private var cachedApps: List<AppInfo> = emptyList()

    /**
     * Load installed apps into memory cache.
     * Call this once during app startup.
     */
    fun loadInstalledApps(context: Context) {
        val pm = context.packageManager

        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        cachedApps = apps.mapNotNull { app ->
            val label = pm.getApplicationLabel(app).toString()

            AppInfo(
                appName = label,
                packageName = app.packageName,
                aliases = generateAliases(label),
                isSystemApp = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            )
        }
    }

    /**
     * Resolve user input into package name.
     *
     * Example:
     * "whatsapp" → "com.whatsapp"
     */
    fun resolvePackage(appName: String): String? {
        val query = appName.lowercase().trim()

        val match = cachedApps.firstOrNull { app ->
            app.appName.equals(query, ignoreCase = true) ||
            app.aliases.any { it.equals(query, ignoreCase = true) }
        }

        return match?.packageName
    }

    /**
     * Return full AppInfo if needed by executor/UI.
     */
    fun resolveApp(appName: String): AppInfo? {
        val query = appName.lowercase().trim()

        return cachedApps.firstOrNull { app ->
            app.appName.equals(query, ignoreCase = true) ||
            app.aliases.any { it.equals(query, ignoreCase = true) }
        }
    }

    /**
     * Simple alias generator (can be extended later).
     *
     * Example:
     * "WhatsApp Messenger" → ["whatsapp", "whatsapp messenger"]
     */
    private fun generateAliases(label: String): List<String> {
        return listOf(
            label.lowercase(),
            label.lowercase().replace(" ", ""),
            label.lowercase().split(" ").firstOrNull() ?: label.lowercase()
        ).distinct()
    }

    /**
     * Optional: return all cached apps (for UI listing)
     */
    fun getAllApps(): List<AppInfo> {
        return cachedApps
    }
}
