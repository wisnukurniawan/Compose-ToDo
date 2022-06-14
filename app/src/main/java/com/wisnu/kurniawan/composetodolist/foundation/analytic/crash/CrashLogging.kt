package com.wisnu.kurniawan.composetodolist.foundation.analytic.crash

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wisnu.kurniawan.coreLogger.Logging

class CrashLogging : Logging {
    override fun log(priority: Int, tag: String, message: String, throwable: Throwable?) {
        if (priority == Log.ASSERT) {
            FirebaseCrashlytics.getInstance().log(message)
            if (throwable != null) {
                FirebaseCrashlytics.getInstance().recordException(throwable)
            }
        }
    }
}
