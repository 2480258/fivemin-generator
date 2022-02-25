package com.fivemin.generator.model
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

import com.fivemin.generator.service.JsonOptionFormat

data class VerifyResult(val collectedMessages: List<String>, val notExecutedRules: List<String>)

class RuleVerifier(private val rules: Iterable<JsonRule>) {

    fun verifyParameter(json: String): VerifyResult {
        val ruleStack: RuleStack = RuleStack(rules)

        val results = try {
            val option = Json.decodeFromString<JsonOptionFormat>(json)

            selectRules(option, ruleStack)
        } catch (e: Throwable) {
            listOf(RuleMatchedErrorException("Can't build json: " + (e.message ?: "null")))
        }

        return VerifyResult(results.map {
            it.message!!
        }, ruleStack.getNotPerformed().map {
            it.key.keyString
        })
    }

    private fun selectRules(option: JsonOptionFormat, ruleStack: RuleStack): MutableList<RuleMatchedErrorException> {
        val messages = mutableListOf<RuleMatchedErrorException>()
        var rule: JsonRule? = ruleStack.getNextRules()
        while (rule != null) {
            try {
                rule.checkAcceptability(option)
            } catch (e: RuleMatchedErrorException) {
                messages.add(e)
            } finally {
                rule = ruleStack.getNextRules()
            }
        }
        return messages
    }
}

class RuleStack(rules: Iterable<JsonRule>) {
    private val performedRules_executed: List<ExecutableRule>

    init {
        performedRules_executed = rules.map {
            ExecutableRule(it, false)
        }
    }

    fun getNextRules() : JsonRule? {
        val executableRule = performedRules_executed.firstOrNull {
            isPossibleToRun(it)
        }

        executableRule?.markExecuted()

        return executableRule?.rule
    }

    fun getNotPerformed(): Iterable<JsonRule> {
        return performedRules_executed.filter {
            !it.isExecuted
        }.map {
            it.rule
        }
    }

    private fun isPossibleToRun(rule: ExecutableRule): Boolean {
        if(rule.isExecuted) {
            return false
        }

        val performed = performedRules_executed.filter {
            it.isExecuted
        }.map {
            it.rule.key
        }

        val isNotSatisify = rule.rule.requires.filter {
            !performed.contains(it)
        }.any()

        return !isNotSatisify
    }
}

data class ExecutableRule (val rule: JsonRule, var isExecuted: Boolean) {
    fun markExecuted() {
        isExecuted = true
    }
}