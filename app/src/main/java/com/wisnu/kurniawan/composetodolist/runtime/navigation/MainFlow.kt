package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

sealed class MainFlow(val route: String) {
    object Root : MainFlow("main-root")
}

sealed class AuthFlow(val route: String) {
    object Root : AuthFlow("auth-root")
    object LoginScreen : AuthFlow("login-screen")
}

sealed class HomeFlow(val route: String) {
    object Root : HomeFlow("home-root")
    object DashboardScreen : HomeFlow("dashboard-screen")

    object GroupMenu : HomeFlow("group-menu-screen") {
        val arguments = listOf(
            navArgument(ARG_GROUP_ID) {
                defaultValue = ""
            }
        )

        val routeRegistry = "$route?$ARG_GROUP_ID={$ARG_GROUP_ID}"

        fun route(groupId: String): String {
            return "$route?$ARG_GROUP_ID=${groupId}"
        }
    }

    object CreateGroup : HomeFlow("create-group-screen")
    object UpdateGroup : HomeFlow("update-group-screen") {
        val arguments = listOf(
            navArgument(ARG_GROUP_ID) {
                defaultValue = ""
            }
        )

        val routeRegistry = "$route?$ARG_GROUP_ID={$ARG_GROUP_ID}"

        fun route(groupId: String?): String {
            return "$route?$ARG_GROUP_ID=${groupId}"
        }
    }

    object UpdateGroupList : HomeFlow("update-group-list-screen") {
        val arguments = listOf(
            navArgument(ARG_GROUP_ID) {
                defaultValue = ""
            }
        )

        val routeRegistry = "$route?$ARG_GROUP_ID={$ARG_GROUP_ID}"

        fun route(groupId: String): String {
            return "$route?$ARG_GROUP_ID=${groupId}"
        }
    }

    object EditGroupList : HomeFlow("edit-group-list-screen") {
        val arguments = listOf(
            navArgument(ARG_GROUP_ID) {
                defaultValue = ""
            }
        )

        val routeRegistry = "$route?$ARG_GROUP_ID={$ARG_GROUP_ID}"

        fun route(groupId: String?): String {
            return "$route?$ARG_GROUP_ID=${groupId}"
        }
    }
}

sealed class ListDetailFlow(val route: String) {
    object Root : ListDetailFlow("list-detail-root") {
        val routeRegistry = "$route?$ARG_LIST_ID={$ARG_LIST_ID}"

        fun route(listId: String = ""): String {
            return "$route?$ARG_LIST_ID=${listId}"
        }
    }

    object ListDetailScreen : ListDetailFlow("list-detail-screen") {
        val arguments = listOf(
            navArgument(ARG_LIST_ID) {
                defaultValue = ""
            }
        )

        val routeRegistry = "$route?$ARG_LIST_ID={$ARG_LIST_ID}"
    }

    object CreateList : ListDetailFlow("create-list-screen")
    object UpdateList : ListDetailFlow("update-list-screen")
    object CreateTask : ListDetailFlow("create-task-screen")
}

sealed class SettingFlow(val route: String) {
    object Root : SettingFlow("setting-root")
    object Setting : SettingFlow("setting-screen")
    object Theme : SettingFlow("theme-screen")
    object Logout : SettingFlow("logout-screen")
    object Language : SettingFlow("language-screen")
}

sealed class StepFlow(val route: String) {
    object Root : StepFlow("step-root") {
        val routeRegistry = "$route?$ARG_TASK_ID={$ARG_TASK_ID}&$ARG_LIST_ID={$ARG_LIST_ID}"

        fun route(taskId: String, listId: String): String {
            return route + "?$ARG_TASK_ID=${taskId}&$ARG_LIST_ID=${listId}"
        }
    }

    object TaskDetailScreen : StepFlow("task-detail-screen") {
        val arguments = listOf(
            navArgument(ARG_TASK_ID) {
                defaultValue = ""
            },
            navArgument(ARG_LIST_ID) {
                defaultValue = ""
            }
        )

        val routeRegistry = "$route?$ARG_TASK_ID={$ARG_TASK_ID}&$ARG_LIST_ID={$ARG_LIST_ID}"

        val deepLinks = listOf(navDeepLink { uriPattern = "$BASE_DEEPLINK/$routeRegistry" })

        fun deeplink(taskId: String, listId: String): String {
            return "$BASE_DEEPLINK/$route?$ARG_TASK_ID=${taskId}&$ARG_LIST_ID=${listId}"
        }
    }

    object CreateStep : StepFlow("create-step-screen")
    object EditStep : StepFlow("edit-step-screen") {
        val arguments = listOf(
            navArgument(ARG_STEP_ID) {
                defaultValue = ""
            }
        )

        val routeRegistry = "$route?$ARG_STEP_ID={$ARG_STEP_ID}"

        fun route(stepId: String): String {
            return route + "?$ARG_STEP_ID=${stepId}"
        }
    }

    object EditTask : StepFlow("edit-task-screen")
    object SelectRepeatTask : StepFlow("select-repeat-task-screen")
}

const val BASE_DEEPLINK = "todox://com.wisnu.kurniawan"
const val ARG_STEP_ID = "stepId"
const val ARG_TASK_ID = "taskId"
const val ARG_LIST_ID = "listId"
const val ARG_GROUP_ID = "groupId"
