package com.wisnu.kurniawan.composetodolist.foundation.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.serializer.CredentialPreferenceSerializer
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.serializer.LanguagePreferenceSerializer
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.serializer.ThemePreferenceSerializer
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.serializer.UserPreferenceSerializer
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.CredentialPreference
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserLanguagePreference
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserPreference
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserThemePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val CREDENTIAL_NAME = "credential-preference.pb"
private const val USER_NAME = "user-preference.pb"
private const val THEME_NAME = "theme-preference.pb"
private const val LANGUAGE_NAME = "language-preference.pb"

private val Context.credentialDataStore: DataStore<CredentialPreference> by dataStore(
    fileName = CREDENTIAL_NAME,
    serializer = CredentialPreferenceSerializer
)
private val Context.userDataStore: DataStore<UserPreference> by dataStore(
    fileName = USER_NAME,
    serializer = UserPreferenceSerializer
)
private val Context.themeDataStore: DataStore<UserThemePreference> by dataStore(
    fileName = THEME_NAME,
    serializer = ThemePreferenceSerializer
)
val Context.languageDatastore: DataStore<UserLanguagePreference> by dataStore(
    fileName = LANGUAGE_NAME,
    serializer = LanguagePreferenceSerializer
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideCredentialDataStore(@ApplicationContext context: Context): DataStore<CredentialPreference> {
        return context.credentialDataStore
    }

    @Singleton
    @Provides
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<UserPreference> {
        return context.userDataStore
    }

    @Singleton
    @Provides
    fun provideThemeDataStore(@ApplicationContext context: Context): DataStore<UserThemePreference> {
        return context.themeDataStore
    }

    @Singleton
    @Provides
    fun provideLanguageDataStore(@ApplicationContext context: Context): DataStore<UserLanguagePreference> {
        return context.languageDatastore
    }

}
