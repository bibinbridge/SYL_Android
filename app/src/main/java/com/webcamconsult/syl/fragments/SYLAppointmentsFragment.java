package com.webcamconsult.syl.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLCaneledRequestDetailedActivity;
import com.webcamconsult.syl.activities.SYLConfirmedRequestAppointmentActivity;
import com.webcamconsult.syl.activities.SYLFinishedAppointmentDetailActivity;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.activities.SYLHistoryAppointmentsActivity;
import com.webcamconsult.syl.activities.SYLRequestReceivedAppointmentActivity;
import com.webcamconsult.syl.activities.SYLRequestSendActivity;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.activities.SYLTodaysAppointmentDetailActivity;
import com.webcamconsult.syl.adapters.SYLAppointmentsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLHistoryAppointmentsdatamanager;
import com.webcamconsult.syl.interfaces.SYLAppointmentDetailsListner;
import com.webcamconsult.syl.interfaces.SYLAppointmentRemoveListener;
import com.webcamconsult.syl.interfaces.SYLFetchAppointmentDetailsListener;
import com.webcamconsult.syl.interfaces.SYLFinishedAppointmentListener;
import com.webcamconsult.syl.model.Response;
import com.webcamconsult.syl.model.SYLAppointmentDetails;
import com.webcamconsult.syl.model.SYLAppointmentDetailsAll;
import com.webcamconsult.syl.model.SYLAppointmentRemoveResponseModel;
import com.webcamconsult.syl.model.SYLAppointmentsDescription;
import com.webcamconsult.syl.model.SYLFetchAppointmentsDetails;
import com.webcamconsult.syl.model.SYLFetchAppointmentsResponseModel;
import com.webcamconsult.syl.model.SYLFinishAppointmentResponseModel;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.swiperefresh.SwipeDismissListViewTouchListener;
import com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLAppointmentRemoveViewManager;
import com.webcamconsult.viewmanager.SYLAppointmentsDetailsManager;
import com.webcamconsult.viewmanager.SYLAppointmentsManager;
import com.webcamconsult.viewmanager.SYLFinishAppointmentViewManager;

