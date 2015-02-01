package com.yrazlik.loltr.requestclasses;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.yrazlik.loltr.listener.ResponseListener;

public class Request {
	
	public static final int WEEKLY_FREE_CHAMPIONS_REQUEST = 1;
	
	protected int requestID;
	protected ArrayList<String> pathParams;
	protected HashMap<String, String> queryParams;
	protected boolean isCancelled;
	protected ResponseListener listener;
	private String data = "";
	
	public Request(){
		
	}
	
	public String getQueryParametersString() {

		StringBuilder params = new StringBuilder("");

		if (queryParams != null && queryParams.size() != 0) {

			for (int i = 0; i < queryParams.keySet().size(); i++) {

				if (i == 0) {
					params.append("?");
				}

				String key = (String) queryParams.keySet().toArray()[i];

				if (queryParams.get(key) != null) {
					try {
						params.append(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(queryParams.get(key), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
					}
				}

				if (i + 1 < queryParams.keySet().size()) {
					params.append("&");
				}
			}
		}

		return params.toString();
	}
	
	public static String join(ArrayList<String> strings, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strings.size(); i++) {
			if (i != 0) {
				sb.append(separator);
			}
			sb.append(strings.get(i));
		}
		return sb.toString();
	}
	
	public String getPathParametersString() {
		StringBuilder params = new StringBuilder("");

		if (pathParams != null && pathParams.size() != 0) {

			params.append("/");
			params.append(join(pathParams, "/"));

		}
		return params.toString();
	}
	
	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public Request(int requestID, ArrayList<String> pathParams, HashMap<String, String> queryParams){
		this.requestID = requestID;
		this.pathParams = pathParams;
		this.queryParams = queryParams;
	}
	
	
	public ResponseListener getListener() {
		return listener;
	}

	public void setListener(ResponseListener listener) {
		this.listener = listener;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public int getRequestID() {
		return requestID;
	}
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	public ArrayList<String> getPathParams() {
		return pathParams;
	}
	public void setPathParams(ArrayList<String> pathParams) {
		this.pathParams = pathParams;
	}
	public HashMap<String, String> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(HashMap<String, String> queryParams) {
		this.queryParams = queryParams;
	}
	
	

}
