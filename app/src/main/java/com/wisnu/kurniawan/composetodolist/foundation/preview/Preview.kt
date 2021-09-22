package com.wisnu.kurniawan.composetodolist.foundation.preview

import android.view.MotionEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.guide2
import com.wisnu.kurniawan.coreLogger.Loggr
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@Preview
private fun ToDoMainScreenPreview() {
    //ToDoMainScreen()
}

@Preview
@Composable
private fun DashboardScreenP() {
//    DashboardScreen(
//        email = "wsnkkrn@gmail.com",
//        onSettingClick = {},
//        onAddNewListClick = {},
//        onAddNewGroupClick = {}
//    )
}

@Preview
@Composable
private fun Stack() {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.Red,
        modifier = Modifier.fillMaxSize().padding(
            50.dp
        )
    ) {

        Text("Text asdadasd", fontSize = 20.sp)

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.Green,
            modifier = Modifier.fillMaxWidth()
                .size(30.dp)
                .padding(top = 80.dp)
        ) {

        }


    }
}

@Preview
@Composable
private fun RepeatingButtonPreview() {
    Box(Modifier.fillMaxSize().guide2(), contentAlignment = Alignment.Center) {
        Column {
            RepeatingButton(
                onClick = {
                    Loggr.debug { "wsnkrn resultt" }
                }
            ) {
                Text("Click me")
            }

            RepeatingButton2(
                onClick = {
                    Loggr.debug { "wsnkrn resultt 2" }
                }
            ) {
                Text("Click me 2")
            }

            // LaunchedEffectPrev()
        }
    }

}

@Composable
fun LaunchedEffectPrev() {
    var result by remember { mutableStateOf("null") }
    val snackbarHostState = remember { SnackbarHostState() }
    var text by remember { mutableStateOf("Initial data") }

    LaunchedEffect(Unit) {
        Loggr.debug { "wsnkrn snackbar click 1" }
        snackbarHostState.showSnackbar(
            message = "Are you happy with your input?",
            actionLabel = "Yes",
            duration = SnackbarDuration.Indefinite,
        )
        Loggr.debug { "wsnkrn snackbar click 2" }
        result = text
    }

    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    Scaffold(
        modifier = Modifier.height(400.dp),
        scaffoldState = scaffoldState,
        content = {
            Column {
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    }
                )
                Text(result)
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RepeatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    maxDelayMillis: Long = 1000,
    minDelayMillis: Long = 5,
    delayDecayFactor: Float = .15f,
    content: @Composable RowScope.() -> Unit
) {
    val pressed = remember { mutableStateOf(false) }
    val currentClickListener = remember { mutableStateOf(onClick) }
        .apply { value = onClick }

    Button(
        onClick = {},
        modifier = modifier.pointerInteropFilter {
            pressed.value = when (it.action) {
                MotionEvent.ACTION_DOWN -> true
                MotionEvent.ACTION_MOVE -> true
                else -> false
            }
            true
        },
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )


    LaunchedEffect(pressed.value, enabled) {
        var currentDelayMillis = maxDelayMillis

        while (enabled && pressed.value) {
            currentClickListener.value.invoke()
            Loggr.debug { "wsnkrn click $currentDelayMillis" }
            delay(currentDelayMillis)
            currentDelayMillis = (currentDelayMillis - (currentDelayMillis * delayDecayFactor)).toLong().coerceAtLeast(minDelayMillis)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RepeatingButton2(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    maxDelayMillis: Long = 1000,
    minDelayMillis: Long = 5,
    delayDecayFactor: Float = .15f,
    content: @Composable RowScope.() -> Unit
) {
    val currentClickListener = remember { mutableStateOf(onClick) }
        .apply { value = onClick }

    Button(
        onClick = {},
        modifier = modifier.pointerInput(interactionSource, enabled) {
            forEachGesture {
                coroutineScope {
                    awaitPointerEventScope {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val heldButtonJob = launch {
                            var currentDelayMillis = maxDelayMillis
                            while (enabled && down.pressed) {
                                currentClickListener.value()
                                delay(currentDelayMillis)
                                Loggr.debug { "wsnkrn click 2 $currentDelayMillis" }
                                currentDelayMillis = (currentDelayMillis - (currentDelayMillis * delayDecayFactor)).toLong().coerceAtLeast(minDelayMillis)
                            }
                        }
                        waitForUpOrCancellation()
                        heldButtonJob.cancel()
                    }
                }
            }
        },
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}
