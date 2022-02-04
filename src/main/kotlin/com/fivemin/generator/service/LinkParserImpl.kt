package com.fivemin.generator.service


import arrow.core.Option
import arrow.core.Some
import arrow.core.filterOption
import arrow.core.none
import java.net.URI

class LinkParserImpl {
    private val allNavigator: ParserNavigator = ParserNavigator("*")
    private val linkNavigator: ParserNavigator = ParserNavigator("[href], [src]")

    private val HREF_ATTR = "href"
    private val SRC_ATTR = "src"

    private val REFERRERPOLICY_ATTR = "referrerpolicy"
    private val REL_ATTR = "rel"

    fun parse(
        parsable: HtmlParsable,
        host: URI,
        selector: Option<LinkSelector>
    ): Iterable<ParsedLink> {
        val suspects = selector.fold({
            parsable.getElements(allNavigator)
        }, {
            parsable.getElements(it.navigator)
        })

        val extractedLinks = suspects.map {
            getHrefAndSrc(it, host)
        }.flatten()

        var uniqueUris = extractedLinks.distinctBy {
            it.absoluteUri
        }.filter { x ->
            selector.fold({ true }) {
                it.regex.fold({ true }) {
                    it.containsMatchIn(x.absoluteUri.toString())
                }
            }
        }

        return uniqueUris
    }

    private fun getHrefAndSrc(
        elem: HtmlElement,
        host: URI
    ): Iterable<ParsedLink> {
        var tag = elem.getElements(linkNavigator)

        var links = tag.map {
            convert(host, it, HREF_ATTR)
        }.filterOption()

        var srcs = tag.map {
            convert(host, it, SRC_ATTR)
        }.filterOption()

        return links.plus(srcs).distinct()
    }

    private fun convert(
        host: URI,
        elem: HtmlElement,
        attr: String
    ): Option<ParsedLink> {
        return makeAbsoluteURI(host, elem, attr).map {
            ParsedLink(it, ReferrerInfo(elem.getAttribute(REFERRERPOLICY_ATTR), elem.getAttribute(REL_ATTR)))
        }
    }

    private fun makeAbsoluteURI(host: URI, elem: HtmlElement, attributeName: String): Option<URI> {
        val tag = elem.getAttribute(attributeName)

        return tag.fold({ none<URI>() }, {
            var uri = it

            if (uri.contains("#")) {
                uri = uri.split("#")[0]
            }

            if (uri.contains("://") && uri.contains("http")) {
                try {
                    return Some(URI(uri))
                } catch (e: Exception) {
                    return none<URI>()
                }
            }

            var temp: URI? = null
            var path: String? = null

            return try {
                temp = URI(uri)

                path = temp.path

                if (path.first() != '/') {
                    path = "/$path"
                }

                Some(URI(host.scheme, null, host.host, host.port, path, temp.query, null))
            } catch (e: Exception) {
                none<URI>()
            }
        })
    }
}

data class ParsedLink(val absoluteUri: URI, val referrerInfo: ReferrerInfo)

data class ReferrerInfo(val referrerPolicy: Option<String>, val rel: Option<String>)

data class LinkSelector(val navigator: ParserNavigator, val regex: Option<Regex>)

class ParserNavigator(val queryStr: String)
