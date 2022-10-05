package com.wisnu.kurniawan.composetodolist.features.logout.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.logout.data.ILogoutEnvironment
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(logoutEnvironment: ILogoutEnvironment) :
    StatefulViewModel<LogoutState, LogoutEffect, LogoutAction, ILogoutEnvironment>(LogoutState(), logoutEnvironment) {

    init {
        initUser()
    }

    override fun dispatch(action: LogoutAction) {
        when (action) {
            is LogoutAction.ClickLogout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            environment.logout()
            setEffect(LogoutEffect.NavigateToSplash)
        }
    }

    private fun initUser() {
        viewModelScope.launch {
            environment.getUser()
                .collect { setState { copy(user = it) } }
        }
    }

}
