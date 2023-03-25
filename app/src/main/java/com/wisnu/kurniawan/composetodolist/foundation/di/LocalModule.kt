package com.wisnu.kurniawan.composetodolist.foundation.di

import android.content.Context
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.ToDoDatabase
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoGroupReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoListReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoStepReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoTaskReadDao
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
    fun provideToDoWriteDao(@ApplicationContext context: Context): ToDoWriteDao {
        return ToDoDatabase.getInstance(context)
            .toDoWriteDao()
    }

    @Singleton
    @Provides
    fun provideToDoGroupReadDao(@ApplicationContext context: Context): ToDoGroupReadDao {
        return ToDoDatabase.getInstance(context)
            .toDoGroupReadDao()
    }

    @Singleton
    @Provides
    fun provideToDoListReadDao(@ApplicationContext context: Context): ToDoListReadDao {
        return ToDoDatabase.getInstance(context)
            .toDoListReadDao()
    }

    @Singleton
    @Provides
    fun provideToDoTaskReadDao(@ApplicationContext context: Context): ToDoTaskReadDao {
        return ToDoDatabase.getInstance(context)
            .toDoTaskReadDao()
    }

    @Singleton
    @Provides
    fun provideToDoStepReadDao(@ApplicationContext context: Context): ToDoStepReadDao {
        return ToDoDatabase.getInstance(context)
            .toDoStepReadDao()
    }

}
