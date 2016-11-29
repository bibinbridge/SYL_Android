package com.webcamconsult.syl.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.adapters.SYLContactsAdapter;

import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.fragments.RefreshableListView.OnRefreshListener;
//import com.webcamconsult.syl.fragments.SwipeDetector.Action;
import com.webcamconsult.syl.guidednavigation.SYLGuidedContactsActivity;
import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLContactsViewManager;

public class SYLFavouritesFragment1 extends Fragment implements
SYLContactsListener,com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout.OnRefreshListener  {
	private SYLContactsAdapter mContactsAdapter;
	ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_arraylist;
	ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_databasearraylist;
	EditText etxt_search;
	ListView mFavouritesListView;
	String intentfrom;
	String userid = null;
	String name = null;
	ProgressDialog msylProgressDialog;
	public boolean searchfilterflag = false;
	public String searchfiltervalue;
	  boolean refreshToggle = true;
		private boolean progressdialogflag=true;
	 private com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout swipeView;
	Bundle detailsbundle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
View		view = inflater.inflate(R.layout.favourites_layout1, container, false);
etxt_search = (EditText) view.findViewById(R.id.etxt_searchfavourites);
swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_view);
mFavouritesListView=(ListView)view.findViewById(android.R.id.list);
return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setswipeviewProperties();
		Bundle bundle_values = getArguments();
		Log.e("SYL-favouritesfragment", bundle_values.getString("intentfrom"));
		intentfrom = bundle_values.getString("intentfrom");
		detailsbundle=bundle_values.getBundle("detailsbundle");
		Log.e("intent from-syl favourites", intentfrom);
		FragmentActivity i = getActivity();
		SlidingFragmentActivity k = (SlidingFragmentActivity) i;
		k.getSupportActionBar().setCustomView(R.layout.action_custom1);
		View v = k.getSupportActionBar().getCustomView();
		TextView mhead = (TextView) v.findViewById(R.id.mytext);
		mhead.setText("Recent Contacts");
		ImageView mButton = (ImageView) v.findViewById(R.id.btnnew);
		if (intentfrom.equals("newappointmentimportcontact")) {
			mButton.setVisibility(View.INVISIBLE);
		} else if (intentfrom.equals("menufragment")) {
			mButton.setVisibility(View.VISIBLE);
		}
		mButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (intentfrom.equals("menufragment")) {
					Intent intent = new Intent(getActivity(),
							SYLRequestnewappointmentActivity.class);
					intent.putExtra("intentvalue", "menufragment");
					intent.putExtra("user_id", userid);
					intent.putExtra("name", name);
					intent.putExtra("contactsource", "favourites");
					startActivity(intent);
					getActivity().finish();
				} else if (intentfrom.equals("newappointmentimportcontact")) {
					Intent intent = new Intent(getActivity(),
							SYLRequestnewappointmentActivity.class);
					intent.putExtra("intentvalue",
							"newappointmentimportcontact");
					intent.putExtra("user_id", userid);
					intent.putExtra("name", name);
					intent.putExtra("contactsource", "favourites");
					startActivity(intent);
					getActivity().finish();
				}
			}
		});
	

loadFavouritesdata();

mFavouritesListView.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub

		// for (SYLContactPersonsDetails contactpersondetails :
		// mContactpersonsdetails_databasearraylist) {
		// contactpersondetails.setPositiommark(null);
		// }
		// SYLContactPersonsDetails mcontactpersonsdetails =
		// mContactpersonsdetails_databasearraylist
		// .get(position);
		// mcontactpersonsdetails.setPositiommark("postionmarked");
		// mContactsAdapter.notifyDataSetChanged();


		

		
 	   Log.e("onclick detected","onclick detected");
		
	TextView textviewuserid = (TextView) view
			.findViewById(R.id.txt_userid);
	TextView txtview_firstname = (TextView) view
			.findViewById(R.id.txt_firstname);
	userid = textviewuserid.getText().toString();
		name = txtview_firstname.getText().toString();
	SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
			getActivity().getBaseContext());

		if (searchfilterflag) {

			mdatamangaer.updatePositionmarkerFalse();
		mdatamangaer.updatePositionmarker(userid);
		mContactpersonsdetails_databasearraylist.clear();
		ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_databasearraylist1 = mdatamangaer
					.getFavouritesContactswithvalue(searchfiltervalue);
			mContactpersonsdetails_databasearraylist
					.addAll(mContactpersonsdetails_databasearraylist1);
			mContactsAdapter.notifyDataSetChanged();
		} else {

		Log.e("POsition UserID", textviewuserid.getText()
					.toString());

			mdatamangaer.updatePositionmarkerFalse();
			mdatamangaer.updatePositionmarker(userid);
			mContactpersonsdetails_databasearraylist.clear();
			ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_databasearraylist1 = mdatamangaer
					.getFavouritesContacts();
			mContactpersonsdetails_databasearraylist
					.addAll(mContactpersonsdetails_databasearraylist1);
		mContactsAdapter.notifyDataSetChanged();
		}
	if (intentfrom.equals("newappointmentimportcontact")) {
			Log.e("vavigate to request new appointment", "navigate");
			Intent intent = new Intent(getActivity(),
				SYLRequestnewappointmentActivity.class);
		intent.putExtra("intentvalue",
					"newappointmentimportcontact");
	
		intent.putExtra("name", name);
		intent.putExtra("user_id", userid);
		intent.putExtra("contactsource", "favourites");
		intent.putExtra("detailsbundle", detailsbundle);
		startActivity(intent);
			getActivity().finish();
		}
}

});



