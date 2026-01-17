package com.kastik.apps.core.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity


data class AnnouncementDetailRelation(
    @Embedded val announcement: AnnouncementEntity,

    @Relation(parentColumn = "id", entityColumn = "announcementId") val body: BodyEntity,

    @Relation(
        parentColumn = "authorId", entityColumn = "id"
    ) val author: AuthorEntity,

    @Relation(
        parentColumn = "id", entityColumn = "id", associateBy = Junction(
            TagsCrossRefEntity::class, parentColumn = "announcementId", entityColumn = "tagId"
        )
    ) val tags: List<TagEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "announcementId"
    ) val attachments: List<AttachmentEntity>
)