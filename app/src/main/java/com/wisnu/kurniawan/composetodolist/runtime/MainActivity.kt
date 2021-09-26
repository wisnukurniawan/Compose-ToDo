package com.wisnu.kurniawan.composetodolist.runtime

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.wisnu.kurniawan.composetodolist.features.host.ui.Host
import com.wisnu.kurniawan.composetodolist.features.localized.base.ui.LocalizedActivity
import com.wisnu.kurniawan.composetodolist.runtime.navigation.MainNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : LocalizedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Host {
                ProvideWindowInsets {
                    Surface {
                        MainNavHost()
                    }
                }
            }
        }
    }
}

