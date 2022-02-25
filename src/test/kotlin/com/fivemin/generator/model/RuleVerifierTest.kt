package com.fivemin.generator.model

import io.mockk.every
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach

import io.mockk.mockk
import java.io.File
import kotlin.test.assertEquals

internal class RuleVerifierTest {

    lateinit var ruleVerifier: RuleVerifier

    @BeforeEach
    fun before() {
    }

    @Test
    fun jsonBuildErrorTest() {
        val rule = mockk<JsonRule>()

        every {
            rule.key
        } returns(RuleCheckKey("test"))

        every {
            rule.checkAcceptability(any())
        } throws(RuleMatchedErrorException("test"))

        every {
            rule.requires
        } returns(listOf())

        ruleVerifier = RuleVerifier(listOf(rule))

        val result = ruleVerifier.verifyParameter("")

        assertEquals(result.notExecutedRules, listOf("test"))
        assert(result.collectedMessages.count() == 1)
        assert(result.collectedMessages.first().contains("build"))
    }

    @Test
    fun meetRequiresTest() {
        val afterRule = mockk<JsonRule>()
        every {
            afterRule.requires
        } returns(listOf(RuleCheckKey("test")))

        every {
            afterRule.checkAcceptability(any())
        } throws(RuleMatchedErrorException("after"))

        val beforeRule = mockk<JsonRule>()

        every {
            beforeRule.key
        } returns(RuleCheckKey("test"))

        every {
            beforeRule.checkAcceptability(any())
        } throws(RuleMatchedErrorException("before"))

        every {
            beforeRule.requires
        } returns(listOf())


        ruleVerifier = RuleVerifier(listOf(afterRule, beforeRule))

        val result = ruleVerifier.verifyParameter(File("skeleton.json").readText())

        assertEquals(result.collectedMessages, listOf("before", "after"))
        assertEquals(result.notExecutedRules, listOf())
    }

    @Test
    fun neverRequiresTest() {
        val afterRule = mockk<JsonRule>()
        every {
            afterRule.requires
        } returns(listOf(RuleCheckKey("never")))

        every {
            afterRule.key
        } returns(RuleCheckKey("after"))

        every {
            afterRule.checkAcceptability(any())
        } throws(RuleMatchedErrorException("after"))

        val beforeRule = mockk<JsonRule>()

        every {
            beforeRule.key
        } returns(RuleCheckKey("test"))

        every {
            beforeRule.checkAcceptability(any())
        } throws(RuleMatchedErrorException("before"))

        every {
            beforeRule.requires
        } returns(listOf())


        ruleVerifier = RuleVerifier(listOf(afterRule, beforeRule))

        val result = ruleVerifier.verifyParameter(File("skeleton.json").readText())

        assertEquals(result.collectedMessages, listOf("before"))
        assertEquals(result.notExecutedRules, listOf("after"))
    }
}