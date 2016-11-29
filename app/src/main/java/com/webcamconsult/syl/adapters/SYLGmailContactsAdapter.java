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
import com.webcamconsult.syl.model.GmailContacts;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;

public class SYLGmailContactsAdapter extends BaseAdapter {
	public Activity activity;

	/** The array list tip. */
	ArrayList<GmailContacts> mgmailContactPersonsDetailsArrayList;

	/** The inflater. */
	public LayoutInflater inflater = null;

	/** The image loader. */
	public ImageLoader mImageLoader;

	public SYLGmailContactsAdapter(Activity listingActivity, int listrowTip,
			ArrayList<GmailContacts> arrayListTip, Context con) {
		activity = listingActivity;
		this.mgmailContactPersonsDetailsArrayList = arrayListTip;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageLoader = new ImageLoader(activity.getApplicationContext());
	}

GmailContacts mContactpersonsdetails;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mgmailContactPersonsDetailsArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mgmailContactPersonsDetailsArrayList.get(position);
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

		if (convertView == null) {
			listContentView = inflater.inflate(R.layout.sylcontacts_eachrow,
					parent,false);

		}

		mContactpersonsdetails = mgmailContactPersonsDetailsArrayList
				.get(position);
		if (mContactpersonsdetails != null) {

		
		

			TextView txtview_firstname = (TextView) listContentView
					.findViewById(R.id.txt_firstname);
			TextView txtview_userid = (TextView) listContentView
					.findViewById(R.id.txt_userid);
			TextView txtview_email = (TextView) listContentView
					.findViewById(R.id.txt_email);
			ImageView mtickmarkimageview=(ImageView)listContentView.findViewById(R.id.img_marker);
	// mImageview=(ImageView)listContentView.findViewById(R.id.img_test);

			// LinearLayout
			// newsheadlayout=(LinearLayout)listContentView.findViewById(R.id.liout);
			// listContentView.setBackgroundColor(Color.parseColor(points.getColorCode()
			// newsheadlayout.setBackgroundColor(Color.parseColor(points.getColorCode()));
			String email=mContactpersonsdetails.getEmail().toString().trim();
			if(email.length()>16)
			{
				email= email.substring(0, Math.min(email.length(), 12))+"...";
			}



			txtview_firstname.setText(email);
			txtview_email.setText(mContactpersonsdetails.getEmail());
			txtview_userid.setText(Integer.toString(mContactpersonsdetails.getSerialNo()));
String positionmarker=mContactpersonsdetails.getPositionmark();
if(positionmarker==null || positionmarker.equals("false"))
{
	mtickmarkimageview.setVisibility(View.INVISIBLE);
	}
else {
	mtickmarkimageview.setVisibility(View.VISIBLE);
}
		


		}
		return listContentView;
	}
}
