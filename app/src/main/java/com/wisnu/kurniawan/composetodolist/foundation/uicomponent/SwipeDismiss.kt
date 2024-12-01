package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.drawGrowingCircle
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.guide
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.guide2
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.lerp
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.onPositionInParentChanged
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.hypot

private const val dismissFraction = 0.4f
private const val iconShownFraction = 0.07f

object ContentVisibility {
    const val visible: Float = 1f
    const val hidden: Float = 0f
}

@Composable
fun SwipeDismiss(
    modifier: Modifier = Modifier,
    backgroundModifier: Modifier = Modifier,
    backgroundSecondaryModifier: Modifier = Modifier,
    content: @Composable (isDismissed: Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    SwipeDismiss(
        modifier = modifier,
        background = { _, fraction ->
            val wouldCompleteOnRelease = fraction.absoluteValue >= dismissFraction
            val iconVisible = fraction.absoluteValue >= iconShownFraction
            val haptic = LocalHapticFeedback.current

            var shouldTriggerHaptic by remember { mutableStateOf(false) }
            var bounceState by remember { mutableStateOf(false) }
            val lottieIcon by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_swipe_delete))
            val lottieAnimatable = rememberLottieAnimatable()
            var iconCenter by remember { mutableStateOf(Offset(0f, 0f)) }

            val circleFraction by animateFloatAsState(
                targetValue = if (wouldCompleteOnRelease) ContentVisibility.visible else ContentVisibility.hidden,
                animationSpec = tween(durationMillis = 300)
            )
            val bounceInOut by animateFloatAsState(
                targetValue = if (bounceState) 1.2f else 1f
            )

            val maxRadius = hypot(iconCenter.x.toDouble(), iconCenter.y.toDouble())

            LaunchedEffect(wouldCompleteOnRelease) {
                if (wouldCompleteOnRelease) {
                    shouldTriggerHaptic = true

                    launch {
                        bounceState = true
                        delay(100)
                        bounceState = false
                    }
                }

                if (shouldTriggerHaptic) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
            LaunchedEffect(iconVisible) {
                if (iconVisible) {
                    launch {
                        delay(50)
                        lottieAnimatable.animate(
                            composition = lottieIcon,
                        )
                    }
                }
            }

            Box(
                modifier = backgroundModifier
                    .fillMaxSize()
            ) {
                // A simple box to draw the growing circle, which emanates from behind the icon
                Spacer(
                    modifier = backgroundSecondaryModifier
                        .fillMaxSize()
                        .drawGrowingCircle(
                            color = MaterialTheme.colorScheme.error,
                            center = iconCenter,
                            radius = lerp(
                                startValue = 0f,
                                endValue = maxRadius.toFloat(),
                                fraction = FastOutLinearInEasing.transform(circleFraction)
                            )
                        )
                )

                Box(
                    Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 16.dp)
                        .onPositionInParentChanged { iconCenter = it.boundsInParent().center }
                ) {
                    LottieAnimation(
                        lottieIcon,
                        modifier = Modifier
                            .size(32.dp)
                            .scale(bounceInOut),
                        progress = {
                            lottieAnimatable.progress
                        }
                    )
                }
            }
        },
        content = content,
        onDismiss = onDismiss
    )
}

@Composable
fun SwipeDismiss(
    modifier: Modifier = Modifier,
    background: @Composable (isDismissed: Boolean, fraction: Float) -> Unit,
    content: @Composable (isDismissed: Boolean) -> Unit,
    directions: Set<SwipeToDismissBoxValue> = setOf(SwipeToDismissBoxValue.EndToStart),
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(
        animationSpec = tween(
            durationMillis = 400,
        )
    ),
    onDismiss: () -> Unit
) {
    var isDismissed by rememberSaveable { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isDismissed = true
                return@rememberSwipeToDismissBoxState true
            } else {
                return@rememberSwipeToDismissBoxState false
            }
        }
    )

    LaunchedEffect(isDismissed) {
        if (isDismissed) {
            delay(600)
            onDismiss()
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = !isDismissed,
        enter = enter,
        exit = exit
    ) {
        SwipeToDismissBox(
            modifier = modifier,
            state = dismissState,
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            backgroundContent = {
                if (dismissState.dismissDirection in directions) {
                    val fraction = dismissState.progress
                    background(isDismissed, fraction)
                }
            },
            content = { content(isDismissed) },
        )
    }
}

@Preview
@Composable
fun LottieSample() {
    Box(Modifier.fillMaxSize().guide(), contentAlignment = Alignment.Center) {
        var playLottieIcon by remember { mutableStateOf(false) }

        Column {
            val lottieIcon by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_swipe_delete))
            val lottieAnimatable = rememberLottieAnimatable()
            LottieAnimation(
                lottieIcon,
                modifier = Modifier.size(32.dp).guide2().padding(16.dp),
                progress = {
                    lottieAnimatable.progress
                }
            )

            Spacer(Modifier.height(16.dp))

            LaunchedEffect(playLottieIcon) {
                lottieAnimatable.animate(
                    composition = lottieIcon,
                )
            }
        }

        Button(onClick = {
            playLottieIcon = true
        }, Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
            Text("Play")
        }
    }
}
