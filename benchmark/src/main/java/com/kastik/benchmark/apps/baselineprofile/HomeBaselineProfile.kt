package com.kastik.benchmark.apps.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.textAsString
import androidx.test.uiautomator.uiAutomator
import org.junit.Rule
import org.junit.Test


class HomeBaselineProfile {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        "com.kastik.apps",
    ) {
        uiAutomator {
            startActivityAndWait()
            onElementOrNull(timeoutMs = 2000) { textAsString() == "Dismiss" }?.click()
            val scrollable = onElement { isScrollable }
            val timesToScrollDown = 6

            repeat(timesToScrollDown) {
                scrollable.scroll(Direction.DOWN, 1f)
            }

            repeat(timesToScrollDown + 1) {
                scrollable.scroll(Direction.UP, 1f)
            }
        }
    }
}
