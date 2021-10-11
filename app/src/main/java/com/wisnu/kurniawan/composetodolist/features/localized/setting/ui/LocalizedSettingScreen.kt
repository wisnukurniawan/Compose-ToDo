package com.wisnu.kurniawan.composetodolist.features.localized.setting.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.features.localized.base.ui.LocalizedEffect
import com.wisnu.kurniawan.composetodolist.foundation.localization.LocalizationUtil
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.collectAsEffect
import java.util.*

@Composable
fun LanguageScreen(
    navController: NavController,
    viewModel: LocalizedSettingViewModel
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsEffect()
    val context = LocalContext.current

    when (effect) {
        is LocalizedEffect.ApplyLanguage -> {
            LaunchedEffect(effect) {
                LocalizationUtil.applyLanguageContext(context, Locale((effect as LocalizedEffect.ApplyLanguage).language.lang))
                navController.navigateUp()
            }
        }
    }

    LanguageScreen(
        items = state.items,
        clickItem = {
            viewModel.dispatch(LocalizedSettingAction.SelectLanguage(it))
        },
        clickBack = {
            navController.navigateUp()
        }
    )
}

@Composable
private fun LanguageScreen(
    items: List<LanguageItem>,
    clickItem: (LanguageItem) -> Unit,
    clickBack: () -> Unit
) {
    PgPageLayout(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.CenterStart)
            ) {
                PgIconButton(
                    onClick = clickBack,
                    color = Color.Transparent
                ) {
                    PgIcon(imageVector = Icons.Rounded.ChevronLeft)
                }
            }

            Text(
                text = stringResource(R.string.setting_language),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item { Spacer(Modifier.height(16.dp)) }

            items(items) { item ->
                LanguageItem(
                    onClick = { clickItem(item) },
                    item = item
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LanguageItem(
    onClick: () -> Unit,
    item: LanguageItem,
) {
    PgModalCell(
        onClick = onClick,
        text = stringResource(item.title),
        color = if (item.applied) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.secondary
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
