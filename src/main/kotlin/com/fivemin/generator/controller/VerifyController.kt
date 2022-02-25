package com.fivemin.generator.controller

import com.fivemin.generator.model.*
import com.fivemin.generator.model.RuleVerifier
import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.HttpErrorCode
import com.fivemin.generator.model.ruleVerify.VerifyRequestEntity
import com.fivemin.generator.model.ruleVerify.VerifyResultEntity
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
