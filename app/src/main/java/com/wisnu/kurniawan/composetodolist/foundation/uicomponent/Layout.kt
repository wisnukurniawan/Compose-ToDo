package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun PgPageLayout(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun PgModalLayout(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    PgModalLazyColumn(modifier) {
        item {
            Spacer(Modifier.height(24.dp))
            title()
            Spacer(Modifier.height(24.dp))
        }

        content()

        item {
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun PgModalLazyColumn(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    content: LazyListScope.() -> Unit
) {
    Box(
        modifier = Modifier.background(
            color = MaterialTheme.colors.secondaryVariant,
            shape = shape
        )
    ) {
        LazyColumn(
            modifier = modifier.navigationBarsWithImePadding(),
            content = content
        )
    }
}

@Composable
fun PgModalRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = Modifier.background(
            color = MaterialTheme.colors.secondaryVariant
        )
    ) {
        Row(
            modifier = modifier
                .navigationBarsWithImePadding(),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}
