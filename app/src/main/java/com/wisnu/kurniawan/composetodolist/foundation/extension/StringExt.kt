package com.wisnu.kurniawan.composetodolist.foundation.extension

fun String.firstOrEmpty(): String = if (isBlank()) "" else first().toString()




