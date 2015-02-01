package com.yrazlik.loltr.requestclasses;

import java.util.ArrayList;
import java.util.HashMap;

public class AllChampionsRequest extends Request{
	 
	private String region;
	private boolean freeToPlay;
	
	public AllChampionsRequest(String region, boolean freeToPlay){
		setPathParams();
		setQueryParams();
		this.region = region;
		this.freeToPlay = freeToPlay;
	}
	
	private void setQueryParams() {
		queryParams = new HashMap<String, String>();
		queryParams.put("freeToPlay", String.valueOf(freeToPlay));
		
	}

	private void setPathParams() {
		pathParams = new ArrayList<String>();
		pathParams.add(region);
	}

	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public boolean isFreeToPlay() {
		return freeToPlay;
	}
	public void setFreeToPlay(boolean freeToPlay) {
		this.freeToPlay = freeToPlay;
	}
	
	

}
