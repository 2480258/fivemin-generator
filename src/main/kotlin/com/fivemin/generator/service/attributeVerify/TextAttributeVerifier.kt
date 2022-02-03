package com.fivemin.generator.service.attributeVerify

import arrow.core.left
import arrow.core.right
import com.fivemin.core.engine.ArrayMemoryData
import com.fivemin.core.engine.HtmlMemoryDataImpl
import com.fivemin.core.engine.ParserNavigator
import com.fivemin.core.engine.StringMemoryDataImpl
import com.fivemin.core.engine.parser.TextExtractorImpl
import com.fivemin.core.engine.transaction.serialize.postParser.TextSelectionMode
import com.fivemin.core.parser.HtmlDocumentFactoryImpl
import com.fivemin.generator.domain.attributeVerify.AttributeResponseEntity
import com.fivemin.generator.domain.attributeVerify.InternalAttributeRequestEntity
import com.fivemin.generator.domain.attributeVerify.NoParsedContentException
import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.HttpErrorCode

class TextAttributeVerifier {

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

    fun verifyTextAttribute(request: InternalAttributeRequestEntity): AttributeResponseEntity{
        val parserNavigator = ParserNavigator(request.queryStr)

        val memoryData = HtmlMemoryDataImpl(StringMemoryDataImpl(ArrayMemoryData(Charsets.UTF_8.encode(request.html).array()), Charsets.UTF_8), htmlFactory)

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