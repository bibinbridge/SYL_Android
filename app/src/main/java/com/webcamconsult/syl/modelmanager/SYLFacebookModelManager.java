package com.webcamconsult.syl.modelmanager;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLSigninListener;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Sandeep on 8/24/2015.
 */
public class SYLFacebookModelManager {
String facebookaccesstoken,deviceUID,deviceType,timezone;
    SYLSigninResponseModel msylsigninresponsemodel=null;
    public SYLSigninListener mSylSigninListener;
    public void doFacebookSignin(String facebookaccesstoken,String deviceUID,String deviceType,String timezone)

    {

this.facebookaccesstoken=facebookaccesstoken;
        this.deviceUID=deviceUID;
        this.deviceType=deviceType;
        this.timezone=timezone;

        new AsyncWebHandler() {

            @Override
            public HttpUriRequest postHttpRequestMethod()
                    throws UnsupportedEncodingException {
                try {

                    // TODO Auto-generated method stub
                    HttpPost httppost = new HttpPost(
                            (SYLUtil.SYL_BASEURL+"api/Authentication/LoginWithFaceBook"));
                    httppost.addHeader("www-request-type", "SYL2WAPP07459842");
                    httppost.addHeader("www-request-api-version", "1.0");
                    httppost.addHeader("Content-Type", "application/json");

                    JSONObject holder = new JSONObject();
                    holder.put("fbAccessToken", SYLFacebookModelManager.this.facebookaccesstoken);
                    holder.put("deviceUid", SYLFacebookModelManager.this.deviceUID);
                    holder.put("deviceType", SYLFacebookModelManager.this.deviceType);
                    holder.put("timeZone", SYLFacebookModelManager.this.timezone);
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
            public void onResponse(String result) {
                // TODO Auto-generated method stub
                if(result!=null) {
                    Log.e("facebooksigninresult", result);
                    try {
                        Gson gson = new Gson();
                        msylsigninresponsemodel = gson.fromJson(result,
                                SYLSigninResponseModel.class);

                    } catch (Exception e) {
                        msylsigninresponsemodel = null;
                    }
                    mSylSigninListener.onDidFinished(msylsigninresponsemodel);

                }

                else {
                    mSylSigninListener.onDidFinished(msylsigninresponsemodel);
                }
            }
        }.execute("post");


    }





}
