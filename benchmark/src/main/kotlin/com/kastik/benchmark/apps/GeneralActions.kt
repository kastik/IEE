package com.kastik.benchmark.apps

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString

fun UiAutomatorTestScope.launchAppAndDismissSigningDialog() {
    pressHome()
    startApp("com.kastik.apps")
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