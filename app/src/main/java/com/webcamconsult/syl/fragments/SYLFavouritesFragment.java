package com.webcamconsult.syl.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.adapters.SYLContactsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.fragments.RefreshableListView.OnRefreshListener;
//import com.webcamconsult.syl.fragments.SwipeDetector.Action;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLContactsViewManager;

public class SYLFavouritesFragment extends Fragment implements
		SYLContactsListener {
	private SYLContactsAdapter mContactsAdapter;

	ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_arraylist;
	ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_databasearraylist;
	private RefreshableListView mFavouritesListView;
	LayoutInflater inflater1;
	View parentView;
	View childView;
	LinearLayout linearlayout;
	View view;
	String intentfrom;
	EditText etxt_search;
	String userid = null;
	String name = null;
	public ImageLoader mImageLoader;
	public String searchfiltervalue;
	public boolean searchfilterflag = false;
	ProgressDialog msylProgressDialog;
public boolean refreshflag=false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater1 = inflater;
		view = inflater.inflate(R.layout.favourites_layout, container, false);
		mFavouritesListView = (RefreshableListView) view.findViewById(R.id.list_favourites);
		etxt_search = (EditText) view.findViewById(R.id.etxt_searchfavourites);
	
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Bundle bundle_values = getArguments();
		Log.e("SYL-favouritesfragment", bundle_values.getString("intentfrom"));
		intentfrom = bundle_values.getString("intentfrom");
		Log.e("intent from-syl favourites", intentfrom);
		FragmentActivity i = getActivity();
		SlidingFragmentActivity k = (SlidingFragmentActivity) i;
		k.getSupportActionBar().setCustomView(R.layout.action_custom1);
		View v = k.getSupportActionBar().getCustomView();
		TextView mhead = (TextView) v.findViewById(R.id.mytext);
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
					intent.putExtra("contactsource","favourites");
					startActivity(intent);
					getActivity().finish();
				} else if (intentfrom.equals("newappointmentimportcontact")) {
					Intent intent = new Intent(getActivity(),
							SYLRequestnewappointmentActivity.class);
					intent.putExtra("intentvalue",
							"newappointmentimportcontact");
					intent.putExtra("user_id", userid);
					intent.putExtra("name", name);
					intent.putExtra("contactsource","favourites");
					startActivity(intent);
					getActivity().finish();
				}
			}
		});
		mhead.setText("Favourites");
		msylProgressDialog=new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");
		
