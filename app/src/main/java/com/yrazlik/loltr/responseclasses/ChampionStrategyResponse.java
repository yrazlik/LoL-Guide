package com.yrazlik.loltr.responseclasses;

import java.util.ArrayList;

public class ChampionStrategyResponse {
	
	private ArrayList<String> enemytips;
	private ArrayList<String> allytips;
	private String key;
	
	public ArrayList<String> getEnemytips() {
		return enemytips;
	}
	public void setEnemytips(ArrayList<String> enemytips) {
		this.enemytips = enemytips;
	}
	public ArrayList<String> getAllytips() {
		return allytips;
	}
	public void setAllytips(ArrayList<String> allytips) {
		this.allytips = allytips;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	

}
