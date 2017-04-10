package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 27/03/17.
 */

public class InfoDto {

    @SerializedName("difficulty")
    private int difficulty;
    @SerializedName("attack")
    private int attack;
    @SerializedName("defense")
    private int defense;
    @SerializedName("magic")
    private int magic;

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }
}
