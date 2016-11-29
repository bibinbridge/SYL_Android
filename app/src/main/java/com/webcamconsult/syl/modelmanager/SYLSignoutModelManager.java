package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLSignoutListener;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.model.SYLSignoutRespomseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLSignoutModelManager {
	
	String userid,deviceUID,timezone;
	public SYLSignoutListener msylsignoutListener;
public void dosyluserSignout(String userid,String deviceUID,String timezone)
{
	this.userid=userid;
	this.deviceUID=deviceUID;
	this.timezone=timezone;
	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			try {
				
				// TODO Auto-generated method stub
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/User/LogoutAndDetachDevice"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");

				JSONObject holder = new JSONObject();
				holder.put("userId",Integer.parseInt(SYLSignoutModelManager.this.userid));
				holder.put("deviceUID", SYLSignoutModelManager.this.deviceUID);
				holder.put("timeZone", SYLSignoutModelManager.this.timezone);
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

			SYLSignoutRespomseModel mResponsemodel;
			try {
				Log.e("signout result",result);
			Gson gson = new Gson();
			mResponsemodel = gson.fromJson(result,
					SYLSignoutRespomseModel.class);
			}
			catch(Exception e){
				mResponsemodel=null;
				msylsignoutListener.fisnishsylusersignout(mResponsemodel);
				
			}
			msylsignoutListener.fisnishsylusersignout(mResponsemodel);
		}
	
	}.execute("post");

}
}
