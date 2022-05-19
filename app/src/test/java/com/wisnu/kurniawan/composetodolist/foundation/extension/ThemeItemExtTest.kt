package com.wisnu.kurniawan.composetodolist.foundation.extension

import androidx.compose.ui.graphics.Brush
import com.wisnu.kurniawan.composetodolist.features.theme.ui.ThemeItem
import com.wisnu.kurniawan.composetodolist.model.Theme
import org.junit.Assert
import org.junit.Test

class ThemeItemExtTest {

    @Test
    fun update() {
        val data = listOf(
            ThemeItem(
                title = 1,
                theme = Theme.AURORA,
                brush = Brush.verticalGradient(),
                applied = false
            ),
            ThemeItem(
                title = 1,
                theme = Theme.SUNRISE,
                brush = Brush.verticalGradient(),
                applied = false
            ),
            ThemeItem(
                title = 1,
                theme = Theme.TWILIGHT,
                brush = Brush.verticalGradient(),
                applied = true
            )
        )

        Assert.assertEquals(
            listOf(
                ThemeItem(
                    title = 1,
                    theme = Theme.AURORA,
                    brush = Brush.verticalGradient(),
                    applied = true
                ),
                ThemeItem(
                    title = 1,
                    theme = Theme.SUNRISE,
                    brush = Brush.verticalGradient(),
                    applied = false
                ),
                ThemeItem(
                    title = 1,
                    theme = Theme.TWILIGHT,
                    brush = Brush.verticalGradient(),
                    applied = false
                )
            ),
            data.select(Theme.AURORA)
        )
    }

}
