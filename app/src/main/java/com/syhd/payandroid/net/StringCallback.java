package com.syhd.payandroid.net;


public abstract class StringCallback {
	public abstract void onError(String request);

    public abstract void onResponse(String response);
}
