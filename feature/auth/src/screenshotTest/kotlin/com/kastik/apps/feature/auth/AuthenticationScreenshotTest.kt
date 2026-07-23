package com.kastik.apps.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.kastik.apps.core.designsystem.component.IeePreview

class AuthenticationScreenshotTest {

    @Preview
    @PreviewTest
    @Composable
    fun AuthenticationLoadingTest() {
        AuthenticationLoadingPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun AuthenticationErrorTest() {
        IeePreview {
            AuthenticationErrorPreview()
        }
    }

    @Preview
    @PreviewTest
    @Composable
    fun AuthenticationSuccessTest() {
        IeePreview {
            AuthenticationSuccessPreview()
        }
    }
}
