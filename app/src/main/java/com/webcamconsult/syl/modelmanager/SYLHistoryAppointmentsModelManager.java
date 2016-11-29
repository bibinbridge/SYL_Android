package com.webcamconsult.syl.modelmanager;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLFetchHistoryAppointmentsListener;
import com.webcamconsult.syl.model.SYLFetchAppointmentsResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandlerForGetApi;
import com.webcamconsult.syl.utilities.SYLUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Sandeep on 11/24/2015.
 */
public class SYLHistoryAppointmentsModelManager  {
    public SYLFetchHistoryAppointmentsListener mFetchhistoryAppointmentsListener;
    public String accesstoken, userid,timezone;


    public void geHistorytAppointments(String method, String accesstoken, String userid,String gmttimezone) {

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
                            (SYLUtil.SYL_BASEURL+"api/Appointment/FetchAllArchivedAppointments"));
                    httppost.addHeader("www-request-type", "SYL2WAPP07459842");
                    httppost.addHeader("www-request-api-version", "1.0");
                    httppost.addHeader("Content-Type", "application/json");

                    JSONObject holder = new JSONObject();

                    // holder.put("userID",
                    // Integer.parseInt(SYLAppointmentsModelManager.this.userid));
                    // holder.put("accessToken",
                    // SYLAppointmentsModelManager.this.accesstoken);
                    holder.put("userID", Integer
                            .parseInt(SYLHistoryAppointmentsModelManager.this.userid));
                    holder.put("accessToken",
                            SYLHistoryAppointmentsModelManager.this.accesstoken);
                    holder.put("timeZone",
                            SYLHistoryAppointmentsModelManager.this.timezone);
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

                SYLFetchAppointmentsResponseModel mappointmentsResponseModel = null;

                Gson gson = new Gson();
                try {
                Log.e("historyappointmenresult",result);
                    mappointmentsResponseModel = gson.fromJson(result,
                            SYLFetchAppointmentsResponseModel.class);

                }

                catch (Exception e) {
                    Log.e("historyappointmenresult",result);
                    mappointmentsResponseModel = null;
                }
                mFetchhistoryAppointmentsListener
                        .getHistoryAppointmentDetailsFinish(mappointmentsResponseModel);

            }

        }.execute(method);

    }


}
