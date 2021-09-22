package com.wisnu.kurniawan.composetodolist.features.localized.base.data

import com.wisnu.kurniawan.composetodolist.model.Language
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ILocalizedEnvironment {
    val dispatcher: CoroutineDispatcher
    fun getLanguage(): Flow<Language>
}
