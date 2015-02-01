package com.yrazlik.loltr.data;

public class Spell {

	private String name;
	private String sanitizedDescription;
	private ItemImage image;
	private String spellKey;
	
	public Spell(){
		
	}
	
	public Spell(String name, String sanitizedDescription, ItemImage image, String spellKey){
		this.name = name;
		this.sanitizedDescription = sanitizedDescription;
		this.image = image;
		this.spellKey = spellKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSanitizedDescription() {
		return sanitizedDescription;
	}

	public void setSanitizedDescription(String sanitizedDescription) {
		this.sanitizedDescription = sanitizedDescription;
	}

	public ItemImage getImage() {
		return image;
	}

	public void setImage(ItemImage image) {
		this.image = image;
	}

	public String getSpellKey() {
		if (spellKey == null) {
			return "";
		}
		return spellKey;
	}

	public void setSpellKey(String spellKey) {
		this.spellKey = spellKey;
	}

}
