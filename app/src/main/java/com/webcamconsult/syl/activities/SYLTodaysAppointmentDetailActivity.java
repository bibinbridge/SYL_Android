package com.webcamconsult.syl.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLCancelAppointmentListener;
import com.webcamconsult.syl.interfaces.SYLFetchActiveSessionListener;
import com.webcamconsult.syl.interfaces.SYLOpentokconfigureListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.model.SYLFetchActiveSessionResponeModel;
import com.webcamconsult.syl.model.SYLOpentokconfigureResponse;
import com.webcamconsult.syl.opentok.send.HelloWorldActivitySender;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLCancelAppointmentViewManager;
import com.webcamconsult.viewmanager.SYLFetchActiveSessionViewManager;
import com.webcamconsult.viewmanager.SYLOpentokConfigureViewManager;

public class SYLTodaysAppointmentDetailActivity extends Activity implements SYLCancelAppointmentListener,SYLOpentokconfigureListener, SYLFetchActiveSessionListener

{
	TextView textview_name, textview_appointmenttopic,
			textview_appointmentdetailsdescription, textview_prioritydate;
	ImageView imageview_clientImage;
	EditText edittext_mobNo, edittext_skypeId, edittext_priorityone;
	TextView txtViewAppointmentType;
	public ImageLoader imageLoader;
String intentfrom;
	Button btn_reshedule, btn_cancel;
	Context mContext;

	String name;
	String profile_image;
	String topic;
	String description;
	String mobile;
	String skypeid,hangoutid,facetimeid;
	String priority1;
	String priority2;
	String priority3;
	String date1;
	String date2;
	String date3;
	String accesstoken;
String appointmentid;

	ImageView img_backarrow;
	TextView mtxtview_appointmentheading, txtview_heading;
	ProgressDialog msylProgressDialog;
String 	participantid;
EditText	mEditTextsylConference,mEditTexthangoutid,mEdittextFacetimeid;
	BroadcastReceiver br;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		registerReceiver(
				br, intentFilter);


		setContentView(R.layout.confirmedappointmentdetails);

		mContext = this;
		imageLoader = new ImageLoader(mContext);

			Bundle extras = getIntent().getExtras();

				// userid= extras.getInt("id", 0);
				// mobilenumber= extras.getString("mobilenumber");
				name = extras.getString("name");
		if(name.length()>16)
		{
			name= name.substring(0, Math.min(name.length(), 12))+"...";
		}
				profile_image = extras.getString("profile_image");
				topic = extras.getString("topic");
				description = extras.getString("description");
				mobile = extras.getString("mobile");
				skypeid = extras.getString("skypeid");
		hangoutid=extras.getString("hangoutid");
		facetimeid=extras.getString("facetimeid");
	/*Getting the values*/
appointmentid= extras.getString("appointmentid");
			
				participantid =extras.getString("participantid");
				priority1 = extras.getString("priority1");
				priority2 = extras.getString("priority2");
				priority3 = extras.getString("priority3");
				date1 = extras.getString("date1");
			
				date2 = extras.getString("date2");
				date3 = extras.getString("date3");
				participantid =extras.getString("participantid");
				
				
				initActivity();
				
			
		
