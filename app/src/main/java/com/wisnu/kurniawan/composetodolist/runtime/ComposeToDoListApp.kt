package com.wisnu.kurniawan.composetodolist.runtime

import android.app.Application
import com.wisnu.kurniawan.coreLogger.Logger
import com.wisnu.kurniawan.testDebug.DebugTools
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComposeToDoListApp : Application() {

    override fun onCreate() {
        initDebugTools()
        super.onCreate()

        initLogger()
    }

    private fun initDebugTools() {
        DebugTools.init()
    }

    private fun initLogger() {
        Logger.initialize(DebugTools.getLoggings())
    }

}
