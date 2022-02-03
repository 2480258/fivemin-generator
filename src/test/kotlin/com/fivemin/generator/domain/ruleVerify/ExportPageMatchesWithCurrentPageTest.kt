package com.fivemin.generator.domain.ruleVerify

import com.fivemin.core.initialize.json.*
import com.fivemin.generator.domain.ExportPageMatchesWithCurrentPage
import com.fivemin.generator.domain.RuleMatchedErrorException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

internal class ExportPageMatchesWithCurrentPageTest {

    val exportPageMatchesWithCurrentPage = ExportPageMatchesWithCurrentPage()

    @Test
    fun checkAcceptability() {
        val option = mockk<JsonOptionFormat>()
        val parseOpt = mockk<JsonPrePostParserFormat>()
        val page = mockk<JsonParserPageFormat>()

        every {
            option.parseFormat
        } returns(parseOpt)

        every {
            parseOpt.pages
        } returns(listOf(page))

        every {
            page.pageName
        } returns("testInput")

        val exportOpt = mockk<JsonExportParserFormat>()
        val exportPage = mockk<JsonExportPageFormat>()

        every {
            option.exportFormat
        } returns(exportOpt)

        every {
            exportOpt.pages
        } returns (listOf(exportPage))

        every {
            exportPage.pageName
        } returns("notMatch")


        assertThrows<RuleMatchedErrorException> {
            exportPageMatchesWithCurrentPage.checkAcceptability(option)
        }
    }
}