package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.OnboardRepository
import com.kastik.apps.core.model.onboard.OnboardStage
import javax.inject.Inject


class GetOnboardStageUseCase @Inject constructor(
    private val onboardRepository: OnboardRepository
) {
    operator fun invoke() = onboardRepository.currentOnboardingOnboardStage

}

class GetHasFinishedOnboardUseCase @Inject constructor(
    private val onboardRepository: OnboardRepository
) {
    operator fun invoke() = onboardRepository.hasFinishedOnboarding
}


class SetHasFinishedOnboardUseCase @Inject constructor(
    private val onboardRepository: OnboardRepository
) {
    suspend operator fun invoke(hasFinished: Boolean) =
        onboardRepository.setHasFinishedOnboarding(hasFinished)
}

class SetOnboardStageUseCase @Inject constructor(
    private val onboardRepository: OnboardRepository
) {
    suspend operator fun invoke(onboardStage: OnboardStage) =
        onboardRepository.setCurrentOnboardingStage(onboardStage)
}
