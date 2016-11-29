package com.webcamconsult.syl.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webcamconsult.syl.R;

public class SYLPrioritySpinnerAdapter extends ArrayAdapter<String> {
	Activity mActivity;
	ArrayList<String> medium1ArrayList=new ArrayList<String>();
	int total_images[] = { R.drawable.skype_largeicon, R.drawable.mobile_largeicon,
			R.drawable.mobile_largeicon,R.drawable.skype_largeicon };
	String[] objects = { "Skype", "opentok", "Mobile" };
ArrayList<Integer> mImagesArrayList;
	public SYLPrioritySpinnerAdapter(Context ctx, int txtViewResourceId,
			int[] total_images,ArrayList<String> mArraylist,ArrayList<Integer>imagearrarylist) {
		super(ctx, txtViewResourceId, mArraylist);
		this.mActivity = (Activity) ctx;

this.total_images=total_images;
this.medium1ArrayList=mArraylist;
this.mImagesArrayList=imagearrarylist;
	}

	@Override
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
		return getCustomView(position, cnvtView, prnt);
	}

	@Override
	public View getView(int pos, View cnvtView, ViewGroup prnt) {
		return getCustomView(pos, cnvtView, prnt);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = mActivity.getLayoutInflater();
		View mySpinner = inflater.inflate(R.layout.spinner_priorityrow, parent,
				false);
		TextView main_text = (TextView) mySpinner
				.findViewById(R.id.txt_priorityname);
	//	main_text.setText(objects[position]);
		String g=medium1ArrayList.get(position);

		main_text.setText(medium1ArrayList.get(position));
		ImageView left_icon = (ImageView) mySpinner
				.findViewById(R.id.img_priorityicon);

	//	left_icon.setImageResource(total_images[position]);
		left_icon.setImageResource(mImagesArrayList.get(position));

		return mySpinner;
	}
}
