package com.wisnu.kurniawan.coreLogger

object Logger {

    private lateinit var loggingList: List<Logging>

    fun initialize(logging: List<Logging>) {
        loggingList = logging
    }

    fun log(
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