		// else {
		// userid= (int) savedInstanceState.getInt("id", 0);
		// mobilenumber=(String)
		// savedInstanceState.getSerializable("mobilenumber");
		// }


	}

	private void initActivity() {
		accesstoken=SYLSaveValues.getSYLaccessToken(SYLTodaysAppointmentDetailActivity.this);
		txtview_heading = (TextView) findViewById(R.id.txt_apptype);
		txtview_heading.setText("Today's Appointments");
		mtxtview_appointmentheading = (TextView) findViewById(R.id.tvAppointmentHeading);
		mEditTextsylConference=(EditText)findViewById(R.id.etxt_sylconference);
		edittext_mobNo = (EditText) findViewById(R.id.etmobNo);
		edittext_skypeId = (EditText) findViewById(R.id.etskypeId);
mEditTexthangoutid=(EditText)findViewById(R.id.etxt_googlehangout);
		mEdittextFacetimeid=(EditText)findViewById(R.id.etxt_facetimeid);
		edittext_priorityone = (EditText) findViewById(R.id.etpriorityone);
		img_backarrow = (ImageView) findViewById(R.id.img_backarrow);
		textview_name = (TextView) findViewById(R.id.tvname);
		textview_appointmenttopic = (TextView) findViewById(R.id.tvAppointmentTopic);
		textview_appointmentdetailsdescription = (TextView) findViewById(R.id.tvAppointmentDetailsdescription);
		textview_prioritydate = (TextView) findViewById(R.id.tvprioritydate);

		btn_reshedule = (Button) findViewById(R.id.btnReshedule);
		btn_cancel = (Button) findViewById(R.id.btnCancel);

		imageview_clientImage = (ImageView) findViewById(R.id.ImClientImage);

		textview_name.setText(name);
		mtxtview_appointmentheading.setText(topic);
		textview_appointmentdetailsdescription.setText(Html.fromHtml("<html><strong>Description:</strong>" + description + "</html>"));
		edittext_mobNo.setText(mobile);
		edittext_skypeId.setText(skypeid);
		mEditTexthangoutid.setText(hangoutid);
		mEdittextFacetimeid.setText(facetimeid);
		edittext_priorityone.setText("priority 1 is " + " " + priority1);
		setImageforPriorityone(priority1);
		textview_prioritydate.setText("preferred date and time " + "\n" + date1
		);

		imageLoader.DisplayImage(profile_image, imageview_clientImage);
		img_backarrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				navigatetoAppointmentsListingScreen("false");
			}
		});


		mEditTextsylConference.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doFetchActivesessionWebapicall();
			}
		});
		if(skypeid.equals("not available"))
		{
			if(SYLUtil.isTablet(SYLTodaysAppointmentDetailActivity.this)) {
				edittext_skypeId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_skypeicon, 0,0, 0);
			}
			else {
				edittext_skypeId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.skypeicon2, 0, 0, 0);
			}
		}
		if(mobile.equals("not available"))
		{
			if(SYLUtil.isTablet(SYLTodaysAppointmentDetailActivity.this)) {
				edittext_mobNo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_mobileicon, 0,0, 0);
			}
			else {
				edittext_mobNo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobileicon2, 0, 0, 0);
			}
		}



		edittext_skypeId.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!skypeid.equals("not available"))
					callviaSkype(edittext_skypeId.getText().toString());
			}
		});
		edittext_mobNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("clicked mobile number", "mobile number");
				if(!edittext_mobNo.getText().toString().equals("not available")) {
					calltoMobilenumber(edittext_mobNo.getText().toString());
				}
			}
		});


	}
	private void callviaSkype(String skypeid){

		if(	SYLUtil.appInstalledOrNot("com.skype.raider", SYLTodaysAppointmentDetailActivity.this))
		{
			Intent skypeVideo = new Intent("android.intent.action.VIEW");
			skypeVideo.setData(Uri.parse("skype:" + skypeid + "?call&video=true"));
			startActivity(skypeVideo);
		}
		else {
			SYLUtil.buildAlertMessage(SYLTodaysAppointmentDetailActivity.this, "Please install Skype to enable this functionality");
		}
	}
	private void calltoMobilenumber(String mobilenumber)
	{
		Intent intent = new Intent(Intent.ACTION_CALL);

		intent.setData(Uri.parse("tel:" + mobilenumber));
		startActivity(intent);
	}
	private void doFetchActivesessionWebapicall()
	{

		if (SYLUtil.isNetworkAvailable(SYLTodaysAppointmentDetailActivity.this)) {
			msylProgressDialog = new ProgressDialog(SYLTodaysAppointmentDetailActivity.this);
			msylProgressDialog.setMessage("Please wait...");
			msylProgressDialog.setCancelable(false);
			msylProgressDialog.setCanceledOnTouchOutside(false);
			msylProgressDialog.show();


			String timezone=SYLUtil.getTimeGMTZone(SYLTodaysAppointmentDetailActivity.this);

			SYLFetchActiveSessionViewManager mFetchactivesessionviewmanager=new SYLFetchActiveSessionViewManager();
			mFetchactivesessionviewmanager.mActiveSessionListener=SYLTodaysAppointmentDetailActivity.this;
			mFetchactivesessionviewmanager.fetchActiveSession(appointmentid, accesstoken, timezone);
		}


	}
	protected void SuccessAlert(Context context, String title, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@SuppressLint("NewApi")
							public void onClick(final DialogInterface dialog,
												final int id) {

								// Intent intent = new Intent
								// (SYLConfirmedRequestAppointmentActivity.this,SYLAppointmentsFragment.class);
								// startActivity(intent);

								// FragmentTransaction ft =
								// getFragmentManager().beginTransaction();
								// SYLAppointmentsFragment
								// sylappointmentsfragment = new
								// SYLAppointmentsFragment();
								// ft.replace(R.id.content_frame,
								// sylappointmentsfragment);
								// ft.commit();

								dialog.cancel();
							}
						});

		builder.setNegativeButton(R.string.Cancel,
				new DialogInterface.OnClickListener() {
					@SuppressLint("NewApi")
					public void onClick(final DialogInterface dialog,
										final int id) {

						dialog.cancel();
					}
				});

		final AlertDialog alert = builder.create();
		alert.show();

	}

	public void navigatetoReschuduleScreen(View v) {
		Intent rescheduleintent = new Intent(
				SYLTodaysAppointmentDetailActivity.this,
				SYLRescheduleappointmentActivity.class);
		rescheduleintent.putExtra("name", name);
		rescheduleintent.putExtra("appointmentid", appointmentid);
		rescheduleintent.putExtra("topic", topic);
		rescheduleintent.putExtra("description", description);
		rescheduleintent.putExtra("profile_image", profile_image);
	
		rescheduleintent.putExtra("priority1", priority1);
		if(priority2.equals("not available")){

			rescheduleintent.putExtra("priority2", "Choose priority 2");
		}
		else {
			rescheduleintent.putExtra("priority2", priority2);
		}
		if(priority3.equals("not available")){

			rescheduleintent.putExtra("priority3", "Choose priority 3");
		}
		else {
			rescheduleintent.putExtra("priority3", priority3);
		}
			rescheduleintent.putExtra("date1", date1);
		rescheduleintent.putExtra("hangoutid", hangoutid);
		rescheduleintent.putExtra("facetimeid", facetimeid);
		rescheduleintent.putExtra("skypeid",skypeid);
		rescheduleintent.putExtra("mobile",mobile);
		rescheduleintent.putExtra("date2", date2);
		rescheduleintent.putExtra("date3", date3);
		rescheduleintent.putExtra("participantid", participantid);

		rescheduleintent.putExtra("intentfrom", "todaysappointment");
		rescheduleintent.putExtra("intentname", getIntent().getStringExtra("intentfrom"));
		startActivity(rescheduleintent);
		finish();
	}

	private void navigatetoAppointmentsListingScreen(String refreshvalue) {
if(getIntent().getStringExtra("intentfrom").equals("futureappointment")) {
	Intent intent = new Intent(SYLTodaysAppointmentDetailActivity.this,
			SYLFragmentChangeActivity.class);
	intent.putExtra("fragmentvalue", "newappointment");
	intent.putExtra("needrefresh", refreshvalue);
	startActivity(intent);
	finish();
}
		else {
	Intent intent = new Intent(SYLTodaysAppointmentDetailActivity.this,
			SYLHistoryAppointmentsActivity.class);

	startActivity(intent);
	finish();
}
	}
	private void navigateToFutureAppointmentListingScreen()
	{
		Intent intent = new Intent(SYLTodaysAppointmentDetailActivity.this,
				SYLFragmentChangeActivity.class);
		intent.putExtra("fragmentvalue", "newappointment");
		intent.putExtra("needrefresh", "true");
		startActivity(intent);
		finish();
	}
