package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLForgotpasswordListener;
import com.webcamconsult.syl.model.SYLForgotPasswordResponseModel;
import com.webcamconsult.syl.model.SYLVerificationcodeResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLForgetPasswordModelManager {
	String forgotpasswordemailaddress;
	String timezone;
	public SYLForgotpasswordListener mforgetpasswordlistener;
	SYLForgotPasswordResponseModel mforgetpasswordresponsemodel;;
public void doForgetPassword(String forgotpasswordemailaddress,String timezone)
{
	this.forgotpasswordemailaddress=forgotpasswordemailaddress;
this.timezone=timezone;
	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			try {
				
				// TODO Auto-generated method stub
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/User/ForgotPassword"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");

				JSONObject holder = new JSONObject();
				holder.put("email",SYLForgetPasswordModelManager.this.forgotpasswordemailaddress);
				holder.put("timeZone",SYLForgetPasswordModelManager.this.timezone);
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
				Log.e("forgotpassword-result",result);
		mforgetpasswordresponsemodel = gson.fromJson(result,
						SYLForgotPasswordResponseModel.class);	
			}
			catch(Exception e){
				mforgetpasswordresponsemodel=null;
			}
			mforgetpasswordlistener.onFinishForgotPassword(mforgetpasswordresponsemodel);
			}
		
		
	}.execute("post");
}
}
