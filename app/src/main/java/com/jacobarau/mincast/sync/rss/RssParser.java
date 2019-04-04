package com.jacobarau.mincast.sync.rss;

import android.util.Xml;

import com.jacobarau.mincast.subscription.Subscription;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class RssParser {
    public ParseResult parseRSS(InputStream inputStream, String encoding) throws XmlPullParserException, IOException, ParseException {
        ParseResult result = new ParseResult();
        Subscription subscription = new Subscription();
        result.subscription = subscription;
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, encoding);
        // Skip the START_DOCUMENT event
        parser.next();

        skipInto("rss", parser);
        skipInto("channel", parser);

        do {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case "title":
                        subscription.setTitle(processTextTag(parser));
                        break;
                    case "link":
                        subscription.setLink(processTextTag(parser));
                        break;
                    case "description":
                        subscription.setDescription(processTextTag(parser));
                        break;
                    case "image":
                        skipTag(parser);
                        break;
                    case "item":
                        skipTag(parser);
                        break;
                    default:
                        skipTag(parser);
                }
            }
        } while (parser.getEventType() != XmlPullParser.END_DOCUMENT &&
                 parser.getEventType() != XmlPullParser.END_TAG);


        return result;
    }

    private void skipInto(String tag, XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
            throw new ParseException("End of document not expected but reached");
        }

        boolean foundTag = false;
        do {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (!parser.getName().equals(tag)) {
                    skipTag(parser);
                } else {
                    foundTag = true;
                    // Go into the child nodes of the tag.
                    parser.next();
                }
            } else {
                // Consumes things like text nodes and such.
                parser.next();
            }
        } while (parser.getEventType() != XmlPullParser.END_DOCUMENT
                && parser.getEventType() != XmlPullParser.END_TAG
                && !foundTag);

        if (!foundTag) {
            throw new ParseException("No <" + tag + "> tag found");
        }
    }

    private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
            }
        }
        parser.next();
    }

    private String processTextTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        parser.next();
        if (parser.getEventType() != XmlPullParser.TEXT) {
            //TODO: act more gracefully when we hit tags we don't expect. Maybe gather all
            //TEXT nodes from the children of the node we started at. Can't be arsed just now.
            skipTag(parser);
            return "";
        }
        String result = parser.getText();
        skipTag(parser);
        return result;
    }
}
