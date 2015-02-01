package com.yrazlik.loltr.responseclasses;

import java.util.Map;

public class RuneResponse {
	private Map<String, Map<String, Object>> data;

	public Map<String, Map<String, Object>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, Object>> data) {
		this.data = data;
	}
}
