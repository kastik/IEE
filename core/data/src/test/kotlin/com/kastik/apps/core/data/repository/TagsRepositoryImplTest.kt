package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.crashlytics.FakeCrashlytics
import com.kastik.apps.core.data.mappers.toSubscribableTagProto
import com.kastik.apps.core.data.mappers.toSubscribedTagProto
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.datastore.datasource.FakeAuthenticationLocalDataSource
import com.kastik.apps.core.datastore.datasource.FakeTagsLocalDataSource
import com.kastik.apps.core.datastore.datasource.TagsLocalDataSource
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.FakeTagsRemoteDataSource
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import com.kastik.apps.core.network.model.common.ListResponseDto
import com.kastik.apps.core.network.model.response.TagDto
import com.kastik.apps.core.network.testdata.baseTagDto
import com.kastik.apps.core.testing.dao.FakeTagsDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class TagsRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeCrashlytics = FakeCrashlytics()
    private val announcementTagsLocalDataSource: TagsDao = FakeTagsDao()
    private val subscribableTagsLocalDataSource: TagsLocalDataSource = FakeTagsLocalDataSource()
    private val tagsRemoteDataSource: TagsRemoteDataSource = FakeTagsRemoteDataSource()
    private val fakeAuthenticationLocalDataSource = FakeAuthenticationLocalDataSource()

    private lateinit var repository: TagsRepositoryImpl

    @Before
    fun setUp() {
        repository = TagsRepositoryImpl(
            crashlytics = fakeCrashlytics,
            announcementTagsLocalDataSource = announcementTagsLocalDataSource,
            subscribableTagsLocalDataSource = subscribableTagsLocalDataSource,
            tagsRemoteDataSource = tagsRemoteDataSource,
            ioDispatcher = testDispatcher,
            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
        )
    }

// --- Announcement Tags Tests ---

    @Test
    fun announcementTagsReturnsEmptyWhenNotSet() = runTest(testDispatcher) {
        val result = repository.announcementTags.first()
        assertThat(result).isEmpty()
    }

    @Test
    fun announcementTagsEmitsFromLocalSource() = runTest(testDispatcher) {
        val entityTags = listOf(baseTagDto.toTagEntity())
        announcementTagsLocalDataSource.upsertTags(entityTags)

        val result = repository.announcementTags.first()

        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(entityTags.map { it.toTag() })
    }

    @Test
    fun syncAnnouncementTagsFetchesRemoteTagsAndSavesToLocal() = runTest(testDispatcher) {
        // Enforce the use of baseTagDto for remote fetches using delegation
        val mockRemoteSource = object : TagsRemoteDataSource by tagsRemoteDataSource {
            override suspend fun fetchAnnouncementTags(): ListResponseDto<TagDto> {
                // Adjust constructor if ListResponseDto takes different parameter names
                return ListResponseDto(data = listOf(baseTagDto))
            }
        }
        val refreshRepo = TagsRepositoryImpl(
            crashlytics = fakeCrashlytics,
            announcementTagsLocalDataSource = announcementTagsLocalDataSource,
            subscribableTagsLocalDataSource = subscribableTagsLocalDataSource,
            tagsRemoteDataSource = mockRemoteSource,
            ioDispatcher = testDispatcher,
            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
        )

        refreshRepo.syncAnnouncementTags()
        val result = refreshRepo.announcementTags.first()

        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(listOf(baseTagDto.toTagEntity().toTag()))
    }

    @Test
    fun syncAnnouncementTagsRethrowsCancellationException() = runTest(testDispatcher) {
        val errorRepo = TagsRepositoryImpl(
            crashlytics = fakeCrashlytics,
            announcementTagsLocalDataSource = announcementTagsLocalDataSource,
            subscribableTagsLocalDataSource = subscribableTagsLocalDataSource,
            tagsRemoteDataSource = object : TagsRemoteDataSource by tagsRemoteDataSource {
                override suspend fun fetchAnnouncementTags(): ListResponseDto<TagDto> {
                    throw CancellationException()
                }
            },
            ioDispatcher = testDispatcher,
            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
        )

        var caughtCancellation = false
        try {
            errorRepo.syncAnnouncementTags()
        } catch (e: CancellationException) {
            caughtCancellation = true
        }

        assertThat(caughtCancellation).isTrue()
    }

    // --- Subscribable Tags Tests ---

    @Test
    fun tagsReturnsEmptyWhenNotSet() = runTest(testDispatcher) {
        val result = repository.subscribableTags.first()
        assertThat(result).isEmpty()
    }

    @Test
    fun tagsEmitsFromLocalSource() = runTest(testDispatcher) {
        val protoTags = listOf(baseTagDto.toSubscribableTagProto())
        subscribableTagsLocalDataSource.setSubscribableTags(protoTags)

        val result = repository.subscribableTags.first()

        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(protoTags.map { it.toTag() })
    }

    @Test
    fun syncSubscribableTagsFetchesRemoteTagsAndSavesToLocal() = runTest(testDispatcher) {
        val mockRemoteSource = object : TagsRemoteDataSource by tagsRemoteDataSource {
            override suspend fun fetchSubscribableTags(): List<TagDto> = listOf(baseTagDto)
        }
        val refreshRepo = TagsRepositoryImpl(
            crashlytics = fakeCrashlytics,
            announcementTagsLocalDataSource = announcementTagsLocalDataSource,
            subscribableTagsLocalDataSource = subscribableTagsLocalDataSource,
            tagsRemoteDataSource = mockRemoteSource,
            ioDispatcher = testDispatcher,
            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
        )

        refreshRepo.syncSubscribableTags()
        val result = refreshRepo.subscribableTags.first()

        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(
            listOf(
                baseTagDto.toSubscribableTagProto().toTag()
            )
        )
    }

    // --- Subscribed Tags Tests ---

    @Test
    fun subscribedTagsAreEmptyWhenNotSet() = runTest(testDispatcher) {
        val result = repository.subscribedTags.first()
        assertThat(result).isEmpty()
    }

    @Test
    fun subscribedTagsReturnsSubscribedTagsWhenSet() = runTest(testDispatcher) {
        val protoTags = listOf(baseTagDto.toSubscribedTagProto())
        subscribableTagsLocalDataSource.setSubscriptions(protoTags)

        val result = repository.subscribedTags.first()

        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(protoTags.size)

        protoTags.zip(result).forEach { (proto, domain) ->
            assertThat(domain.id).isEqualTo(proto.id)
            assertThat(domain.title).isEqualTo(proto.title)
        }
    }

