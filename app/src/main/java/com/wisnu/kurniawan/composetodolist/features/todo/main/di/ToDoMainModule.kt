package com.wisnu.kurniawan.composetodolist.features.todo.main.di

import com.wisnu.kurniawan.composetodolist.features.todo.main.data.IToDoMainEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.main.data.ToDoMainEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ToDoMainModule {

    @Binds
    abstract fun provideEnvironment(
        environment: ToDoMainEnvironment
    ): IToDoMainEnvironment

}
