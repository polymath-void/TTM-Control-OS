package com.ttm.controlos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.ttm.controlos.core.resolver.PackageResolver
import com.ttm.controlos.ui.CommandScreen

/**
 * MainActivity
 *
 * Entry point of TTM Control OS.
 *
 * Responsibilities:
 * - Initialize system modules
 * - Load installed apps into resolver cache
 * - Start UI layer
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize package resolver cache
        PackageResolver.loadInstalledApps(this)

        // 2. Start UI
        setContent {
            CommandScreen(this)
        }
    }
}
