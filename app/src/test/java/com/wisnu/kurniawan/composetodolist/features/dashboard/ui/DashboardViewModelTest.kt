package com.wisnu.kurniawan.composetodolist.features.dashboard.ui

import app.cash.turbine.test
import com.wisnu.kurniawan.composetodolist.BaseViewModelTest
import com.wisnu.kurniawan.composetodolist.features.dashboard.data.IDashboardEnvironment
import com.wisnu.kurniawan.composetodolist.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class DashboardViewModelTest : BaseViewModelTest() {

    @Test
    fun init() = test {
        val fakeDashboardEnvironment = buildFakeDashboardEnvironment(User("wisnu@dev.id"))
        val dashboardViewModel = DashboardViewModel(fakeDashboardEnvironment)

        dashboardViewModel.state.test {
            Assert.assertEquals(User("wisnu@dev.id"), awaitItem().user)

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun buildFakeDashboardEnvironment(user: User) = object : IDashboardEnvironment {
        override val dispatcher: CoroutineDispatcher = coroutineDispatcher

        override fun getUser(): Flow<User> {
            return flow { emit(user) }
        }
    }
}
