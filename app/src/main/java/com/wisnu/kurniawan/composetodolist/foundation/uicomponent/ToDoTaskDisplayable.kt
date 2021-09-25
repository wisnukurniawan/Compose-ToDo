package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.displayable
import com.wisnu.kurniawan.composetodolist.foundation.extension.formatDateTime
import com.wisnu.kurniawan.composetodolist.foundation.extension.isExpired
import com.wisnu.kurniawan.composetodolist.foundation.extension.isSameDay
import com.wisnu.kurniawan.composetodolist.foundation.extension.isSameHour
import com.wisnu.kurniawan.composetodolist.foundation.extension.isSameMinute
import com.wisnu.kurniawan.composetodolist.foundation.extension.isTomorrow
import com.wisnu.kurniawan.composetodolist.foundation.extension.isYesterday
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun ToDoTask.itemInfoDisplayable(): AnnotatedString? {
    val info = mutableListOf<String>()
    val totalStepInfo = totalStepInfo()
    val dueDateInfo = dueDateInfo()

    if (totalStepInfo.isNotBlank()) info.add(totalStepInfo)
    if (dueDateInfo.isNotBlank()) info.add(dueDateInfo)

    if (info.isEmpty()) return null

    val fullText = info.joinToString("・")

    return AnnotatedString(
        text = fullText,
        spanStyles = if (isExpired()) {
            listOf(
                AnnotatedString.Range(
                    SpanStyle(color = MaterialTheme.colors.error),
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

@Composable
fun ToDoTask.dueDateDisplayable(currentDate: LocalDateTime = LocalDateTime.now()): String? {
    return if (dueDate != null) {
        when {
            dueDate.isSameDay(currentDate) -> stringResource(R.string.todo_task_due_date_today)
            dueDate.isTomorrow(currentDate) -> stringResource(R.string.todo_task_due_date_tomorrow)
            dueDate.isYesterday(currentDate) -> stringResource(R.string.todo_task_due_date_yesterday)
            else -> stringResource(R.string.todo_task_due_date, dueDate.formatDateTime())
        }
    } else {
        null
    }
}

@Composable
fun ToDoTask.timeDisplayable(): String? {
    return if (isDueDateTimeSet) {
        dueDate?.toLocalTime().toString()
    } else {
        null
    }
}

@Composable
fun ToDoTask.dateTimeDisplayable(currentDate: LocalDateTime = LocalDateTime.now()): String {
    return when (status) {
        ToDoStatus.IN_PROGRESS -> {
            if (createdAt.isSameDay(currentDate)) {
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
            } else {
                stringResource(R.string.todo_task_in_progress_date_creation_date, createdAt.formatDateTime())
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
private fun ToDoTask.totalStepInfo(): String {
    val totalStep = steps.size
    return if (totalStep != 0) {
        val totalStepDone = steps.filter { it.status == ToDoStatus.COMPLETE }.size
        stringResource(R.string.todo_task_item_info, totalStepDone.toString(), totalStep.toString())
    } else {
        ""
    }
}

@Composable
private fun ToDoTask.dueDateInfo(): String {
    val info = StringBuilder()
    val dueDateInfo = dueDateDisplayable()
    val timeInfo = timeDisplayable()

    return if (dueDateInfo != null) {
        info.append(dueDateInfo)
        if (timeInfo != null) info.append(" $timeInfo")
        if (repeat != ToDoRepeat.NEVER) info.append(", ${stringResource(repeat.displayable())}")
        info.toString()
    } else {
        ""
    }
}
