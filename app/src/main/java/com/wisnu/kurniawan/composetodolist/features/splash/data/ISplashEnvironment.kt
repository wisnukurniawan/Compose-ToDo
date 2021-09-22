package com.wisnu.kurniawan.composetodolist.features.splash.data

import com.wisnu.kurniawan.composetodolist.model.Credential
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ISplashEnvironment {
    val dispatcher: CoroutineDispatcher
    fun getCredential(): Flow<Credential>
}
