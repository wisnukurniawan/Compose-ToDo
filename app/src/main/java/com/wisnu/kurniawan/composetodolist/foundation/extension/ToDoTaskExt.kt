package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun List<ToDoTask>.toTaskDb(listId: String): List<ToDoTaskDb> {
    return map {
        ToDoTaskDb(
            id = it.id,
            name = it.name,
            status = it.status,
            listId = listId,
            dueDate = it.dueDate,
            completedAt = it.completedAt,
            isDueDateTimeSet = it.isDueDateTimeSet,
            repeat = it.repeat,
            note = it.note,
            noteUpdatedAt = it.noteUpdatedAt,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt
        )
    }
}

fun ToDoTask.newStatus(): ToDoStatus {
    return when (status) {
        ToDoStatus.IN_PROGRESS -> ToDoStatus.COMPLETE
        ToDoStatus.COMPLETE -> ToDoStatus.IN_PROGRESS
    }
}

fun ToDoTask.isDueDateSet(): Boolean = this.dueDate != null

fun ToDoTask.updatedDate(newLocalDate: LocalDate): LocalDateTime {
    val localTime = dueDate?.toLocalTime() ?: defaultTaskLocalTime()
    return LocalDateTime.of(newLocalDate, localTime)
}

fun ToDoTask.updatedTime(defaultDate: LocalDate, newLocalTime: LocalTime): LocalDateTime {
    val localDate = dueDate?.toLocalDate() ?: defaultDate
    return LocalDateTime.of(localDate, newLocalTime)
}

fun defaultTaskLocalTime(): LocalTime {
    return LocalTime.of(23, 59)
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
        val newStatus = newStatus()
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
