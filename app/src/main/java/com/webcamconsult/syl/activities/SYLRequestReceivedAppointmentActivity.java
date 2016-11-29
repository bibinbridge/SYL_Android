package com.webcamconsult.syl.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLCancelAppointmentListener;
import com.webcamconsult.syl.interfaces.SYLConfirmAppointmentListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.model.SYLConfirmRequestResponseModel;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLCancelAppointmentViewManager;
import com.webcamconsult.viewmanager.SYLConfirmedAppointmentViewManager;

public class SYLRequestReceivedAppointmentActivity extends Activity implements SYLConfirmAppointmentListener,SYLCancelAppointmentListener{

	Context mContext;
	TextView textview_prioritythree, textview_prefDateandtimethree,
			textview_name, textview_appointmenttopic,
			textview_appointmentdetailsdescription;
	ImageView imageview_clientImage;
	EditText edittext_mobNo, edittext_skypeId, edittext_chooseContacts,
			edittext_priorityone, edittext_prioritytwo, edittext_choosedate,
			edittext_prefDateandtimeone, edittext_prefDateandtimetwo;
	EditText mHangoutidEditText,mFacetimeidEditText;
	RelativeLayout relativelay_contactsdropdown, relativelay_datedown;
	Boolean choosecontactcclicked = false;
	Boolean choosedatecclicked = false;
	Button btn_confirm, btn_cancel;
	public ImageLoader imageLoader;
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

	TextView mtxtviewAppointmenttype;
	ImageView img_backarrow;
	String accesstoken;
String appointmentid,hangoutid,facetimeid;
ProgressDialog msylProgressDialog;
String participantid;
	BroadcastReceiver br;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*We need to finish this activity when logout button press. This activity will be in active state when notification comes*/
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




		setContentView(R.layout.appointmentsdetails);
		accesstoken=SYLSaveValues.getSYLaccessToken(SYLRequestReceivedAppointmentActivity.this);
		mContext = this;
		imageLoader = new ImageLoader(mContext);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {

				name = null;
				profile_image = null;
				topic = null;
				description = null;
				mobile = null;
				skypeid = null;
				priority1 = null;
				priority2 = null;
				priority3 = null;
				date1 = null;
				date2 = null;
				date3 = null;
			} else {
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
				priority1 = extras.getString("priority1");
				priority2 = extras.getString("priority2");
				priority3 = extras.getString("priority3");
				date1 = extras.getString("date1");
				date2 = extras.getString("date2");
				date3 = extras.getString("date3");
			appointmentid=	 extras.getString("appointmentid");
		hangoutid= extras.getString("hangoutid");
				facetimeid=extras.getString("facetimeid");
			
			}
		}
		// else {
		// userid= (int) savedInstanceState.getInt("id", 0);
		// mobilenumber=(String)
		// savedInstanceState.getSerializable("mobilenumber");
		// }

		initActivity();
	}

	private void initActivity() {

		edittext_chooseContacts = (EditText) findViewById(R.id.etchooseContacts);
		edittext_chooseContacts.setText("choose one contact");
		edittext_choosedate = (EditText) findViewById(R.id.etchoosedate);
		edittext_mobNo = (EditText) findViewById(R.id.etmobNo);
		edittext_skypeId = (EditText) findViewById(R.id.etskypeId);
		mHangoutidEditText=(EditText)findViewById(R.id.etxt_googlehangout);
		mFacetimeidEditText=(EditText)findViewById(R.id.etxt_facetimeid);
		edittext_priorityone = (EditText) findViewById(R.id.etskypeiddetailed);
		edittext_prioritytwo = (EditText) findViewById(R.id.etmobilenodetailed);
		edittext_prefDateandtimeone = (EditText) findViewById(R.id.etprefDateandtimeone);
		edittext_prefDateandtimetwo = (EditText) findViewById(R.id.etprefDateandtimetwo);
		imageview_clientImage = (ImageView) findViewById(R.id.ImClientImage);
		relativelay_contactsdropdown = (RelativeLayout) findViewById(R.id.RlContactsDropdown);
		relativelay_datedown = (RelativeLayout) findViewById(R.id.RlDatedown);
		mtxtviewAppointmenttype = (TextView) findViewById(R.id.txt_apptype);
		textview_prioritythree = (TextView) findViewById(R.id.tvopentokdetailed);
		textview_prefDateandtimethree = (TextView) findViewById(R.id.tvprefDateandtimethree);
		textview_name = (TextView) findViewById(R.id.tvname);
		textview_appointmenttopic = (TextView) findViewById(R.id.tvAppointmentHeading);
		textview_appointmentdetailsdescription = (TextView) findViewById(R.id.tvAppointmentDetailsdescription);
		btn_confirm = (Button) findViewById(R.id.btnConfirm);
		btn_cancel = (Button) findViewById(R.id.btnCancel);
		img_backarrow = (ImageView) findViewById(R.id.img_backarrow);

		textview_name.setText(name);
		textview_appointmenttopic.setText(topic);
		textview_appointmentdetailsdescription.setText(Html.fromHtml("<html><strong>Description:</strong>" + description + "</html>"));
		edittext_mobNo.setText(mobile);
		edittext_skypeId.setText(skypeid);
		edittext_priorityone.setText("priority 1 is " + " " + priority1);
		edittext_priorityone.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priority1), 0, 0, 0);
		edittext_prioritytwo.setText("priority 2 is " + " " + priority2);
		edittext_prioritytwo.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priority2), 0, 0, 0);
		textview_prioritythree.setText("priority 3 is " + " " + priority3);
		textview_prioritythree.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priority3), 0, 0, 0);
		edittext_prefDateandtimeone.setText("preferred date and time " + "\n"
				+ date1);

		if(date2.equals(" "))
		{
			edittext_prefDateandtimetwo.setText("option1 date and time " + "\n"
					+"not available");
		}
		else
		{
			edittext_prefDateandtimetwo.setText("option1 date and time " + "\n"
					+ date2);
		}
	if(date3.equals(" "))
	{
		textview_prefDateandtimethree.setText("option2 date and time " + "\n"
				+ "not available");
	}
	else
	{
		textview_prefDateandtimethree.setText("option2 date and time " + "\n"
				+ date3);
	}
		mHangoutidEditText.setText(hangoutid);
