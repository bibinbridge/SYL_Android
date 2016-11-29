package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLChangeconferenceListener;
import com.webcamconsult.syl.model.SYLChangeConferenceResponeModel;
import com.webcamconsult.syl.model.SYLResendVerificationResponsemodel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLChangeOpentokstatusModelManager {
public SYLChangeconferenceListener mChangeConferenceListener;
String statuscode,timezone,appointmentid;

public void changeOpentokStatus(String statuscode,String timezone,String appointmentid)
{
	this.statuscode=statuscode;
	this.timezone=timezone;
	this.appointmentid=appointmentid;

	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
		try {
				
				// TODO Auto-generated method stub
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/SylConference/ChangeSylConferenceStatusCode"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");
			
				JSONObject holder = new JSONObject();
		
				holder.put("appointmentId",Integer.parseInt(SYLChangeOpentokstatusModelManager.this.appointmentid));
				holder.put("statusCode",SYLChangeOpentokstatusModelManager.this.statuscode);
				holder.put("timeZone",SYLChangeOpentokstatusModelManager.this.timezone);
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

			SYLChangeConferenceResponeModel mChangeConferenceResponsemodel=null;
			Gson gson = new Gson();
			try {
				Log.e("resendVerification code response",result);
				mChangeConferenceResponsemodel= gson.fromJson(result,
					SYLChangeConferenceResponeModel .class);
					
					}
					catch(Exception e){
						mChangeConferenceResponsemodel=null;	
					}
				mChangeConferenceListener.finishConferenceStstus(mChangeConferenceResponsemodel);
					}
		



	}.execute("post");



}
}
