package com.wisnu.kurniawan.composetodolist.features.logout.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.PreferenceManager
import com.wisnu.kurniawan.composetodolist.model.Credential
import com.wisnu.kurniawan.composetodolist.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutEnvironment @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ILogoutEnvironment {
    override suspend fun logout() {
        preferenceManager.setCredential(Credential(token = ""))
        preferenceManager.setUser(User(email = ""))
    }

    override fun getUser(): Flow<User> {
        return preferenceManager.getUser()
    }

}
