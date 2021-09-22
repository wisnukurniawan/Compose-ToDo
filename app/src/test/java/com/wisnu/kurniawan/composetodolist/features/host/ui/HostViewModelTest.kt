package com.wisnu.kurniawan.composetodolist.features.host.ui

import app.cash.turbine.test
import com.wisnu.kurniawan.composetodolist.BaseViewModelTest
import com.wisnu.kurniawan.composetodolist.features.host.data.IHostEnvironment
import com.wisnu.kurniawan.composetodolist.model.Theme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class HostViewModelTest : BaseViewModelTest() {

    @Test
    fun init() = test {
        val environment = object : IHostEnvironment {
            override val dispatcher: CoroutineDispatcher = coroutineDispatcher

            override fun getTheme(): Flow<Theme> {
                return flow { emit(Theme.SUNRISE) }
            }
        }

        val viewModel = HostViewModel(environment)

        viewModel.state.test {
            Assert.assertEquals(Theme.SUNRISE, awaitItem().theme)

            cancelAndConsumeRemainingEvents()
        }
    }

}
