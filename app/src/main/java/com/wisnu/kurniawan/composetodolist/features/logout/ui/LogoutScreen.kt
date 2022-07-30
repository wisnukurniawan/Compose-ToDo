package com.wisnu.kurniawan.composetodolist.features.logout.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalBackHeader
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.Profile
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.HandleEffect
import com.wisnu.kurniawan.composetodolist.runtime.navigation.HomeFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.MainFlow

@Composable
fun LogoutScreen(
    navController: NavController,
    viewModel: LogoutViewModel
) {
    val state by viewModel.state.collectAsState()

    HandleEffect(viewModel) {
        when (it) {
            LogoutEffect.NavigateToSplash -> {
                navController.navigate(MainFlow.Root.route) {
                    popUpTo(HomeFlow.DashboardScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    PgModalLayout(
        title = {
            PgModalBackHeader(
                text = stringResource(R.string.setting_logout_confirm),
                onClickBack = {
                    navController.navigateUp()
                }
            )
        },
        content = {
            item {
                Profile(state.user.email, modifier = Modifier.padding(bottom = 8.dp))
            }

            item {
                PgButton(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    onClick = { viewModel.dispatch(LogoutAction.ClickLogout) }
                ) { Text(text = stringResource(R.string.setting_logout), color = Color.White) }
            }
        }
    )
}
