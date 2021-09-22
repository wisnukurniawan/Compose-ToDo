package com.wisnu.kurniawan.composetodolist.foundation.wrapper

import java.util.*

object IdGenerator {
    fun generate(): String {
        return UUID.randomUUID().toString()
    }
}
