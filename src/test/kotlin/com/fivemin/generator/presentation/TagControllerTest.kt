package com.fivemin.generator.presentation

import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.TagRequestEntity
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

internal class TagControllerTest {

    val tagController = TagController()

    @Test
    fun createTagFromURLSuccessTest() {
        val request = TagRequestEntity("name", "http://test.com", "(test)")

        val result = tagController.createTagFromURL(request)

        assertEquals(result.name, "name")
        assertEquals(result.value, "test")
    }

    @Test
    fun createTagFromURLRegexFailTest() {
        val request = TagRequestEntity("name", "http://test.com", "(test11)")

        assertThrows(ErrorCodeThrowable::class.java) { tagController.createTagFromURL(request) }
    }

    @Test
    fun createTagFromURLURIFailTest() {
        val request = TagRequestEntity("name", "aabc", "(aabc)")

        val result = tagController.createTagFromURL(request)

        assertEquals(result.name, "name")
        assertEquals(result.value, "aabc")
    }
}
