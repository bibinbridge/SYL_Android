package com.webcamconsult.syl.networkoperations;

import org.apache.http.client.methods.HttpUriRequest;


public abstract class AsyncWebHandlerForGetApi
{
	public abstract HttpUriRequest getHttpRequestMethod();
	 public abstract void ongetResponse(String result);
	 public void execute(String ifGetRequest)
	 {
	  new AsyncWebClient(this).execute(ifGetRequest);

	 }

}
