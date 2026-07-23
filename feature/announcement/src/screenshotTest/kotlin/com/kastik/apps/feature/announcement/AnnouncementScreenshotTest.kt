package com.kastik.apps.feature.announcement

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

class AnnouncementScreenshotTest {

    @Preview
    @PreviewTest
    @Composable
    fun AnnouncementScreenLoadingTest() {
        AnnouncementScreenLoadingPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun AnnouncementScreenErrorTest() {
        AnnouncementScreenErrorPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun AnnouncementScreenSuccessTest() {
        AnnouncementScreenSuccessPreview()
    }
}
