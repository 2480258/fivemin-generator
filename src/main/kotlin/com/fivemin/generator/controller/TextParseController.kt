package com.fivemin.generator.controller

import com.fivemin.generator.model.attributeVerify.AttributeResponseEntity
import com.fivemin.generator.model.attributeVerify.InternalAttributeRequestEntity
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
