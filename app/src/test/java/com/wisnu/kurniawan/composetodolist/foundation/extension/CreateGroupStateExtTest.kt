package com.wisnu.kurniawan.composetodolist.foundation.extension

import androidx.compose.ui.text.input.TextFieldValue
import com.wisnu.kurniawan.composetodolist.features.todo.group.ui.CreateGroupState
import org.junit.Assert
import org.junit.Test

class CreateGroupStateExtTest {

    @Test
    fun validGroupName() {
        Assert.assertTrue(CreateGroupState(TextFieldValue("name")).isValidGroupName())
    }

    @Test
    fun invalidGroupName() {
        Assert.assertFalse(CreateGroupState(TextFieldValue("")).isValidGroupName())
    }

}
