package com.ttm.controlos.core.executor

import android.content.Context
import com.ttm.controlos.core.intent.TTMIntent
import com.ttm.controlos.core.root.RootExecutor
import com.ttm.controlos.core.security.PolicyEngine
import com.ttm.controlos.core.security.RootPolicyEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RootCommandRouter(private val context: Context) {

    sealed class RouterResult {
        data class Success(val info: String) : RouterResult()
        data class Failure(val errorReason: String) : RouterResult()
    }

    suspend fun route(intent: TTMIntent): RouterResult = withContext(Dispatchers.IO) {
        // 1. Framework-level Policy Check (Your existing PolicyEngine)
        if (!PolicyEngine.isAllowed(intent)) {
            return@withContext RouterResult.Failure(PolicyEngine.getDeniedReason(intent))
        }

        // 2. Routing based on intent
        when (intent) {
            is TTMIntent.UninstallApp -> handleRootUninstall(intent.appName)
            is TTMIntent.SetBrightness -> handleRootSetting("screen_brightness", intent.value)
            is TTMIntent.SetVolume -> handleRootSetting("volume_music", intent.value)
            // ... add others
            else -> RouterResult.Failure("Intent mapped to non-root execution.")
        }
    }

    private suspend fun handleRootUninstall(pkg: String): RouterResult {
        // 3. Deep Root-level Protection Check
        if (!RootPolicyEngine.isUninstallAllowed(pkg)) {
            return RouterResult.Failure("Root security violation: Package is protected.")
        }
        
        val result = RootExecutor.silentUninstall(pkg)
        return if (result.isSuccess) RouterResult.Success("Success") else RouterResult.Failure("Root failed")
    }

    private suspend fun handleRootSetting(key: String, value: Int): RouterResult {
        // 3. Deep Root-level Protection Check
        if (!RootPolicyEngine.isSettingBoundValid(key, value)) {
            return RouterResult.Failure("Root constraint violation: value out of bounds.")
        }

        val result = RootExecutor.setSystemSetting("system", key, value)
        return if (result.isSuccess) RouterResult.Success("Applied $key") else RouterResult.Failure("Root failed")
    }
}
