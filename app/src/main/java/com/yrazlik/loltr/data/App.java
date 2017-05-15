package com.yrazlik.loltr.data;

/**
 * Created by yrazlik on 15/05/17.
 */

public class App {

    private String id;
    private String name;
    private String url;
    private String packageName;
    private String img;

    public App(String id, String name, String url, String packageName, String img) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.packageName = packageName;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
