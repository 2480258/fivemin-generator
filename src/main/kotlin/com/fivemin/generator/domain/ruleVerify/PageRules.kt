package com.fivemin.generator.domain

import com.fivemin.generator.service.JsonOptionFormat

fun <T> Iterable<T>.extractNotUnique(): Map<T, Int> {
    return this.groupBy { it }.filter {
        it.value.count() > 1
    }.filter {
        it.value.any()
    }.map {
        Pair(it.key, it.value.count())
    }.toMap()
}


class PageShouldUnique : JsonRule {
    companion object : JsonRuleKey {
        private val keyString = "PageShouldUniqueRule"

        override val key: RuleCheckKey = RuleCheckKey(keyString)
        override val requires: Iterable<RuleCheckKey> = listOf()
    }

    override fun checkAcceptability(format: JsonOptionFormat) {
        val notUniqueNames = format.parseFormat.pages.map {
            it.pageName
        }.extractNotUnique()

        if (notUniqueNames.any()) {
            throw RuleMatchedErrorException("Duplicated page name: " + notUniqueNames.keys.fold("") { f, s ->
                "$f, $s"
            } + "\nMake sure to use different page name.")
        }
    }

    override val key: RuleCheckKey
        get() = Companion.key
    override val requires: Iterable<RuleCheckKey>
        get() = Companion.requires
}

class ExportPageMatchesWithCurrentPage : JsonRule {
    companion object : JsonRuleKey {
        private val keyString = "ExportPageMatchesWithCurrentPage"

        override val key: RuleCheckKey = RuleCheckKey(keyString)
        override val requires: Iterable<RuleCheckKey> = listOf()
    }

    override fun checkAcceptability(format: JsonOptionFormat) {
        val exportPages = format.exportFormat.pages.map {
            it.pageName
        }

        val parsePages = format.parseFormat.pages.map {
            it.pageName
        }

        val notUsedExports = exportPages.filter {
            !parsePages.contains(it)
        }

        if (notUsedExports.any()) {
            throw RuleMatchedErrorException("Not used export pages -> " + notUsedExports.fold("") { f, s ->
                "$f, $s"
            } + "\nMake sure to point valid page name. ")
        }
    }


    override val key: RuleCheckKey
        get() = PageShouldUnique.key
    override val requires: Iterable<RuleCheckKey>
        get() = PageShouldUnique.requires
}

class AttributeNameShouldUnique : JsonRule {
    companion object : JsonRuleKey {
        private val keyString = "AttributeNameShouldUnique"

        override val key: RuleCheckKey = RuleCheckKey(keyString)
        override val requires: Iterable<RuleCheckKey> = listOf()
    }

    override fun checkAcceptability(format: JsonOptionFormat) {
        val attributeMap = format.parseFormat.pages.associate {
            val attributes = it.internalAttributes.map {
                it.attributeName
            }.plus(it.externalAttributes.map {
                it.attributeName
            }).plus(it.linkAttributes.map {
                it.attributeName
            })

            Pair(it.pageName, attributes)
        }

        val duplicated_page_attribute = attributeMap.map {
            Pair(it.key, it.value.extractNotUnique())
        }.filter {
            it.second.any()
        }

        if (duplicated_page_attribute.isNotEmpty()) {
            val message = duplicated_page_attribute.map {
                "Page: " + it.first + ", attributes" + it.second.keys.fold("") { f, s ->
                    "$f, $s"
                }
            }.fold("") { f, s ->
                "$f\n$s"
            }

            throw RuleMatchedErrorException("Duplicated attributes -> \n$message")
        }
    }

    override val key: RuleCheckKey
        get() = PageShouldUnique.key
    override val requires: Iterable<RuleCheckKey>
        get() = PageShouldUnique.requires
}


class ExportTargettedAttributeMatches : JsonRule {
    companion object : JsonRuleKey {
        private val keyString = "ExportTargettedAttributeMatches"

        override val key: RuleCheckKey = RuleCheckKey(keyString)
        override val requires: Iterable<RuleCheckKey> = listOf(PageShouldUnique.key, AttributeNameShouldUnique.key)
    }

