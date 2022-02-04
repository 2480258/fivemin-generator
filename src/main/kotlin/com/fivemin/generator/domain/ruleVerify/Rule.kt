package com.fivemin.generator.domain

import com.fivemin.generator.service.JsonOptionFormat

class RuleMatchedErrorException(message: String) : Exception(message)

interface JsonRule: JsonRuleKey {
    fun checkAcceptability(format: JsonOptionFormat)
}

interface  JsonRuleKey {
    val key: RuleCheckKey
    val requires: Iterable<RuleCheckKey>
}

data class RuleCheckKey(val keyString: String)

