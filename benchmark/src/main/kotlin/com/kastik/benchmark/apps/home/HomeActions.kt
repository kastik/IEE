package com.kastik.benchmark.apps.home

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope


fun UiAutomatorTestScope.refreshFeed() {
    waitForStableInActiveWindow()
    val announcementList =
        onElement(timeoutMs = 10000) { viewIdResourceName == "announcement_feed" }
    announcementList.scroll(Direction.UP, 1f)
}
