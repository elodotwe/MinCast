package com.jacobarau.mincast

import com.jacobarau.mincast.sync.rss.RssParser
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class RssParserTest {
    @Test
    fun parseValidRSS() {
        var parser = RssParser()
        var istream = this.javaClass::class.java.getResourceAsStream("/cst.xml")

        var result = parser.parseRSS(istream)
    }
}
