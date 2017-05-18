package com.yrazlik.loltr.db.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by yrazlik on 13/04/17.
 */

@Table(name = "AllChampions")
public class AllChampionsTable extends Model{

    @Column(name = "imageUrl")
    public String imageUrl;
    @Column(name = "name")
    public String name;
    @Column(name = "champId")
    public int champId;
    @Column(name = "key")
    public String key;
    @Column(name = "title")
    public String title;

    public AllChampionsTable() {
    }

    public AllChampionsTable(String imageUrl, String name, int champId, String key, String title) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.champId = champId;
        this.key = key;
        this.title = title;
    }
}