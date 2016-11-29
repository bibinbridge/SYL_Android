package com.webcamconsult.syl.fragments;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.interfaces.SYLDateListener;

public class SYLDatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	public SYLDateListener mDateListener;
	SYLRequestnewappointmentActivity m;
	String date;
	String date_type;
	Bundle bundle;

	public SYLDatePickerFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker


Bundle dateBundle=getArguments();

		date_type = getTag();
		final Calendar c = Calendar.getInstance();
		if(dateBundle!=null) {
			String entereddate = dateBundle.getString("entereddate");


			if (date_type.equals("option1date") || date_type.equals("option2date")) {
				String year_month_date[] = entereddate.split("/");
				int year = Integer.parseInt(year_month_date[2].toString());
				int month = Integer.parseInt(year_month_date[1].toString());
				int date = Integer.parseInt(year_month_date[0].toString());
				c.set(year, month - 1, date);
			}
		}

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
DatePickerDialog datepickerdialog= new DatePickerDialog(getActivity(), this, year, month, day);
		datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
		// Create a new instance of DatePickerDialog and return it
		//return new DatePickerDialog(getActivity(), this, year, month, day);
		return datepickerdialog;
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user

		date = Integer.toString(day) + "/" + Integer.toString(month + 1) + "/"
				+ Integer.toString(year);

		mDateListener.getDate(date, this.date_type);
		// mDateListener.getDate(date,"preferred");

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mDateListener = (SYLDateListener) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mDateListener = null;
	}

}
