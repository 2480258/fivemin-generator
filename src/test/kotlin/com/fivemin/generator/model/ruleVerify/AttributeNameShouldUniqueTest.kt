package com.fivemin.generator.model.ruleVerify


import com.fivemin.generator.service.JsonOptionFormat

import com.fivemin.generator.model.AttributeNameShouldUnique
import com.fivemin.generator.model.RuleMatchedErrorException
import com.fivemin.generator.service.JsonParserInternalAttributeFormat
import com.fivemin.generator.service.JsonParserPageFormat
import com.fivemin.generator.service.JsonPrePostParserFormat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AttributeNameShouldUniqueTest {

    val attributeNameShouldUnique = AttributeNameShouldUnique()


    @Test
    fun checkAcceptabilityNonThrows() {
        val option = mockk<JsonOptionFormat>()
        val parseOpt = mockk<JsonPrePostParserFormat>()
        val pageA = mockk<JsonParserPageFormat>()
        val pageB = mockk<JsonParserPageFormat>()

        val attributeNameA = mockk<JsonParserInternalAttributeFormat>()
        val attributeNameB = mockk<JsonParserInternalAttributeFormat>()

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
        } returns (listOf(attributeNameA, attributeNameB))

        every {
            pageA.externalAttributes
        } returns(listOf())

        every {
            pageA.linkAttributes
        } returns(listOf())

        every {
            pageB.internalAttributes
        } returns (listOf(attributeNameA, attributeNameB))

        every {
            pageB.externalAttributes
        } returns (listOf())

        every {
            pageB.linkAttributes
        } returns (listOf())

        every {
            pageB.pageName
        } returns ("testInputB")

        every {
            attributeNameA.attributeName
        } returns ("attrA")

        every {
            attributeNameB.attributeName
        } returns ("attrB")

        attributeNameShouldUnique.checkAcceptability(option)
    }

    @Test
    fun checkAcceptabilityThrows() {
        val option = mockk<JsonOptionFormat>()
        val parseOpt = mockk<JsonPrePostParserFormat>()
        val pageA = mockk<JsonParserPageFormat>()
        val pageB = mockk<JsonParserPageFormat>()

        val attributeNameA = mockk<JsonParserInternalAttributeFormat>()
        val attributeNameB = mockk<JsonParserInternalAttributeFormat>()

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
        } returns (listOf(attributeNameA, attributeNameA))

        every {
            pageA.externalAttributes
        } returns(listOf())

        every {
            pageA.linkAttributes
        } returns(listOf())

        every {
            pageB.internalAttributes
        } returns (listOf(attributeNameA, attributeNameB))

        every {
            pageB.externalAttributes
        } returns (listOf())

        every {
            pageB.linkAttributes
        } returns (listOf())

        every {
            pageB.pageName
        } returns ("testInputB")

        every {
            attributeNameA.attributeName
        } returns ("attrA")

        every {
            attributeNameB.attributeName
        } returns ("attrB")

        assertThrows<RuleMatchedErrorException> {
            attributeNameShouldUnique.checkAcceptability(option)
        }
    }
}