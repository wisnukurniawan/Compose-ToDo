package com.wisnu.kurniawan.composetodolist.features.theme.ui

import androidx.compose.ui.graphics.Brush
import com.wisnu.kurniawan.composetodolist.foundation.extension.select
import com.wisnu.kurniawan.composetodolist.model.Theme
import org.junit.Assert
import org.junit.Test

class ThemeItemExtTest {

    @Test
    fun updateThemeItem() {
        val test = listOf(
            ThemeItem(
                title = 1,
                theme = Theme.SYSTEM,
                brush = Brush.linearGradient(),
                applied = false
            ),
            ThemeItem(
                title = 2,
                theme = Theme.LIGHT,
                brush = Brush.linearGradient(),
                applied = false
            ),
            ThemeItem(
                title = 3,
                theme = Theme.NIGHT,
                brush = Brush.linearGradient(),
                applied = false
            )
        )

        val expected1 = listOf(
            ThemeItem(
                title = 1,
                theme = Theme.SYSTEM,
                brush = Brush.linearGradient(),
                applied = false
            ),
            ThemeItem(
                title = 2,
                theme = Theme.LIGHT,
                brush = Brush.linearGradient(),
                applied = true
            ),
            ThemeItem(
                title = 3,
                theme = Theme.NIGHT,
                brush = Brush.linearGradient(),
                applied = false
            )
        )
        val expected2 = listOf(
            ThemeItem(
                title = 1,
                theme = Theme.SYSTEM,
                brush = Brush.linearGradient(),
                applied = false
            ),
            ThemeItem(
                title = 2,
                theme = Theme.LIGHT,
                brush = Brush.linearGradient(),
                applied = false
            ),
            ThemeItem(
                title = 3,
                theme = Theme.NIGHT,
                brush = Brush.linearGradient(),
                applied = true
            )
        )

        Assert.assertEquals(
            expected1,
            test.select(Theme.LIGHT)
        )
        Assert.assertEquals(
            expected2,
            test.select(Theme.NIGHT)
        )
    }

}
