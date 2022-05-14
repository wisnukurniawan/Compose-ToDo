package com.wisnu.kurniawan.composetodolist.features.login.ui

import app.cash.turbine.test
import com.wisnu.kurniawan.composetodolist.BaseViewModelTest
import com.wisnu.kurniawan.composetodolist.features.login.data.ILoginEnvironment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class LoginViewModelTest : BaseViewModelTest() {

    private val loginEnvironment = object : ILoginEnvironment {
        override fun login(email: String, password: String): Flow<Any> {
            return flow { emit(Any()) }
        }
    }
    private val loginViewModel: LoginViewModel = LoginViewModel(loginEnvironment)

    @Test
    fun updateUserName() {
        loginViewModel.dispatch(LoginAction.ChangeEmail("qwe"))
        loginViewModel.dispatch(LoginAction.ChangeEmail("qwe2"))

        Assert.assertEquals("qwe2", loginViewModel.state.value.email)
    }

    @Test
    fun updatePassword() {
        loginViewModel.dispatch(LoginAction.ChangePassword("123"))
        loginViewModel.dispatch(LoginAction.ChangePassword("1234"))

        Assert.assertEquals("1234", loginViewModel.state.value.password)
    }

    @Test
    fun loginValidEmail() = runTest {
        loginViewModel.dispatch(LoginAction.ChangeEmail("qwe@gmail.com"))
        loginViewModel.dispatch(LoginAction.ChangePassword("1234"))

        loginViewModel.effect.test {
            loginViewModel.dispatch(LoginAction.ClickLogin)

            Assert.assertFalse(loginViewModel.state.value.showEmailInvalidError)
            Assert.assertEquals(LoginEffect.NavigateToDashboard, awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun loginInvalidEmail() = runTest {
        loginViewModel.dispatch(LoginAction.ChangeEmail("qwegmail.com"))
        loginViewModel.dispatch(LoginAction.ChangePassword("1234"))

        loginViewModel.effect.test {
            loginViewModel.dispatch(LoginAction.ClickLogin)

            Assert.assertTrue(loginViewModel.state.value.showEmailInvalidError)
            expectNoEvents()

            cancelAndConsumeRemainingEvents()
        }
    }

}
