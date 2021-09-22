package com.wisnu.kurniawan.composetodolist.features.splash.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.PreferenceManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.model.Credential
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class SplashEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val preferenceManager: PreferenceManager
) : ISplashEnvironment {

    override fun getCredential(): Flow<Credential> {
        return preferenceManager.getCredential()
    }

}