loadFavouritesdata();
	
		// mImageLoader = new ImageLoader(getActivity());
	  //    final SwipeDetector swipeDetector = new SwipeDetector();
	   //   mFavouritesListView.setOnTouchListener(swipeDetector);
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



				/*

		       if (swipeDetector.getAction() == Action.RL){
                    // do the onSwipe action 
            	   Log.e("swipe detected","swipe detected");
       			TextView textviewuserid = (TextView) view
						.findViewById(R.id.txt_userid);
				TextView txtview_firstname = (TextView) view
					.findViewById(R.id.txt_firstname);
				userid = textviewuserid.getText().toString();
			Log.e("Userid",userid);
               showAlertDialog();
		        
	        
		        }
				
	        else {	
				
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
				startActivity(intent);
					getActivity().finish();
				}
		}
		*/

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
				mContactpersonsdetails_databasearraylist.clear();
				mContactpersonsdetails_databasearraylist
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

	private void loadFavouritesContacts() {
if(!refreshflag)
{
	msylProgressDialog.show();}
String userid=SYLSaveValues.getSYLUserID(getActivity());
String accesstoken=SYLSaveValues.getSYLaccessToken(getActivity());
String timezone=SYLUtil.getTimeGMTZone(getActivity());
		SYLContactsViewManager mContactsviewmanager = new SYLContactsViewManager();
		mContactsviewmanager.mContactsListener = SYLFavouritesFragment.this;
		mContactsviewmanager.getContacts("post",getActivity().getBaseContext(),userid,timezone,accesstoken);
	}

	@Override
	public void onDidFinishGetContacts(SYLContacts sylcontacts)
{
		// TODO Auto-generated method stub
		mFavouritesListView.completeRefreshing();
		
		if(!refreshflag){
		
		if(msylProgressDialog!=null&&msylProgressDialog.isShowing()){
			msylProgressDialog.dismiss();
		}
		
		}
		
		

		SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
				getActivity().getBaseContext());
		mContactpersonsdetails_databasearraylist = mdatamangaer
				.getFavouritesContacts();

		// mContactpersonsdetails_arraylist = sylcontacts
		// .getSYLContactPersonsDetails();
		mContactsAdapter = new SYLContactsAdapter(getActivity(),
				R.layout.sylcontacts_eachrow,
				mContactpersonsdetails_databasearraylist, getActivity());

		mFavouritesListView.setAdapter(mContactsAdapter);
		mFavouritesListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(
					com.webcamconsult.syl.fragments.RefreshableListView listView) {
				// TODO Auto-generated method stub
				refreshflag=true;
				etxt_search.setText("");
				Log.e("refresh1","refresh1");
				SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						getActivity().getBaseContext());
				mdatamangaer.clearTable("contactpersons_details");
				loadFavouritesdata();
			}
		});
		// mImageLoader = new ImageLoader(getActivity());
		//
		// for(int i=0;i<sylcontacts.getSYLContactPersonsDetails().size();i++)
		// {
		// linearlayout=(LinearLayout)view.findViewById(R.id.rel_parent);
		// LinearLayout.LayoutParams currentLayout = new
		// LinearLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		//
		// final View childview=this.inflater1.inflate(R.layout.childllayout,
		// null);
		// childview.setTag("Value"+i);
		// //ed=(EditText)childview.findViewById(R.id.etext_test);
		// //ed.setId(i);
		// //allEds.add(ed);
		// TextView
		// firstname=(TextView)childview.findViewById(R.id.txt_firstname);
		// TextView
		// lastname=(TextView)childview.findViewById(R.id.txt_lastname);
		// ImageView userImageView =
		// (ImageView)childview.findViewById(R.id.row_icon);
		// SYLContactPersonsDetails
		// s=sylcontacts.getSYLContactPersonsDetails().get(i);
		// mImageLoader.DisplayImage(s.getProfileimage_url(),userImageView);
		//
		// firstname.setText(s.getFirst_name());
		//
		//
		// //ev=evaluatequestiondetails.get(i);
		// //question_txtview.setText(ev.getQuestion());
		// linearlayout.addView(childview,currentLayout);
		// linearlayout.setBackgroundColor(Color.TRANSPARENT);
		// linearlayout.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		//
		// Log.e("Valuere",""+childview.getTag());
		// }
		// });
	}
private void showAlertDialog()
{
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		getActivity());

		// set title
		alertDialogBuilder.setTitle("SYL");

		// set dialog message
		alertDialogBuilder
			.setMessage("Are you sure to delete this contact")
		
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					deleteSYLContact();
					dialog.cancel();
				}
			  })
			.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
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
public void  deleteSYLContact()
{
	SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
			getActivity().getBaseContext());
	mdatamangaer.deletefavoritesContactwithuserid(userid);
	mContactpersonsdetails_databasearraylist.clear();
	ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_databasearraylist1 = mdatamangaer
			.getFavouritesContacts();
	mContactpersonsdetails_databasearraylist
			.addAll(mContactpersonsdetails_databasearraylist1);
	mContactsAdapter.notifyDataSetChanged();
	
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
@Override
public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	Log.e("fragment on pause","onpause");
}
@Override
public void onStop() {
	// TODO Auto-generated method stub
	super.onStop();

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
		mFavouritesListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(
					com.webcamconsult.syl.fragments.RefreshableListView listView) {
				// TODO Auto-generated method stub
				Log.e("refresh","refresh");
				name="";
				etxt_search.setText("");
				refreshflag=true;
				SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						getActivity().getBaseContext());
				mdatamangaer.clearTable("contactpersons_details");
				loadFavouritesdata();
		
			}
		});
	}
	else {
		loadFavouritesContacts();
	}

}
}
