package com.kastik.benchmark.apps

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString

fun UiAutomatorTestScope.launchAppAndSkipOnboarding() {
    pressHome()
    startApp("com.kastik.apps")
    onElementOrNull(timeoutMs = 2000) { textAsString() == "Let's Go" }?.click()
    onElementOrNull(timeoutMs = 2000) { textAsString() == "Just Browsing (Guest)" }?.click()
    onElementOrNull(timeoutMs = 2000) { textAsString() == "Maybe Later" }?.click()
    onElementOrNull(timeoutMs = 2000) { textAsString() == "Keep Going" }?.click()
    onElement { isScrollable }.scroll(Direction.DOWN, 1f)
    onElementOrNull(timeoutMs = 2000) { textAsString() == "Almost Done" }?.click()
    onElementOrNull(timeoutMs = 2000) { textAsString() == "Take Me to the App" }?.click()
}


fun UiAutomatorTestScope.dismissSigningDialog() {
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