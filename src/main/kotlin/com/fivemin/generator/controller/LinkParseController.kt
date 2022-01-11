package com.fivemin.generator.controller

import arrow.core.toOption
import com.fivemin.core.engine.ParserNavigator
import com.fivemin.core.engine.transaction.serialize.postParser.LinkSelector
import com.fivemin.core.engine.transaction.serialize.postParser.linkExtract.LinkParserImpl
import com.fivemin.core.parser.HtmlParseableImpl
import com.fivemin.generator.model.* // ktlint-disable no-wildcard-imports
import org.jsoup.Jsoup
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/preprocess/link")
class LinkParseController {
    private val extractor = LinkParserImpl()

    @PostMapping("with/html")
    fun createTextFromHtml(request: LinkAttributeRequestEntity): AttributeResponseEntity {
        try {
            val parsable = HtmlParseableImpl(Jsoup.parse(request.html))

            val parserNavigator = ParserNavigator(request.queryStr)
            val selector = LinkSelector(parserNavigator, request.uriRegex.toOption().map { Regex(it) })

            val result = extractor.parse(parsable, URI(request.hostUri), selector.toOption())

            val attributeConverted = if (!result.any()) {
                throw NoParsedContentException("No parsed internal attribute")
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
