package com.kastik.benchmark.apps.home

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction


fun MacrobenchmarkScope.refreshFeed() {
    val announcementList = onElement(timeoutMs = 2000) { contentDescription == "announcement_feed" }
    announcementList.scroll(Direction.UP, 1f)
}
