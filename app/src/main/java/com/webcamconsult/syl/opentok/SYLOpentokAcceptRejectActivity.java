package com.webcamconsult.syl.opentok;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLConfirmedRequestAppointmentActivity;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.activities.SYLRescheduleappointmentActivity;
import com.webcamconsult.syl.activities.SYLSigninActivity;
import com.webcamconsult.syl.activities.SYLVerificationCodeActivity;
import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLChangeconferenceListener;
import com.webcamconsult.syl.interfaces.SYLFetchSessionStatusListener;
import com.webcamconsult.syl.model.SYLChangeConferenceResponeModel;
import com.webcamconsult.syl.model.SYLFetchAppointmentsDetails;
import com.webcamconsult.syl.model.SYLFetchSessionAppointmentResponseModel;
import com.webcamconsult.syl.opentok.send.HelloWorldActivityReceiver;
import com.webcamconsult.syl.opentok.send.HelloWorldActivitySender;
import com.webcamconsult.syl.opentok.send.HelloWorldActivitySender.CounterClass;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLChangeOpentokStatusViewmanager;
import com.webcamconsult.viewmanager.SYLFetchSessionStatusViewManager;

public class SYLOpentokAcceptRejectActivity extends Activity implements  SYLChangeconferenceListener,SYLFetchSessionStatusListener {
	String name, profileimageurl;
	TextView mNameTextview;
	ImageView muserImageView;
	ImageLoader imageLoader;
	String appointmentid;
	public String statuscode = "";
	CounterClass timer;
	String callstatuscode = "";
	MediaPlayer mediaPlayer;
	ProgressDialog msylProgressDialog;
	private int checkingcallstatuscounter=0;
	Timer checkingcallstatusTimer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sylconferencecallreceive_layout);
		mediaPlayer = new MediaPlayer();
		playReceiverRingtone();

		mNameTextview = (TextView) findViewById(R.id.txt_name);
		muserImageView = (ImageView) findViewById(R.id.row_icon);
		appointmentid = getIntent().getExtras().getString("appointmentid");
		name = getIntent().getExtras().getString("name");
		profileimageurl = getIntent().getExtras().getString("profileimageurl");
		mNameTextview.setText(name);
		ImageLoader imageLoader = new ImageLoader(SYLOpentokAcceptRejectActivity.this);
		imageLoader.DisplayImage(profileimageurl, muserImageView);
		timer = new CounterClass(22000, 1000);
		timer.start();
		TimerTask k=new TimerTask() {
			@Override
			public void run() {
				checkingcallstatuscounter++;
				Log.e("check call status","check call status");
				fetchSessionStatusForAppointment();
			}
		};
		checkingcallstatusTimer=new Timer();
		checkingcallstatusTimer.scheduleAtFixedRate(k, 1000, 3 * 1000);
	}

	public void navigatetoOpenTokchatscreen() {
		Intent resultIntent = new Intent(this, HelloWorldActivityReceiver.class);
		resultIntent.putExtra("sessionid", getIntent().getExtras().getString("sessionid"));
		resultIntent.putExtra("token", getIntent().getExtras().getString("token"));
		resultIntent.putExtra("apikey", getIntent().getExtras().getString("apikey"));
		resultIntent.putExtra("appointmentid", appointmentid);
		startActivity(resultIntent);
		finish();
	}

	private void playReceiverRingtone()
	{
		try {
			String packagename=getPackageName();

			Uri notificationsoundpath = Uri.parse("android.resource://" + packagename + "/" + R.raw.sylopentoktone);



			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			mediaPlayer.setDataSource(SYLOpentokAcceptRejectActivity.this, notificationsoundpath);

			mediaPlayer.prepare(); // might take long! (for buffering, etc)

			mediaPlayer.start();
		}
		catch (Exception e)
		{
Log.e("Media player error",e.getMessage());
		}
	}
	public void acceptOpentokcall(View v)
	{
		checkingcallstatusTimer.cancel();
		mediaPlayer.stop();
		statuscode="INCALL";
	  changeConferenceStatuscode("INCALL");
		timer.cancel();
	//	fetchSessionStatusForAppointment();
	}
	public void rejectOpentokcall(View v)
	{
		checkingcallstatusTimer.cancel();
		mediaPlayer.stop();
		timer.cancel();
		statuscode="REJECTED";
	  changeConferenceStatuscode("REJECTED");
	}
	private void changeConferenceStatuscode(String statuscode)
	{
		if (SYLUtil.isNetworkAvailable(SYLOpentokAcceptRejectActivity.this)) {

			msylProgressDialog = new ProgressDialog(SYLOpentokAcceptRejectActivity.this);
			msylProgressDialog.setMessage("Please wait...");
			msylProgressDialog.setCancelable(false);
			msylProgressDialog.setCanceledOnTouchOutside(false);
			msylProgressDialog.show();
			String appointmentid = getIntent().getExtras().getString("appointmentid");
			String timezone = SYLUtil
					.getTimeGMTZone(SYLOpentokAcceptRejectActivity.this);
			String status = statuscode;
			SYLChangeOpentokStatusViewmanager mChangeOpentokstausviewmanager = new SYLChangeOpentokStatusViewmanager();
			mChangeOpentokstausviewmanager.mConferenceChangeListener = SYLOpentokAcceptRejectActivity.this;
			mChangeOpentokstausviewmanager.changeOpentokStatus(status, timezone, appointmentid);
		}
		else {
			SYLUtil.buildAlertMessage(SYLOpentokAcceptRejectActivity.this,"Please check your network connection");
		}


	}
	@Override
	public void finishConferenceStstus(
			SYLChangeConferenceResponeModel msylChangeConferencemodel) {
		// TODO Auto-generated method stub

		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}


		if(msylChangeConferencemodel!=null)
		{
			if(msylChangeConferencemodel.isSuccess())
			{
if(msylChangeConferencemodel.isAlreadyInCall())
{
	showAlertMessage(SYLOpentokAcceptRejectActivity.this, "Logged in user already accepted the call in web app or mobile app");
	return;
}


				if(statuscode.equals("INCALL")){
				navigatetoOpenTokchatscreen();
				}
				else if(statuscode.equals("REJECTED"))
				{
	finish();
				}
				else if(statuscode.equals("UNANSWERED"))
				{
					finish();
				}
			}
			else {
				SYLUtil.buildAlertMessage(SYLOpentokAcceptRejectActivity.this, msylChangeConferencemodel.getError().getErrorTitle());
			}
		}
		else {
			SYLUtil.buildAlertMessage(this.getApplicationContext(), "Unexpected server error occured");
		}
	}
	
	
	private void navigatetoConfirmedAppointmentScreen(String appointmentid)
	{

		SYLFetchAppointmentsDetails mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);

		Intent intent = new Intent(
				SYLOpentokAcceptRejectActivity.this,
				SYLConfirmedRequestAppointmentActivity.class);
		String curr_userid = SYLSaveValues.getSYLUserID(SYLOpentokAcceptRejectActivity.this);
		String organizer_id = Integer.toString(mfetchappointmentdetails
				.getOrganizer().getUserId());
		String participnat_id = Integer.toString(mfetchappointmentdetails
				.getParticipant().getUserId());
		if (curr_userid.equals(organizer_id)) {
			intent.putExtra("name", mfetchappointmentdetails.getParticipant()
					.getName());
			int participantid = mfetchappointmentdetails.getParticipant()
					.getUserId();
			intent.putExtra("participantid", Integer.toString(participantid));
			intent.putExtra("profile_image", mfetchappointmentdetails
					.getParticipant().getProfileImage());
			intent.putExtra("hangoutid", mfetchappointmentdetails
					.getParticipant().getHangoutId());
		} else {
			intent.putExtra("name", mfetchappointmentdetails.getOrganizer()
					.getName());
			int participantid = mfetchappointmentdetails.getOrganizer()
					.getUserId();
			intent.putExtra("participantid", Integer.toString(participantid));
			intent.putExtra("profile_image", mfetchappointmentdetails
					.getOrganizer().getProfileImage());
			intent.putExtra("hangoutid", mfetchappointmentdetails
					.getOrganizer().getHangoutId());
		}

		intent.putExtra("topic", mfetchappointmentdetails.getTopic());
		intent.putExtra("description",
				mfetchappointmentdetails.getDescription());
		intent.putExtra("mobile", mfetchappointmentdetails.getParticipant()
				.getMobile());
		intent.putExtra("skypeid", mfetchappointmentdetails.getParticipant()
				.getSkypeId());
		intent.putExtra("priority1",
				mfetchappointmentdetails.getAppointmentPriority1());
		intent.putExtra("priority2",
				mfetchappointmentdetails.getAppointmentPriority2());
		intent.putExtra("priority3",
				mfetchappointmentdetails.getAppointmentPriority3());
		intent.putExtra("appointmentid", appointmentid);
		String date1 = mfetchappointmentdetails.getAppointmentDate1();
		String time1 = mfetchappointmentdetails.getAppointmentTime1();
		String date2 = mfetchappointmentdetails.getAppointmentDate2();
		String time2 = mfetchappointmentdetails.getAppointmentTime2();
		String date3 = mfetchappointmentdetails.getAppointmentDate3();
		String time3 = mfetchappointmentdetails.getAppointmentTime3();
		intent.putExtra("date1", date1 + " " + time1);

		if (date2.equals("") || time2.equals("")) {
			intent.putExtra("date2", "");
		} else {
			intent.putExtra("date2", date2 + " " + time2);
		}
		if (date3.equals("") || time3.equals("")) {
			intent.putExtra("date3", "");
		} else {
			intent.putExtra("date3", date3 + " " + time3);
		}
		startActivity(intent);
		finish();

		}
	private SYLFetchAppointmentsDetails getAppointmentDetailsFromDB(String appointmentid) {
		SYLFetchAppointmentsDetails mfetchappointmentdetailsobject = null;
		SYLAppointmentdetailsdatamanager datamanager = new SYLAppointmentdetailsdatamanager(
				SYLOpentokAcceptRejectActivity.this);
		mfetchappointmentdetailsobject = datamanager
				.getAllAppointmentFetchDetails("TodaysAppointments",appointmentid);
		return mfetchappointmentdetailsobject;
	}
	
	
	public class CounterClass extends CountDownTimer {
		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			Log.e("timer finished", "timer finished");
			//navigatetoConfirmedAppointmentScreen(appointmentid);

			finish();

		if(statuscode.equals(""))
		{
			//statuscode="UNANSWERED";
			 // changeConferenceStatuscode("UNANSWERED");
		}
		}

		@Override
		public void onTick(long millisUntilFinished) {

			long millis = millisUntilFinished;
			String hms = String.format(
					"%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(millis),
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis)));
			System.out.println(hms);

			Log.e("time-accept-reject", hms);
		}
	}

	private void  showAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(SYLOpentokAcceptRejectActivity.this);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {

								dialog.cancel();
					finish();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}

	private void fetchSessionStatusForAppointment() {
		String accesstoken = SYLSaveValues.getSYLaccessToken(SYLOpentokAcceptRejectActivity.this);
		String timezone = SYLUtil.getTimeGMTZone(SYLOpentokAcceptRejectActivity.this);
		SYLFetchSessionStatusViewManager mViewmanager = new SYLFetchSessionStatusViewManager();
		mViewmanager.msylFetchSessionStatusListener = SYLOpentokAcceptRejectActivity.this;
		mViewmanager.fetchSessionStatus(appointmentid, accesstoken, timezone);
	}
	@Override
	public void finishFetchSessionAppointmentStatus(
			SYLFetchSessionAppointmentResponseModel mSylFetchsessionappointmentresponsemodel) {
try {
	if (mSylFetchsessionappointmentresponsemodel.getSessionStatusCode().equals("FINISHED")) {
		showAlertMessage(SYLOpentokAcceptRejectActivity.this,
				"SYL user ended the call");
		checkingcallstatusTimer.cancel();
		mediaPlayer.stop();
	}

}
catch(Exception e)
{

}



	}
}
