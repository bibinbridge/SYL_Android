package com.webcamconsult.syl.modelmanager;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLAppointmentRemoveListener;
import com.webcamconsult.syl.interfaces.SYLDeleteHistoryAppoitmentsListener;
import com.webcamconsult.syl.model.SYLAppointmentRemoveResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Sandeep on 11/27/2015.
 */
public class SYLDeleteHistoryAppointmentsModelmanager
{
 public   String  accesstoken,timezone;
    public SYLDeleteHistoryAppoitmentsListener  mdeleteHistoryAppointmentsListener;
public void deleteHistoryAppointments(String accesstoken,String timezone) {
    this.accesstoken = accesstoken;
    this.timezone=timezone;
    new AsyncWebHandler() {

        @Override
        public HttpUriRequest postHttpRequestMethod()
                throws UnsupportedEncodingException {
            // TODO Auto-generated method stub
            try {
                HttpPost httppost = new HttpPost(
                        (SYLUtil.SYL_BASEURL+"api/Appointment/RemoveAllArchivedAppointments"));
                httppost.addHeader("www-request-type", "SYL2WAPP07459842");
                httppost.addHeader("www-request-api-version", "1.0");
                httppost.addHeader("Content-Type", "application/json");
                JSONObject holder = new JSONObject();


                holder.put("accessToken", SYLDeleteHistoryAppointmentsModelmanager.this.accesstoken);
                holder.put("timeZone",SYLDeleteHistoryAppointmentsModelmanager.this.timezone);

                // passes the results to a string builder/entity
                Log.e("history delete request", holder.toString());
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
            SYLAppointmentRemoveResponseModel msylappointmentremoveresponsemodel;

            try {
                Log.e("delete response",result);
                Gson gson = new Gson();
                msylappointmentremoveresponsemodel = gson.fromJson(result,
                        SYLAppointmentRemoveResponseModel.class);
            }
            catch(Exception e)
            {
                msylappointmentremoveresponsemodel =null;
            }
            mdeleteHistoryAppointmentsListener.deleteHistoryAppointmentsFinish(msylappointmentremoveresponsemodel);
        }



    }.execute("post");

}
}
