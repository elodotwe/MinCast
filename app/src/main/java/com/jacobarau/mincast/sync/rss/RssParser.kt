package com.jacobarau.mincast.sync.rss

import com.jacobarau.mincast.subscription.Item
import com.jacobarau.mincast.subscription.Subscription
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser
import android.util.Xml
import org.xmlpull.v1.XmlPullParserException


class RssParser {
    class ParseResult(resultState: ResultState, subscription: Subscription?, itemList: List<Item>?)

    enum class ResultState {
        SUCCESS,
        NETWORK_ERROR,
        PARSE_ERROR
    }

    fun parseRSS(inputStream: InputStream) : ParseResult {
        inputStream.use { stream ->
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(stream, null)
            parser.require(XmlPullParser.START_TAG, null, "rss")
        }

        return ParseResult(ResultState.PARSE_ERROR, null, null)
    }
}