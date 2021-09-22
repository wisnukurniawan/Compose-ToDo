package com.wisnu.kurniawan.testDebug

import android.util.Log
import com.wisnu.kurniawan.coreLogger.Logging

class DebugLogging : Logging {

    override fun log(priority: Int, message: String, throwable: Throwable?) {
        val tag = "DebugLogging"
        when (priority) {
            Log.DEBUG -> Log.d(tag, message)
            Log.VERBOSE -> Log.v(tag, message)
            Log.ERROR -> Log.e(tag, message, throwable)
            Log.WARN -> Log.w(tag, message, throwable)
            Log.INFO -> Log.i(tag, message, throwable)
            Log.ASSERT -> Log.wtf(tag, message, throwable)
        }
    }

}
