package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val DEFAULT_TASK_LOCAL_TIME: LocalTime = LocalTime.of(23, 59)

fun ToDoTask.isDueDateSet(): Boolean = this.dueDate != null

fun ToDoTask.updatedDate(newLocalDate: LocalDate): LocalDateTime {
    val localTime = dueDate?.toLocalTime() ?: DEFAULT_TASK_LOCAL_TIME
    return LocalDateTime.of(newLocalDate, localTime)
}

fun ToDoTask.updatedTime(defaultDate: LocalDate, newLocalTime: LocalTime): LocalDateTime {
    val localDate = dueDate?.toLocalDate() ?: defaultDate
    return LocalDateTime.of(localDate, newLocalTime)
}

fun ToDoTask.isExpired(currentDate: LocalDateTime = DateTimeProviderImpl().now()): Boolean {
    return dueDate?.isBefore(currentDate) ?: false
}

fun ToDoTask.getNextScheduledDueDate(currentDate: LocalDateTime): LocalDateTime {
    require(dueDate != null)

    val usedDueDate = if (isExpired(currentDate)) {
        LocalDateTime.of(currentDate.toLocalDate(), dueDate.toLocalTime())
    } else {
        dueDate
    }

    return when (repeat) {
        ToDoRepeat.NEVER -> usedDueDate
        ToDoRepeat.DAILY -> usedDueDate.plusDays(1)
        ToDoRepeat.WEEKDAYS -> {
            if (usedDueDate.isFriday()) {
                usedDueDate.plusDays(3) // Monday
            } else {
                usedDueDate.plusDays(1)
            }
        }
        ToDoRepeat.WEEKLY -> usedDueDate.plusWeeks(1)
        ToDoRepeat.MONTHLY -> usedDueDate.plusMonths(1)
        ToDoRepeat.YEARLY -> usedDueDate.plusYears(1)
    }
}

fun ToDoTask.getScheduledDueDate(currentDate: LocalDateTime): LocalDateTime {
    require(dueDate != null)

    return if (repeat != ToDoRepeat.NEVER) {
        if (isExpired(currentDate)) {
            currentDate.plusMinutes(1)
        } else {
            dueDate
        }
    } else {
        dueDate
    }
}

suspend fun ToDoTask.toggleStatusHandler(
    currentDate: LocalDateTime,
    onUpdateStatus: suspend (LocalDateTime?, ToDoStatus) -> Unit,
    onUpdateDueDate: suspend (LocalDateTime) -> Unit,
) {
    if (repeat == ToDoRepeat.NEVER) {
        val newStatus = status.toggle()
        val completedAt = when (newStatus) {
            ToDoStatus.IN_PROGRESS -> null
            ToDoStatus.COMPLETE -> currentDate
        }
        onUpdateStatus(completedAt, newStatus)
    } else {
        val nextDueDate = getNextScheduledDueDate(currentDate)
        onUpdateDueDate(nextDueDate)
    }
}
