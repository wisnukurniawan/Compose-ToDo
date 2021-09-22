package com.wisnu.kurniawan.composetodolist.foundation.di

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeGenerator
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun provideIdGenerator(): IdGenerator {
        return IdGenerator
    }

    @Singleton
    @Provides
    fun provideDateTimeGenerator(): DateTimeGenerator {
        return DateTimeGenerator
    }

}
