package com.wisnu.kurniawan.composetodolist.features.localized.setting.data

import com.wisnu.kurniawan.composetodolist.model.Language
import kotlinx.coroutines.flow.Flow

interface ILocalizedSettingEnvironment {
    fun getLanguage(): Flow<Language>
    suspend fun setLanguage(language: Language)
}

