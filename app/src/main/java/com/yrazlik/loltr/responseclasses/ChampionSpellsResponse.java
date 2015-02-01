package com.yrazlik.loltr.responseclasses;

import java.util.ArrayList;

import com.yrazlik.loltr.data.Passive;
import com.yrazlik.loltr.data.Spell;

public class ChampionSpellsResponse {
	
	private Passive passive;
	private ArrayList<Spell> spells;
	
	

	public Passive getPassive() {
		return passive;
	}

	public void setPassive(Passive passive) {
		this.passive = passive;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}
	
	

}
