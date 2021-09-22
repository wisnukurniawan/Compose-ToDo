package com.wisnu.kurniawan.composetodolist.features.setting.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalTitle
import com.wisnu.kurniawan.composetodolist.runtime.navigation.SettingFlow

@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel,
) {
    val state by viewModel.state.collectAsState()

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
                                navController.navigate(SettingFlow.Logout.route)
                            }
                            is SettingItem.Theme -> {
                                navController.navigate(SettingFlow.Theme.route)
                            }
                            is SettingItem.Language -> {
                                navController.navigate(SettingFlow.Language.route)
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
        color = MaterialTheme.colors.secondary
    )
}
