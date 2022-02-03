package com.fivemin.generator.service.attributeVerify

import arrow.core.toOption
import com.fivemin.core.engine.ParserNavigator
import com.fivemin.core.engine.transaction.serialize.postParser.LinkSelector
import com.fivemin.core.engine.transaction.serialize.postParser.linkExtract.LinkParserImpl
import com.fivemin.core.parser.HtmlParseableImpl
import com.fivemin.generator.domain.attributeVerify.AttributeResponseEntity
import com.fivemin.generator.domain.attributeVerify.LinkAttributeRequestEntity
import com.fivemin.generator.domain.attributeVerify.NoParsedContentException
import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.HttpErrorCode
import org.jsoup.Jsoup
import java.net.URI

class LinkAttributeVerifier {
    private val extractor = LinkParserImpl()

    fun verifyLinkAttribute(request: LinkAttributeRequestEntity): AttributeResponseEntity {
        try {
            val parsable = HtmlParseableImpl(Jsoup.parse(request.html))

            val parserNavigator = ParserNavigator(request.queryStr)
            val selector = LinkSelector(parserNavigator, request.uriRegex.toOption().map { Regex(it) })

            val result = extractor.parse(parsable, URI(request.hostUri), selector.toOption())

            val attributeConverted = if (!result.any()) {
                throw NoParsedContentException("No parsed attribute")
            } else if (result.count() == 1) {
                AttributeResponseEntity(request.name, result.first().absoluteUri.toString())
            } else {
                AttributeResponseEntity(
                    request.name,
                    result.map {
                        it.absoluteUri.toString()
                    }.toList()
                )
            }

            return attributeConverted
        } catch (e: Exception) {
            throw ErrorCodeThrowable(HttpErrorCode.BAD_REQUEST, e.message ?: "null")
        }
    }
}