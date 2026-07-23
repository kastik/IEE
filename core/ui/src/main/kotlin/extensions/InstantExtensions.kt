package com.kastik.apps.core.ui.extensions

import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun Instant.toFormattedString(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())

    val customFormat = LocalDateTime.Format {
        monthNumber()
        char('/')
        dayOfMonth()
        char('/')
        year()
        char(' ')
        hour()
        char(':')
        minute()
    }

    return localDateTime.format(customFormat)
}
