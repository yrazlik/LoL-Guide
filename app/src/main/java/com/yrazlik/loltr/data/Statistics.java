package com.yrazlik.loltr.data;

/**
 * Created by yrazlik on 1/6/16.
 */
public class Statistics {

    private String name;
    private String value;

    public Statistics(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
