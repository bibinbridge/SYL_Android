package com.webcamconsult.syl.activities;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.fragments.SYLDatePickerFragment;
import com.webcamconsult.syl.fragments.SYLPrioritySelectDialogFragment;
import com.webcamconsult.syl.fragments.SYLTimePickerFragment;
import com.webcamconsult.syl.guidednavigation.SYLGuidedNewAppointmentActivity;
import com.webcamconsult.syl.interfaces.SYLCreatenewAppointmentListener;
import com.webcamconsult.syl.interfaces.SYLDateListener;
import com.webcamconsult.syl.interfaces.SYLPrioritySelectListener;
import com.webcamconsult.syl.interfaces.SYLTimeListener;
import com.webcamconsult.syl.model.SYLCreateNewAppointmentResponse;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLCreateNewAppointmentViewManager;

public class SYLRequestnewappointmentActivity extends FragmentActivity
		implements SYLDateListener, SYLTimeListener, SYLPrioritySelectListener,
		SYLCreatenewAppointmentListener {
	String intentvalue;
	String[] objects1 = { "Choose Priority1", "Skype", "opentok", "Mobile" };
	String[] objects2 = { "opentok", "Mobile", "skype" };
	String[] objects3 = { "Mobile", "opentok", "Skype" };
	public int medium1check = 0;
	private String contactsource = null;
	private boolean whatsappflag;
	RelativeLayout mChooseoption1relativelayout, mChooseoptionrelativelayout2,
			mChooseoptionrelativelayout3;
	RelativeLayout mImportcontactrelativelayout;
	String mobilenumber = null;
public static boolean guidedflag=false;
	ArrayList<String> medium1ArrayList = new ArrayList<String>();
	ArrayList<Integer> medium1imageArrayList = new ArrayList<Integer>();

	int total_images1[] = { R.drawable.skype_largeicon,
			R.drawable.skype_largeicon, R.drawable.opentok_icon,
			R.drawable.mobile_largeicon };
	int total_images2[] = { R.drawable.opentok_icon,
			R.drawable.mobile_largeicon, R.drawable.skype_largeicon, };
	int total_images3[] = { R.drawable.mobile_largeicon,
			R.drawable.mobile_largeicon, R.drawable.skype_largeicon };
	ImageView mbackarrowImageview;
	TextView mtxtview_preferreddate, mtxtview_optiondate1,
			mtxtview_optiondate2;
	TextView mtxtview_preftime, mtxtview_optiontime1, mtxtview_optiontime2;
	LinearLayout mLinearPreferreddate, mLinearoptiondate1, mLinearOptiondate2;
	LinearLayout mLinearPreferredtime, mLineartimeoption1, mLineartimeoption2;
	Bundle args;
	EditText mChooseContactsEdittext;
	TextView mtxtview_chooseoption1, mtxtview_chooseoption2,
			mtxtview_chooseoption3;
	ImageView mChooseoptionImageView, mChooseoptionimageview2,
			mChooseoptionimageview3;
	TextView mnameTextView;
	String participantuserid, appointmenttopic, appointmentdescription, priority1,
			priority2, priority3;
	String preferreddatetime1, preferreddatetime2, preferreddatetime3,
			accesstoken, timezone;;
	EditText mAppointmenttopicEdittExt, mAppointmentDescriptionEdittExt;
	ProgressDialog msylProgressDialog;
String appointmentid;
String fullname="",mobile="",email="";
 String importcontactsource="";
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



		setContentView(R.layout.syl_newappointment);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		intentvalue = getIntent().getStringExtra("intentvalue");
		medium1ArrayList.add("SYL Videocall");
		medium1ArrayList.add("Skype");
		
		medium1ArrayList.add("Mobile");
		medium1ArrayList.add("Facetime");
		medium1ArrayList.add("Hangout");
		medium1imageArrayList.add(R.drawable.opentok_icon);
		medium1imageArrayList.add(R.drawable.skype_largeicon);

		medium1imageArrayList.add(R.drawable.mobile_largeicon);
		medium1imageArrayList.add(R.drawable.facetime_icon);
		medium1imageArrayList.add(R.drawable.hangout_icon);

		mAppointmenttopicEdittExt = (EditText) findViewById(R.id.liout_appointmenttopic);
		mAppointmentDescriptionEdittExt = (EditText) findViewById(R.id.liout_appointmentdescription);

		mChooseoption1relativelayout = (RelativeLayout) findViewById(R.id.relout_chooseoption1);
		mChooseoptionrelativelayout2 = (RelativeLayout) findViewById(R.id.relout_chooseoption2);
		mChooseoptionrelativelayout3 = (RelativeLayout) findViewById(R.id.relout_chooseoption3);
		mtxtview_preferreddate = (TextView) findViewById(R.id.preftxt_date);
		mtxtview_optiondate1 = (TextView) findViewById(R.id.txt_optiondate1);
		mtxtview_optiondate2 = (TextView) findViewById(R.id.txtoptiondate2);
		mtxtview_optiontime1 = (TextView) findViewById(R.id.txt_optiontime1);
		mtxtview_optiontime2 = (TextView) findViewById(R.id.txt_optiontime2);
		mtxtview_preftime = (TextView) findViewById(R.id.pref_txttime);
		mtxtview_chooseoption1 = (TextView) findViewById(R.id.txt_chooseoption);
		mChooseoptionImageView = (ImageView) findViewById(R.id.img_selectimage);
		mChooseoptionimageview2 = (ImageView) findViewById(R.id.img_selectimage2);
		mChooseoptionimageview3 = (ImageView) findViewById(R.id.img_selectimage3);
		mtxtview_preferreddate = (TextView) findViewById(R.id.preftxt_date);
		mnameTextView = (TextView) findViewById(R.id.txt_importcontact);
		mtxtview_chooseoption2 = (TextView) findViewById(R.id.txt_chooseoption2);
		mtxtview_chooseoption3 = (TextView) findViewById(R.id.txt_chooseoption3);
		mLinearPreferreddate = (LinearLayout) findViewById(R.id.liout_prefereddate);
		mLinearoptiondate1 = (LinearLayout) findViewById(R.id.liout_optiondate1);
		mLinearOptiondate2 = (LinearLayout) findViewById(R.id.liout_optiondate2);
		mLinearOptiondate2 = (LinearLayout) findViewById(R.id.liout_optiondate2);
		mLinearPreferredtime = (LinearLayout) findViewById(R.id.liout_preferedtime);
		mLineartimeoption1 = (LinearLayout) findViewById(R.id.liout_optiontime1);
		mLineartimeoption2 = (LinearLayout) findViewById(R.id.liout_preferedtime2);
		mImportcontactrelativelayout = (RelativeLayout) findViewById(R.id.relout_importcontact);
		if (intentvalue.equals("menufragment")) {
			String name = getIntent().getStringExtra("name");
			String userid1 = getIntent().getStringExtra("user_id");
			participantuserid = userid1;
			contactsource = getIntent().getStringExtra("contactsource");
	importcontactsource=SYLUtil.getContactSource(contactsource);
	if(importcontactsource.equals("SYL"))
	{
	fullname="";
	mobile="";
	email="";
	}
	else if(importcontactsource.equals("PHONE"))
	{
		participantuserid="0";
		fullname=name;
		mobile=getIntent().getStringExtra("mobilenumber");
		email=getIntent().getStringExtra("email");;
	}
	else if(importcontactsource.equals("GOOGLE"))
	{
		participantuserid="0";
		fullname=name;
		
		email=getIntent().getStringExtra("email");;
	}
			mobilenumber = getIntent().getStringExtra("mobilenumber");
			if (name == null || name.equals("")) {
				name = "Import One Contact";
			}
			mnameTextView.setText(name);
		} 
		
		else if (intentvalue.equals("newappointmentimportcontact")) {
			String name = getIntent().getStringExtra("name");
			String userid1 = getIntent().getStringExtra("user_id");
			Bundle detailsbundle=getIntent().getBundleExtra("detailsbundle");
checkandSetPreviousValues( detailsbundle);
			contactsource = getIntent().getStringExtra("contactsource");
			importcontactsource=SYLUtil.getContactSource(contactsource);
			if(importcontactsource.equals("SYL"))
			{
		participantuserid=userid1;
		fullname=name;
		email="";
		mobile="";
			}
			else if(importcontactsource.equals("GOOGLE"))
			{
				participantuserid="0";
				fullname=name;
				
				email=getIntent().getStringExtra("email");;
			}
			
			else if(importcontactsource.equals("PHONE"))
			{
				participantuserid="0";
				fullname=name;
				mobile=getIntent().getStringExtra("mobilenumber");
				email=getIntent().getStringExtra("email");;
			}
			
			
			if (name == null || name.equals("")) {
				name = "Import One Contact";
			}
			mnameTextView.setText(name);
		}
		mbackarrowImageview = (ImageView) findViewById(R.id.img_backarrow);

		mLinearPreferreddate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePickerDialog("preferred","preferred");
			}
		});
		mImportcontactrelativelayout
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						navigateToContactsFragment();
					}
				});
		mLinearoptiondate1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!SYLUtil.checkPreferredDate(mtxtview_preferreddate))
				{
					Toast.makeText(SYLRequestnewappointmentActivity.this,"Please select preferreddate",Toast.LENGTH_LONG).show();
				}
				else{
				showDatePickerDialog("option1date",mtxtview_preferreddate.getText().toString());}
			}
		});
		mLinearOptiondate2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mtxtview_optiondate1.getText().toString().equals("Option 1 Date"))
				{
					Toast.makeText(SYLRequestnewappointmentActivity.this,"Please select option1 date",Toast.LENGTH_LONG).show();
				}
				else {
					showDatePickerDialog("option2date",mtxtview_preferreddate.getText().toString());
				}

			}
		});
		mLinearPreferredtime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog("preferredtime","preferredtime");
			}
		});
		mLineartimeoption1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(!SYLUtil.checkOptionTime(mtxtview_preftime))
			{
Toast.makeText(SYLRequestnewappointmentActivity.this,"Please select preferred time",Toast.LENGTH_LONG).show();
			}
				else {
				showTimePickerDialog("timeoption1",mtxtview_preftime.getText().toString());
			}



			}
		});
		mLineartimeoption2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mtxtview_optiontime1.getText().equals("Time"))
				{
					Toast.makeText(SYLRequestnewappointmentActivity.this,"Please select time option1",Toast.LENGTH_LONG).show();
				}
