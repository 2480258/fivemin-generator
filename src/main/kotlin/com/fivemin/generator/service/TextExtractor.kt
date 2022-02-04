package com.fivemin.generator.service

class TextExtractor {
    fun parse(data: HtmlParsable, nav: ParserNavigator, mode: TextSelectionMode): Iterable<String> {
        var ret = data.getElements(nav)

        return ret.map {
            selectMode(it, mode)
        }
    }

    private fun selectMode(elem: HtmlElement, mode: TextSelectionMode): String {
        return when (mode) {
            TextSelectionMode.TEXT_CONTENT -> elem.textContent
            TextSelectionMode.OUTER_HTML -> elem.outerHtml
            TextSelectionMode.INNER_HTML -> elem.innerHtml
        }
    }
}

enum class TextSelectionMode {
    INNER_HTML, OUTER_HTML, TEXT_CONTENT
}