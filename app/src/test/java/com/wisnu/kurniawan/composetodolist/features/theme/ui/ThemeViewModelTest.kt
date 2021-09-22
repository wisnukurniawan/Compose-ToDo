package com.wisnu.kurniawan.composetodolist.features.theme.ui

import androidx.compose.ui.graphics.Brush
import app.cash.turbine.test
import com.wisnu.kurniawan.composetodolist.BaseViewModelTest
import com.wisnu.kurniawan.composetodolist.features.theme.data.IThemeEnvironment
import com.wisnu.kurniawan.composetodolist.model.Theme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class ThemeViewModelTest : BaseViewModelTest() {

    @Test
    fun init() = test {
        val environment = object : IThemeEnvironment {
            override val dispatcher: CoroutineDispatcher = coroutineDispatcher

            override fun getTheme(): Flow<Theme> {
                return flow { emit(Theme.SUNRISE) }
            }

            override suspend fun setTheme(theme: Theme) {

            }
        }

        val viewModel = ThemeViewModel(environment)

        viewModel.state.test {
            Assert.assertTrue(awaitItem().items.find { it.theme == Theme.SUNRISE }?.applied ?: false)

            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun applyTheme() = test {
        val environment = object : IThemeEnvironment {

            private val flow = MutableStateFlow(Theme.SUNRISE)

            override val dispatcher: CoroutineDispatcher = coroutineDispatcher

            override fun getTheme(): Flow<Theme> {
                return flow.asStateFlow()
            }

            override suspend fun setTheme(theme: Theme) {
                flow.emit(theme)
            }
        }

        val viewModel = ThemeViewModel(environment)

        viewModel.dispatch(
            ThemeAction.SelectTheme(
                ThemeItem(
                    1,
                    Theme.TWILIGHT,
                    Brush.verticalGradient(),
                    false
                )
            )
        )

        viewModel.state.test {
            Assert.assertTrue(awaitItem().items.find { it.theme == Theme.TWILIGHT }?.applied ?: false)

            cancelAndConsumeRemainingEvents()
        }
    }

}
