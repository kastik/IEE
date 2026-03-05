package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.model.search.FilterOptions
import com.kastik.apps.core.model.search.QuickResults
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetQuickResultsUseCase @Inject constructor(
    private val getTagsQuickResults: GetTagsQuickResults,
    private val getAuthorQuickResults: GetAuthorQuickResultsUseCase,
    private val getAnnouncementsQuickResults: GetSearchQuickResultsAnnouncementsUseCase
) {
    operator fun invoke(query: String): Flow<QuickResults> {
        return combine(
            getTagsQuickResults(query),
            getAuthorQuickResults(query),
            getAnnouncementsQuickResults(query)
        ) { tags, authors, announcements ->
            QuickResults(
                tags = tags,
                authors = authors,
                announcements = announcements
            )
        }
    }
}

class GetFilterOptionsUseCase @Inject constructor(
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val getAnnouncementTagsUseCase: GetAnnouncementTagsUseCase,
    private val refreshFilterOptionsUseCase: RefreshFilterOptionsUseCase,
) {
    operator fun invoke(): Flow<FilterOptions> {
        return combine(
            getAuthorsUseCase(),
            getAnnouncementTagsUseCase(),
        ) { authors, tags ->
            FilterOptions(
                tags = tags,
                authors = authors,
            )
        }.onStart {
            coroutineScope {
                launch { refreshFilterOptionsUseCase() }
            }
        }
    }
}

class RefreshFilterOptionsUseCase @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val tagsRepository: TagsRepository,
) {
    suspend operator fun invoke() = coroutineScope {
        val authorsDeferred = async { authorRepository.refreshAuthors() }
        val tagsDeferred = async { tagsRepository.refreshAnnouncementTags() }
        val (authorsResult, tagsResult) = awaitAll(authorsDeferred, tagsDeferred)
        if (authorsResult is Result.Error) {
            return@coroutineScope authorsResult
        }
        if (tagsResult is Result.Error) {
            return@coroutineScope tagsResult
        }
        return@coroutineScope Result.Success(Unit)
    }
}