package com.wisnu.kurniawan.composetodolist.foundation.di

import android.content.Context
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.ToDoDatabase
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.ToDoReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.ToDoWriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Singleton

@DelicateCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideToDoReadDao(@ApplicationContext context: Context): ToDoReadDao {
        return ToDoDatabase.getInstance(context)
            .toDoReadDao()
    }

    @Singleton
    @Provides
    fun provideToDoWriteDao(@ApplicationContext context: Context): ToDoWriteDao {
        return ToDoDatabase.getInstance(context)
            .toDoWriteDao()
    }

}
