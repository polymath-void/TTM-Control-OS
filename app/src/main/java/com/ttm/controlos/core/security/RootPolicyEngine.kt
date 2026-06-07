package com.ttm.controlos.core.security

import com.ttm.controlos.core.root.RootManager

/**
 * Validates system control execution payloads against security protection invariants.
 * Located within the core/security package block.
 */
object RootPolicyEngine {

    // System packages that are strictly protected from modification or deletion.
    private val PROTECTED_PACKAGES = setOf(
        "android",
        "com.android.systemui",
        "com.android.settings",
        "com.android.vending",
        "com.google.android.gms"
    )

    /**
     * Evaluates whether a package removal operation is structurally safe.
     * * @param packageName The package identifier intended for removal.
     * @return Boolean true if the application can safely be manipulated.
     */
    fun isUninstallAllowed(packageName: String): Boolean {
        if (packageName.isBlank()) return false
        
        // Block modification if package matches critical infrastructure
        if (PROTECTED_PACKAGES.contains(packageName.trim().lowercase())) {
            return false
        }
        
        return true
    }

    /**
     * Verifies system operational status to ensure root execution loops can run.
     * * @return Boolean true if root capability is confirmed.
     */
    fun verifyExecutionSafety(): Boolean {
        return try {
            RootManager.isRootAvailable()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Validates hardware configuration bounds before updating OS configuration tables.
     * * @param key The target system setting key (e.g., screen_brightness).
     * @param value The raw numerical integer bound for evaluation.
     * @return Boolean true if parameters are within absolute hardware limits.
     */
    fun isSettingBoundValid(key: String, value: Int): Boolean {
        return when (key.lowercase()) {
            "screen_brightness" -> value in 0..255
            "volume_music" -> value in 0..15
            else -> true
        }
    }
}
