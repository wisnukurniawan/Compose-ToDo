package com.wisnu.kurniawan.composetodolist.features.logout.di

import com.wisnu.kurniawan.composetodolist.features.logout.data.ILogoutEnvironment
import com.wisnu.kurniawan.composetodolist.features.logout.data.LogoutEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LogoutModule {

    @Binds
    abstract fun provideEnvironment(
        environment: LogoutEnvironment
    ): ILogoutEnvironment

}
