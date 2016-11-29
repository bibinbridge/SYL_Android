package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLCreatenewAppointmentListener;
import com.webcamconsult.syl.model.SYLCreateNewAppointmentResponse;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLCreateNewAppointmentModelManager {

	String appointmentid;
	int userId;
	String topic, description, appointmentPriority1, appointmentPriority2,
			appointmentPriority3, appointmentDate1, appointmentDate2;
	String appointmentDate3, timeZone, accessToken;
	String importedfrom, fullname, email, mobile;
	String paricipantuserid;
	String url;
	public SYLCreatenewAppointmentListener msylcreatenewappointmentlistener;

	public void createnewAppointment(int userid, String importedfrom,
			String fullname, String mobile, String email, String topic,
			String description, String appointmentPriority1,
			String appointmentPriority2, String appointmentPriority3,
			String appointmentDate1, String appointmentDate2,
			String appointmentDate3, String timeZone, String accessToken,
			String participantuserid,String appointmentid,String url) {
		this.url=url;
		this.appointmentid=appointmentid;
		this.userId = userid;
		this.importedfrom = importedfrom;
		this.fullname = fullname;
		this.email = email;
		this.mobile = mobile;
		this.topic = topic;
		this.description = description;
		this.appointmentPriority1 = appointmentPriority1;
		this.appointmentPriority2 = appointmentPriority2;
		this.appointmentPriority3 = appointmentPriority3;
		this.appointmentDate1 = SYLUtil
				.getBackendDatetimeFormat(appointmentDate1);
		try {
			this.appointmentDate2 = SYLUtil
					.getBackendDatetimeFormat(appointmentDate2);
		} catch (Exception e) {
			this.appointmentDate2 = "";
		}

		try {
			this.appointmentDate3 = SYLUtil
					.getBackendDatetimeFormat(appointmentDate3);
		} catch (Exception e) {
			this.appointmentDate3 = "";
		}
		this.timeZone = timeZone;
		this.accessToken = accessToken;
		this.paricipantuserid = participantuserid;
		new AsyncWebHandler() {

			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException {
				// TODO Auto-generated method stub
				try {
					HttpPost httppost = new HttpPost(
							(SYLCreateNewAppointmentModelManager.this.url));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");
					JSONObject jo = new JSONObject();
					jo.put("userId", SYLCreateNewAppointmentModelManager.this.paricipantuserid);
					jo.put("importedFrom",
							SYLCreateNewAppointmentModelManager.this.importedfrom);
					jo.put("name",
							SYLCreateNewAppointmentModelManager.this.fullname);
					jo.put("mobile",
							SYLCreateNewAppointmentModelManager.this.mobile);
					jo.put("email",
							SYLCreateNewAppointmentModelManager.this.email);

					JSONArray participantsarray = new JSONArray();
					participantsarray.put(jo);
					JSONObject holder = new JSONObject();
					holder.put("appointmentID", Integer.parseInt(SYLCreateNewAppointmentModelManager.this.appointmentid));
					holder.put("userId", userId);
					holder.put("topic",
							SYLCreateNewAppointmentModelManager.this.topic);
					holder.put(
							"description",
							SYLCreateNewAppointmentModelManager.this.description);
					holder.put(
							"appointmentPriority1",
							SYLCreateNewAppointmentModelManager.this.appointmentPriority1);
					holder.put(
							"appointmentPriority2",
							SYLCreateNewAppointmentModelManager.this.appointmentPriority2);
					holder.put(
							"appointmentPriority3",
							SYLCreateNewAppointmentModelManager.this.appointmentPriority3);

					holder.put(
							"appointmentDate1",
							SYLCreateNewAppointmentModelManager.this.appointmentDate1);
					holder.put(
							"appointmentDate2",
							SYLCreateNewAppointmentModelManager.this.appointmentDate2);
					holder.put(
							"appointmentDate3",
							SYLCreateNewAppointmentModelManager.this.appointmentDate3);
					holder.put("timeZone",
							SYLCreateNewAppointmentModelManager.this.timeZone);
					holder.put(
							"accessToken",
							SYLCreateNewAppointmentModelManager.this.accessToken);
					holder.put("participants", participantsarray);
					// passes the results to a string builder/entity
					Log.e("new appointment request", holder.toString());
					StringEntity se = new StringEntity(holder.toString());
					httppost.setEntity(se);

					return httppost;
				}

				catch (Exception e) {
Log.e("Exception-reschedule",e.getMessage());
				}
				return null;
			}

			@Override
			public void onResponse(String result) {
				// TODO Auto-generated method stub
				SYLCreateNewAppointmentResponse mcreatenewappointmentresponse = null;
				try {
					Log.e("Request new appointment result", result);
					Gson gson = new Gson();

					mcreatenewappointmentresponse = gson.fromJson(result,
							SYLCreateNewAppointmentResponse.class);
				} catch (Exception e) {
					mcreatenewappointmentresponse = null;
				}
				msylcreatenewappointmentlistener
						.getCreatenewAppointmentFinish(mcreatenewappointmentresponse);
			}
		}.execute("post");

	}
}
