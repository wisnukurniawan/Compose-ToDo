package com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.di

import com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.data.GroupMenuEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.data.IGroupMenuEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class GroupMenuModule {

    @Binds
    abstract fun provideEnvironment(
        environment: GroupMenuEnvironment
    ): IGroupMenuEnvironment

}
