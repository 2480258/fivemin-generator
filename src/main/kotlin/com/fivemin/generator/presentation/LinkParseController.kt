package com.fivemin.generator.presentation

import com.fivemin.generator.domain.attributeVerify.AttributeResponseEntity
import com.fivemin.generator.domain.attributeVerify.LinkAttributeRequestEntity
import com.fivemin.generator.service.attributeVerify.LinkAttributeVerifier
import org.springframework.web.bind.annotation.*

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
