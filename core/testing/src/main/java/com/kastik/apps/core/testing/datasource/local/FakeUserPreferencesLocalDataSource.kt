package com.kastik.apps.core.testing.datasource.local

import com.kastik.apps.core.datastore.UserPreferencesLocalDataSource

class FakeUserPreferencesLocalDataSource : UserPreferencesLocalDataSource {

    var skipped: Boolean = false

    override suspend fun getHasSkippedSignIn(): Boolean = skipped

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        skipped = hasSkippedSignIn
    }
}