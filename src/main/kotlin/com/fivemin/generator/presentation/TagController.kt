package com.fivemin.generator.presentation

import arrow.core.Either
import arrow.core.flatten
import com.fivemin.core.engine.Tag
import com.fivemin.core.engine.TagFlag
import com.fivemin.core.engine.transaction.TagSelector
import com.fivemin.generator.domain.attributeVerify.NoParsedContentException
import com.fivemin.generator.model.* // ktlint-disable no-wildcard-imports
import com.fivemin.generator.service.attributeVerify.TagVerifier
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports
import java.net.URI
import java.util.EnumSet

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