public class SYLAppointmentsFragment extends ListFragment implements
 SYLAppointmentDetailsListner,
		SYLFetchAppointmentDetailsListener,com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout.OnRefreshListener,SYLAppointmentRemoveListener,SYLFinishedAppointmentListener {

	ArrayList<SYLAppointmentsDescription> sYLAppointmentsDetails;
	/** declaring the object of list Adapter class */
	SYLAppointmentsAdapter sYLAppointmentsAdapter;
	String method = "get";
	Context mContext;
	ImageView mbtnNew;
	String temp;
	ProgressDialog msylProgressDialog;
	ArrayList<SYLAppointmentDetails> todaysappointmentarraylist;
	ArrayList<SYLAppointmentDetails> requestReceivedarraylist;
	ArrayList<SYLAppointmentDetails> confirmedAppointmentsarraylist;
	ArrayList<SYLAppointmentDetails> requestSendarraylist;
	ArrayList<SYLAppointmentDetails> cancelledRequestarraylist;
	ArrayList<SYLAppointmentDetailsAll> allappointmentsArraylist;

	ArrayList<SYLFetchAppointmentsDetails> todaysAppointmentsdetailarraylist;
	ArrayList<SYLFetchAppointmentsDetails> requestReceiveddetailarraylist;
	ArrayList<SYLFetchAppointmentsDetails> confirmedAppointmentsdetailarraylist;
	ArrayList<SYLFetchAppointmentsDetails> requestSenddetailarraylist;
	ArrayList<SYLFetchAppointmentsDetails> cancelledRequestdetailarraylist;
	ArrayList<SYLFetchAppointmentsDetails> finishedAppointmentarraylist;
	ArrayList<Response> sYLResponse;
	View headerView;
	ListView listView;
	LinearLayout mAppointmentHistoryLayout;

	Fragment mSYLRequestReceivedDetailedFragment;
	Fragment mSYLConfirmedAppointmentDetailedFragment;
String userid,accesstoken,GMTtimezone;
String delete_appointmentid,finishAppointmentid;
boolean refreshToggle = true;
//private boolean progressdialogflag=true;
	SYLFragmentChangeActivity mFragmentChangeActivity;
private com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout swipeView;
	public SYLAppointmentsFragment() {
	}

	@SuppressWarnings("unused")
	private Fragment mContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_appointments, null);
		   swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_view);
		headerView = getActivity().getLayoutInflater().inflate(
				R.layout.empty_search_result_cell, null);
		listView = (ListView) view.findViewById(android.R.id.list);
		mAppointmentHistoryLayout=(LinearLayout)view.findViewById(R.id.liout_history);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setswipeviewProperties();
		FragmentActivity i = getActivity();
		SlidingFragmentActivity k = (SlidingFragmentActivity) i;
		k.getSupportActionBar().setCustomView(R.layout.action_custom1);
		View v = k.getSupportActionBar().getCustomView();
		mbtnNew = (ImageView) v.findViewById(R.id.btnnew);
		// View v=getS
		TextView mhead = (TextView) v.findViewById(R.id.mytext);

		// listView= (ListView)v.findViewById(R.id.list);
		// mtxt_nearby=(TextView)v.findViewById(R.id.txt_nearby);
		Typeface signupfont = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/MuseoSans-300.otf");
		mhead.setTypeface(signupfont);

		mhead.setText("Appointments");
		accesstoken = SYLSaveValues.getSYLaccessToken(mFragmentChangeActivity);
		msylProgressDialog = new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");
		msylProgressDialog.setCancelable(false);
		msylProgressDialog.setCanceledOnTouchOutside(false);

		mbtnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						SYLRequestnewappointmentActivity.class);
				intent.putExtra("intentvalue", "appointments");
				startActivity(intent);
				getActivity().finish();
			}
		});
		getCachedData();
		if (SYLUtil.isNetworkAvailable(getActivity())) {

			//loadAppointmentList();
		} else {
			listView.addHeaderView(headerView);
			//getCachedData();
		}
		mAppointmentHistoryLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				navigateToAppointmentHistoryScreen();
			}
		});
	}

	private void loadAppointmentList() {
		// sYLAppointmentsAdapter = new SYLAppointmentsAdapter(getActivity());
		// for (int p = 1; p < 50; p++)
		// {
		// sYLAppointmentsAdapter.addItem("Sameer Blog " + p);
		// if (p % 4 == 0) {
		// sYLAppointmentsAdapter.addSeparatorItem("Ahmad " + p);
		// }
		// sYLAppointmentsAdapter.
	//	if(progressdialogflag){
			msylProgressDialog.show();
		//progressdialogflag=false;
		//}
		GMTtimezone=SYLUtil.getTimeGMTZone(getActivity());
	
		accesstoken=SYLSaveValues.getSYLaccessToken(getActivity());
		userid=SYLSaveValues.getSYLUserID(getActivity());
		SYLAppointmentsManager sYLAppointmentsManager = new SYLAppointmentsManager();
		// sYLAppointmentsManager.sYLAppointmentListner =
		// SYLAppointmentsiniFragment.this;
		sYLAppointmentsManager.mFetchAppointmentsListener = SYLAppointmentsFragment.this;
		sYLAppointmentsManager.getAppointments(method, getActivity(),accesstoken,userid,GMTtimezone);

		// requestReceivedarraylist=new ArrayList<SYLAppointmentDetails>();
		// confirmedAppointmentsarraylist=new
		// ArrayList<SYLAppointmentDetails>();
		// requestSendarraylist=new ArrayList<SYLAppointmentDetails>();
		// cancelledRequestarraylist=new ArrayList<SYLAppointmentDetails>();
		// allappointmentsArraylist=new ArrayList<SYLAppointmentDetailsAll>();
		//
		// sYLAppointmentsAdapter = new SYLAppointmentsAdapter(getActivity());
		// for(int i=0;i<1;i++){
		// SYLAppointmentDetails requestReceived=new SYLAppointmentDetails();
		// requestReceived.setAppointmentid(1);
		// requestReceived.setUserid(1);
		// requestReceived.setFirstname("Camela");
		// requestReceived.setLastname("test");
		// requestReceived.setDate("17-10-2014");
		// requestReceived.setTime("10:00AM");
		// // Bitmap roundedbitmapimage =
		// circle_image("http://lh4.ggpht.com/_XjNwVI0kmW8/TCOwNtzGheI/AAAAAAAAC84/SxFJhG7Scgo/s144-c/0014.jpg");
		// // BitMapToString(roundedbitmapimage);
		// requestReceived.setIcon("http://lh4.ggpht.com/_XjNwVI0kmW8/TCOwNtzGheI/AAAAAAAAC84/SxFJhG7Scgo/s144-c/0014.jpg");
		// requestReceivedarraylist.add(requestReceived);
		// }
		// for(int i=0;i<2;i++)
		// {
		// SYLAppointmentDetails confirmedAppointment=new
		// SYLAppointmentDetails();
		//
		//
		//
		// confirmedAppointment.setAppointmentid(1);
		// confirmedAppointment.setUserid(1);
		// confirmedAppointment.setFirstname("John");
		// confirmedAppointment.setLastname("tesjk");
		// confirmedAppointment.setDate("18-10-2014");
		// confirmedAppointment.setTime("11:00AM");
		// confirmedAppointment.setIcon("http://lh5.ggpht.com/_hepKlJWopDg/TB-_WXikaYI/AAAAAAAAElI/715k4NvBM4w/s144-c/IMG_0075.JPG");
		// confirmedAppointmentsarraylist.add(confirmedAppointment);
		// }
		//
		// for(int i=0;i<3;i++)
		// {
		// SYLAppointmentDetails requestSend=new SYLAppointmentDetails();
		//
		//
		//
		// requestSend.setAppointmentid(1);
		// requestSend.setUserid(1);
		// requestSend.setFirstname("Mathias");
		// requestSend.setLastname("tesjk");
		// requestSend.setDate("19-10-2014");
		// requestSend.setTime("1:00PM");
		// requestSend.setIcon("http://lh5.ggpht.com/_loGyjar4MMI/S-InWuHkR9I/AAAAAAAADJE/wD-XdmF7yUQ/s144-c/Colorado%20River%20Sunset.jpg");
		// requestSendarraylist.add(requestSend);
		// }
		// for(int i=0;i<4;i++)
		// {
		// SYLAppointmentDetails cancelledRequest=new SYLAppointmentDetails();
		//
		//
		//
		// cancelledRequest.setAppointmentid(1);
		// cancelledRequest.setUserid(1);
		// cancelledRequest.setFirstname("Don");
		// cancelledRequest.setLastname("tesjk");
		// cancelledRequest.setDate("20-10-2014");
		// cancelledRequest.setTime("2:00pm");
		// cancelledRequest.setIcon("http://lh3.ggpht.com/_syQa1hJRWGY/TBwkCHcq6aI/AAAAAAABBEg/R5KU1WWq59E/s144-c/Antelope.JPG");
		// cancelledRequestarraylist.add(cancelledRequest);
		// }
		//
		//
		// sYLAppointmentsAdapter.addSectionHeaderItem("Request Received");
		//
		// for(SYLAppointmentDetails reqReceived:requestReceivedarraylist)
		// {
		//
		//
		// sYLAppointmentsAdapter.addItem(reqReceived.getFirstname()+","+reqReceived.getDate()+","+reqReceived.getTime()+","+reqReceived.getIcon());
		//
		// }
		// sYLAppointmentsAdapter.addSectionHeaderItem("Confirmed Appointments");
		//
		// for(SYLAppointmentDetails
		// confAppointment:confirmedAppointmentsarraylist)
		// {
		//
		//
		// sYLAppointmentsAdapter.addItem(confAppointment.getFirstname()+","+confAppointment.getDate()+","+confAppointment.getTime()+","+confAppointment.getIcon());
		//
		//
		// }
		//
		//
		// sYLAppointmentsAdapter.addSectionHeaderItem("Request Send");
		// for(SYLAppointmentDetails reqSend:requestSendarraylist)
		// {
		//
		//
		// sYLAppointmentsAdapter.addItem(reqSend.getFirstname()+","+reqSend.getDate()+","+reqSend.getTime()+","+reqSend.getIcon());
		//
		//
		// }
		//
		// sYLAppointmentsAdapter.addSectionHeaderItem("Cancelled Request");
		// for(SYLAppointmentDetails cancelReq:cancelledRequestarraylist)
		// {
		//
		//
		// sYLAppointmentsAdapter.addItem(cancelReq.getFirstname()+","+cancelReq.getDate()+","+cancelReq.getTime()+","+cancelReq.getIcon());
		//
		//
		// }
		// setListAdapter(sYLAppointmentsAdapter);
		//

	}

	public String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	private Bitmap circle_image(String file_path) {

		Bitmap mIcon11 = null;
		InputStream in = null;
		File f = new File(file_path);
		try {

			final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
			in = new FileInputStream(f);
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			Log.d("TAG", "scale = " + scale + ", orig-width: " + o.outWidth
					+ ", orig-height: " + o.outHeight);

			Bitmap b = null;
			in = new FileInputStream(f);

			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int width = b.getHeight();
				int height = b.getWidth();
				Log.d("TAG", "1th scale operation dimenions - width: " + width
						+ ",height: " + height);

				double y = Math.sqrt(IMAGE_MAX_SIZE
						/ (((double) width) / height));
				double x = (y / height) * width;

				// Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
				// (int) y, true);
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, 150, 150,
						true);
				b.recycle();
				b = scaledBitmap;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			// Log.d("TAG", "bitmap size - width: " +b.getWidth() + ", height: "
			// +
			// b.getHeight());

			/*
			 * 
			 * try { String path =
			 * Environment.getExternalStorageDirectory().toString
			 * ()+"/LazyList/a.png"; FileOutputStream out = new
			 * FileOutputStream(path); b.compress(Bitmap.CompressFormat.PNG, 90,
			 * out); out.close(); } catch (Exception e) { e.printStackTrace(); }
			 */

			return b;
		} catch (IOException e) {
			Log.e("TAG", e.getMessage(), e);
			return null;
		}

	}



	private void navigatetoTodaysAppointmentDetail(String appointmentid) {

		SYLFetchAppointmentsDetails mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);
		if(mfetchappointmentdetails!=null) {

			Intent intent = new Intent(SYLAppointmentsFragment.this
					.getActivity(),
					SYLTodaysAppointmentDetailActivity.class);
			String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
			String organizer_id = Integer.toString(mfetchappointmentdetails.getOrganizer().getUserId());
			String participnat_id = Integer.toString(mfetchappointmentdetails.getParticipant().getUserId());
			if (curr_userid.equals(organizer_id)) {
				intent.putExtra("name", mfetchappointmentdetails
						.getParticipant().getName());
				int participantid = mfetchappointmentdetails
						.getParticipant().getUserId();
				intent.putExtra("participantid",
						Integer.toString(participantid));
				intent.putExtra("profile_image",
						mfetchappointmentdetails.getParticipant()
								.getProfileImage());


				if (mfetchappointmentdetails.getParticipant()
						.getSkypeId().equals("") ||
						mfetchappointmentdetails.getParticipant()
								.getSkypeId().equals("Not Available")
						) {
					intent.putExtra("skypeid",
							"not available");
				} else {
					intent.putExtra("skypeid",
							mfetchappointmentdetails.getParticipant()
									.getSkypeId());
				}

				if(mfetchappointmentdetails.getParticipant().getMobile().equals("")||
						mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available")
						)
				{
					intent.putExtra("mobile",
							"not available");
				}
				else {
					intent.putExtra("mobile",
							mfetchappointmentdetails.getParticipant()
									.getMobile());
				}
				if (mfetchappointmentdetails.getParticipant()
						.getHangoutId().equals("")
						|| mfetchappointmentdetails.getParticipant()
						.getHangoutId().equals("Not Available")            ) {
					intent.putExtra("hangoutid",
							"not available");
				} else {
					intent.putExtra("hangoutid",
							mfetchappointmentdetails.getParticipant()
									.getHangoutId());
				}

				if (mfetchappointmentdetails.getParticipant()
						.getFacetimeId().equals("")
						|| mfetchappointmentdetails.getParticipant()
						.getFacetimeId().equals("Not Available")        ) {
					intent.putExtra("facetimeid",
							"not available");
				} else {
					intent.putExtra("facetimeid",
							mfetchappointmentdetails.getParticipant()
									.getFacetimeId());
				}
			} else {
				intent.putExtra("name", mfetchappointmentdetails
						.getOrganizer().getName());
				int participantid = mfetchappointmentdetails
						.getOrganizer().getUserId();
				intent.putExtra("participantid",
						Integer.toString(participantid));
				intent.putExtra("profile_image",
						mfetchappointmentdetails.getOrganizer().getProfileImage());
				intent.putExtra("facetimeid",
						mfetchappointmentdetails.getOrganizer()
								.getFacetimeId());


				if (mfetchappointmentdetails.getOrganizer()
						.getSkypeId().equals("") ||
						mfetchappointmentdetails.getOrganizer()
								.getSkypeId().equals("Not Available")
						) {
					intent.putExtra("skypeid",
							"not available");
				} else {
					intent.putExtra("skypeid",
							mfetchappointmentdetails.getOrganizer()
									.getSkypeId());
				}




				if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")||
						mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available")
						)
				{
					intent.putExtra("mobile",
							"not available");
				}
				else {
					intent.putExtra("mobile",
							mfetchappointmentdetails.getOrganizer()
									.getMobile());
				}

				if (mfetchappointmentdetails.getOrganizer()
						.getHangoutId().equals("")
						|| mfetchappointmentdetails.getOrganizer()
						.getHangoutId().equals("Not Available")            ) {
					intent.putExtra("hangoutid",
							"not available");
				} else {
					intent.putExtra("hangoutid",
							mfetchappointmentdetails.getOrganizer()
									.getHangoutId());
				}


			}

			intent.putExtra("topic",
					mfetchappointmentdetails.getTopic());
			intent.putExtra("description",
					mfetchappointmentdetails.getDescription());


			intent.putExtra("priority1",
					mfetchappointmentdetails.getAppointmentPriority1());

			if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
				intent.putExtra("priority2",
						"not available");
			} else {
				intent.putExtra("priority2",
						mfetchappointmentdetails.getAppointmentPriority2());
			}
			if (mfetchappointmentdetails.getAppointmentPriority3().equals("")) {
				intent.putExtra("priority3",
						"not available");
			} else {
				intent.putExtra("priority3",
						mfetchappointmentdetails.getAppointmentPriority3());
			}

			intent.putExtra("appointmentid",
					appointmentid);

			String date1 = mfetchappointmentdetails
					.getAppointmentDate1();
			String time1 = mfetchappointmentdetails
					.getAppointmentTime1();
			String date2 = mfetchappointmentdetails
					.getAppointmentDate2();
			String time2 = mfetchappointmentdetails
					.getAppointmentTime2();
			String date3 = mfetchappointmentdetails
					.getAppointmentDate3();
			String time3 = mfetchappointmentdetails
					.getAppointmentTime3();
			intent.putExtra("date1", date1 + " " + time1);
			intent.putExtra("date2", date2 + " " + time2);
			intent.putExtra("date3", date3 + " " + time3);
			intent.putExtra("intentfrom","futureappointment");
			startActivity(intent);
			getActivity().finish();
		}
	}

	private void navigatetoCanelrequestDetailscreen(String appointmentid) {
		Intent canceledrequestintent = new Intent(getActivity(),
				SYLCaneledRequestDetailedActivity.class);
		SYLFetchAppointmentsDetails mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);

		
		
		
		
		
		String curr_userid=SYLSaveValues.getSYLUserID(getActivity());
		String organizer_id= Integer.toString(mfetchappointmentdetails.getOrganizer().getUserId());
		String participnat_id=Integer.toString(mfetchappointmentdetails.getParticipant().getUserId());
		if(curr_userid.equals(organizer_id))
		{
			canceledrequestintent .putExtra("name", mfetchappointmentdetails
					.getParticipant().getName());
			int participantid = mfetchappointmentdetails
					.getParticipant().getUserId();
			canceledrequestintent .putExtra("participantid",
					Integer.toString(participantid));
			canceledrequestintent.putExtra("profile_image",
					mfetchappointmentdetails.getParticipant()
							.getProfileImage());

if(mfetchappointmentdetails.getParticipant().getMobile().equals("")||
		mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available"))
{
	canceledrequestintent.putExtra("mobile",
			"not available");
}
			else {
	canceledrequestintent.putExtra("mobile",
			mfetchappointmentdetails.getParticipant()
					.getMobile());
}

			if(	mfetchappointmentdetails.getParticipant()
					.getSkypeId().equals("")||
					mfetchappointmentdetails.getParticipant()
							.getSkypeId().equals("Not Available")
					)
			{
				canceledrequestintent.putExtra("skypeid",
						"not available");
			}
			else {
				canceledrequestintent.putExtra("skypeid",
						mfetchappointmentdetails.getParticipant()
								.getSkypeId());
			}



if( mfetchappointmentdetails.getParticipant()
		.getHangoutId().equals("")||
		mfetchappointmentdetails.getParticipant()
				.getHangoutId().equals("Not Available")
		)
{
	canceledrequestintent.putExtra("hangoutid",
			"not available");
}
			else{
	canceledrequestintent.putExtra("hangoutid",
			mfetchappointmentdetails.getParticipant()
					.getHangoutId());
	}
	if(mfetchappointmentdetails.getParticipant()
		.getFacetimeId().equals("")||
		mfetchappointmentdetails.getParticipant()
				.getFacetimeId().equals("Not Available")

		)
	{
	canceledrequestintent.putExtra("facetimeid",
			"not available");
	}
			else {
		canceledrequestintent.putExtra("facetimeid",
				mfetchappointmentdetails.getParticipant()
						.getFacetimeId());
		}

			}
		else {
			canceledrequestintent.putExtra("name", mfetchappointmentdetails
					.getOrganizer().getName());
			int participantid = mfetchappointmentdetails
					.getOrganizer().getUserId();
			canceledrequestintent.putExtra("participantid",
					Integer.toString(participantid));
			canceledrequestintent.putExtra("profile_image",
					mfetchappointmentdetails.getOrganizer().getProfileImage());

if(mfetchappointmentdetails.getParticipant()
		.getMobile().equals("")||mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available"))
{
	canceledrequestintent.putExtra("mobile",
			"Not Available");
}
			else {
	canceledrequestintent.putExtra("mobile",
			mfetchappointmentdetails.getParticipant()
					.getMobile());
}

		if(mfetchappointmentdetails.getOrganizer()
				.getSkypeId().equals("")
				|| mfetchappointmentdetails.getOrganizer()
				.getSkypeId().equals("Not Available")      )
		{
			canceledrequestintent.putExtra("skypeid",
					"not available");
		}
		else {
			canceledrequestintent.putExtra("skypeid",
					mfetchappointmentdetails.getOrganizer()
							.getSkypeId());
		}
		if(mfetchappointmentdetails.getOrganizer()
				.getHangoutId().equals("")||
				mfetchappointmentdetails.getOrganizer()
						.getHangoutId().equals("Not Available")
				)
		{
			canceledrequestintent.putExtra("hangoutid",
					"not available");
		}
			else {
			canceledrequestintent.putExtra("hangoutid",
					mfetchappointmentdetails.getOrganizer()
							.getHangoutId());
		}
		if(mfetchappointmentdetails.getOrganizer()
				.getFacetimeId().equals("")||
				mfetchappointmentdetails.getOrganizer()
						.getFacetimeId().equals("Not Available")
				)
		{
			canceledrequestintent.putExtra("facetimeid",
					"not available");
		}
			else {
			canceledrequestintent.putExtra("facetimeid",
					mfetchappointmentdetails.getOrganizer()
							.getFacetimeId());
		}
if(	mfetchappointmentdetails.getOrganizer()
		.getMobile().equals("")||    mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available"))
{
	canceledrequestintent.putExtra("mobile",
			"not available");
}
			else{
	canceledrequestintent.putExtra("mobile",
			mfetchappointmentdetails.getOrganizer()
					.getMobile());
}


							
		}	

		canceledrequestintent.putExtra("topic", 	mfetchappointmentdetails.getTopic());
		canceledrequestintent.putExtra("description", mfetchappointmentdetails.getDescription());
		
		canceledrequestintent.putExtra("priority1",	mfetchappointmentdetails.getAppointmentPriority1());
		canceledrequestintent.putExtra("priority2",mfetchappointmentdetails.getAppointmentPriority2());
		canceledrequestintent.putExtra("priority3", 	mfetchappointmentdetails.getAppointmentPriority3());
		String date1 = mfetchappointmentdetails
				.getAppointmentDate1();
		String time1 = mfetchappointmentdetails
				.getAppointmentTime1();
		String date2 = mfetchappointmentdetails
				.getAppointmentDate2();
		String time2 = mfetchappointmentdetails
				.getAppointmentTime2();
		String date3 = mfetchappointmentdetails
				.getAppointmentDate3();
		String time3 = mfetchappointmentdetails
				.getAppointmentTime3();
		canceledrequestintent.putExtra("date1", date1 + " " + time1);
		canceledrequestintent.putExtra("date2", date2 + " " + time2);
		canceledrequestintent.putExtra("date3", date3 + " " + time3);
		canceledrequestintent.putExtra("intentfrom", "futureappointment");
		canceledrequestintent.putExtra("created_at", "1-3-2014  8:00:00");
		startActivity(canceledrequestintent);
		getActivity().finish();
	}

	private void navigatetoRequestsenddetailscreen(String appointmentid) {

		
		try {
			SYLFetchAppointmentsDetails mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);

			Intent intent = new Intent(SYLAppointmentsFragment.this
					.getActivity(),
					SYLRequestSendActivity.class);


			String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
			String organizer_id = Integer.toString(mfetchappointmentdetails.getOrganizer().getUserId());
			String participnat_id = Integer.toString(mfetchappointmentdetails.getParticipant().getUserId());
			if (curr_userid.equals(organizer_id)) {
				intent.putExtra("name", mfetchappointmentdetails
						.getParticipant().getName());
				int participantid = mfetchappointmentdetails
						.getParticipant().getUserId();
				intent.putExtra("participantid",
						Integer.toString(participantid));
				intent.putExtra("profile_image",
						mfetchappointmentdetails.getParticipant()
								.getProfileImage());


				if(mfetchappointmentdetails.getParticipant()
						.getMobile().equals("")||
						mfetchappointmentdetails.getParticipant()
								.getMobile().equals("Not Available")
						)
				{

					intent.putExtra("mobile",
							"not available");
				}
				else {

					intent.putExtra("mobile",
							mfetchappointmentdetails.getParticipant()
									.getMobile());
				}


				if(mfetchappointmentdetails.getParticipant()
						.getFacetimeId().equals("")||
						mfetchappointmentdetails.getParticipant()
								.getFacetimeId().equals("Not Available")
						)
				{
					intent.putExtra("facetimeid",
							"not available");
				}
				else {
					intent.putExtra("facetimeid",
							mfetchappointmentdetails.getParticipant()
									.getFacetimeId());
				}
			if(mfetchappointmentdetails.getParticipant()
					.getHangoutId().equals("") ||
					mfetchappointmentdetails.getParticipant()
							.getHangoutId().equals("Not Available")
					)
			{
				intent.putExtra("hangoutid",
						"not available");
			}
				else {
				intent.putExtra("hangoutid",
						mfetchappointmentdetails.getParticipant()
								.getHangoutId());
			}
			if(mfetchappointmentdetails
					.getParticipant().getSkypeId().equals("")
					|| mfetchappointmentdetails
					.getParticipant().getSkypeId().equals("Not Available")  )
			{
				intent.putExtra("skypeid","not available");
			}
				else {
				intent.putExtra("skypeid", mfetchappointmentdetails
						.getParticipant().getSkypeId());
			}

			} else {
				intent.putExtra("name", mfetchappointmentdetails
						.getOrganizer().getName());
				int participantid = mfetchappointmentdetails
						.getOrganizer().getUserId();
				intent.putExtra("participantid",
						Integer.toString(participantid));
				intent.putExtra("profile_image",
						mfetchappointmentdetails.getOrganizer().getProfileImage());

				if(mfetchappointmentdetails.getOrganizer()
						.getMobile().equals("")||
						mfetchappointmentdetails.getOrganizer()
								.getMobile().equals("Not Available")
						)
				{
					intent.putExtra("mobile",
							"not available");
				}
				else {
					intent.putExtra("mobile",
							mfetchappointmentdetails.getOrganizer()
									.getMobile());
				}

				if(mfetchappointmentdetails.getOrganizer()
						.getFacetimeId().equals("")||
						mfetchappointmentdetails.getOrganizer()
								.getFacetimeId().equals("Not Available")
						)
				{
					intent.putExtra("facetimeid",
							"not available");
				}
				else {
					intent.putExtra("facetimeid",
							mfetchappointmentdetails.getOrganizer()
									.getFacetimeId());
				}
				if(	mfetchappointmentdetails.getOrganizer()
						.getHangoutId().equals("")||
						mfetchappointmentdetails.getOrganizer()
								.getHangoutId().equals("Not Available")
						)
				{
					intent.putExtra("hangoutid",
							"not available");
				}
				else {
					intent.putExtra("hangoutid",
							mfetchappointmentdetails.getOrganizer()
									.getHangoutId());
				}
                if(mfetchappointmentdetails
						.getOrganizer().getSkypeId().equals("")||
						mfetchappointmentdetails
								.getOrganizer().getSkypeId().equals("Not Available")
						)
				{
					intent.putExtra("skypeid","not available");
				}
			else{
					intent.putExtra("skypeid", mfetchappointmentdetails
							.getOrganizer().getSkypeId());
				}


			}


			intent.putExtra("topic",
					mfetchappointmentdetails.getTopic());
			intent.putExtra("description",
					mfetchappointmentdetails.getDescription());


			intent.putExtra("priority1",
					mfetchappointmentdetails.getAppointmentPriority1());
			if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
				intent.putExtra("priority2",
						"not available");
			} else {
				intent.putExtra("priority2",
						mfetchappointmentdetails.getAppointmentPriority2());
			}
			if (mfetchappointmentdetails.getAppointmentPriority3().equals("")) {
				intent.putExtra("priority3",
						"not available");
			} else {
				intent.putExtra("priority3",
						mfetchappointmentdetails.getAppointmentPriority3());
			}

			intent.putExtra("appointmentid",
					appointmentid);

			String date1 = mfetchappointmentdetails
					.getAppointmentDate1();
			String time1 = mfetchappointmentdetails
					.getAppointmentTime1();
			String date2 = mfetchappointmentdetails
					.getAppointmentDate2();
			String time2 = mfetchappointmentdetails
					.getAppointmentTime2();
			String date3 = mfetchappointmentdetails
					.getAppointmentDate3();
			String time3 = mfetchappointmentdetails
					.getAppointmentTime3();
			intent.putExtra("date1", date1 + " " + time1);
			if (date2.equals("") || time2.equals("")) {
				intent.putExtra("date2", " ");
			} else {
				intent.putExtra("date2", date2 + " " + time2);
			}
			if (date3.equals("") || time3.equals("")) {
				intent.putExtra("date3", " ");
			} else {
				intent.putExtra("date3", date3 + " " + time3);
			}
			intent.putExtra("intentfrom", "futureappointment");
			startActivity(intent);
			getActivity().finish();

		}
		catch (Exception e)
		
		{
			Log.e("Error","Appointments Fragment-->exception occured");
		}
		
		
		
		
		
	}
	protected void navigatetoConfirmedAppointmentDetailScreen(String appointmentid) {

		SYLFetchAppointmentsDetails mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);
