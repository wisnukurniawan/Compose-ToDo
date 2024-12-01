package com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.provider

import androidx.datastore.core.DataStore
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.mapper.toTheme
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.mapper.toThemePreference
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserThemePreference
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.model.Theme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ThemeProvider @Inject constructor(
    @Named(DiName.DISPATCHER_IO) private val dispatcher: CoroutineDispatcher,
    private val themeDataStore: DataStore<UserThemePreference>,
) {

    fun getTheme(): Flow<Theme> {
        return themeDataStore.data.map { it.theme.toTheme() }
            .catch { emit(Theme.SYSTEM) }
            .flowOn(dispatcher)
    }

    suspend fun setTheme(data: Theme) {
        withContext(dispatcher) {
            themeDataStore.updateData {
                UserThemePreference
                    .newBuilder()
                    .setTheme(data.toThemePreference())
                    .build()

            }
        }
    }

}
