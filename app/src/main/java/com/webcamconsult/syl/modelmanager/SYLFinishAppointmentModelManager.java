package com.webcamconsult.syl.modelmanager;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLCancelAppointmentListener;
import com.webcamconsult.syl.interfaces.SYLFinishedAppointmentListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.model.SYLFinishAppointmentResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SYLFinishAppointmentModelManager {

	public SYLFinishedAppointmentListener mFinishedAppointmentListener;
	public String accesstoken,appointmentid,timezone;
	public void doFinishAppoitment(String accesstoken,String appointmentid,String timezone)

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
						(SYLUtil.SYL_BASEURL+"api/Appointment/Finish"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");
				JSONObject holder = new JSONObject();
				holder.put("appointmentID", Integer.parseInt(SYLFinishAppointmentModelManager.this.appointmentid));
				holder.put("accessToken", SYLFinishAppointmentModelManager.this.accesstoken);
				holder.put("timeZone", SYLFinishAppointmentModelManager.this.timezone);
				
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
			SYLFinishAppointmentResponseModel mfinishAppoinmentResponsemodel;

		try {
			Log.e("finish Appointment result",result);
			Gson gson = new Gson();
			mfinishAppoinmentResponsemodel = gson.fromJson(result,
					SYLFinishAppointmentResponseModel.class);
		}
		catch(Exception e)
		{
			mfinishAppoinmentResponsemodel=null;
		}
			mFinishedAppointmentListener.finishAppointment(mfinishAppoinmentResponsemodel);
		}



	}.execute("post");
}
}
