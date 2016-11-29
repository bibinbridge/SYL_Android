package com.webcamconsult.syl.fragments;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import com.webcamconsult.syl.interfaces.SYLTimeListener;

public class SYLTimePickerFragment extends DialogFragment implements
TimePickerDialog.OnTimeSetListener {
	public SYLTimeListener mTimeListener;
	String timeoption_type;
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current time as the default values for the picker
Bundle timebundle=getArguments();
	timeoption_type = getTag();
	final Calendar c = Calendar.getInstance();
	if(timebundle!=null) {
		String enteredtime = timebundle.getString("enteredtime");


		if (timeoption_type.equals("timeoption1") || timeoption_type.equals("timeoption2")) {
			String time_hour_min[] = enteredtime.split(":");
			int hour = Integer.parseInt(time_hour_min[0].toString());
			int min = Integer.parseInt(time_hour_min[1].toString());
			c.set(Calendar.HOUR_OF_DAY, hour);
			c.set(Calendar.MINUTE, min);

		}
	}
int hour = c.get(Calendar.HOUR_OF_DAY);
int minute = c.get(Calendar.MINUTE);


// Create a new instance of TimePickerDialog and return it
return new TimePickerDialog(getActivity(), this, hour, minute,
		true);

}

public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

mTimeListener.ondidgetTime(getTwentyfourhourTime(hourOfDay, minute),timeoption_type);

}
private String getExactTime(int hourofday, int minute) {
	String minuteString = "";
	String format = "";
	String selectedtime = null;
	if (minute < 10) {
		minuteString = "0" + Integer.toString(minute);
	} else {
		minuteString = Integer.toString(minute);

	}

	if (hourofday == 0) {
		hourofday += 12;
		format = "AM";
	}

	else if (hourofday > 12) {
		format = "PM";
		hourofday -= 12;
	} else if (hourofday == 12) {
		format = "PM";
	} else {
		format = "AM";
	}
	selectedtime = Integer.toString(hourofday) + ":" + minuteString
			+ " " + format;
	return selectedtime;
}
private String getTwentyfourhourTime(int hour,int minute)
{
	String minutevalue;
	if(minute<10)
	{
		minutevalue="0"+Integer.toString(minute);
	}
	else {
		minutevalue=Integer.toString(minute);
	}
	return Integer.toString(hour)+":"+minutevalue;
	}
@Override
public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
	super.onAttach(activity);
	mTimeListener = (SYLTimeListener) activity;
}

@Override
public void onDetach() {
	super.onDetach();
	mTimeListener = null;
}






}
