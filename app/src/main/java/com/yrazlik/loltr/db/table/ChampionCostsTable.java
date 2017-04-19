package com.yrazlik.loltr.db.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by yrazlik on 14/04/17.
 */

@Table(name = "ChampionCosts")
public class ChampionCostsTable extends Model{

    @Column(name = "champId")
    public String champId;
    @Column(name = "ip")
    public String ip;
    @Column(name = "rp")
    public String rp;

    public ChampionCostsTable() {
    }

    public ChampionCostsTable(String champId, String ip, String rp) {
        this.champId = champId;
        this.ip = ip;
        this.rp = rp;
    }
}
