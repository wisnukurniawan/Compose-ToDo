package com.wisnu.kurniawan.coreLogger

interface Logging {
    fun log(priority: Int, message: String, throwable: Throwable?)
}
