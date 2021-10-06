package com.wisnu.kurniawan.composetodolist.features.todo.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTextField

@Composable
fun SearchWidget(
    modifier: Modifier,
    searchText: TextFieldValue,
    focusRequester: FocusRequester,
    onSearchChange: (TextFieldValue) -> Unit,
    onCancelClick: () -> Unit,
) {
    PgTextField(
        value = searchText,
        onValueChange = onSearchChange,
        placeholderValue = stringResource(R.string.todo_search),
        modifier = modifier
            .height(50.dp)
            .focusRequester(focusRequester),
        shape = MaterialTheme.shapes.large,
        textStyle = MaterialTheme.typography.subtitle2,
        leadingIcon = {
            PgIcon(
                imageVector = Icons.Rounded.Search
            )
        }
    )

    Spacer(Modifier.width(8.dp))

    Column {
        TextButton(
            onClick = onCancelClick,
            shape = CircleShape,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface)
        ) {
            Text(
                text = stringResource(R.string.todo_cancel),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface
            )
        }

        Spacer(Modifier.height(6.dp))
    }
}
