package com.wisnu.kurniawan.composetodolist.foundation.uiextension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow

@Composable
fun <T : R, R> Flow<T>.collectAsEffect(): State<R?> = collectAsState(initial = null)
