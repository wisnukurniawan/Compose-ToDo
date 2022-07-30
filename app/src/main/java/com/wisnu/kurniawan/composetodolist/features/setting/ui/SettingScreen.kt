package com.wisnu.kurniawan.composetodolist.features.setting.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalTitle

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    onClickLogout: () -> Unit,
    onClickTheme: () -> Unit,
    onClickLanguage: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PgModalLayout(
        title = {
            PgModalTitle(stringResource(R.string.setting_title))
        },
        content = {
            items(state.items) { item ->
                SettingItem(
                    onClick = {
                        when (item) {
                            is SettingItem.Logout -> {
                                onClickLogout()
                            }
                            is SettingItem.Theme -> {
                                onClickTheme()
                            }
                            is SettingItem.Language -> {
                                onClickLanguage()
                            }
                        }
                    },
                    stringResource(item.title)
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    )
}

@Composable
private fun SettingItem(
    onClick: () -> Unit,
    title: String,
) {
    PgModalCell(
        onClick = onClick,
        text = title,
        color = MaterialTheme.colorScheme.surfaceVariant
    )
}
