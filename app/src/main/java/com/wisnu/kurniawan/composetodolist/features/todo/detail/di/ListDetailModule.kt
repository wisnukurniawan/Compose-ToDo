package com.wisnu.kurniawan.composetodolist.features.todo.detail.di

import com.wisnu.kurniawan.composetodolist.features.todo.detail.data.IListDetailEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.detail.data.ListDetailEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.FlowPreview

@Module
@InstallIn(ViewModelComponent::class)
abstract class ListDetailModule {

    @OptIn(FlowPreview::class)
    @Binds
    abstract fun provideEnvironment(
        environment: ListDetailEnvironment
    ): IListDetailEnvironment

}
