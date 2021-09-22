package com.wisnu.kurniawan.composetodolist.features.localized.base.di

import com.wisnu.kurniawan.composetodolist.features.localized.base.data.ILocalizedEnvironment
import com.wisnu.kurniawan.composetodolist.features.localized.base.data.LocalizedEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalizedModule {

    @Binds
    abstract fun provideEnvironment(
        environment: LocalizedEnvironment
    ): ILocalizedEnvironment

}
