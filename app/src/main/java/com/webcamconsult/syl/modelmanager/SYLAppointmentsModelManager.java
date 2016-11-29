package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLAppointmentListner;
import com.webcamconsult.syl.interfaces.SYLFetchAppointmentDetailsListener;
import com.webcamconsult.syl.model.SYLAppointmentDetailsAll;
import com.webcamconsult.syl.model.SYLAppointmentsDescription;
import com.webcamconsult.syl.model.SYLFetchAppointmentsResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandlerForGetApi;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLAppointmentsModelManager {

	public SYLAppointmentListner sYLAppointmentListner;
	public SYLFetchAppointmentDetailsListener mfetchAppointmentsListener;
	List<SYLAppointmentsDescription> sYLappointmentdetails;
	public String accesstoken, userid,timezone;

	public void getAppointments(String method, String accesstoken, String userid,String gmttimezone) {

		this.accesstoken = accesstoken;
		this.userid = userid;
		this.timezone=gmttimezone;
		new AsyncWebHandlerForGetApi() {

			@Override
			public HttpUriRequest getHttpRequestMethod() {
				// TODO Auto-generated method stub
				try {

					// TODO Auto-generated method stub
					HttpPost httppost = new HttpPost(
							(SYLUtil.SYL_BASEURL+"api/Appointment/FetchAllAppointments"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");

					JSONObject holder = new JSONObject();

					// holder.put("userID",
					// Integer.parseInt(SYLAppointmentsModelManager.this.userid));
					// holder.put("accessToken",
					// SYLAppointmentsModelManager.this.accesstoken);
					holder.put("userID", Integer
							.parseInt(SYLAppointmentsModelManager.this.userid));
					holder.put("accessToken",
							SYLAppointmentsModelManager.this.accesstoken);
					holder.put("timeZone",
							SYLAppointmentsModelManager.this.timezone);
					Log.e("appointment request", holder.toString());
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
			public void ongetResponse(String result) {
				/*
				 * 
				 * String s =
				 * " {\"success\":\"true\",\"TodaysAppointments\":[{\"user_id\":12,\"email\":\"john@gmail.com\",\"firstname\":\"Ajay\",\"lastname\":\"Raj\",\"profile_image\":\"http://farm6.staticflickr.com/5457/9046948185_3be564ac10_s.jpg\",\"time\":\"10:00:00\",\"date\":\"10-10-2014\"},{\"user_id\":12,\"email\":\"john@gmail.com\",\"firstname\":\"Praveen\",\"lastname\":\"jose\",\"profile_image\":\"http://farm3.staticflickr.com/2828/9046946983_923887b17d_s.jpg\",\"time\":\"10:00:00\",\"date\":\"10-10-2014\"}],\"RequestReceived\":[{\"user_id\":12,\"email\":\"john@gmail.com\",\"firstname\":\"Ajay\",\"lastname\":\"Akshay\",\"profile_image\":\"http://farm4.staticflickr.com/3773/9049175264_b0ea30fa75_s.jpg\",\"time\":\"10:00:00\",\"date\":\"10-10-2014\"},{\"user_id\":12,\"email\":\"john@gmail.com\",\"firstname\":\"Praveen\",\"lastname\":\"jose\",\"profile_image\":\"http://lh4.ggpht.com/_XjNwVI0kmW8/TCOwNtzGheI/AAAAAAAAC84/SxFJhG7Scgo/s144-c/0014.jpg\",\"time\":\"10:00:00\",\"date\":\"10-10-2014\"}],\"ConfirmedAppointments\":[{\"user_id\":13,\"email\":\"john@gmail.com\",\"firstname\":\"Abi\",\"lastname\":\"Rahul\",\"profile_image\":\"http://lh5.ggpht.com/_hepKlJWopDg/TB-_WXikaYI/AAAAAAAAElI/715k4NvBM4w/s144-c/IMG_0075.JPG\",\"time\":\"10:00:00\",\"date\":\"10-10-2014\"}],\"RequestSend\":[{\"user_id\":12,\"email\":\"john@gmail.com\",\"firstname\":\"Ajith\",\"lastname\":\"Antony\",\"profile_image\":\"http://lh5.ggpht.com/_loGyjar4MMI/S-InWuHkR9I/AAAAAAAADJE/wD-XdmF7yUQ/s144-c/Colorado%20River%20Sunset.jpg\",\"time\":\"10:00:00\",\"date\":\"10-10-2014\"}],\"CancelledRequest\":[{\"user_id\":12,\"email\":\"john@gmail.com\",\"firstname\":\"john\",\"lastname\":\"Sanjay\",\"profile_image\":\"http://lh3.ggpht.com/_syQa1hJRWGY/TBwkCHcq6aI/AAAAAAABBEg/R5KU1WWq59E/s144-c/Antelope.JPG\",\"time\":\"10:00:00\",\"date\":\"10-10-2014\"}]}"
				 * ; result = s; SYLAppointmentDetailsAll
				 * sYLAppointmentsDetailsall = null;
				 * 
				 * //
				 * 
				 * Gson gson = new Gson(); // JsonReader reader = new
				 * JsonReader(new StringReader(result)); //
				 * reader.setLenient(true); try { sYLAppointmentsDetailsall =
				 * gson.fromJson(result, SYLAppointmentDetailsAll.class);
				 * 
				 * Log.e("WAt", "wat"); } catch (Exception e) { Log.e("error",
				 * e.getMessage()); }
				 * 
				 * sYLAppointmentListner.onDidFinished(sYLAppointmentsDetailsall)
				 * ;
				 */
				SYLFetchAppointmentsResponseModel mResponseModel = null;

				Gson gson = new Gson();
				try {

					mResponseModel = gson.fromJson(result,
							SYLFetchAppointmentsResponseModel.class);

				}

				catch (Exception e) {
					mResponseModel = null;
				}
				mfetchAppointmentsListener
						.getAppointmentDetailsFinish(mResponseModel);

			}

		}.execute(method);

	}

}