mFacetimeidEditText.setText(facetimeid);
		imageLoader.DisplayImage(profile_image, imageview_clientImage);
		mtxtviewAppointmenttype.setText("Request Received");
		img_backarrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		edittext_chooseContacts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!choosecontactcclicked) {
					relativelay_contactsdropdown.setVisibility(View.VISIBLE);

					if(edittext_chooseContacts.getText().toString().equals("choose one contact")) {

						if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this))
						{
							edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.uparrow, 0);
						}
						else
						{
							edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.uparrow, 0);
						}


					}

					choosecontactcclicked = true;
				} else {
					relativelay_contactsdropdown.setVisibility(View.GONE);
					choosecontactcclicked = false;
					if(edittext_chooseContacts.getText().toString().equals("choose one contact")) {

						if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this))
						{
							edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.downarrow, 0);
						}
						else {
							edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.downarrow, 0);
						}


					}
				}

			}
		});

		edittext_choosedate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!choosedatecclicked) {
					relativelay_datedown.setVisibility(View.VISIBLE);
					choosedatecclicked = true;
					if(edittext_choosedate.getText().toString().equals("choose any one date")) {

						if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this))
						{
							edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.uparrow, 0);
						}
						else {
							edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.uparrow, 0);
						}




					}
				} else {
					relativelay_datedown.setVisibility(View.GONE);
					choosedatecclicked = false;
					if(edittext_choosedate.getText().toString().equals("choose any one date")) {

						if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this))
						{
							edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.downarrow, 0);
						}
						else {
							edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.downarrow, 0);
						}

					}
				}

			}
		});

		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				confirmAppointments();
			}
		});

		edittext_priorityone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String priorityArray[];
				priorityArray = edittext_priorityone.getText().toString()
						.split("is");
				edittext_chooseContacts.setText(priorityArray[1].toString()
						.trim());
				Drawable img = getResources()
						.getDrawable(R.drawable.skypeicon2);
			/*	edittext_chooseContacts
						.setCompoundDrawablesWithIntrinsicBounds(img, null,
								null, null); */
				relativelay_contactsdropdown.setVisibility(View.GONE);
				choosecontactcclicked = false;
				edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priorityArray[1].toString().trim()), 0,  R.drawable.downarrow,0);
			}

		});
		edittext_prioritytwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String priorityArray[];
				priorityArray = edittext_prioritytwo.getText().toString()
						.split("is");
		if(!priorityArray[1].toString().trim().contains("not available"))
	{
		edittext_chooseContacts.setText(priorityArray[1].toString()
			.trim());
		Drawable left_img = getResources().getDrawable(
			R.drawable.mobileicon2);

	/*	edittext_chooseContacts
			.setCompoundDrawablesWithIntrinsicBounds(left_img,
					null, null, null); */
		relativelay_contactsdropdown.setVisibility(View.GONE);
		choosecontactcclicked = false;
		edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priorityArray[1].toString().trim()), 0, R.drawable.downarrow, 0);
}
			}
		});
		textview_prioritythree.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String priorityArray[];
				priorityArray = textview_prioritythree.getText().toString()
						.split("is");
				if(!priorityArray[1].toString().trim().contains("not available")) {
					edittext_chooseContacts.setText(priorityArray[1].toString()
							.trim());

				/*	Drawable left_img = getResources().getDrawable(
							R.drawable.opentok_icon2);

					edittext_chooseContacts
							.setCompoundDrawablesWithIntrinsicBounds(left_img,
									null, null, null);*/
					relativelay_contactsdropdown.setVisibility(View.GONE);
					choosecontactcclicked = false;
					edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(setImageforPriorityone(priorityArray[1].toString().trim()), 0, R.drawable.downarrow, 0);
				}
			}
		});

		edittext_prefDateandtimeone
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Drawable left_img = getResources().getDrawable(
								R.drawable.dateandtimeicon);
						edittext_choosedate.setText(date1);
					/*	edittext_choosedate
								.setCompoundDrawablesWithIntrinsicBounds(
										left_img, null, null, null); */
						edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dateandtimeicon, 0, R.drawable.downarrow, 0);
						relativelay_datedown.setVisibility(View.GONE);
						choosedatecclicked = false;
					}
				});

		edittext_prefDateandtimetwo
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if(!edittext_prefDateandtimetwo.getText().toString().contains("not available")) {
							Drawable left_img = getResources().getDrawable(
									R.drawable.dateandtimeicon);
							edittext_choosedate.setText(date2);
						/*	edittext_choosedate
									.setCompoundDrawablesWithIntrinsicBounds(
											left_img, null, null, null); */
							edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dateandtimeicon, 0, R.drawable.downarrow, 0);
							relativelay_datedown.setVisibility(View.GONE);
							choosedatecclicked = false;
						}
					}
				});

		textview_prefDateandtimethree
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					if(!textview_prefDateandtimethree.getText().toString().contains("not available")) {
						Drawable left_img = getResources().getDrawable(
								R.drawable.dateandtimeicon);
						edittext_choosedate.setText(date3);
					/*	edittext_choosedate
								.setCompoundDrawablesWithIntrinsicBounds(
										left_img, null, null, null);   */
						relativelay_datedown.setVisibility(View.GONE);
						edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dateandtimeicon, 0, R.drawable.downarrow, 0);
						choosedatecclicked = false;
					}
					}
				});
		edittext_mobNo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!edittext_mobNo.getText().toString().equals("not avaialble"))
				{
					calltoMobilenumber(edittext_mobNo.getText().toString());
				}


			}
		});
		if(skypeid.equals("not available"))
		{
			if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this)) {
				edittext_skypeId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_skypeicon, 0,0, 0);
			}
			else {
				edittext_skypeId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.skypeicon2, 0, 0, 0);
			}
		}
		if(mobile.equals("not available"))
		{
			if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this)) {
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

		if(!edittext_skypeId.getText().toString().equals("not available")) {

			callviaSkype(edittext_skypeId.getText().toString());
		}

			}
		});
		
		
		
		
		
	}

	private void calltoMobilenumber(String mobilenumber)
	{
		Intent intent = new Intent(Intent.ACTION_CALL);

		intent.setData(Uri.parse("tel:" + mobilenumber));
		startActivity(intent);
	}
	private void callviaSkype(String skypeid){
		
if(	SYLUtil.appInstalledOrNot("com.skype.raider", SYLRequestReceivedAppointmentActivity.this))
{
	Intent skypeVideo = new Intent("android.intent.action.VIEW");
	skypeVideo.setData(Uri.parse("skype:" + skypeid + "?call&video=true"));
	startActivity(skypeVideo);
}
else {
	SYLUtil.buildAlertMessage(SYLRequestReceivedAppointmentActivity.this,"Please install Skype to enable this functionality");
}
	}	
	private void confirmAppointments() {
		if(validate()){
			if(SYLUtil.isNetworkAvailable(SYLRequestReceivedAppointmentActivity.this)) {
				msylProgressDialog = new ProgressDialog(SYLRequestReceivedAppointmentActivity.this);
				msylProgressDialog.setMessage("Please wait...");
				msylProgressDialog.setCancelable(false);
				msylProgressDialog.setCanceledOnTouchOutside(false);
				msylProgressDialog.show();
				String timezone = SYLUtil.getTimeGMTZone(SYLRequestReceivedAppointmentActivity.this);
				String date;
				int selectedoptionindex = getOptionSelectedIndex();
				int selecteddateindex = getDateSelectedIndex();
				String selecteddatetimeArray[], selecteddatearray[];
				String selecteddate = edittext_choosedate.getText().toString();
				String selectedoption = edittext_chooseContacts.getText().toString();
				selecteddatetimeArray = selecteddate.split(" ");
				selecteddatearray = selecteddatetimeArray[0].toString().split("-");
				date = selecteddatearray[2] + "-" + selecteddatearray[1] + "-" + selecteddatearray[0] + "T" + selecteddatetimeArray[1];
				SYLConfirmedAppointmentViewManager mViewmanager = new SYLConfirmedAppointmentViewManager();
				mViewmanager.mSylConfirmAppointmentListener = SYLRequestReceivedAppointmentActivity.this;
				mViewmanager.doconfirmAppointment(appointmentid, accesstoken, true, date, selectedoption, timezone, selectedoptionindex, selecteddateindex);
			}
			else{
				SYLUtil.buildAlertMessage(SYLRequestReceivedAppointmentActivity.this,"Please check your network connection and try again");
			}

		}
	}