//    @Test
//    fun syncSubscribedTagsFetchesFromRemoteAndSavesToLocal() = runTest(testDispatcher) {
//        val mockRemoteSource = object : TagsRemoteDataSource by tagsRemoteDataSource {
//            override suspend fun fetchSubscriptions(): List<TagDto> = listOf(baseTagDto)
//        }
//        val refreshRepo = TagsRepositoryImpl(
//            crashlytics = fakeCrashlytics,
//            announcementTagsLocalDataSource = announcementTagsLocalDataSource,
//            subscribableTagsLocalDataSource = subscribableTagsLocalDataSource,
//            tagsRemoteDataSource = mockRemoteSource,
//            ioDispatcher = testDispatcher,
//            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
//        )
//
//        refreshRepo.syncSubscribedTags()
//        val result = refreshRepo.subscribedTags.first()
//
//        assertThat(result).isNotEmpty()
//        assertThat(result).containsExactlyElementsIn(listOf(baseTagDto.toSubscribedTagProto().toTag()))
//    }

    // --- Subscribe Action Tests ---

    @Test
    fun subscribeToTagsReturnsSuccessResult() = runTest(testDispatcher) {
        val tagIds = listOf(1, 2, 3)

        val result = repository.subscribeToTags(tagIds)

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun subscribeToTagsReturnsErrorOnException() = runTest(testDispatcher) {
        val errorRepo = TagsRepositoryImpl(
            crashlytics = fakeCrashlytics,
            announcementTagsLocalDataSource = announcementTagsLocalDataSource,
            subscribableTagsLocalDataSource = subscribableTagsLocalDataSource,
            tagsRemoteDataSource = object : TagsRemoteDataSource by tagsRemoteDataSource {
                override suspend fun subscribeToTags(tagIds: List<Int>) {
                    throw RuntimeException("Network issue")
                }
            },
            ioDispatcher = testDispatcher,
            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
        )

        val result = errorRepo.subscribeToTags(listOf(1))

        // Simply asserting we receive Result.Error, ignoring crashlytics entirely
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }
}