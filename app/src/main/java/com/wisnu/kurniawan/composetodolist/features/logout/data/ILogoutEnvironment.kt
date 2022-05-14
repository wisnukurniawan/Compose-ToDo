package com.wisnu.kurniawan.composetodolist.features.logout.data

import com.wisnu.kurniawan.composetodolist.model.User
import kotlinx.coroutines.flow.Flow

interface ILogoutEnvironment {
    suspend fun logout()
    fun getUser(): Flow<User>
}
