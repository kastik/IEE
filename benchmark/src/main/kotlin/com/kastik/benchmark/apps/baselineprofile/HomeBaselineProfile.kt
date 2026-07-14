package com.kastik.benchmark.apps.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.kastik.benchmark.apps.dismissSigningDialog
import com.kastik.benchmark.apps.home.refreshFeed
import com.kastik.benchmark.apps.launchAppAndSkipOnboarding
import org.junit.Rule
import org.junit.Test


class HomeBaselineProfile {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        "com.kastik.apps",
    ) {
        launchAppAndSkipOnboarding()
        dismissSigningDialog()
        refreshFeed()
    }
}
