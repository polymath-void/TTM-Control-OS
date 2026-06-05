package com.ttm.controlos.system.notification

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

/**
 * NotificationTrackingService
 *
 * System-level listener that tracks active notification sources
 * (package names only).
 *
 * Role:
 * Feeds system data into the command OS (read-only input layer).
 */
class NotificationTrackingService : NotificationListenerService() {

    companion object {

        /**
         * Stores active apps that currently have notifications.
         */
        private val notificationPackages = mutableSetOf<String>()

        /**
         * Callback for UI or system observers.
         */
        var onNotificationsChanged: (() -> Unit)? = null

        /**
         * External access for core/UI layer.
         */
        fun getActiveNotificationPackages(): List<String> {
            return notificationPackages.toList()
        }
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        refreshActiveNotifications()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn?.packageName?.let { pkg ->
            if (notificationPackages.add(pkg)) {
                onNotificationsChanged?.invoke()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        refreshActiveNotifications()
    }

    /**
     * Rebuild full state from system.
     */
    private fun refreshActiveNotifications() {
        try {
            val active = mutableSetOf<String>()

            val sbns = try {
                getActiveNotifications()
            } catch (e: Exception) {
                null
            }

            sbns?.forEach { sbn ->
                sbn.packageName?.let { active.add(it) }
            }

            if (active != notificationPackages) {
                notificationPackages.clear()
                notificationPackages.addAll(active)
                onNotificationsChanged?.invoke()
            }

        } catch (_: Exception) {
            // Fail silently (system-safe design)
        }
    }
}
