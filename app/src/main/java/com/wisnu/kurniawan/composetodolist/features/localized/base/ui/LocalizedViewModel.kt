package com.wisnu.kurniawan.composetodolist.features.localized.base.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.localized.base.data.ILocalizedEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalizedViewModel @Inject constructor(localizedEnvironment: ILocalizedEnvironment) :
    StatefulViewModel<LocalizedState, LocalizedEffect, Unit, ILocalizedEnvironment>(LocalizedState(), localizedEnvironment) {

    init {
        initLanguage()
    }

    override fun dispatch(action: Unit) {

    }

    private fun initLanguage() {
        viewModelScope.launch {
            environment.getLanguage()
                .flowOn(environment.dispatcher)
                .collect {
                    if (state.value.language != null) {
                        setEffect(LocalizedEffect.ApplyLanguage(it))
                    }

                    setState { copy(language = it) }
                }
        }
    }
}
