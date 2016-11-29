package com.webcamconsult.syl.activities;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.fragments.SYLDatePickerFragment;
import com.webcamconsult.syl.fragments.SYLPrioritySelectDialogFragment;
import com.webcamconsult.syl.fragments.SYLTimePickerFragment;
import com.webcamconsult.syl.interfaces.SYLCreatenewAppointmentListener;
import com.webcamconsult.syl.interfaces.SYLDateListener;
import com.webcamconsult.syl.interfaces.SYLPrioritySelectListener;
import com.webcamconsult.syl.interfaces.SYLTimeListener;
import com.webcamconsult.syl.model.SYLCreateNewAppointmentResponse;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLCreateNewAppointmentViewManager;

public class SYLRescheduleappointmentActivity extends FragmentActivity
		implements SYLDateListener, SYLTimeListener, SYLPrioritySelectListener,
		SYLCreatenewAppointmentListener {

	String[] objects1 = { "Skype", "opentok", "Mobile" };
	String[] objects2 = { "opentok", "Mobile", "skype" };
	String[] objects3 = { "Mobile", "opentok", "Skype" };
	TextView mtxtview_preferreddate, mtxtview_optiondate1,
			mtxtview_optiondate2;
	TextView mtxtview_preftime, mtxtview_optiontime1, mtxtview_optiontime2;
	int total_images1[] = { R.drawable.skype_largeicon,
			R.drawable.mobile_largeicon, R.drawable.mobile_largeicon };
	int total_images2[] = { R.drawable.mobile_largeicon,
			R.drawable.mobile_largeicon, R.drawable.skype_largeicon, };
	int total_images3[] = { R.drawable.mobile_largeicon,
			R.drawable.mobile_largeicon, R.drawable.skype_largeicon };
	ImageView mbackarrowImageview;
	RelativeLayout mChooseoption1relativelayout, mChooseoptionrelativelayout2,
			mChooseoptionrelativelayout3;
	ArrayList<String> medium1ArrayList = new ArrayList<String>();
	ArrayList<Integer> medium1imageArrayList = new ArrayList<Integer>();

	LinearLayout mLinearPreferreddate, mLinearoptiondate1, mLinearOptiondate2;
	LinearLayout mLinearPreferredtime, mLineartimeoption1, mLineartimeoption2;
	TextView mtxtview_chooseoption1, mtxtview_chooseoption2,
			mtxtview_chooseoption3;
	ImageView mChooseoptionImageView, mChooseoptionimageview2,
			mChooseoptionimageview3;
	String participantid, name, appointmenttopic, appointmentdescription,
			date1, date2, date3, priority1, priority2, priority3;
	String time1, time2, time3;
	String time1split[], time2split[], time3split[];
	TextView txtview_importcontactname;
	TextView mAppointmentTopicEdittext,mAppointmentDescriptionEdittext;
	ProgressDialog msylProgressDialog;
	BroadcastReceiver br;

	protected void onCreate(Bundle savedInstancestate) {
		super.onCreate(savedInstancestate);

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



		setContentView(R.layout.syl_rescheduleappointment);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		medium1ArrayList.add("Skype");
		medium1ArrayList.add("SYL Videocall(in beta)");
		medium1ArrayList.add("Mobile");
		medium1ArrayList.add("Facetime");
		medium1ArrayList.add("Hangout");
		medium1imageArrayList.add(R.drawable.skype_largeicon);
		medium1imageArrayList.add(R.drawable.opentok_icon);
		medium1imageArrayList.add(R.drawable.mobile_largeicon);
		medium1imageArrayList.add(R.drawable.facetime_icon);
		medium1imageArrayList.add(R.drawable.hangout_icon);

		txtview_importcontactname = (TextView) findViewById(R.id.txt_importcontact);

		mtxtview_preferreddate = (TextView) findViewById(R.id.preftxt_date);
		mtxtview_optiondate1 = (TextView) findViewById(R.id.txt_optiondate1);
		mtxtview_optiondate2 = (TextView) findViewById(R.id.txtoptiondate2);
		mtxtview_optiontime1 = (TextView) findViewById(R.id.txt_optiontime1);
		mtxtview_optiontime2 = (TextView) findViewById(R.id.txt_optiontime2);
		mtxtview_preftime = (TextView) findViewById(R.id.pref_txttime);
		mbackarrowImageview = (ImageView) findViewById(R.id.img_backarrow);

		mLinearPreferreddate = (LinearLayout) findViewById(R.id.liout_prefereddate);
		mLinearoptiondate1 = (LinearLayout) findViewById(R.id.liout_optiondate1);
		mLinearOptiondate2 = (LinearLayout) findViewById(R.id.liout_optiondate2);
		mLinearOptiondate2 = (LinearLayout) findViewById(R.id.liout_optiondate2);
		mLinearPreferredtime = (LinearLayout) findViewById(R.id.liout_preferedtime);
		mLineartimeoption1 = (LinearLayout) findViewById(R.id.liout_optiontime1);
		mLineartimeoption2 = (LinearLayout) findViewById(R.id.liout_preferedtime2);
		mChooseoption1relativelayout = (RelativeLayout) findViewById(R.id.relout_chooseoption1);
		mChooseoptionrelativelayout2 = (RelativeLayout) findViewById(R.id.relout_chooseoption2);
		mChooseoptionrelativelayout3 = (RelativeLayout) findViewById(R.id.relout_chooseoption3);
		mtxtview_chooseoption1 = (TextView) findViewById(R.id.txt_chooseoption);
		mtxtview_chooseoption2 = (TextView) findViewById(R.id.txt_chooseoption2);
		mtxtview_chooseoption3 = (TextView) findViewById(R.id.txt_chooseoption3);
		mChooseoptionImageView = (ImageView) findViewById(R.id.img_selectimage);
		mChooseoptionimageview2 = (ImageView) findViewById(R.id.img_selectimage2);
		mChooseoptionimageview3 = (ImageView) findViewById(R.id.img_selectimage3);
		mAppointmentTopicEdittext=(TextView)findViewById(R.id.txt_appointmenttopic);
		mAppointmentDescriptionEdittext=(TextView)findViewById(R.id.txt_appointmentdescription);
		getIntentValuesandsetFields();
		mLinearPreferreddate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePickerDialog("preferred");
			}
		});
		mLinearoptiondate1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePickerDialog("option1date");
			}
		});
		mLinearOptiondate2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePickerDialog("option2date");
			}
		});
		mLinearPreferredtime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog("preferredtime");
			}
		});
		mLineartimeoption1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog("timeoption1");
			}
		});
		mLineartimeoption2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog("timeoption2");
			}
		});
		mChooseoption1relativelayout
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						medium1ArrayList.clear();
						medium1ArrayList.add("Skype");
						medium1ArrayList.add("SYL Videocall(in beta)");
						medium1ArrayList.add("Mobile");
						medium1ArrayList.add("Facetime");
						medium1ArrayList.add("Hangout");
						medium1imageArrayList.clear();
						medium1imageArrayList.add(R.drawable.skype_largeicon);
						medium1imageArrayList.add(R.drawable.opentok_icon);
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
									SYLRescheduleappointmentActivity.this,
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
									SYLRescheduleappointmentActivity.this,
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

	public void navigatetoConfirmedAppointmentsscreen(View v) {
		// Intent intent = new Intent(SYLRescheduleappointmentActivity.this,
		// SYLFragmentChangeActivity.class);
		// intent.putExtra("fragmentvalue", "newappointment");
		// startActivity(intent);
		onBackPressed();
	}

	private void showDatePickerDialog(String datetype) {
		SYLDatePickerFragment mdatepickerfragment = new SYLDatePickerFragment();

		mdatepickerfragment.mDateListener = SYLRescheduleappointmentActivity.this;

		mdatepickerfragment.show(getSupportFragmentManager(), datetype);

	}

	public void showTimePickerDialog(String timeoptiontype) {
		SYLTimePickerFragment mtimepickerfragment = new SYLTimePickerFragment();
		mtimepickerfragment.mTimeListener = SYLRescheduleappointmentActivity.this;
		mtimepickerfragment.show(getSupportFragmentManager(), timeoptiontype);

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
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent intent = null;
		if (getIntent().getStringExtra("intentfrom").equals("confirmed")) {
			intent = new Intent(SYLRescheduleappointmentActivity.this,
					SYLConfirmedRequestAppointmentActivity.class);
		} else if (getIntent().getStringExtra("intentfrom").equals(
				"requestsend")) {
			intent = new Intent(SYLRescheduleappointmentActivity.this,
					SYLRequestSendActivity.class);
		} else if (getIntent().getStringExtra("intentfrom").equals(
				"todaysappointment")) {
			intent = new Intent(SYLRescheduleappointmentActivity.this,
					SYLTodaysAppointmentDetailActivity.class);
		}
		else if (getIntent().getStringExtra("intentfrom").equals(
				"cancelled")) {
			intent = new Intent(SYLRescheduleappointmentActivity.this,
					SYLCaneledRequestDetailedActivity.class);
		}

		intent.putExtra("name", getIntent().getStringExtra("name"));
		intent.putExtra("appointmentid", getIntent().getStringExtra("appointmentid"));
		intent.putExtra("profile_image",
				getIntent().getStringExtra("profile_image"));
		intent.putExtra("topic", getIntent().getStringExtra("topic"));
		intent.putExtra("description", getIntent()
				.getStringExtra("description"));
		intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
		intent.putExtra("skypeid", getIntent().getStringExtra("skypeid"));
		intent.putExtra("priority1", getIntent().getStringExtra("priority1"));
		intent.putExtra("priority2", getIntent().getStringExtra("priority2"));
		intent.putExtra("priority3", getIntent().getStringExtra("priority3"));
		intent.putExtra("date1", getIntent().getStringExtra("date1"));
		intent.putExtra("date2", getIntent().getStringExtra("date2"));
		intent.putExtra("date3", getIntent().getStringExtra("date3"));
		intent.putExtra("hangoutid", getIntent().getStringExtra("hangoutid"));
		intent.putExtra("facetimeid", getIntent().getStringExtra("facetimeid"));
		intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
		intent.putExtra("profile_image", getIntent().getStringExtra("profile_image"));
		intent.putExtra("intentfrom", getIntent().getStringExtra("intentname"));
	
		startActivity(intent);
		finish();
	}

	private void getIntentValuesandsetFields() {
		name = getIntent().getStringExtra("name");
		appointmenttopic = getIntent().getStringExtra("topic");
		appointmentdescription = getIntent().getStringExtra("description");
		priority1 = getIntent().getStringExtra("priority1");
		setPriorityoneImage(priority1, "1");
		priority2 = getIntent().getStringExtra("priority2");
		if(!priority2.equals("Choose priority 2")){
	setPriorityoneImage(priority2, "2");}
		priority3 = getIntent().getStringExtra("priority3");
		if(!priority3.equals("Choose priority 3")) {
			setPriorityoneImage(priority3, "3");
		}
		txtview_importcontactname.setText(name);
mAppointmentTopicEdittext.setText(appointmenttopic);
		mAppointmentDescriptionEdittext.setText(appointmentdescription);
		mtxtview_chooseoption1.setText(priority1);
		mtxtview_chooseoption2.setText(priority2);
		mtxtview_chooseoption3.setText(priority3);
		date1 = getIntent().getStringExtra("date1");
		date2 = getIntent().getStringExtra("date2");
		date3 = getIntent().getStringExtra("date3");
		time1split = date1.split(" ");
		mtxtview_preftime.setText(time1split[1].toString());
		if(!date2.equals(" ")                       )
		{
		time2split = date2.split(" ");
		mtxtview_optiontime1.setText(time2split[1].toString());
		}
		if(!date3.equals(" ")){
		time3split = date3.split(" ");
		
	
		mtxtview_optiontime2.setText(time3split[1].toString());}
		mtxtview_preferreddate.setText(time1split[0].toString().replace("-",
				"/"));
		if(!date2.equals(" ")){
		mtxtview_optiondate1
				.setText(time2split[0].toString().replace("-", "/"));
		}
		if(!date3.equals(" ")){
		mtxtview_optiondate2
				.setText(time3split[0].toString().replace("-", "/"));}
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

	public void doRescheduleAppointment(View v) {

if(validateDateAndTime())
{


	String url = SYLUtil.SYL_BASEURL + "api/Appointment/ReSchedule";
	String appointmentid = getIntent().getStringExtra("appointmentid");
	String syl_userid = SYLSaveValues
			.getSYLUserID(SYLRescheduleappointmentActivity.this);
	participantid = getIntent().getStringExtra("participantid");
	String appointmenttopic = mAppointmentTopicEdittext.getText().toString();
	String appointmentdescription = mAppointmentDescriptionEdittext
			.getText().toString();
	String preferddate = mtxtview_preferreddate.getText().toString();
	String preftime = mtxtview_preftime.getText().toString();
	String prefdate_time = preferddate + "T" + preftime;
	String optindate1 = mtxtview_optiondate1.getText().toString();
	String optiontime1 = mtxtview_optiontime1.getText().toString();
	String optiondate_time1 = optindate1 + "T" + optiontime1;
	String optiondate2 = mtxtview_optiondate2.getText().toString();
	String optiontime2 = mtxtview_optiontime2.getText().toString();
	String optiondate_time2 = optiondate2 + "T" + optiontime2;
	String priority1 = mtxtview_chooseoption1.getText().toString();


	String priority2 = mtxtview_chooseoption2.getText().toString();
	if (priority2.equals("Choose priority 2")) {
		priority2 = "";
	}
	String priority3 = mtxtview_chooseoption3.getText().toString();
	if (priority3.equals("Choose priority 3")) {
		priority3 = "";
	}
if(validatesameDateAndTime(prefdate_time,optiondate_time1,optiondate_time2)) {

	msylProgressDialog = new ProgressDialog(
			SYLRescheduleappointmentActivity.this);
	msylProgressDialog.setMessage("Please wait...");
	msylProgressDialog.setCancelable(false);
	msylProgressDialog.setCanceledOnTouchOutside(false);
	msylProgressDialog.show();

	String accesstoken = SYLSaveValues
			.getSYLaccessToken(SYLRescheduleappointmentActivity.this);
	String timezone = SYLUtil
			.getTimeGMTZone(SYLRescheduleappointmentActivity.this);

	SYLCreateNewAppointmentViewManager mviewmanager = new SYLCreateNewAppointmentViewManager();
	mviewmanager.msylcreatenewappointmentlistener = SYLRescheduleappointmentActivity.this;
	mviewmanager.createnewAppointment(Integer.parseInt(syl_userid), "SYL",
			"", "", "", appointmenttopic, appointmentdescription,
			priority1, priority2, priority3, prefdate_time,
			optiondate_time1, optiondate_time2, timezone, accesstoken,
			participantid, appointmentid, url);
}
}
	}

	@Override
	public void getCreatenewAppointmentFinish(
			SYLCreateNewAppointmentResponse msylcreatenewappointmentresponse)

	{

		// TODO Auto-generated method stub
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		try {
			if (msylcreatenewappointmentresponse != null) {
				if (msylcreatenewappointmentresponse.isSuccess()) {
					buildAlertMessage(SYLRescheduleappointmentActivity.this, "Appointment has been rescheduled successfully");
				} else {
					SYLUtil.buildAlertMessage(
							SYLRescheduleappointmentActivity.this,
							"Rescheduling  Appointment Failed");
				}
			} else {
				SYLUtil.buildAlertMessage(SYLRescheduleappointmentActivity.this,
						"Unexpeted Server Error Occured");
			}
		}
		catch (Exception e)
		{
			SYLUtil.buildAlertMessage(SYLRescheduleappointmentActivity.this,
					"Unexpeted Server Error Occured");
		}
	}
private void setPriorityoneImage(String priority,String prioritynumber)
{
	switch (priority) {
	case "Mobile":
		if(prioritynumber.equals("1"))
		{
		mChooseoptionImageView
		.setImageResource(R.drawable.mobile_largeicon);
		}
		else if(prioritynumber.equals("2"))
		{
			mChooseoptionimageview2
			.setImageResource(R.drawable.mobile_largeicon);
		}
		else if(prioritynumber.equals("3"))
		{
			mChooseoptionimageview3
			.setImageResource(R.drawable.mobile_largeicon);
		}
		break;
	case "SYL Videocall(in beta)":
		if(prioritynumber.equals("1"))
		{
		mChooseoptionImageView
		.setImageResource(R.drawable.opentok_icon);
		}
		else if(prioritynumber.equals("2"))
		{
			mChooseoptionimageview2
			.setImageResource(R.drawable.opentok_icon);
		}
		else if(prioritynumber.equals("3"))
		{
			mChooseoptionimageview3
			.setImageResource(R.drawable.opentok_icon);
		}
		break;
	case "Skype":
		if(prioritynumber.equals("1"))
		{
		mChooseoptionImageView
		.setImageResource(R.drawable.skype_largeicon);
		}
		else if(prioritynumber.equals("2"))
		{
			mChooseoptionimageview2
			.setImageResource(R.drawable.skype_largeicon);
		}
		else if(prioritynumber.equals("3"))
		{
			mChooseoptionimageview3
			.setImageResource(R.drawable.skype_largeicon);
		}
		break;
	case "Facetime":
		if(prioritynumber.equals("1"))
		{
		mChooseoptionImageView
		.setImageResource(R.drawable.facetime_icon);
		}
		else if(prioritynumber.equals("2"))
		{
			mChooseoptionimageview2
			.setImageResource(R.drawable.facetime_icon);
		}
		else if(prioritynumber.equals("3"))
		{
			mChooseoptionimageview3
			.setImageResource(R.drawable.facetime_icon);
		}
		break;
	case "Hangout":
		if(prioritynumber.equals("1"))
		{
		mChooseoptionImageView
		.setImageResource(R.drawable.hangout_icon);
		}
		else if(prioritynumber.equals("2"))
		{
			mChooseoptionimageview2
			.setImageResource(R.drawable.hangout_icon);
		}
		else if(prioritynumber.equals("3"))
		{
			mChooseoptionimageview3
			.setImageResource(R.drawable.hangout_icon);
		}
		break;
	default:
		break;
	}
	
	
	}

	private boolean validateDateAndTime()
	{
		boolean flag = true;


		if(mtxtview_optiondate1.getText().equals("Option 1 Date")&& !mtxtview_optiontime1.getText().equals("Time"))
		{
			flag = false;
			Toast.makeText(SYLRescheduleappointmentActivity.this,
					"Please select option 1 date", Toast.LENGTH_LONG).show();
		}

		if(mtxtview_optiontime1.getText().equals("Time") && !mtxtview_optiondate1.getText().equals("Option 1 Date") )
		{
			flag = false;
			Toast.makeText(SYLRescheduleappointmentActivity.this,
					"Please select option 1 time", Toast.LENGTH_LONG).show();
		}

		if(mtxtview_optiontime2.getText().toString().equals("Time") &&  !mtxtview_optiondate2.getText().equals("Option 2 Date")    )
		{
			flag = false;
			Toast.makeText(SYLRescheduleappointmentActivity.this,
					"Please select option 2 time", Toast.LENGTH_LONG).show();
		}
		if(!mtxtview_optiontime2.getText().toString().equals("Time") &&  mtxtview_optiondate2.getText().equals("Option 2 Date"))
		{
			flag = false;
			Toast.makeText(SYLRescheduleappointmentActivity.this,
					"Please select option 2 date", Toast.LENGTH_LONG).show();
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

private boolean validatesameDateAndTime(String prefdatetime,String date_time1,String date_time2)
{
boolean	flag=true;
if(date_time1.equals(date_time2) || prefdatetime.equals(date_time1) || prefdatetime.equals(date_time2)     )
{
	flag=false;
	Toast.makeText(SYLRescheduleappointmentActivity.this, "Please select different date and time", Toast.LENGTH_SHORT).show();
}
	return flag;
}





	private  void buildAlertMessage(Context context, String message) {
		try {
			final AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(message)
					.setCancelable(false)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(final DialogInterface dialog,
													final int id) {

									dialog.cancel();
									navigatetoAppointmentsListingScreen("true");
								}
							});

			final AlertDialog alert = builder.create();
			alert.show();
		}
		catch (Exception e)
		{
			Log.e("Error",e.getMessage());
		}
	}

	private void navigatetoAppointmentsListingScreen(String refreshvalue) {

		Intent intent = new Intent(SYLRescheduleappointmentActivity.this,
				SYLFragmentChangeActivity.class);
		intent.putExtra("fragmentvalue", "newappointment");
		intent.putExtra("needrefresh",refreshvalue);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(br);
	}

}
