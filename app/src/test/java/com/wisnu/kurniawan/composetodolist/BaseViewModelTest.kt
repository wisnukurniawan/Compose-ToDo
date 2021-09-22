package com.wisnu.kurniawan.composetodolist

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

@ExperimentalCoroutinesApi
open class BaseViewModelTest {

    protected val coroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(coroutineDispatcher)

    fun test(content: suspend CoroutineScope.() -> Unit) = coroutineDispatcher.runBlockingTest {
        content()
    }

}
