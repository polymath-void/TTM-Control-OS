package com.ttm.controlos.core.executor

import android.content.Context
import android.provider.Settings
import android.media.AudioManager
import android.view.WindowManager

/**
 * DeviceExecutor
 *
 * Handles safe device-level system controls using Android APIs only.
 *
 * Scope:
 * - Brightness control
 * - Volume control
 *
 * No root, no shell, no Termux fallback (per architecture choice A).
 */
object DeviceExecutor {

    /**
     * Set screen brightness (0–255)
     *
     * Requires:
     * WRITE_SETTINGS permission (system-level toggle permission)
     */
    fun setBrightness(context: Context, value: Int) {
        val brightness = value.coerceIn(0, 255)

        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            brightness
        )
    }

    /**
     * Set media volume (0–max)
     */
    fun setVolume(context: Context, value: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume = (value * maxVolume / 100).coerceIn(0, maxVolume)

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            volume,
            0
        )
    }

    /**
     * Optional helper: get current brightness
     */
    fun getBrightness(context: Context): Int {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            125
        )
    }

    /**
     * Optional helper: get current volume %
     */
    fun getVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        return if (max == 0) 0 else (current * 100 / max)
    }
}
