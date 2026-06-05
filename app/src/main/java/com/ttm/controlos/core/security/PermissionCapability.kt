package com.ttm.controlos.core.security

sealed class PermissionCapability {

    object WriteSettings : PermissionCapability()
    object Accessibility : PermissionCapability()
    object Overlay : PermissionCapability()

    object None : PermissionCapability()
}
