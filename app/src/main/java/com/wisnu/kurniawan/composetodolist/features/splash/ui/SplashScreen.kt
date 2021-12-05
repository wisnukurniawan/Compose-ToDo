package com.wisnu.kurniawan.composetodolist.features.splash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.collectAsEffect
import com.wisnu.kurniawan.composetodolist.runtime.navigation.AuthFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.HomeFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.MainFlow

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel
) {
    val effect by viewModel.effect.collectAsEffect()
    when (effect) {
        SplashEffect.NavigateToDashboard -> {
            LaunchedEffect(effect) {
                navController.navigate(HomeFlow.Root.route) {
                    popUpTo(MainFlow.Root.route) {
                        inclusive = true
                    }
                }
            }
        }
        SplashEffect.NavigateToLogin -> {
            LaunchedEffect(effect) {
                navController.navigate(AuthFlow.Root.route) {
                    popUpTo(MainFlow.Root.route) {
                        inclusive = true
                    }
                }
            }
        }
        null -> {}
    }
}
