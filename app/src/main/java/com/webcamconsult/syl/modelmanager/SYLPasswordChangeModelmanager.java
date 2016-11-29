package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLPasswordChangeListener;
import com.webcamconsult.syl.model.SYLPasswordChangeResponseModel;
import com.webcamconsult.syl.model.SYLSignupResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLPasswordChangeModelmanager {
	String sylaccesstoken,oldpassword,newpassword,userid,timezone;
public 	SYLPasswordChangeListener mSylpasswordchangelistener;
public void doPasswordChange(String userid,String sylaccesstoken,String oldpassword,String newpassword,String timezone)
{
	
	this.userid=userid;
	this.sylaccesstoken=sylaccesstoken;
	this.oldpassword=oldpassword;
	this.newpassword=newpassword;
	this.timezone=timezone;
	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			try{
			HttpPost httppost = new HttpPost(
					(SYLUtil.SYL_BASEURL+"api/User/ChangePassword"));
			httppost.addHeader("www-request-type", "SYL2WAPP07459842");
			httppost.addHeader("www-request-api-version", "1.0");
			httppost.addHeader("Content-Type", "application/json");

			JSONObject holder = new JSONObject();
			holder.put("userId", Integer.parseInt(SYLPasswordChangeModelmanager.this.userid));
			holder.put("accessToken", SYLPasswordChangeModelmanager.this.sylaccesstoken);
			holder.put("oldPassword", SYLPasswordChangeModelmanager.this.oldpassword);
			holder.put("newPassword", SYLPasswordChangeModelmanager.this.newpassword);
			holder.put("timeZone", SYLPasswordChangeModelmanager.this.timezone);
			// passes the results to a string builder/entity
			StringEntity se = new StringEntity(holder.toString());
			httppost.setEntity(se);

			return httppost;
			}
			catch(Exception e )
			
			{
				
			}
			
			
			
			return null;
		}

		@Override
		public void onResponse(String result) {
			// TODO Auto-generated method stub

			SYLPasswordChangeResponseModel mPasswordchangeresponsemodel=null;
			try {
				Log.e("change password result",result);
				Gson gson = new Gson();
				mPasswordchangeresponsemodel = gson.fromJson(result,
						SYLPasswordChangeResponseModel.class);	
				mSylpasswordchangelistener.finishPasswordChange(mPasswordchangeresponsemodel);
			}
			catch(Exception e )
			{
				mSylpasswordchangelistener.finishPasswordChange(mPasswordchangeresponsemodel);
				
			}
		}
		
	}.execute("post");
}
}
