package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.activities.SYLMenuFragment;
import com.webcamconsult.syl.interfaces.SYLSigninListener;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.model.SYLSignupResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLSigninModelManager {
	SYLSigninResponseModel msylsigninresponsemodel=null;
public	SYLSigninListener mSylSigninListener;
public String email,password,timezone,deviceUID;
	public void doUserSignin(String email, String password,String timezone,String deviceUID) {
		this.email=email;
		this.password=password;
		this.timezone=timezone;
		this.deviceUID=deviceUID;
		new AsyncWebHandler() {
		
			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException {
				try {
				
					// TODO Auto-generated method stub
					HttpPost httppost = new HttpPost(
							(SYLUtil.SYL_BASEURL+"api/Authentication/Login"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");

					JSONObject holder = new JSONObject();
					holder.put("email", SYLSigninModelManager.this.email);
					holder.put("password", SYLSigninModelManager.this.password);
					holder.put("timeZone", SYLSigninModelManager.this.timezone);
					holder.put("deviceUid", SYLSigninModelManager.this.deviceUID);
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
				if(result!=null) {

				try {
					Log.e("signinresult", result);
					Gson gson = new Gson();
					msylsigninresponsemodel = gson.fromJson(result,
							SYLSigninResponseModel.class);

				} catch (Exception e) {
					msylsigninresponsemodel = null;
				}
				mSylSigninListener.onDidFinished(msylsigninresponsemodel);
				
			}
			
			else {
				mSylSigninListener.onDidFinished(msylsigninresponsemodel);
			}
			}
		}.execute("post");
	}
}
