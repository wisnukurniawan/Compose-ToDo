package com.wisnu.kurniawan.composetodolist.foundation.analytic

import com.wisnu.kurniawan.composetodolist.foundation.analytic.google.GoogleAnalytics
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
class AnalyticManager @Inject constructor(
    private val googleAnalytics: GoogleAnalytics
) : AnalyticsApi {
    override fun setUser(properties: Map<String, String>) {
        GlobalScope.launch(Dispatchers.IO) {
            googleAnalytics.setUser(properties)
        }
    }

    override fun updateUser(properties: Map<String, String>) {
        GlobalScope.launch(Dispatchers.IO) {
            googleAnalytics.updateUser(properties)
        }
    }

    override fun trackEvent(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            googleAnalytics.trackEvent(name)
        }
    }

    override fun trackEvent(name: String, properties: Map<String, String>) {
        GlobalScope.launch(Dispatchers.IO) {
            googleAnalytics.trackEvent(name, properties)
        }
    }

    override fun flush() {
        GlobalScope.launch(Dispatchers.IO) {
            googleAnalytics.flush()
        }
    }
}
