package com.webcamconsult.syl.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;

public class SYLContactsAdapter extends BaseAdapter {
	public Activity activity;

	/** The array list tip. */
	ArrayList<SYLContactPersonsDetails> mSYLContactPersonsDetailsArrayList;

	/** The inflater. */
	public LayoutInflater inflater = null;

	/** The image loader. */
	public ImageLoader mImageLoader;

	public SYLContactsAdapter(Activity listingActivity, int listrowTip,
			ArrayList<SYLContactPersonsDetails> arrayListTip, Context con) {
		activity = listingActivity;
		this.mSYLContactPersonsDetailsArrayList = arrayListTip;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageLoader = new ImageLoader(activity.getApplicationContext());
	}

	SYLContactPersonsDetails mContactpersonsdetails;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSYLContactPersonsDetailsArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mSYLContactPersonsDetailsArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View listContentView = convertView;
	final	ImageView mImageview;
		if (convertView == null) {
			listContentView = inflater.inflate(R.layout.sylcontacts_eachrow,
					null);
		}

		mContactpersonsdetails = mSYLContactPersonsDetailsArrayList
				.get(position);
		if (mContactpersonsdetails != null) {

			ImageView userImageView = null;
		
			RelativeLayout rel=(RelativeLayout)listContentView.findViewById(R.id.rel_parent);
			TextView txtview_firstname = (TextView) listContentView
					.findViewById(R.id.txt_firstname);
			TextView txtview_userid = (TextView) listContentView
					.findViewById(R.id.txt_userid);
			ImageView mtickmarkimageview=(ImageView)listContentView.findViewById(R.id.img_marker);
	// mImageview=(ImageView)listContentView.findViewById(R.id.img_test);

			// LinearLayout
			// newsheadlayout=(LinearLayout)listContentView.findViewById(R.id.liout);
			// listContentView.setBackgroundColor(Color.parseColor(points.getColorCode()
			// newsheadlayout.setBackgroundColor(Color.parseColor(points.getColorCode()));
			txtview_firstname.setText(mContactpersonsdetails.getName());
			txtview_userid.setText(Integer.toString(mContactpersonsdetails.getUserId()));
String positionmarker=mContactpersonsdetails.getPositionmark();
if(positionmarker==null || positionmarker.equals("false"))
{
	mtickmarkimageview.setVisibility(View.INVISIBLE);
	}
else {
	mtickmarkimageview.setVisibility(View.VISIBLE);
}
			userImageView = (ImageView) listContentView
					.findViewById(R.id.row_icon);
			String profileimageurl=mContactpersonsdetails.getProfileImage();
			if(!profileimageurl.equals("")){
			mImageLoader
					.DisplayImage(mContactpersonsdetails.getProfileImage(),
							userImageView);
			}
			else {
				userImageView.setImageResource(R.drawable.syl_nouserimage);
			}
		}
		return listContentView;
	}
}
