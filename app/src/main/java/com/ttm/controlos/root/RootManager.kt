package com.ttm.controlos.core.root

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Singleton manager for root shell lifecycle and command execution.
 * Utilizes libsu to maintain a persistent, thread-safe 'su' instance.
 */
object RootManager {

    init {
        // Configure the global shell environment upon first access.
        // Redirects STDERR to STDOUT to simplify log parsing.
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setTimeout(10)
        )
    }

    /**
     * Verifies if the underlying Linux system has an accessible 'su' binary
     * and if the user has granted root privileges to TTM-Control-OS.
     * * @return true if root is granted and the shell is active.
     */
    fun isRootAvailable(): Boolean {
        // This will block briefly on first call to request root from Magisk/KernelSU.
        return Shell.getShell().isRoot
    }

    /**
     * Executes a raw bash command or script in the root environment.
     * * @param command The POSIX-compliant shell command to execute.
     * @return Shell.Result containing the exit code, STDOUT, and STDERR.
     */
    suspend fun execute(command: String): Shell.Result = withContext(Dispatchers.IO) {
        Shell.cmd(command).exec()
    }
    
    /**
     * Terminates the persistent root shell. 
     * Useful for resource cleanup during application destruction.
     */
    fun closeShell() {
        Shell.getCachedShell()?.close()
    }
}
