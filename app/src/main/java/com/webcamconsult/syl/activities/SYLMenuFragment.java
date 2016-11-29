package com.webcamconsult.syl.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.adapters.SYLSlidingmenuAdapter;
import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLGmailFacebookContactsManager;
import com.webcamconsult.syl.databaseaccess.SYLHistoryAppointmentsdatamanager;
import com.webcamconsult.syl.fragments.SYLAppointmentsFragment;
import com.webcamconsult.syl.fragments.SYLContactsFragment;
import com.webcamconsult.syl.fragments.SYLProfileFragment;
import com.webcamconsult.syl.fragments.SYLSettingsFragment;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLDateListener;
import com.webcamconsult.syl.interfaces.SYLKeyboardcloseInterface;
import com.webcamconsult.syl.interfaces.SYLSignoutListener;
import com.webcamconsult.syl.model.SYLSignoutRespomseModel;
import com.webcamconsult.syl.model.SlidingmenuItem;
import com.webcamconsult.syl.modelmanager.SYLSignoutModelManager;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;



public class SYLMenuFragment extends ListFragment implements SYLSignoutListener{
    private ArrayList<SlidingmenuItem> navDrawerItems;
    private SYLSlidingmenuAdapter adapter;
    private TypedArray navMenuIcons;
    private String[] lvMenuItems;
    private ListView lvMenu;
    TextView txtview_versionname,txtview_sylusername;
    ScrollView mScrollview;
    ImageView mImageView;
	ProgressDialog msylProgressDialog;
public SYLKeyboardcloseInterface iSYLKeyBoardcloseInterface;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
	
		Log.e("test123","test123");
		View v=inflater.inflate(R.layout.leftslidemenu_list, null);
	//	txtview_versionname=(TextView)v.findViewById(R.id.txt_version);
		txtview_sylusername=(TextView)v.findViewById(R.id.txt_name);
		txtview_sylusername.setFocusable(true);
		mImageView=(ImageView)v.findViewById(R.id.img_image);
		mScrollview=(ScrollView)v.findViewById(R.id.scr);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setFocusable(false);

		mScrollview.post(new Runnable() {
			public void run() {
				mScrollview.fullScroll(ScrollView.FOCUS_UP);
			}
		});






	ImageLoader	imageLoader = new ImageLoader(getActivity());
String profileimageurl=SYLSaveValues.getProfileImageurl(getActivity());
String sylusername=SYLSaveValues.getSYLusername(getActivity());
txtview_sylusername.setText(sylusername);
imageLoader.DisplayImage(profileimageurl, mImageView);

	     lvMenuItems = getResources().getStringArray(R.array.menu_items);
		
	        navMenuIcons = getResources()
	                .obtainTypedArray(R.array.nav_drawer_icons);
		
		
		
	        navDrawerItems = new ArrayList<SlidingmenuItem>();
	        navDrawerItems.add(new SlidingmenuItem(lvMenuItems[0], navMenuIcons.getResourceId(0, -1)));
	        // Find People
	        navDrawerItems.add(new SlidingmenuItem(lvMenuItems[1], navMenuIcons.getResourceId(1, -1)));
	        // Photos
	        navDrawerItems.add(new SlidingmenuItem(lvMenuItems[2], navMenuIcons.getResourceId(2, -1)));
	        // Find People
	        navDrawerItems.add(new SlidingmenuItem(lvMenuItems[3], navMenuIcons.getResourceId(3, -1)));
	        
	        navDrawerItems.add(new SlidingmenuItem(lvMenuItems[4], navMenuIcons.getResourceId(4, -1)));
	 //       navDrawerItems.add(new SlidingmenuItem(lvMenuItems[5], navMenuIcons.getResourceId(5, -1)));
	   //     navDrawerItems.add(new SlidingmenuItem(lvMenuItems[6], navMenuIcons.getResourceId(6, -1)));
	        
	        navMenuIcons.recycle();
	    //    adapter=new SlidingmenuAdapter(getActivity(), navDrawerItems);
	        adapter=new SYLSlidingmenuAdapter(getActivity(), navDrawerItems);
	     
	        setListAdapter(adapter);
	

	        Typeface titilefont=Typeface.createFromAsset(getActivity().getAssets(),"fonts/myriadproregular.OTF");
	 	   
