package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.coreLogger.Loggr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberSwipeSearchState(
    initialValue: SwipeSearchValue
): SwipeSearchState {
    return remember {
        SwipeSearchState(initialValue)
    }.apply {
        this.currentValue = initialValue
    }
}

enum class SwipeSearchValue {
    Closed,
    Opened
}

@Stable
class SwipeSearchState(
    initialValue: SwipeSearchValue
) {
    private val offsetState = Animatable(0f)
    private val mutatorMutex = MutatorMutex()

    var currentValue: SwipeSearchValue by mutableStateOf(initialValue)

    var isSwipeInProgress: Boolean by mutableStateOf(false)
        internal set

    val offset: Float get() = offsetState.value

    internal suspend fun animateOffsetTo(offset: Float) {
        mutatorMutex.mutate {
            offsetState.animateTo(offset)
        }
    }

    internal suspend fun dispatchScrollDelta(delta: Float, max: Float) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            offsetState.snapTo((offsetState.value + delta).coerceAtMost(max))
        }
    }
}

@Composable
fun SwipeSearch(
    modifier: Modifier = Modifier,
    state: SwipeSearchState = rememberSwipeSearchState(SwipeSearchValue.Closed),
    enabled: Boolean = true,
    onFling: (SwipeSearchValue) -> Unit,
    onSearchAreaClick: () -> Unit,
    content: @Composable () -> Unit,
    searchSection: @Composable RowScope.() -> Unit,
    searchBody: LazyListScope.() -> Unit,
) {
    Scaffold { }
    val searchHeightPx = with(LocalDensity.current) { SearchHeight.roundToPx() }
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollConnection = remember(state, coroutineScope) {
        SwipeSearchNestedScrollConnection(
            state,
            coroutineScope,
            searchHeightPx,
            enabled,
            onFling
        )
    }
    var offset by remember { mutableStateOf(0f) }

    Box(modifier.nestedScroll(connection = nestedScrollConnection)) {
        Column {
            val pushHeight = with(LocalDensity.current) { (offset / PushBackDropMultiplier).toDp() }
            Spacer(Modifier.height(pushHeight))

            content()
        }

        Box(
            Modifier
                .padding(PaddingValues(0.dp))
                .matchParentSize()
                .clipToBounds()
        ) {
            if (state.isSwipeInProgress) {
                offset = state.offset
            }

            LaunchedEffect(state.isSwipeInProgress, state.currentValue) {
                if (!state.isSwipeInProgress) {
                    val targetOffset = when (state.currentValue) {
                        SwipeSearchValue.Opened -> searchHeightPx.toFloat()
                        SwipeSearchValue.Closed -> 0f
                    }

                    animate(
                        initialValue = offset,
                        targetValue = targetOffset
                    ) { value, _ ->
                        offset = value
                    }

                    state.animateOffsetTo(targetOffset)
                }
            }

            Box {
                val alpha = offset / searchHeightPx
                if (alpha != 0f) {
                    LazyColumn(
                        modifier = Modifier
                            .alpha(alpha)
                            .background(MaterialTheme.colors.secondaryVariant)
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onSearchAreaClick
                            )
                    ) {
                        item {
                            Spacer(Modifier.height(SearchHeight))
                        }

                        searchBody()
                    }
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .height(SearchHeight)
                        .padding(start = 16.dp, end = 16.dp)
                        .graphicsLayer {
                            translationY = offset - searchHeightPx
                        },
                ) {
                    searchSection()
                }
            }

        }
    }
}

private class SwipeSearchNestedScrollConnection(
    private val state: SwipeSearchState,
    private val coroutineScope: CoroutineScope,
    private val searchHeightPx: Int,
    private val enabled: Boolean,
    private val onFling: (SwipeSearchValue) -> Unit,
) : NestedScrollConnection {
    private var shouldConsumeScrollDown = true

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return when {
            !enabled -> Offset.Zero
            source == NestedScrollSource.Drag && available.y < 0 -> {
                performDrag(available, SCROLL_UP)
            }
            else -> Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (available.y == 0f && state.offset == 0f) {
            shouldConsumeScrollDown = false
        }

        return when {
            !enabled -> Offset.Zero
            !shouldConsumeScrollDown -> Offset.Zero
            state.offset >= searchHeightPx -> Offset.Zero
            source == NestedScrollSource.Drag && available.y > 0 -> {
                performDrag(available, SCROLL_DOWN)
            }
            else -> Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        performFling(available)

        // Reset
        shouldConsumeScrollDown = true

        return Velocity.Zero
    }

    private fun performFling(available: Velocity) {
        when (state.currentValue) {
            SwipeSearchValue.Closed -> {
                when {
                    available.y > OpenVelocityThreshold -> {
                        onFling(SwipeSearchValue.Opened)
                    }
                    state.offset >= (searchHeightPx * CloseDragThreshold) -> {
                        onFling(SwipeSearchValue.Opened)
                    }
                    else -> {
                        onFling(SwipeSearchValue.Closed)
                    }
                }
            }
            SwipeSearchValue.Opened -> {
                when {
                    available.y < CloseVelocityThreshold -> {
                        onFling(SwipeSearchValue.Closed)
                    }
                    state.offset >= (searchHeightPx * OpenDragThreshold) -> {
                        onFling(SwipeSearchValue.Opened)
                    }
                    else -> {
                        onFling(SwipeSearchValue.Closed)
                    }
                }
            }
        }

        state.isSwipeInProgress = false
    }


    private fun performDrag(available: Offset, scrollInfo: String): Offset {
        state.isSwipeInProgress = true

        val clamped = (available.y * DragMultiplier + state.offset).coerceAtLeast(0f)
        val deltaToConsume = clamped - state.offset

        if (scrollInfo == SCROLL_UP && deltaToConsume == 0f) return Offset.Zero

        Loggr.debug { "performDrag $deltaToConsume $scrollInfo" }

        coroutineScope.launch {
            state.dispatchScrollDelta(deltaToConsume, searchHeightPx.toFloat())
        }

        return Offset(x = 0f, y = deltaToConsume / DragMultiplier)
    }
}

private val SearchHeight = 100.dp

private const val DragMultiplier = 0.5f
private const val PushBackDropMultiplier = 3f

private const val CloseVelocityThreshold = -5000
private const val OpenVelocityThreshold = 5000

private const val CloseDragThreshold = 0.76
private const val OpenDragThreshold = 0.33

private const val SCROLL_UP = "UP"
private const val SCROLL_DOWN = "DOWN"
