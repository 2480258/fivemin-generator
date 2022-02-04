package com.fivemin.generator.service

import arrow.core.Option
import arrow.core.none
import arrow.core.toOption
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.InputStreamReader


class HtmlDocumentFactory {
    fun create(html: String): HtmlParsable {
        return HtmlParsable(Jsoup.parse(html, ""))
    }

    fun create(html: InputStreamReader): HtmlParsable {
        return HtmlParsable(Jsoup.parse(html.readText()))
    }
}

class HtmlParsable (
    val doc: Document
) {

    fun getElements(nav: ParserNavigator): Iterable<HtmlElement> {
        return doc.select(nav.queryStr).map {
            HtmlElement(it)
        }
    }

    fun getElement(nav: ParserNavigator): Option<HtmlElement> {
        return doc.selectFirst(nav.queryStr).toOption().map {
            HtmlElement(it)
        }
    }
}

class HtmlElement(
    val elem: Element
)  {
    val outerHtml: String
        get() = elem.outerHtml()
    val innerHtml: String
        get() = elem.html()
    val textContent: String
        get() = elem.text()

    fun getAttribute(name: String): Option<String> {
        var ret = elem.attr(name)

        if (ret == "") {
            return none()
        }

        return ret.toOption()
    }

    fun getElements(nav: ParserNavigator): Iterable<HtmlElement> {
        return elem.select(nav.queryStr).map {
            HtmlElement(it)
        }
    }

    fun getElement(nav: ParserNavigator): Option<HtmlElement> {
        return elem.selectFirst(nav.queryStr).toOption().map {
            HtmlElement(it)
        }
    }
}
