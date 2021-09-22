package com.wisnu.kurniawan.composetodolist.features.todo.group.di

import com.wisnu.kurniawan.composetodolist.features.todo.group.data.CreateGroupEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.group.data.ICreateGroupEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CreateGroupModule {

    @Binds
    abstract fun provideEnvironment(
        environment: CreateGroupEnvironment
    ): ICreateGroupEnvironment

}
