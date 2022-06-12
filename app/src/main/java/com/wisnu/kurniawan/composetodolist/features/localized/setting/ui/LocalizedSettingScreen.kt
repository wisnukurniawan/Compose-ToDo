package com.wisnu.kurniawan.composetodolist.features.localized.setting.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalBackHeader
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.collectAsEffect

@Composable
fun LanguageScreen(
    navController: NavController,
    viewModel: LocalizedSettingViewModel
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsEffect()

    when (effect) {
        is LocalizedEffect.ApplyLanguage -> {
            LaunchedEffect(effect) {
                val lang = (effect as LocalizedEffect.ApplyLanguage).language.lang
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lang)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
        }
        null -> {}
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
    PgModalLayout(
        title = {
            PgModalBackHeader(
                text = stringResource(R.string.setting_language),
                onClickBack = clickBack
            )
        },
        content = {
            items(items) { item ->
                LanguageItem(
                    onClick = { clickItem(item) },
                    item = item
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    )
}

@Composable
private fun LanguageItem(
    onClick: () -> Unit,
    item: LanguageItem,
) {
    PgModalCell(
        onClick = onClick,
        text = stringResource(item.title),
        color = if (item.applied) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.secondary
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
