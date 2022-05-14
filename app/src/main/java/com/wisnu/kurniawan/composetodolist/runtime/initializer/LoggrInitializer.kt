package com.wisnu.kurniawan.composetodolist.runtime.initializer

import android.content.Context
import androidx.startup.Initializer
import com.wisnu.kurniawan.coreLogger.Loggr
import com.wisnu.kurniawan.testDebug.DebugTools

class LoggrInitializer : Initializer<Loggr> {
    override fun create(context: Context): Loggr {
        Loggr.initialize(DebugTools.getLoggings())
        return Loggr
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
