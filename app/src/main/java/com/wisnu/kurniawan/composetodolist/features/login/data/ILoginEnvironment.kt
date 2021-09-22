package com.wisnu.kurniawan.composetodolist.features.login.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ILoginEnvironment {
    val dispatcher: CoroutineDispatcher
    fun login(email: String, password: String): Flow<Any>
}
