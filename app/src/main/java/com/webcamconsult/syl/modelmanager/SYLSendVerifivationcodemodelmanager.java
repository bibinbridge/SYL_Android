package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLSendVerificationcodeListener;
import com.webcamconsult.syl.model.SYLAppointmentDetailsAll;
import com.webcamconsult.syl.model.SYLVerificationcodeResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLSendVerifivationcodemodelmanager {
	
	public String  verificationcode,email,accesstoken,timezone;
	public SYLSendVerificationcodeListener msendverificationcodelistener;
public	SYLVerificationcodeResponseModel mverificationcoderesponsemodel;
public void sendverificationcode(String verificationcode,String email,String accesstoken,String timezone)
{
	this.verificationcode=verificationcode;
	this.email=email;
	this.accesstoken=accesstoken;
	this.timezone=timezone;
	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			try {
				
				// TODO Auto-generated method stub
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/User/ValidateVerificationCode"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");
			
				JSONObject holder = new JSONObject();
				holder.put("verificationCode", SYLSendVerifivationcodemodelmanager.this.verificationcode);
				holder.put("email", SYLSendVerifivationcodemodelmanager.this.email);
				holder.put("accessToken", SYLSendVerifivationcodemodelmanager.this.accesstoken);
				holder.put("timeZone", SYLSendVerifivationcodemodelmanager.this.timezone);
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

		Gson gson = new Gson();
		try {
			Log.e("Verification code response",result);
			mverificationcoderesponsemodel = gson.fromJson(result,
					SYLVerificationcodeResponseModel.class);	
		}
		catch(Exception e){}
		msendverificationcodelistener.onDidFinished(mverificationcoderesponsemodel);
		}
		
	}.execute("post");

}
}
