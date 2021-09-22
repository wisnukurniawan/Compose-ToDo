package com.wisnu.kurniawan.composetodolist.features.localized.base.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.model.Language

@Immutable
data class LocalizedState(val language: Language? = null)
