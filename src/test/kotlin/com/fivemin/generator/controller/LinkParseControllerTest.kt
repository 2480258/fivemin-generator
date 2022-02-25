package com.fivemin.generator.controller

import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.attributeVerify.LinkAttributeRequestEntity
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

internal class LinkParseControllerTest {
    private val linkParseController = LinkParseController()

    @Test
    fun createTextFromHtmlMultipleTest() {
        val entity = LinkAttributeRequestEntity(
            "name",
            "a",
            "http://aaa.com",
            "<a href=\"/a\"></a><a href=\"/b\"></a><a href=\"/c\"></a>",
            null
        )

        val result = linkParseController.createTextFromHtml(entity)

        assertEquals(result.mode, "multiple")
        assertEquals(result.name, "name")
        assertEquals(result.parseResult.count(), 3)
        assertEquals(result.parseResult.first(), "http://aaa.com/a")
    }

    @Test
    fun createTextFromHtmlTest() {
        val entity = LinkAttributeRequestEntity(
            "name",
            "a",
            "http://aaa.com",
            "<a href=\"/a\"></a><a href=\"/a\"></a><a href=\"/a\"></a>",
            null
        )

        val result = linkParseController.createTextFromHtml(entity)

        assertEquals(result.mode, "single")
        assertEquals(result.name, "name")
        assertEquals(result.parseResult.count(), 1)
        assertEquals(result.parseResult.first(), "http://aaa.com/a")
    }

    @Test
    fun createTextFromHtmlWithRegexTest() {
        val entity = LinkAttributeRequestEntity(
            "name",
            "a",
            "http://aaa.com",
            "<a href=\"/a\"></a><a href=\"/b\"></a><a href=\"/a\"></a>",
            "b"
        )

        val result = linkParseController.createTextFromHtml(entity)

        assertEquals(result.mode, "single")
        assertEquals(result.name, "name")
        assertEquals(result.parseResult.count(), 1)
        assertEquals(result.parseResult.first(), "http://aaa.com/b")
    }

    @Test
    fun createTextFromHtmlNoParsedTest() {
        val entity = LinkAttributeRequestEntity(
            "name",
            "a",
            "http://aaa.com",
            "<a href=\"/a\"></a><a href=\"/b\"></a><a href=\"/a\"></a>",
            "no_regex"
        )

        assertThrows(ErrorCodeThrowable::class.java) {
            linkParseController.createTextFromHtml(entity)
        }
    }
}
