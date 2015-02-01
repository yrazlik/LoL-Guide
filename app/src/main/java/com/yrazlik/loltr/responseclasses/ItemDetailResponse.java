package com.yrazlik.loltr.responseclasses;

import com.yrazlik.loltr.data.Gold;

public class ItemDetailResponse {

	private int id;
	private String plaintext, description, name, group; 
	private Gold gold;
	private String sanitizedDescription;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlaintext() {
		return plaintext;
	}
	public void setPlaintext(String plaintext) {
		this.plaintext = plaintext;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Gold getGold() {
		return gold;
	}
	public void setGold(Gold gold) {
		this.gold = gold;
	}
	public String getSanitizedDescription() {
		return sanitizedDescription;
	}
	public void setSanitizedDescription(String sanitizedDescription) {
		this.sanitizedDescription = sanitizedDescription;
	}
	
	
	
}
