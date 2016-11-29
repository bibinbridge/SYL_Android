package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLContactUnfriendListener;

import com.webcamconsult.syl.model.SYLContactUnfriendResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLContactsUnfriendModelManager {
	String sender_userid, accesstoken,unfriend_userid,timezone;
	public SYLContactUnfriendListener mContactUnfriendListener;
	public void doUnfriendsylContact(String sender_userid,String accesstoken,String unfriend_userid,String timezone)

{
		this.sender_userid=sender_userid;
		this.accesstoken=accesstoken;
		this.unfriend_userid=unfriend_userid;
		this.timezone=timezone;
		new AsyncWebHandler() {

			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException {
				// TODO Auto-generated method stub
				try {
					HttpPost httppost = new HttpPost(
							(SYLUtil.SYL_BASEURL+"api/Contacts/Unfriend"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");
					JSONObject holder = new JSONObject();
					holder.put("userId1", Integer.parseInt(SYLContactsUnfriendModelManager.this.sender_userid));
					holder.put("userId2", Integer.parseInt(SYLContactsUnfriendModelManager.this.unfriend_userid));
					holder.put("accessToken", SYLContactsUnfriendModelManager.this.accesstoken);
					holder.put("timeZone", SYLContactsUnfriendModelManager.this.timezone);
					
					// passes the results to a string builder/entity
					Log.e("unfriend request",holder.toString());
					StringEntity se = new StringEntity(holder.toString());
					httppost.setEntity(se);

					return httppost;
				}
				 catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				catch(Exception e)
				{
					
					
					
				}
				return null;
			}

			@Override
			public void onResponse(String result) {
				// TODO Auto-generated method stub
				SYLContactUnfriendResponseModel  msylcontactUnfrienResponseModel;

			try {
				Log.e("Unfriend response",result);
				Gson gson = new Gson();
				msylcontactUnfrienResponseModel = gson.fromJson(result,
						SYLContactUnfriendResponseModel.class);
			}
			catch(Exception e)
			{
				msylcontactUnfrienResponseModel=null;
			}
			mContactUnfriendListener.finishUnfriendRequest(msylcontactUnfrienResponseModel);
			}



		}.execute("post");	
}
}
