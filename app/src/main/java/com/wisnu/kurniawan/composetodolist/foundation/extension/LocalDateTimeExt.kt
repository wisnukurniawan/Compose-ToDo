package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProviderImpl
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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

fun LocalDateTime.formatDateTime(currentDate: LocalDateTime = DateTimeProviderImpl().now()): String {
    val patternWithYear = "EEE, dd MMM yyyy"
    val patternWithoutYear = "EEE, dd MMM"
    val zoneId = ZoneId.systemDefault()

    return if (year == currentDate.year) {
        SimpleDateFormat(patternWithoutYear, Locale.getDefault()).format(atZone(zoneId).toInstant().toEpochMilli())
    } else {
        SimpleDateFormat(patternWithYear, Locale.getDefault()).format(atZone(zoneId).toInstant().toEpochMilli())
    }
}

fun LocalDateTime.toMillis(): Long {
    val zoneId = ZoneId.systemDefault()
    return atZone(zoneId).toInstant().toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime {
    val zoneId = ZoneId.systemDefault()
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), zoneId)
}

fun LocalDateTime.isFriday(): Boolean {
    return dayOfWeek == DayOfWeek.FRIDAY
}
