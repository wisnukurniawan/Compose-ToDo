package com.wisnu.kurniawan.composetodolist.features.logout.data

import com.wisnu.kurniawan.composetodolist.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ILogoutEnvironment {
    val dispatcher: CoroutineDispatcher
    suspend fun logout()
    fun getUser(): Flow<User>
}
