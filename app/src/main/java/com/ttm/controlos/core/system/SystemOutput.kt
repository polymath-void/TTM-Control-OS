package com.ttm.controlos.core.system

import android.util.Log

/**
 * SystemOutput
 * * Represents the state of the command execution engine.
 */
sealed class SystemOutput {
    object Idle : SystemOutput()
    data class Processing(val status: String) : SystemOutput()
    data class Success(val message: String) : SystemOutput()
    data class Error(val reason: String) : SystemOutput()

    // Override toString for easy UI display
    override fun toString(): String = when(this) {
        is Idle -> "Ready"
        is Processing -> "Processing: $status"
        is Success -> "Success: $message"
        is Error -> "Error: $reason"
    }

    // Helper to log state changes globally
    fun log() {
        Log.d("TTM_SYSTEM", this.toString())
    }
}
