package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalTitle
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.requestFocusImeAware
import kotlinx.coroutines.launch

@Composable
fun UpdateTaskNoteScreen(
    viewModel: StepViewModel,
) {
    val state by viewModel.state.collectAsState()
    val focusRequest = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        launch { focusRequest.requestFocusImeAware() }
        viewModel.dispatch(StepAction.NoteAction.OnShow)
    }

    PgModalLayout(
        title = {
            PgModalTitle(stringResource(R.string.todo_note))
        },
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                BasicTextField(
                    value = state.editNote,
                    onValueChange = { viewModel.dispatch(StepAction.NoteAction.ChangeNote(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .focusRequester(focusRequest),
                    textStyle = MaterialTheme.typography.body1.copy(color = LocalContentColor.current),
                    cursorBrush = SolidColor(MaterialTheme.colors.primaryVariant)
                )

                if (state.editNote.text.isBlank()) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                        Text(
                            text = stringResource(R.string.todo_add_note),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }
}
