package com.kastik.apps.core.testing.datasource.local

import com.kastik.apps.core.datastore.PreferencesLocalDataSource
import com.kastik.apps.core.datastore.proto.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPreferencesLocalDataSource : PreferencesLocalDataSource {
    var skipped: Boolean = false
    var theme: Theme = Theme.Light
    var dynamicColor: Boolean = true


    override suspend fun getHasSkippedSignIn(): Boolean = skipped

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        skipped = hasSkippedSignIn
    }

    override fun getUserTheme(): Flow<Theme> {
        return flowOf(theme)
    }

    override suspend fun setUserTheme(value: Theme) {
        this.theme = value
    }

    override fun getDynamicColor(): Flow<Boolean> {
        return flowOf(dynamicColor)
    }

    override suspend fun setDynamicColor(value: Boolean) {
        dynamicColor = value
    }
}