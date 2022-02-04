package com.fivemin.generator.domain.ruleVerify

import com.fivemin.generator.service.*
import com.fivemin.generator.domain.RuleMatchedErrorException
import com.fivemin.generator.domain.TagNameNotMatchWithFileExpression
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

internal class TagNameNotMatchWithFileExpressionTest {

    val tagNameNotMatchWithFileExpression = TagNameNotMatchWithFileExpression()

    @Test
    fun checkAcceptability() {
        val option = mockk<JsonOptionFormat>()
        val exportPage = mockk<JsonExportPageFormat>()
        val exportOpt = mockk<JsonExportParserFormat>()
        val exportAdapter = mockk<JsonExportAdapterFormat>()


        every {
            option.exportFormat
        } returns(exportOpt)

        every {
            exportOpt.pages
        } returns (listOf(exportPage))

        every {
            exportPage.adapter
        } returns(exportAdapter)

        every {
            exportPage.pageName
        } returns("")

        every {
            exportAdapter.fileNameTagExp
        } returns ("test")

        assertThrows<RuleMatchedErrorException> {
            tagNameNotMatchWithFileExpression.checkAcceptability(option)
        }
    }
}