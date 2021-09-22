package com.wisnu.kurniawan.testDebug

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.wisnu.kurniawan.coreLogger.Logging
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor


object DebugTools {

    fun init() {
        initStrictMode()
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )

        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
    }

    fun getLoggings(): List<Logging> {
        return listOf(
            DebugLogging()
        )
    }

    fun getInterceptors(context: Context): List<Interceptor> {
        return listOf(
            buildChuckerInterceptor(context),
            builtHttpLoggingInterceptor()
        )
    }

    private fun builtHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun buildChuckerInterceptor(context: Context): Interceptor {
        return ChuckerInterceptor
            .Builder(context)
            .alwaysReadResponseBody(true)
            .build()
    }

}
