package com.fivemin.generator.domain.ruleVerify

import com.fivemin.core.initialize.json.*
import com.fivemin.generator.domain.NoParsedPage
import com.fivemin.generator.domain.RuleMatchedErrorException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class NoParsedPageTest {

    val noParsedPage = NoParsedPage()

    @Test
    fun checkAcceptability() {
        val option = mockk<JsonOptionFormat>()
        val parseOpt = mockk<JsonPrePostParserFormat>()

        every {
            option.parseFormat
        } returns (parseOpt)

        every {
            parseOpt.pages
        } returns (listOf())

        org.junit.jupiter.api.assertThrows<RuleMatchedErrorException> {
            noParsedPage.checkAcceptability(option)
        }
    }
}