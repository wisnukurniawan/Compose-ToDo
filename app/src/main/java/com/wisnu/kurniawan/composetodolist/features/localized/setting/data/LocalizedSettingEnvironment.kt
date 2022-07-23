package com.wisnu.kurniawan.composetodolist.features.localized.setting.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.PreferenceManager
import com.wisnu.kurniawan.composetodolist.model.Language
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalizedSettingEnvironment @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ILocalizedSettingEnvironment {

    override fun getLanguage(): Flow<Language> {
        return preferenceManager.getLanguage()
    }

    override suspend fun setLanguage(language: Language) {
        preferenceManager.setLanguage(language)
    }

}
