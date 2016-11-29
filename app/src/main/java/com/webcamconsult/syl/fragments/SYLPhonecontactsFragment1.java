package com.webcamconsult.syl.fragments;

import java.util.ArrayList;

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
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.adapters.SYLPhoneContactsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.fragments.RefreshableListView.OnRefreshListener;
import com.webcamconsult.syl.interfaces.SYLPhoneContactsListener;
import com.webcamconsult.syl.model.SYLPhoneContacts;
import com.webcamconsult.syl.modelmanager.SYLPhoneContactsAsyncTask;
import com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout;

public class SYLPhonecontactsFragment1  extends Fragment implements
SYLPhoneContactsListener,com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout.OnRefreshListener   {
	 private com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout swipeView;
	private ListView  mPhonecontactsListview;
	SYLPhoneContactsAdapter mPhoneContactsAdapter;
	ImageView newButton;
	String intentfrom;
	EditText mFavouritesEdittext;
	public String searchfiltervalue;
	ArrayList<SYLPhoneContacts> msylphonecontacts;
	public boolean refreshflag = false;
	public boolean searchfilterflag = false;
	String userid = null;
	String name = null;
	String email = null;
	String mobilenumber;
	  boolean refreshToggle = true;
		private boolean progressdialogflag=true;
		ProgressDialog msylProgressDialog;
		Bundle detailsbundle;
	ArrayList<SYLPhoneContacts> mPhoneContactpersonsdetails_databasearraylist;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.phonecontacts_layout1, container,
				false);
		swipeView = (SwipeRefreshLayout) v.findViewById(R.id.swipe_view);
		mFavouritesEdittext = (EditText) v
				.findViewById(R.id.etxt_searchfavourites);
		mPhonecontactsListview = (ListView) v
				.findViewById(R.id.listview_phonecontacts);
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setswipeviewProperties();
		Bundle bundle_values = getArguments();
		detailsbundle=bundle_values.getBundle("detailsbundle");
		Log.e("SYL-favouritesfragment", bundle_values.getString("intentfrom"));
		intentfrom = bundle_values.getString("intentfrom");
		Log.e("intent from-syl favourites", intentfrom);
		FragmentActivity i = getActivity();
		SlidingFragmentActivity k = (SlidingFragmentActivity) i;
		View v = k.getSupportActionBar().getCustomView();
		TextView mhead = (TextView) v.findViewById(R.id.mytext);
		mhead.setText("Phone contacts");
		newButton = (ImageView) v.findViewById(R.id.btnnew);

		newButton.setVisibility(View.VISIBLE);
		if(intentfrom.equals("newappointmentimportcontact"))
		{
			newButton.setVisibility(View.INVISIBLE);
		}
		
		msylphonecontacts = new ArrayList<SYLPhoneContacts>();
		msylProgressDialog = new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");	
		loadphoneContactdata();
		newButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (intentfrom.equals("menufragment")
						|| intentfrom.equals("newappointmentimportcontact")) {
					Intent intent = new Intent(getActivity(),
							SYLRequestnewappointmentActivity.class);
					intent.putExtra("intentvalue", "menufragment");
					intent.putExtra("user_id", userid);
					intent.putExtra("name", name);
					intent.putExtra("mobilenumber", mobilenumber);
					intent.putExtra("email", email);
					intent.putExtra("contactsource", "phonecontacts");
					startActivity(intent);
					getActivity().finish();
				}

			}
		});
		mFavouritesEdittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				searchfiltervalue = s.toString();
				// if(!searchfiltervalue.equals("")) {
				searchfilterflag = true;

				SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
						getActivity().getBaseContext());
				ArrayList<SYLPhoneContacts> mContactpersonsdetails_databasearraylist2 = datamanager
						.getphoneContactswithvalue(searchfiltervalue);

				mPhoneContactpersonsdetails_databasearraylist.clear();
				mPhoneContactpersonsdetails_databasearraylist
						.addAll(mContactpersonsdetails_databasearraylist2);
				mPhoneContactsAdapter.notifyDataSetChanged();
				// }
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		
		mPhonecontactsListview
		.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView txtview_firstname = (TextView) view
						.findViewById(R.id.txt_firstname);
				TextView txtview_userid = (TextView) view
						.findViewById(R.id.txt_userid);
				TextView txtview_mobilenumber = (TextView) view
						.findViewById(R.id.txt_mobilenumber);
				TextView txtview_email = (TextView) view
						.findViewById(R.id.txt_email);
				userid = txtview_userid.getText().toString();
				name = txtview_firstname.getText().toString();
				mobilenumber = txtview_mobilenumber.getText()
						.toString();
				email = txtview_email.getText().toString();
				Log.e("userid", userid);
				SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
						getActivity().getBaseContext());
				if (searchfilterflag) {

					datamanager
							.updatePositionmarkerphonecontactsFalse();
					datamanager
							.updatePositionmarkerphoneContacts(userid);
					mPhoneContactpersonsdetails_databasearraylist
							.clear();
					ArrayList<SYLPhoneContacts> mfacebookContactpersonsdetails_databasearraylist1 = datamanager
							.getphoneContactswithvalue(searchfiltervalue);
					mPhoneContactpersonsdetails_databasearraylist
							.addAll(mfacebookContactpersonsdetails_databasearraylist1);
					mPhoneContactsAdapter.notifyDataSetChanged();

				} else {

					Log.e("POsition UserID", txtview_userid.getText()
							.toString());

					datamanager
							.updatePositionmarkerphonecontactsFalse();
					datamanager
							.updatePositionmarkerphoneContacts(userid);
					mPhoneContactpersonsdetails_databasearraylist
							.clear();
					ArrayList<SYLPhoneContacts> mContactpersonsdetails_databasearraylist1 = datamanager
							.getPhoneContacts();
					mPhoneContactpersonsdetails_databasearraylist
							.addAll(mContactpersonsdetails_databasearraylist1);
					mPhoneContactsAdapter.notifyDataSetChanged();
				}

				if (intentfrom.equals("newappointmentimportcontact")) {
					Log.e("vavigate to request new appointment",
							"navigate");
					Intent intent = new Intent(getActivity(),
							SYLRequestnewappointmentActivity.class);
					intent.putExtra("intentvalue",
							"newappointmentimportcontact");
					intent.putExtra("name", name);
					intent.putExtra("user_id", userid);
					intent.putExtra("mobilenumber", mobilenumber);
					intent.putExtra("email", email);
					intent.putExtra("contactsource", "phonecontacts");
					intent.putExtra("detailsbundle", detailsbundle);
					startActivity(intent);
					getActivity().finish();
				}

			}
		});
		
		
		
		
		
		
		
	}
	private void loadphoneContactdata() {
		// new Phonecontacts().execute();
		if (checkphoneContactsLocaldata()) {
			// Load phone contacts from local database
			loadPhoneContactsfromlocaldata();
		}
		// else {
		//
		// loadPhoneContacts();
		// }
		else {
			loadcontactsFromPhone();
		}
	}
	private boolean checkphoneContactsLocaldata() {
		SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
				getActivity().getBaseContext());

		mPhoneContactpersonsdetails_databasearraylist = datamanager
				.getPhoneContacts();
		if (mPhoneContactpersonsdetails_databasearraylist.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	private void loadPhoneContactsfromlocaldata()

	{

		SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
				getActivity().getBaseContext());
	datamanager.	updatePositionmarkerphonecontactsFalse();

		mPhoneContactpersonsdetails_databasearraylist = datamanager
				.getPhoneContacts();

		mPhoneContactsAdapter = new SYLPhoneContactsAdapter(getActivity(),
				R.layout.sylcontacts_eachrow,
				mPhoneContactpersonsdetails_databasearraylist, getActivity());

		mPhonecontactsListview.setAdapter(mPhoneContactsAdapter);



	}
	private void loadcontactsFromPhone() {
		
		if(progressdialogflag){
		
			msylProgressDialog.show();
			progressdialogflag=false;
			}
		
	
		SYLFragmentChangeActivity activity = (SYLFragmentChangeActivity) getActivity();
		SYLPhoneContactsAsyncTask asynctask = new SYLPhoneContactsAsyncTask(
				activity);
		asynctask.mListener = SYLPhonecontactsFragment1.this;
		asynctask.execute();
		
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
	public void getPhoneContactsFinish(
			ArrayList<SYLPhoneContacts> sylphonecontacts) {
		// TODO Auto-generated method stub
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		try {
			SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
					getActivity().getBaseContext());
			ArrayList<SYLPhoneContacts> sylphonecontacts1 = sylphonecontacts;
			mPhoneContactpersonsdetails_databasearraylist = datamanager
					.getPhoneContacts();

			mPhoneContactsAdapter = new SYLPhoneContactsAdapter(getActivity(),
					R.layout.sylcontacts_eachrow,
					mPhoneContactpersonsdetails_databasearraylist, getActivity());

			mPhonecontactsListview.setAdapter(mPhoneContactsAdapter);
		}
		catch (Exception e)
		{
			Log.e("Exception","Exception occured");
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
		 
		         if (refreshToggle) {
		            refreshToggle = false;
		        	name="";
		        	mFavouritesEdittext.setText("");
					refreshflag = true;
					name="";
					mFavouritesEdittext.setText("");
							Log.e("refresh1", "refresh1");
							SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
									getActivity().getBaseContext());
							mdatamangaer.clearTable("contactpersons_detailsphonecontacts");
							loadphoneContactdata();
		         
		         } else {
		 			refreshflag = true;
		 			name="";
		 			mFavouritesEdittext.setText("");
		 					Log.e("refresh1", "refresh1");
		 					SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
		 							getActivity().getBaseContext());
		 					mdatamangaer.clearTable("contactpersons_detailsphonecontacts");
		 					loadphoneContactdata();
		         }
		     
		         swipeView.postDelayed(new Runnable() {

					 @Override
					 public void run() {
		 /*       		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
		        			msylProgressDialog.dismiss();
		        		} */
						 if (getActivity() != null) {
							 Toast.makeText(getActivity(),
									 "SYL Phone Contacts list Refreshed", Toast.LENGTH_SHORT).show();
						 }
						 swipeView.setRefreshing(false);
					 }
				 }, 1000);
		      };
		   }; 
			@Override
			public void onStop() {
				// TODO Auto-generated method stub
				super.onStop();
				try {
				Log.e("fragment on stop", "onstop");
				mFavouritesEdittext.setText("");
				mFavouritesEdittext.setHint("Search Phone Contacts");
				}
				catch(Exception e){}
			}
}