if(mfetchappointmentdetails!=null) {
	Intent intent = new Intent(
			SYLAppointmentsFragment.this.getActivity(),
			SYLConfirmedRequestAppointmentActivity.class);
	String curr_userid = SYLSaveValues.getSYLUserID(mFragmentChangeActivity);
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


		if(mfetchappointmentdetails.getParticipant().getMobile().equals("")||
				mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available")
				)
		{
			intent.putExtra("mobile", "not available");
		}
		else {
			intent.putExtra("mobile", mfetchappointmentdetails.getParticipant()
					.getMobile());
		}

		if (mfetchappointmentdetails
				.getParticipant().getHangoutId().equals("") ||
				mfetchappointmentdetails
						.getParticipant().getHangoutId().equals("Not Available")
				) {
			intent.putExtra("hangoutid", "not available");
		} else {
			intent.putExtra("hangoutid", mfetchappointmentdetails
					.getParticipant().getHangoutId());
		}
		if (mfetchappointmentdetails.getParticipant()
				.getSkypeId().equals("") ||
				mfetchappointmentdetails.getParticipant()
						.getSkypeId().equals("Not Available")
				) {
			intent.putExtra("skypeid", "not available"
			);
		} else {
			intent.putExtra("skypeid", mfetchappointmentdetails.getParticipant()
					.getSkypeId());
		}
		if (mfetchappointmentdetails.getParticipant().getFacetimeId().equals("")
				|| mfetchappointmentdetails.getParticipant().getFacetimeId().equals("Not Available")   ) {
			intent.putExtra("facetimeid", "not available");
		} else {
			intent.putExtra("facetimeid", mfetchappointmentdetails.getParticipant().getFacetimeId());
		}


	} else {
		intent.putExtra("name", mfetchappointmentdetails.getOrganizer()
				.getName());
		int participantid = mfetchappointmentdetails.getOrganizer()
				.getUserId();
		intent.putExtra("participantid", Integer.toString(participantid));
		intent.putExtra("profile_image", mfetchappointmentdetails
				.getOrganizer().getProfileImage());


		if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")||
				mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available")
						)
		{
			intent.putExtra("mobile","not available");
		}
		else {
			intent.putExtra("mobile", mfetchappointmentdetails.getOrganizer()
					.getMobile());
		}

		if (mfetchappointmentdetails
				.getOrganizer().getHangoutId().equals("") ||
				mfetchappointmentdetails
						.getOrganizer().getHangoutId().equals("Not Available")
				) {
			intent.putExtra("hangoutid", "not available");
		} else {
			intent.putExtra("hangoutid", mfetchappointmentdetails
					.getOrganizer().getHangoutId());
		}
		if  ( mfetchappointmentdetails
				.getOrganizer().getSkypeId().equals("") ||
				mfetchappointmentdetails
						.getOrganizer().getSkypeId().equals("Not Available")

				) {
			intent.putExtra("skypeid", "not available");
		} else {
			intent.putExtra("skypeid", mfetchappointmentdetails
					.getOrganizer().getSkypeId());
		}
		if (mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("")||
				mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("Not Available")
				) {
			intent.putExtra("facetimeid","not available");
		} else {
			intent.putExtra("facetimeid", mfetchappointmentdetails
					.getOrganizer().getFacetimeId());
		}

	}

	intent.putExtra("topic", mfetchappointmentdetails.getTopic());
	intent.putExtra("description",
			mfetchappointmentdetails.getDescription());


	intent.putExtra("priority1",
			mfetchappointmentdetails.getAppointmentPriority1());

	if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
		intent.putExtra("priority2",
				"not available");
	} else {
		intent.putExtra("priority2",
				mfetchappointmentdetails.getAppointmentPriority2());
	}
	if (mfetchappointmentdetails.getAppointmentPriority3().equals("")) {
		intent.putExtra("priority3",
				"not available");
	} else {
		intent.putExtra("priority3",
				mfetchappointmentdetails.getAppointmentPriority3());
	}

	intent.putExtra("appointmentid", appointmentid);
	String date1 = mfetchappointmentdetails.getAppointmentDate1();
	String time1 = mfetchappointmentdetails.getAppointmentTime1();
	String date2 = mfetchappointmentdetails.getAppointmentDate2();
	String time2 = mfetchappointmentdetails.getAppointmentTime2();
	String date3 = mfetchappointmentdetails.getAppointmentDate3();
	String time3 = mfetchappointmentdetails.getAppointmentTime3();
	intent.putExtra("date1", date1 + " " + time1);

	if (date2.equals("") || time2.equals("")) {
		intent.putExtra("date2", " ");
	} else {
		intent.putExtra("date2", date2 + " " + time2);
	}
	if (date3.equals("") || time3.equals(" ")) {
		intent.putExtra("date3", " ");
	} else {
		intent.putExtra("date3", date3 + " " + time3);
	}
	intent.putExtra("intentfrom", "futureappointment");
	startActivity(intent);
	getActivity().finish();


}
		
	}
	private void navigatetoFinishedAppointmentDetailScreen(String appointmentid)
	{
		Intent finishedappointmentintent = new Intent(getActivity(),
				SYLFinishedAppointmentDetailActivity.class);
		SYLFetchAppointmentsDetails mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);
		String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
		String organizer_id = Integer.toString(mfetchappointmentdetails
				.getOrganizer().getUserId());
		String participnat_id = Integer.toString(mfetchappointmentdetails
				.getParticipant().getUserId());
		if (curr_userid.equals(organizer_id)) {
			finishedappointmentintent.putExtra("name", mfetchappointmentdetails
					.getParticipant().getName());
			int participantid = mfetchappointmentdetails.getParticipant()
					.getUserId();
			finishedappointmentintent.putExtra("participantid",
					Integer.toString(participantid));
			finishedappointmentintent
					.putExtra("profile_image", mfetchappointmentdetails
							.getParticipant().getProfileImage());

if(mfetchappointmentdetails.getParticipant().getMobile().equals("")|| mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available"))
{
	finishedappointmentintent
			.putExtra("mobile", "not available");
}
			else{
	finishedappointmentintent
			.putExtra("mobile", mfetchappointmentdetails
					.getParticipant().getMobile());
}

			if(mfetchappointmentdetails
					.getParticipant().getSkypeId().equals("") ||mfetchappointmentdetails
					.getParticipant().getSkypeId().equals("Not Available")      )
			{
				finishedappointmentintent
						.putExtra("skypeid", "not available");
			}
			else {
				finishedappointmentintent
						.putExtra("skypeid", mfetchappointmentdetails
								.getParticipant().getSkypeId());
			}



			if(mfetchappointmentdetails
					.getParticipant().getFacetimeId().equals("") || mfetchappointmentdetails
					.getParticipant().getFacetimeId().equals("Not Available")    )
			{
				finishedappointmentintent
						.putExtra("facetimeid", "not available");
			}
			else {
				finishedappointmentintent
						.putExtra("facetimeid", mfetchappointmentdetails
								.getParticipant().getFacetimeId());
			}
			if(mfetchappointmentdetails
					.getParticipant().getHangoutId().equals("")|| mfetchappointmentdetails
					.getParticipant().getHangoutId().equals("Not Available"))
			{
				finishedappointmentintent
						.putExtra("hangoutid", "not available");
			}
			else {
				finishedappointmentintent
						.putExtra("hangoutid", mfetchappointmentdetails
								.getParticipant().getHangoutId());
			}

		} else {
			finishedappointmentintent.putExtra("name", mfetchappointmentdetails
					.getOrganizer().getName());
			int participantid = mfetchappointmentdetails.getOrganizer()
					.getUserId();
			finishedappointmentintent.putExtra("participantid",
					Integer.toString(participantid));
			finishedappointmentintent.putExtra("profile_image",
					mfetchappointmentdetails.getOrganizer().getProfileImage());
			if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")||
					mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available"))
			{
				finishedappointmentintent
						.putExtra("mobile", "not available");
			}
			else {
				finishedappointmentintent
						.putExtra("mobile", mfetchappointmentdetails
								.getOrganizer().getMobile());
			}



			if(mfetchappointmentdetails
					.getOrganizer().getSkypeId().equals("") ||  mfetchappointmentdetails
					.getOrganizer().getSkypeId().equals("Not Available")     )
			{
				finishedappointmentintent
						.putExtra("skypeid", "not available");
			}
			else {
				finishedappointmentintent
						.putExtra("skypeid", mfetchappointmentdetails
								.getOrganizer().getSkypeId());
			}



			if(mfetchappointmentdetails
					.getOrganizer().getFacetimeId().equals("") ||mfetchappointmentdetails
					.getOrganizer().getFacetimeId().equals("Not Available")   )
			{
				finishedappointmentintent
						.putExtra("facetimeid", "not available");
			}
			else {
				finishedappointmentintent
						.putExtra("facetimeid", mfetchappointmentdetails
								.getOrganizer().getFacetimeId());
			}
if(mfetchappointmentdetails
		.getOrganizer().getHangoutId().equals("")||  mfetchappointmentdetails
		.getOrganizer().getHangoutId().equals("Not Available")  )
{
	finishedappointmentintent
			.putExtra("hangoutid", "not available");
}
			else {
	finishedappointmentintent
			.putExtra("hangoutid", mfetchappointmentdetails
					.getOrganizer().getHangoutId());
}

		}

		finishedappointmentintent.putExtra("topic",
				mfetchappointmentdetails.getTopic());
		finishedappointmentintent.putExtra("description",
				mfetchappointmentdetails.getDescription());


		finishedappointmentintent.putExtra("priority1",
				mfetchappointmentdetails.getAppointmentPriority1());
		finishedappointmentintent.putExtra("priority2",
				mfetchappointmentdetails.getAppointmentPriority2());
		finishedappointmentintent.putExtra("priority3",
				mfetchappointmentdetails.getAppointmentPriority3());
		String date1 = mfetchappointmentdetails.getAppointmentDate1();
		String time1 = mfetchappointmentdetails.getAppointmentTime1();
		String date2 = mfetchappointmentdetails.getAppointmentDate2();
		String time2 = mfetchappointmentdetails.getAppointmentTime2();
		String date3 = mfetchappointmentdetails.getAppointmentDate3();
		String time3 = mfetchappointmentdetails.getAppointmentTime3();
		finishedappointmentintent.putExtra("date1", date1 + " " + time1);
		finishedappointmentintent.putExtra("date2", date2 + " " + time2);

		if (date3.equals("") || time3.equals("")) {
			finishedappointmentintent.putExtra("date3", "");
		} else {
			finishedappointmentintent.putExtra("date3", date3 + " " + time3);
		}
		finishedappointmentintent.putExtra("intentfrom","futureappointment");
		finishedappointmentintent.putExtra("created_at", "1-3-2014  8:00:00");
		startActivity(finishedappointmentintent);
		getActivity().finish();
	}



	protected void loadAppointmentsdetails(String userID) {
		String listclick = "listclick";
		SYLAppointmentsDetailsManager sYLAppointmentsDetailsManager = new SYLAppointmentsDetailsManager();
		sYLAppointmentsDetailsManager.sYLAppointmentDetailsListner = SYLAppointmentsFragment.this;
		sYLAppointmentsDetailsManager
				.getAppointments(method, userID, listclick);

	}

	@Override
	public void onDidFinished(
			SYLAppointmentsDescription sYLAppointmentsDescription) {

		if (sYLAppointmentsDescription.getSuccess().equals("true")) {
			sYLResponse = sYLAppointmentsDescription.getResponse();

			if (sYLAppointmentsDescription.getResponse().get(0).getType()
					.equals("Request Received")) {

				String name = sYLAppointmentsDescription.getResponse().get(0)
						.getFirstname();
				String profile_image = sYLAppointmentsDescription.getResponse()
						.get(0).getProfile_image();
				String topic = sYLAppointmentsDescription.getResponse().get(0)
						.getTopic();
				String description = sYLAppointmentsDescription.getResponse()
						.get(0).getDescription();
				String mobile = sYLAppointmentsDescription.getResponse().get(0)
						.getMobile();
				String skypeid = sYLAppointmentsDescription.getResponse()
						.get(0).getSkypeid();
				String priority1 = sYLAppointmentsDescription.getResponse()
						.get(0).getPriority1();
				String priority2 = sYLAppointmentsDescription.getResponse()
						.get(0).getPriority2();
				String priority3 = sYLAppointmentsDescription.getResponse()
						.get(0).getPriority3();
				String date1 = sYLAppointmentsDescription.getResponse().get(0)
						.getDate1();
				String date2 = sYLAppointmentsDescription.getResponse().get(0)
						.getDate2();
				String date3 = sYLAppointmentsDescription.getResponse().get(0)
						.getDate3();
				String created_at = sYLAppointmentsDescription.getResponse()
						.get(0).getCreated_at();

			

				Intent intent = new Intent(
						SYLAppointmentsFragment.this.getActivity(),
						SYLRequestReceivedAppointmentActivity.class);

				intent.putExtra("name", name);
				intent.putExtra("profile_image", profile_image);
				intent.putExtra("topic", topic);
				intent.putExtra("description", description);
				intent.putExtra("mobile", mobile);
				intent.putExtra("skypeid", skypeid);
				intent.putExtra("priority1", priority1);
				intent.putExtra("priority2", priority2);
				intent.putExtra("priority3", priority3);
				intent.putExtra("date1", date1);
				intent.putExtra("date2", date2);
				intent.putExtra("date3", date3);
				intent.putExtra("created_at", created_at);
				startActivity(intent);

			}

		}

	}

	private void getCachedData() {

		SYLAppointmentdetailsdatamanager datamanager = new SYLAppointmentdetailsdatamanager(
				getActivity());
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
			if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
				msylProgressDialog.dismiss();
			}
			SYLUtil.buildAlertMessage(getActivity(),"No appointments found");
			return;
		}




		sYLAppointmentsAdapter = new SYLAppointmentsAdapter(getActivity());
		if(todaysappointmentsarraylistcached.size()>0)
		{
			sYLAppointmentsAdapter
					.addSectionHeaderItem("Today's Appointments");
			for (SYLFetchAppointmentsDetails todaysAppointments : todaysappointmentsarraylistcached) {
				String name, profile_image,appointment_indicator;
				String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
				String organizer_id = Integer.toString(todaysAppointments.getOrganizer().getUserId());
				if (curr_userid.equals(organizer_id)) {
					name = todaysAppointments.getParticipant()
							.getName();
					profile_image = todaysAppointments.getParticipant().getProfileImage();
					appointment_indicator="createdbyuser";
				} else {
					name = todaysAppointments.getOrganizer().getName();
					profile_image = todaysAppointments.getOrganizer().getProfileImage();
					appointment_indicator="createdbyothers";
				}
				sYLAppointmentsAdapter.addItem(name + "_" + ""

								+ "_"
								+ todaysAppointments.getAppointmentDate1()
								+ "_"
								+ todaysAppointments.getAppointmentTime1()
								+ "_"
								+ profile_image
								+ "_"
								+ todaysAppointments.getListingType()
								+ "_"
								+ todaysAppointments.getAppointmentID()
								+ "_"
								+appointment_indicator


				);
			}
		}
