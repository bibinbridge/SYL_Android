package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLOpentokconfigureListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.model.SYLOpentokconfigureResponse;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLOpentokModelManager {
	String appointmentid,userid,accesstoken,timezone;
public 	SYLOpentokconfigureListener sylopentokconfigurelistener;
	public void configureOpentok(String appointmentid,String userid,String accesstoken,String timezone)
	{
		this.appointmentid=appointmentid;
		this.userid=userid;
		this.accesstoken=accesstoken;
		this.timezone=timezone;
		
		new AsyncWebHandler() {

			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException {
				// TODO Auto-generated method stub
				try {
					HttpPost httppost = new HttpPost(
							(SYLUtil.SYL_BASEURL+"api/SylConference/Configure"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");

					JSONObject holder = new JSONObject();
					holder.put("appointmentId",Integer.parseInt(SYLOpentokModelManager.this.appointmentid));
					holder.put("userId",Integer.parseInt(SYLOpentokModelManager.this.userid));
					holder.put("accessToken",SYLOpentokModelManager.this.accesstoken);
					holder.put("timeZone",SYLOpentokModelManager.this.timezone);
				
					// passes the results to a string builder/entity
					Log.e("opentok-configure",holder.toString());
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
				Log.e("opentok-configure response",result);
				SYLOpentokconfigureResponse  mopentokconfigureresponsemodel;
				try {
					Gson gson = new Gson();
					mopentokconfigureresponsemodel= gson.fromJson(result,
							SYLOpentokconfigureResponse.class);
				}
				catch(Exception e)
				{
					mopentokconfigureresponsemodel=null;
				}
				sylopentokconfigurelistener.opentokconfigureFinish(mopentokconfigureresponsemodel);
			}
	}.execute("post");
}
}