else {
					showTimePickerDialog("timeoption2",mtxtview_preftime.getText().toString() );
				}
			}
		});
		mChooseoption1relativelayout
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						medium1ArrayList.clear();
						medium1ArrayList.add("SYL Videocall(in beta)");
						medium1ArrayList.add("Skype");
					
						medium1ArrayList.add("Mobile");
						medium1ArrayList.add("Facetime");
						medium1ArrayList.add("Hangout");
						medium1imageArrayList.clear();
						medium1imageArrayList.add(R.drawable.opentok_icon);
						medium1imageArrayList.add(R.drawable.skype_largeicon);
				
						medium1imageArrayList.add(R.drawable.mobile_largeicon);
						medium1imageArrayList.add(R.drawable.facetime_icon);
						medium1imageArrayList.add(R.drawable.hangout_icon);

						showPriorityDialogFragment("id1");
					}
				});
		mChooseoptionrelativelayout2
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!mtxtview_chooseoption1.getText().equals(
								"Choose priority 1")) {
							medium1ArrayList.clear();
							medium1imageArrayList.clear();
							medium1ArrayList.add("Skype");
							medium1ArrayList.add("SYL Videocall(in beta)");
							medium1ArrayList.add("Mobile");
							medium1ArrayList.add("Facetime");
							medium1ArrayList.add("Hangout");
							medium1ArrayList.remove(mtxtview_chooseoption1
									.getText().toString());

							medium1imageArrayList
									.add(R.drawable.skype_largeicon);
							medium1imageArrayList.add(R.drawable.opentok_icon);
							medium1imageArrayList
									.add(R.drawable.mobile_largeicon);
							medium1imageArrayList.add(R.drawable.facetime_icon);
							medium1imageArrayList.add(R.drawable.hangout_icon);
							removeImageFromList(mtxtview_chooseoption1
									.getText().toString());
							showPriorityDialogFragment("id2");
						} else {
							Toast.makeText(
									SYLRequestnewappointmentActivity.this,
									"Please select priority one initially",
									Toast.LENGTH_LONG).show();
						}
					}
				});
		mChooseoptionrelativelayout3
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mtxtview_chooseoption2.getText().equals(
								"Choose priority 2")) {
							Toast.makeText(
									SYLRequestnewappointmentActivity.this,
									"Please select priority2",
									Toast.LENGTH_LONG).show();
						} else {
							medium1ArrayList.clear();
							medium1imageArrayList.clear();
							medium1ArrayList.add("Skype");
							medium1ArrayList.add("SYL Videocall(in beta)");
							medium1ArrayList.add("Mobile");
							medium1ArrayList.add("Facetime");
							medium1ArrayList.add("Hangout");
							medium1ArrayList.remove(mtxtview_chooseoption1
									.getText().toString());
							medium1ArrayList.remove(mtxtview_chooseoption2
									.getText().toString());
							medium1imageArrayList
									.add(R.drawable.skype_largeicon);
							medium1imageArrayList.add(R.drawable.opentok_icon);
							medium1imageArrayList
									.add(R.drawable.mobile_largeicon);
							medium1imageArrayList.add(R.drawable.facetime_icon);
							medium1imageArrayList.add(R.drawable.hangout_icon);
							removeImageFromList(mtxtview_chooseoption1
									.getText().toString());

							removeImageFromList(mtxtview_chooseoption2
									.getText().toString());
							showPriorityDialogFragment("id3");
						}
					}
				});

	}

	private void showDatePickerDialog(String datetype,String entereddate) {
		SYLDatePickerFragment mdatepickerfragment = new SYLDatePickerFragment();

Bundle dateBundle=new Bundle();

			dateBundle.putString("entereddate", entereddate);
		mdatepickerfragment.setArguments(dateBundle);
		mdatepickerfragment.mDateListener = SYLRequestnewappointmentActivity.this;

		mdatepickerfragment.show(getSupportFragmentManager(), datetype);

	}

	public void showTimePickerDialog(String timeoptiontype,String enteredtime) {
		Bundle timebundle=new Bundle();
		timebundle.putString("enteredtime",enteredtime);
		SYLTimePickerFragment mtimepickerfragment = new SYLTimePickerFragment();
		mtimepickerfragment.setArguments(timebundle);
		mtimepickerfragment.mTimeListener = SYLRequestnewappointmentActivity.this;
		mtimepickerfragment.show(getSupportFragmentManager(), timeoptiontype);

	}

	private  void sendsmsorwhatsappmessage()

	{
		// This will replace after integrating API call
		if (importcontactsource.equals("PHONE")) {
		if	(SYLUtil.checkSimisPresent(SYLRequestnewappointmentActivity.this))

		{
		if(!TextUtils.isEmpty(mobile)) {

			/*
			whatsappflag = SYLUtil.appInstalledOrNot("com.whatsapp",
					SYLRequestnewappointmentActivity.this);
			if (whatsappflag) {
				showwhatsappAlertMessage(SYLRequestnewappointmentActivity.this,
						"Please send an appropriate message via whats app about scheduling");


			} else { */

				if (mobile != null) {
					Uri uri = Uri.parse("smsto:" + mobile);
					Intent intent = new Intent(Intent.ACTION_SENDTO, uri);


					String smsmessage = getSMSMessage1();

					intent.putExtra("sms_body",
							smsmessage);
					startActivityForResult(intent, 2);
				}
			}
	//	}
		//Code for sending email message if mobilenumber is absent
			else {
			if(email!=null || !email.equals(""))
				buildEmailAlertMessage(SYLRequestnewappointmentActivity.this, "Please send an email to the correponding user about SYL");
		}



		}
		else {
			if(email!=null || !email.equals(""))
			buildEmailAlertMessage(SYLRequestnewappointmentActivity.this, "Please send an email to the correponding user about SYL");
		}
		}
		else if(importcontactsource.equals("SYL") || importcontactsource.equals("favourites") || importcontactsource.equals("") || importcontactsource.equals("GOOGLE") )
		{
		       Log.e("redirect to appointments list","redirect to appointments list");
	         	Intent signinIntent = new Intent(
						SYLRequestnewappointmentActivity.this,
						SYLFragmentChangeActivity.class);
				signinIntent.putExtra("fragmentvalue", "newappointment");
			signinIntent.putExtra("needrefresh", "true");
				SYLRequestnewappointmentActivity.this.startActivity(signinIntent);
			finish();
		}
		// Intent intent = new Intent(SYLRequestnewappointmentActivity.this,
		// SYLFragmentChangeActivity.class);
		// intent.putExtra("fragmentvalue", "newappointment");
		//
		// startActivity(intent);
		// finish();
	}

	private void showwhatsappAlertMessage(Context context, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// openWhatsappContact(mobilenumber);
						openWhatsappContact(mobile);

					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	void openWhatsappContact(String number) {
		Uri uri = Uri.parse("smsto:" + number);
		Intent i = new Intent(Intent.ACTION_SENDTO, uri);

		i.setPackage("com.whatsapp");

		//startActivity(Intent.createChooser(i, "Share with"));
		startActivityForResult(Intent.createChooser(i, "Share with"), 2);


	}

	public void navigatetheactionbarbackButton(View v) {
		// onBackPressed();
		// Intent signinIntent = new
		// Intent(SYLRequestnewappointmentActivity.this,
		// SYLFragmentChangeActivity.class);
		// if (intentvalue.equals("appointments")) {
		// signinIntent.putExtra("fragmentvalue", "newappointment");
		// SYLRequestnewappointmentActivity.this.startActivity(signinIntent);
		// finish();
		// } else if (intentvalue.equals("favourites")) {
		// signinIntent.putExtra("fragmentvalue", "favourites");
		// SYLRequestnewappointmentActivity.this.startActivity(signinIntent);
		// finish();
		// }
		// else {
		// signinIntent.putExtra("fragmentvalue", "newappointment");
		// SYLRequestnewappointmentActivity.this.startActivity(signinIntent);
		// finish();
		// }

		onBackPressed();
	}

public static 	class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					false);

		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			int k = minute;
			Log.e("Minute", "" + k);

		}
	}

	@Override
	public void getDate(String date, String datetype) {
		// TODO Auto-generated method stub



		if (datetype.equals("preferred")) {
			mtxtview_preferreddate.setText(date);
		} else if (datetype.equals("option1date")) {
			mtxtview_optiondate1.setText(date);
		} else if (datetype.equals("option2date")) {
			mtxtview_optiondate2.setText(date);
		}



	}

	@Override
	public void ondidgetTime(String time, String timeoptiontype) {
		// TODO Auto-generated method stub



		if (timeoptiontype.equals("preferredtime")) {
			mtxtview_preftime.setText(time);
		} else if (timeoptiontype.equals("timeoption1")) {
			mtxtview_optiontime1.setText(time);
		} else if (timeoptiontype.equals("timeoption2")) {
			mtxtview_optiontime2.setText(time);
		}


	}

	private void showPriorityDialogFragment(String id) {
		SYLPrioritySelectDialogFragment priorityselectdialogfragment = new SYLPrioritySelectDialogFragment();
		Bundle c = new Bundle();
		c.putString("test", "test");
		if (id.equals("id1")) {
			c.putStringArrayList("testarraylist", medium1ArrayList);
			c.putIntegerArrayList("imagearraylist", medium1imageArrayList);
			c.putString("optionnumber", "1");
		} else if (id.equals("id2")) {
			c.putStringArrayList("testarraylist", medium1ArrayList);
			c.putIntegerArrayList("imagearraylist", medium1imageArrayList);
			c.putString("optionnumber", "2");
		} else if (id.equals("id3")) {
			c.putStringArrayList("testarraylist", medium1ArrayList);
			c.putIntegerArrayList("imagearraylist", medium1imageArrayList);
			c.putString("optionnumber", "3");
		}
		priorityselectdialogfragment.setArguments(c);

		priorityselectdialogfragment.show(getSupportFragmentManager(), "tag");

	}

	@Override
	public void onPrioritySelectFinish(String value, String optionnumber) {
		// TODO Auto-generated method stub
		int imageicon = R.drawable.skype_largeicon;
		if (optionnumber.equals("1")) {
			mtxtview_chooseoption1.setText(value);

			addSelectedImage(value, optionnumber);
		}
		if (optionnumber.equals("2")) {
			mtxtview_chooseoption2.setText(value);
			addSelectedImage(value, optionnumber);
		}
		if (optionnumber.equals("3")) {
			mtxtview_chooseoption3.setText(value);
			addSelectedImage(value, optionnumber);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		Log.e("Back Pressed", "Back Pressed");

		if (intentvalue.equals("appointments")) {
			Intent signinIntent = new Intent(
					SYLRequestnewappointmentActivity.this,
					SYLFragmentChangeActivity.class);
			signinIntent.putExtra("fragmentvalue", "newappointment");
			signinIntent.putExtra("needrefresh", "false");
			SYLRequestnewappointmentActivity.this.startActivity(signinIntent);

			SYLRequestnewappointmentActivity.this.finish();
		} else if (intentvalue.equals("newappointmentimportcontact")) {
			Intent signinIntent = new Intent(
					SYLRequestnewappointmentActivity.this,
					SYLFragmentChangeActivity.class);
			signinIntent.putExtra("fragmentvalue",
					"newappointmentimportcontact");
			signinIntent.putExtra("needrefresh", "false");

			SYLRequestnewappointmentActivity.this.startActivity(signinIntent);
			SYLRequestnewappointmentActivity.this.finish();
		} else if (intentvalue.equals("menufragment")) {
			Intent signinIntent = new Intent(
					SYLRequestnewappointmentActivity.this,
					SYLFragmentChangeActivity.class);
			signinIntent.putExtra("fragmentvalue", "menufragment");
			signinIntent.putExtra("needrefresh", "false");
			SYLRequestnewappointmentActivity.this.startActivity(signinIntent);
			SYLRequestnewappointmentActivity.this.finish();
		}
	}

	private void navigateToContactsFragment() {
		Log.e("navigate to contacts", "navigate to contacts");
		Intent contactsIntent = new Intent(
				SYLRequestnewappointmentActivity.this,
				SYLFragmentChangeActivity.class);
		contactsIntent.putExtra("fragmentvalue", "newappointmentimportcontact");
		saveEnteredValues();

		
contactsIntent.putExtra("detailsbundle", saveEnteredValues());
		SYLRequestnewappointmentActivity.this.startActivity(contactsIntent);
		finish();
	}

	public void doRequestNewAppointment(View v)

	{
		hideKeyboard();
		if(importcontactsource.equals("GOOGLE"))
		{
			if(SYLSaveValues.getSYLEmailAddress(SYLRequestnewappointmentActivity.this).equals(email))
			{
				SYLUtil.buildAlertMessage(SYLRequestnewappointmentActivity.this,"You can't make appointment with this user");
				return;
			}
		}



		if(!(importcontactsource.equals("favourites")|| importcontactsource.equals("SYL") )        )
		{
			

	boolean simvalue=SYLUtil.checkSimisPresent(getBaseContext());
		if(!simvalue)
		{


			if(email.equals("")||email==null)
			{
				SYLUtil.buildAlertMessage(SYLRequestnewappointmentActivity.this, "You cant make an appointment with this user.");
			return;
			}

		}


		}
		
		if (validate()) {
			if (SYLUtil
					.isNetworkAvailable(SYLRequestnewappointmentActivity.this)) {


				appointmentid="0";// we are sending appointmentid as no value when creating apoointment
				String syluserid = SYLSaveValues
						.getSYLUserID(SYLRequestnewappointmentActivity.this);
				String url=SYLUtil.SYL_BASEURL+"api/Appointment/Schedule";
				int syluser_id = Integer.parseInt(syluserid);
				appointmentdescription = mAppointmentDescriptionEdittExt
						.getText().toString();
				appointmenttopic = mAppointmenttopicEdittExt.getText()
						.toString();
				priority1 = mtxtview_chooseoption1.getText().toString();
				priority2 = mtxtview_chooseoption2.getText().toString();
				priority3 = mtxtview_chooseoption3.getText().toString();
				String option1date, option1time, option2date, option2time;
				option1date = mtxtview_optiondate1.getText().toString();
				option1time = mtxtview_optiontime1.getText().toString();
				option2date = mtxtview_optiondate2.getText().toString();
				option2time = mtxtview_optiontime2.getText().toString();
				if(priority2.equals("Choose priority 2"))
				{
					priority2="";
				}
				if(priority3.equals("Choose priority 3"))
				{
					priority3="";
				}
				if (option1date.equals("Option 1 Date")) {
					option1date = "";
				}
				if (option1time.equals("Time")) {
					option1time = "";
				}
				if (option2date.equals("Option 2 Date")) {
					option2date = "";
				}
				if (option2date.equals("Time")) {
					option2time = "";
				}
				preferreddatetime1 = mtxtview_preferreddate.getText()
						.toString()
						+ "T"
						+ mtxtview_preftime.getText().toString();
				preferreddatetime2 = option1date + "T" + option1time;
				preferreddatetime3 = option2date + "T" + option2time;





if(validateSamedateAndtime()) {
	msylProgressDialog = new ProgressDialog(
			SYLRequestnewappointmentActivity.this);
	msylProgressDialog.setMessage("Please wait...");
	msylProgressDialog.setCancelable(false);
	msylProgressDialog.setCanceledOnTouchOutside(false);
	msylProgressDialog.show();
	accesstoken = SYLSaveValues
			.getSYLaccessToken(SYLRequestnewappointmentActivity.this);
	timezone = SYLUtil
			.getTimeGMTZone(SYLRequestnewappointmentActivity.this);

	SYLCreateNewAppointmentViewManager mviewmanager = new SYLCreateNewAppointmentViewManager();
	mviewmanager.msylcreatenewappointmentlistener = SYLRequestnewappointmentActivity.this;
	mviewmanager

			.createnewAppointment(syluser_id, importcontactsource, fullname, mobile, email, appointmenttopic,
					appointmentdescription, priority1, priority2,
					priority3, preferreddatetime1, preferreddatetime2,
					preferreddatetime3, timezone, accesstoken, participantuserid, appointmentid, url);

}
			} else {

			}
		}
	}

	@Override
	public void getCreatenewAppointmentFinish(
			SYLCreateNewAppointmentResponse msylcreatenewappointmentresponse) {
		// TODO Auto-generated method stub
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		if (msylcreatenewappointmentresponse != null) {
			if (msylcreatenewappointmentresponse.isSuccess()) {
				buildAlertMessage(
						SYLRequestnewappointmentActivity.this,
						"Created Appointment Successfully");
			} else {
				SYLUtil.buildAlertMessage(
						SYLRequestnewappointmentActivity.this,
						"Creating  Appointment Failed");
			}
		} else {
			SYLUtil.buildAlertMessage(SYLRequestnewappointmentActivity.this,
					"Unexpected Server Error Occured");
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
								
								sendsmsorwhatsappmessage();
						
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}
	public  void buildEmailAlertMessage(Context context, String message) {
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {

								dialog.cancel();
								
								sendEmail(email);
								
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}

	
	
	
	
	private boolean validate() {
		boolean flag = true;

		if (mnameTextView.getText().equals("Import One Contact")) {
			flag = false;
			Toast.makeText(SYLRequestnewappointmentActivity.this,
					"Please import a contact", Toast.LENGTH_LONG).show();
		}
		if (TextUtils.isEmpty(mAppointmenttopicEdittExt.getText().toString())) {
			flag = false;
			Toast.makeText(SYLRequestnewappointmentActivity.this,
					"Please enter appointment topic", Toast.LENGTH_LONG).show();
		}
	
		if (mtxtview_chooseoption1.getText().equals("Choose priority 1")) {
			flag = false;
			Toast.makeText(SYLRequestnewappointmentActivity.this,
					"Please select a priority", Toast.LENGTH_LONG).show();
		}
		if (mtxtview_preferreddate.getText().equals("Preferred Date")) {
			flag = false;
			Toast.makeText(SYLRequestnewappointmentActivity.this,
					"Please select a date", Toast.LENGTH_LONG).show();
		}
		if (mtxtview_preftime.getText().equals("Time")) {
			flag = false;
			Toast.makeText(SYLRequestnewappointmentActivity.this,
					"Please select time", Toast.LENGTH_LONG).show();
		}
		if(mtxtview_optiondate1.getText().equals("Option 1 Date")&& !mtxtview_optiontime1.getText().equals("Time"))
		{
			flag = false;
			Toast.makeText(SYLRequestnewappointmentActivity.this,
					"Please select option 1 date", Toast.LENGTH_LONG).show();
		}

		if(mtxtview_optiontime1.getText().equals("Time") && !mtxtview_optiondate1.getText().equals("Option 1 Date") )
		{
			flag = false;
			Toast.makeText(SYLRequestnewappointmentActivity.this,
					"Please select option 1 time", Toast.LENGTH_LONG).show();
		}

if(mtxtview_optiontime2.getText().toString().equals("Time") &&  !mtxtview_optiondate2.getText().equals("Option 2 Date")    )
{
	flag = false;
	Toast.makeText(SYLRequestnewappointmentActivity.this,
			"Please select option 2 time", Toast.LENGTH_LONG).show();
}
		if(!mtxtview_optiontime2.getText().toString().equals("Time") &&  mtxtview_optiondate2.getText().equals("Option 2 Date"))
		{
			flag = false;
			Toast.makeText(SYLRequestnewappointmentActivity.this,
					"Please select option 2 date", Toast.LENGTH_LONG).show();
		}

		if(mtxtview_chooseoption1.getText().toString().equals(mtxtview_chooseoption3.getText().toString())){
			flag = false;
			Toast.makeText(getApplicationContext(), "Priority 1 and Priority 3 are same.", Toast.LENGTH_SHORT).show();

		}
		if(mtxtview_chooseoption1.getText().toString().equals(mtxtview_chooseoption2.getText().toString())){
			flag = false;
			Toast.makeText(getApplicationContext(), "Priority 1 and Priority 2 are same.", Toast.LENGTH_SHORT).show();

		}
		if(mtxtview_chooseoption2.getText().toString().equals(mtxtview_chooseoption3.getText().toString())){
			flag = false;
			Toast.makeText(getApplicationContext(), "Priority 2 and Priority 3 are same.", Toast.LENGTH_SHORT).show();

		}

if(!mtxtview_preferreddate.getText().equals("Preferred Date") && !mtxtview_preftime.getText().equals("Time") )
{

	boolean dateflag= SYLUtil.checkgivenTimeisValid(mtxtview_preferreddate.getText().toString(),mtxtview_preftime.getText().toString());
	if(!dateflag)
	{
		flag=false;
		Toast.makeText(getApplicationContext(), "Preferred date and time is not valid.Please select current/upcoming date and time", Toast.LENGTH_SHORT).show();
	}
}
if(!mtxtview_optiondate1.getText().toString().equals("Option 1 Date") && !mtxtview_optiontime1.getText().toString().equals("Time"))
{

	boolean dateflag= SYLUtil.checkgivenTimeisValid(mtxtview_optiondate1.getText().toString(),mtxtview_optiontime1.getText().toString());
	if(!dateflag)
	{
		flag=false;
		Toast.makeText(getApplicationContext(), "Selected option1 date and time is not valid.Please select current/upcoming date and time", Toast.LENGTH_SHORT).show();
	}
}
if(!mtxtview_optiontime2.getText().toString().equals("Time") && !mtxtview_optiondate2.getText().equals("Option 2 Date"))
{

	boolean dateflag= SYLUtil.checkgivenTimeisValid(mtxtview_optiondate2.getText().toString(), mtxtview_optiontime2.getText().toString());
	if(!dateflag)
	{
		flag=false;
		Toast.makeText(getApplicationContext(), "Selected option2 date and time is not valid.Please select current/upcoming date and time", Toast.LENGTH_SHORT).show();
	}
}

		return flag;
	}
private boolean	validateSamedateAndtime()

{
	boolean flag=true;
	if(preferreddatetime1.equals(preferreddatetime2))

	{
		flag=false;
		Toast.makeText(getApplicationContext(), "Please select different date and time", Toast.LENGTH_SHORT).show();
	}
	if(preferreddatetime1.equals(preferreddatetime3))
	{
		flag=false;
		Toast.makeText(getApplicationContext(), "Please select different date and time", Toast.LENGTH_SHORT).show();
	}
	if(preferreddatetime2.equals(preferreddatetime3))
	{
		flag=false;
		Toast.makeText(getApplicationContext(), "Please select different date and time", Toast.LENGTH_SHORT).show();
	}


return flag;
}
	public void removeImageFromList(String option) {

		switch (option) {
		case "Skype":
			medium1imageArrayList.remove((Integer) R.drawable.skype_largeicon);
			break;
		case "SYL Videocall(in beta)":
			medium1imageArrayList.remove((Integer) R.drawable.opentok_icon);
			break;
		case "Mobile":
			medium1imageArrayList.remove((Integer) R.drawable.mobile_largeicon);
			break;
		case "Facetime":
			medium1imageArrayList.remove((Integer) R.drawable.facetime_icon);
			break;
		case "Hangout":
			medium1imageArrayList.remove((Integer) R.drawable.hangout_icon);
			break;

		default:
			throw new IllegalArgumentException("Invalid day of the week: ");
		}

	}

	private void addSelectedImage(String value, String prioritynumber) {
		switch (value) {
		case "Skype":
			medium1ArrayList.remove((Integer) R.drawable.skype_largeicon);
			if (prioritynumber.equals("1")) {
				mChooseoptionImageView
						.setImageResource(R.drawable.skype_largeicon);
			} else if (prioritynumber.equals("2")) {
				mChooseoptionimageview2
						.setImageResource(R.drawable.skype_largeicon);
			} else if (prioritynumber.equals("3")) {
				mChooseoptionimageview3
						.setImageResource(R.drawable.skype_largeicon);
			}
			break;
		case "SYL Videocall(in beta)":
			medium1ArrayList.remove((Integer) R.drawable.opentok_icon);
			if (prioritynumber.equals("1")) {
				mChooseoptionImageView
						.setImageResource(R.drawable.opentok_icon);
			} else if (prioritynumber.equals("2")) {
				mChooseoptionimageview2
						.setImageResource(R.drawable.opentok_icon);
			} else if (prioritynumber.equals("3")) {
				mChooseoptionimageview3
						.setImageResource(R.drawable.opentok_icon);
			}
			break;
		case "Mobile":
			medium1ArrayList.remove((Integer) R.drawable.mobile_largeicon);

			if (prioritynumber.equals("1")) {
				mChooseoptionImageView
						.setImageResource(R.drawable.mobile_largeicon);
			} else if (prioritynumber.equals("2")) {
				mChooseoptionimageview2
						.setImageResource(R.drawable.mobile_largeicon);
			} else if (prioritynumber.equals("3")) {
				mChooseoptionimageview3
						.setImageResource(R.drawable.mobile_largeicon);
			}

			break;
		case "Facetime":
			medium1ArrayList.remove((Integer) R.drawable.facetime_icon);
			if (prioritynumber.equals("1")) {
				mChooseoptionImageView
						.setImageResource(R.drawable.facetime_icon);
			} else if (prioritynumber.equals("2")) {
				mChooseoptionimageview2
						.setImageResource(R.drawable.facetime_icon);
			} else if (prioritynumber.equals("3")) {
				mChooseoptionimageview3
						.setImageResource(R.drawable.facetime_icon);
			}

			break;
		case "Hangout":
			medium1ArrayList.remove((Integer) R.drawable.hangout_icon);

			if (prioritynumber.equals("1")) {
				mChooseoptionImageView
						.setImageResource(R.drawable.hangout_icon);
			} else if (prioritynumber.equals("2")) {
				mChooseoptionimageview2
						.setImageResource(R.drawable.hangout_icon);
			} else if (prioritynumber.equals("3")) {
				mChooseoptionimageview3
						.setImageResource(R.drawable.hangout_icon);
			}

			break;

		default:
			throw new IllegalArgumentException("Invalid day of the week: ");
		}

	}

protected void sendEmail(String receipietemailaddress) {
    Log.i("Send email", "");

    String[] TO = {receipietemailaddress};

    Intent emailIntent = new Intent(Intent.ACTION_SEND);
    emailIntent.setData(Uri.parse("mailto:"));
    emailIntent.setType("text/plain");


    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SYL");
    emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(getSMSMessage()));

    try {
       startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."),2);

       Log.i("Finished sending email...", "");
    } catch (android.content.ActivityNotFoundException ex) {
       Toast.makeText(SYLRequestnewappointmentActivity.this, 
       "There is no email client installed.", Toast.LENGTH_SHORT).show();
    }
 }
 private Bundle  saveEnteredValues()
 {
		Bundle enterteddetails_bundle=new Bundle();
		enterteddetails_bundle.putString("appointmenttopic",mAppointmenttopicEdittExt.getText().toString());
		enterteddetails_bundle.putString("appointmentdescription",mAppointmentDescriptionEdittExt.getText().toString());
		enterteddetails_bundle.putString("choosepriority1",mtxtview_chooseoption1.getText().toString());
		enterteddetails_bundle.putString("choosepriority2",mtxtview_chooseoption2.getText().toString());
		enterteddetails_bundle.putString("choosepriority3", mtxtview_chooseoption3.getText().toString());
		enterteddetails_bundle.putString("preferredtime",mtxtview_preftime.getText().toString());
		enterteddetails_bundle.putString("preferreddate", mtxtview_preferreddate.getText().toString());
		enterteddetails_bundle.putString("optiontime1", mtxtview_optiontime1.getText().toString());
	
		enterteddetails_bundle.putString("optiondate1", mtxtview_optiondate1.getText().toString());
		enterteddetails_bundle.putString("optiontime2", mtxtview_optiontime2.getText().toString());
		enterteddetails_bundle.putString("optiondate2", mtxtview_optiondate2.getText().toString());
		
		
	
		return enterteddetails_bundle;
 }
private void checkandSetPreviousValues(Bundle bundle)
{
	if(bundle!=null)
	{
		mAppointmenttopicEdittExt.setText(bundle.getString("appointmenttopic"));
		mAppointmentDescriptionEdittExt.setText(bundle.getString("appointmentdescription"));
		mtxtview_chooseoption1.setText(bundle.getString("choosepriority1"));
	if(	!(bundle.getString("choosepriority1").equals("Choose priority 1")))
		{
	setImage(mtxtview_chooseoption1.getText().toString(),mChooseoptionImageView);
	}
		mtxtview_chooseoption2.setText(bundle.getString("choosepriority2"));
		if(	!(bundle.getString("choosepriority2").equals("Choose priority 2")))
		{
		setImage(mtxtview_chooseoption2.getText().toString(),	mChooseoptionimageview2);
		}
		mtxtview_chooseoption3.setText(bundle.getString("choosepriority3"));
		if(	!(bundle.getString("choosepriority3").equals("Choose priority 3"))){
		setImage(mtxtview_chooseoption3.getText().toString(),mChooseoptionimageview3);
		}
		mtxtview_preftime.setText(bundle.getString("preferredtime"));
		mtxtview_preferreddate.setText(bundle.getString("preferreddate"));
		mtxtview_optiontime1.setText(bundle.getString("optiontime1"));
		mtxtview_optiondate1.setText(bundle.getString("optiondate1"));
		mtxtview_optiontime2.setText(bundle.getString("optiontime2"));
		mtxtview_optiondate2.setText(bundle.getString("optiondate2"));
	}
}
@Override  
protected void onActivityResult(int requestCode, int resultCode, Intent data)  
{  
          super.onActivityResult(requestCode, resultCode, data);  
           // check if the request code is same as what is passed  here it is 2  
            if(requestCode==2)  
                  {  
             Log.e("redirect to appointments list", "redirect to appointments list");
         	Intent signinIntent = new Intent(
					SYLRequestnewappointmentActivity.this,
					SYLFragmentChangeActivity.class);
			signinIntent.putExtra("fragmentvalue", "newappointment");
					  signinIntent.putExtra("needrefresh", "true");
			SYLRequestnewappointmentActivity.this.startActivity(signinIntent);
					  finish();
                  }  
}

private void setImage(String option,ImageView optionImageView)
{
	switch (option) {
	case "Skype":
		optionImageView
		.setImageResource(R.drawable.skype_largeicon);
		break;
	case "SYL Videocall(in beta)":
	
		optionImageView
		.setImageResource(R.drawable.opentok_icon);
		break;
	case "Mobile":
	
		optionImageView
		.setImageResource(R.drawable.mobile_largeicon);
		break;
	case "Facetime":
		
		optionImageView
		.setImageResource(R.drawable.facetime_icon);
		break;
	case "Hangout":
		
		optionImageView
		.setImageResource(R.drawable.hangout_icon);
		break;

	default:
		throw new IllegalArgumentException("Invalid day of the week: ");
	}



}
private String  getSMSMessage()
{
	String appointment_topic,appointment_description,sms_priority1,sms_priority2,sms_priority3,sms_pref_datetime,sms_option1datetime,sms_option2datetime;

	

 appointment_topic=appointmenttopic;
		if(appointmentdescription.equals(""))	
		{
			appointment_description="Not given";
		}
		else {
			appointment_description=appointmentdescription;
		}
	
	sms_priority1=priority1;
	if(  priority2.equals("Choose priority 2") ||  priority2.equals("")           )
	{
		sms_priority2="Not given";
	}
	else {
		sms_priority2=priority2;
	}
	if(priority3.equals("Choose priority 3") ||  priority3.equals("")            )
	{
		sms_priority3="Not given";
	}
	else {
		sms_priority3=priority3;
	}
	sms_pref_datetime=preferreddatetime1;
	if (mtxtview_optiondate1.getText().toString().equals("Option 1 Date")) {
		sms_option1datetime="Not given";
	}
	else {
		sms_option1datetime=	preferreddatetime2;
	}
	if (mtxtview_optiondate2.getText().toString().equals("Option 2 Date")) {
		sms_option2datetime="Not given";
	}
	else{
		sms_option2datetime=	preferreddatetime3;
	}
	String sendername=SYLSaveValues.getSYLusername(SYLRequestnewappointmentActivity.this);
	String senderemailAddress=SYLSaveValues.getSYLEmailAddress(SYLRequestnewappointmentActivity.this);
	String receivername=fullname;
	String receiveremailaddress=email;
	String finalSMSBody1="You have been invited for a video call appointment via SYL"+"<br> "+
			"Sender Name "+sendername+"<br>"+" And Email "+senderemailAddress+"<br>"+" Invited"+ "<br>"+
			"Receiver name  "+receivername+
			" And  Email "+receiveremailaddress+"<br>"+
			"Appointment Topic:-"+appointment_topic+"<br>"+
			"Appointment Description:-"+appointment_description+"<br>"+
			"Priority 1 :"+sms_priority1+"<br>"+
			"Priority 2 :"+sms_priority2+"<br>"+
			"Priority 3 :"+sms_priority3+"<br>"+
			"Preferred DateTime-"+sms_pref_datetime+"<br>"+
			"Option1DateTime-"+sms_option1datetime+"<br>"+
			"Option2DateTime"+sms_option2datetime+"<br>"+
			"<p>If you want to confirm the appointment you have to download the  SYL app, available for free  on the Appstore or Playstore " +"<br>"+

			"Google Play Store :- <a href=\"https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en\">https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en</a></p>\n" +
			"   <p>Appstore :- <a href=\"https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8\" >https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8</a></p>";


	String finalSMSbody="Hello I like to make an appointment for a video call with you. Here are the details:-"
			+"Topic-"+appointment_topic+","+"Description-"+appointment_description+","+"Priority1-"+sms_priority1+","
			+"Priority2-"+sms_priority2+","+"Priority3-"+sms_priority3+","+"Preferred DateTime-"+sms_pref_datetime+","
			+"Option1DateTime-"+sms_option1datetime+","+"Option2DateTime"+sms_option2datetime+","
			+"<p>If you want to confirm the appointment you have to download the  SYL app, available for free  on the Appstore or Playstore \n" +
			"   Please visit following link :Android App Link :- <a href=\"https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en\">https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en</a></p>\n" +
			"   <p>iOS App Link :- <a href=\"https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8\" >https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8</a></p>";
			;
		//	return finalSMSbody;
	      return finalSMSBody1;
}

	private String  getSMSMessage1()
	{
		String finalSMSBody1="syl";
		String finalSMSBody2="syl";
		String finalSMSBody3="syl";
		String appointment_topic,appointment_description,sms_priority1,sms_priority2,sms_priority3,sms_pref_datetime,sms_option1datetime,sms_option2datetime;

try {

	appointment_topic = appointmenttopic;
	if (appointmentdescription.equals("")) {
		appointment_description = "Not given";
	} else {
		appointment_description = appointmentdescription;
	}

	sms_priority1 = priority1;
	if (priority2.equals("Choose priority 2") || priority2.equals("")) {
		sms_priority2 = "Not given";
	} else {
		sms_priority2 = priority2;
	}
	if (priority3.equals("Choose priority 3") || priority3.equals("")) {
		sms_priority3 = "Not given";
	} else {
		sms_priority3 = priority3;
	}
	sms_pref_datetime = preferreddatetime1;
	if (mtxtview_optiondate1.getText().toString().equals("Option 1 Date")) {
		sms_option1datetime = "Not given";
	} else {
		sms_option1datetime = preferreddatetime2;
	}
	if (mtxtview_optiondate2.getText().toString().equals("Option 2 Date")) {
		sms_option2datetime = "Not given";
	} else {
		sms_option2datetime = preferreddatetime3;
	}

	String sendername = SYLSaveValues.getSYLusername(SYLRequestnewappointmentActivity.this);
	String senderemailAddress = SYLSaveValues.getSYLEmailAddress(SYLRequestnewappointmentActivity.this);
	String receivername = fullname;
	String receiveremailaddress = email;
	finalSMSBody1 = "You have been invited for a video call appointment via SYL" + '\n' +
			"Sender Name " + sendername + " And Email " + senderemailAddress + '\n' + " Invited" + '\n' +
			"Receiver name  " + receivername +
			" And  Email " + receiveremailaddress + '\n' +
			"Appointment Topic:-" + appointment_topic + '\n' +
			"Appointment Description:-" + appointment_description + '\n' +
			"Priority 1 :" + sms_priority1 + '\n' +
			"Priority 2 :" + sms_priority2 + '\n' +
			"Priority 3 :" + sms_priority3 + '\n' +
			"Preferred DateTime-" + sms_pref_datetime + '\n' +
			"Option1DateTime-" + sms_option1datetime + '\n' +
			"Option2DateTime-" + sms_option2datetime + '\n' +



			"If you want to confirm the appointment you have to download the  SYL app, available for free  on the Appstore or Playstore " + '\n' +

			"Google Play Store :- " + "https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en" + '\n' +
			" Appstore :- " + "https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8";


	finalSMSBody3="You have been invited for a video call appointment via SYL." + '\n' +
			"Sender Name " + sendername  +  " Invited "  +
			  receivername +'\n'+
			"If you have already installed app in phone please open http://psylwebapp.seeyoulater.com/AppOpener.html. "+
			"if you are a new SYL user please download SYL from Play store https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en"+'\n'+
			"App Store Link https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8";







	String finalSMSbody = "Hello I like to make an appointment for a video call with you. Here are the details:-"
			+ "Topic-" + appointment_topic + "," + "Description-" + appointment_description + "," + "Priority1-" + sms_priority1 + ","
			+ "Priority2-" + sms_priority2 + "," + "Priority3-" + sms_priority3 + "," + "Preferred DateTime-" + sms_pref_datetime + ","
			+ "Option1DateTime-" + sms_option1datetime + "," + "Option2DateTime" + sms_option2datetime + ","
			+ "<p>If you want to confirm the appointment you have to download the  SYL app, available for free  on the Appstore or Playstore \n" +
			"   Please visit following link :Android App Link :- https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en\">https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en\n" +
			"   <p>iOS App Link :- <a href=\"https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8\" >https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8</a></p>";
	;
	//	return finalSMSbody;

 finalSMSBody2="You have been invited for a video call appointment by (Sender Name) via SYL. To view the details and confirm, please open the appointments page in SYL app. You can download SYL , for free, from Appstore(https://itunes.apple.com/us/app/syl/id1077227625?ls=1&mt=8) / Playstore(https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en\">https://play.google.com/store/apps/details?id=syl.webcamconsult.com.sysg&hl=en)"
		+"  if you have already installed app in device please open      http://dsylwebapp.seeyoulater.com/AppOpener.html"
		;






	if (finalSMSBody1.equals("")) {
		finalSMSBody1 = "cant send sms";
	}
}
catch (Exception e){

}
		return finalSMSBody3;
	}





	private void hideKeyboard()
	{
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(br);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.e("calling on start", "calling on start");


if(!SYLSaveValues.getSylHideguidednavigationnewappointment(SYLRequestnewappointmentActivity.this).equals("true")) {
	Intent i = new Intent(SYLRequestnewappointmentActivity.this, SYLGuidedNewAppointmentActivity.class);
	startActivity(i);

}



	}
}