package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.module

import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.data.IScheduledEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.data.ScheduledEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ScheduledModule {

    @Binds
    abstract fun provideEnvironment(
        environment: ScheduledEnvironment
    ): IScheduledEnvironment

}
