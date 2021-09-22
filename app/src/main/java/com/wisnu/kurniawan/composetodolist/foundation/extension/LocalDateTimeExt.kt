package com.wisnu.kurniawan.composetodolist.foundation.extension

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*

fun LocalDateTime.isSameDay(dateTime: LocalDateTime): Boolean {
    return toLocalDate().isEqual(dateTime.toLocalDate())
}

fun LocalDateTime.isTomorrow(dateTime: LocalDateTime): Boolean {
    return toLocalDate().isEqual(dateTime.toLocalDate().plusDays(1))
}

fun LocalDateTime.isYesterday(dateTime: LocalDateTime): Boolean {
    return toLocalDate().isEqual(dateTime.toLocalDate().minusDays(1))
}

fun LocalDateTime.isSameMinute(dateTime: LocalDateTime): Boolean {
    return truncatedTo(ChronoUnit.MINUTES).isEqual(dateTime.truncatedTo(ChronoUnit.MINUTES))
}

fun LocalDateTime.isSameHour(dateTime: LocalDateTime): Boolean {
    return truncatedTo(ChronoUnit.HOURS).isEqual(dateTime.truncatedTo(ChronoUnit.HOURS))
}

fun LocalDateTime.formatDateTime(currentDate: LocalDateTime = LocalDateTime.now()): String {
    val patternWithYear = "EEE, dd MMM yyyy"
    val patternWithoutYear = "EEE, dd MMM"
    val zoneId = ZoneId.ofOffset("UTC", ZoneOffset.UTC)

    return if (year == currentDate.year) {
        SimpleDateFormat(patternWithoutYear, Locale.getDefault()).format(atZone(zoneId).toInstant().toEpochMilli())
    } else {
        SimpleDateFormat(patternWithYear, Locale.getDefault()).format(atZone(zoneId).toInstant().toEpochMilli())
    }
}
