package com.fivemin.generator.domain.ruleVerify

import com.fivemin.generator.service.*
import com.fivemin.generator.domain.PageShouldUnique
import com.fivemin.generator.domain.RuleMatchedErrorException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
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



