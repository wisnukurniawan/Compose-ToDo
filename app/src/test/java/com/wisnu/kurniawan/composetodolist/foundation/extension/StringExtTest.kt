package com.wisnu.kurniawan.composetodolist.foundation.extension

import org.junit.Assert
import org.junit.Test

class StringExtTest {

    @Test
    fun firstOrEmptyOnBlankString() {
        val testString = ""

        Assert.assertEquals("", testString.firstOrEmpty())
    }


    @Test
    fun firstOrEmptyOnNotBlankString() {
        val testString = "Test"

        Assert.assertEquals("T", testString.firstOrEmpty())
    }

    @Test
    fun newNameWithSuffix() {
        Assert.assertEquals("1", "".addSuffixIdentifier())
        Assert.assertEquals("3 1", "3".addSuffixIdentifier())
        Assert.assertEquals("name 1", "name".addSuffixIdentifier())
        Assert.assertEquals("name name 1", "name name".addSuffixIdentifier())
        Assert.assertEquals("name 2", "name 1".addSuffixIdentifier())
        Assert.assertEquals("name 2 4", "name 2 3".addSuffixIdentifier())
        Assert.assertEquals("name 900", "name 899".addSuffixIdentifier())
        Assert.assertEquals("name  900", "name  899".addSuffixIdentifier())
        Assert.assertEquals("name  1", "name  0".addSuffixIdentifier())
        Assert.assertEquals("1 2", "1 1".addSuffixIdentifier())
        Assert.assertEquals("1 1 2", "1 1 1".addSuffixIdentifier())
    }

    @Test
    fun resolveConflict() {
        val list1 = listOf(
            "name",
            "name 1",
            "name 2",
            "name 3",
            "name 4",
        )
        val list2 = listOf(
            "name",
            "name 1",
            "name 3",
            "name 4",
        )
        val list3 = listOf(
            "name",
            "name 1",
            "name 3",
            "name 4",
            "name 50",
        )

        Assert.assertEquals("name 5", "name".resolveDuplicate(list1))
        Assert.assertEquals("name 5", "name 3".resolveDuplicate(list1))
        Assert.assertEquals("name 6", "name 6".resolveDuplicate(list1))
        Assert.assertEquals("name 2", "name".resolveDuplicate(list2))
        Assert.assertEquals("name 5", "name 3".resolveDuplicate(list2))
        Assert.assertEquals("name 51", "name 50".resolveDuplicate(list3))
        Assert.assertEquals("name 49", "name 49".resolveDuplicate(list3))
        Assert.assertEquals("name1", "name1".resolveDuplicate(list3))
        Assert.assertEquals("namee 1", "namee 1".resolveDuplicate(list3))
    }

}
