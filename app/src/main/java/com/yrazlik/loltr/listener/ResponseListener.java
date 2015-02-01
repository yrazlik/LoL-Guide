package com.yrazlik.loltr.listener;

import android.content.Context;

public interface ResponseListener {
	
	public void onSuccess(Object response);
	public void onFailure(Object response);
	public Context getContext();

}
