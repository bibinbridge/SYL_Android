package com.webcamconsult.syl.networkoperations;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncWebClient extends AsyncTask<String, Void, String> 
{
	
	public String method;
	private AsyncWebHandler httpWebHandler;
	private AsyncWebHandlerForGetApi httpWebHandlerForGetApi;
	
	public AsyncWebClient(AsyncWebHandler httpWebHandler){
		this.httpWebHandler = httpWebHandler;	
	}
	
	
	public AsyncWebClient(AsyncWebHandlerForGetApi httpWebHandlerForGetApi) {
		this.httpWebHandlerForGetApi=httpWebHandlerForGetApi;
	}


	@Override
	protected String doInBackground(String... paramsStrings)
	{
		method=paramsStrings[0].toString();
		InputStream is = null;
		HttpResponse httpResponse;
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 60000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 60000*5;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		 HttpClient httpclient = new DefaultHttpClient(httpParameters);
		 
		 
		 try 
		 {
			
			 if(paramsStrings[0].equals("get"))
				{
					/** make the http request*/
					 httpResponse = httpclient.execute(httpWebHandlerForGetApi.getHttpRequestMethod());
					
				}	
				else {
	                 httpResponse = httpclient.execute(httpWebHandler.postHttpRequestMethod());
			
				}
		
		    HttpEntity entity = httpResponse.getEntity();
		    		    
		    
		    is = entity.getContent();
		    String contentAsString = getString(is);
	    Log.e("Response",contentAsString);
	        
	        return contentAsString;
	        
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 

		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Timeout exception happened";
		}

		 catch(Exception e)
		 {
			 e.printStackTrace();
			 
			 return null;
		 }
	}
	public static String getString(InputStream is) throws IOException
	 {
		 try {
			 Writer writer = new StringWriter();
			 char[] buffer = new char[1024];
			 try {
				 Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				 int n;
				 while ((n = reader.read(buffer)) != -1) {
					 writer.write(buffer, 0, n);
				 }
			 } finally {
				 is.close();
			 }

			 return writer.toString();
		 }
		 catch (Exception e){
			 return null;
		 }
	

	}
	
@Override
protected void onPostExecute(String s) {
		
		if(method.equals("post")){
			
			
			
			
		httpWebHandler.onResponse(s);
		}
		else  if(method.equals("get"))
		{
			httpWebHandlerForGetApi.ongetResponse(s);
		}
	
	
}

}
