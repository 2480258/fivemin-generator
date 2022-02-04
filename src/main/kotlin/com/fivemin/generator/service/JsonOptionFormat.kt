package com.fivemin.generator.service

@kotlinx.serialization.Serializable
data class JsonOptionFormat(
    val requestFormat: JsonRequesterCompFormat,
    val parseFormat: JsonPrePostParserFormat,
    val exportFormat: JsonExportParserFormat
)

@kotlinx.serialization.Serializable
data class JsonRequesterCompFormat(
    val engines: List<JsonRequesterEngineFormat>,
    val cookiePolicies: List<JsonRequesterCookieSyncFormat>
)

@kotlinx.serialization.Serializable
class JsonRequesterCookieSyncFormat(
    val syncDest: JsonRequesterIndex,
    val syncSrc: JsonRequesterIndex
)

@kotlinx.serialization.Serializable
class JsonRequesterIndex(
    val engine: String,
    val index: Int?
)

@kotlinx.serialization.Serializable
class JsonRequesterEngineFormat(
    val requesterEngineName: String,
    val type: String,
    val requesters: List<JsonRequesterFormat>
)

@kotlinx.serialization.Serializable
class JsonRequesterFormat(
    val userAgent: String,
    val key: String = "Default"
)


@kotlinx.serialization.Serializable
data class JsonPrePostParserFormat(
    val bookName: String,
    val globalCondition: JsonPageConditionFormat,
    val pages: List<JsonParserPageFormat>,
    val attributeRequester: JsonParseRequesterFormat
)

@kotlinx.serialization.Serializable
class JsonParserPageFormat(
    val pageName: String,
    val condition: JsonPageConditionFormat,
    val internalAttributes: List<JsonParserInternalAttributeFormat> = listOf(),
    val linkAttributes: List<JsonParserLinkAttributeFormat> = listOf(),
    val externalAttributes: List<JsonParserLinkAttributeFormat> = listOf(),
    val targetContainer: JsonParserContainerFormat,
    val tag: List<JsonParserPageTagFormat> = listOf(),
    val targetRequesterEngine: JsonParseRequesterFormat
)

@kotlinx.serialization.Serializable
data class JsonParseRequesterFormat(val targetRequester: String)

@kotlinx.serialization.Serializable
data class JsonParserPageTagFormat(
    val name: String,
    val tagRegex: String,
    val isAlias: Boolean
)

@kotlinx.serialization.Serializable
data class JsonParserContainerFormat(val workingSetMode: String)

@kotlinx.serialization.Serializable
data class JsonParserLinkAttributeFormat(
    val attributeName: String,
    val uriRegex: String? = null,
    val queryStr: String,
    val destPage: String? = null
)

@kotlinx.serialization.Serializable
data class JsonParserInternalAttributeFormat(
    val attributeName: String,
    val queryStr: String,
    var parseMode: String
)

@kotlinx.serialization.Serializable
data class JsonPageConditionFormat(
    val uriRegex: String
)


@kotlinx.serialization.Serializable
data class JsonExportParserFormat(
    val bookName: String,
    val pages: List<JsonExportPageFormat>
)

@kotlinx.serialization.Serializable
data class JsonExportPageFormat(
    val pageName: String,
    val targetAttributeName: List<String>,
    val adapter: JsonExportAdapterFormat
)

@kotlinx.serialization.Serializable
data class JsonExportAdapterFormat(
    val mode: String,
    val fileNameTagExp: String
) {
    val JSON_ADAPTER = "Json"
    val BIN_ADAPTER = "Binary"
}
