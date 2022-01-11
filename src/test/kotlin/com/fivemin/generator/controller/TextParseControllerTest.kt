package com.fivemin.generator.controller

import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.InternalAttributeRequestEntity
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

internal class TextParseControllerTest {

    val textParseController = TextParseController()

    @Test
    fun createTextFromHtmlSuccTest() {
        val request = InternalAttributeRequestEntity("name", "a", "OUTER_HTML", "<a href=\"abc\"></a>")

        val result = textParseController.createTextFromHtml(request)

        assertEquals(result.mode, "single")
        assertEquals(result.name, "name")
        assertEquals(result.parseResult.count(), 1)
        assertEquals(result.parseResult.first(), "<a href=\"abc\"></a>")
    }

    @Test
    fun createTextFromHtmlFailTest() {
        val request = InternalAttributeRequestEntity("name", "q", "OUTER_HTML", "<a href=\"abc\"></a>")

        assertThrows(ErrorCodeThrowable::class.java) { textParseController.createTextFromHtml(request) }
    }
}
