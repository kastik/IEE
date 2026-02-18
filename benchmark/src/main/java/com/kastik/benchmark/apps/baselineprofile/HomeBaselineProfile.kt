package com.kastik.benchmark.apps.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.kastik.benchmark.apps.home.refreshFeed
import com.kastik.benchmark.apps.launchAppAndDismissSigningDialog
import com.kastik.benchmark.apps.scrollFeed
import org.junit.Rule
import org.junit.Test


class HomeBaselineProfile {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        "com.kastik.apps",
    ) {
        launchAppAndDismissSigningDialog()
        scrollFeed()
        refreshFeed()
    }
}
