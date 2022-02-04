package com.fivemin.generator.domain.ruleVerify

import com.fivemin.generator.service.*
import com.fivemin.generator.domain.ExternalAttributeWithJsonParse
import com.fivemin.generator.domain.RuleMatchedErrorException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

internal class ExternalAttributeWithJsonParseTest {

    val exportAttributeWithJsonParse = ExternalAttributeWithJsonParse()

    @Test
    fun checkAcceptability() {
        val option = mockk<JsonOptionFormat>()
        val parseOpt = mockk<JsonPrePostParserFormat>()
        val page = mockk<JsonParserPageFormat>()
        val externalAttributeA = mockk<JsonParserLinkAttributeFormat>()


        every {
            option.parseFormat
        } returns (parseOpt)

        every {
            parseOpt.pages
        } returns (listOf(page))

        every {
            page.pageName
        } returns ("testInput")

        every {
            page.externalAttributes
        } returns (listOf(externalAttributeA))

        every {
            externalAttributeA.attributeName
        } returns ("testAttr")


        val exportOpt = mockk<JsonExportParserFormat>()
        val exportPage = mockk<JsonExportPageFormat>()
        val exportAdapter = mockk<JsonExportAdapterFormat>()

        every {
            option.exportFormat
        } returns (exportOpt)

        every {
            exportOpt.pages
        } returns (listOf(exportPage))

        every {
            exportPage.pageName
        } returns ("testInput")

        every {
            exportPage.targetAttributeName
        } returns (listOf("testAttr"))

        every {
            exportPage.adapter
        } returns (exportAdapter)

        every {
            exportAdapter.mode
        } returns ("Json")

        every {
            exportAdapter.JSON_ADAPTER
        } returns ("Json")


        assertThrows<RuleMatchedErrorException> {
            exportAttributeWithJsonParse.checkAcceptability(option)
        }
    }
}