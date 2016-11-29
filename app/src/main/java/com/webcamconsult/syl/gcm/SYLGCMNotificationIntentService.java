package com.webcamconsult.syl.gcm;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;


import com.webcamconsult.syl.opentok.UIActivity;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLApplicationConstants;
import com.webcamconsult.syl.utilities.SYLUtil;

import java.util.List;

public class SYLGCMNotificationIntentService extends IntentService {
	public static final int notifyID = 9001;
    NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
	BroadcastReceiver br;
	public String h;
    public SYLGCMNotificationIntentService() {
        super("GcmIntentService");
    }



	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
 
        String messageType = gcm.getMessageType(intent);
 
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString(),"error","error","error");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString(),"error","error","error");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
         String message=extras.toString();
         if(message.contains("Call From ")){
                String user=extras.get(SYLApplicationConstants.MSG_USER).toString();
                String conference=extras.get(SYLApplicationConstants.MSG_CONFERENCE).toString();
                String syl_call=extras.get(SYLApplicationConstants.SYL_CALL).toString();
                Log.e("user",user);
                Log.e("confernce",conference);
                Log.e("call",syl_call);
                sendNotification(
                        extras.get(SYLApplicationConstants.MSG_KEY).toString(),user,conference,syl_call);
         }
         else {
			 try {
				 String messagekey = extras.get(SYLApplicationConstants.MSG_KEY).toString();
				 if (messagekey != null)
					 scheduleNotification(messagekey);
			 }
			 catch (Exception e)
			 {
				 Log.e("notification error","-->"+e.getMessage());
			 }

         }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);	
	}
	 private void sendNotification(String msg,String userddetails,String conferencedetails,String sylcall) {
	    try{   
		 JSONObject jobject=new JSONObject(conferencedetails);
		 String token=jobject.getString("subscriberToken");
		 String publishertoken=jobject.getString("publisherToken");
		 String sessionid=jobject.getString("opentokSessionId");
		 String apikey=jobject.getString("apiKey");
		 String appointmentid=jobject.getString("appointmentId");
		 JSONObject   userjobject=new JSONObject(userddetails);
		 String name=userjobject.getString("name");
		 String profileimageurl=userjobject.getString("profileImage");
		 JSONObject   sylcallobject=new JSONObject(sylcall);
	boolean isConferencecall=	 sylcallobject.getBoolean("isSylConferenceCall");
		
		 
	/*	 Intent intent=new Intent(this,UIActivity.class);
		 intent.putExtra("sessionid", sessionid);
		 intent.putExtra("token",token);
		 intent.putExtra("apikey", apikey);
		 intent.putExtra("name",name);
		 intent.putExtra("profileimageurl", profileimageurl);
		 intent.putExtra("appointmentid", appointmentid);

		*/

		 mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);
/*
	       PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	              intent, PendingIntent.FLAG_UPDATE_CURRENT); 
PendingIntent pi=PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.ic_launcher)
	        .setContentTitle("GCM Notification")
	        .setStyle(new NotificationCompat.BigTextStyle()
	        .bigText(msg))
	        .setContentText(msg);

	        mBuilder.setContentIntent(contentIntent);
	        mNotificationManager.notify(notifyID, mBuilder.build());
	        sendBroadcast(intent);
	    }
	    catch(Exception e)
	    {
	    	Log.e("Error",e.getMessage());
	    }
*/

			String packagename=getPackageName();
	  /*  Intent resultIntent = new Intent(this, UIActivity.class);
	    resultIntent.putExtra("sessionid", sessionid);
	    resultIntent.putExtra("token",publishertoken);
	    resultIntent.putExtra("apikey", apikey);
	    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	    // Adds the back stack
	    stackBuilder.addParentStack(UIActivity.class);
	    // Adds the Intent to the top of the stack
	    stackBuilder.addNextIntent(resultIntent);
	    // Gets a PendingIntent containing the entire back stack
	    PendingIntent resultPendingIntent =
	            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT); */
			Uri notificationsoundpath = Uri.parse("android.resource://" + packagename + "/" + R.raw.sylopentoktone);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.syl_applaunchericon)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
						.setAutoCancel(true)
        .setContentText(msg)
				;

        //mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(notifyID, mBuilder.build());
        Intent resultIntent1 = new Intent(this, CallBroadcastReceiver.class);
	    resultIntent1.putExtra("sessionid", sessionid);
	    resultIntent1.putExtra("token",publishertoken);
	    resultIntent1.putExtra("apikey", apikey);
	    resultIntent1.putExtra("name", name);
	    resultIntent1.putExtra("profileimageurl",profileimageurl);
	    resultIntent1.putExtra("appointmentid", appointmentid);
	if( isConferencecall){
sendBroadcast(resultIntent1);
	}
	    
	    }
	    
	    catch(Exception e){}
	 }
	 
	private void scheduleNotification(String message) 
	{
		try {


			mNotificationManager = (NotificationManager)
					this.getSystemService(Context.NOTIFICATION_SERVICE);
			Intent homeIntent = new Intent(this,
					SYLFragmentChangeActivity.class);
			homeIntent.putExtra("fragmentvalue", "newappointment");
			homeIntent.putExtra("needrefresh", "true");
			String packagename = getPackageName();
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					homeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			Uri notificationsoundpath = Uri.parse("android.resource://" + packagename + "/" + R.raw.normalnotification);
			NotificationCompat.Builder mBuilder =
					new NotificationCompat.Builder(this)
							.setSmallIcon(R.drawable.syl_applaunchericon)
							.setContentTitle("SYL Notification")
							.setStyle(new NotificationCompat.BigTextStyle()
									.bigText(message))
							.setSound(notificationsoundpath)
							.setAutoCancel(true)
							.setContentText(message);

				 mBuilder.setContentIntent(contentIntent);


			boolean foregroud = new ForegroundCheckTask().execute(getBaseContext()).get();
			if (foregroud) {

				String sylaccesstoken=	SYLSaveValues.getSYLaccessToken(getApplicationContext());
				if(!sylaccesstoken.equals("")) {
					Intent intent = new Intent(getBaseContext(), com.webcamconsult.syl.gcm.NotificationReceiverActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("message", message);

					Log.e("start notification", "start notification");

					startActivity(intent);
					Log.e("start notification", "start notification");
				}



			}
			else {
				Log.e("app in background","app in background");
			}

		String sylaccesstoken=	SYLSaveValues.getSYLaccessToken(getApplicationContext());
			if(!sylaccesstoken.equals("")) {
				mNotificationManager.notify(notifyID, mBuilder.build());
			}




		}
		    catch(Exception e)
		    {
		    	Log.e("Error",e.getMessage());
		    }
	}



	public  void buildAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
												final int id) {

								dialog.cancel();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}


	class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Context... params) {
			final Context context = params[0].getApplicationContext();
			return isAppOnForeground(context);
		}

		private boolean isAppOnForeground(Context context) {
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
			if (appProcesses == null) {
				return false;
			}
			final String packageName = context.getPackageName();
			for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
	}




















}
	    
	    
	    
	    
	    


