package com.ttm.controlos.core.security

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object PermissionRequester {

    fun requestWriteSettingsPermission(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            Uri.parse("package:${activity.packageName}")
        )
        activity.startActivity(intent)
    }
}
