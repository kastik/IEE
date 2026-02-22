package com.kastik.apps.core.data.mappers

import com.google.protobuf.Timestamp
import kotlin.time.Instant

fun Timestamp.toInstant() = Instant.fromEpochSeconds(
    this.seconds,
    this.nanos
)

fun Instant.toTimestamp() = Timestamp.newBuilder()
    .setSeconds(this.epochSeconds)
    .setNanos(this.nanosecondsOfSecond)
    .build()