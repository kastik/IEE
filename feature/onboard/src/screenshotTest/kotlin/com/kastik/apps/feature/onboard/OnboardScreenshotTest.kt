package com.kastik.apps.feature.onboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.kastik.apps.feature.onboarding.screen.OnboardAppearancePreview
import com.kastik.apps.feature.onboarding.screen.OnboardFinishPreview
import com.kastik.apps.feature.onboarding.screen.OnboardNotificationsGrantedPreview
import com.kastik.apps.feature.onboarding.screen.OnboardNotificationsNotGrantedPreview
import com.kastik.apps.feature.onboarding.screen.OnboardPreferencesPreview
import com.kastik.apps.feature.onboarding.screen.OnboardSignInSignedInPreview
import com.kastik.apps.feature.onboarding.screen.OnboardSignInSignedOutPreview
import com.kastik.apps.feature.onboarding.screen.OnboardWelcomePreview

class OnboardScreenshotTest {

    @Preview
    @PreviewTest
    @Composable
    fun OnboardWelcomeTest() {
        OnboardWelcomePreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun OnboardAppearanceTest() {
        OnboardAppearancePreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun OnboardNotificationsNotGrantedTest() {
        OnboardNotificationsNotGrantedPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun OnboardNotificationsGrantedTest() {
        OnboardNotificationsGrantedPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun OnboardPreferencesTest() {
        OnboardPreferencesPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun OnboardSignInSignedOutTest() {
        OnboardSignInSignedOutPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun OnboardSignInSignedInTest() {
        OnboardSignInSignedInPreview()
    }

    @Preview
    @PreviewTest
    @Composable
    fun OnboardFinishTest() {
        OnboardFinishPreview()
    }
}