    override fun checkAcceptability(format: JsonOptionFormat) {
        val exportPages = format.exportFormat.pages

        val attributeMap = format.parseFormat.pages.associate {
            val attributes = it.internalAttributes.map {
                it.attributeName
            }.plus(it.externalAttributes.map {
                it.attributeName
            }).plus(it.linkAttributes.map {
                it.attributeName
            })

            Pair(it.pageName, attributes)
        }

        val invalidAttributes = exportPages.associate { page ->
            Pair(page.pageName, page.targetAttributeName.filter {
                !(attributeMap[page.pageName]?.contains(it) ?: false)
            })
        }

        val results = invalidAttributes.filter {
            it.value.any()
        }

        if (results.isNotEmpty()) {
            val message = results.map {
                "Export Page: " + it.key + ", targetted attributes" + it.value.fold("") { f, s ->
                    "$f, $s"
                }
            }.fold("") { f, s ->
                "$f\n$s"
            }

            throw RuleMatchedErrorException("Invalid target attributes -> \n$message")
        }
    }

    override val key: RuleCheckKey
        get() = PageShouldUnique.key
    override val requires: Iterable<RuleCheckKey>
        get() = PageShouldUnique.requires
}

class ExternalAttributeWithJsonParse : JsonRule {
    companion object : JsonRuleKey {
        private val keyString = "ExternalAttributeWithJsonParse"

        override val key: RuleCheckKey = RuleCheckKey(keyString)
        override val requires: Iterable<RuleCheckKey> = listOf(PageShouldUnique.key, AttributeNameShouldUnique.key)
    }

    override fun checkAcceptability(format: JsonOptionFormat) {
        val external = format.parseFormat.pages.map {
            Pair(it.pageName, it.externalAttributes.map {
                it.attributeName
            })
        }

        val exportWith = format.exportFormat.pages.filter {
            it.adapter.mode == it.adapter.JSON_ADAPTER
        }.associate {
            Pair(it.pageName, it.targetAttributeName)
        }

        val externalWithJson = external.associate { page ->
            Pair(page.first, page.second.filter {
                exportWith[page.first]!!.contains(it)
            })
        }.filter {
            it.value.any()
        }

        if (externalWithJson.any()) {
            throw RuleMatchedErrorException("Exterenal attribute attached to json: " + externalWithJson.keys.fold("") { f, s ->
                "$f, $s"
            })
        }
    }

    override val key: RuleCheckKey
        get() = PageShouldUnique.key
    override val requires: Iterable<RuleCheckKey>
        get() = PageShouldUnique.requires
}

class TagNameNotMatchWithFileExpression : JsonRule {
    companion object : JsonRuleKey {
        private val keyString = "TagNameNotMatchWithFileExpression"

        override val key: RuleCheckKey = RuleCheckKey(keyString)
        override val requires: Iterable<RuleCheckKey> = listOf(PageShouldUnique.key, AttributeNameShouldUnique.key)
    }
    override fun checkAcceptability(format: JsonOptionFormat) {
        val tag = format.exportFormat.pages.filter {
            !it.adapter.fileNameTagExp.contains("&(")
        }.map {
            it.pageName
        }

        if(tag.any()) {
            throw RuleMatchedErrorException("File expression does not contain any tags! Files will have fixed name and may duplicate: " + tag.fold("") { f, s ->
                "$f, $s"
            })
        }
    }

    override val key: RuleCheckKey
        get() = PageShouldUnique.key
    override val requires: Iterable<RuleCheckKey>
        get() = PageShouldUnique.requires
}

class NoExportPage: JsonRule {
    companion object : JsonRuleKey {
        private val keyString = "TagNameNotMatchWithFileExpression"

        override val key: RuleCheckKey = RuleCheckKey(keyString)
        override val requires: Iterable<RuleCheckKey> = listOf(PageShouldUnique.key, AttributeNameShouldUnique.key)
    }

    override fun checkAcceptability(format: JsonOptionFormat) {
        if(!format.exportFormat.pages.any()) {
            throw RuleMatchedErrorException("There's no export page! This setting will not generate output!")
        }
    }

    override val key: RuleCheckKey
        get() = PageShouldUnique.key
    override val requires: Iterable<RuleCheckKey>
        get() = PageShouldUnique.requires
}

class NoParsedPage: JsonRule, JsonRuleKey {
    companion object : JsonRuleKey {
        private val keyString = "NoParsedPage"

        override val key: RuleCheckKey = RuleCheckKey(keyString)
        override val requires: Iterable<RuleCheckKey> = listOf(PageShouldUnique.key, AttributeNameShouldUnique.key)
    }

    override fun checkAcceptability(format: JsonOptionFormat) {
        if(!format.parseFormat.pages.any()) {
            throw RuleMatchedErrorException("There's no parse page! This setting will not generate output!")
        }
    }

    override val key: RuleCheckKey
        get() = PageShouldUnique.key
    override val requires: Iterable<RuleCheckKey>
        get() = PageShouldUnique.requires
}

