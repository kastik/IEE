package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import com.kastik.apps.core.network.model.aboard.AnnouncementResponse
import com.kastik.apps.core.testing.testdata.announcementResponses

class FakeAnnouncementRemoteDataSource : AnnouncementRemoteDataSource {

    private var error: Throwable? = null
    val calls = mutableListOf<Pair<Int, Int>>()

    fun setError(t: Throwable?) {
        error = t
    }

    override suspend fun fetchAnnouncements(page: Int, perPage: Int): AnnouncementResponse {
        calls += page to perPage
        error?.let { throw it }

        return announcementResponses[page - 1]
    }
}



