package com.wisnu.kurniawan.composetodolist.foundation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun <STATE, EFFECT, ACTION, ENVIRONMENT> HandleEffect(
    viewModel: StatefulViewModel<STATE, EFFECT, ACTION, ENVIRONMENT>,
    handle: suspend CoroutineScope.(EFFECT) -> Unit
) {
    val effect by viewModel.effect.collectAsStateWithLifecycle()
    LaunchedEffect(effect) {
        effect?.let {
            handle(it)
            viewModel.resetEffect()
        }
    }
}
