package com.fivemin.generator.presentation

import com.fivemin.generator.domain.*
import com.fivemin.generator.domain.RuleVerifier
import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.HttpErrorCode
import com.fivemin.generator.domain.ruleVerify.VerifyRequestEntity
import com.fivemin.generator.domain.ruleVerify.VerifyResultEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/verify")
class VerifyController {
    val ruleVerifier: RuleVerifier = RuleVerifier(listOf(PageShouldUnique(), ExportPageMatchesWithCurrentPage(), AttributeNameShouldUnique(), ExportTargettedAttributeMatches(), TagNameNotMatchWithFileExpression(), NoExportPage(), NoParsedPage(), ExternalAttributeWithJsonParse()))

    @PostMapping("json")
    fun verifyJson(@RequestBody request: VerifyRequestEntity) : VerifyResultEntity {
        try {
            val result = ruleVerifier.verifyParameter(request.json)

            return VerifyResultEntity(result.collectedMessages, result.notExecutedRules)
        } catch (e: Exception) {
            throw ErrorCodeThrowable(HttpErrorCode.BAD_REQUEST, e.message ?: "null")
        }
    }
}
