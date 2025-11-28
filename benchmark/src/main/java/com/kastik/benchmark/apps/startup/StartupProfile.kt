package com.kastik.benchmark.apps.startup

import android.content.Intent
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.core.net.toUri
import androidx.test.uiautomator.uiAutomator
import org.junit.Rule
import org.junit.Test

class StartupProfile {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateLauncherStartupProfile() = baselineProfileRule.collect(
        "com.kastik.apps",
        includeInStartupProfile = true,
    ) {
        uiAutomator {
            startActivityAndWait()
        }
    }

    @Test
    fun generateIntentFilterStartupProfile() = baselineProfileRule.collect(
        packageName = "com.kastik.apps",
        includeInStartupProfile = true,
    ) {
        val intent = Intent(Intent.ACTION_VIEW, "com.kastik.apps://auth".toUri())
        intent.setPackage("com.kastik.apps")
        startActivityAndWait(intent)
    }
}