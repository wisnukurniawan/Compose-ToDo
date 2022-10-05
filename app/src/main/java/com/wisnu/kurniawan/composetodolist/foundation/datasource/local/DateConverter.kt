package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import androidx.room.TypeConverter
import com.wisnu.foundation.coredatetime.toLocalDateTime
import com.wisnu.foundation.coredatetime.toMillis
import java.time.LocalDateTime

class DateConverter {

    @TypeConverter
    fun toDate(date: Long?): LocalDateTime? {
        if (date == null) return null

        return date.toLocalDateTime()
    }

    @TypeConverter
    fun toDateLong(date: LocalDateTime?): Long? {
        if (date == null) return null

        return date.toMillis()
    }

}
