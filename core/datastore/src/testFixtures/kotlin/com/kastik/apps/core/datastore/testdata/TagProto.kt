package com.kastik.apps.core.datastore.testdata

import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto


private val baseSubscribableTag = SubscribableTagProto.newBuilder()
    .setId(1)
    .setTitle("ABC")
    .setIsPublic(true)
    .build()

val baseSubscribableTagsProto = SubscribableTagsProto.newBuilder()
    .addTags(baseSubscribableTag)
    .build()

private val baseSubscribedTag = SubscribedTagProto.newBuilder()
    .setId(1)
    .setTitle("ABC")
    .build()

val baseSubscriptionsProto = SubscriptionsProto.newBuilder()
    .addTags(baseSubscribedTag)
    .build()

