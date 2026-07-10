package com.kastik.apps.core.database.dao

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.database.entities.RemoteKeysEntity
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.baseAnnouncementEntity
import com.kastik.apps.core.testing.testdata.baseAttachmentEntity
import com.kastik.apps.core.testing.testdata.baseAuthorEntity
import com.kastik.apps.core.testing.testdata.baseBodyEntity
import com.kastik.apps.core.testing.testdata.baseTagEntity
import com.kastik.apps.core.testing.testdata.baseTagsCrossRefEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(RoboDatabaseTestRunner::class)
internal class AnnouncementDaoTest : MemoryDatabase() {


    @Before
    fun setupForeignKeys() = runTest {
        authorsDao.upsertAuthors(baseAuthorEntity)
        tagsDao.upsertTags(listOf(baseTagEntity))
    }

    @Test
    fun upsertListAnnouncementsInsertsNewAnnouncements() = runTest {
        announcementDao.upsertAnnouncements(listOf(baseAnnouncementEntity))

        val result = announcementDao.getAnnouncementWithId(1).first()

        assertThat(result).isNotNull()
        assertThat(result?.announcement?.id).isEqualTo(1)
        assertThat(result?.announcement?.title).isEqualTo("Test Title")
    }

    @Test
    fun upsertSingleAnnouncementInsertsNewAnnouncement() = runTest {
        announcementDao.upsertAnnouncements(listOf(baseAnnouncementEntity))

        val result = announcementDao.getAnnouncementWithId(1).first()

        assertThat(result).isNotNull()
        assertThat(result?.announcement?.id).isEqualTo(1)
        assertThat(result?.announcement?.title).isEqualTo("Test Title")
    }

    @Test
    fun getAnnouncementWithIdMapsCompleteDetailRelation() = runTest {
        announcementDao.upsertAnnouncements(baseAnnouncementEntity)
        announcementDao.upsertBodies(baseBodyEntity)
        announcementDao.upsertAttachments(listOf(baseAttachmentEntity))
        announcementDao.upsertTagCrossRefs(listOf(baseTagsCrossRefEntity))

        val result = announcementDao.getAnnouncementWithId(1).first()

        assertThat(result).isNotNull()
        assertThat(result?.announcement?.id).isEqualTo(1)
        assertThat(result?.author?.name).isEqualTo("John")
        assertThat(result?.body?.body).isEqualTo("Full content here.")
        assertThat(result?.tags).hasSize(1)
        assertThat(result?.tags?.first()?.title).isEqualTo("Important")
        assertThat(result?.attachments).hasSize(1)
        assertThat(result?.attachments?.first()?.filename).isEqualTo("file.pdf")
    }

    @Test
    fun getQuickSearchReturnsMatchingAnnouncementTitles() = runTest {
        val target =
            baseAnnouncementEntity.copy(id = 2, title = "Find Apple Here", preview = "Nothing")
        val noise = baseAnnouncementEntity.copy(id = 3, title = "Banana", preview = "Nothing")
        announcementDao.upsertAnnouncements(listOf(target, noise))

        val results = announcementDao.getQuickSearchAnnouncements("Apple", SortType.DESC).first()

        assertThat(results).hasSize(1)
        assertThat(results.first().announcement.id).isEqualTo(2)
    }

    @Test
    fun getQuickSearchReturnsMatchingAnnouncementBody() = runTest {
        val target =
            baseAnnouncementEntity.copy(id = 2, title = "Nothing", preview = "Contains Apple")
        announcementDao.upsertAnnouncements(target)

        val results = announcementDao.getQuickSearchAnnouncements("Apple", SortType.DESC).first()

        assertThat(results).hasSize(1)
        assertThat(results.first().announcement.id).isEqualTo(2)
    }


    @Test
    fun getPagedAnnouncementsReturnsMatchingRemoteKeyAnnouncements() = runTest {
        announcementDao.upsertAnnouncements(baseAnnouncementEntity)
        val remoteKey = RemoteKeysEntity(
            announcementId = 1, titleQuery = "Filter", bodyQuery = "",
            authorIds = emptyList(), tagIds = emptyList(), sortType = SortType.DESC,
            prevKey = null, nextKey = null
        )
        remoteKeysDao.insertOrReplaceKeys(listOf(remoteKey))

        val pagingSource = announcementDao.getPagedAnnouncements(
            titleQuery = "Filter", sortType = SortType.DESC
        )

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )

        assertThat(loadResult).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        val page = loadResult as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(1)
        assertThat(page.data.first().announcement.id).isEqualTo(1)
    }


    fun getPagedAnnouncementsWithoutMatchingRemoteKeyReturnsEmpty() = runTest {
        announcementDao.upsertAnnouncements(baseAnnouncementEntity)
        val remoteKey = RemoteKeysEntity(
            announcementId = 1, titleQuery = "Filter A", bodyQuery = "", // Cached for "Filter A"
            authorIds = emptyList(), tagIds = emptyList(), sortType = SortType.DESC,
            prevKey = null, nextKey = null
        )
        remoteKeysDao.insertOrReplaceKeys(listOf(remoteKey))

        val pagingSource = announcementDao.getPagedAnnouncements(
            titleQuery = "Filter B", sortType = SortType.DESC
        )

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )

        val page = loadResult as PagingSource.LoadResult.Page
        assertThat(page.data).isEmpty()
    }


    @Test
    fun getAttachmentWithIdReturnsExactUrlString() = runTest {
        announcementDao.upsertAnnouncements(baseAnnouncementEntity)
        announcementDao.upsertAttachments(listOf(baseAttachmentEntity))

        val url = announcementDao.getAttachmentWithId(1)

        assertThat(url).isEqualTo("https://example.com")
    }

    @Test
    fun clearAllAnnouncementsRemovesAnnouncementsAndCascades() = runTest {
        announcementDao.upsertAnnouncements(baseAnnouncementEntity)

        announcementDao.clearAllAnnouncements()

        val result = announcementDao.getAnnouncementWithId(1).first()
        assertThat(result).isNull()
    }
}