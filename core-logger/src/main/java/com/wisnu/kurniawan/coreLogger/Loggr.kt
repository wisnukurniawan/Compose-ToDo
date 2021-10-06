package com.wisnu.kurniawan.coreLogger

import android.util.Log

object Loggr {

    private lateinit var loggingList: List<Logging>

    fun initialize(logging: List<Logging>) {
        loggingList = logging
    }

    fun debug(tag: String? = null, message: () -> Any?) {
        log(priority = Log.DEBUG, tag = tag, message = message)
    }

    fun error(tag: String? = null, throwable: Throwable? = null, message: () -> Any?) {
        log(priority = Log.ERROR, tag = tag, message = message, throwable = throwable)
    }

    fun verbose(tag: String? = null, message: () -> Any?) {
        log(priority = Log.VERBOSE, tag = tag, message = message)
    }

    fun warn(tag: String? = null, message: () -> Any?) {
        log(priority = Log.WARN, tag = tag, message = message)
    }

    fun info(tag: String? = null, message: () -> Any?) {
        log(priority = Log.INFO, tag = tag, message = message)
    }

    fun record(tag: String? = null, throwable: Throwable? = null, message: () -> Any?) {
        log(priority = Log.ASSERT, tag = tag, message = message, throwable = throwable)
    }

    private fun log(
        priority: Int,
        tag: String? = null,
        message: () -> Any?,
        throwable: Throwable? = null
    ) {
        val tagOrCaller = tag ?: "Loggr"
        log(
            priority,
            tagOrCaller,
            message()?.toString() ?: "null",
            throwable
        )
    }

    private fun log(
        priority: Int,
        tag: String,
        message: String,
        throwable: Throwable? = null
    ) {
        loggingList.forEach {
            it.log(priority, tag, message, throwable)
        }
    }

}
