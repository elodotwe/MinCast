package com.jacobarau.mincast.sync.rss

import com.jacobarau.mincast.subscription.Item
import com.jacobarau.mincast.subscription.Subscription
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser
import android.util.Xml
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import java.lang.NumberFormatException


class RssParser {
    class ParseResult(val subscription: Subscription,
                      val itemList: List<Item>)

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
        parser.next()
    }

    private fun skipToTag(name: List<String>, parser: XmlPullParser) : Boolean {
        do {
            if (parser.eventType != XmlPullParser.START_TAG) {
                parser.next()
                continue
            }

            if (name.contains(parser.name)) {
                return true
            }

            try {
                skip(parser)
            } catch (e: IllegalStateException) {
                return false
            }
        } while (parser.eventType != XmlPullParser.END_DOCUMENT &&
                parser.eventType != XmlPullParser.END_TAG)
        return false
    }

    class ParseException : Exception {
        constructor(description: String) : super(description)
        constructor(description: String, throwable: Throwable) : super(description, throwable)
    }

    /**
     * @throws java.io.IOException if the given input stream fails for some reason
     * @throws ParseException if the given XML is malformed in any way
     */
    fun parseRSS(inputStream: InputStream) : ParseResult {
        inputStream.use { stream ->
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(stream, null)

            // Find the <rss> tag, then within it find the <channel> tag.
            // Return failure if we can't find either one.
            if (!skipToTag(listOf("rss"), parser)) {
                throw ParseException("Could not find <rss> tag")
            }

            parser.next()

            if (!skipToTag(listOf("channel"), parser)) {
                throw ParseException("Could not find <channel>")
            }
            parser.next()
            var title: String? = null
            var link: String? = null
            var description: String? = null
            var imgUrl: String? = null

            val items = ArrayList<Item>()
            // Tags at this level can be:
            // <title>
            // <link>
            // <description>
            // <image>
            // one or more <item> tags
            while (skipToTag(listOf("title", "link", "description", "image", "item"), parser)) {
                when (parser.name) {
                    "title" -> {
                        title = getText(parser, title)
                    }
                    "link" -> {
                        link = getText(parser, link)
                    }
                    "description" -> {
                        description = getText(parser, description)
                    }
                    "item" -> {
                        items.add(parseItem(parser))
                    }
                    "image" -> {
                        imgUrl = getImageURL(parser)
                    }
                }
            }

            if (title == null || description == null || link == null) {
                throw ParseException("Missing a required tag")
            }

            val subscription = Subscription()
            subscription.title = title
            subscription.description = description
            subscription.link = link
            subscription.imageUrl = imgUrl
            return ParseResult(subscription, items)
        }
    }

    private fun parseItem(parser: XmlPullParser) : Item {
        val item = Item()
        parser.next()
        while (skipToTag(listOf("title", "description", "pubDate", "enclosure"), parser)) {
            when (parser.name) {
                "title" -> {
                    item.title = getText(parser, item.title)
                }
                "description" -> {
                    item.description = getText(parser, item.description)
                }
                "pubDate" -> {
                    try {
                        item.publishDate = ZonedDateTime.parse(getText(parser, null), DateTimeFormatter.RFC_1123_DATE_TIME).toInstant()
                    } catch (e: DateTimeParseException) {
                        throw ParseException("pubDate format isn't RFC_1123 parseable", e)
                    }
                }
                "enclosure" -> {
                    try {
                        item.enclosureLengthBytes = parser.getAttributeValue(null, "length").toInt()
                    } catch (e: NumberFormatException) {
                        throw ParseException("enclosure length attribute should be a number but isn't", e)
                    }

                    item.enclosureMimeType = parser.getAttributeValue(null, "type")
                    item.enclosureUrl = parser.getAttributeValue(null, "url")

                    if (item.enclosureLengthBytes == null || item.enclosureMimeType == null || item.enclosureUrl == null) {
                        throw ParseException("missing at least one of the enclosure attributes (length, type, and url are all required)")
                    }
                }
            }
            parser.next()
        }
        return item
    }

    private fun getImageURL(parser: XmlPullParser) : String? {
        // We start on the <image> tag
        parser.require(XmlPullParser.START_TAG, null, "image")

        var url: String? = null

        // Index into the image tag--we'll look at its children.
        parser.next()

        // Calling skipToTag in a while loop like this ensures we'll exhaust all children of <image>.
        while (skipToTag(listOf("url"), parser)) {
            url = getText(parser, url)
        }

        // Index past the </image> node.
        parser.next()

        return url
    }

    private fun getText(parser: XmlPullParser, existingValue: String? = null) : String {
        parser.require(XmlPullParser.START_TAG, null, null)

        if (existingValue != null) {
            throw ParseException("Found duplicate tag, existing value is $existingValue")
        }
        if (parser.next() == XmlPullParser.TEXT) {
            val result = parser.text
            parser.next() // We expect an END_TAG here.
            parser.next() // Skip the END_TAG into the next event for the next guy to use.
            return result.trim()
        }
        throw ParseException("tag seems not to contain text")
    }
}