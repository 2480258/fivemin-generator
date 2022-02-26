package com.fivemin.generator.service.attributeVerify

import arrow.core.left
import arrow.core.right
import com.fivemin.generator.model.attributeVerify.AttributeResponseEntity
import com.fivemin.generator.model.attributeVerify.InternalAttributeRequestEntity
import com.fivemin.generator.model.attributeVerify.NoParsedContentException
import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.HttpErrorCode
import com.fivemin.generator.service.HtmlDocumentFactory
import com.fivemin.generator.service.ParserNavigator
import com.fivemin.generator.service.TextExtractor
import com.fivemin.generator.service.TextSelectionMode

class TextAttributeVerifier {

    private val selector = TextExtractor()
    private val htmlFactory = HtmlDocumentFactory()

    private fun extractModeFromString(mode: String): TextSelectionMode {
        return when (mode) {
            "TEXT_CONTENT" -> TextSelectionMode.TEXT_CONTENT
            "OUTER_HTML" -> TextSelectionMode.OUTER_HTML
            "INNER_HTML" -> TextSelectionMode.INNER_HTML
            else -> throw NoParsedContentException("No text selection mode matched")
        }
    }

    fun verifyTextAttribute(request: InternalAttributeRequestEntity): AttributeResponseEntity{
        if(request.name == "" || request.html == "" || request.parseMode == "" || request.queryStr == "") {
            throw ErrorCodeThrowable(HttpErrorCode.BAD_REQUEST, "Please check your form. Name, html, parseMode, queryStr should be filled")
        }

        val parserNavigator = ParserNavigator(request.queryStr)

        val memoryData = htmlFactory.create(request.html)

        val result = selector.parse(memoryData, parserNavigator, extractModeFromString(request.parseMode))

        val attributeConverted = if (!result.any()) {
            NoParsedContentException("No parsed attribute").left()
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