package com.wisnu.kurniawan.composetodolist.foundation.datasource.preference

import androidx.datastore.core.DataStore
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.CredentialPreference
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.LanguagePreference
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.ThemePreference
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserPreference
import com.wisnu.kurniawan.composetodolist.foundation.extension.toLanguage
import com.wisnu.kurniawan.composetodolist.foundation.extension.toLanguagePreference
import com.wisnu.kurniawan.composetodolist.foundation.extension.toTheme
import com.wisnu.kurniawan.composetodolist.foundation.extension.toThemePreference
import com.wisnu.kurniawan.composetodolist.model.Credential
import com.wisnu.kurniawan.composetodolist.model.Language
import com.wisnu.kurniawan.composetodolist.model.Theme
import com.wisnu.kurniawan.composetodolist.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    private val credentialDataStore: DataStore<CredentialPreference>,
    private val userDataStore: DataStore<UserPreference>,
    private val themeDataStore: DataStore<ThemePreference>,
    private val languageDataStore: DataStore<LanguagePreference>,
) {

    fun getCredential(): Flow<Credential> {
        return credentialDataStore.data
            .map { Credential(it.token) }
            .catch { emit(Credential(token = "")) }
    }

    fun getUser(): Flow<User> {
        return userDataStore.data
            .map { User(it.email) }
            .catch { emit(User(email = "")) }
    }

    fun getTheme(): Flow<Theme> {
        return themeDataStore.data.map { it.toTheme() }
            .catch { emit(Theme.SYSTEM) }
    }

    fun getLanguage(): Flow<Language> {
        return languageDataStore.data.map { it.toLanguage() }
            .catch { emit(Language.ENGLISH) }
    }

    suspend fun setCredential(data: Credential) {
        credentialDataStore.updateData {
            CredentialPreference(data.token)
        }
    }

    suspend fun setUser(data: User) {
        userDataStore.updateData {
            UserPreference(data.email)
        }
    }

    suspend fun setTheme(data: Theme) {
        themeDataStore.updateData {
            data.toThemePreference()
        }
    }

    suspend fun setLanguage(data: Language) {
        languageDataStore.updateData {
            data.toLanguagePreference()
        }
    }

}
