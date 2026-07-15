package com.kastik.apps.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

class SettingsScreenshotTest {

    @Preview
    @PreviewTest
    @Composable
    fun SettingsScreenSuccessTest() {
        SettingsScreenSuccessPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun SettingsScreenLoadingTest() {
        SettingsScreenLoadingPreview()
    }
}
