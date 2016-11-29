package com.webcamconsult.syl.fragments;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.adapters.SYLContactsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.fragments.RefreshableListView.OnRefreshListener;
import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLContactsViewManager;

public class SYLFragment extends Fragment implements SYLContactsListener {
	private SYLContactsAdapter mContactsAdapter;
	private RefreshableListView mSYLContactsListview;
	EditText etxt_search;
	String intentfrom;
	public boolean searchfilterflag = false;
	ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_arraylist;
	ArrayList<SYLContactPersonsDetails> msylContactpersonsdetails_databasearraylist;
	View view;
	String userid = null;
	String name = null;
	public String searchfiltervalue;
	ProgressDialog msylProgressDialog;
	public boolean refreshflag = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.favourites_layout, container, false);
		etxt_search = (EditText) view.findViewById(R.id.etxt_searchfavourites);
		etxt_search.setHint("Search SYL Contacts");

		mSYLContactsListview = (RefreshableListView) view
				.findViewById(R.id.list_favourites);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Bundle bundle_values = getArguments();
		Log.e("SYL-favouritesfragment", bundle_values.getString("intentfrom"));
		intentfrom = bundle_values.getString("intentfrom");
		Log.e("intent from-syl contacts", intentfrom);
		FragmentActivity i = getActivity();
		SlidingFragmentActivity k = (SlidingFragmentActivity) i;
		View v = k.getSupportActionBar().getCustomView();
		TextView mhead = (TextView) v.findViewById(R.id.mytext);
		mhead.setText("SYL Contacts");
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
					intent.putExtra("contactsource", "sylcontacts");
					startActivity(intent);
					getActivity().finish();
				} else if (intentfrom.equals("newappointmentimportcontact")) {
					Intent intent = new Intent(getActivity(),
							SYLRequestnewappointmentActivity.class);
					intent.putExtra("intentvalue",
							"newappointmentimportcontact");
					intent.putExtra("user_id", userid);
					intent.putExtra("name", name);
					intent.putExtra("contactsource", "sylcontacts");
					startActivity(intent);
					getActivity().finish();
				}
			}
		});
		msylProgressDialog = new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");
		loadSYLdata();
		mSYLContactsListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				// for(SYLContactPersonsDetails
				// contactpersondetails:mContactpersonsdetails_arraylist)
				// {
				// contactpersondetails.setPositiommark(null);
				// }
				// SYLContactPersonsDetails
				// mcontactpersonsdetails=mContactpersonsdetails_arraylist.get(position);
				// mcontactpersonsdetails.setPositiommark("postionmarked");
				// mContactsAdapter.notifyDataSetChanged();

				TextView textviewuserid = (TextView) view
						.findViewById(R.id.txt_userid);

				TextView txtview_firstname = (TextView) view
						.findViewById(R.id.txt_firstname);
				userid = textviewuserid.getText().toString();
				name = txtview_firstname.getText().toString();
				Log.e("userid", userid);
				SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						getActivity().getBaseContext());
				if (searchfilterflag) {

					mdatamangaer.updatePositionmarkersylcontactsFalse();
					mdatamangaer.updatePositionmarkersylContacts(userid);
					msylContactpersonsdetails_databasearraylist.clear();
					ArrayList<SYLContactPersonsDetails> msylContactpersonsdetails_databasearraylist1 = mdatamangaer
							.getsylContactswithvalue(searchfiltervalue);
					msylContactpersonsdetails_databasearraylist
							.addAll(msylContactpersonsdetails_databasearraylist1);
					mContactsAdapter.notifyDataSetChanged();
				} else {

					Log.e("POsition UserID", textviewuserid.getText()
							.toString());

					mdatamangaer.updatePositionmarkersylcontactsFalse();
					mdatamangaer.updatePositionmarkersylContacts(userid);
					msylContactpersonsdetails_databasearraylist.clear();
					ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_databasearraylist1 = mdatamangaer
							.getsylContacts();
					msylContactpersonsdetails_databasearraylist
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
					intent.putExtra("contactsource","sylcontacts");
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
						.getsylContactswithvalue(searchfiltervalue);
				msylContactpersonsdetails_databasearraylist.clear();
				msylContactpersonsdetails_databasearraylist
						.addAll(mContactpersonsdetails_databasearraylist2);
				mContactsAdapter.notifyDataSetChanged();

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

	private void loadSYLContacts() {
		if (!refreshflag) {
			msylProgressDialog.show();
		}
		String userid=SYLSaveValues.getSYLUserID(getActivity());
		String accesstoken=SYLSaveValues.getSYLaccessToken(getActivity());
		String timezone=SYLUtil.getTimeGMTZone(getActivity());
		SYLContactsViewManager mContactsviewmanager = new SYLContactsViewManager();
		mContactsviewmanager.mContactsListener = SYLFragment.this;
		mContactsviewmanager.getSYLContacts("post",getActivity().getBaseContext(),userid,timezone,accesstoken);
	}

	@Override
	public void onDidFinishGetContacts(SYLContacts sylcontacts) {
		// TODO Auto-generated method stub
		mSYLContactsListview.completeRefreshing();
		if (!refreshflag) {

			if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
				msylProgressDialog.dismiss();
			}

		}
		SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
				getActivity().getBaseContext());
		msylContactpersonsdetails_databasearraylist = mdatamangaer
				.getsylContacts();

		// mContactpersonsdetails_arraylist=sylcontacts.getSYLContactPersonsDetails();
		mContactsAdapter = new SYLContactsAdapter(getActivity(),
				R.layout.sylcontacts_eachrow,
				msylContactpersonsdetails_databasearraylist, getActivity());

		mSYLContactsListview.setAdapter(mContactsAdapter);
		mSYLContactsListview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(RefreshableListView listView) {
				// TODO Auto-generated method stub
				refreshflag = true;
				name="";
				etxt_search.setText("");
				SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						getActivity().getBaseContext());
				mdatamangaer.clearTable("contactpersons_detailssyl");
				loadSYLdata();
			}
		});

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Log.e("fragment on stop", "onstop");
		etxt_search.setText("");
		etxt_search.setHint("Search Favourites Contacts");

	}

	private boolean checkCachedData() {
		ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_cacheddatearraylist = null;
		SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
				getActivity().getBaseContext());
		mContactpersonsdetails_cacheddatearraylist = mdatamangaer
				.getsylContacts();
		if (mContactpersonsdetails_cacheddatearraylist.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	private void loadSYLdata() {

		if (checkCachedData()) {
			SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
					getActivity().getBaseContext());
			mdatamangaer.updatePositionmarkersylcontactsFalse();
			msylContactpersonsdetails_databasearraylist = mdatamangaer
					.getsylContacts();

			// mContactpersonsdetails_arraylist=sylcontacts.getSYLContactPersonsDetails();
			mContactsAdapter = new SYLContactsAdapter(getActivity(),
					R.layout.sylcontacts_eachrow,
					msylContactpersonsdetails_databasearraylist, getActivity());

			mSYLContactsListview.setAdapter(mContactsAdapter);

			mSYLContactsListview.setOnRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh(RefreshableListView listView) {
					// TODO Auto-generated method stub
					refreshflag = true;
					etxt_search.setText("");
					SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
							getActivity().getBaseContext());
					mdatamangaer.clearTable("contactpersons_detailssyl");
					loadSYLdata();
				}
			});

		} else {
			loadSYLContacts();
		}
	}

}
