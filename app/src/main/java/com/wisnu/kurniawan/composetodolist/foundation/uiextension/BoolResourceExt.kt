package com.wisnu.kurniawan.composetodolist.foundation.uiextension

import android.content.res.Resources
import androidx.annotation.BoolRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Composable
@ReadOnlyComposable
fun boolResource(@BoolRes id: Int): Boolean {
    val resources = resources()
    return resources.getBoolean(id)
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
