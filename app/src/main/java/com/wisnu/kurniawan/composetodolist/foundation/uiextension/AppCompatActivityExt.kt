package com.wisnu.kurniawan.composetodolist.foundation.uiextension

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

private const val DATE_PICKER_TAG = "date_picker"
private const val TIME_PICKER_TAG = "time_picker"

fun AppCompatActivity.showDatePicker(
    selection: LocalDate? = null,
    selectedDate: (LocalDate) -> Unit
) {
    val zoneId = ZoneId.ofOffset("UTC", ZoneOffset.UTC)
    val picker = MaterialDatePicker
        .Builder
        .datePicker()
        .apply {
            if (selection != null) {
                setSelection(selection.atStartOfDay(zoneId).toInstant().toEpochMilli())
            }
        }
        .build()

    picker.show(supportFragmentManager, DATE_PICKER_TAG)
    picker.addOnPositiveButtonClickListener {
        selectedDate(
            Instant
                .ofEpochMilli(it)
                .atZone(zoneId)
                .toLocalDate()
        )
    }
}

fun AppCompatActivity.showTimePicker(
    time: LocalTime? = null,
    selectedTime: (LocalTime) -> Unit
) {
    val picker = MaterialTimePicker
        .Builder()
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .apply {
            if (time != null) {
                setHour(time.hour)
                setMinute(time.minute)
            } else {
                setHour(9)
            }
        }
        .build()

    picker.show(supportFragmentManager, TIME_PICKER_TAG)
    picker.addOnPositiveButtonClickListener {
        selectedTime(LocalTime.of(picker.hour, picker.minute))
    }
}
