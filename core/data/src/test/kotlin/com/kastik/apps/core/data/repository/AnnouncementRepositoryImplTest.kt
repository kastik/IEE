package com.kastik.apps.core.data.repository


import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class AnnouncementRepositoryImplTest {
//    private val testDispatcher = StandardTestDispatcher()
//    private val fakeCrashlytics = FakeCrashlytics()
//    private val fakeDatabase = FakeAppDatabase()
//    private val announcementDao = fakeDatabase.announcementDao() as FakeAnnouncementDao
//    private val fakeAuthorDao = fakeDatabase.authorDao() as FakeAuthorsDao
//    private val fakeTagsDao = fakeDatabase.tagsDao() as FakeTagsDao
//    private val fakeAnnouncementRemoteDataSource = FakeAnnouncementRemoteDataSource()
//    private val fakeBase64ImageExtractor = FakeBase64ImageExtractor()
//
//
//    private val announcementRepoImpl =
//        AnnouncementRepositoryImpl(
//            crashlytics = fakeCrashlytics,
//            database = fakeDatabase,
//            announcementRemoteDataSource = fakeAnnouncementRemoteDataSource,
//            base64ImageExtractor = fakeBase64ImageExtractor,
//            ioDispatcher = testDispatcher,
//        )
//
//    @Before
//    fun initMocks() {
//        MockKAnnotations.init(this)
//
//        mockkStatic("androidx.room.RoomDatabaseKt")
//
//        val transactionLambda = slot<suspend () -> R>()
//        coEvery { fakeDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
//            transactionLambda.captured.invoke()
//        }
//    }
//
//    @Test
//    fun getPagedAnnouncementsReturnsMappedPagingDataFlow() = runTest(testDispatcher) {
//        // Given
//        val sortType = SortType.ASC
//        val titleQuery = "Test"
//        val bodyQuery = ""
//        val authorIds = listOf(1)
//        val tagIds = listOf(2)
//
//        // When
//        val flow = announcementRepoImpl.getPagedAnnouncements(
//            sortType, titleQuery, bodyQuery, authorIds, tagIds
//        )
//
//        // Then
//        // We verify the flow is successfully created. Deep paging data assertions
//        // require androidx.paging:paging-testing artifact extensions like .asSnapshot().
//        assertThat(flow).isNotNull()
//    }
//
//    @Test
//    fun fetchAnnouncementsSuccessReturnsMappedNetworkDataAndConvertsTimeZone() = runTest(testDispatcher) {
//        // Given
//        val epochInstant = Instant.fromEpochSeconds(1700000000) // Adjust based on your Instant lib
//        fakeAnnouncementRemoteDataSource.pagedResponseToReturn = basePagedAnnouncementDto // Assuming base test data
//
//        // When
//        val result = announcementRepoImpl.fetchAnnouncements(
//            page = 1,
//            perPage = 20,
//            sortType = SortType.Priority,
//            titleQuery = "Title",
//            bodyQuery = "",
//            authorIds = emptyList(),
//            tagIds = emptyList(),
//            updatedAfter = epochInstant
//        )
//
//        // Then
//        assertThat(result).isInstanceOf(Result.Success::class.java)
//
//        // Assert Remote DataSource received the mapped Athens local time properly
//        assertThat(fakeAnnouncementRemoteDataSource.receivedUpdatedAfter).isNotNull()
//        // (You can add specific Assertions here comparing receivedUpdatedAfter to the expected Athens LocalDateTime)
//    }
//
//    @Test
//    fun fetchAnnouncementsExceptionReturnsMappedNetworkError() = runTest(testDispatcher) {
//        // Given
//        val exception = RuntimeException("Network Error")
//        fakeAnnouncementRemoteDataSource.exceptionToThrow = exception
//
//        // When
//        val result = announcementRepoImpl.fetchAnnouncements(
//            page = 1, perPage = 20, sortType = SortType.DATE, titleQuery = "",
//            bodyQuery = "", authorIds = emptyList(), tagIds = emptyList(), updatedAfter = null
//        )
//
//        // Then
//        assertThat(result).isInstanceOf(Result.Error::class.java)
//        val errorResult = result as Result.Error
//        assertThat(errorResult.error).isEqualTo(exception.toNetworkError())
//    }
//
//    @Test(expected = CancellationException::class)
//    fun fetchAnnouncementsCancellationExceptionIsRethrown() = runTest(testDispatcher) {
//        fakeAnnouncementRemoteDataSource.exceptionToThrow = CancellationException()
//        announcementRepoImpl.fetchAnnouncements(1, 20, SortType.DATE, "", "", emptyList(), emptyList(), null)
//    }
//
//    @Test
//    fun getAnnouncementsQuickResultsReturnsMappedDomainFlow() = runTest(testDispatcher) {
//        // Given (Stubbing)
//        announcementDao.quickSearchFlowToReturn = flowOf(listOf(baseAnnouncementEntity))
//
//        // When
//        val result = announcementRepoImpl.getAnnouncementsQuickResults(SortType.DATE, "Query").first()
//
//        // Then
//        assertThat(result.size).isEqualTo(1)
//        assertThat(result.first()).isEqualTo(baseAnnouncementEntity.toAnnouncement())
//    }
//
//    @Test
//    fun refreshAnnouncementWithIdSuccessUpsertsAllEntitiesInTransaction() = runTest(testDispatcher) {
//        // Given
//        val id = 1
//        // Assuming baseAnnouncementRemoteDto has nested author, tags, attachments
//        fakeAnnouncementRemoteDataSource.singleAnnouncementToReturn = baseAnnouncementRemoteDto
//
//        // When
//        val result = announcementRepoImpl.refreshAnnouncementWithId(id)
//
//        // Then
//        assertThat(result).isInstanceOf(Result.Success::class.java)
//
//        // Asserting our Spies to ensure ALL tables were updated appropriately
//        assertThat(fakeAuthorDao.upsertedAuthor).isEqualTo(baseAnnouncementRemoteDto.author.toAuthorEntity())
//        assertThat(fakeTagsDao.upsertedTags).isEqualTo(baseAnnouncementRemoteDto.tags.map { it.toTagEntity() })
//        assertThat(announcementDao.upsertedAnnouncements).isEqualTo(baseAnnouncementRemoteDto.toAnnouncementEntity())
//        assertThat(announcementDao.upsertedTagCrossRefs).isEqualTo(baseAnnouncementRemoteDto.toTagCrossRefs())
//        assertThat(announcementDao.upsertedAttachments).isEqualTo(baseAnnouncementRemoteDto.attachments.map { it.toAttachmentEntity() })
//
//        // Verify Image Extraction was called
//        assertThat(announcementDao.upsertedBodies).isNotNull()
//    }
//
//    @Test
//    fun refreshAnnouncementWithIdExceptionRecordsCrashlyticsAndReturnsNetworkError() = runTest(testDispatcher) {
//        // Given
//        val exception = RuntimeException("Remote Fetch Failed")
//        fakeAnnouncementRemoteDataSource.exceptionToThrow = exception
//
//        // When
//        val result = announcementRepoImpl.refreshAnnouncementWithId(1)
//
//        // Then
//        assertThat(result).isInstanceOf(Result.Error::class.java)
//        val errorResult = result as Result.Error
//        assertThat(errorResult.error).isEqualTo(exception.toNetworkError())
//        assertThat(testCrashlytics.recordedExceptions).contains(exception)
//
//        // Ensure no local database writes occurred
//        assertThat(announcementDao.upsertedAnnouncements).isNull()
//    }
//
//    @Test
//    fun getAnnouncementWithIdReturnsMappedDomainObject() = runTest(testDispatcher) {
//        // Given (Stubbing)
//        announcementDao.singleAnnouncementFlowToReturn = flowOf(baseAnnouncementEntity)
//
//        // When
//        val result = announcementRepoImpl.getAnnouncementWithId(1).first()
//
//        // Then
//        assertThat(result).isEqualTo(baseAnnouncementEntity.toAnnouncement())
//    }
//
//    @Test
//    fun getAttachmentUrlReturnsDataFromDao() = runTest(testDispatcher) {
//        // Given
//        val expectedUrl = "https://example.com/attachment"
//        announcementDao.attachmentUrlToReturn = expectedUrl
//
//        // When
//        val result = announcementRepoImpl.getAttachmentUrl(1)
//
//        // Then
//        assertThat(result).isEqualTo(expectedUrl)
//    }
//
//    @Test
//    fun clearAnnouncementCacheSuccessClearsAllFourTables() = runTest(testDispatcher) {
//        // When
//        val result = announcementRepoImpl.clearAnnouncementCache()
//
//        // Then
//        assertThat(result).isInstanceOf(Result.Success::class.java)
//
//        // Verify all 4 specific clearing methods were invoked on the Spy
//        assertThat(announcementDao.isClearAllAnnouncementsCalled).isTrue()
//        assertThat(announcementDao.isClearBodiesCalled).isTrue()
//        assertThat(announcementDao.isClearAttachmentsCalled).isTrue()
//        assertThat(announcementDao.isClearTagCrossRefsCalled).isTrue()
//
//        assertThat(testCrashlytics.recordedExceptions).isEmpty()
//    }
//
//    @Test
//    fun clearAnnouncementCacheExceptionRecordsCrashlyticsAndReturnsLocalError() = runTest(testDispatcher) {
//        // Given
//        val exception = RuntimeException("DB Delete Failed")
//        announcementDao.exceptionToThrowOnClear = exception
//
//        // When
//        val result = announcementRepoImpl.clearAnnouncementCache()
//
//        // Then
//        assertThat(result).isInstanceOf(Result.Error::class.java)
//        val errorResult = result as Result.Error
//        assertThat(errorResult.error).isEqualTo(exception.toLocalError())
//        assertThat(testCrashlytics.recordedExceptions).contains(exception)
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @Test
//    fun getPagedAnnouncements_returnsMappedData() = runTest(testDispatcher) {
//        val result: List<Announcement> = announcementRepoImpl.getPagedAnnouncements(
//            sortType = SortType.DESC,
//            titleQuery = "",
//            bodyQuery = "",
//            authorIds = emptyList(),
//            tagIds = emptyList()
//        ).asSnapshot()
//
//        assertThat(result).isNotEmpty()
//        assertThat(result.first().id).isEqualTo(announcementDetailsRelationTestData.first().announcement.id)
//    }
//
//    @Test
//    fun getAnnouncementWithIdFetchesTheCorrectDataTest() = runTest(testDispatcher) {
//        announcementDao.insertTestData()
//        announcementDetailsRelationTestData.forEach { announcementDetail ->
//            val result =
//                announcementRepoImpl.getAnnouncementWithId(announcementDetail.announcement.id)
//                    .first()!!
//            assertThat(result).isNotNull()
//            assertThat(announcementDetail.announcement.id).isEqualTo(result.id)
//            assertThat(announcementDetail.announcement.title).isEqualTo(result.title)
//            assertThat(announcementDetail.body.body).isEqualTo(result.body)
//            assertThat(announcementDetail.author.name).isEqualTo(result.author)
//        }
//    }
//
//    //TODO Create test for sorting, limiting, etc
//    @Test
//    fun getAnnouncementsQuickResultsReturnsQuickResults() = runTest(testDispatcher) {
//        announcementDao.insertTestData()
//        val result =
//            announcementRepoImpl.getAnnouncementsQuickResults(SortType.DESC, "Test").first()
//        assertThat(result).isNotEmpty()
//    }
//
//
//    @Test
//    fun getAttachmentUrlReturnsAttachmentUrl() = runTest(testDispatcher) {
//        announcementDao.insertTestData()
//        val attachments = announcementDao.attachments.value
//
//        assertThat(attachments).isNotEmpty()
//
//        attachments.forEach { attachment ->
//            val result = announcementRepoImpl.getAttachmentUrl(attachment.id)
//            assertThat(result).isEqualTo(attachment.attachmentUrl)
//        }
//    }
//
//
//    @Test
//    fun clearAnnouncementCacheClearsLocallyStoredAnnouncements() = runTest(testDispatcher) {
//        announcementDao.insertTestData()
//        announcementRepoImpl.clearAnnouncementCache()
//        val result = announcementDao.announcements.value
//        assertThat(result).isEmpty()
//    }


}
