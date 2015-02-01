package com.yrazlik.loltr.responseclasses;

import java.util.Map;

public class AllItemsResponse {

	private Map<String, Map<String, String>> data;

	public Map<String, Map<String, String>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, String>> data) {
		this.data = data;
	}
	
	
	
}
