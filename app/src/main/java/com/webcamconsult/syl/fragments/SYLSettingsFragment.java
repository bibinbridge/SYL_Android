package com.webcamconsult.syl.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLSigninActivity;
import com.webcamconsult.syl.interfaces.SYLNotificationListener;
import com.webcamconsult.syl.model.SYLNotificationResponseModel;
import com.webcamconsult.syl.modelmanager.SYLNotificationStatusModelmanager;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLNotificationViewManager;

public class SYLSettingsFragment extends Fragment implements SYLNotificationListener {

	TextView mTitletxtview;
	Switch mNotificationSwitch;
boolean notificationstatus=false;
ProgressDialog msylProgressDialog;
String switchnotificationstatus="";
	public SYLSettingsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, null);

		View v = getActivity().getActionBar().getCustomView();
mNotificationSwitch=(Switch)view.findViewById(R.id.notificationswitch);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		FragmentActivity i = getActivity();
		SlidingFragmentActivity k = (SlidingFragmentActivity) i;

		k.getSupportActionBar().setCustomView(R.layout.action_custom_profile);

		View v = k.getSupportActionBar().getCustomView();
		TextView mhead = (TextView) v.findViewById(R.id.mytext);
		mhead.setText("Settings");
		Button mButton = (Button) v.findViewById(R.id.btnnew);
		mButton.setVisibility(View.INVISIBLE);
		if(SYLSaveValues.getNotificationstatus(getActivity()).equals("true")||SYLSaveValues.getNotificationstatus(getActivity()).equals(""))
		{
			mNotificationSwitch.setChecked(true);
		}
		else {
			mNotificationSwitch.setChecked(false);
		}

	if(mNotificationSwitch.isChecked())
	{
		switchnotificationstatus="true";
	}
	else{
		switchnotificationstatus="false";
	}
		mNotificationSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					notificationstatus=true;
					switchnotificationstatus="true";
				}
				else {
					notificationstatus=false;
					switchnotificationstatus="false";
				}
				doNotificationStatuschange();
			}
		});

	}
private void doNotificationStatuschange()
{
	
	if (SYLUtil.isNetworkAvailable(getActivity())) {
	
	msylProgressDialog = new ProgressDialog(getActivity());
	msylProgressDialog.setMessage("Please wait...");
	msylProgressDialog.show();
	String userid=SYLSaveValues.getSYLUserID(getActivity());
String timezone=SYLUtil.getTimeGMTZone(getActivity());
	String deviceUID=SYLUtil.getUID(getActivity());
	SYLNotificationViewManager mViewmanager=new SYLNotificationViewManager();
	mViewmanager.mNotificationListener=SYLSettingsFragment.this;
	mViewmanager.dosylNotificationChange(userid, notificationstatus, deviceUID,timezone);
	}
	else {
		SYLUtil.buildAlertMessage(getActivity(),
				getString(R.string.network_alertmessage));
	}


}

@Override
public void finishsylNotificationStatus(
		SYLNotificationResponseModel mNotificationResponseModel) {
	// TODO Auto-generated method stub
	if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
		msylProgressDialog.dismiss();
	}
	if(mNotificationResponseModel!=null)
	{
		if(mNotificationResponseModel.isSuccess())
		{
			SYLSaveValues.setNotificationstatus(switchnotificationstatus, getActivity());
			Toast.makeText(getActivity(),"Changed notification status successfully",Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(getActivity(),mNotificationResponseModel.getError().getErrorTitle(),Toast.LENGTH_LONG).show();
			SYLSaveValues.setNotificationstatus(switchnotificationstatus, getActivity());
		}
	}
	else {
		Toast.makeText(getActivity(),"Unexpected server error happened",Toast.LENGTH_LONG).show();
	}
}
}
