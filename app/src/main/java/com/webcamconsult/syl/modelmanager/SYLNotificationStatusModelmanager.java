package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLNotificationListener;
import com.webcamconsult.syl.model.SYLNotificationResponseModel;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLNotificationStatusModelmanager {
	
	String userId;
	boolean isActivated;
	String deviceUId;
	public SYLNotificationListener mNotificationListener;
	String timezone;
public void doNotifivationChange(String userId,boolean isActivated,String deviceUId,String timezone)
{
	this.userId=userId;
	this.isActivated=isActivated;
	this.deviceUId=deviceUId;
	this.timezone=timezone;
	new AsyncWebHandler() {
		
		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			try {
			
				// TODO Auto-generated method stub
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/PushNotification/ChangeDeviceNotificationStatus"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");

				JSONObject holder = new JSONObject();
				holder.put("userId", Integer.parseInt(SYLNotificationStatusModelmanager.this.userId));
				holder.put("isActivated", SYLNotificationStatusModelmanager.this.isActivated);
				holder.put("deviceUId", SYLNotificationStatusModelmanager.this.deviceUId);
				holder.put("timeZone", SYLNotificationStatusModelmanager.this.timezone);
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

			SYLNotificationResponseModel mNotificationResponseModel;
			try {
				Log.e("notificationresult", result);
				Gson gson = new Gson();
				mNotificationResponseModel = gson.fromJson(result,
						SYLNotificationResponseModel.class);

			} catch (Exception e) {
			mNotificationResponseModel=null;
			}
		mNotificationListener.finishsylNotificationStatus(mNotificationResponseModel);
			
		}

	}.execute("post");
	
	
	
	
}
}