etxt_search.addTextChangedListener(new TextWatcher() {

	@Override
	public void onTextChanged(CharSequence s, int start, int before,
			int count) {
		// TODO Auto-generated method stub
		searchfilterflag = true;
		searchfiltervalue = s.toString();
		SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
				getActivity().getBaseContext());
		ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_databasearraylist2 = mdatamangaer
				.getFavouritesContactswithvalue(searchfiltervalue);
		if(mContactpersonsdetails_databasearraylist2.size()>0) {
			mContactpersonsdetails_databasearraylist.clear();
			mContactpersonsdetails_databasearraylist
					.addAll(mContactpersonsdetails_databasearraylist2);
			mContactsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		Log.e("After Text Changed", "success");
	}
});
	}
	private void loadFavouritesdata()
	{
		if(checkCachedData())
		{
			SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
					getActivity().getBaseContext());
			mdatamangaer.updatePositionmarkerFalse();
			mContactpersonsdetails_databasearraylist = mdatamangaer
					.getFavouritesContacts();

			// mContactpersonsdetails_arraylist = sylcontacts
			// .getSYLContactPersonsDetails();

				mContactsAdapter = new SYLContactsAdapter(getActivity(),
						R.layout.sylcontacts_eachrow,
						mContactpersonsdetails_databasearraylist, getActivity());

				mFavouritesListView.setAdapter(mContactsAdapter);


	
		}
		else {
			if(SYLUtil.isNetworkAvailable(getActivity())) {
				msylProgressDialog = new ProgressDialog(getActivity());
				msylProgressDialog.setMessage("Please wait...");
				msylProgressDialog.show();
				loadFavouritesContacts();
			}
			else {
				SYLUtil.buildAlertMessage(getActivity(),"Kindly please check your network connection");
			}
		}

	}
	private boolean checkCachedData()
	{
		ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_cacheddatearraylist=null;
		SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
				getActivity().getBaseContext());
		mContactpersonsdetails_cacheddatearraylist = mdatamangaer
				.getFavouritesContacts();
		if(mContactpersonsdetails_cacheddatearraylist.size()>0)
		{
			return true;
		}
		else {
			return false;
		}

	}
	private void loadFavouritesContacts() {

	




String userid=SYLSaveValues.getSYLUserID(getActivity());
String accesstoken=SYLSaveValues.getSYLaccessToken(getActivity());
String timezone=SYLUtil.getTimeGMTZone(getActivity());
		SYLContactsViewManager mContactsviewmanager = new SYLContactsViewManager();
		mContactsviewmanager.mContactsListener = SYLFavouritesFragment1.this;
		mContactsviewmanager.getContacts("post", getActivity().getBaseContext(), userid, timezone, accesstoken);
	}
	private void setswipeviewProperties()
	{
	      swipeView.setOnRefreshListener(this);
		  	int color1=getResources().getColor(R.color.darkgreen);
			int color2=getResources().getColor(R.color.red);
			int color3=getResources().getColor(R.color.blue);
			int color4=getResources().getColor(R.color.banana_yellow);
			swipeView.setColorScheme(color1,color2,color3,color4);
	}
	@Override
	public void onDidFinishGetContacts(SYLContacts sylcontacts) {
		// TODO Auto-generated method stub
		try {
			if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
				msylProgressDialog.dismiss();
			}
			if (sylcontacts.getSuccess()) {
				SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						getActivity().getBaseContext());
				mContactpersonsdetails_databasearraylist = mdatamangaer
						.getFavouritesContacts();

				// mContactpersonsdetails_arraylist = sylcontacts
				// .getSYLContactPersonsDetails();
				if (mContactpersonsdetails_databasearraylist.size() > 0) {
					mContactsAdapter = new SYLContactsAdapter(getActivity(),
							R.layout.sylcontacts_eachrow,
							mContactpersonsdetails_databasearraylist, getActivity());

					mFavouritesListView.setAdapter(mContactsAdapter);
				} else {
					SYLUtil.buildAlertMessage(getActivity(), "No Recent contacts found");
				}
			} else {
				SYLUtil.buildAlertMessage(getActivity(), sylcontacts.getError().getErrorDetail());
			}
		}
		catch (Exception e)
		{
			Log.e("Error","Exception occured");
		}
		}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
	      swipeView.postDelayed(new Runnable() {

			  @Override
			  public void run() {
				  swipeView.setRefreshing(true);
				  handler.sendEmptyMessage(0);
			  }
		  }, 1000);
	}
	   Handler handler = new Handler() {
		      public void handleMessage(android.os.Message msg) {
		 try {
			 if (refreshToggle) {
				 refreshToggle = false;
				 name = "";
				 etxt_search.setText("");

				 SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						 getActivity().getBaseContext());
				 mdatamangaer.clearTable("contactpersons_details");
				 loadFavouritesdata();

			 } else {
				 name = "";
				 etxt_search.setText("");
				 refreshToggle = true;

				 SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						 getActivity().getBaseContext());
				 mdatamangaer.clearTable("contactpersons_details");

				 loadFavouritesdata();
			 }

			 swipeView.postDelayed(new Runnable() {

				 @Override
				 public void run() {
					 Toast.makeText(getActivity(),
							 "SYL Favourites list Refreshed", Toast.LENGTH_SHORT).show();
					 swipeView.setRefreshing(false);
				 }
			 }, 1000);
		 }
		 catch (Exception e)
		 {

		 }
		      };
		   };
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("fragment on stop", "onstop");
		etxt_search.setText("");
		etxt_search.setHint("Search Your Recent Contacts");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e("on attach called", "onattach called1");

if(!SYLSaveValues.getSylHideguidednavigationContacts(getActivity()).equals("true")) {
	Intent intent = new Intent(getActivity(), SYLGuidedContactsActivity.class);
	getActivity().startActivity(intent);
}

	}
}
