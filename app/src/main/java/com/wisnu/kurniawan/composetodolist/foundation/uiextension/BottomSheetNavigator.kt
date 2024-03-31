package com.wisnu.kurniawan.composetodolist.foundation.uiextension

import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberBottomSheetNavigator(
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}
