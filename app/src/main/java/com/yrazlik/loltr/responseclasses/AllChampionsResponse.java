package com.yrazlik.loltr.responseclasses;

import java.util.Map;

public class AllChampionsResponse {
	private String type;
	private String version;
	private Map<String, Map<String, String>> data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Map<String, String>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, String>> data) {
		this.data = data;
	}
}
