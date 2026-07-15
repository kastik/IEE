package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.data.mappers.toStage
import com.kastik.apps.core.data.mappers.toStageProto
import com.kastik.apps.core.datastore.datasource.OnboardLocalDatasource
import com.kastik.apps.core.domain.repository.OnboardRepository
import com.kastik.apps.core.model.onboard.OnboardStage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnboardRepositoryImpl @Inject constructor(
    private val onboardLocalDatasource: OnboardLocalDatasource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : OnboardRepository {

    override val hasFinishedOnboarding: Flow<Boolean> =
        onboardLocalDatasource.currentState.map { it.hasFinishedOnboard }

    override val currentOnboardingOnboardStage: Flow<OnboardStage> =
        onboardLocalDatasource.currentState.map { it.currentStage.toStage() }

    override suspend fun setHasFinishedOnboarding(hasFinished: Boolean) =
        onboardLocalDatasource.setHasOnboarded(hasFinished)

    override suspend fun setCurrentOnboardingStage(onboardStage: OnboardStage) =
        onboardLocalDatasource.setOnboardStage(onboardStage.toStageProto())
}