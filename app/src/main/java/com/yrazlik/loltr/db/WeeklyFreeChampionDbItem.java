package com.yrazlik.loltr.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by yrazlik on 13/04/17.
 */

@Table(name = "WeeklyFreeChampions")
public class WeeklyFreeChampionDbItem extends Model{

    @Column(name = "imageUrl")
    public String imageUrl;
    @Column(name = "name")
    public String name;
    @Column(name = "dateInterval")
    public int dateInterval;
    @Column(name = "rpPrice")
    public String rpPrice;
    @Column(name = "ipPrice")
    public String ipPrice;

    public WeeklyFreeChampionDbItem() {
    }

    public WeeklyFreeChampionDbItem(String imageUrl, String name, int dateInterval, String rpPrice, String ipPrice) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.dateInterval = dateInterval;
        this.rpPrice = rpPrice;
        this.ipPrice = ipPrice;
    }
}
