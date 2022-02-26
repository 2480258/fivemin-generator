package com.fivemin.generator.service.attributeVerify

import arrow.core.toOption
import com.fivemin.generator.model.attributeVerify.AttributeResponseEntity
import com.fivemin.generator.model.attributeVerify.LinkAttributeRequestEntity
import com.fivemin.generator.model.attributeVerify.NoParsedContentException
import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.HttpErrorCode
import com.fivemin.generator.service.HtmlParsable
import com.fivemin.generator.service.LinkParserImpl
import com.fivemin.generator.service.LinkSelector
import com.fivemin.generator.service.ParserNavigator
import org.jsoup.Jsoup
import java.net.URI

class LinkAttributeVerifier {
    private val extractor = LinkParserImpl()

    fun verifyLinkAttribute(request: LinkAttributeRequestEntity): AttributeResponseEntity {
        try {
            if(request.name == "" || request.html == "" || request.hostUri == "" || request.queryStr == "") {
                throw ErrorCodeThrowable(HttpErrorCode.BAD_REQUEST, "Please check your form. Name, html, hostUri, queryStr should be filled")
            }

            val parsable = HtmlParsable(Jsoup.parse(request.html))

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