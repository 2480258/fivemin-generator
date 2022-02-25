package com.fivemin.generator.model.ruleVerify

import com.fivemin.generator.service.*
import com.fivemin.generator.model.NoParsedPage
import com.fivemin.generator.model.RuleMatchedErrorException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

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