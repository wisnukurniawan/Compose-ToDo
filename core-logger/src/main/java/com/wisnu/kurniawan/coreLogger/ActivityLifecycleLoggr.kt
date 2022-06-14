package com.wisnu.kurniawan.coreLogger

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Parcel

class ActivityLifecycleLoggr : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) {
        Loggr.record { "$TAG - ${activity.className()} onPause()" }
    }

    override fun onActivityResumed(activity: Activity) {
        Loggr.record { "$TAG - ${activity.className()} onResume()" }
    }

    override fun onActivityStarted(activity: Activity) {
        Loggr.record { "$TAG - ${activity.className()} onStart()" }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Loggr.record { "$TAG - ${activity.className()} onSaveInstanceState(outState: $outState, sizeKb: ${outState.dataSize().toKb()})" }
    }

    override fun onActivityStopped(activity: Activity) {
        Loggr.record { "$TAG - ${activity.className()} onStop()" }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Loggr.record { "$TAG - ${activity.className()} onCreate(savedInstanceState: $savedInstanceState, sizeKb: ${savedInstanceState.dataSize().toKb()})" }
    }

    override fun onActivityDestroyed(activity: Activity) {
        Loggr.record { "$TAG - ${activity.className()} onDestroy()" }
    }

    private fun Activity?.className() = this?.javaClass?.canonicalName.orEmpty()

    private fun Int.toKb(): Float {
        return this.toFloat() / 1000f
    }

    /**
     * Source from:
     *      https://github.com/guardian/toolargetool/blob/master/toolargetool
     *      /src/main/java/com/gu/toolargetool/TooLargeTool.kt#L162
     */
    private fun Bundle?.dataSize(): Int {
        if (this == null) return 0

        val parcel = Parcel.obtain()

        return try {
            parcel.writeBundle(this)
            parcel.dataSize()
        } catch (e: Exception) {
            0
        } finally {
            parcel.recycle()
        }
    }

    companion object {
        private const val TAG = "ActivityLifecycleLoggr"

        fun bind(application: Application) {
            application.registerActivityLifecycleCallbacks(
                ActivityLifecycleLoggr()
            )
        }
    }
}

