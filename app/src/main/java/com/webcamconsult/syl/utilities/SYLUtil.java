package com.webcamconsult.syl.utilities;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLHistoryAppointmentsdatamanager;
import com.webcamconsult.syl.model.SYLFetchAppointmentsDetails;

public class SYLUtil {
	/*Server base url for development */
public static final String SYL_BASEURL="http://dsylbackend.seeyoulater.com/";
	/*Server base url for production*/
//public static final String SYL_BASEURL="http://psylbackend.seeyoulater.com/";
	/*Server base url for testing*/
//public static final String SYL_BASEURL="http://tsylbackend.seeyoulater.com/";


	public static final String TAG = "SYLUtility";

	public static void goToGitHub(Context context) {
		Uri uriUrl = Uri.parse("http://github.com/jfeinstein10/slidingmenu");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		context.startActivity(launchBrowser);
	}
/*Method is for checking network availability*/
	public static boolean isNetworkAvailable(Context context) {
		Log.v(TAG, "isNetworkAvailable called.");
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm.getActiveNetworkInfo() == null) {
			Log.d(TAG, "no active network info found.");
			return false;
		}

		Log.v(TAG, "active network info found.");
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
/*Method is for showing the Alert Dialog*/
	public static void buildAlertMessage(Context context, String message) {
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

	public static String getAccountName(
			SYLFragmentChangeActivity mSylFragmentChangeAcyivity) {
		String accountname = null;

		try {
			Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
			Account[] accounts = AccountManager.get(mSylFragmentChangeAcyivity)
					.getAccounts();
			for (Account account : accounts) {
				if (emailPattern.matcher(account.name).matches()) {
					String possibleEmail = account.name;
					if (possibleEmail.contains("@gmail.com")) {
						accountname = possibleEmail;
						return accountname;
					}
				}
			}
		} catch (Exception e) {
			accountname = "Error";
		}
		return accountname;
	}

	public static boolean appInstalledOrNot(String uri,
			SYLRequestnewappointmentActivity mnewAppointmentActivity) {
		PackageManager pm = mnewAppointmentActivity.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	public static boolean appInstalledOrNot(String uri, Activity activity) {
		PackageManager pm = activity.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	public static String getUID(Context context) {
		try {
			String deviceId = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
			return deviceId;
		} catch (Exception e) {
			return "nodeviceid";
		}
	}

	public static String getTimeZone(Context context) {

		Calendar cal = Calendar.getInstance();
		TimeZone tz = cal.getTimeZone();

		return tz.getDisplayName();

	}

	public static String getTimeGMTZone(Context context) {
		String GMTtime;
/*
		try {
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
					"yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

			// Local time zone
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat(
					"yyyy-MMM-dd HH:mm:ss");

			// Time in GMT
			Date d = dateFormatLocal.parse(dateFormatGmt.format(new Date()));

			String date1 = d.toString();
			String[] date1array = date1.split(" ");
			Log.e("date", date1array[4].toString());
			String date2[] = date1array[4].split("GMT");
			String k = date2[1].toString();
			Log.e("date2", k);
			GMTtime = k;
		} catch (Exception e) {
			GMTtime = "error";
		}
		return GMTtime;
*/
		try {
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
			String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
			return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);

		}
		catch (Exception e){
			return "error";
		}








	}

	public static boolean validateEmail(EditText editText) {
		if ((editText.getText().toString().trim().equals(""))) {
			return false;
		} else {
			//Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
			Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[A-Za-z0-9.-]+\\.+[A-Za-z]+");
			String email = (editText.getText().toString().trim());

			Matcher matcher = pattern.matcher(email);

			if (matcher.matches()) {

				return true;
			}

		}
		return false;
	}

	public static String getBackendDatetimeFormat(String datetimemobile) {
		String dateArray[];
		String dateString;
		String dateFormatArray[];
		String timesplitarray[];
		String dateformat = "";
		dateArray = datetimemobile.split("T");
		timesplitarray = dateArray[1].split(":");
		if (timesplitarray[0].toString().length() < 2) {
			timesplitarray[0] = "0" + timesplitarray[0];
			dateArray[1] = timesplitarray[0] + ":" + timesplitarray[1];
		}
		dateString = dateArray[0].toString();
		dateFormatArray = dateString.split("/");
		if (dateFormatArray[0].toString().length() < 2) {
			dateFormatArray[0] = "0" + dateFormatArray[0];
		}
		dateformat = dateFormatArray[2] + "-" + dateFormatArray[1] + "-"
				+ dateFormatArray[0] + "T" + dateArray[1];
		return dateformat;
	}

	public static byte[] getBytes(Bitmap bitmap) {
		if (bitmap != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0, stream);
			return stream.toByteArray();
		} else {
			return null;
		}
	}

	// convert from byte array to bitmap
	public static Bitmap getImage(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}

	public static String getContactSource(String contacttype)

	{
		String contactsource = "";

		switch (contacttype) {
		case "favourites":
			contactsource = "SYL";
			break;
		case "phonecontacts":
			contactsource = "PHONE";

			break;
		case "sylcontacts":
			contactsource = "SYL";
			break;
		case "gmailcontacts":
			contactsource = "GOOGLE";
			break;

		default:
			contactsource = "nocontactsource";

		}

		return contactsource;
	}

	public static void pushAppointmentsToCalender(int appointment_id,Activity curActivity, String title,
			String addInfo, String place, int status, long startDate,
			boolean needReminder, boolean needMailService,Context c) {
		try {
		String eventUriString = "content://com.android.calendar/events";
		ContentValues eventValues = new ContentValues();
		TimeZone timeZone = TimeZone.getDefault();

		eventValues.put("calendar_id", 1); // id, We need to choose from
		// our mobile for primary
		// its 1
		eventValues.put("title", title);
		eventValues.put("description", addInfo);
		eventValues.put("eventLocation", place);

		long endDate = startDate + 1000 * 60 * 60; // For next 1hr

		eventValues.put("dtstart", startDate);
		eventValues.put("dtend", endDate);

		// values.put("allDay", 1); //If it is bithday alarm or such
		// kind (which should remind me for whole day) 0 for false, 1
		// for true
		eventValues.put("eventStatus", status); // This information is
		eventValues.put("eventTimezone", "Europe/London");
		// sufficient for most
		// entries tentative (0),
		// confirmed (1) or canceled
		// (2):

		/*
		 * Comment below visibility and transparency column to avoid
		 * java.lang.IllegalArgumentException column visibility is invalid error
		 */

		/*
		 * eventValues.put("visibility", 3); // visibility to default (0), //
		 * confidential (1), private // (2), or public (3):
		 * eventValues.put("transparency", 0); // You can control whether // an
		 * event consumes time // opaque (0) or transparent // (1).
		 */

		eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

		Uri eventUri = curActivity.getApplicationContext().getContentResolver()
				.insert(Uri.parse(eventUriString), eventValues);
		long eventID = Long.parseLong(eventUri.getLastPathSegment());
		
		SYLAppointmentdetailsdatamanager mmanager=new SYLAppointmentdetailsdatamanager(c);
		mmanager.insertcalendarEvevtIDs(Integer.toString(appointment_id),Long.toString(eventID), c);
	

		if (needReminder) {
			/***************** Event: Reminder(with alert) Adding reminder to event *******************/

			String reminderUriString = "content://com.android.calendar/reminders";

			ContentValues reminderValues = new ContentValues();

			reminderValues.put("event_id", eventID);
			reminderValues.put("minutes", 5); // Default value of the
												// system. Minutes is a
												// integer
			reminderValues.put("method", 1); // Alert Methods: Default(0),
												// Alert(1), Email(2),
												// SMS(3)

			Uri reminderUri = curActivity.getApplicationContext()
					.getContentResolver()
					.insert(Uri.parse(reminderUriString), reminderValues);

		}

		/***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

		if (needMailService) {
			String attendeuesesUriString = "content://com.android.calendar/attendees";

			/********
			 * To add multiple attendees need to insert ContentValues multiple
			 * times
			 ***********/
			ContentValues attendeesValues = new ContentValues();

			attendeesValues.put("event_id", eventID);
			attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
			attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
																	// E
																	// mail
																	// id
			attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
															// Relationship_None(0),
															// Organizer(2),
															// Performer(3),
															// Speaker(4)
			attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
													// Required(2), Resource(3)
			attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
														// Decline(2),
														// Invited(3),
														// Tentative(4)

			Uri attendeuesesUri = curActivity.getApplicationContext()
					.getContentResolver()
					.insert(Uri.parse(attendeuesesUriString), attendeesValues);
		}
		}
		catch(Exception e)
		{
			Log.e("calendar exception",e.getMessage());
		}
		return;
	}
	public static String splitGetAppointmentid(String appointmentdetail)
	{
		String[] appointmentid=appointmentdetail.split("_");
		String appointment_id=appointmentid[6].toString();
		return appointment_id;
		
	}
	public static boolean checkPreferredDate(TextView mPrefdateTextView)
	{
if(mPrefdateTextView.getText().equals("Preferred Date"))
{
	return false;
}
		else {
	return true;
}
	}


