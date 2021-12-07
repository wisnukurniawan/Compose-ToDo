package com.wisnu.kurniawan.composetodolist.runtime

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.wisnu.kurniawan.composetodolist.features.host.ui.Host
import com.wisnu.kurniawan.composetodolist.features.localized.base.ui.LocalizedActivity
import com.wisnu.kurniawan.composetodolist.foundation.window.WindowState
import com.wisnu.kurniawan.composetodolist.foundation.window.rememberWindowState
import com.wisnu.kurniawan.composetodolist.runtime.navigation.MainNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : LocalizedActivity() {
    private lateinit var windowState: WindowState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            windowState = rememberWindowState()

            ProvideWindowInsets {
                Host {
                    Surface {
                        MainNavHost(windowState)
                    }
                }
            }
        }
    }
}

