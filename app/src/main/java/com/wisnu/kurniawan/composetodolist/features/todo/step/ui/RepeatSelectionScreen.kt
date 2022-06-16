package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.displayable
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalTitle

@Composable
fun RepeatSelectionScreen(
    navController: NavController,
    viewModel: StepViewModel,
) {
    val state by viewModel.state.collectAsState()

    PgModalLayout(
        title = {
            PgModalTitle(
                text = stringResource(R.string.todo_add_due_date_repeat_task)
            )
        },
        content = {
            items(state.repeatItems) { item ->
                RepeatItem(
                    onClick = {
                        viewModel.dispatch(StepAction.TaskAction.SelectRepeat(item))
                        navController.navigateUp()
                    },
                    item = item
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    )
}

@Composable
private fun RepeatItem(
    onClick: () -> Unit,
    item: ToDoRepeatItem,
) {
    PgModalCell(
        onClick = onClick,
        text = stringResource(item.repeat.displayable()),
        color = if (item.applied) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        rightIcon = if (item.applied) {
            @Composable {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        } else {
            null
        }
    )
}
