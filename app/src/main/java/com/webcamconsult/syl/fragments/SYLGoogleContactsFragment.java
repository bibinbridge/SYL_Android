package com.webcamconsult.syl.fragments;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.activities.SYLSigninActivity;
import com.webcamconsult.syl.adapters.SYLContactsAdapter;
import com.webcamconsult.syl.adapters.SYLGmailContactsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLGmailFacebookContactsManager;
import com.webcamconsult.syl.fragments.RefreshableListView.OnRefreshListener;
import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.interfaces.SYLFacebookTokenListener;
import com.webcamconsult.syl.interfaces.SYLGmailContactsListener;
import com.webcamconsult.syl.model.GmailContacts;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.model.SYLGmailContactResponsemodel;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLGoogleContacts;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLFacebookGmailContactsViewManager;


public class SYLGoogleContactsFragment extends Fragment implements
 SYLGmailContactsListener {

	private static final String TAG = "RetrieveAccessToken";
	private static final int REQ_SIGN_IN_REQUIRED = 55664;

	private String mAccountName;
	private RefreshableListView mGmailcontactsListview;
	private SYLGmailContactsAdapter mGmailContactsAdapter;
	Button mGoogleplusbutton;
	public String account;
	public TextView mhead;
	private Button mGoogleContactsButton;
	EditText mFavouritesEdittext;
	public String google_accesstoken;
	public boolean searchfilterflag = false;
	public String searchfiltervalue;
	ImageView newButton;
	String intentfrom;
	String userid = null;
	String name = null;
	String email=null;
	private Dialog auth_dialog;
	public String accesstoken;
	public String syluserid;
	ProgressDialog msylProgressDialog;
	ArrayList<GmailContacts> mGmailContactsContactpersonsdetails_databasearraylist;
	  boolean refreshToggle = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.googlecontacts_layout, container,
				false);

		mGoogleContactsButton = (Button) view
				.findViewById(R.id.btn_googlecontacts);
		mGmailcontactsListview = (RefreshableListView) view
				.findViewById(R.id.listview_gmailcontacts);
		mFavouritesEdittext = (EditText) view
				.findViewById(R.id.etxt_searchfavourites);
		mFavouritesEdittext.setHint("Search Gmail Contacts");
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
		View v = k.getSupportActionBar().getCustomView();
		mhead = (TextView) v.findViewById(R.id.mytext);
		accesstoken = SYLSaveValues.getSYLaccessToken(getActivity());
		syluserid = SYLSaveValues.getSYLUserID(getActivity());
		newButton = (ImageView) v.findViewById(R.id.btnnew);

		if (intentfrom.equals("newappointmentimportcontact")) {
			newButton.setVisibility(View.INVISIBLE);
		} else if (intentfrom.equals("menufragment")) {
			newButton.setVisibility(View.VISIBLE);
		}

		google_accesstoken = SYLSaveValues.getGoogleToken(getActivity());
		// Initializing google plus api client
		if (!google_accesstoken.equals("")) {
			mhead.setText("Gmail Contacts");
			mGoogleContactsButton.setVisibility(View.GONE);
			newButton.setVisibility(View.VISIBLE);
			loadGmailContactsData();

		} else {
			mGoogleContactsButton.setVisibility(View.VISIBLE);
			mhead.setText("Google Account Signin");
			newButton.setVisibility(View.INVISIBLE);
		}

		mGoogleContactsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// signInWithGplus();
				// ((SYLFragmentChangeActivity) getActivity())
				// .getAccesToken("googleplus");
				// ((SYLFragmentChangeActivity)
				// getActivity()).mGoogleTokenListener =
				// SYLGoogleContactsFragment.this;
				// launchAuthDialog();
	launchAuthDialog();
			}
		});

		mGmailcontactsListview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView textviewuserid = (TextView) view
								.findViewById(R.id.txt_userid);
						TextView txtview_firstname = (TextView) view
								.findViewById(R.id.txt_firstname);
						TextView txtview_email = (TextView) view
								.findViewById(R.id.txt_email);
						userid = textviewuserid.getText().toString();
						name = txtview_firstname.getText().toString();
						email=txtview_email.getText().toString();
						SYLGmailFacebookContactsManager mdatamangaer = new SYLGmailFacebookContactsManager(
								getActivity().getBaseContext());
						if (searchfilterflag) {

							mdatamangaer
									.updatePositionmarkergmailcontactsFalse();
							mdatamangaer
									.updatePositionmarkerGmailcontacts(userid);
							mGmailContactsContactpersonsdetails_databasearraylist
									.clear();
							ArrayList<GmailContacts> mgmailContactContactpersonsdetails_databasearraylist1 = mdatamangaer
									.getGmailContactswithvalue(searchfiltervalue);
							mGmailContactsContactpersonsdetails_databasearraylist
									.addAll(mgmailContactContactpersonsdetails_databasearraylist1);
							mGmailContactsAdapter.notifyDataSetChanged();
						} else {
							Log.e("POsition UserID", textviewuserid.getText()
									.toString());

							mdatamangaer
									.updatePositionmarkergmailcontactsFalse();
							mdatamangaer
									.updatePositionmarkerGmailcontacts(userid);
							mGmailContactsContactpersonsdetails_databasearraylist
									.clear();
							ArrayList<GmailContacts> mContactpersonsdetails_databasearraylist1 = mdatamangaer
									.getGmailContacts();
							mGmailContactsContactpersonsdetails_databasearraylist
									.addAll(mContactpersonsdetails_databasearraylist1);
							mGmailContactsAdapter.notifyDataSetChanged();
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
							intent.putExtra("email", email);
							intent.putExtra("contactsource","gmailcontacts");
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
try{
				searchfilterflag = true;
				searchfiltervalue = s.toString();
				SYLGmailFacebookContactsManager mdatamangaer = new SYLGmailFacebookContactsManager(
						getActivity().getBaseContext());
				ArrayList<GmailContacts> mContactpersonsdetails_databasearraylist2 = mdatamangaer
						.getGmailContactswithvalue(searchfiltervalue);

				mGmailContactsContactpersonsdetails_databasearraylist.clear();
				mGmailContactsContactpersonsdetails_databasearraylist
						.addAll(mContactpersonsdetails_databasearraylist2);
				mGmailContactsAdapter.notifyDataSetChanged();
}
catch(Exception e){}
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

	}

	private void loadGmailContacts(String userid, String accesstoken,
			String gmailtoken, String refreshtoken) {
		msylProgressDialog = new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");
		msylProgressDialog.show();
		String timezone=SYLUtil.getTimeGMTZone(getActivity());
		SYLSaveValues.setGoogleToken(gmailtoken, getActivity());
		SYLFacebookGmailContactsViewManager mFacebookgmailmanager = new SYLFacebookGmailContactsViewManager();
		mFacebookgmailmanager.mGmailContactsListener = SYLGoogleContactsFragment.this;
		mFacebookgmailmanager.getGmailContacts(userid, accesstoken, gmailtoken,
				refreshtoken, getActivity().getBaseContext(),timezone);
	}



	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("fragment on stop", "onstop");
		//mFavouritesEdittext.setText("");
		//mFavouritesEdittext.setHint("Search Google Contacts");
	}

	private void launchAuthDialog() {
		final Context context = getActivity();
		auth_dialog = new Dialog(context);
		auth_dialog.setTitle("Google");
		auth_dialog.setCancelable(true);
		auth_dialog.setContentView(R.layout.sylgmailauth_dialog);

		auth_dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				getActivity().finish();
			}
		});

		WebView web = (WebView) auth_dialog.findViewById(R.id.webv);
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl(SYLGoogleContacts.OAUTH_URL + "?redirect_uri="
				+ SYLGoogleContacts.REDIRECT_URI
				+ "&response_type=code&client_id="
				+ SYLGoogleContacts.CLIENT_ID + "&scope="
				+ SYLGoogleContacts.OAUTH_SCOPE);
		web.setWebViewClient(new WebViewClient() {
			boolean authComplete = false;

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (url.contains("?code=") && authComplete != true) {
					Uri uri = Uri.parse(url);
					String authCode = uri.getQueryParameter("code");
					authComplete = true;
					auth_dialog.dismiss();
					new GoogleAuthToken(context).execute(authCode);
				} else if (url.contains("error=access_denied")) {
					Log.i("", "ACCESS_DENIED_HERE");
					authComplete = true;
					auth_dialog.dismiss();
				}
			}
		});
		auth_dialog.show();
	}

	private class GoogleAuthToken extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;
		private Context context;

		public GoogleAuthToken(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Contacting Google ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					getActivity().finish();
				}
			});
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			String authCode = args[0];
			com.webcamconsult.syl.networkoperations.GetAccessToken jParser = new com.webcamconsult.syl.networkoperations.GetAccessToken();
			JSONObject json = jParser.gettoken(SYLGoogleContacts.TOKEN_URL,
					authCode, SYLGoogleContacts.CLIENT_ID,
					SYLGoogleContacts.CLIENT_SECRET,
					SYLGoogleContacts.REDIRECT_URI,
					SYLGoogleContacts.GRANT_TYPE);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			if (json != null) {
				try {
					String tok = json.getString("access_token");
					google_accesstoken=tok;
					String expire = json.getString("expires_in");
					String refresh = json.getString("refresh_token");
					loadGmailContacts(syluserid, accesstoken, tok, refresh);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void finishGmailContacts(
			SYLGmailContactResponsemodel mGmailContactresponsemodel) {
		// TODO Auto-generated method stub
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		mGmailcontactsListview.completeRefreshing();
		if(mGmailContactresponsemodel.isSuccess()){
		 SYLSaveValues.setGoogleToken(google_accesstoken, getActivity());
		 
		 mhead.setText("Gmail Contacts");
//		SYLGmailFacebookContactsManager mmanager = new SYLGmailFacebookContactsManager(
//				getActivity().getBaseContext());
//		mGmailContactsContactpersonsdetails_databasearraylist = mmanager
//				.getGmailContacts();
//		mGmailcontactsListview.setVisibility(View.VISIBLE);
//		mFavouritesEdittext.setVisibility(View.VISIBLE);
//		mGoogleContactsButton.setVisibility(View.INVISIBLE);
//		mGmailContactsAdapter = new SYLGmailContactsAdapter(getActivity(),
//				R.layout.sylcontacts_eachrow,
//				mGmailContactsContactpersonsdetails_databasearraylist,
//				getActivity());
//		mGmailcontactsListview.setAdapter(mGmailContactsAdapter);
		loadFromLocalDatabase();
		}
	}
private boolean checkLocalData()
{
	ArrayList<GmailContacts> mContactpersonsdetails_cacheddatearraylist=null;
	SYLGmailFacebookContactsManager mdatamangaer = new SYLGmailFacebookContactsManager(
			getActivity().getBaseContext());
	mContactpersonsdetails_cacheddatearraylist = mdatamangaer
			.getGmailContacts();
	if(mContactpersonsdetails_cacheddatearraylist.size()>0)
	{
		return true;
	}
	else {
		return false;
	}

}
private void  loadGmailContactsData()
{
if(	checkLocalData())
{
	loadFromLocalDatabase();
}
else {
	launchAuthDialog();
}
}
private void loadFromLocalDatabase()
{
	
	SYLGmailFacebookContactsManager mmanager = new SYLGmailFacebookContactsManager(
			getActivity().getBaseContext());
	mGmailContactsContactpersonsdetails_databasearraylist = mmanager
			.getGmailContacts();
	mGmailcontactsListview.setVisibility(View.VISIBLE);
	mFavouritesEdittext.setVisibility(View.VISIBLE);
	mGoogleContactsButton.setVisibility(View.INVISIBLE);
	mGmailContactsAdapter = new SYLGmailContactsAdapter(getActivity(),
			R.layout.sylcontacts_eachrow,
			mGmailContactsContactpersonsdetails_databasearraylist,
			getActivity());
	mGmailcontactsListview.setAdapter(mGmailContactsAdapter);
	mGmailcontactsListview.setOnRefreshListener(new OnRefreshListener() {
		
		@Override
		public void onRefresh(
				com.webcamconsult.syl.fragments.RefreshableListView listView) {
			// TODO Auto-generated method stub
			
			Log.e("refresh1","refresh1");
			mFavouritesEdittext.setText("");
			name="";
			SYLGmailFacebookContactsManager mdatamangaer = new SYLGmailFacebookContactsManager(
					getActivity().getBaseContext());
			mdatamangaer.clearTable("contactpersons_detailsgmail");
		if(SYLSaveValues.getGoogleToken(getActivity()).equals(""))
		{
			launchAuthDialog();
		}
		else {
			String refreshtoken=SYLSaveValues.getGoogleToken(getActivity());
			loadGmailContacts(syluserid, accesstoken, google_accesstoken, refreshtoken);
		}	
		}
	});
	
	if (intentfrom.equals("newappointmentimportcontact")) {
		newButton.setVisibility(View.INVISIBLE);
	} else if (intentfrom.equals("menufragment")) {
		newButton.setVisibility(View.VISIBLE);
	}
	newButton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (intentfrom.equals("menufragment")) {
				Intent intent = new Intent(getActivity(),
						SYLRequestnewappointmentActivity.class);
				intent.putExtra("intentvalue", "menufragment");
				intent.putExtra("contactsource", "gmailcontacts");
				intent.putExtra("user_id", userid);
				intent.putExtra("name", name);
				intent.putExtra("email", email);
				startActivity(intent);
				getActivity().finish();
			} else if (intentfrom.equals("newappointmentimportcontact")) {
				Intent intent = new Intent(getActivity(),
						SYLRequestnewappointmentActivity.class);
				intent.putExtra("intentvalue",
						"newappointmentimportcontact");
				intent.putExtra("user_id", userid);
				intent.putExtra("name", name);
				intent.putExtra("email", email);
				intent.putExtra("contactsource", "googlecontacts");
				startActivity(intent);
				getActivity().finish();
			}
		}
	});
	
	
	

}


}
