package com.kastik.benchmark.apps.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.kastik.benchmark.apps.launchAppAndDismissSigningDialog
import com.kastik.benchmark.apps.search.navigateToSearchViaAuthor
import com.kastik.benchmark.apps.search.navigateToSearchViaFAB
import com.kastik.benchmark.apps.search.navigateToSearchViaQuery
import com.kastik.benchmark.apps.search.openSearchScreenViaTag
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
        launchAppAndDismissSigningDialog()
        openSearchScreenViaTag()
        launchAppAndDismissSigningDialog()
        navigateToSearchViaAuthor()
        launchAppAndDismissSigningDialog()
        navigateToSearchViaFAB()
    }
}