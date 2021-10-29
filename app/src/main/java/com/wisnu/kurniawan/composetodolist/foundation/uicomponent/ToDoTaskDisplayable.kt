package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.displayable
import com.wisnu.kurniawan.composetodolist.foundation.extension.ellipsisAt
import com.wisnu.kurniawan.composetodolist.foundation.extension.formatDateTime
import com.wisnu.kurniawan.composetodolist.foundation.extension.isExpired
import com.wisnu.kurniawan.composetodolist.foundation.extension.isSameDay
import com.wisnu.kurniawan.composetodolist.foundation.extension.isSameHour
import com.wisnu.kurniawan.composetodolist.foundation.extension.isSameMinute
import com.wisnu.kurniawan.composetodolist.foundation.extension.isTomorrow
import com.wisnu.kurniawan.composetodolist.foundation.extension.isYesterday
import com.wisnu.kurniawan.composetodolist.foundation.extension.joinToString
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun ToDoTask.itemInfoDisplayable(resources: Resources, expiredColor: Color, listName: String = ""): AnnotatedString? {
    val info = mutableListOf<String>()
    val noteInfo = note.ellipsisAt(255)
    val totalStepInfo = totalStepInfo(resources)
    val dueDateInfo = dueDateInfo(resources)

    if (noteInfo.isNotBlank()) info.add(noteInfo)
    if (listName.isNotBlank()) info.add(listName)
    if (totalStepInfo.isNotBlank()) info.add(totalStepInfo)
    if (dueDateInfo.isNotBlank()) info.add(dueDateInfo)

    if (info.isEmpty()) return null

    val fullText = info.joinToString {
        if (it == 1 && noteInfo.isNotBlank()) {
            "\n"
        } else {
            "・"
        }
    }

    return AnnotatedString(
        text = fullText,
        spanStyles = if (isExpired()) {
            listOf(
                AnnotatedString.Range(
                    SpanStyle(color = expiredColor),
                    fullText.indexOf(dueDateInfo),
                    fullText.indexOf(dueDateInfo) + dueDateInfo.length
                )
            )
        } else {
            listOf()
        },
        paragraphStyles = listOf()
    )
}

fun ToDoTask.dueDateDisplayable(resources: Resources, currentDate: LocalDateTime = DateTimeProviderImpl().now()): String? {
    return if (dueDate != null) {
        when {
            dueDate.isSameDay(currentDate) -> resources.getString(R.string.todo_task_due_date_today)
            dueDate.isTomorrow(currentDate) -> resources.getString(R.string.todo_task_due_date_tomorrow)
            dueDate.isYesterday(currentDate) -> resources.getString(R.string.todo_task_due_date_yesterday)
            else -> resources.getString(R.string.todo_task_due_date, dueDate.formatDateTime())
        }
    } else {
        null
    }
}

fun LocalDate.headerDateDisplayable(resources: Resources, currentDate: LocalDateTime = DateTimeProviderImpl().now()): String {
    val date = LocalDateTime.of(this, LocalTime.MIN)
    return when {
        date.isSameDay(currentDate) -> resources.getString(R.string.todo_task_today)
        date.isTomorrow(currentDate) -> resources.getString(R.string.todo_task_tomorrow)
        date.isYesterday(currentDate) -> resources.getString(R.string.todo_task_yesterday)
        else -> date.formatDateTime()
    }
}

fun ToDoTask.timeDisplayable(): String? {
    return if (isDueDateTimeSet) {
        dueDate?.toLocalTime().toString()
    } else {
        null
    }
}

@Composable
fun ToDoTask.dateTimeDisplayable(currentDate: LocalDateTime = DateTimeProviderImpl().now()): String {
    return when (status) {
        ToDoStatus.IN_PROGRESS -> {
            when {
                createdAt.isSameDay(currentDate) -> {
                    when {
                        createdAt.isSameMinute(currentDate) -> {
                            stringResource(R.string.todo_task_in_progress_date_creation_second)
                        }
                        createdAt.isSameHour(currentDate) -> {
                            val minutes = ChronoUnit.MINUTES.between(createdAt, currentDate)
                            stringResource(R.string.todo_task_in_progress_date_creation_minute, minutes.toString())
                        }
                        else -> {
                            val hours = ChronoUnit.HOURS.between(createdAt, currentDate)
                            stringResource(R.string.todo_task_in_progress_date_creation_hour, hours.toString())
                        }
                    }
                }
                createdAt.isYesterday(currentDate) -> stringResource(R.string.todo_task_in_progress_date_creation_date, createdAt.formatDateTime())
                else -> stringResource(R.string.todo_task_in_progress_date_creation_yesterday)
            }
        }
        ToDoStatus.COMPLETE -> {
            when {
                completedAt == null -> ""
                completedAt.isSameDay(currentDate) -> stringResource(R.string.todo_task_complete_date_today)
                completedAt.isYesterday(currentDate) -> stringResource(R.string.todo_task_complete_date_yesterday)
                else -> stringResource(R.string.todo_task_complete_date, completedAt.formatDateTime())
            }
        }
    }
}

@Composable
fun ToDoTask.noteUpdatedAtDisplayable(currentDate: LocalDateTime = DateTimeProviderImpl().now()): String {
    return when {
        noteUpdatedAt == null -> ""
        noteUpdatedAt.isSameDay(currentDate) -> {
            when {
                noteUpdatedAt.isSameMinute(currentDate) -> {
                    stringResource(R.string.todo_task_note_update_second)
                }
                noteUpdatedAt.isSameHour(currentDate) -> {
                    val minutes = ChronoUnit.MINUTES.between(noteUpdatedAt, currentDate)
                    stringResource(R.string.todo_task_note_update_minute, minutes.toString())
                }
                else -> {
                    val hours = ChronoUnit.HOURS.between(noteUpdatedAt, currentDate)
                    stringResource(R.string.todo_task_note_update_hour, hours.toString())
                }
            }
        }
        noteUpdatedAt.isYesterday(currentDate) -> stringResource(R.string.todo_task_note_update_yesterday, createdAt.formatDateTime())
        else -> stringResource(R.string.todo_task_note_update_date, noteUpdatedAt.formatDateTime())
    }
}

private fun ToDoTask.totalStepInfo(resources: Resources): String {
    val totalStep = steps.size
    return if (totalStep != 0) {
        val totalStepDone = steps.filter { it.status == ToDoStatus.COMPLETE }.size
        resources.getString(R.string.todo_task_item_info, totalStepDone.toString(), totalStep.toString())
    } else {
        ""
    }
}

private fun ToDoTask.dueDateInfo(resources: Resources): String {
    val info = StringBuilder()
    val dueDateInfo = dueDateDisplayable(resources)
    val timeInfo = timeDisplayable()

    return if (dueDateInfo != null) {
        info.append(dueDateInfo)
        if (timeInfo != null) info.append(" $timeInfo")
        if (repeat != ToDoRepeat.NEVER) info.append(", ${resources.getString(repeat.displayable())}")
        info.toString()
    } else {
        ""
    }
}
