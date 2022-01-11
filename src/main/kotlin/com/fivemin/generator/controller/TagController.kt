package com.fivemin.generator.controller

import arrow.core.Either
import arrow.core.flatten
import com.fivemin.core.engine.Tag
import com.fivemin.core.engine.TagFlag
import com.fivemin.core.engine.transaction.TagSelector
import com.fivemin.generator.model.* // ktlint-disable no-wildcard-imports
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports
import java.net.URI
import java.util.EnumSet

@RestController
@RequestMapping("/api/preprocess/tag")
class TagController {
    private fun tagToEntity(tag: Tag): TagResultEntity {
        return TagResultEntity(tag.name, tag.value, tag.flag)
    }

    @PostMapping("with/url")
    fun createTagFromURL(
        request: TagRequestEntity
    ): TagResultEntity {
        val selector = TagSelector(request.name, Regex(request.tagRegex), EnumSet.of(TagFlag.NONE))

        val urlInstance = Either.catch { // pre-check url parameter is really URL
            URI(request.url)
        }

        val result = urlInstance.map {
            selector.build(it).toEither {
                NoParsedContentException("No parsed tags from given regex")
            }
        }.flatten()

        return result.fold({ throw ErrorCodeThrowable(HttpErrorCode.BAD_REQUEST, it.message ?: "Not provided") }) {
            tagToEntity(it)
        }
    }
}
