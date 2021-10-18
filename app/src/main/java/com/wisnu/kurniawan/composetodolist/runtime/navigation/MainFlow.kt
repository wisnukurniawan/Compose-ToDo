package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ScheduledType

sealed class MainFlow(val name: String) {
    object Root : MainFlow("main-root") {
        val route = name
    }

    object RootEmpty : MainFlow("list-detail-root-empty") {
        val route = name
    }
}

sealed class AuthFlow(val name: String) {
    object Root : AuthFlow("auth-root") {
        val route = name
    }

    object LoginScreen : AuthFlow("login-screen") {
        val route = name
    }
}

sealed class HomeFlow(val name: String) {
    object Root : HomeFlow("home-root") {
        val route = name
    }

    object DashboardScreen : HomeFlow("dashboard-screen") {
        val route = name
    }

    object GroupMenu : HomeFlow("group-menu-screen") {
        val arguments = listOf(
            navArgument(ARG_GROUP_ID) {
                defaultValue = ""
            }
        )

        val route = "$name?$ARG_GROUP_ID={$ARG_GROUP_ID}"

        fun route(groupId: String): String {
            return "$name?$ARG_GROUP_ID=${groupId}"
        }
    }

    object CreateGroup : HomeFlow("create-group-screen") {
        val route = name
    }

    object UpdateGroup : HomeFlow("update-group-screen") {
        val arguments = listOf(
            navArgument(ARG_GROUP_ID) {
                defaultValue = ""
            }
        )

        val route = "$name?$ARG_GROUP_ID={$ARG_GROUP_ID}"

        fun route(groupId: String?): String {
            return "$name?$ARG_GROUP_ID=${groupId}"
        }
    }

    object UpdateGroupList : HomeFlow("update-group-list-screen") {
        val arguments = listOf(
            navArgument(ARG_GROUP_ID) {
                defaultValue = ""
            }
        )

        val route = "$name?$ARG_GROUP_ID={$ARG_GROUP_ID}"

        fun route(groupId: String): String {
            return "$name?$ARG_GROUP_ID=${groupId}"
        }
    }

    object EditGroupList : HomeFlow("edit-group-list-screen") {
        val arguments = listOf(
            navArgument(ARG_GROUP_ID) {
                defaultValue = ""
            }
        )

        val route = "$name?$ARG_GROUP_ID={$ARG_GROUP_ID}"

        fun route(groupId: String?): String {
            return "$name?$ARG_GROUP_ID=${groupId}"
        }
    }
}

sealed class ScheduledFlow(val name: String) {
    object Root : ScheduledFlow("scheduled-root") {
        val route = "$name/{$ARG_SCHEDULED_TYPE}"

        fun route(): String {
            return "$name/${ScheduledType.SCHEDULED}"
        }
    }

    object ScheduledScreen : ScheduledFlow("scheduled-screen") {
        val arguments = listOf(
            navArgument(ARG_SCHEDULED_TYPE) {
                type = NavType.StringType
            }
        )

        val route = "$name/{$ARG_SCHEDULED_TYPE}"
    }
}

sealed class ScheduledTodayFlow(val name: String) {
    object Root : ScheduledTodayFlow("scheduled-today-root") {
        val route = "$name/{$ARG_SCHEDULED_TYPE}"

        fun route(): String {
            return "$name/${ScheduledType.TODAY}"
        }
    }

    object ScheduledTodayScreen : ScheduledTodayFlow("scheduled-today-screen") {
        val arguments = listOf(
            navArgument(ARG_SCHEDULED_TYPE) {
                type = NavType.StringType
            }
        )

        val route = "$name/{$ARG_SCHEDULED_TYPE}"
    }
}

sealed class AllFlow(val name: String) {
    object Root : AllFlow("all-root") {
        val route = name
    }

    object AllScreen : AllFlow("all-screen") {
        val route = name
    }
}

sealed class ListDetailFlow(val name: String) {
    object Root : ListDetailFlow("list-detail-root") {
        val route = "$name?$ARG_LIST_ID={$ARG_LIST_ID}"

        fun route(listId: String = ""): String {
            return "$name?$ARG_LIST_ID=${listId}"
        }
    }

    object ListDetailScreen : ListDetailFlow("list-detail-screen") {
        val arguments = listOf(
            navArgument(ARG_LIST_ID) {
                defaultValue = ""
            }
        )

        val route = "$name?$ARG_LIST_ID={$ARG_LIST_ID}"
    }

    object CreateList : ListDetailFlow("create-list-screen") {
        val route = name
    }

    object UpdateList : ListDetailFlow("update-list-screen") {
        val route = name
    }

    object CreateTask : ListDetailFlow("create-task-screen") {
        val route = name
    }
}

sealed class SettingFlow(val name: String) {
    object Root : SettingFlow("setting-root") {
        val route = name
    }

    object Setting : SettingFlow("setting-screen") {
        val route = name
    }

    object Theme : SettingFlow("theme-screen") {
        val route = name
    }

    object Logout : SettingFlow("logout-screen") {
        val route = name
    }

    object Language : SettingFlow("language-screen") {
        val route = name
    }
}

sealed class StepFlow(val name: String) {
    object Root : StepFlow("step-root") {
        val route = "$name?$ARG_TASK_ID={$ARG_TASK_ID}&$ARG_LIST_ID={$ARG_LIST_ID}"

        fun route(taskId: String, listId: String): String {
            return "$name?$ARG_TASK_ID=${taskId}&$ARG_LIST_ID=${listId}"
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

        val route = "$name?$ARG_TASK_ID={$ARG_TASK_ID}&$ARG_LIST_ID={$ARG_LIST_ID}"

        val deepLinks = listOf(navDeepLink { uriPattern = "$BASE_DEEPLINK/$route" })

        fun deeplink(taskId: String, listId: String): String {
            return "$BASE_DEEPLINK/$name?$ARG_TASK_ID=${taskId}&$ARG_LIST_ID=${listId}"
        }
    }

    object CreateStep : StepFlow("create-step-screen") {
        val route = name
    }

    object EditStep : StepFlow("edit-step-screen") {
        val arguments = listOf(
            navArgument(ARG_STEP_ID) {
                defaultValue = ""
            }
        )

        val route = "$name?$ARG_STEP_ID={$ARG_STEP_ID}"

        fun route(stepId: String): String {
            return "$name?$ARG_STEP_ID=${stepId}"
        }
    }

    object EditTask : StepFlow("edit-task-screen") {
        val route = name
    }

    object SelectRepeatTask : StepFlow("select-repeat-task-screen") {
        val route = name
    }

    object UpdateTaskNote : StepFlow("update-task-note-screen") {
        val route = name
    }
}

sealed class SearchFlow(val name: String) {
    object Root : SearchFlow("search-root") {
        val route = name
    }

    object SearchScreen : SearchFlow("search-screen") {
        val route = name
    }
}

const val BASE_DEEPLINK = "todox://com.wisnu.kurniawan"

const val ARG_STEP_ID = "stepId"
const val ARG_TASK_ID = "taskId"
const val ARG_LIST_ID = "listId"
const val ARG_GROUP_ID = "groupId"
const val ARG_SCHEDULED_TYPE = "scheduledType"
