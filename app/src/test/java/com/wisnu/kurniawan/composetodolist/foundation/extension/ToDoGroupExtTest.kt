package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.DateFactory
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ItemMainState
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.SelectedItemState
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.toItemGroup
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toGroupDp
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import org.junit.Assert
import org.junit.Test

class ToDoGroupExtTest {

    @Test
    fun toItemGroupSingleList() {
        val data = listOf(
            ToDoGroup(
                id = "group1",
                name = "group1",
                lists = listOf(
                    ToDoList(
                        id = "list1",
                        name = "list1",
                        color = ToDoColor.BROWN,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    )
                ),
                createdAt = DateFactory.constantDate,
                updatedAt = DateFactory.constantDate,
            )
        )

        Assert.assertEquals(
            listOf(
                ItemMainState.ItemGroup(
                    group = ToDoGroup(
                        id = "group1",
                        name = "group1",
                        lists = listOf(
                            ToDoList(
                                id = "list1",
                                name = "list1",
                                color = ToDoColor.BROWN,
                                tasks = listOf(),
                                createdAt = DateFactory.constantDate,
                                updatedAt = DateFactory.constantDate,
                            )
                        ),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    )
                ),
                ItemMainState.ItemListType.Single(
                    list = ToDoList(
                        id = "list1",
                        name = "list1",
                        color = ToDoColor.BROWN,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    ),
                    selected = false
                )
            ),
            data.toItemGroup(SelectedItemState.Empty)
        )
    }

    @Test
    fun toItemGroupSingleMultiple() {
        val data = listOf(
            ToDoGroup(
                id = "group1",
                name = "group1",
                lists = listOf(
                    ToDoList(
                        id = "list1",
                        name = "list1",
                        color = ToDoColor.BROWN,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    ),
                    ToDoList(
                        id = "list2",
                        name = "list2",
                        color = ToDoColor.BROWN,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    ),
                    ToDoList(
                        id = "list3",
                        name = "list3",
                        color = ToDoColor.BROWN,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    )
                ),
                createdAt = DateFactory.constantDate,
                updatedAt = DateFactory.constantDate,
            )
        )

        Assert.assertEquals(
            listOf(
                ItemMainState.ItemGroup(
                    group = ToDoGroup(
                        id = "group1",
                        name = "group1",
                        lists = listOf(
                            ToDoList(
                                id = "list1",
                                name = "list1",
                                color = ToDoColor.BROWN,
                                tasks = listOf(),
                                createdAt = DateFactory.constantDate,
                                updatedAt = DateFactory.constantDate,
                            ),
                            ToDoList(
                                id = "list2",
                                name = "list2",
                                color = ToDoColor.BROWN,
                                tasks = listOf(),
                                createdAt = DateFactory.constantDate,
                                updatedAt = DateFactory.constantDate,
                            ),
                            ToDoList(
                                id = "list3",
                                name = "list3",
                                color = ToDoColor.BROWN,
                                tasks = listOf(),
                                createdAt = DateFactory.constantDate,
                                updatedAt = DateFactory.constantDate,
                            )
                        ),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    )
                ),
                ItemMainState.ItemListType.First(
                    list = ToDoList(
                        id = "list1",
                        name = "list1",
                        color = ToDoColor.BROWN,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    ),
                    selected = false
                ),
                ItemMainState.ItemListType.Middle(
                    list = ToDoList(
                        id = "list2",
                        name = "list2",
                        color = ToDoColor.BROWN,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    ),
                    selected = false
                ),
                ItemMainState.ItemListType.Last(
                    list = ToDoList(
                        id = "list3",
                        name = "list3",
                        color = ToDoColor.BROWN,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    ),
                    selected = false
                )
            ),
            data.toItemGroup(SelectedItemState.Empty)
        )
    }

    @Test
    fun toGroupDp() {
        Assert.assertEquals(
            listOf(
                ToDoGroupDb(
                    id = "id",
                    name = "name",
                    createdAt = DateFactory.constantDate,
                    updatedAt = DateFactory.constantDate,
                )
            ),
            listOf(
                ToDoGroup(
                    id = "id",
                    name = "name",
                    lists = listOf(),
                    createdAt = DateFactory.constantDate,
                    updatedAt = DateFactory.constantDate,
                )
            ).toGroupDp()
        )
    }

}
