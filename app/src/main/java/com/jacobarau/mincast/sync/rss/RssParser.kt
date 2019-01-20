package com.jacobarau.mincast.sync.rss

import android.text.TextUtils
import android.util.Log
import com.jacobarau.mincast.subscription.Item
import com.jacobarau.mincast.subscription.Subscription
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser
import android.util.Xml
import org.xmlpull.v1.XmlPullParserException


class RssParser {
    val TAG = "RssParser"

    class ParseResult(val subscription: Subscription,
                      val itemList: List<Item>)

    enum class ResultState {
        SUCCESS,
        NETWORK_ERROR,
        PARSE_ERROR
    }

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
    }

    private fun skipToTag(name: List<String>, parser: XmlPullParser) : Boolean {
        Log.i(TAG, "skipToTag(names: " + TextUtils.join(",", name) + ")")
        do {
            Log.i(TAG, "skipToTag: eventType is " + XmlPullParser.TYPES[parser.eventType])
            if (parser.eventType != XmlPullParser.START_TAG) {
                Log.i(TAG, "skipToTag: skipping event as it's not a START_TAG")
                parser.next()
                continue
            }

            Log.i(TAG, "Considering tag with name " + parser.name)
            if (name.contains(parser.name)) {
                Log.i(TAG, "Found tag we care about. Returning.")
                return true
            }

            try {
                Log.i(TAG, "We don't care about this tag; skipping it")
                skip(parser)
            } catch (e: IllegalStateException) {
                Log.e(TAG, "skip() was angry", e)
                return false
            }
        } while (parser.eventType != XmlPullParser.END_DOCUMENT)
        Log.i(TAG, "Reached end of document")
        return false
    }

    class ParseException(description: String) : Exception(description)

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
            var description: String? = null
            var items = ArrayList<Item>()
            // Tags at this level can be:
            // <title>
            // <link>
            // <description>
            // <image>
            // one or more <item> tags
            while (skipToTag(listOf("title", "link", "description"/*, "image"*/), parser)) {
                when (parser.name) {
                    "title" -> {
                        title = getText(parser, "<title>", title)
                    }
                    "link" -> {
                        //TODO: implement me
                    }
                    "description" -> {
                        description = getText(parser, "<description>", description)
                    }
                }
                parser.next()
            }

            val subscription = Subscription()
            if (title == null || description == null) {
                throw ParseException("Missing a required tag")
            }

            subscription.title = title
            subscription.description = description
            return ParseResult(subscription, items)
        }
    }

    private fun getText(parser: XmlPullParser, tagName: String, existingValue: String? = null) : String? {
        Log.i(TAG, "getText(tagName=$tagName, existing value=$existingValue)")
        if (existingValue != null) {
            throw ParseException("Found duplicate $tagName")
        }
        if (parser.next() == XmlPullParser.TEXT) {
            val result = parser.text
            Log.i(TAG, "Text found was '$result'")
            parser.nextTag()
            return result
        }
        throw ParseException("$tagName seems not to contain text")
    }

    /*
    var title: String? = null
    var link: String? = null
    var description: String? = null
    var imageUrl: String? = null
    var lastUpdated: Instant? = null
     */

//    private fun parseChannelTag(parser: XmlPullParser) : ParseResult {
//        parser.require(XmlPullParser.START_TAG, null, "rss")
//        var subscription = Subscription()
//        skipToTag(listOf("channel"))
//    }
}