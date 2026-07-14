package com.kastik.benchmark.apps.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.kastik.benchmark.apps.launchAppAndDismissSigningDialog
import com.kastik.benchmark.apps.licence.navigateToLicences
import com.kastik.benchmark.apps.licence.openRandomLicence
import com.kastik.benchmark.apps.licence.scrollLicencesList
import org.junit.Rule
import org.junit.Test

class LicenceScreenBaselineProfile {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        "com.kastik.apps",
    ) {
        launchAppAndDismissSigningDialog()
        navigateToLicences()
        scrollLicencesList()
        openRandomLicence()
    }
}