public void	cancelAppointment(View v)
{
	if (SYLUtil.isNetworkAvailable(SYLRequestReceivedAppointmentActivity.this)) {
		msylProgressDialog = new ProgressDialog(SYLRequestReceivedAppointmentActivity.this);
		msylProgressDialog.setMessage("Please wait...");
		msylProgressDialog.setCancelable(false);
		msylProgressDialog.setCanceledOnTouchOutside(false);
		msylProgressDialog.show();
		String timezone=SYLUtil.getTimeGMTZone(SYLRequestReceivedAppointmentActivity.this);
		SYLCancelAppointmentViewManager mViewmanager = new SYLCancelAppointmentViewManager();
		mViewmanager.mCancelAppointmentListener = SYLRequestReceivedAppointmentActivity.this;
		mViewmanager.doCancelAppointment(accesstoken, appointmentid,timezone);
	} else {
		SYLUtil.buildAlertMessage(SYLRequestReceivedAppointmentActivity.this,
				getString(R.string.network_alertmessage));
	}

}
private int getOptionSelectedIndex()
{
	int optionselectedindex=0;
String selectedoption=	edittext_chooseContacts.getText().toString();
if(selectedoption.contains(priority1))
{optionselectedindex=0;
	}
else if(selectedoption.contains(priority2))
{
	optionselectedindex=1;}
else if(selectedoption.contains(priority3))
{
	optionselectedindex=2;
	
}
return optionselectedindex;
}
private int getDateSelectedIndex()
{
	int selecteddateIndex=0;
	String selecteddate=	edittext_choosedate.getText().toString();
	if(selecteddate.equals(date1))
	{
		selecteddateIndex=0;
	}
	else if(selecteddate.equals(date2))
	{
		selecteddateIndex=1;
	}
	else if(selecteddate.equals(date3))
	{
		selecteddateIndex=2;
	}
	return selecteddateIndex;
}

	private void navigatetoAppointmentsListingScreen() {
		Intent intent = new Intent(SYLRequestReceivedAppointmentActivity.this,
				SYLFragmentChangeActivity.class);
		intent.putExtra("fragmentvalue", "newappointment");
		intent.putExtra("needrefresh", "true");
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
Intent intent=null;
		if(getIntent().getStringExtra("intentfrom").equals("futureappointment")) {
			intent = new Intent(SYLRequestReceivedAppointmentActivity.this,
					SYLFragmentChangeActivity.class);
			intent.putExtra("fragmentvalue", "newappointment");
			intent.putExtra("needrefresh", "false");
			startActivity(intent);
			finish();
		}
		else {
			intent = new Intent(SYLRequestReceivedAppointmentActivity.this,
					SYLHistoryAppointmentsActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void getConfirmAppointmentFinish(
			SYLConfirmRequestResponseModel mconfirmrequestResponseModel) {
		// TODO Auto-generated method stub
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		if(mconfirmrequestResponseModel!=null)
		{
		if(	mconfirmrequestResponseModel.isSuccess())
		{
			showAlertMessage(SYLRequestReceivedAppointmentActivity.this,"Appointment has been confirmed successfully");
		}
		else{
			SYLUtil.buildAlertMessage(SYLRequestReceivedAppointmentActivity.this,"Confirm the appointment failed");
		}
		}
		else {
			SYLUtil.buildAlertMessage(SYLRequestReceivedAppointmentActivity.this,"Unexpected server error occured");
		}
	}
	private boolean validate()
	{
		boolean validateflag=true;

	if(	edittext_chooseContacts.getText().toString().equals("choose one contact"))
	{
		Toast.makeText(SYLRequestReceivedAppointmentActivity.this, "Please select a contact option", Toast.LENGTH_LONG).show();
	validateflag=false;
	}
	if(edittext_choosedate.getText().toString().equals("choose any one date"))
	{
		Toast.makeText(SYLRequestReceivedAppointmentActivity.this, "Please select a date", Toast.LENGTH_LONG).show();
		validateflag=false;
	}
	if(!edittext_choosedate.getText().toString().equals("choose any one date"))

	{
		String selecteddate=edittext_choosedate.getText().toString();
		String selecteddateArray[]=selecteddate.split(" ");
		boolean datecomapreflag=SYLUtil.checkgivenTimeisValid(selecteddateArray[0].toString().replace("-","/"),selecteddateArray[1].toString());
		if(!datecomapreflag)
		{
			validateflag=false;
			Toast.makeText(getApplicationContext(), "Selected date is past.Please schedule new appointment", Toast.LENGTH_SHORT).show();
		}
	}
	return validateflag;	
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
			showAlertMessage(SYLRequestReceivedAppointmentActivity.this,"Cancelled appointments successfully");
				} else {
					SYLUtil.buildAlertMessage(SYLRequestReceivedAppointmentActivity.this,
							mcancelappointmentmodel.getError().getErrorDetail());
				}
			} else {
				SYLUtil.buildAlertMessage(SYLRequestReceivedAppointmentActivity.this,
						"Unexpected server error happened");
			}
		} catch (Exception e) {
			SYLUtil.buildAlertMessage(SYLRequestReceivedAppointmentActivity.this,
					"Unexpected server error happened");
		}
	}
	public  void showAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {

								dialog.cancel();
								navigatetoAppointmentsListingScreen();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}
	private int	setImageforPriorityone(String priority)
	{
		int imgResource=R.drawable.facetime_icon2;;
		if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this)) {
		 imgResource = R.drawable.tablet_chooseicon;
		}
		else {
		 imgResource = R.drawable.chooseicon;
		}

		switch (priority) {
			case "Skype":
				if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this))
				{
					imgResource =R.drawable.tablet_skypeicon;
				}
				else {
					imgResource =R.drawable.skypeicon2;
				}

				break;
			case "Mobile":

				if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this))
				{
					imgResource=	R.drawable.tablet_mobileicon;
				}
				else {
					imgResource=	R.drawable.mobileicon2;
				}


				break;
			case "SYL Videocall(in beta)":

				if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this)) {
					imgResource=	R.drawable.tablet_opentokicon;
				}
				else {
					imgResource=	R.drawable.opentok_icon2;
				}


				break;
			case "Hangout":

				if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this))
				{
					imgResource=R.drawable.tablet_hangouticon;
				}
				else {
					imgResource=R.drawable.googlehangouticon;
				}
				break;
			case "Facetime":
				if(SYLUtil.isTablet(SYLRequestReceivedAppointmentActivity.this))
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(br);
	}
}
