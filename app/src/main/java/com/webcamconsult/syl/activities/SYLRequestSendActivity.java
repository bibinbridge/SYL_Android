package com.webcamconsult.syl.activities;

import android.R.integer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.webcamconsult.syl.R;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLCancelAppointmentListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.modelmanager.SYLCancelAppointmentModelManager;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLCancelAppointmentViewManager;

public class SYLRequestSendActivity extends Activity implements
		SYLCancelAppointmentListener {
	/** Called when the activity is first created. */
	TextView mRequestTypeTxview;
	Context mContext;
	TextView textview_prioritythree, textview_prefDateandtimethree,
			textview_name, textview_appointmentheading,
			textview_appointmentdetailsdescription;
	ImageView imageview_clientImage;
	EditText edittext_mobNo, edittext_skypeId, edittext_chooseContacts,
			edittext_priorityone, edittext_prioritytwo, edittext_choosedate,
			edittext_prefDateandtimeone, edittext_prefDateandtimetwo;
	EditText mSylConferenceEditxt,mFacetimeEdittext;
	EditText mHangoutEditText;
	RelativeLayout relativelay_contactsdropdown, relativelay_datedown;
	Boolean choosecontactcclicked = false;
	Boolean choosedatecclicked = false;
	Button btn_confirm, btn_cancel;
	String name;
	String profile_image;
	String topic;
	String description;
	String mobile;
	String skypeid;
	String priority1;
	String priority2;
	String priority3;
	String date1;
	String date2;
	String date3;
	String accesstoken, appointmentid;
	public ImageLoader imageLoader;
	ImageView img_backarrow;
	ProgressDialog msylProgressDialog;
	String participantid,hangoutid,facetimeid;
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








		setContentView(R.layout.requestsend_detail);


		mContext = this;
		imageLoader = new ImageLoader(mContext);
		Bundle extras = getIntent().getExtras();
		appointmentid = extras.getString("appointmentid");
		name = extras.getString("name");
		profile_image = extras.getString("profile_image");
		topic = extras.getString("topic");
		description = extras.getString("description");
		mobile = extras.getString("mobile");
		hangoutid= extras.getString("hangoutid");
		skypeid = extras.getString("skypeid");
		priority1 = extras.getString("priority1");
		priority2 = extras.getString("priority2");
		priority3 = extras.getString("priority3");
		facetimeid=extras.getString("facetimeid");
		date1 = extras.getString("date1");
	
		date2 = extras.getString("date2");
		date3 = extras.getString("date3");
		participantid =extras.getString("participantid");
		hangoutid=extras.getString("hangoutid");
		mRequestTypeTxview = (TextView) findViewById(R.id.txt_apptype);
		mRequestTypeTxview.setText("Request Sent");
		accesstoken = SYLSaveValues
				.getSYLaccessToken(SYLRequestSendActivity.this);
		initActivity();
	}

	private void initActivity() {
		img_backarrow = (ImageView) findViewById(R.id.img_backarrow);
		mHangoutEditText=(EditText)findViewById(R.id.etxt_googlehangout);
		mFacetimeEdittext=(EditText)findViewById(R.id.etxt_facetime);
		edittext_chooseContacts = (EditText) findViewById(R.id.etchooseContacts);
		mSylConferenceEditxt=(EditText)findViewById(R.id.etxt_sylconference);
		edittext_choosedate = (EditText) findViewById(R.id.etchoosedate);
		edittext_mobNo = (EditText) findViewById(R.id.etmobNo);
		edittext_skypeId = (EditText) findViewById(R.id.etskypeId);
		edittext_priorityone = (EditText) findViewById(R.id.etskypeiddetailed);
		edittext_prioritytwo = (EditText) findViewById(R.id.etmobilenodetailed);
		edittext_prefDateandtimeone = (EditText) findViewById(R.id.etprefDateandtimeone);
		edittext_prefDateandtimetwo = (EditText) findViewById(R.id.etprefDateandtimetwo);
		imageview_clientImage = (ImageView) findViewById(R.id.ImClientImage);
		relativelay_contactsdropdown = (RelativeLayout) findViewById(R.id.RlContactsDropdown);
		relativelay_datedown = (RelativeLayout) findViewById(R.id.RlDatedown);

		textview_prioritythree = (TextView) findViewById(R.id.tvopentokdetailed);
		textview_prefDateandtimethree = (TextView) findViewById(R.id.tvprefDateandtimethree);
		textview_name = (TextView) findViewById(R.id.tvname);
		textview_appointmentheading = (TextView) findViewById(R.id.tvAppointmentHeading);
		textview_appointmentdetailsdescription = (TextView) findViewById(R.id.tvAppointmentDetailsdescription);
		btn_confirm = (Button) findViewById(R.id.btnConfirm);
		btn_confirm.setText("Reschedule");
		btn_cancel = (Button) findViewById(R.id.btnCancel);
if(name.length()>16)
{
	name= name.substring(0, Math.min(name.length(), 12))+"...";
}
		textview_name.setText(name);
		textview_appointmentheading.setText(topic);
		textview_appointmentdetailsdescription.setText(Html.fromHtml("<html><strong>Description:</strong>" + description + "</html>"));
		edittext_mobNo.setText(mobile);
		if(!hangoutid.equals(""))
		{
			mHangoutEditText.setText(hangoutid);
		}
		else {
			mHangoutEditText.setText("Not available");
		}
		
		edittext_skypeId.setText(skypeid);
		mFacetimeEdittext.setText(facetimeid);
		edittext_priorityone.setText("priority 1 is " + " " + priority1);
		edittext_priorityone.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priority1), 0, 0, 0);
		edittext_prioritytwo.setText("priority 2 is " + " " + priority2);
		edittext_prioritytwo.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priority2), 0, 0, 0);
		textview_prioritythree.setText("priority 3 is " + " " + priority3);
		textview_prioritythree.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priority3), 0, 0, 0);
		edittext_prefDateandtimeone.setText("preferred date and time " + "\n"
				+ date1 + " ");
		if(date2.equals(" ")){
			edittext_prefDateandtimetwo.setText("option1 date and time " + "\n"
					+"not available");
		} else {
			edittext_prefDateandtimetwo.setText("option1 date and time " + "\n"
					+ date2 + " ");
		}
		if(date3.equals(" "))
		{
			textview_prefDateandtimethree.setText("option2 date and time " + "\n"
					+ "not available");
		}
		else
		{
			textview_prefDateandtimethree.setText("option2 date and time " + "\n"
					+ date3 + " ");
		}

		imageLoader.DisplayImage(profile_image, imageview_clientImage);
		img_backarrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				navigatetoAppointmentsListingScreen("false");
			}
		});
		edittext_chooseContacts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!choosecontactcclicked) {
					relativelay_contactsdropdown.setVisibility(View.VISIBLE);
					if(SYLUtil.isTablet(SYLRequestSendActivity.this))
					{
						edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.uparrow, 0);
					}
					else {

					edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.uparrow, 0);
					}

					choosecontactcclicked = true;
				} else {
					relativelay_contactsdropdown.setVisibility(View.GONE);
					if(SYLUtil.isTablet(SYLRequestSendActivity.this))
					{
						edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.downarrow, 0);
					}
					else {
						edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.downarrow, 0);
					}

					choosecontactcclicked = false;
				}

			}
		});

		edittext_choosedate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!choosedatecclicked) {
					relativelay_datedown.setVisibility(View.VISIBLE);
					if(SYLUtil.isTablet(SYLRequestSendActivity.this))
					{
						edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.uparrow, 0);
					}
					else{
						edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.uparrow, 0);
					}

					choosedatecclicked = true;
				} else {
					relativelay_datedown.setVisibility(View.GONE);
					if(SYLUtil.isTablet(SYLRequestSendActivity.this))
					{
						edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.downarrow, 0);
					}
					else{
						edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.downarrow, 0);
					}

					choosedatecclicked = false;
				}

			}
		});
		if(skypeid.equals("not available"))
		{
			if(SYLUtil.isTablet(SYLRequestSendActivity.this)) {
				edittext_skypeId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_skypeicon, 0,0, 0);
			}
			else {
				edittext_skypeId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.skypeicon2, 0, 0, 0);
			}
		}
		if(mobile.equals("not available"))
		{
			if(SYLUtil.isTablet(SYLRequestSendActivity.this)) {
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
				if(!edittext_mobNo.getText().toString().equals("not available"))
				calltoMobilenumber(edittext_mobNo.getText().toString());
			}
		});
		mSylConferenceEditxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			}
		});
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SYLRequestSendActivity.this,
						SYLRescheduleappointmentActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("appointmentid", appointmentid);
				intent.putExtra("topic", topic);
				intent.putExtra("description", description);
				intent.putExtra("skypeid", skypeid);
				intent.putExtra("mobile", mobile);
				intent.putExtra("profile_image", profile_image);
				intent.putExtra("priority1", priority1);
				if(priority2.equals("not available")){

					intent.putExtra("priority2", "Choose priority 2");
				}
				else {
					intent.putExtra("priority2", priority2);
				}
				if(priority3.equals("not available")){

					intent.putExtra("priority3", "Choose priority 3");
				}
				else {
					intent.putExtra("priority3", priority3);
				}

				intent.putExtra("date1", date1);
			
				intent.putExtra("date2", date2);
				intent.putExtra("date3", date3);
				intent.putExtra("hangoutid", hangoutid);
				intent.putExtra("facetimeid", facetimeid);
				intent.putExtra("participantid", participantid);
				intent.putExtra("intentfrom", "requestsend");
				intent.putExtra("intentname", getIntent().getStringExtra("intentfrom"));
				startActivity(intent);
				finish();
			}
		});
		edittext_priorityone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// edittext_chooseContacts.setText("Skype");
				// Drawable img =
				// getResources().getDrawable(R.drawable.skypeicon);
				// edittext_chooseContacts
				// .setCompoundDrawablesWithIntrinsicBounds(img, null,
				// null, null);
				// relativelay_contactsdropdown.setVisibility(View.GONE);
				// choosecontactcclicked = false;
			}

		});
		edittext_prioritytwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// edittext_chooseContacts.setText("Mobile");
				// Drawable left_img = getResources().getDrawable(
				// R.drawable.mobileicon);
				//
				// edittext_chooseContacts
				// .setCompoundDrawablesWithIntrinsicBounds(left_img,
				// null, null, null);
				// relativelay_contactsdropdown.setVisibility(View.GONE);
				// choosecontactcclicked = false;
			}
		});
		textview_prioritythree.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// edittext_chooseContacts.setText("Opentok");
				// Drawable left_img = getResources().getDrawable(
				// R.drawable.mobileicon);
				//
				// edittext_chooseContacts
				// .setCompoundDrawablesWithIntrinsicBounds(left_img,
				// null, null, null);
				// relativelay_contactsdropdown.setVisibility(View.GONE);
				// choosecontactcclicked = false;
			}
		});
		edittext_prefDateandtimeone
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Drawable left_img = getResources().getDrawable(
						// R.drawable.dateandtimeicon);
						// edittext_choosedate.setText(date1);
						// edittext_choosedate
						// .setCompoundDrawablesWithIntrinsicBounds(
						// left_img, null, null, null);
						// relativelay_datedown.setVisibility(View.GONE);
						// choosedatecclicked = false;
					}
				});
		edittext_prefDateandtimetwo
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Drawable left_img = getResources().getDrawable(
						// R.drawable.dateandtimeicon);
						// edittext_choosedate.setText(date2);
						// edittext_choosedate
						// .setCompoundDrawablesWithIntrinsicBounds(
						// left_img, null, null, null);
						// relativelay_datedown.setVisibility(View.GONE);
						// choosedatecclicked = false;
					}
				});
		textview_prefDateandtimethree
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Drawable left_img = getResources().getDrawable(
						// R.drawable.dateandtimeicon);
						// edittext_choosedate.setText(date3);
						// edittext_choosedate
						// .setCompoundDrawablesWithIntrinsicBounds(
						// left_img, null, null, null);
						// relativelay_datedown.setVisibility(View.GONE);
						// choosedatecclicked = false;
					}
				});
	}

	private void navigatetoAppointmentsListingScreen(String refreshvalue) {
		if(getIntent().getStringExtra("intentfrom").equals("futureappointment")) {
			Intent intent = new Intent(SYLRequestSendActivity.this,
					SYLFragmentChangeActivity.class);
			intent.putExtra("fragmentvalue", "newappointment");
			intent.putExtra("needrefresh", refreshvalue);
			startActivity(intent);
			finish();
		}
		else {
			Intent intent = new Intent(SYLRequestSendActivity.this,
					SYLHistoryAppointmentsActivity.class);

			startActivity(intent);
			finish();
		}
	}

	public void cancelAppointment(View v) {
		if (SYLUtil.isNetworkAvailable(SYLRequestSendActivity.this)) {
			msylProgressDialog = new ProgressDialog(SYLRequestSendActivity.this);
			msylProgressDialog.setMessage("Please wait...");
			msylProgressDialog.show();
			String timezone=SYLUtil.getTimeGMTZone(SYLRequestSendActivity.this);
			SYLCancelAppointmentViewManager mViewmanager = new SYLCancelAppointmentViewManager();
			mViewmanager.mCancelAppointmentListener = SYLRequestSendActivity.this;
			mViewmanager.doCancelAppointment(accesstoken, appointmentid, timezone);
		} else {
			SYLUtil.buildAlertMessage(SYLRequestSendActivity.this,
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
				showAlertMessage(SYLRequestSendActivity.this,"Appointment has been cancelled successfully");
				} else {
					SYLUtil.buildAlertMessage(SYLRequestSendActivity.this,
							mcancelappointmentmodel.getError().getErrorDetail());
				}
			} else {
				SYLUtil.buildAlertMessage(SYLRequestSendActivity.this,
						"Unexpected server error happened");
			}
		} catch (Exception e) {
			SYLUtil.buildAlertMessage(SYLRequestSendActivity.this,
					"Unexpected server error happened");
		}
	}
	private void calltoMobilenumber(String mobilenumber)
	{
		Intent intent = new Intent(Intent.ACTION_CALL);

		intent.setData(Uri.parse("tel:" + mobilenumber));
		startActivity(intent);
	}
	private void callviaSkype(String skypeid){
		
if(	SYLUtil.appInstalledOrNot("com.skype.raider", SYLRequestSendActivity.this))
{
	Intent skypeVideo = new Intent("android.intent.action.VIEW");
	skypeVideo.setData(Uri.parse("skype:" + skypeid + "?call&video=true"));
	startActivity(skypeVideo);
}
else {
	SYLUtil.buildAlertMessage(SYLRequestSendActivity.this,"Please install Skype to enable this functionality");
}
	}


	private void showAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
												final int id) {

								dialog.cancel();
							//	navigatetoAppointmentsListingScreen("true");
								navigateToFutureAppointmentListingScreen();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}


	private int	setImageforPriorityone(String priority)
	{
		int imgResource=R.drawable.facetime_icon2;;
		if(SYLUtil.isTablet(SYLRequestSendActivity.this)) {
			imgResource = R.drawable.tablet_chooseicon;
		}
		else {
			imgResource = R.drawable.chooseicon;
		}

		switch (priority) {
			case "Skype":
				if(SYLUtil.isTablet(SYLRequestSendActivity.this))
				{
					imgResource =R.drawable.tablet_skypeicon;
				}
				else {
					imgResource =R.drawable.skypeicon2;
				}

				break;
			case "Mobile":

				if(SYLUtil.isTablet(SYLRequestSendActivity.this))
				{
					imgResource=	R.drawable.tablet_mobileicon;
				}
				else {
					imgResource=	R.drawable.mobileicon2;
				}


				break;
			case "SYL Videocall(in beta)":

				if(SYLUtil.isTablet(SYLRequestSendActivity.this)) {
					imgResource=	R.drawable.tablet_opentokicon;
				}
				else {
					imgResource=	R.drawable.opentok_icon2;
				}


				break;
			case "Hangout":

				if(SYLUtil.isTablet(SYLRequestSendActivity.this))
				{
					imgResource=R.drawable.tablet_hangouticon;
				}
				else {
					imgResource=R.drawable.googlehangouticon;
				}
				break;
			case "Facetime":
				if(SYLUtil.isTablet(SYLRequestSendActivity.this))
				{
					imgResource=R.drawable.tablet_facetimeicon;
				}
				else {
					imgResource=R.drawable.facetime_icon2;
				}

				break;
			default:
				break;
		}
		return imgResource;
	}

	private void navigateToFutureAppointmentListingScreen()
	{
		Intent intent = new Intent(SYLRequestSendActivity.this,
				SYLFragmentChangeActivity.class);
		intent.putExtra("fragmentvalue", "newappointment");
		intent.putExtra("needrefresh", "true");
		startActivity(intent);
		finish();
	}
	
	
	
}