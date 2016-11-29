package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLAppointmentRemoveListener;
import com.webcamconsult.syl.interfaces.SYLContactUnfriendListener;
import com.webcamconsult.syl.model.SYLAppointmentRemoveResponseModel;
import com.webcamconsult.syl.model.SYLContactUnfriendResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLAppointmentRemoveModelManager {
	String appointmentid, accesstoken,timezone;
	public SYLAppointmentRemoveListener mAppointmentremoveListener;
	public void doRemoveAppointment(String appointmentid,String accesstoken,String timezone)

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
							(SYLUtil.SYL_BASEURL+"api/Appointment/Remove"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");
					JSONObject holder = new JSONObject();
					holder.put("appointmentID", Integer.parseInt(SYLAppointmentRemoveModelManager.this.appointmentid));
			
					holder.put("accessToken", SYLAppointmentRemoveModelManager.this.accesstoken);
					holder.put("timeZone",SYLAppointmentRemoveModelManager.this.timezone);
					
					// passes the results to a string builder/entity
					Log.e("appointment delete request",holder.toString());
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
				SYLAppointmentRemoveResponseModel  msylappointmentremoveresponsemodel;

			try {
				Log.e("Unfriend response",result);
				Gson gson = new Gson();
				msylappointmentremoveresponsemodel = gson.fromJson(result,
						SYLAppointmentRemoveResponseModel.class);
			}
			catch(Exception e)
			{
				msylappointmentremoveresponsemodel =null;
			}
			mAppointmentremoveListener.finishAppointmentRemove(msylappointmentremoveresponsemodel);
			}



		}.execute("post");	
}
}
