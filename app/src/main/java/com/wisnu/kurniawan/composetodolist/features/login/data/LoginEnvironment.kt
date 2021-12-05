package com.wisnu.kurniawan.composetodolist.features.login.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.PreferenceManager
import com.wisnu.kurniawan.composetodolist.foundation.datasource.server.ServerManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.model.Credential
import com.wisnu.kurniawan.composetodolist.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

class LoginEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val serverManager: ServerManager,
    private val preferenceManager: PreferenceManager
) : ILoginEnvironment {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun login(email: String, password: String): Flow<Any> {
        return merge(
            serverManager.fetchCredential(),
            serverManager.fetchUser(email, password)
        ).onEach {
            when (it) {
                is Credential -> preferenceManager.setCredential(it)
                is User -> preferenceManager.setUser(it)
            }
        }
    }

}
