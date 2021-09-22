package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.localized.setting.ui.LanguageItem
import com.wisnu.kurniawan.composetodolist.model.Language

fun List<LanguageItem>.update(language: Language): List<LanguageItem> {
    return map {
        it.copy(applied = it.language == language)
    }
}
