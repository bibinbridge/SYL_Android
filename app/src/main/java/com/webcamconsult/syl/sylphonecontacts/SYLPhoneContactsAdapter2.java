package com.webcamconsult.syl.sylphonecontacts;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.model.SYLPhoneContacts;

public class SYLPhoneContactsAdapter2 extends BaseAdapter {
	public Activity activity;

	/** The array list tip. */
	ArrayList<SYLPhoneContacts2> mSYLPhoneContactsArraylist;

	/** The inflater. */
	public LayoutInflater inflater = null;

	/** The image loader. */
	public ImageLoader mImageLoader;

	public SYLPhoneContactsAdapter2(Activity listingActivity, int listrowTip,
			ArrayList<SYLPhoneContacts2> arrayListTip, Context con) {
		activity = listingActivity;
		this.mSYLPhoneContactsArraylist = arrayListTip;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageLoader = new ImageLoader(activity.getApplicationContext());
	}

	SYLPhoneContacts2 mSYLPhoneContacts;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSYLPhoneContactsArraylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mSYLPhoneContactsArraylist.get(position);
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
			listContentView = inflater.inflate(R.layout.sylphonecontacts_eachrow,
					null);
		}

		mSYLPhoneContacts = mSYLPhoneContactsArraylist
				.get(position);
		if (mSYLPhoneContacts != null) {
		
			ImageView userImageView = null;
		
			RelativeLayout rel=(RelativeLayout)listContentView.findViewById(R.id.rel_parent);
			TextView txtview_firstname = (TextView) listContentView
					.findViewById(R.id.txt_firstname);
			TextView txtview_userid = (TextView) listContentView
					.findViewById(R.id.txt_userid);
			TextView txtview_mobilenumber = (TextView) listContentView
					.findViewById(R.id.txt_mobilenumber);
			TextView txtview_lastname = (TextView) listContentView
					.findViewById(R.id.txt_lastname);
			TextView txtview_email = (TextView) listContentView
					.findViewById(R.id.txt_email);
			userImageView = (ImageView) listContentView
					.findViewById(R.id.row_icon);
			ImageView mtickmarkimageview=(ImageView)listContentView.findViewById(R.id.img_marker);
	// mImageview=(ImageView)listContentView.findViewById(R.id.img_test);

			// LinearLayout
			// newsheadlayout=(LinearLayout)listContentView.findViewById(R.id.liout);
			// listContentView.setBackgroundColor(Color.parseColor(points.getColorCode()));
			// newsheadlayout.setBackgroundColor(Color.parseColor(points.getColorCode()));
			txtview_firstname.setText(mSYLPhoneContacts.getName().trim());
			txtview_userid.setText(mSYLPhoneContacts.getContactid());


			String positionmarker=mSYLPhoneContacts.getPositiommark();
			if(positionmarker==null || positionmarker.equals("false"))
			{
				mtickmarkimageview.setVisibility(View.INVISIBLE);
				}
			else {
				mtickmarkimageview.setVisibility(View.VISIBLE);
			}
			try {
				String photouri = mSYLPhoneContacts.getProfileimageurl();

				Uri uri = Uri.parse(photouri);
				Bitmap bitmap = MediaStore.Images.Media
						.getBitmap(activity.getContentResolver(),
								uri);
				if (bitmap != null) {
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150,true);
					userImageView.setImageBitmap(scaledBitmap);
				} else {
					userImageView.setImageResource(R.drawable.syl_nouserimage);
				}
			}
			catch (Exception e){
				userImageView.setImageResource(R.drawable.syl_nouserimage);
			}
//	bitmap=null;
//	profileimageuri=null;
	
	
	
	
	

//		InputStream inputStream = ContactsContract.Contacts
//				.openContactPhotoInputStream(
//						activity.getContentResolver(),uri);
//	
//		if (inputStream != null) {
//	Bitmap		photo = BitmapFactory.decodeStream(inputStream);
//	userImageView.setImageBitmap(photo);
//	photo=null;
//		}
//
//		if (inputStream != null)
//			inputStream.close();



	}
		return listContentView;
	}
}
