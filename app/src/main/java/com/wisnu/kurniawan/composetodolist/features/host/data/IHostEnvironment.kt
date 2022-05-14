package com.wisnu.kurniawan.composetodolist.features.host.data

import com.wisnu.kurniawan.composetodolist.model.Theme
import kotlinx.coroutines.flow.Flow

interface IHostEnvironment {
    fun getTheme(): Flow<Theme>
}
