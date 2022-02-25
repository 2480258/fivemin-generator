package com.fivemin.generator.controller

import com.fivemin.generator.model.* // ktlint-disable no-wildcard-imports
import com.fivemin.generator.service.attributeVerify.TagVerifier
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports

@CrossOrigin
@RestController
@RequestMapping("/api/preprocess/tag")
class TagController {
    val tagVerifier = TagVerifier()

    @PostMapping("with/url")
    fun createTagFromURL(@RequestBody request: TagRequestEntity): TagResultEntity {
        return tagVerifier.verifyTag(request)
    }
}
