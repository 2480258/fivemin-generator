package com.fivemin.generator.domain.ruleVerify

import com.fivemin.generator.service.*
import com.fivemin.generator.domain.NoExportPage
import com.fivemin.generator.domain.RuleMatchedErrorException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class NoExportPageTest {

    val noExportPage = NoExportPage()

    @Test
    fun checkAcceptability() {
        val option = mockk<JsonOptionFormat>()
        val exportOpt = mockk<JsonExportParserFormat>()


        every {
            option.exportFormat
        } returns(exportOpt)

        every {
            exportOpt.pages
        } returns (listOf())

        org.junit.jupiter.api.assertThrows<RuleMatchedErrorException> {
            noExportPage.checkAcceptability(option)
        }
    }
}