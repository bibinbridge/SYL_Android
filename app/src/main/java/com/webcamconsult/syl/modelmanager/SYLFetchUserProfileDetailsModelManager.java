package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLFetchUserDetailsListener;
import com.webcamconsult.syl.model.SYLFetchUserDetailsResponseModel;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLFetchUserProfileDetailsModelManager {
String accessToken,timezone;
public SYLFetchUserDetailsListener mFetchUserdetailsListener;
	
	public void getUserDetails(String accessToken,String timezone)
	{
		this.accessToken=accessToken;
		this.timezone=timezone;
		new AsyncWebHandler() {
			
			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException {
				try {
				
					// TODO Auto-generated method stub
					HttpPost httppost = new HttpPost(
							(SYLUtil.SYL_BASEURL+"api/User/UserProfileDetails"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");

					JSONObject holder = new JSONObject();
					holder.put("accessToken", SYLFetchUserProfileDetailsModelManager.this.accessToken);
					holder.put("timeZone", SYLFetchUserProfileDetailsModelManager.this.timezone);
			
					// passes the results to a string builder/entity
					StringEntity se = new StringEntity(holder.toString());
					httppost.setEntity(se);

					return httppost;

					// httppost.setEntity(new
					// UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					Log.e("Error", "Error");
				}

				return null;

			}

			@Override
			public void onResponse(String result) {
				// TODO Auto-generated method stub

				SYLFetchUserDetailsResponseModel mFetchuserdetailsresponsemodel;
				try {
					Log.e("profilefetchresult", result);
					Gson gson = new Gson();
					 mFetchuserdetailsresponsemodel = gson.fromJson(result,
							 SYLFetchUserDetailsResponseModel.class);

				} catch (Exception e) {
					mFetchuserdetailsresponsemodel=null;
				}
			mFetchUserdetailsListener.getUserDetailsFinish(mFetchuserdetailsresponsemodel);
				
			}

		}.execute("post");
	}
	
	
	
	
	
	
	
	
	
	
}
