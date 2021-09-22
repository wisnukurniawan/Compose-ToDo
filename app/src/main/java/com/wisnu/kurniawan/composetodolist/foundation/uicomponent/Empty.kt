package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PgEmpty(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxWidth()) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
            Text(text, modifier = Modifier.align(Alignment.Center))
        }
    }
}
