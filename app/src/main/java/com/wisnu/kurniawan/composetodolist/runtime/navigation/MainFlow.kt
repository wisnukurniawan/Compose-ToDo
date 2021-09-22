package com.wisnu.kurniawan.composetodolist.runtime.navigation

sealed class MainFlow(val route: String) {
    object Root : MainFlow("main-root")
}

sealed class AuthFlow(val route: String) {
    object Root : HomeFlow("auth-root")
    object LoginScreen : AuthFlow("login-screen")
}

sealed class HomeFlow(val route: String) {
    object Root : HomeFlow("home-root")
    object DashboardScreen : HomeFlow("dashboard-screen")

    object GroupMenu : HomeFlow("group-menu-screen")
    object CreateGroup : HomeFlow("create-group-screen")
    object UpdateGroup : HomeFlow("update-group-screen")
    object UpdateGroupList : HomeFlow("update-group-list-screen")
    object EditGroupList : HomeFlow("edit-group-list-screen")
}

sealed class ListDetailFlow(val route: String) {
    object Root : ListDetailFlow("list-detail-root")
    object ListDetailScreen : ListDetailFlow("list-detail-screen")
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
    object Root : StepFlow("step-root")
    object TaskDetailScreen : StepFlow("task-detail-screen")
    object CreateStep : StepFlow("create-step-screen")
    object EditStep : StepFlow("edit-step-screen")
    object EditTask : StepFlow("edit-task-screen")
    object SelectRepeatTask : StepFlow("select-repeat-task-screen")
}

const val ARG_STEP_ID = "stepId"
const val ARG_TASK_ID = "taskId"
const val ARG_LIST_ID = "listId"
const val ARG_GROUP_ID = "groupId"