	public static boolean checkOptionTime(TextView mTimeTextView)
	{
if(mTimeTextView.getText().equals("Time"))
{
	return  false;
}
		else {
	return true;
}
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}


public static boolean checkSimisPresent(Context mContext)
{

	boolean value=false;
	TelephonyManager telMgr = (TelephonyManager)mContext.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
	int simState = telMgr.getSimState();
	switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
		value=false;
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
	value=false;
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
		value=false;
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			value=false;
			break;
		case TelephonyManager.SIM_STATE_READY:
		value=true;
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			value=false;
			break;
	}

return value;
}
public static void getCurrentTime()
{
	Calendar c = Calendar.getInstance();
	int hour = c.get(Calendar.HOUR_OF_DAY);
	int min=c.get(Calendar.MINUTE);
}
public static boolean checkgivenTimeisValid(String date,String time)
{
	boolean flag=false;
	try {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String currentDateandTime = sdf.format(new Date());
		Date current_date = sdf.parse(currentDateandTime);

		String str2 = date+" "+time;
		Date selecteddate = sdf.parse(str2);

		if (current_date.compareTo(selecteddate)<0)
		{
			Log.e("date","selected date is Greater than currentdate");
			flag=true;
		}
else {
			Log.e("date","selected date is smaller than current date");
		flag=false;
		}


	}
	catch (Exception e)
	{
flag=false;
	}
	return flag;
}
public static boolean checkAppointmentsCacheddata(FragmentActivity mActivity)
{
	SYLAppointmentdetailsdatamanager datamanager = new SYLAppointmentdetailsdatamanager(
			mActivity);
	ArrayList<SYLFetchAppointmentsDetails> requestReceivedarraylistcached = datamanager
			.getAllAppointmentDetails("REQUEST RECEIVED");
	ArrayList<SYLFetchAppointmentsDetails> confirmedAppointmentsarraylistcached = datamanager
			.getAllAppointmentDetails("OTHER CONFIRMED");
	ArrayList<SYLFetchAppointmentsDetails> requestSendarraylistcached = datamanager
			.getAllAppointmentDetails("REQUEST SENT");
	ArrayList<SYLFetchAppointmentsDetails> cancelledRequestarraylistcached = datamanager
			.getAllAppointmentDetails("CANCELLED");
	ArrayList<SYLFetchAppointmentsDetails> finishedappointmentsarraylistcached = datamanager
			.getAllAppointmentDetails("FINISHED");
	ArrayList<SYLFetchAppointmentsDetails> todaysappointmentsarraylistcached = datamanager
			.getAllAppointmentDetails("TODAYS CONFIRMED");

	if( (todaysappointmentsarraylistcached.size()<1)
			&&
			(requestReceivedarraylistcached.size()<1)
			&&
			(confirmedAppointmentsarraylistcached.size()<1)
			&&
			(requestSendarraylistcached.size()<1)
			&&
			(cancelledRequestarraylistcached.size()<1)
			&&
			(finishedappointmentsarraylistcached.size()<1)
			)
	{

	return false;
	}
	else {
		return true;
	}

}

	public static boolean checkHistoryAppointmentsCacheddata(Activity mActivity)
	{
		SYLHistoryAppointmentsdatamanager datamanager = new SYLHistoryAppointmentsdatamanager(
				mActivity);
		ArrayList<SYLFetchAppointmentsDetails> requestReceivedarraylistcached = datamanager
				.getHistoryAppointmentDetails("REQUEST RECEIVED");
		ArrayList<SYLFetchAppointmentsDetails> confirmedAppointmentsarraylistcached = datamanager
				.getHistoryAppointmentDetails("OTHER CONFIRMED");
		ArrayList<SYLFetchAppointmentsDetails> requestSendarraylistcached = datamanager
				.getHistoryAppointmentDetails("REQUEST SENT");
		ArrayList<SYLFetchAppointmentsDetails> cancelledRequestarraylistcached = datamanager
				.getHistoryAppointmentDetails("CANCELLED");
		ArrayList<SYLFetchAppointmentsDetails> finishedappointmentsarraylistcached = datamanager
				.getHistoryAppointmentDetails("FINISHED");
		ArrayList<SYLFetchAppointmentsDetails> todaysappointmentsarraylistcached = datamanager
				.getHistoryAppointmentDetails("TODAYS CONFIRMED");

		if( (todaysappointmentsarraylistcached.size()<1)
				&&
				(requestReceivedarraylistcached.size()<1)
				&&
				(confirmedAppointmentsarraylistcached.size()<1)
				&&
				(requestSendarraylistcached.size()<1)
				&&
				(cancelledRequestarraylistcached.size()<1)
				&&
				(finishedappointmentsarraylistcached.size()<1)
				)
		{

			return false;
		}
		else {
			return true;
		}

	}






}
