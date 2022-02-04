package com.fivemin.generator.domain.ruleVerify

import com.fivemin.generator.service.*
import com.fivemin.generator.domain.ExportTargettedAttributeMatches
import com.fivemin.generator.domain.RuleMatchedErrorException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

internal class ExportTargettedAttributeMatchesTest {

    val exportTargettedAttributeMatches = ExportTargettedAttributeMatches()

    @Test
    fun checkAcceptability() {
        val option = mockk<JsonOptionFormat>()
        val parseOpt = mockk<JsonPrePostParserFormat>()
        val pageA = mockk<JsonParserPageFormat>()
        val exportPage = mockk<JsonExportPageFormat>()
        val exportOpt = mockk<JsonExportParserFormat>()

        every {
            option.parseFormat
        } returns (parseOpt)

        every {
            parseOpt.pages
        } returns (listOf(pageA))

        every {
            pageA.pageName
        } returns ("testInputA")

        every {
            pageA.internalAttributes
        } returns (listOf())

        every {
            pageA.externalAttributes
        } returns(listOf())

        every {
            pageA.linkAttributes
        } returns(listOf())

        every {
            option.exportFormat
        } returns(exportOpt)

        every {
            exportOpt.pages
        } returns (listOf(exportPage))

        every {
            exportPage.pageName
        } returns("")

        every {
            exportPage.targetAttributeName
        } returns(listOf("none"))

        assertThrows<RuleMatchedErrorException> {
            exportTargettedAttributeMatches.checkAcceptability(option)
        }
    }
}