package com.yrazlik.loltr.data;

/**
 * Created by yrazlik on 1/6/16.
 */
public class Statistics {

    private String name;
    private String value;
    private boolean isHeader;

    public Statistics(String name, String value){
        this.name = name;
        this.value = value;
    }

    public Statistics(String name, String value, boolean isHeader){
        this.name = name;
        this.value = value;
        this.isHeader = isHeader;
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

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }
}
