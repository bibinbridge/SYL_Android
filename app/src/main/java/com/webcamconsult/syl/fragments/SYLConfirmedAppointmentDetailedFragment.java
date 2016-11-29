package com.webcamconsult.syl.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.activities.SYLRescheduleappointmentActivity;
import com.webcamconsult.syl.imageloader.ImageLoader;

public class SYLConfirmedAppointmentDetailedFragment extends Fragment {
	TextView textview_name, textview_appointmenttopic,
			textview_appointmentdetailsdescription, textview_prioritydate;
	ImageView imageview_clientImage;
	EditText edittext_mobNo, edittext_skypeId, edittext_priorityone;

	public ImageLoader imageLoader;
	Button btn_reshedule, btn_cancel;
	Context mContext;

	public SYLConfirmedAppointmentDetailedFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContext = getActivity();
		imageLoader = new ImageLoader(mContext);

		View view = inflater
				.inflate(R.layout.confirmedappointmentdetails, null);

		edittext_mobNo = (EditText) view.findViewById(R.id.etmobNo);
		edittext_skypeId = (EditText) view.findViewById(R.id.etskypeId);
		edittext_priorityone = (EditText) view.findViewById(R.id.etpriorityone);

		textview_name = (TextView) view.findViewById(R.id.tvname);
		textview_appointmenttopic = (TextView) view
				.findViewById(R.id.tvAppointmentTopic);
		textview_appointmentdetailsdescription = (TextView) view
				.findViewById(R.id.tvAppointmentDetailsdescription);
		textview_prioritydate = (TextView) view
				.findViewById(R.id.tvprioritydate);

		btn_reshedule = (Button) view.findViewById(R.id.btnReshedule);
		btn_cancel = (Button) view.findViewById(R.id.btnCancel);

		imageview_clientImage = (ImageView) view
				.findViewById(R.id.ImClientImage);

		String name = getArguments().getString("name");
		String profile_image = getArguments().getString("profile_image");
		String topic = getArguments().getString("topic");
		String description = getArguments().getString("description");
		String mobile = getArguments().getString("mobile");
		String skypeid = getArguments().getString("skypeid");
		String priority1 = getArguments().getString("priority1");
		// String priority2 = getArguments().getString("priority2");
		// String priority3 = getArguments().getString("priority3");
		String date1 = getArguments().getString("date1");
		// String date2 = getArguments().getString("date2");
		// String date3 = getArguments().getString("date3");
		// String created_at = getArguments().getString("created_at");

		textview_name.setText(name);
		imageLoader.DisplayImage(profile_image, imageview_clientImage);
		textview_appointmenttopic.setText(topic);
		textview_appointmentdetailsdescription.setText(description);
		edittext_mobNo.setText(mobile);
		edittext_skypeId.setText(skypeid);
		edittext_priorityone.setText("priority 2 is " + " " + priority1);
		// edittext_prioritytwo.setText(priority2);
		// textview_prioritythree.setText(priority3);
		textview_prioritydate.setText("preferred date and time " + "\n" + date1+"AM");
		// edittext_prefDateandtimetwo.setText(date2);
		// textview_prefDateandtimethree.setText(date3);
		btn_reshedule.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				navigatetoResheduleScreen();
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		FragmentActivity i = getActivity();
		SlidingFragmentActivity k = (SlidingFragmentActivity) i;
		k.getSupportActionBar().setCustomView(R.layout.actionbardetails);
		View v = k.getSupportActionBar().getCustomView();

		// View v=getS
		TextView mhead = (TextView) v.findViewById(R.id.mytext);
		Button btn_new = (Button) v.findViewById(R.id.btnnew);
		btn_new.setVisibility(View.INVISIBLE);

		Typeface signupfont = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/MuseoSans-300.otf");
		mhead.setTypeface(signupfont);

		mhead.setText("Confirmed Appointments");



	}
	private void navigatetoResheduleScreen()
	{
		Intent intent_requestnewappointment= new Intent(getActivity(),SYLRescheduleappointmentActivity.class);
		startActivity(intent_requestnewappointment);
	}
	protected void SuccessAlert(Context context, String title, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@SuppressLint("NewApi")
							public void onClick(final DialogInterface dialog,
									final int id) {

								SYLAppointmentsFragment sYLFragmentAppointments = new SYLAppointmentsFragment();

								SYLFragmentChangeActivity fca = (SYLFragmentChangeActivity) getActivity();
								fca.switchContent(sYLFragmentAppointments,"");

								dialog.cancel();
							}
						});

		builder.setNegativeButton(R.string.Cancel,
				new DialogInterface.OnClickListener() {
					@SuppressLint("NewApi")
					public void onClick(final DialogInterface dialog,
							final int id) {

						dialog.cancel();
					}
				});

		final AlertDialog alert = builder.create();
		alert.show();

	}

}
