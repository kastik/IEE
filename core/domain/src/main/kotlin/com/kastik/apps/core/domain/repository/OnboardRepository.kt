package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.onboard.OnboardStage
import kotlinx.coroutines.flow.Flow

interface OnboardRepository {
    val hasFinishedOnboarding: Flow<Boolean>
    val currentOnboardingOnboardStage: Flow<OnboardStage>
    suspend fun setHasFinishedOnboarding(hasFinished: Boolean)
    suspend fun setCurrentOnboardingStage(onboardStage: OnboardStage)
}