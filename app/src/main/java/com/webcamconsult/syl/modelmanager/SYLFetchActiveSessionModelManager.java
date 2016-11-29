package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLFetchActiveSessionListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.model.SYLFetchActiveSessionResponeModel;
import com.webcamconsult.syl.model.SYLOpentokconfigureResponse;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLFetchActiveSessionModelManager {
	String appointmentid,accesstoken,timezone;
	public SYLFetchActiveSessionListener mActiveSessionListener;
public void fetchActiveSession(String appointmentid,String accesstoken,String timezone)

{
	this.appointmentid=appointmentid;
	this.accesstoken=accesstoken;
	this.timezone=timezone;
	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			try {
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/SylConference/FetchActiveSessionForAppointment"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");

				JSONObject holder = new JSONObject();
				holder.put("appointmentID",Integer.parseInt(SYLFetchActiveSessionModelManager.this.appointmentid));
				holder.put("accessToken",SYLFetchActiveSessionModelManager.this.accesstoken);
				holder.put("timeZone",SYLFetchActiveSessionModelManager.this.timezone);
			Log.e("open-fetchactivesessio-",holder.toString());
			
				// passes the results to a string builder/entity
				StringEntity se = new StringEntity(holder.toString());
				httppost.setEntity(se);

				return httppost;
			}
			catch(Exception e)
			{
				}
			return null;
		}

		@Override
		public void onResponse(String result) {
			// TODO Auto-generated method stub

			SYLFetchActiveSessionResponeModel  mFetchActiveSessionResponsemodel;
			try {
				Log.e("opentok-fetchactivesession response",result);
				Gson gson = new Gson();
				mFetchActiveSessionResponsemodel = gson.fromJson(result,
						SYLFetchActiveSessionResponeModel.class);
			}
			catch(Exception e)
			{
				mFetchActiveSessionResponsemodel=null;
			}
			mActiveSessionListener.fetchActiveSessionFinish(mFetchActiveSessionResponsemodel);
		}
}.execute("post");
}
}
