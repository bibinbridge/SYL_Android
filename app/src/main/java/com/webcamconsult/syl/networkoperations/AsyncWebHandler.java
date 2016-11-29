package com.webcamconsult.syl.networkoperations;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpUriRequest;


public abstract class AsyncWebHandler 
{
	public abstract HttpUriRequest postHttpRequestMethod() throws UnsupportedEncodingException;
	 public abstract void onResponse(String result);
	 public void execute( String method){
		  new AsyncWebClient(this).execute(method);
		 
		
		  
//		  if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
//			    new AsyncWebClient(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,method);
//			} else {
//			    new AsyncWebClient(this).execute(method);
//			}
		  
		 }
	 
}