	   // 	txtview_versionname.setTypeface(titilefont);
	        txtview_sylusername.setTypeface(titilefont);
	        

		
		/*
		
		String[] colors = getResources().getStringArray(R.array.color_names);
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, colors);
		setListAdapter(colorAdapter);
		*/
	}




	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		String identifier=null;
		switch (position) {
		case 0:
			newContent = new SYLAppointmentsFragment();
			identifier="appointments";
			break;
		case 1:
			newContent = new SYLProfileFragment();
			identifier="profile";
			break;
		case 2:
			newContent = new SYLContactsFragment();
			Bundle args=new Bundle();
			args.putString("intentfrom", "menufragment");
			newContent.setArguments(args);
			identifier="contacts";
			break;
//		case 3:
//			newContent = new SYLPasswordchangeFragment();
//			break;
		case 3:
			
			newContent = new SYLSettingsFragment();
			identifier="settings";
			break;
	
		case 4:

				Log.e("Logout", "logout");
			hideKeyboard();
				opensignoutAlert();


			break;
//	case 5:
//		newContent = new FragmentSettings();
//		break;
//	case 6:
//		newContent = new FragmentFeedback();
//		break;
		}
		if (newContent != null)
			switchFragment(newContent,identifier);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment,String identifier) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof SYLFragmentChangeActivity) {
			SYLFragmentChangeActivity fca = (SYLFragmentChangeActivity) getActivity();
			fca.switchContent(fragment,identifier);
		} 
		
		/*
		else if (getActivity() instanceof ResponsiveUIActivity) {
			ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
			ra.switchContent(fragment);
		}
		*/
	}
	 private void opensignoutAlert(){
		      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		      alertDialogBuilder.setMessage("Are you sure, want to signout from SYL app?");
		      alertDialogBuilder.setPositiveButton("YES", 
		      new DialogInterface.OnClickListener() {
				
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
		      callSignoutAPI();
		        arg0.dismiss();
		         }
		      });
		      alertDialogBuilder.setNegativeButton("NO", 
		      new DialogInterface.OnClickListener() {
					
		         @Override
		         public void onClick(DialogInterface dialog, int which) {
		        dialog.dismiss();
				 }
		      });
			    
		      AlertDialog alertDialog = alertDialogBuilder.create();
		      alertDialog.show();
			    
		   }
private void callSignoutAPI()
{
	if (SYLUtil.isNetworkAvailable(getActivity())) {
		msylProgressDialog = new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");
		msylProgressDialog.setCancelable(false);
		msylProgressDialog.setCanceledOnTouchOutside(false);
		msylProgressDialog.show();
		String deviceUID = SYLUtil.getUID(getActivity());
		String userid = SYLSaveValues.getSYLUserID(getActivity());
		String timezone = SYLUtil.getTimeGMTZone(getActivity());
		SYLSignoutModelManager mmodelmanager = new SYLSignoutModelManager();
		mmodelmanager.msylsignoutListener = SYLMenuFragment.this;
		mmodelmanager.dosyluserSignout(userid, deviceUID, timezone);
	}
	else {
		SYLUtil.buildAlertMessage(getActivity(),
				getString(R.string.network_alertmessage));
	}
}
	public void hideKeyboard()
	{
		View view =getActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
@Override
public void fisnishsylusersignout(SYLSignoutRespomseModel msignoutresponsemodel) {
	// TODO Auto-generated method stub
	if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
		msylProgressDialog.dismiss();
	}

	try{
		if(msignoutresponsemodel.isSuccess())
		{
	new AsyncTask<Void, Void,Boolean>() {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
			clearAllLocalData();
			return true;
			}
			catch(Exception e)
			{
				return false;
			}
			
		}
		protected void onPostExecute(Boolean result)
		{
		if(result)	
		{
		/*	Intent intent=new Intent(getActivity(),SYLSigninActivity.class);
			startActivity(intent);
			getActivity().finish();
*/
	NotificationManager		mNotificationManager = (NotificationManager)
					getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel(9001);
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("com.package.ACTION_LOGOUT");
		getActivity().sendBroadcast(broadcastIntent);
			Intent intent = new Intent(getActivity().getApplicationContext(),SYLSigninActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			getActivity().finish();
		}
			
		};
	}.execute();
		
		}
		else {
			SYLUtil.buildAlertMessage(getActivity(),"Signout from SYL app is failed");
		}
	}
	catch(Exception e )
	{
		SYLUtil.buildAlertMessage(getActivity(),"Signout from SYL app is failed");
	}
}
private void clearAllLocalData()
{
	SYLSaveValues.setGCMRegistrationid("", getActivity());
	SYLSaveValues.setGoogleRefreshToken("", getActivity());
	SYLSaveValues.setGoogleToken("", getActivity());
	SYLSaveValues.setSYLaccessToken("", getActivity());
	SYLSaveValues.setSYLEmailAddress("", getActivity());
	SYLSaveValues.setSYLUserID("", getActivity());
	SYLSaveValues.setUserImageSource("", getActivity());
	SYLSaveValues.setProfileImageurl("", getActivity());
	SYLSaveValues.setNotificationstatus("", getActivity());
	SYLSaveValues.setSYLFirsttimeLogin("", getActivity());
	SYLSaveValues.setFacebookConnected("", getActivity());
	SYLSaveValues.setSylHideguidednavigation("", getActivity());
	SYLSaveValues.setSylHideguidednavigationContacts("", getActivity());
	SYLSaveValues.setSylHideguidednavigationnewappointment("", getActivity());


	SYLAppointmentdetailsdatamanager  datamanager = new SYLAppointmentdetailsdatamanager(getActivity().getBaseContext());
	datamanager.clearTable("appointment_details");
SYLContactPersonsdatamanager mcontactpersondatamanager=new SYLContactPersonsdatamanager(getActivity().getBaseContext());
mcontactpersondatamanager.clearTable("contactpersons_detailssyl");

mcontactpersondatamanager.clearTable("contactpersons_details");
mcontactpersondatamanager.clearTable("contactpersons_detailsphonecontacts");

	SYLGmailFacebookContactsManager mmanager=new SYLGmailFacebookContactsManager(getActivity().getBaseContext());
mmanager.	clearTable("contactpersons_detailsgmail");
	SYLHistoryAppointmentsdatamanager mhistorymanager=new SYLHistoryAppointmentsdatamanager(getActivity().getBaseContext());
			mhistorymanager.clearTable("appointmenthistory_details");

}



}
