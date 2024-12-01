package com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.provider

import androidx.datastore.core.DataStore
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserPreference
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UserProvider @Inject constructor(
    @Named(DiName.DISPATCHER_IO) private val dispatcher: CoroutineDispatcher,
    private val userDataStore: DataStore<UserPreference>,
) {

    fun getUser(): Flow<User> {
        return userDataStore.data
            .map { User(it.email) }
            .catch { emit(User(email = "")) }
            .flowOn(dispatcher)
    }

    suspend fun setUser(data: User) {
        withContext(dispatcher) {
            userDataStore.updateData {
                UserPreference
                    .newBuilder()
                    .setEmail(data.email)
                    .build()
            }
        }
    }

}
