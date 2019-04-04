package com.jacobarau.mincast;

import com.jacobarau.mincast.sync.rss.RssParser;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowLog;

public class RssParserTest {
    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }

    @Test
    public void testParser() {
        RssParser parser = new RssParser();
        
    }
}
