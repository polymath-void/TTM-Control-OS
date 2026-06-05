package com.ttm.controlos.core.executor

import android.content.Context
import android.content.Intent
import android.net.Uri

import com.ttm.controlos.core.intent.TTMIntent
import com.ttm.controlos.core.resolver.PackageResolver
import com.ttm.controlos.core.security.PolicyEngine

/**
 * Executor
 *
 * Final execution layer of the system.
 *
 * Responsibilities:
 * - Execute validated TTMIntent
 * - Perform Android system actions
 * - NEVER decide permission logic (handled by PolicyEngine)
 */
object Executor {

    /**
     * Entry point for all command execution.
     */
    fun execute(context: Context, intent: TTMIntent) {

        // POLICY CHECK (single source of truth)
        if (!PolicyEngine.isAllowed(intent)) {
            return
        }

        when (intent) {

            /**
             * OPEN APP
             */
            is TTMIntent.OpenApp -> {
                val packageName = PackageResolver.resolvePackage(intent.appName)

                if (packageName != null) {
                    val launchIntent =
                        context.packageManager.getLaunchIntentForPackage(packageName)

                    launchIntent?.apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(this)
                    }
                }
            }

            /**
             * UNINSTALL APP
             */
            is TTMIntent.UninstallApp -> {
                val packageName = PackageResolver.resolvePackage(intent.appName)

                if (packageName != null) {
                    val uninstallIntent = Intent(Intent.ACTION_DELETE).apply {
                        data = Uri.parse("package:$packageName")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    context.startActivity(uninstallIntent)
                }
            }

            /**
             * LIST APPS
             */
            TTMIntent.ListApps -> {
                // Future: connect to UI state layer
            }

            /**
             * SHOW NOTIFICATIONS
             */
            TTMIntent.ShowNotificationApps -> {
                // Future: connect to NotificationTrackingService
            }

            /**
             * UNKNOWN COMMAND
             */
            is TTMIntent.Unknown -> {
                // No-op (UI layer will handle feedback later)
            }
        }
    }
}