if(requestReceivedarraylistcached.size()>0)
{
		sYLAppointmentsAdapter.addSectionHeaderItem("Request Received");

		for (SYLFetchAppointmentsDetails reqReceived : requestReceivedarraylistcached) {
			String name, profile_image, appointment_indicator;
			String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
			String organizer_id = Integer.toString(reqReceived.getOrganizer().getUserId());
			if (curr_userid.equals(organizer_id)) {
				name = reqReceived.getParticipant()
						.getName();
				profile_image = reqReceived.getParticipant().getProfileImage();
				appointment_indicator = "createdbyuser";
			} else {
				name = reqReceived.getOrganizer().getName();
				profile_image = reqReceived.getOrganizer().getProfileImage();
				appointment_indicator = "createdbyothers";
			}
			sYLAppointmentsAdapter.addItem(reqReceived.getOrganizer().getName() + "_"
							+ "" + "_" + reqReceived.getAppointmentDate1()
							+ "_" + reqReceived.getAppointmentTime1() + "_"
							+ profile_image + "_"
							+ reqReceived.getListingType() + "_"
							+ reqReceived.getAppointmentID()
							+ "_"
							+ appointment_indicator


			);
		}
		}
if(confirmedAppointmentsarraylistcached.size()>0) {
	sYLAppointmentsAdapter.addSectionHeaderItem("Confirmed Appointments");

	for (SYLFetchAppointmentsDetails confAppointment : confirmedAppointmentsarraylistcached) {
		String name, profile_image, appointment_indicator;
		String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
		String organizer_id = Integer.toString(confAppointment.getOrganizer().getUserId());
		if (curr_userid.equals(organizer_id)) {
			name = confAppointment.getParticipant()
					.getName();
			profile_image = confAppointment.getParticipant().getProfileImage();
			appointment_indicator = "createdbyuser";
		} else {
			name = confAppointment.getOrganizer().getName();
			profile_image = confAppointment.getOrganizer().getProfileImage();
			appointment_indicator = "createdbyothers";
		}


		sYLAppointmentsAdapter.addItem(name + "_"
						+ "" + "_"
						+ confAppointment.getAppointmentDate1() + "_"
						+ confAppointment.getAppointmentTime1() + "_"
						+ profile_image + "_"
						+ confAppointment.getListingType() + "_"
						+ confAppointment.getAppointmentID()
						+ "_"
						+ appointment_indicator


		);

	}
}
		if(requestSendarraylistcached.size()>0)
		{
		sYLAppointmentsAdapter.addSectionHeaderItem("Request Sent");
		for (SYLFetchAppointmentsDetails reqSend : requestSendarraylistcached) {


			String name, profile_image, appointment_indicator;
			String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
			String organizer_id = Integer.toString(reqSend.getOrganizer().getUserId());
			if (curr_userid.equals(organizer_id)) {
				name = reqSend.getParticipant()
						.getName();
				profile_image = reqSend.getParticipant().getProfileImage();
				appointment_indicator = "createdbyuser";
			} else {
				name = reqSend.getOrganizer().getName();
				profile_image = reqSend.getOrganizer().getProfileImage();
				appointment_indicator = "createdbyothers";
			}

			sYLAppointmentsAdapter.addItem(
					name + "_"
							+ ""
							+ "_" + reqSend.getAppointmentDate1() + "_"
							+ reqSend.getAppointmentTime1() + "_"
							+ profile_image
							+ "_"
							+ reqSend.getListingType() + "_"
							+ reqSend.getAppointmentID() + "_"
							+ appointment_indicator


			);
		}
		}
		if (cancelledRequestarraylistcached.size() > 0) {
		sYLAppointmentsAdapter.addSectionHeaderItem("Cancelled Request");
		for (SYLFetchAppointmentsDetails cancelReq : cancelledRequestarraylistcached) {

			String name, profile_image, appointment_indicator;
			String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
			String organizer_id = Integer.toString(cancelReq.getOrganizer().getUserId());
			if (curr_userid.equals(organizer_id)) {
				name = cancelReq.getParticipant()
						.getName();
				profile_image = cancelReq.getParticipant().getProfileImage();
				appointment_indicator = "createdbyuser";
			} else {
				name = cancelReq.getOrganizer().getName();
				profile_image = cancelReq.getOrganizer().getProfileImage();
				appointment_indicator = "createdbyothers";
			}


			sYLAppointmentsAdapter.addItem(name + "_"
							+ " " + "_" + cancelReq.getAppointmentDate1() + "_"
							+ cancelReq.getAppointmentTime1() + "_" + profile_image
							+ "_"
							+ cancelReq.getListingType()
							+ "_"
							+ cancelReq.getAppointmentID()
							+ "_"
							+ appointment_indicator


			);
		}
		}
		if (finishedappointmentsarraylistcached.size() > 0) {
			sYLAppointmentsAdapter
					.addSectionHeaderItem("Finished Appointments");
			for (SYLFetchAppointmentsDetails finishedAppointments : finishedappointmentsarraylistcached) {
				String name, profile_image,appointment_indicator;
				String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
				String organizer_id = Integer.toString(finishedAppointments.getOrganizer().getUserId());
				if (curr_userid.equals(organizer_id)) {
					name =  finishedAppointments.getParticipant()
							.getName();
					profile_image =  finishedAppointments.getParticipant().getProfileImage();
					appointment_indicator="createdbyuser";
				} else {
					name = finishedAppointments.getOrganizer().getName();
					profile_image =finishedAppointments.getOrganizer().getProfileImage();
					appointment_indicator="createdbyothers";
				}
				sYLAppointmentsAdapter.addItem(name + "_"
						+ " " + "_" + finishedAppointments.getAppointmentDate1() + "_"
						+ finishedAppointments.getAppointmentTime1() + "_" + profile_image
						+ "_"
						+ finishedAppointments.getListingType()
						+ "_"
						+finishedAppointments.getAppointmentID()
						+"_"
						+appointment_indicator
				);


			}




		}
		setListAdapter(sYLAppointmentsAdapter);
		listView.setOnItemClickListener(mOnItemClickListener);
		listView.setOnItemLongClickListener(mLongclickListener);

	}

	@Override
	public void getAppointmentDetailsFinish(
			SYLFetchAppointmentsResponseModel mFetchappointmentsResponseModel) {
		// TODO Auto-generated method stub
		swipeView.setRefreshing(false);
		try {

			if (mFetchappointmentsResponseModel != null) {

				todaysAppointmentsdetailarraylist = mFetchappointmentsResponseModel
						.getTodaysAppointments();

				requestReceiveddetailarraylist = mFetchappointmentsResponseModel
						.getRequestReceived();
				confirmedAppointmentsdetailarraylist = mFetchappointmentsResponseModel
						.getConfirmedAppointments();


				requestSenddetailarraylist = mFetchappointmentsResponseModel
						.getRequestSend();
				cancelledRequestdetailarraylist = mFetchappointmentsResponseModel
						.getCancelledRequest();
				finishedAppointmentarraylist=mFetchappointmentsResponseModel
						.getFinishedAppointments();
				sYLAppointmentsAdapter = new SYLAppointmentsAdapter(getActivity());
				if( (todaysAppointmentsdetailarraylist.size()<1)
						&&
						(requestReceiveddetailarraylist.size()<1)
						&&
						(confirmedAppointmentsdetailarraylist.size()<1)
						&&
						(requestSenddetailarraylist.size()<1)
						&&
						(cancelledRequestdetailarraylist.size()<1)
						&&
						(finishedAppointmentarraylist.size()<1)
						)
				{
					if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
						msylProgressDialog.dismiss();
					}
					setListAdapter(sYLAppointmentsAdapter);
					SYLUtil.buildAlertMessage(getActivity(),"No appointments found");
					return;
				}


				if (todaysAppointmentsdetailarraylist.size() > 0) {
					sYLAppointmentsAdapter.addSectionHeaderItem("Today's Appointments");

					for (SYLFetchAppointmentsDetails todaysAppointments : todaysAppointmentsdetailarraylist) {

						String name, profile_image,appointment_indicator;
						String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
						String organizer_id = Integer.toString(todaysAppointments.getOrganizer().getUserId());
						if (curr_userid.equals(organizer_id)) {
							name = todaysAppointments.getParticipant()
									.getName();
							profile_image = todaysAppointments.getParticipant().getProfileImage();
							appointment_indicator="createdbyuser";
						} else {
							name = todaysAppointments.getOrganizer().getName();
							profile_image = todaysAppointments.getOrganizer().getProfileImage();
							appointment_indicator="createdbyothers";
						}


						sYLAppointmentsAdapter.addItem(name + "_" + ""

								+ "_"


								+ todaysAppointments.getAppointmentDate1()
								+ "_"
								+ todaysAppointments.getAppointmentTime1()
								+ "_"
								+ profile_image
								+ "_"
								+ todaysAppointments.getListingType()
								+ "_"
								+ todaysAppointments.getAppointmentID()
						        + "_"
								+appointment_indicator
						);

					}
				}
				if (requestReceiveddetailarraylist.size() > 0) {
					sYLAppointmentsAdapter.addSectionHeaderItem("Request Received");

					for (SYLFetchAppointmentsDetails reqReceived : requestReceiveddetailarraylist) {
						String name, profile_image,appointment_indicator;
						String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
						String organizer_id = Integer.toString(reqReceived.getOrganizer().getUserId());
						if (curr_userid.equals(organizer_id)) {
							name = reqReceived.getParticipant()
									.getName();
							profile_image = reqReceived.getParticipant().getProfileImage();
							appointment_indicator="createdbyuser";
						} else {
							name = reqReceived.getOrganizer().getName();
							profile_image = reqReceived.getOrganizer().getProfileImage();
							appointment_indicator="createdbyothers";
						}
						sYLAppointmentsAdapter.addItem(
								name
										+ "_"
										+ ""
										+ "_"
										+ reqReceived.getAppointmentDate1()
										+ "_"
										+ reqReceived.getAppointmentTime1()
										+ "_"
										+ profile_image
										+ "_"
										+ reqReceived.getListingType()
										+ "_"
										+ reqReceived.getAppointmentID()
										+ "_"
										+appointment_indicator

						);

					}
				}
				if (confirmedAppointmentsdetailarraylist.size() > 0) {
					sYLAppointmentsAdapter.addSectionHeaderItem("Confirmed Appointments");

					for (SYLFetchAppointmentsDetails confAppointment : confirmedAppointmentsdetailarraylist) {
						String name, profile_image,appointment_indicator;
						String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
						String organizer_id = Integer.toString(confAppointment.getOrganizer().getUserId());
						if (curr_userid.equals(organizer_id)) {
							name = confAppointment.getParticipant()
									.getName();
							profile_image = confAppointment.getParticipant().getProfileImage();
							appointment_indicator="createdbyuser";
						} else {
							name = confAppointment.getOrganizer().getName();
							profile_image = confAppointment.getOrganizer().getProfileImage();
							appointment_indicator="createdbyothers";
						}


						sYLAppointmentsAdapter.addItem(
								name
										+ "_"
										+ ""
										+ "_"
										+ confAppointment.getAppointmentDate1()
										+ "_"
										+ confAppointment.getAppointmentTime1()
										+ "_"
										+ profile_image
										+ "_"
										+ confAppointment.getListingType()
										+ "_"
										+ confAppointment.getAppointmentID()
										+ "_"
										+appointment_indicator

						);

					}
				}
				if (requestSenddetailarraylist.size() > 0) {
					sYLAppointmentsAdapter.addSectionHeaderItem("Request Sent");
					for (SYLFetchAppointmentsDetails reqSend : requestSenddetailarraylist) {
						String name, profile_image,appointment_indicator;
						String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
						String organizer_id = Integer.toString(reqSend.getOrganizer().getUserId());
						if (curr_userid.equals(organizer_id)) {
							name = reqSend.getParticipant()
									.getName();
							profile_image = reqSend.getParticipant().getProfileImage();
							appointment_indicator="createdbyuser";
						} else {
							name = reqSend.getOrganizer().getName();
							profile_image = reqSend.getOrganizer().getProfileImage();
							appointment_indicator="createdbyothers";
						}

						sYLAppointmentsAdapter.addItem(name
								+ "_" + "" + "_" + reqSend.getAppointmentDate1() + "_"
								+ reqSend.getAppointmentTime1() + "_"
								+ profile_image + "_"
								+ reqSend.getListingType() + "_"
								+ reqSend.getAppointmentID()
								+ "_"
								+appointment_indicator



						);

					}
				}
				if (cancelledRequestdetailarraylist.size() > 0) {
					sYLAppointmentsAdapter.addSectionHeaderItem("Cancelled Request");
					for (SYLFetchAppointmentsDetails cancelReq : cancelledRequestdetailarraylist) {

						String name, profile_image,appointment_indicator;
						String curr_userid = SYLSaveValues.getSYLUserID(getActivity());
						String organizer_id = Integer.toString(cancelReq.getOrganizer().getUserId());
						if (curr_userid.equals(organizer_id)) {
							name = cancelReq.getParticipant()
									.getName();
							profile_image = cancelReq.getParticipant().getProfileImage();
							appointment_indicator="createdbyuser";
						} else {
							name = cancelReq.getOrganizer().getName();
							profile_image = cancelReq.getOrganizer().getProfileImage();
							appointment_indicator="createdbyothers";
						}


						sYLAppointmentsAdapter.addItem(name
								+ "_" + "" + "_" + cancelReq.getAppointmentDate1() + "_"
								+ cancelReq.getAppointmentTime1() + "_"
								+ profile_image + "_"
								+ cancelReq.getListingType() + "_"
								+ cancelReq.getAppointmentID()
								+ "_"
								+appointment_indicator
						);

					}
				}
				if (finishedAppointmentarraylist.size() > 0) {
					sYLAppointmentsAdapter
							.addSectionHeaderItem("Finished Appointments");
					for (SYLFetchAppointmentsDetails finishedAppointment : finishedAppointmentarraylist) {
						String name, profile_image,appointment_indicator;
						String curr_userid = SYLSaveValues
								.getSYLUserID(getActivity());
						String organizer_id = Integer.toString(finishedAppointment
								.getOrganizer().getUserId());
						if (curr_userid.equals(organizer_id)) {
							name = finishedAppointment.getParticipant().getName();
							profile_image = finishedAppointment.getParticipant()
									.getProfileImage();
							appointment_indicator="createdbyuser";
						} else {
							name = finishedAppointment.getOrganizer().getName();
							profile_image =finishedAppointment.getOrganizer()
									.getProfileImage();
							appointment_indicator="createdbyothers";
						}
						sYLAppointmentsAdapter.addItem(name + "_" + "" + "_"
								+ finishedAppointment.getAppointmentDate1() + "_"
								+ finishedAppointment.getAppointmentTime1() + "_"
								+ profile_image + "_" + finishedAppointment.getListingType()
								+ "_" + finishedAppointment.getAppointmentID()
								+ "_"
								+appointment_indicator
						);

					}

				}












			} else {
				SYLUtil.buildAlertMessage(getActivity(), "Unexpeted Server error happened");
			}

			new AsyncTask<SYLFetchAppointmentsResponseModel, Void, SYLFetchAppointmentsResponseModel>() {
				protected void onPreExecute() {
					// Pre Code
				}
				protected SYLFetchAppointmentsResponseModel doInBackground(SYLFetchAppointmentsResponseModel... unused) {
					// Background Code
					clearCalendarEvents();
					SYLAppointmentdetailsdatamanager mmanger = new SYLAppointmentdetailsdatamanager(
							getActivity().getBaseContext());
					mmanger.clearTable("appointments_calendar");
					SYLFetchAppointmentsResponseModel  mFetchappointmentsResponseModel=unused[0];
					if(todaysAppointmentsdetailarraylist!=null) {
						if (todaysAppointmentsdetailarraylist.size() > 0) {

							for (SYLFetchAppointmentsDetails mFetchAppointmentdetails : todaysAppointmentsdetailarraylist) {
									/*The current and upcoming date is only need to add in Calendar*/
								String date1 = mFetchAppointmentdetails.getAppointmentDate1().replace("-", "/");
								String time1 = mFetchAppointmentdetails.getAppointmentTime1();
								if (SYLUtil.checkgivenTimeisValid(date1, time1)) {
									addAppointmenttoCalendar(mFetchAppointmentdetails);
								}
							}

						}
					}
					if(confirmedAppointmentsdetailarraylist!=null) {
						if (confirmedAppointmentsdetailarraylist.size() > 0) {
			/*	SYLAppointmentdetailsdatamanager mmanger = new SYLAppointmentdetailsdatamanager(
						getActivity().getBaseContext());
				mmanger.clearTable("appointments_calendar"); */

							for (SYLFetchAppointmentsDetails mFetchAppointmentdetails : confirmedAppointmentsdetailarraylist) {

						/*The current and upcoming date is only need to add in Calendar*/
								String date1 = mFetchAppointmentdetails.getAppointmentDate1().replace("-", "/");
								String time1 = mFetchAppointmentdetails.getAppointmentTime1();
								if (SYLUtil.checkgivenTimeisValid(date1, time1)) {
									addAppointmenttoCalendar(mFetchAppointmentdetails);
								}
							}
						}
					}

					return mFetchappointmentsResponseModel;
				}
				protected void onPostExecute(SYLFetchAppointmentsResponseModel flag) {
					// Post Code
					listView.removeHeaderView(headerView);
					setListAdapter(sYLAppointmentsAdapter);
					Log.e("after adapter","after adapter");

					if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
						msylProgressDialog.dismiss();
					}

					Log.e("after dismiss","after dismiss");
					listView.setOnItemClickListener(mOnItemClickListener);
					listView.setOnItemLongClickListener(mLongclickListener);

				}
			}.execute(mFetchappointmentsResponseModel);

			/*
			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					String appointmenttype = "";
					String appointmentid = "";
					String headervalue;
					Log.e("itemclick", "item click");
					TextView header = (TextView) view.findViewById(R.id.textSeparator);
					if (header == null) {


						TextView tvappointmenttype = (TextView) view
								.findViewById(R.id.tvappointmenttype);
						TextView tvappointmentid = (TextView) view
								.findViewById(R.id.tvuserid);

						appointmenttype = tvappointmenttype.getText().toString();
						appointmentid = tvappointmentid.getText().toString();
						Log.e("Appointment type", appointmenttype);
						Log.e("Appointment id", appointmentid);


						if (appointmenttype.equals("REQUEST RECEIVED")) {
							SYLFetchAppointmentsDetails mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);
							if (mfetchappointmentdetails != null) {
								Intent intent = new Intent(SYLAppointmentsFragment.this
										.getActivity(),
										SYLRequestReceivedAppointmentActivity.class);
								String curr_userid = SYLSaveValues.getSYLUserID(mFragmentChangeActivity);
								String organizer_id = Integer.toString(mfetchappointmentdetails.getOrganizer().getUserId());
								String participnat_id = Integer.toString(mfetchappointmentdetails.getParticipant().getUserId());
								if (curr_userid.equals(organizer_id)) {
									intent.putExtra("name", mfetchappointmentdetails
											.getParticipant().getName());
									int participantid = mfetchappointmentdetails
											.getParticipant().getUserId();
									intent.putExtra("participantid",
											Integer.toString(participantid));
									intent.putExtra("profile_image",
											mfetchappointmentdetails.getParticipant()
													.getProfileImage());

									if (mfetchappointmentdetails
											.getParticipant().getSkypeId().equals("")) {
										intent.putExtra("skypeid", "not available");
									} else {
										intent.putExtra("skypeid", mfetchappointmentdetails
												.getParticipant().getSkypeId());
									}


									intent.putExtra("mobile", mfetchappointmentdetails
											.getParticipant().getMobile());

									if (mfetchappointmentdetails.getParticipant().getFacetimeId().equals("")) {
										intent.putExtra("facetimeid", "not available");
									} else {
										intent.putExtra("facetimeid", mfetchappointmentdetails
												.getParticipant().getFacetimeId());
									}
									if (mfetchappointmentdetails.getParticipant().getHangoutId().equals("")) {
										intent.putExtra("hangoutid", "not available");
									} else {
										intent.putExtra("hangoutid", mfetchappointmentdetails
												.getParticipant().getHangoutId());
									}

								} else {
									intent.putExtra("name", mfetchappointmentdetails
											.getOrganizer().getName());
									int participantid = mfetchappointmentdetails
											.getOrganizer().getUserId();
									intent.putExtra("participantid",
											Integer.toString(participantid));
									intent.putExtra("profile_image",
											mfetchappointmentdetails.getOrganizer().getProfileImage());
									if (mfetchappointmentdetails
											.getOrganizer().getSkypeId().equals("")) {
										intent.putExtra("skypeid", "not available");
									} else {
										intent.putExtra("skypeid", mfetchappointmentdetails
												.getOrganizer().getSkypeId());
									}

									intent.putExtra("mobile", mfetchappointmentdetails
											.getOrganizer().getMobile());

									if (mfetchappointmentdetails
											.getOrganizer().getFacetimeId().equals("")) {
										intent.putExtra("facetimeid", "not available");
									} else {
										intent.putExtra("facetimeid", mfetchappointmentdetails
												.getOrganizer().getFacetimeId());
									}


									if (mfetchappointmentdetails.getOrganizer().getHangoutId().equals("")) {
										intent.putExtra("hangoutid", "not available");
									} else {
										intent.putExtra("hangoutid", mfetchappointmentdetails
												.getOrganizer().getHangoutId());
									}

								}


								intent.putExtra("topic",
										mfetchappointmentdetails.getTopic());
								intent.putExtra("description",
										mfetchappointmentdetails.getDescription());
								intent.putExtra("mobile", mfetchappointmentdetails
										.getParticipant().getMobile());

								intent.putExtra("priority1",
										mfetchappointmentdetails.getAppointmentPriority1());

								if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
									intent.putExtra("priority2",
											"not available");
								} else {
									intent.putExtra("priority2",
											mfetchappointmentdetails.getAppointmentPriority2());
								}

								if (mfetchappointmentdetails.getAppointmentPriority3().equals("")) {
									intent.putExtra("priority3",
											"not available");
								} else {
									intent.putExtra("priority3",
											mfetchappointmentdetails.getAppointmentPriority3());
								}

								intent.putExtra("appointmentid",
										appointmentid);
								String date1 = mfetchappointmentdetails
										.getAppointmentDate1();
								String time1 = mfetchappointmentdetails
										.getAppointmentTime1();
								String date2 = mfetchappointmentdetails
										.getAppointmentDate2();
								String time2 = mfetchappointmentdetails
										.getAppointmentTime2();
								String date3 = mfetchappointmentdetails
										.getAppointmentDate3();
								String time3 = mfetchappointmentdetails
										.getAppointmentTime3();
								intent.putExtra("date1", date1 + " " + time1);
								intent.putExtra("date2", date2 + " " + time2);
								intent.putExtra("date3", date3 + " " + time3);

								startActivity(intent);
								getActivity().finish();
							}

						} else if (appointmenttype.equals("TODAYS CONFIRMED")) {
							navigatetoTodaysAppointmentDetail(appointmentid);
						} else if (appointmenttype.equals("CANCELLED")) {
							navigatetoCanelrequestDetailscreen(appointmentid);
						} else if (appointmenttype.equals("OTHER CONFIRMED")) {
							navigatetoConfirmedAppointmentDetailScreen(appointmentid);
						} else if (appointmenttype.equals("REQUEST SENT")) {
							navigatetoRequestsenddetailscreen(appointmentid);
						} else if (appointmenttype.equals("FINISHED")) {
							navigatetoFinishedAppointmentDetailScreen(appointmentid);
						}


					} else {
						headervalue = header.getText().toString();
						Log.e("Header value", headervalue);
					}
				}

			});
*//*
			listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View view,
											   int pos, long id) {
					// TODO Auto-generated method stub

					Log.e("long clicked", "pos: " + pos);
					TextView header = (TextView) view.findViewById(R.id.textSeparator);
					if (header == null) {
						TextView tvappointmenttype = (TextView) view
								.findViewById(R.id.tvappointmenttype);
						TextView tvappointmentid = (TextView) view
								.findViewById(R.id.tvuserid);

						String appointmenttype = tvappointmenttype.getText().toString();

						finishAppointmentid = tvappointmentid.getText().toString();
						Log.e("Appointment type", appointmenttype);

						if (appointmenttype.equals("TODAYS CONFIRMED") || appointmenttype.equals("OTHER CONFIRMED")) {
							showAppointmentFinishAlertDialog();
						}

					}
					return true;
				}
			});
*/




		}
		catch (Exception e)
		{
			Log.e("Error",e.getMessage());
		}
	}
