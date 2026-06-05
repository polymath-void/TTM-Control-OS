package com.ttm.controlos.core.executor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

import com.ttm.controlos.core.intent.TTMIntent
import com.ttm.controlos.core.resolver.PackageResolver
import com.ttm.controlos.core.security.PolicyEngine

/**
 * Executor
 *
 * Final execution layer of the system.
 */
object Executor {

    fun execute(context: Context, intent: TTMIntent) {

        // Single security gate
        if (!PolicyEngine.isAllowed(intent)) return

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
                // TODO: connect to UI state / repository
            }

            /**
             * SHOW NOTIFICATIONS
             */
            TTMIntent.ShowNotificationApps -> {
                // TODO: connect NotificationTrackingService
            }

            /**
             * SET BRIGHTNESS
             */
            is TTMIntent.SetBrightness -> {
                setBrightness(context, intent.value)
            }

            /**
             * SET VOLUME
             */
            is TTMIntent.SetVolume -> {
                setVolume(context, intent.value)
            }

            /**
             * UNKNOWN COMMAND
             */
            is TTMIntent.Unknown -> {
                // no-op
            }
        }
    }

    /**
     * Brightness control (system level)
     */
    private fun setBrightness(context: Context, value: Int) {
        try {
            val resolved = value.coerceIn(0, 255)

            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                resolved
            )
        } catch (e: Exception) {
            // silent fail (later hook logging system)
        }
    }

    /**
     * Volume control (media stream)
     */
    private fun setVolume(context: Context, value: Int) {
        try {
            val audioManager =
                context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager

            val maxVolume = audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC)
            val target = (maxVolume * (value / 100.0)).toInt()

            audioManager.setStreamVolume(
                android.media.AudioManager.STREAM_MUSIC,
                target,
                0
            )
        } catch (e: Exception) {
            // silent fail
        }
    }
}
