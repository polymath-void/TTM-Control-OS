package com.ttm.controlos.core.security

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object PermissionHandler {

    fun request(context: Context, capability: PermissionCapability) {

        when (capability) {

            PermissionCapability.WriteSettings -> {
                val intent = Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + context.packageName)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            PermissionCapability.Accessibility -> {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            PermissionCapability.Overlay -> {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.packageName)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            PermissionCapability.None -> {
                // no-op
            }
        }
    }
}
