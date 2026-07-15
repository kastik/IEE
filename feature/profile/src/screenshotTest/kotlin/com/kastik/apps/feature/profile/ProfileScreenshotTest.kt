package com.kastik.apps.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

class ProfileScreenshotTest {

    @Preview
    @PreviewTest
    @Composable
    fun ProfileLoadingTest() {
        ProfileLoadingPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun ProfileSignedOutTest() {
        ProfileSignedOutPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun ProfileSuccessTest() {
        ProfileSuccessPreview()
    }

}