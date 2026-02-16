package com.kastik.apps.core.datastore.testdata

import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto

internal fun subscribableTagBuilder() = SubscribableTagProto.newBuilder()
    .setId(0)
    .setTitle("")
    .setIsPublic(true)
    .setCreatedAt("")
    .setMailListName("root_tag_mail_list")

val subscribableTagProtoTestData = listOf(
    subscribableTagBuilder()
        .setId(1)
        .setTitle("Root Tag")
        .addAllSubTags(
            listOf(
                subscribableTagBuilder()
                    .setId(2)
                    .setParentId(1)
                    .setTitle("Sub Tag 1")
                    .addAllSubTags(
                        listOf(
                            subscribableTagBuilder()
                                .setId(4)
                                .setParentId(2)
                                .setTitle("Sub Tag 3")
                                .build(),
                            subscribableTagBuilder()
                                .setId(5)
                                .setParentId(2)
                                .setTitle("Sub Tag 4")
                                .build()
                        )
                    )
                    .build(),
                subscribableTagBuilder()
                    .setId(3)
                    .setParentId(1)
                    .setTitle("Sub Tag 2")
                    .setDeletedAt("20-12-2025")
                    .setCreatedAt("20-12-2025")
                    .build()
            )
        )
        .build()
)

val subscribableTagsProtoTestData: SubscribableTagsProto =
    SubscribableTagsProto.newBuilder()
        .addAllTags(subscribableTagProtoTestData)
        .build()

val subscribedTagProtoTestData = listOf(
    SubscribedTagProto.newBuilder().setId(1).setTitle("Tag 1").build(),
    SubscribedTagProto.newBuilder().setId(2).setTitle("Tag 2").build(),
    SubscribedTagProto.newBuilder().setId(3).setTitle("Tag 3").build(),
    SubscribedTagProto.newBuilder().setId(4).setTitle("Tag 4").build(),
    SubscribedTagProto.newBuilder().setId(5).setTitle("Tag 5").build()
)