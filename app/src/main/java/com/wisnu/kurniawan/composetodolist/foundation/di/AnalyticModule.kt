package com.wisnu.kurniawan.composetodolist.foundation.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticModule {

    @Singleton
    @Provides
    fun provideGoogleAnalytic(): FirebaseAnalytics {
        return Firebase.analytics
    }

}
