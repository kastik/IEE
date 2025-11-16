package com.kastik.apps.core.database.model

import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity

data class AnnouncementEntityWrapper(
    val announcement: AnnouncementEntity,
    val body: BodyEntity,
    val author: AuthorEntity,
    val tags: List<TagEntity>,
    val tagCrossRefs: List<TagsCrossRefEntity>,
    val attachments: List<AttachmentEntity>,
)