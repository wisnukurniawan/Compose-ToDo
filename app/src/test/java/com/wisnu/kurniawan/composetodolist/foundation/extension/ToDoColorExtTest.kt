package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.theme.ListBlue
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListBrown
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListGreen
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListOrange
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListPurple
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListYellow
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import org.junit.Assert
import org.junit.Test

class ToDoColorExtTest {

    @Test
    fun toColor() {
        Assert.assertEquals(ListBlue, ToDoColor.BLUE.toColor())
        Assert.assertEquals(ListRed, ToDoColor.RED.toColor())
        Assert.assertEquals(ListGreen, ToDoColor.GREEN.toColor())
        Assert.assertEquals(ListYellow, ToDoColor.YELLOW.toColor())
        Assert.assertEquals(ListOrange, ToDoColor.ORANGE.toColor())
        Assert.assertEquals(ListPurple, ToDoColor.PURPLE.toColor())
        Assert.assertEquals(ListBrown, ToDoColor.BROWN.toColor())
    }

}
