package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;

public class SYLChangePasswordModelManager {
public void doPasswordChange()
{
	
	new AsyncWebHandler() {
		
		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			try {
			
				// TODO Auto-generated method stub
	

				// httppost.setEntity(new
				// UrlEncodedFormEntity(nameValuePairs));
			} 
	 catch (Exception e) {
				Log.e("Error", "Error");
			}

			return null;

		}

		@Override
		public void onResponse(String result) {
			// TODO Auto-generated method stub

			try {
				Log.e("signinresult", result);

			} catch (Exception e) {
			
			}
	
			
		}

	}.execute("post");












}
}
