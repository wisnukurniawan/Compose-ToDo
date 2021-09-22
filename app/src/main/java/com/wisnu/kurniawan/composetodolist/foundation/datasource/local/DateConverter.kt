package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateConverter {

    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        if (dateString == null) return null

        return LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }

}
