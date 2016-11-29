package com.webcamconsult.syl.activities;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.interfaces.SYLDeviceRegisterListener;
import com.webcamconsult.syl.model.SYLDeviceRegisterResponseModel;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLApplicationConstants;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLRegisterDeviceViewManager;

import io.fabric.sdk.android.Fabric;

public class SYLSplashScreenActivity extends FragmentActivity  {
	public static final String TAG = "SYLSplashScreenActivity";
	private final int SPLASH_DISPLAY_LENGTH = 1000;
	private TextView txtview_versionname;
	GoogleCloudMessaging gcmObj;
	Context applicationContext;
	String regId = "";
	String appname,appversion,deviceUID,deviceName,devicemodel,deviceversion,devicetype,GMTTime;
	boolean pushbadge,pushalert,pushsound;
	boolean internertflag=true;
	boolean netflag;
	boolean gcmregidflag=false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.syl_splashscreenlayout);

		appname="SeeYouLater";
		appversion="1.1";
		deviceUID = SYLUtil.getUID(getBaseContext());
		deviceName=android.os.Build.MANUFACTURER;
		devicemodel = android.os.Build.MODEL;
	    deviceversion = android.os.Build.VERSION.RELEASE;
	    devicetype="ANDROID";
	    pushbadge=true;
	    pushalert=true;
	    pushsound=true;
		applicationContext = getApplicationContext();
		GMTTime=SYLUtil.getTimeGMTZone(SYLSplashScreenActivity.this);
netflag=SYLUtil.isNetworkAvailable(SYLSplashScreenActivity.this);
if(netflag)
{
	internertflag=true;
	if((SYLSaveValues.getGCMRegistrationid(SYLSplashScreenActivity.this).equals(""))) {
		gcmregidflag = false;
		getRegistrationId();
	}

	}
else {
	if(!(SYLSaveValues.getGCMRegistrationid(SYLSplashScreenActivity.this).equals("")) )
	{
		gcmregidflag=true;
	}
	else{
		SYLUtil.buildAlertMessage(SYLSplashScreenActivity.this, "SYL needs the internet connection for running. Kindly please check your network connection");
	}
		internertflag=false;
}
	
		txtview_versionname = (TextView) findViewById(R.id.version_name);

		Typeface titilefont = Typeface.createFromAsset(this.getAssets(),
				"fonts/myriadproregular.OTF");// Setting the font for the
												// TextView

		txtview_versionname.setTypeface(titilefont);
		if ((icicle == null)) {
			Log.e("not null", "not null");
			/*
			 * This method is for showing the splash screen after a specific
			 * time delay
			 */
			if(internertflag || gcmregidflag) {
				handlerAction();
			}
		}

	}

	private void handlerAction() {
		new Handler().postDelayed(new Runnable() {
			// @Override
			public void run() {
				doStartScreenAction();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

	/* method for starting signin screen */
	private void doStartScreenAction() {
		
		Intent mainIntent=null;
		if(checkautoLogin()){

			mainIntent = new Intent(SYLSplashScreenActivity.this,
					SYLFragmentChangeActivity.class);
			mainIntent.putExtra("fragmentvalue", "newappointment");
			mainIntent.putExtra("needrefresh", "true");

	 }
		else {
			mainIntent = new Intent(SYLSplashScreenActivity.this,
					SYLSigninActivity.class);
			mainIntent.putExtra("finish", true);
		}
		SYLSplashScreenActivity.this.startActivity(mainIntent);
		SYLSplashScreenActivity.this.finish();
	}
	private void getRegistrationId() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcmObj == null) {
						gcmObj = GoogleCloudMessaging
								.getInstance(applicationContext);
					}
					regId = gcmObj
							.register(SYLApplicationConstants.GOOGLE_PROJ_ID);
					msg = "Registration ID :" + regId;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return regId;
			}

			@Override
			protected void onPostExecute(String msg) {
				if (!TextUtils.isEmpty(regId)) {
					// Store RegId created by GCM Server in SharedPref
					// storeRegIdinSharedPref(applicationContext, regId,
					// emailID);
//					Toast.makeText(
//							applicationContext,
//							"Registered with GCM Server successfully.\n\n"
//									+ msg, Toast.LENGTH_SHORT).show();
					Log.e("registration ID", msg);
					SYLSaveValues.setGCMRegistrationid(msg, SYLSplashScreenActivity.this);
					sendRegidtoServer(msg);
				} else {
					Toast.makeText(
							applicationContext,
							"Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
									+ msg, Toast.LENGTH_LONG).show();
				}
			}
		}.execute(null, null, null);
	}
	
	public void sendRegidtoServer(String regid)
	{
		SYLRegisterDeviceViewManager mviewmanager=new SYLRegisterDeviceViewManager();
	
		mviewmanager.sendregidtoServer(appname,appversion,deviceUID,regid,deviceName,devicemodel,deviceversion,pushbadge,pushalert,pushsound,devicetype,GMTTime);
	}

private boolean checkautoLogin()
{
	String sylfirsttimelogin=SYLSaveValues.getSYLFirsttimeLogin(SYLSplashScreenActivity.this);
	if(sylfirsttimelogin.equals("signedin"))
	{
		return true;
	}
	else {
		return false;
	}
}
}
