package com.yrazlik.loltr.responseclasses;

import com.yrazlik.loltr.parsers.YahooRSSFeedParser;

import java.util.ArrayList;

/**
 * Created by yrazlik on 10/05/16.
 */
public class YahooRSSFeedResponse {

    private ArrayList<YahooRSSFeedParser.FeedItem> feedItems;

    public ArrayList<YahooRSSFeedParser.FeedItem> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(ArrayList<YahooRSSFeedParser.FeedItem> feedItems) {
        this.feedItems = feedItems;
    }
}
