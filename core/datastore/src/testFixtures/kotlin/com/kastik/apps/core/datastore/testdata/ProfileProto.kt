package com.kastik.apps.core.datastore.testdata

import com.google.protobuf.Timestamp
import com.kastik.apps.core.datastore.proto.ProfileProto

val baseProfileProto =
    ProfileProto.newBuilder()
        .setId(1)
        .setUid("it192168")
        .setName("Some Name")
        .setEmail("someemail@domain.com")
        .setIsAdmin(false)
        .setIsAuthor(false)
        .setCreatedAtTimestamp(Timestamp.getDefaultInstance())
        .setUpdatedAtTimestamp(Timestamp.getDefaultInstance())
        .setDeletedAtTimestamp(Timestamp.getDefaultInstance())
        .setLastLoginAtTimestamp(Timestamp.getDefaultInstance())
        .build()
