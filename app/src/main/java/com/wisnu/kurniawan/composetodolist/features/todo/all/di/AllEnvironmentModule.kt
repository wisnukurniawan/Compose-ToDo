package com.wisnu.kurniawan.composetodolist.features.todo.all.di

import com.wisnu.kurniawan.composetodolist.features.todo.all.data.AllEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.all.data.IAllEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AllEnvironmentModule {

    @Binds
    abstract fun provideEnvironment(
        environment: AllEnvironment
    ): IAllEnvironment

}
