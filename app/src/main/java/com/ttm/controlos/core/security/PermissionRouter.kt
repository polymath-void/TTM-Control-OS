package com.ttm.controlos.core.security

import com.ttm.controlos.core.intent.TTMIntent

object PermissionRouter {

    fun requiredPermission(intent: TTMIntent): PermissionCapability {

        return when (intent) {

            is TTMIntent.SetBrightness ->
                PermissionCapability.WriteSettings

            is TTMIntent.SetVolume ->
                PermissionCapability.None

            is TTMIntent.OpenApp ->
                PermissionCapability.None

            is TTMIntent.UninstallApp ->
                PermissionCapability.Accessibility // optional future upgrade

            TTMIntent.ListApps ->
                PermissionCapability.None

            TTMIntent.ShowNotificationApps ->
                PermissionCapability.Accessibility

            is TTMIntent.Unknown ->
                PermissionCapability.None
        }
    }
}
