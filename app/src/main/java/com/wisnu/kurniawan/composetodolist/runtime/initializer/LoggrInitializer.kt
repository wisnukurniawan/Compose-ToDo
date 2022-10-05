package com.wisnu.kurniawan.composetodolist.runtime.initializer

import android.content.Context
import androidx.startup.Initializer
import com.wisnu.foundation.coreloggr.Loggr
import com.wisnu.foundation.libanalyticsmanager.crash.CrashLogging
import com.wisnu.foundation.testdebug.DebugTools

class LoggrInitializer : Initializer<Loggr> {
    override fun create(context: Context): Loggr {
        val loggings = DebugTools.getLoggings().toMutableList()
        loggings.add(CrashLogging())
        Loggr.initialize(loggings)
        return Loggr
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
