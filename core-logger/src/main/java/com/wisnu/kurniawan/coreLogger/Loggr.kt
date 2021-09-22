package com.wisnu.kurniawan.coreLogger

import android.util.Log

object Loggr {

    private lateinit var loggingList: List<Logging>

    fun initialize(logging: List<Logging>) {
        loggingList = logging
    }

    fun debug(message: () -> Any?) {
        log(Log.DEBUG, message)
    }

    fun error(throwable: Throwable? = null, message: () -> Any?) {
        log(Log.ERROR, message, throwable)
    }

    fun verbose(message: () -> Any?) {
        log(Log.VERBOSE, message)
    }

    fun warn(message: () -> Any?) {
        log(Log.WARN, message)
    }

    fun info(message: () -> Any?) {
        log(Log.INFO, message)
    }

    fun record(throwable: Throwable? = null, message: () -> Any?) {
        log(Log.ASSERT, message, throwable)
    }

    private fun log(
        priority: Int,
        message: () -> Any?,
        throwable: Throwable? = null
    ) {
        loggingList.forEach {
            it.log(priority, message()?.toString() ?: "null", throwable)
        }
    }

}