private void clearCalendarEvents()
{
	try {
		SYLAppointmentdetailsdatamanager mmanager = new SYLAppointmentdetailsdatamanager(getActivity().getBaseContext());
		ArrayList<String> eventid_arraylist = mmanager.getCalendarEventIDs();
		for (String eventid : eventid_arraylist) {
			ContentResolver cr = getActivity().getContentResolver();
			ContentValues values = new ContentValues();
			Uri deleteUri = null;
			int evetid = Integer.parseInt(eventid);
			deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, evetid);
			int rows = getActivity().getContentResolver().delete(deleteUri, null, null);

		}
	}
	catch (Exception e)
	{
		Log.e("Error",e.getMessage());
	}
}
private void addAppointmenttoCalendar(SYLFetchAppointmentsDetails sylfetchappointmentdetails)
{
	try {
		//String date_time = sylfetchappointmentdetails.getAppointmentDate1()+" "+sylfetchappointmentdetails.getAppointmentTime1();
		String personname;

		String curr_userid = SYLSaveValues
				.getSYLUserID(getActivity());
		String organizer_id = Integer.toString(sylfetchappointmentdetails
				.getOrganizer().getUserId());

		if (curr_userid.equals(organizer_id)) {
			personname=sylfetchappointmentdetails.getParticipant().getName();

			;
		} else {
			personname=sylfetchappointmentdetails.getOrganizer().getName();

		}
		String date_time = sylfetchappointmentdetails.getAppointmentDate1()+" "+sylfetchappointmentdetails.getAppointmentTime1();

		SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date d = f.parse(date_time);
		long milliseconds = d.getTime();
	int appointment_id=sylfetchappointmentdetails.getAppointmentID();

	String title=sylfetchappointmentdetails.getTopic();

	SYLUtil.pushAppointmentsToCalender(appointment_id, getActivity(), title, "SYL", "SYL-Appointment with "+personname, 1, milliseconds, true, false,getActivity().getBaseContext());
	}
	catch(Exception e)
	{
		
	}
}
	private SYLFetchAppointmentsDetails getAppointmentDetailsFromDB(String appointmentid) {
		SYLFetchAppointmentsDetails mfetchappointmentdetailsobject = null;
		SYLAppointmentdetailsdatamanager datamanager = new SYLAppointmentdetailsdatamanager(
				getActivity());
		mfetchappointmentdetailsobject = datamanager
				.getAllAppointmentFetchDetails("TodaysAppointments",appointmentid);
		return mfetchappointmentdetailsobject;
	}
	
	private void setswipeviewProperties()
	{
	      swipeView.setOnRefreshListener(this);
		  	int color1=getResources().getColor(R.color.darkgreen);
			int color2=getResources().getColor(R.color.red);
			int color3=getResources().getColor(R.color.blue);
			int color4=getResources().getColor(R.color.banana_yellow);
			swipeView.setColorScheme(color1,color2,color3,color4);
			
		       SwipeDismissListViewTouchListener touchListener =
		                new SwipeDismissListViewTouchListener(
		                		listView ,
		                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
		                            @Override
		                            public boolean canDismiss(int position) {
		                                return true;
		                            }

		                            @Override
		                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
		                                for (int position : reverseSortedPositions) {
		                                //    mAdapter.remove(mAdapter.getItem(position));
		                              //      arraylist.remove(0);
		                             //       mContactsAdapter.notifyDataSetChanged();
		                                	try{
		                                	Log.e("Dismiss position",""+position);
		                                	String appointmentdetail=(String)  listView.getItemAtPosition(position);
		                                	Log.e("value-h",appointmentdetail);
		                         if( ! ( appointmentdetail.equals("Confirmed Appointments")||appointmentdetail.equals("Request Send")|| appointmentdetail.equals("Cancelled Request")||  appointmentdetail.equals("Request Received")  ||    appointmentdetail.equals("Todays Appointments") )                  )
		                         {
		                        	 delete_appointmentid=SYLUtil.splitGetAppointmentid(appointmentdetail);
		                        	 Log.e("show alert dialog","show alert dialog");
		                        	 showDeleteAlertDialog();
		                         }
		                   
		                         }
		                      
		                                	catch(Exception e)
		                                	{
		                                		Log.e("error ",e.getMessage());
		                                	}
		                                }
		                         
		                            }
		                        });
			
			
		      listView.setOnTouchListener(touchListener);
		        // Setting this scroll listener is required to ensure that during ListView scrolling,
		        // we don't look for swipes.
		       listView.setOnScrollListener(touchListener.makeScrollListener());
			
			
	}
	@Override
	public void onRefresh()
	{
		// TODO Auto-generated method stub
		/*
		   swipeView.postDelayed(new Runnable() {
				 
		         @Override
		         public void run() {
		            swipeView.setRefreshing(true);
		           handler.sendEmptyMessage(0);
		         }
		      }, 1000);
		
		*/
		try{
		swipeView.setRefreshing(true);
		SYLAppointmentdetailsdatamanager mmanger = new SYLAppointmentdetailsdatamanager(
				mFragmentChangeActivity);
		mmanger.clearTable("appointment_details");
		clearAppointmentHistoryTable();
		loadAppointmentList();
	}
	catch (Exception e)
	{

	}

	}
	   Handler handler = new Handler() {
		   String message="";
		      public void handleMessage(android.os.Message msg) {
		 try {

			 if (refreshToggle) {
				 refreshToggle = false;

				 if (SYLUtil.isNetworkAvailable(getActivity())) {
					 message="Appointments list refreshed";
					 SYLAppointmentdetailsdatamanager mmanger = new SYLAppointmentdetailsdatamanager(
							 mFragmentChangeActivity);
					 mmanger.clearTable("appointment_details");
					 loadAppointmentList();
				 }
				 else{
					 message="network not available";
				 }
			 } else {
				 if (SYLUtil.isNetworkAvailable(getActivity())) {
					 message="Appointments list refreshed";
					 refreshToggle = true;
					 loadAppointmentList();
				 }
				 else{
					 message="network not available";
				 }
			 }

			 swipeView.postDelayed(new Runnable() {

				 @Override
				 public void run() {
					 if (getActivity() != null)
						 Toast.makeText(getActivity(),
								 message, Toast.LENGTH_SHORT).show();

					 swipeView.setRefreshing(false);
				 }
			 }, 1000);
		 }
		 catch (Exception e)
		 {

		 }
		      };
		   }; 	
	private void doRemoveAppointment()
	{
		if(SYLUtil.isNetworkAvailable(getActivity())) {
			msylProgressDialog.show();
			GMTtimezone = SYLUtil.getTimeGMTZone(mFragmentChangeActivity);
			SYLAppointmentRemoveViewManager mViewmanager = new SYLAppointmentRemoveViewManager();
			mViewmanager.msylappointmentremoveListener = SYLAppointmentsFragment.this;
			mViewmanager.doRemoveAppointment(delete_appointmentid, accesstoken, GMTtimezone);
		}
		else {
			SYLUtil.buildAlertMessage(getActivity(),"Please check your network connection and try again");
		}
	}
	   private void showDeleteAlertDialog()
	   {
		   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());
	 
				// set title
				alertDialogBuilder.setTitle("Delete SYL User");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Do you want to delete this appointment?")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
						doRemoveAppointment();
						}
					  })
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
				}
	
	@Override
	public void finishAppointmentRemove(
			SYLAppointmentRemoveResponseModel msylappointmentremoveResponseModel) {
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		// TODO Auto-generated method stub
		if(msylappointmentremoveResponseModel!=null)
		{
			if(msylappointmentremoveResponseModel.isSuccess())
			{
				loadAppointmentList();
			}
			else {
				SYLUtil.buildAlertMessage(getActivity(), msylappointmentremoveResponseModel.getError().getErrorDetail());
			}
		}
	}


	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if(mFragmentChangeActivity==null)
		{
			mFragmentChangeActivity=(SYLFragmentChangeActivity)activity;
		}
	}

	private void showAppointmentFinishAlertDialog()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title
		alertDialogBuilder.setTitle("Finish Appointment");

		// set dialog message
		alertDialogBuilder
				.setMessage("Would you like to mark this appointment as a finished?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						doFinishAppointment();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	private void doFinishAppointment()
	{
		if (SYLUtil.isNetworkAvailable(getActivity())) {

			msylProgressDialog.show();
			String timezone=SYLUtil.getTimeGMTZone(getActivity());
			SYLFinishAppointmentViewManager mViewmanager = new SYLFinishAppointmentViewManager();
			mViewmanager.mFinishAppointmentListener = SYLAppointmentsFragment.this;
			mViewmanager.doFinishAppointment(accesstoken,finishAppointmentid,timezone);
		} else {
			SYLUtil.buildAlertMessage(getActivity(),
					getString(R.string.network_alertmessage));
		}
	}

	public void finishAppointment(SYLFinishAppointmentResponseModel mFinishappointmentresponsemodel)
	{

		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		try {
			if (mFinishappointmentresponsemodel.isSuccess()) {
			//	progressdialogflag=true;
				loadAppointmentList();
			}
			else {
				SYLUtil.buildAlertMessage(getActivity(), "Mark this appointment as finished is failed");
			}
		}
		catch (Exception e)
		{
			SYLUtil.buildAlertMessage(getActivity(),"Unexpected server error happened");
		}
	}

	AdapterView.OnItemClickListener mOnItemClickListener=new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			String appointmenttype = "";
			String appointmentid = "";
			String headervalue;
			Log.e("itemclick", "item click");
			TextView header = (TextView) view
					.findViewById(R.id.textSeparator);
			if (header == null) {

				TextView tvappointmenttype = (TextView) view
						.findViewById(R.id.tvappointmenttype);
				TextView tvappointmentid = (TextView) view
						.findViewById(R.id.tvuserid);

				appointmenttype = tvappointmenttype.getText().toString();
				appointmentid = tvappointmentid.getText().toString();
				Log.e("Appointment type", appointmenttype);
				Log.e("Appointment id", appointmentid);

				if (appointmenttype.equals("REQUEST RECEIVED")) {
					SYLFetchAppointmentsDetails mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);
					if (mfetchappointmentdetails != null) {
						Intent intent = new Intent(
								SYLAppointmentsFragment.this.getActivity(),
								SYLRequestReceivedAppointmentActivity.class);
						String curr_userid = SYLSaveValues
								.getSYLUserID(getActivity());
						String organizer_id = Integer
								.toString(mfetchappointmentdetails
										.getOrganizer().getUserId());
						String participnat_id = Integer
								.toString(mfetchappointmentdetails
										.getParticipant().getUserId());
						if (curr_userid.equals(organizer_id)) {
							intent.putExtra("name", mfetchappointmentdetails
									.getParticipant().getName());
							int participantid = mfetchappointmentdetails
									.getParticipant().getUserId();
							intent.putExtra("participantid",
									Integer.toString(participantid));
							intent.putExtra("profile_image",
									mfetchappointmentdetails.getParticipant()
											.getProfileImage());


							if(mfetchappointmentdetails.getParticipant().getMobile().equals("")||
									mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available"))
							{
								intent.putExtra("mobile", "not available");
							}
							else {
								intent.putExtra("mobile", mfetchappointmentdetails
										.getParticipant().getMobile());
							}


							if (mfetchappointmentdetails
									.getParticipant().getSkypeId().equals("")) {
								intent.putExtra("skypeid", "not available");
							} else {
								intent.putExtra("skypeid", mfetchappointmentdetails
										.getParticipant().getSkypeId());
							}


							if (mfetchappointmentdetails
									.getParticipant().getHangoutId().equals("")
									||
									mfetchappointmentdetails
											.getParticipant().getHangoutId().equals("Not Available")
									) {
								intent.putExtra("hangoutid", "not available"
								);
							} else {
								intent.putExtra("hangoutid", mfetchappointmentdetails
										.getParticipant().getHangoutId());
							}
							if (mfetchappointmentdetails.getParticipant().getFacetimeId().equals("") ||
									mfetchappointmentdetails.getParticipant().getFacetimeId().equals("Not Available")   ) {
								intent.putExtra("facetimeid", "not available");
							} else {
								intent.putExtra("facetimeid", mfetchappointmentdetails
										.getParticipant().getFacetimeId());
							}

						} else {
							intent.putExtra("name", mfetchappointmentdetails
									.getOrganizer().getName());
							int participantid = mfetchappointmentdetails
									.getOrganizer().getUserId();
							intent.putExtra("participantid",
									Integer.toString(participantid));
							intent.putExtra("profile_image",
									mfetchappointmentdetails.getOrganizer()
											.getProfileImage());

if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")|| mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available"))
{
	intent.putExtra("mobile", "not available");
}
							else {
	intent.putExtra("mobile", mfetchappointmentdetails
			.getOrganizer().getMobile());
}

							if (mfetchappointmentdetails
									.getOrganizer().getSkypeId().equals("") || mfetchappointmentdetails
									.getOrganizer().getSkypeId().equals("Not Available")          ) {
								intent.putExtra("skypeid", "not available");
							} else {
								intent.putExtra("skypeid", mfetchappointmentdetails
										.getOrganizer().getSkypeId());
							}

							if (mfetchappointmentdetails.getOrganizer().getHangoutId().equals("") ||
									mfetchappointmentdetails.getOrganizer().getHangoutId().equals("Not Available")   ) {
								intent.putExtra("hangoutid", "not available");
							} else {
								intent.putExtra("hangoutid", mfetchappointmentdetails
										.getOrganizer().getHangoutId());
							}
							if (mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("")
									|| mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("Not Available")  ) {
								intent.putExtra("facetimeid", "not available");
							} else {
								intent.putExtra("facetimeid", mfetchappointmentdetails
										.getOrganizer().getFacetimeId());
							}

						}
						intent.putExtra("topic",
								mfetchappointmentdetails.getTopic());
						intent.putExtra("description",
								mfetchappointmentdetails.getDescription());


						intent.putExtra("priority1", mfetchappointmentdetails
								.getAppointmentPriority1());
						if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
							intent.putExtra("priority2", "not available");
						} else {
							intent.putExtra("priority2", mfetchappointmentdetails
									.getAppointmentPriority2());
						}
						if (mfetchappointmentdetails
								.getAppointmentPriority3().equals("")) {
							intent.putExtra("priority3", "not available");
						} else {
							intent.putExtra("priority3", mfetchappointmentdetails
									.getAppointmentPriority3());
						}

						intent.putExtra("appointmentid", appointmentid);
						String date1 = mfetchappointmentdetails
								.getAppointmentDate1();
						String time1 = mfetchappointmentdetails
								.getAppointmentTime1();
						String date2 = mfetchappointmentdetails
								.getAppointmentDate2();
						String time2 = mfetchappointmentdetails
								.getAppointmentTime2();
						String date3 = mfetchappointmentdetails
								.getAppointmentDate3();
						String time3 = mfetchappointmentdetails
								.getAppointmentTime3();
						intent.putExtra("date1", date1 + " " + time1);
						intent.putExtra("date2", date2 + " " + time2);
						intent.putExtra("date3", date3 + " " + time3);
						intent.putExtra("intentfrom","futureappointment");
						startActivity(intent);
						getActivity().finish();
					}

				} else if (appointmenttype.equals("TODAYS CONFIRMED")) {
					navigatetoTodaysAppointmentDetail(appointmentid);
				} else if (appointmenttype.equals("CANCELLED")) {
					navigatetoCanelrequestDetailscreen(appointmentid);
				} else if (appointmenttype.equals("OTHER CONFIRMED")) {
					navigatetoConfirmedAppointmentDetailScreen(appointmentid);
				} else if (appointmenttype.equals("REQUEST SENT")) {
					navigatetoRequestsenddetailscreen(appointmentid);
				}
				else if (appointmenttype.equals("FINISHED")) {
					navigatetoFinishedAppointmentDetailScreen(appointmentid);
				}





			} else {
				headervalue = header.getText().toString();
				Log.e("Header value", headervalue);
			}



		}
	};

	AdapterView.OnItemLongClickListener mLongclickListener=new AdapterView.OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
			Log.e("long clicked", "pos: " + pos);
			TextView header = (TextView) view.findViewById(R.id.textSeparator);
			if (header == null) {
				TextView tvappointmenttype = (TextView) view
						.findViewById(R.id.tvappointmenttype);
				TextView tvappointmentid = (TextView) view
						.findViewById(R.id.tvuserid);

				String appointmenttype = tvappointmenttype.getText().toString();
				finishAppointmentid = tvappointmentid.getText().toString();
				Log.e("Appointment type", appointmenttype);
				Log.e("Appointment id", finishAppointmentid);
				if (appointmenttype.equals("TODAYS CONFIRMED") || appointmenttype.equals("OTHER CONFIRMED")) {
					showAppointmentFinishAlertDialog();
				}

			}
			return true;
		}
	};

	private void	navigateToAppointmentHistoryScreen()
	{
		Intent historyappointmentIntent=new Intent(getActivity(), SYLHistoryAppointmentsActivity.class);
		startActivity(historyappointmentIntent);


	}

	private void clearAppointmentHistoryTable() {
		SYLHistoryAppointmentsdatamanager mmanger = new SYLHistoryAppointmentsdatamanager(
				getActivity());
		mmanger.clearTable("appointmenthistory_details");
	}
}
