package com.webcamconsult.syl.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webcamconsult.syl.R;



public class SYLCountrycodeadapter extends ArrayAdapter<String> {
	Context context;
	 String[] countryobjects;
	 Activity mActivity;
	
	public SYLCountrycodeadapter(Context ctx, int txtViewResourceId, String[] countryobjects) {
		super(ctx, txtViewResourceId, countryobjects);
		this.mActivity=(Activity)ctx;
		this.countryobjects=countryobjects;
	}

	@Override
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
		return getCustomView(position, cnvtView, prnt);
	}
	@Override
	public View getView(int pos, View cnvtView, ViewGroup prnt) {
		return getCustomView(pos, cnvtView, prnt);
	}
	public View getCustomView(int position, View convertView,
			ViewGroup parent) {
		LayoutInflater inflater =mActivity.getLayoutInflater();
		View mySpinner = inflater.inflate(R.layout.syl_spinnereachitem, parent,
				false);
		TextView main_text = (TextView) mySpinner
				.findViewById(R.id.txt_countrycode);
	main_text.setText(countryobjects[position]);

		return mySpinner;
	}
}
