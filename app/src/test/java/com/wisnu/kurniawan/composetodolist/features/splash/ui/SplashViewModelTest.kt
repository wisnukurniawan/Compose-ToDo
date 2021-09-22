package com.wisnu.kurniawan.composetodolist.features.splash.ui

import app.cash.turbine.test
import com.wisnu.kurniawan.composetodolist.BaseViewModelTest
import com.wisnu.kurniawan.composetodolist.features.splash.data.ISplashEnvironment
import com.wisnu.kurniawan.composetodolist.model.Credential
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class SplashViewModelTest : BaseViewModelTest() {

    @Test
    fun appLaunchLoggedIn() = test {
        val splashEnvironment = buildFakeSplashEnvironment(Credential("qwe-123"))
        val splashViewModel = SplashViewModel(splashEnvironment)

        splashViewModel.effect.test {
            splashViewModel.dispatch(SplashAction.AppLaunch)

            Assert.assertEquals(SplashEffect.NavigateToDashboard, awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun appLaunchNotLoggedIn() = test {
        val splashEnvironment = buildFakeSplashEnvironment(Credential(""))
        val splashViewModel = SplashViewModel(splashEnvironment)

        splashViewModel.effect.test {
            splashViewModel.dispatch(SplashAction.AppLaunch)

            Assert.assertEquals(SplashEffect.NavigateToLogin, awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun buildFakeSplashEnvironment(credential: Credential): ISplashEnvironment {
        return object : ISplashEnvironment {
            override val dispatcher: CoroutineDispatcher
                get() = coroutineDispatcher

            override fun getCredential(): Flow<Credential> {
                return flow { emit(credential) }
            }
        }
    }

}
