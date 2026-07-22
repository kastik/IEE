package com.kastik.benchmark.apps

import android.content.Intent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString

fun MacrobenchmarkScope.launchAppAndDismissSigningDialog() {
    pressHome()

    val context = InstrumentationRegistry.getInstrumentation().context
    val intent = context.packageManager.getLaunchIntentForPackage("com.kastik.apps")

    requireNotNull(intent) { "Could not find launch intent for com.kastik.apps" }

    intent.apply {
        putExtra("SKIP_ONBOARDING_FOR_BENCHMARK", true)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    startActivityAndWait(intent)
    onElementOrNull(timeoutMs = 2000) { textAsString() == "Dismiss" }?.click()
}

//TODO This broke in HomeScreen after we added Horizontal Pager,
fun UiAutomatorTestScope.scrollFeed() {
    val announcementList = onElement { viewIdResourceName == "announcement_feed" }

    val timesToScroll = 20
    repeat(timesToScroll) {
        announcementList.scroll(Direction.DOWN, 1f)
    }
}