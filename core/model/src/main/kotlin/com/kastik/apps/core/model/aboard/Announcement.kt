package com.kastik.apps.core.model.aboard

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant

data class Announcement(
    val id: Int,
    val title: String,
    val body: String,
    val preview: String,
    val date: Instant,
    val isPinned: Boolean,
    val author: String,
    val tags: ImmutableList<Tag> = persistentListOf(),
    val attachments: ImmutableList<Attachment> = persistentListOf(),
)