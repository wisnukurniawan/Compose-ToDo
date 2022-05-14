package com.wisnu.kurniawan.composetodolist.features.theme.data

import com.wisnu.kurniawan.composetodolist.model.Theme
import kotlinx.coroutines.flow.Flow

interface IThemeEnvironment {
    fun getTheme(): Flow<Theme>
    suspend fun setTheme(theme: Theme)
}
