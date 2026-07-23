package com.kastik.apps.core.model.search

import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FilterOptions(
    val tags: ImmutableList<Tag> = persistentListOf(),
    val authors: ImmutableList<Author> = persistentListOf(),
)
