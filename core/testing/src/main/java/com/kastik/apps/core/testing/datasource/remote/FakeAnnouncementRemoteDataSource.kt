package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse
import com.kastik.apps.core.network.model.aboard.SingleAnnouncementResponse
import com.kastik.apps.core.testing.testdata.announcementResponses

class FakeAnnouncementRemoteDataSource : AnnouncementRemoteDataSource {

    private var error: Throwable? = null
    val calls = mutableListOf<Pair<Int, Int>>()

    fun setError(t: Throwable?) {
        error = t
    }

    override suspend fun fetchAnnouncements(page: Int, perPage: Int): AnnouncementPageResponse {
        calls += page to perPage
        error?.let { throw it }

        return announcementResponses[page - 1]
    }

    override suspend fun fetchAnnouncementWithId(id: Int): SingleAnnouncementResponse {
        TODO("Not yet implemented")
    }
}



