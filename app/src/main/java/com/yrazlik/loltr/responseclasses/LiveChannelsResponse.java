package com.yrazlik.loltr.responseclasses;

import com.yrazlik.loltr.data.Streams;

import java.util.ArrayList;

/**
 * Created by yrazlik on 1/10/15.
 */
public class LiveChannelsResponse {

    private ArrayList<Streams> streams;

    public ArrayList<Streams> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<Streams> streams) {
        this.streams = streams;
    }
}
