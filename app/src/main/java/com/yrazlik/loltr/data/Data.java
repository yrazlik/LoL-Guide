package com.yrazlik.loltr.data;

import com.yrazlik.loltr.data.Gold;
import com.yrazlik.loltr.data.Image;

import java.util.ArrayList;

/**
 * Created by yrazlik on 8/6/15.
 */
public class Data {

    private ArrayList<String> tags;
    private  int id;
    private String sanitizedDescription;
    private String name;
    private Image image;
    private Gold gold;
    private ArrayList<String> from;
    private String group;
    private int depth;

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSanitizedDescription() {
        return sanitizedDescription;
    }

    public void setSanitizedDescription(String sanitizedDescription) {
        this.sanitizedDescription = sanitizedDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Gold getGold() {
        return gold;
    }

    public void setGold(Gold gold) {
        this.gold = gold;
    }

    public ArrayList<String> getFrom() {
        return from;
    }

    public void setFrom(ArrayList<String> from) {
        this.from = from;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
