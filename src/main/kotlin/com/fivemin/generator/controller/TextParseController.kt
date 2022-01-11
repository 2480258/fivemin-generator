package com.fivemin.generator.controller

import arrow.core.left
import arrow.core.right
import com.fivemin.core.engine.* // ktlint-disable no-wildcard-imports
import com.fivemin.core.engine.parser.TextExtractorImpl
import com.fivemin.core.engine.transaction.serialize.postParser.TextSelectionMode
import com.fivemin.core.parser.HtmlDocumentFactoryImpl
import com.fivemin.generator.model.* // ktlint-disable no-wildcard-imports
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/preprocess/text")
class TextParseController {
    private val selector = TextExtractorImpl()
    private val htmlFactory = HtmlDocumentFactoryImpl()

    private fun extractModeFromString(mode: String): TextSelectionMode {
        return when (mode) {
            "TEXT_CONTENT" -> TextSelectionMode.TEXT_CONTENT
            "OUTER_HTML" -> TextSelectionMode.OUTER_HTML
            "INNER_HTML" -> TextSelectionMode.INNER_HTML
            else -> throw NoParsedContentException("No text selection mode matched")
        }
    }

    @PostMapping("with/html")
    fun createTextFromHtml(request: InternalAttributeRequestEntity): AttributeResponseEntity {
        val parserNavigator = ParserNavigator(request.queryStr)

        val memoryData = HtmlMemoryDataImpl(StringMemoryDataImpl(ArrayMemoryData(Charsets.UTF_8.encode(request.html).array()), Charsets.UTF_8), htmlFactory)

        val result = selector.parse(memoryData, parserNavigator, extractModeFromString(request.parseMode))

        val attributeConverted = if (!result.any()) {
            NoParsedContentException("No parsed internal attribute").left()
        } else if (result.count() == 1) {
            AttributeResponseEntity(request.name, result.first()).right()
        } else {
            AttributeResponseEntity(request.name, result.toList()).right()
        }

        return attributeConverted.fold({
            throw ErrorCodeThrowable(HttpErrorCode.BAD_REQUEST, it.message ?: "null")
        }) {
            it
        }
    }
}
