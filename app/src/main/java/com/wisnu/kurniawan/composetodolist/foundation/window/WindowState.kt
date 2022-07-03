package com.wisnu.kurniawan.composetodolist.foundation.window

import android.content.res.Configuration
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
/**
 * Data class that contains foldable and large screen information extracted from the Jetpack
 * Window Manager library
 *
 * @param hasFold: true if window contains a FoldingFeature,
 * @param foldIsHorizontal: true if window contains a FoldingFeature with a horizontal orientation
 * @param foldBoundsDp: DpRect object that describes the bound of the FoldingFeature in Dp
 * @param foldState: state of the fold, based on state property of FoldingFeature
 * @param foldIsSeparating: based on isSeparating property of FoldingFeature
 * @param foldIsOccluding: true if FoldingFeature occlusion type is full
 * @param windowWidthDp: Dp value of the window width
 * @param windowHeightDp: Dp value of the window height
 */
data class WindowState(
    val hasFold: Boolean = false,
    val foldIsHorizontal: Boolean = false,
    val foldBoundsDp: DpRect = DpRect(0.dp, 0.dp, 0.dp, 0.dp),
    val foldState: FoldState = FoldState.FLAT,
    val foldIsSeparating: Boolean = false,
    val foldIsOccluding: Boolean = false,
    val windowWidthDp: Dp = 0.dp,
    val windowHeightDp: Dp = 0.dp,
) {
    /**
     * Dp value of the width of the hinge or the folding line if it is separating, otherwise 0
     */
    val foldSizeDp: Dp =
        if (foldIsSeparating) {
            if (foldIsHorizontal) foldBoundsDp.height else foldBoundsDp.width
        } else {
            0.dp
        }

    /**
     * Current window mode (single portrait, single landscape, dual portrait, or dual landscape)
     */
    val windowMode: WindowMode
        @Composable get() {
            val isPortrait = windowIsPortrait()

            return calculateWindowMode(isPortrait)
        }

    /**
     * Proportion of the window that pane 1 should occupy on a large screen. Used when calculating pane size for
     * the `pane1SizeDp` and `pane2SizeDp` properties.
     *
     * Must be between 0 and 1 - default value is 0.5 to create equal panes
     */
    var largeScreenPane1Weight: Float = 0.5f
        set(value) {
            checkWeight(value)
            field = value
        }

    /**
     * Helper method to check the validity of a pane weight
     *
     * If the given weight is <=0 or >=1, then an exception is thrown
     */
    private fun checkWeight(weight: Float) {
        // Check that 0 < weight < 1
        if (weight <= 0f || weight >= 1f)
            throw IllegalArgumentException("Pane 1 weight must be between 0 and 1")
    }

    /**
     * Returns whether the current window is in a dual screen mode or not.
     *
     * A window is considered dual screen if it's a large screen ("expanded" width size class) or a foldable
     * (folding feature is present and is separating)
     *
     * @return true if dual screen, false otherwise
     */
    @Composable
    fun isDualScreen(): Boolean {
        return windowMode.isDualScreen
    }

    /**
     * Returns whether the current window is in dual portrait mode or not
     *
     * A window is considered dual screen if it's a large screen ("expanded" width size class) or a foldable
     * (folding feature is present and is separating)
     *
     * @return true if in dual portrait mode, false otherwise
     */
    @Composable
    fun isDualPortrait(): Boolean {
        return windowMode == WindowMode.DUAL_PORTRAIT
    }

    /**
     * Returns whether the current window is in dual landscape mode or not
     *
     * A window is considered dual screen if it's a large screen ("expanded" width size class) or a foldable
     * (folding feature is present and is separating)
     *
     * @return true if in dual landscape mode, false otherwise
     */
    @Composable
    fun isDualLandscape(): Boolean {
        return windowMode == WindowMode.DUAL_LANDSCAPE
    }

    /**
     * Returns whether the current window is in single portrait mode or not
     *
     * @return true if in single portrait mode, false otherwise
     */
    @Composable
    fun isSinglePortrait(): Boolean {
        return windowMode == WindowMode.SINGLE_PORTRAIT
    }

    /**
     * Returns whether the current window is in single landscape mode or not
     *
     * @return true if in single landscape mode, false otherwise
     */
    @Composable
    fun isSingleLandscape(): Boolean {
        return windowMode == WindowMode.SINGLE_LANDSCAPE
    }

    /**
     * Returns the size class (compact, medium, or expanded) for the window width
     *
     * @return width size class
     */
    fun widthSizeClass(): WindowSizeClass {
        return getWindowSizeClass(windowWidthDp)
    }

    /**
     * Returns the size class (compact, medium, or expanded) for the window height
     *
     * @return height size class
     */
    fun heightSizeClass(): WindowSizeClass {
        return getWindowSizeClass(windowHeightDp, Dimension.HEIGHT)
    }

    /**
     * Dp size of pane 1 (top and left or right pane depending on local layout direction) when a fold
     * is separating, otherwise the size is zero
     */
    val foldablePane1SizeDp: DpSize
        @Composable get() {
            return getFoldablePaneSizes(LocalLayoutDirection.current).first
        }

    /**
     * Dp size of pane 2 (bottom and left or right pane depending on local layout direction) when a
     * fold is separating, otherwise the size is zero
     */
    val foldablePane2SizeDp: DpSize
        @Composable get() {
            return getFoldablePaneSizes(LocalLayoutDirection.current).second
        }

    /**
     * Returns the dp size of pane 1 (top and left or right pane depending on local layout direction) when a
     * folding feature is separating or the window is large, otherwise the returned size is zero.
     *
     * If a separating folding feature is present, the panes are split according to the folding feature's boundaries.
     *
     * If the window is large, then the panes are split according to the `largeScreenPane1Weight` property. The
     * property must be set before calling this method for the correct value to be used.
     *
     * @return dp size of pane 1
     */
    val pane1SizeDp: DpSize
        @Composable get() {
            return getPaneSizes(windowIsPortrait(), LocalLayoutDirection.current, largeScreenPane1Weight).first
        }

    /**
     * Returns the dp size of pane 2 (bottom and left or right pane depending on local layout direction) when a
     * fold is separating or the window is large, otherwise the returned size is zero.
     *
     * If a separating folding feature is present, the panes are split according to the folding feature's boundaries.
     *
     * If the window is large, then the panes are split according to the `largeScreenPane1Weight` property. The
     * property must be set before calling this method for the correct value to be used.
     *
     * @return dp size of pane 2
     */
    val pane2SizeDp: DpSize
        @Composable get() {
            return getPaneSizes(windowIsPortrait(), LocalLayoutDirection.current, largeScreenPane1Weight).second
        }

    /**
     * Checks whether the window is in the portrait or landscape orientation
     *
     * @return true if portrait, false if landscape
     */
    @Composable
    private fun windowIsPortrait(): Boolean {
        // REVISIT: should width/height ratio of the window be used instead of orientation?
        return LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * Checks whether the window is considered large (expanded width size class) or not. Note: currently,
     * foldables take priority over large screens, so if a window has a separating fold and is a large windnow, it
     * will be treated as a foldable.
     *
     * @return true if large, false otherwise
     */
    private fun windowIsLarge(): Boolean {
        // REVISIT: should height class also be considered?
        // Also, right now we are considering large screens + foldables mutually exclusive
        // (which seems necessary for dualscreen apps), but we may want to think about this
        // more and change our approach if we think there are cases where we want an app to
        // know about both properties
        val widthSizeClass = getWindowSizeClass(windowWidthDp)
        return !foldIsSeparating && widthSizeClass == WindowSizeClass.EXPANDED
    }

    /**
     * Calculates the current window mode
     *
     * @param isPortrait: whether the window is in the portrait orientation or not
     * @return current window mode
     */
    @VisibleForTesting
    internal fun calculateWindowMode(isPortrait: Boolean): WindowMode {
        return when {
            foldIsSeparating -> if (foldIsHorizontal) WindowMode.DUAL_LANDSCAPE else WindowMode.DUAL_PORTRAIT
            windowIsLarge() -> if (isPortrait) WindowMode.DUAL_LANDSCAPE else WindowMode.DUAL_PORTRAIT
            else -> if (isPortrait) WindowMode.SINGLE_PORTRAIT else WindowMode.SINGLE_LANDSCAPE
        }
    }

    /**
     * Calculates pane sizes based on the boundaries of a separating fold. If no separating folds are present,
     * then the returned sizes are zero.
     *
     * @param layoutDir: language layout direction that determines whether the right or left pane is considered
     * pane 1 (primary pane)
     * @return pair of sizes, with pane 1 being the first size and pane 2 being the second
     */
    @VisibleForTesting
    internal fun getFoldablePaneSizes(layoutDir: LayoutDirection): Pair<DpSize, DpSize> {
        // If a separating fold is not present, return size zero
        if (!foldIsSeparating)
            return Pair(DpSize.Zero, DpSize.Zero)

        if (foldIsHorizontal) {
            // When a fold is horizontal, pane widths are equal and pane heights depend on the fold boundaries
            val topPaneHeight = foldBoundsDp.top
            val bottomPaneHeight = windowHeightDp - foldBoundsDp.bottom

            // The top pane is always considered pane 1
            val topPaneSize = DpSize(windowWidthDp, topPaneHeight)
            val bottomPaneSize = DpSize(windowWidthDp, bottomPaneHeight)

            return Pair(topPaneSize, bottomPaneSize)
        } else {
            // When a fold is vertical, pane heights are equal and pane widths depend on the fold boundaries
            val leftPaneWidth = foldBoundsDp.left
            val rightPaneWidth = windowWidthDp - foldBoundsDp.right

            val leftPaneSize = DpSize(leftPaneWidth, windowHeightDp)
            val rightPaneSize = DpSize(rightPaneWidth, windowHeightDp)

            // Pane 1 can be right or left depending on the language layout direction
            return when (layoutDir) {
                LayoutDirection.Ltr -> Pair(leftPaneSize, rightPaneSize)
                LayoutDirection.Rtl -> Pair(rightPaneSize, leftPaneSize)
            }
        }
    }

    /**
     * Calculates pane sizes based on window size and pane weight. If the window is not large, then the returned
     * sizes are zero.
     *
     * @param isPortrait: true if the window is in the portrait orientation, false otherwise
     * @param pane1Weight: the proportion of the window that pane 1 should occupy (must be between 0 and 1),
     * default weight is 0.5 to make equal panes
     * @return pair of sizes, with pane 1 being the first size and pane 2 being the second
     */
    @VisibleForTesting
    internal fun getLargeScreenPaneSizes(isPortrait: Boolean, pane1Weight: Float = 0.5f): Pair<DpSize, DpSize> {
        // Check that 0 < weight < 1
        checkWeight(pane1Weight)

        // If the window is not large, return size zero
        if (!windowIsLarge())
            return Pair(DpSize.Zero, DpSize.Zero)

        if (isPortrait) {
            // When a window is in the portrait orientation, panes should be divided between top and bottom.
            // This means pane widths are equal and pane heights are based on weight (dual landscape)
            val pane1Height = windowHeightDp * pane1Weight
            val pane2Height = windowHeightDp - pane1Height

            return Pair(DpSize(windowWidthDp, pane1Height), DpSize(windowWidthDp, pane2Height))
        } else {
            // When a window is in the landscape orientation, panes should be divided between left and right.
            // This means pane heights are equal and pane widths are based on weight (dual portrait)
            val pane1Width = windowWidthDp * pane1Weight
            val pane2Width = windowWidthDp - pane1Width

            return Pair(DpSize(pane1Width, windowHeightDp), DpSize(pane2Width, windowHeightDp))
        }
    }

    /**
     * Calculates pane sizes based on all window properties. If no separating folds are present and the window
     * is not large, then the returned sizes are zero.
     *
     * @param isPortrait: true if the window is in the portrait orientation, false otherwise
     * @param layoutDir: language layout direction that determines whether the right or left pane is considered
     * pane 1 (primary pane)
     * @param largeScreenPane1Weight: the proportion of the window that pane 1 should occupy on a large screen
     * (must be between 0 and 1), default weight is 0.5 to make equal panes
     * @return pair of sizes, with pane 1 being the first size and pane 2 being the second
     */
    private fun getPaneSizes(
        isPortrait: Boolean,
        layoutDir: LayoutDirection,
        largeScreenPane1Weight: Float
    ): Pair<DpSize, DpSize> {
        return when {
            foldIsSeparating -> getFoldablePaneSizes(layoutDir)
            windowIsLarge() -> getLargeScreenPaneSizes(isPortrait, largeScreenPane1Weight)
            else -> Pair(DpSize.Zero, DpSize.Zero)
        }
    }
}
