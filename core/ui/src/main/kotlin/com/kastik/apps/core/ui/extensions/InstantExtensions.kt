package com.kastik.apps.core.ui.extensions

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import java.text.DateFormatSymbols
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

@Composable
fun Instant.toFormattedString(): String {
    val locale = LocalLocale.current
    val context = LocalContext.current

    val localDateTime = this.toLocalDateTime(TimeZone.UTC)
    val isSystem24Hour = DateFormat.is24HourFormat(context)

    val customFormat = remember(isSystem24Hour, locale) {

        val amPmStrings = DateFormatSymbols.getInstance(locale.platformLocale).amPmStrings
        val localAm = amPmStrings[0]
        val localPm = amPmStrings[1]

        LocalDateTime.Format {
            day()
            char('/')
            monthNumber()
            char('/')
            year()
            char(' ')

            if (isSystem24Hour) {
                hour()
                char(':')
                minute()
            } else {
                amPmHour()
                char(':')
                minute()
                char(' ')
                amPmMarker(localAm, localPm)
            }
        }
    }

    return localDateTime.format(customFormat)
}