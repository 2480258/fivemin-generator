package com.fivemin.generator.model.ruleVerify

import com.fivemin.generator.service.*
import com.fivemin.generator.model.PageShouldUnique
import com.fivemin.generator.model.RuleMatchedErrorException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.assertThrows

internal class PageShouldUniqueTest {

    val pageShouldUnique = PageShouldUnique()

    @Test
    fun checkAcceptability() {
        val option = mockk<JsonOptionFormat>()
        val parseOpt = mockk<JsonPrePostParserFormat>()

        val pageOptA = mockk<JsonParserPageFormat>()
        val pageOptB = mockk<JsonParserPageFormat>()

        every {
            pageOptA.pageName
        } returns ("page")

        every {
            pageOptB.pageName
        } returns ("page")

        every {
            option.parseFormat
        } returns(parseOpt)

        every {
            parseOpt.pages
        } returns(listOf(pageOptA, pageOptB))


        assertThrows<RuleMatchedErrorException> {
            pageShouldUnique.checkAcceptability(option)
        }
    }
}



