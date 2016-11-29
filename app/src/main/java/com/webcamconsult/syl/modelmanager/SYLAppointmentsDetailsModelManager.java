package com.webcamconsult.syl.modelmanager;

import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLAppointmentDetailsListner;
import com.webcamconsult.syl.model.SYLAppointmentsDescription;
import com.webcamconsult.syl.networkoperations.AsyncWebHandlerForGetApi;


public class SYLAppointmentsDetailsModelManager 
{
	
	public SYLAppointmentDetailsListner sYLAppointmentDetailsListner;

	public void getAppointments(String method, String userID, String listclick) 
	{
		new AsyncWebHandlerForGetApi() 
		{

			@Override
			public HttpUriRequest getHttpRequestMethod() 
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void ongetResponse(String result) 
			{
				
				
				 result="{\"success\":\"true\",\"Response\":[{\"user_id\":12,\"firstname\":\"john\",\"email\":\"john@gmail.com\",\"username\":\"john\",\"profile_image\":\"http://lh4.ggpht.com/_XjNwVI0kmW8/TCOwNtzGheI/AAAAAAAAC84/SxFJhG7Scgo/s144-c/0014.jpg\",\"topic\":\"Meeting\",\"description\":\"Description is one of four rhetorical modes (also known as modes of discourse), along with exposition, argumentation, and narration. Each of the rhetorical modes is present in a variety of forms and each has its own purpose and conventions.\",\"mobile\":\"8547475591\",\"skypeid\":\"john@gmail.com\",\"priority1\":\"skype\",\"priority2\":\"mobile\",\"priority3\":\"opentok\",\"date1\":\"2014-10-20 10:00:00\",\"date2\":\"2014-10-13 11:01:02\",\"date3\":\"2014-10-14 12:00:00\",\"created_at\":\"2014-08-20 10:02:54\",\"type\":\"Request Received\"}]}";
				
				 SYLAppointmentsDescription  sYLAppointmentsDescription =null;
				 
				 Gson gson = new Gson();
				 
				 try
					{
					 sYLAppointmentsDescription=gson.fromJson(result,SYLAppointmentsDescription.class);
						Log.e("WAt","wat");
					}
					catch(Exception e)
					{
						Log.e("error", e.getMessage());
					}

				 sYLAppointmentDetailsListner.onDidFinished(sYLAppointmentsDescription);
				 
				 
			}
			
			
			
		}.execute(method);
		
	}


}
