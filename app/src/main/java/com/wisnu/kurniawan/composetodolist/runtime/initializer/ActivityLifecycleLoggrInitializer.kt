package com.wisnu.kurniawan.composetodolist.runtime.initializer

import android.content.Context
import androidx.startup.Initializer
import com.wisnu.kurniawan.composetodolist.runtime.ComposeToDoListApp
import com.wisnu.kurniawan.coreLogger.ActivityLifecycleLoggr

class ActivityLifecycleLoggrInitializer : Initializer<ActivityLifecycleLoggr> {
    override fun create(context: Context): ActivityLifecycleLoggr {
        return ActivityLifecycleLoggr().also {
            (context.applicationContext as ComposeToDoListApp)
                .registerActivityLifecycleCallbacks(it)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(LoggrInitializer::class.java)
}
