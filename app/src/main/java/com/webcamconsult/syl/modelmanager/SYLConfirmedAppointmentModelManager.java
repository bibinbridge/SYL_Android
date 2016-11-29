package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.AppOpsManager;
import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLConfirmAppointmentListener;
import com.webcamconsult.syl.model.SYLConfirmRequestResponseModel;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLConfirmedAppointmentModelManager {
	String appointmentid,accesstoken,selecteddate,selectedoption,timezone;
	int selectedoptionindex,selecteddateindex;
public SYLConfirmAppointmentListener mSylConfirmAppointmentListener;
	boolean flag;
public void doConfirmAppointment(String appointmentid,String accesstoken,boolean flag,String selecteddate,String selectedoption,String timezone,int selectedoptionindex,int selecteddateindex)
{
this.	appointmentid=appointmentid;
this.accesstoken=accesstoken;
this.flag=flag;
this.selecteddate=selecteddate;
this.selectedoption=selectedoption;
this.timezone=timezone;
this.selectedoptionindex=selectedoptionindex;
this.selecteddateindex=selecteddateindex;
	new AsyncWebHandler() {

		@Override
		public HttpUriRequest postHttpRequestMethod()
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			try {
				
				// TODO Auto-generated method stub
				HttpPost httppost = new HttpPost(
						(SYLUtil.SYL_BASEURL+"api/Appointment/InvitationAction"));
				httppost.addHeader("www-request-type", "SYL2WAPP07459842");
				httppost.addHeader("www-request-api-version", "1.0");
				httppost.addHeader("Content-Type", "application/json");

				JSONObject holder = new JSONObject();
				holder.put("appointmentId", Integer.parseInt(SYLConfirmedAppointmentModelManager.this.appointmentid));
				holder.put("accessToken",SYLConfirmedAppointmentModelManager.this.accesstoken);
				holder.put("isInvitationAccepted", SYLConfirmedAppointmentModelManager.this.flag);
				holder.put("selectedDate",SYLConfirmedAppointmentModelManager.this.selecteddate);
				holder.put("selectedOption", SYLConfirmedAppointmentModelManager.this.selectedoption);
				holder.put("timeZone", SYLConfirmedAppointmentModelManager.this.timezone);
				holder.put("selectedOptionIndex", SYLConfirmedAppointmentModelManager.this.selectedoptionindex);
				holder.put("selectedDateIndex", SYLConfirmedAppointmentModelManager.this.selecteddateindex);
			
				// passes the results to a string builder/entity
				Log.e("confirm",holder.toString());
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
		SYLConfirmRequestResponseModel mResponseModel=null;
			try {
				Log.e("Confirm Result",result);
				mResponseModel = gson.fromJson(result,
						SYLConfirmRequestResponseModel.class);

			} catch (Exception e) {
				mResponseModel = null;
			}
			mSylConfirmAppointmentListener.getConfirmAppointmentFinish(mResponseModel);
			
		}
		}.execute("post");
	}
}

