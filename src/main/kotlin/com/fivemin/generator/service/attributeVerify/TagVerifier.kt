package com.fivemin.generator.service.attributeVerify

import arrow.core.Either
import arrow.core.flatten
import com.fivemin.generator.model.attributeVerify.NoParsedContentException
import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.HttpErrorCode
import com.fivemin.generator.model.TagRequestEntity
import com.fivemin.generator.model.TagResultEntity
import com.fivemin.generator.service.Tag
import com.fivemin.generator.service.TagFlag
import com.fivemin.generator.service.TagSelector
import java.net.URI
import java.util.*

class TagVerifier {
    private fun tagToEntity(tag: Tag): TagResultEntity {
        return TagResultEntity(tag.name, tag.value, tag.flag)
    }

    fun verifyTag(request: TagRequestEntity): TagResultEntity {
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