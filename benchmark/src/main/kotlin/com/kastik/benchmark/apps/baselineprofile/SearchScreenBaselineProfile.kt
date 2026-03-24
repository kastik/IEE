package com.kastik.benchmark.apps.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.kastik.benchmark.apps.launchAppAndDismissSigningDialog
import com.kastik.benchmark.apps.scrollFeed
import com.kastik.benchmark.apps.search.navigateToSearchViaQuery
import org.junit.Rule
import org.junit.Test

class SearchScreenBaselineProfile {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        "com.kastik.apps",
    ) {
        launchAppAndDismissSigningDialog()
        navigateToSearchViaQuery()
        scrollFeed()
    }
}