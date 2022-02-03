package com.fivemin.generator.presentation

import arrow.core.toOption
import com.fivemin.core.engine.ParserNavigator
import com.fivemin.core.engine.transaction.serialize.postParser.LinkSelector
import com.fivemin.core.engine.transaction.serialize.postParser.linkExtract.LinkParserImpl
import com.fivemin.core.parser.HtmlParseableImpl
import com.fivemin.generator.domain.attributeVerify.AttributeResponseEntity
import com.fivemin.generator.domain.attributeVerify.LinkAttributeRequestEntity
import com.fivemin.generator.domain.attributeVerify.NoParsedContentException
import com.fivemin.generator.model.* // ktlint-disable no-wildcard-imports
import com.fivemin.generator.service.attributeVerify.LinkAttributeVerifier
import org.jsoup.Jsoup
import org.springframework.web.bind.annotation.*
import java.net.URI

@CrossOrigin
@RestController
@RequestMapping("/api/preprocess/link")
class LinkParseController {
    val linkAttributeVerifier = LinkAttributeVerifier()

    @PostMapping("with/html")
    fun createTextFromHtml(@RequestBody request: LinkAttributeRequestEntity): AttributeResponseEntity {
        return linkAttributeVerifier.verifyLinkAttribute(request)
    }
}
