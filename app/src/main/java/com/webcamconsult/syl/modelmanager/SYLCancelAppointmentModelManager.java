package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLCancelAppointmentListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLCancelAppointmentModelManager {

	public  SYLCancelAppointmentListener mCancelAppointmentListener;
	public String accesstoken,appointmentid,timezone;
	public void doCancelAppointment(String accesstoken,String appointmentid,String timezone)

{
this.accesstoken=accesstoken;
this.appointmentid=appointmentid;
this.timezone=timezone;
	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			try {
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/Appointment/Cancel"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");
				JSONObject holder = new JSONObject();
				holder.put("appointmentID", Integer.parseInt(SYLCancelAppointmentModelManager.this.appointmentid));
				holder.put("accessToken", SYLCancelAppointmentModelManager.this.accesstoken);
				holder.put("timeZone", SYLCancelAppointmentModelManager.this.timezone);
				
				// passes the results to a string builder/entity
				Log.e("cancel request",holder.toString());
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
			SYLCancelAppointmentModel mcancelappointmentmodel;

		try {
			Log.e("Cacel Appointment result",result);
			Gson gson = new Gson();
			mcancelappointmentmodel = gson.fromJson(result,
					SYLCancelAppointmentModel.class);
		}
		catch(Exception e)
		{
			mcancelappointmentmodel=null;
		}
		mCancelAppointmentListener.finishCancelAppointment(mcancelappointmentmodel);
		}



	}.execute("post");
}
}
