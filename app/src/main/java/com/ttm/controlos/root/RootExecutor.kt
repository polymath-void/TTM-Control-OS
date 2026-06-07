package com.ttm.controlos.core.root

import android.util.Log

/**
 * Concrete executor that translates parsed control instructions into 
 * elevated Android shell commands.
 */
object RootExecutor {
    private const val TAG = "RootExecutor"

    /**
     * Data class to encapsulate the result of an elevated system command.
     */
    data class ExecutionResult(
        val isSuccess: Boolean,
        val exitCode: Int,
        val output: List<String>
    )

    /**
     * Executes an operation via RootManager and structures the return payload.
     */
    private suspend fun runElevated(command: String): ExecutionResult {
        return try {
            val result = RootManager.execute(command)
            ExecutionResult(
                isSuccess = result.isSuccess,
                exitCode = result.code,
                output = result.out
            )
        } catch (e: Exception) {
            Log.e(TAG, "Root execution failed for command: $command", e)
            ExecutionResult(
                isSuccess = false,
                exitCode = -1,
                output = listOf(e.localizedMessage ?: "Unknown execution error")
            )
        }
    }

    /**
     * Silently uninstalls an application bypassing user confirmations.
     * Maps to: pm uninstall <package_name>
     */
    suspend fun silentUninstall(packageName: String): ExecutionResult {
        // Safe sanitation check to prevent malicious command injection strings
        val sanitizedPkg = packageName.replace(Regex("[^a-zA-Z0-9._]"), "")
        return runElevated("pm uninstall $sanitizedPkg")
    }

    /**
     * Lists all installed packages on the system directly from the package manager daemon.
     * Maps to: pm list packages
     */
    suspend fun getInstalledPackages(): List<String> {
        val result = runElevated("pm list packages")
        return if (result.isSuccess) {
            result.output.map { it.removePrefix("package:") }
        } else {
            emptyList()
        }
    }

    /**
     * Modifies Android System/Secure/Global settings tables directly.
     * Maps to: settings put <namespace> <key> <value>
     */
    suspend fun setSystemSetting(table: String, key: String, value: Int): ExecutionResult {
        val sanitizedTable = when (table.lowercase()) {
            "secure" -> "secure"
            "global" -> "global"
            else -> "system"
        }
        val sanitizedKey = key.replace(Regex("[^a-zA-Z0-9_]"), "")
        return runElevated("settings put $sanitizedTable $sanitizedKey $value")
    }
}
