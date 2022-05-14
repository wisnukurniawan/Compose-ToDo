package com.wisnu.kurniawan.composetodolist.features.splash.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.PreferenceManager
import com.wisnu.kurniawan.composetodolist.model.Credential
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SplashEnvironment @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ISplashEnvironment {

    override fun getCredential(): Flow<Credential> {
        return preferenceManager.getCredential()
    }

}
