package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLVerifyMobileNumberListner;
import com.webcamconsult.syl.model.SYLVerifyMobileDetails;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;

public class SYLVerifyMobileNumberModelManager
{
	public SYLVerifyMobileNumberListner  iSYLVerifyMobileNumberListner;

	public void verifyMobileNumber(Context mContext, final int userid,
			final String verifycode, final String mobilenumber, String method) 
	{
		
		new AsyncWebHandler()
		{

			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException 
					{
				HttpPost httppost = new HttpPost(("http://10.0.0.45/see-you-later/public/index.php/api/auth/verify_code"));
				 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				 nameValuePairs.add(new BasicNameValuePair("user_id",Integer.toString(userid)));
		         nameValuePairs.add(new BasicNameValuePair("code",verifycode));
		         nameValuePairs.add(new BasicNameValuePair("method",mobilenumber));
		         try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		         catch(Exception e)
		         {
		        	 Log.e("Error", "Error");
		         }
		

			return httppost;
			}

			@Override
			public void onResponse(String result) 
			{
				SYLVerifyMobileDetails  sYLVerifyMobileDetails = null;
				
				Gson gson = new Gson();
				try
				{
					sYLVerifyMobileDetails=gson.fromJson(result,SYLVerifyMobileDetails.class);
					Log.e("WAt","wat");
				}
				catch(Exception e)
				{
					Log.e("error", e.getMessage());
				}
				
				iSYLVerifyMobileNumberListner.onDidFinished(sYLVerifyMobileDetails);
				
			}
			
		}.execute(method);
	}

}
