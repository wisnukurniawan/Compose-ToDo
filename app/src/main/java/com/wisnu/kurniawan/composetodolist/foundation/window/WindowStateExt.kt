package com.wisnu.kurniawan.composetodolist.foundation.window

import android.app.Activity
import android.graphics.Rect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker.Companion.getOrCreate
import androidx.window.layout.WindowMetricsCalculator

@Composable
fun Activity.rememberWindowState(): WindowState {
    val windowLayoutInfo = remember { getOrCreate(this).windowLayoutInfo(this) }

    var hasFold by remember { mutableStateOf(false) }
    var isFoldHorizontal by remember { mutableStateOf(false) }
    var foldBoundsDp by remember { mutableStateOf(DpRect(0.dp, 0.dp, 0.dp, 0.dp)) }
    var foldBoundsPx by remember { mutableStateOf(Rect()) }
    var foldState by remember { mutableStateOf(FoldState.FLAT) }
    var foldSeparates by remember { mutableStateOf(false) }
    var foldOccludes by remember { mutableStateOf(false) }

    LaunchedEffect(windowLayoutInfo) {
        windowLayoutInfo.collect { newLayoutInfo ->
            val foldingFeature = newLayoutInfo.displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
            foldingFeature?.let {
                hasFold = true
                isFoldHorizontal = it.orientation == FoldingFeature.Orientation.HORIZONTAL
                foldBoundsPx = it.bounds
                foldState = when (it.state) {
                    FoldingFeature.State.HALF_OPENED -> FoldState.HALF_OPENED
                    else -> FoldState.FLAT
                }
                foldSeparates = it.isSeparating
                foldOccludes = it.occlusionType == FoldingFeature.OcclusionType.FULL
            }
        }
    }

    val config = LocalConfiguration.current
    val windowMetrics = remember(config) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this).bounds
    }

    // Convert pixel fields to dp
    var windowWidth: Dp
    var windowHeight: Dp
    with(LocalDensity.current) {
        windowWidth = windowMetrics.width().toDp()
        windowHeight = windowMetrics.height().toDp()

        foldBoundsDp = DpRect(
            foldBoundsPx.left.toDp(),
            foldBoundsPx.top.toDp(),
            foldBoundsPx.right.toDp(),
            foldBoundsPx.bottom.toDp()
        )
    }

    return WindowState(
        hasFold,
        isFoldHorizontal,
        foldBoundsDp,
        foldState,
        foldSeparates,
        foldOccludes,
        windowWidth,
        windowHeight
    )
}
