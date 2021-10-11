package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

sealed class StepEffect {
    data class ScrollTo(val position: Int) : StepEffect()
}
