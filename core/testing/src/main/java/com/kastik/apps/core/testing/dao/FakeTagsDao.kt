package com.kastik.apps.core.testing.dao

import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.database.entities.TagEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeTagsDao : TagsDao {

    private val _tags: MutableStateFlow<List<TagEntity>> = MutableStateFlow(emptyList())

    override suspend fun upsertTags(tags: List<TagEntity>) {
        _tags.update { current ->
            val filteredCurrent = current.filter { existing ->
                tags.none { new -> new.id == existing.id }
            }
            filteredCurrent + tags
        }
    }

    override fun getTags(): Flow<List<TagEntity>> {
        return _tags
    }

    override suspend fun clearTags() {
        _tags.update {
            emptyList()
        }
    }
}