public void	cancelAppointment(View v)
{
	if (SYLUtil.isNetworkAvailable(SYLTodaysAppointmentDetailActivity.this)) {
		msylProgressDialog = new ProgressDialog(SYLTodaysAppointmentDetailActivity.this);
		msylProgressDialog.setMessage("Please wait...");
		msylProgressDialog.setCancelable(false);
		msylProgressDialog.setCanceledOnTouchOutside(false);
		msylProgressDialog.show();
		String timezone=SYLUtil.getTimeGMTZone(SYLTodaysAppointmentDetailActivity.this);
		SYLCancelAppointmentViewManager mViewmanager = new SYLCancelAppointmentViewManager();
		mViewmanager.mCancelAppointmentListener = SYLTodaysAppointmentDetailActivity.this;
		mViewmanager.doCancelAppointment(accesstoken, appointmentid,timezone);
	} else {
		SYLUtil.buildAlertMessage(SYLTodaysAppointmentDetailActivity.this,
				getString(R.string.network_alertmessage));
	}

}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	navigatetoAppointmentsListingScreen("false");
	}

	@Override
	public void finishCancelAppointment(
			SYLCancelAppointmentModel mcancelappointmentmodel) {
		// TODO Auto-generated method stub
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}

		try {
			if (mcancelappointmentmodel != null) {
				if (mcancelappointmentmodel.isSuccess()) {
					buildAlertMessage(SYLTodaysAppointmentDetailActivity.this,
							"Cancelled the appointment successfully");
				} else {
					SYLUtil.buildAlertMessage(SYLTodaysAppointmentDetailActivity.this,
							mcancelappointmentmodel.getError().getErrorDetail());
				}
			} else {
				SYLUtil.buildAlertMessage(SYLTodaysAppointmentDetailActivity.this,
						"Unexpected server error happened");
			}
		} catch (Exception e) {
			SYLUtil.buildAlertMessage(SYLTodaysAppointmentDetailActivity.this,
					"Unexpected server error happened");
		}
	}

	private void startOpentokscreen(String sessionid,String token,String apikey) {

		Log.i("LOGTAG", "starting hello-world app with UI");

		Intent intent = new Intent(SYLTodaysAppointmentDetailActivity.this,HelloWorldActivitySender.class);

		intent.putExtra("sessionid", sessionid);
		intent.putExtra("token", token);
		intent.putExtra("apikey", apikey);
		intent.putExtra("appointmentid", appointmentid);
		intent.putExtra("profileimage_url", profile_image);
		intent.putExtra("name", name);
		intent.putExtra("intentfrom","todaysappointment");
		intent.putExtra("intentname",getIntent().getStringExtra("intentfrom"));
		startActivity(intent);
finish();

	}
	@Override
	public void fetchActiveSessionFinish(SYLFetchActiveSessionResponeModel mSessionResponseModel) {
		try {

			if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
				msylProgressDialog.dismiss();
			}
			if (mSessionResponseModel != null) {
				if (mSessionResponseModel.isSuccess()) {
					if (mSessionResponseModel.isActiveSessionPresent()) {
						String sessionid = mSessionResponseModel.getConferenceResponse().getOpentokSessionId();
						String token = mSessionResponseModel.getConferenceResponse().getPublisherToken();
						String apikey = mSessionResponseModel.getConferenceResponse().getApiKey();
						startOpentokscreen(sessionid, token, apikey);
					} else {
						doopentokWebapicall();
					}
				} else {
					SYLUtil.buildAlertMessage(SYLTodaysAppointmentDetailActivity.this, mSessionResponseModel.getError().getErrorDetail());
				}
			}
		}
		catch (Exception e){

		}
	}

	@Override
	public void opentokconfigureFinish(SYLOpentokconfigureResponse mopentokConfigureresponse) {
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		if(mopentokConfigureresponse!=null){

			if(mopentokConfigureresponse.isSuccess())
			{
				String sessionid=mopentokConfigureresponse.getConferenceResponse().getOpentokSessionId();
				String token=mopentokConfigureresponse.getConferenceResponse().getPublisherToken();
				String apikey=mopentokConfigureresponse.getConferenceResponse().getApiKey();
				startOpentokscreen(sessionid, token, apikey);
			}
			else {
				SYLUtil.buildAlertMessage(SYLTodaysAppointmentDetailActivity.this,mopentokConfigureresponse.getError().getErrorCode());
			}
		}
	}
	private void doopentokWebapicall()
	{



		if (SYLUtil.isNetworkAvailable(SYLTodaysAppointmentDetailActivity.this)) {
			msylProgressDialog = new ProgressDialog(SYLTodaysAppointmentDetailActivity.this);
			msylProgressDialog.setMessage("Please wait...");
			msylProgressDialog.setCancelable(false);
			msylProgressDialog.setCanceledOnTouchOutside(false);
			msylProgressDialog.show();

			String userid=SYLSaveValues.getSYLUserID(SYLTodaysAppointmentDetailActivity.this);
			String timezone=SYLUtil.getTimeGMTZone(SYLTodaysAppointmentDetailActivity.this);

			SYLOpentokConfigureViewManager mopentokviewmanager=new SYLOpentokConfigureViewManager();
			mopentokviewmanager.mOpentokconfigurelistener=SYLTodaysAppointmentDetailActivity.this;
			mopentokviewmanager.configureOpentok(appointmentid, userid, accesstoken, timezone);
		}


	}

	private void  buildAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
												final int id) {
//navigatetoAppointmentsListingScreen("true");
								navigateToFutureAppointmentListingScreen();
								dialog.cancel();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(br);
	}

	private void	setImageforPriorityone(String priority)
	{
		int imgResource=R.drawable.facetime_icon;


		switch (priority) {
			case "Skype":
				if(SYLUtil.isTablet(SYLTodaysAppointmentDetailActivity.this))
				{
					imgResource =R.drawable.tablet_skypeicon;
				}
				else {
					imgResource =R.drawable.skypeicon2;
				}

				break;
			case "Mobile":
				if(SYLUtil.isTablet(SYLTodaysAppointmentDetailActivity.this))
				{
					imgResource =R.drawable.tablet_mobileicon;
				}
				else {
					imgResource =R.drawable.mobileicon2;
				}
				break;
			case "SYL Videocall(in beta)":
				if(SYLUtil.isTablet(SYLTodaysAppointmentDetailActivity.this))
				{
					imgResource=	R.drawable.tablet_opentokicon;
				}
				else {
					imgResource=	R.drawable.opentok_icon2;
				}

				break;
			case "Hangout":
if(SYLUtil.isTablet(SYLTodaysAppointmentDetailActivity.this))
{
	imgResource=R.drawable.tablet_hangouticon;
}
				else {
	imgResource=R.drawable.googlehangouticon;
}




				break;
			case "Facetime":
				if(SYLUtil.isTablet(SYLTodaysAppointmentDetailActivity.this)) {
					imgResource=R.drawable.tablet_facetimeicon;
				}
				else{
					imgResource=R.drawable.facetime_icon2;
				}

				break;
			default:
				break;
		}
		edittext_priorityone.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
	}
}
