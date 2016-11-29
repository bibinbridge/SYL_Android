package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLFetchSessionStatusListener;

import com.webcamconsult.syl.model.SYLFetchSessionAppointmentResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLFetchSessionStatusModelManager {
	public String appointmentid,accesstoken,timezone;
	public SYLFetchSessionStatusListener mFetchSessionStatusListener;
	public void fetchSessionStatusForAppointment(String appointmentid,String accesstoken,String timezone)

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
							(SYLUtil.SYL_BASEURL+"api/SylConference/FetchSessionStatusForAppointment"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");

					JSONObject holder = new JSONObject();
					holder.put("appointmentID",Integer.parseInt(SYLFetchSessionStatusModelManager.this.appointmentid));
					holder.put("accessToken",SYLFetchSessionStatusModelManager.this.accesstoken);
					holder.put("timeZone",SYLFetchSessionStatusModelManager.this.timezone);
				Log.e("fetch sessionstatus request-",holder.toString());
				
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

				SYLFetchSessionAppointmentResponseModel  mFetchSessionstatusresponseModel;
				try {
					Log.e("opentok-fetchsessions response",result);
					Gson gson = new Gson();
					mFetchSessionstatusresponseModel = gson.fromJson(result,
							SYLFetchSessionAppointmentResponseModel.class);
				}
				catch(Exception e)
				{
					mFetchSessionstatusresponseModel=null;
				}
				mFetchSessionStatusListener.finishFetchSessionAppointmentStatus(mFetchSessionstatusresponseModel);
			}
	}.execute("post");
	}
}
