package com.kastik.benchmark.apps.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.kastik.benchmark.apps.launchAppAndDismissSigningDialog
import com.kastik.benchmark.apps.settings.navigateToSettings
import com.kastik.benchmark.apps.settings.toggleDynamicTheme
import com.kastik.benchmark.apps.settings.toggleFabFilters
import com.kastik.benchmark.apps.settings.toggleSearchFieldOptions
import com.kastik.benchmark.apps.settings.toggleSortingOptions
import com.kastik.benchmark.apps.settings.toggleThemeOptions
import org.junit.Rule
import org.junit.Test

class SettingsBaselineProfile {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        "com.kastik.apps",
    ) {
        launchAppAndDismissSigningDialog()
        navigateToSettings()
        toggleSortingOptions()
        toggleFabFilters()
        toggleSearchFieldOptions()
        toggleThemeOptions()
        toggleDynamicTheme()
    }
}