package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLDeviceRegisterListener;
import com.webcamconsult.syl.model.SYLDeviceRegisterResponseModel;
import com.webcamconsult.syl.model.SYLVerifyMobileDetails;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLRegisterDeviceViewManager;

public class SYLDeviceRegisterModelManager {


String appname;
String appversion;
String deviceUID;
String regid;
String deviceName;
String devicemodel;
String deviceversion;
boolean pushbadge;
boolean pushalert;
boolean pushsound;
String devicetype;
String timezone;
public void doRegisterDevice(String appname,String appversion,String deviceUID,String regid,String deviceName,String devicemodel,String deviceversion,boolean pushbadge,boolean pushalert,boolean pushsound,String devicetype,String timezone)
{
	this.appname=appname;
	this.appversion=appversion;
	this.deviceUID=deviceUID;
	this.regid=regid;
this.deviceName=deviceName;
this.devicemodel=devicemodel;
this.deviceversion=deviceversion;
this.pushbadge=pushbadge;
this.pushalert=pushalert;
this.pushsound=pushsound;
this.devicetype=devicetype;
this.timezone=timezone;
	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			try {
				
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/PushNotification/RegisterDevice"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");

				JSONObject holder = new JSONObject();
				holder.put("appName", SYLDeviceRegisterModelManager.this.appname);
				holder.put("appVersion", SYLDeviceRegisterModelManager.this.appversion);
				holder.put("deviceUID", SYLDeviceRegisterModelManager.this.deviceUID);
				holder.put("deviceToken", SYLDeviceRegisterModelManager.this.regid);
				holder.put("deviceName", SYLDeviceRegisterModelManager.this.deviceName);
				holder.put("deviceModel", SYLDeviceRegisterModelManager.this.devicemodel);
				holder.put("deviceVersion",SYLDeviceRegisterModelManager.this.deviceversion);
				holder.put("pushBadge", SYLDeviceRegisterModelManager.this.pushbadge);
				holder.put("pushAlert", SYLDeviceRegisterModelManager.this.pushalert);
				holder.put("pushSound", SYLDeviceRegisterModelManager.this.pushsound);
				holder.put("deviceType", SYLDeviceRegisterModelManager.this.devicetype);
				holder.put("timeZone", SYLDeviceRegisterModelManager.this.timezone);
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
			if(result!=null)
			{
			Log.e("device register result",result);
			
			SYLDeviceRegisterResponseModel  syldeviceregisterresponsemodel = null;
			
			Gson gson = new Gson();
			
			try {
				syldeviceregisterresponsemodel=gson.fromJson(result,SYLDeviceRegisterResponseModel.class);
			}
			
		catch(Exception e)	
		{
			syldeviceregisterresponsemodel=null;
		}
			
			}
			
			
			
			
			
		}
		
	}.execute("post");

}
}
