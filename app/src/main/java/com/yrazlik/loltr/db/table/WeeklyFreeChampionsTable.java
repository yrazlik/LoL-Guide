package com.yrazlik.loltr.db.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by yrazlik on 13/04/17.
 */

@Table(name = "WeeklyFreeChampions")
public class WeeklyFreeChampionsTable extends Model{

    @Column(name = "imageUrl")
    public String imageUrl;
    @Column(name = "name")
    public String name;
    @Column(name = "dateInterval")
    public String dateInterval;
    @Column(name = "rpPrice")
    public String rpPrice;
    @Column(name = "ipPrice")
    public String ipPrice;
    @Column(name = "champId")
    public int champId;

    public WeeklyFreeChampionsTable() {
    }

    public WeeklyFreeChampionsTable(int champId, String imageUrl, String name, String dateInterval, String rpPrice, String ipPrice) {
        this.champId = champId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.dateInterval = dateInterval;
        this.rpPrice = rpPrice;
        this.ipPrice = ipPrice;
    }
}
