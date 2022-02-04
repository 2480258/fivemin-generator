package com.fivemin.generator.presentation

import arrow.core.left
import arrow.core.right
import com.fivemin.generator.domain.attributeVerify.AttributeResponseEntity
import com.fivemin.generator.domain.attributeVerify.InternalAttributeRequestEntity
import com.fivemin.generator.domain.attributeVerify.NoParsedContentException
import com.fivemin.generator.model.* // ktlint-disable no-wildcard-imports
import com.fivemin.generator.service.attributeVerify.TextAttributeVerifier
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody

@CrossOrigin
@RestController
@RequestMapping("/api/preprocess/text")
class TextParseController {
    val textAttributeVerifier = TextAttributeVerifier()

    @PostMapping("with/html")
    fun createTextFromHtml(@RequestBody request: InternalAttributeRequestEntity): AttributeResponseEntity {
        return textAttributeVerifier.verifyTextAttribute(request)
    }
}
