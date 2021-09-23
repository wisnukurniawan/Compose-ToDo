package com.wisnu.kurniawan.coreLogger

interface Logging {
    fun log(priority: Int, tag: String, message: String, throwable: Throwable?)
}
