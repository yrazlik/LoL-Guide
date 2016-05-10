package com.yrazlik.loltr.parsers;

import android.util.Xml;

import com.yrazlik.loltr.responseclasses.YahooRSSFeedResponse;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by yrazlik on 10/05/16.
 */
public class YahooRSSFeedParser {

    private static final String ns = null;

    public YahooRSSFeedResponse parse(String response) throws IOException {
        InputStream in = null;
        try{
            in = new ByteArrayInputStream(response.getBytes("UTF-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, "UTF-8");
            parser.nextTag();
            return readFeed(parser);
        }catch (Exception e){
            return null;
        }finally {
            if(in != null) {
                in.close();
            }
        }
    }

    private YahooRSSFeedResponse readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        YahooRSSFeedResponse response = new YahooRSSFeedResponse();
        ArrayList<FeedItem> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equalsIgnoreCase("channel")){
                continue;
            }else if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        response.setFeedItems(items);
        return response;
    }

    private FeedItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String dc_creator = null;
        String description = null;
        String dc_type = null;
        String pubDate = null;
        String link = null;
        String content_encoded = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equalsIgnoreCase("title")) {
                title = readTitle(parser);
            } else if (name.equalsIgnoreCase("dc:creator")) {
                dc_creator = readDcCreator(parser);
            } else if (name.equalsIgnoreCase("description")) {
                description = readDescription(parser);
            } else if (name.equalsIgnoreCase("dc:type")) {
                dc_type = readDcType(parser);
            } else if (name.equalsIgnoreCase("pubDate")) {
                pubDate = readPubDate(parser);
            } else if (name.equalsIgnoreCase("link")) {
                link = readLink(parser);
            } else if (name.equalsIgnoreCase("content:encoded")) {
                content_encoded = readContentEncoded(parser);
            }
            else {
                skip(parser);
            }
        }
        return new FeedItem(title, dc_creator, description, dc_type, pubDate, link, content_encoded);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private String readDcCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "dc:creator");
        String dcCreator = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "dc:creator");
        return dcCreator;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readDcType(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "dc:type");
        String dcType = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "dc:type");
        return dcType;
    }

    private String readContentEncoded(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "content:encoded");
        String contentEncoded = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "content:encoded");
        return contentEncoded;
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }



    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public static class FeedItem {
        public final String title;
        private String dc_creator;
        private String description;
        private String dc_type;
        private String pubDate;
        private String link;
        private String content_encoded;

        private FeedItem(String title, String dc_creator, String description, String dc_type, String pubDate, String link, String content_encoded) {
            this.title = title;
            this.dc_creator = dc_creator;
            this.description = description;
            this.dc_type = dc_type;
            this.pubDate = pubDate;
            this.link = link;
            this.content_encoded = content_encoded;
        }
    }

}
