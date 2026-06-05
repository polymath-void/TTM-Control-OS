package com.ttm.controlos.core.security

/**
 * Capability
 *
 * Atomic permission unit for executing system actions.
 *
 * Every TTMIntent must map to one or more capabilities
 * before execution is allowed.
 *
 * This is the core security model of the system.
 */
enum class Capability {

    /**
     * Launch installed applications
     */
    OPEN_APP,

    /**
     * Remove installed applications via system intent
     */
    UNINSTALL_APP,

    /**
     * Read list of installed applications
     */
    READ_INSTALLED_APPS,

    /**
     * Read active notification sources (package names only)
     */
    READ_NOTIFICATIONS,

    /**
     * Execute system-level settings changes (future)
     */
    MODIFY_SYSTEM_SETTINGS,

    /**
     * Control network state (wifi, bluetooth)
     */
    CONTROL_NETWORK,

    /**
     * Control device audio (volume, mute)
     */
    CONTROL_AUDIO,

    /**
     * Fallback unsafe / unknown capability
     */
    UNKNOWN
}
