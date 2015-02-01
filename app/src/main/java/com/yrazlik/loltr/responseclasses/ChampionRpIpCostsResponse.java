package com.yrazlik.loltr.responseclasses;

import java.util.Map;

public class ChampionRpIpCostsResponse {
	
	private Map<String, Map<String, String>> costs;

	public Map<String, Map<String, String>> getCosts() {
		return costs;
	}

	public void setCosts(Map<String, Map<String, String>> costs) {
		this.costs = costs;
	}
	
	
}
