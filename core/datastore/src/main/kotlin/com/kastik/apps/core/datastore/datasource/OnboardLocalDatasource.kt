package com.kastik.apps.core.datastore.datasource

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.di.OnboardDatastore
import com.kastik.apps.core.datastore.proto.OnboardStageProto
import com.kastik.apps.core.datastore.proto.StageProto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface OnboardLocalDatasource {
    val currentState: Flow<OnboardStageProto>

    suspend fun setOnboardStage(stage: StageProto)

    suspend fun setHasOnboarded(hasOnboarded: Boolean)
}

@Singleton
internal class OnboardLocalDatasourceImpl
@Inject
constructor(@OnboardDatastore private val dataStore: DataStore<OnboardStageProto>) :
    OnboardLocalDatasource {

    override val currentState: Flow<OnboardStageProto> = dataStore.data

    override suspend fun setOnboardStage(stage: StageProto) {
        dataStore.updateData { onboardProto ->
            onboardProto.toBuilder().setCurrentStage(stage).build()
        }
    }

    override suspend fun setHasOnboarded(hasOnboarded: Boolean) {
        dataStore.updateData { userPreferences ->
            userPreferences
                .toBuilder()
                .setCurrentStage(StageProto.Welcome)
                .setHasFinishedOnboard(hasOnboarded)
                .build()
        }
    }
}
