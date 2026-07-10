package com.kastik.apps.core.datastore.testdata

import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import com.kastik.apps.core.datastore.proto.TagProto


val baseTagProto = TagProto.newBuilder()
    .setId(0)
    .setTitle("Root")
    .setParentId(0)
    .setIsPublic(true)
    .setMailListName("root_mail_list")
    .build()

val baseSubscriptionsProto = SubscriptionsProto.newBuilder()
    .addTags(baseTagProto)
    .build()

val baseSubscribableTagsProto = SubscribableTagsProto.newBuilder()
    .addTags(baseTagProto)
    .build()