package com.webcamconsult.syl.fragments;

import java.util.ArrayList;
















import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.adapters.SYLContactsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.fragments.RefreshableListView.OnRefreshListener;
import com.webcamconsult.syl.interfaces.SYLContactUnfriendListener;
import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;
import com.webcamconsult.syl.model.SYLContactUnfriendResponseModel;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLContactUnfriendViewManager;
import com.webcamconsult.viewmanager.SYLContactsViewManager;
import com.webcamconsult.syl.swiperefresh.SwipeDismissListViewTouchListener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class SYLUsersFragment1 extends Fragment implements SYLContactsListener,com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout.OnRefreshListener,SYLContactUnfriendListener{
	View view;
	EditText etxt_search;
	String intentfrom;
	ProgressDialog msylProgressDialog,mSylunfriendprogressdialog;
	private ListView mSYLContactsListview;
	String userid = null;
	String name = null;
	ArrayList<SYLContactPersonsDetails> mContactpersonsdetails_arraylist;
	ArrayList<SYLContactPersonsDetails> msylContactpersonsdetails_databasearraylist;
	private SYLContactsAdapter mContactsAdapter;
	 private com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout swipeView;
	  boolean refreshToggle = true;
		public boolean searchfilterflag = false;
		public String searchfiltervalue;
		public int deleteposition;
		private boolean progressdialogflag=true;
		Bundle detailsbundle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.sylusersfragment_layout, container, false);
	     swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_view);
		etxt_search = (EditText) view.findViewById(R.id.etxt_searchfavourites);
		etxt_search.setHint("Search SYL Contacts");
		mSYLContactsListview = (ListView) view
				.findViewById(R.id.listView_sylusers);
		return view;
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
						.getsylContactswithvalue(searchfiltervalue);
				if (mContactpersonsdetails_databasearraylist2.size() > 0) {
					msylContactpersonsdetails_databasearraylist.clear();
					msylContactpersonsdetails_databasearraylist
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
		                		mSYLContactsListview ,
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
		                                	Log.e("Dismiss position",""+position);
		                                	deleteposition=position;
		                      showDeleteAlertDialod();
		                                }
		                               // mContactsAdapter.notifyDataSetChanged();
		                            }
		                        });
			
			
		       mSYLContactsListview.setOnTouchListener(touchListener);
		        // Setting this scroll listener is required to ensure that during ListView scrolling,
		        // we don't look for swipes.
		        mSYLContactsListview.setOnScrollListener(touchListener.makeScrollListener());
			
			
			
			
			
			
			
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

	

		} else {
			if(SYLUtil.isNetworkAvailable(getActivity())) {

				loadSYLContacts();
			}
			else {
				SYLUtil.buildAlertMessage(getActivity(),"Kindly please ckeck your network connection");
			}
		}
	}
	private void loadSYLContacts() {
	
	//	if(progressdialogflag){
		msylProgressDialog = new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");
	
			msylProgressDialog.show();
		//	progressdialogflag=false;
		//	}
		String userid=SYLSaveValues.getSYLUserID(getActivity());
		String accesstoken=SYLSaveValues.getSYLaccessToken(getActivity());
		String timezone=SYLUtil.getTimeGMTZone(getActivity());
		SYLContactsViewManager mContactsviewmanager = new SYLContactsViewManager();
		mContactsviewmanager.mContactsListener = SYLUsersFragment1.this;
		mContactsviewmanager.getSYLContacts("post", getActivity().getBaseContext(), userid, timezone, accesstoken);
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
				msylContactpersonsdetails_databasearraylist = mdatamangaer
						.getsylContacts();

				// mContactpersonsdetails_arraylist=sylcontacts.getSYLContactPersonsDetails();
				if (msylContactpersonsdetails_databasearraylist.size() > 0) {
					mContactsAdapter = new SYLContactsAdapter(getActivity(),
							R.layout.sylcontacts_eachrow,
							msylContactpersonsdetails_databasearraylist, getActivity());

					mSYLContactsListview.setAdapter(mContactsAdapter);
				} else {
					SYLUtil.buildAlertMessage(getActivity(), "NO SYL contacts found");
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
					etxt_search.setText("");
					SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
							getActivity().getBaseContext());
					mdatamangaer.clearTable("contactpersons_detailssyl");
		            loadSYLContacts();
		         
		         } else {
		            refreshToggle = true;
		loadSYLContacts();
		         }
		     
		         swipeView.postDelayed(new Runnable() {
		 
		            @Override
		            public void run() {
		               Toast.makeText(getActivity(),
		                     "SYL users list Refreshed", Toast.LENGTH_SHORT).show();
		               swipeView.setRefreshing(false);
		            }
		         }, 1000);
		      };
		   }; 
	   
	   private void showDeleteAlertDialod()
	   {
		   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());
	 
				// set title
				alertDialogBuilder.setTitle("Delete SYL User");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Do you want to delete this contact")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
						doSylcontactunfriendRequest();
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
	  private void doSylcontactunfriendRequest()
	  {
		  if(SYLUtil.isNetworkAvailable(getActivity())) {
			  mSylunfriendprogressdialog = new ProgressDialog(getActivity());
			  mSylunfriendprogressdialog.setMessage("Please wait...");
			  mSylunfriendprogressdialog.show();
			  SYLContactPersonsDetails msylContactPersondetail = msylContactpersonsdetails_databasearraylist.get(deleteposition);
			  String unfriend_userid = Integer.toString(msylContactPersondetail.getUserId());
			  String sender_userid = SYLSaveValues.getSYLUserID(getActivity());

			  String accesstoken = SYLSaveValues.getSYLaccessToken(getActivity());
			  String timezone = SYLUtil.getTimeGMTZone(getActivity());
			  SYLContactUnfriendViewManager mUnfriendviewmanager = new SYLContactUnfriendViewManager();
			  mUnfriendviewmanager.msylContactUnfriendListener = SYLUsersFragment1.this;
			  mUnfriendviewmanager.doUnfriendsylContact(sender_userid, accesstoken, unfriend_userid, timezone);
		  }
		  else {
			  SYLUtil.buildAlertMessage(getActivity(),"Kindly please check your network connection");
		  }
	  }
	   
	@Override
	public void finishUnfriendRequest(
			SYLContactUnfriendResponseModel mContactUnfriendResponseModel) {
		// TODO Auto-generated method stub
		if (mSylunfriendprogressdialog != null && mSylunfriendprogressdialog.isShowing()) {
			mSylunfriendprogressdialog.dismiss();
		}
		if(mContactUnfriendResponseModel!=null){
		if(mContactUnfriendResponseModel.isSuccess())
		{
			//progressdialogflag=true;
			loadSYLContacts();
		}
		else {
			SYLUtil.buildAlertMessage(getActivity(), "You cant unfriend this contact");
		}
		}
		else {
			SYLUtil.buildAlertMessage(getActivity(), "Unexpected server error haapened");
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("fragment on stop", "onstop");
		etxt_search.setText("");
		etxt_search.setHint("Search SYL Contacts");
	}
	   
	   
}
