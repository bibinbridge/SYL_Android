package com.webcamconsult.syl.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.activities.SYLSigninActivity;
import com.webcamconsult.syl.adapters.SYLContactsAdapter;
import com.webcamconsult.syl.adapters.SYLPhoneContactsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.fragments.RefreshableListView.OnRefreshListener;
import com.webcamconsult.syl.interfaces.SYLPhoneContactsListener;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;
import com.webcamconsult.syl.model.SYLPhoneContacts;
import com.webcamconsult.syl.modelmanager.SYLPhoneContactsAsyncTask;
import com.webcamconsult.viewmanager.SYLPhoneContactsViewManager;

public class SYLPhonecontactsFragment extends Fragment implements
		SYLPhoneContactsListener {

	private RefreshableListView mPhonecontactsListview;
	SYLPhoneContactsAdapter mPhoneContactsAdapter;
	EditText mFavouritesEdittext;
	public String searchfiltervalue;
	String userid = null;
	String name = null;
	String email = null;
	String intentfrom;
	ImageView newButton;
	String mobilenumber;
	public boolean refreshflag = false;
	public boolean searchfilterflag = false;
	ArrayList<SYLPhoneContacts> msylphonecontacts;
	ArrayList<SYLPhoneContacts> mPhoneContactpersonsdetails_databasearraylist;
	ProgressDialog msylProgressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.phonecontacts_layout, container,
				false);

		mFavouritesEdittext = (EditText) v
				.findViewById(R.id.etxt_searchfavourites);
		mPhonecontactsListview = (RefreshableListView) v
				.findViewById(R.id.listview_phonecontacts);
		return v;
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
		TextView mhead = (TextView) v.findViewById(R.id.mytext);
		mhead.setText("Phone contacts");
		newButton = (ImageView) v.findViewById(R.id.btnnew);
		msylProgressDialog = new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");
		newButton.setVisibility(View.VISIBLE);
		if(intentfrom.equals("newappointmentimportcontact"))
		{
			newButton.setVisibility(View.INVISIBLE);
		}
		
		msylphonecontacts = new ArrayList<SYLPhoneContacts>();

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
							startActivity(intent);
							getActivity().finish();
						}

					}
				});

	}

	private void loadPhoneContacts() {

		SYLPhoneContactsViewManager mPhoneContactsmanager = new SYLPhoneContactsViewManager();
		mPhoneContactsmanager.mPhoneContactsListener = SYLPhonecontactsFragment.this;
		mPhoneContactsmanager
				.getphoneContacts((SYLFragmentChangeActivity) getActivity());
	}

	@Override
	public void getPhoneContactsFinish(
			ArrayList<SYLPhoneContacts> sylphonecontacts) {
		// TODO Auto-generated method stub
		// mPhonecontactsListview.completeRefreshing();
		//
		// SYLContactPersonsdatamanager datamanager = new
		// SYLContactPersonsdatamanager(
		// getActivity().getBaseContext());
		// ArrayList<SYLPhoneContacts> sylphonecontacts1 = sylphonecontacts;
		// mPhoneContactpersonsdetails_databasearraylist = datamanager
		// .getPhoneContacts();
		// mPhoneContactsAdapter = new SYLPhoneContactsAdapter(getActivity(),
		// R.layout.sylcontacts_eachrow,
		// mPhoneContactpersonsdetails_databasearraylist, getActivity());
		//
		// mPhonecontactsListview.setAdapter(mPhoneContactsAdapter);
		//
		// mPhonecontactsListview.setOnRefreshListener(new OnRefreshListener() {
		//
		// @Override
		// public void onRefresh(
		// com.webcamconsult.syl.fragments.RefreshableListView listView) {
		// // TODO Auto-generated method stub
		// refreshflag=true;
		// Log.e("refresh1","refresh1");
		// SYLContactPersonsdatamanager mdatamangaer = new
		// SYLContactPersonsdatamanager(
		// getActivity().getBaseContext());
		// mdatamangaer.clearTable("contactpersons_detailsphonecontacts");
		// loadphoneContactdata();
		// }
		// });
		mPhonecontactsListview.completeRefreshing();
		if(!refreshflag) {
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		}

		SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
				getActivity().getBaseContext());
		ArrayList<SYLPhoneContacts> sylphonecontacts1 = sylphonecontacts;
		mPhoneContactpersonsdetails_databasearraylist = datamanager
				.getPhoneContacts();

		mPhoneContactsAdapter = new SYLPhoneContactsAdapter(getActivity(),
				R.layout.sylcontacts_eachrow,
				mPhoneContactpersonsdetails_databasearraylist, getActivity());

		mPhonecontactsListview.setAdapter(mPhoneContactsAdapter);

		mPhonecontactsListview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(
					com.webcamconsult.syl.fragments.RefreshableListView listView) {
				// TODO Auto-generated method stub
				refreshflag = true;
		name="";
		mFavouritesEdittext.setText("");
				Log.e("refresh1", "refresh1");
				SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						getActivity().getBaseContext());
				mdatamangaer.clearTable("contactpersons_detailsphonecontacts");
				loadphoneContactdata();
			}
		});

	}

	class Phonecontacts extends
			AsyncTask<Void, Void, ArrayList<SYLPhoneContacts>> {

		@Override
		protected ArrayList<SYLPhoneContacts> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			StringBuffer sb = new StringBuffer();
			sb.append("......Contact Details.....");
			ContentResolver cr = getActivity().getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
					null, null,
					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
							+ " ASC");
			String phone = null;
			String emailContact = null;
			String emailType = null;
			String image_uri = "";
			Bitmap bitmap = null;
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					SYLPhoneContacts s = new SYLPhoneContacts();
					String id = cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					s.setName(name);
					image_uri = cur
							.getString(cur
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
					s.setProfileimageurl(image_uri);
					image_uri = null;
					if (Integer
							.parseInt(cur.getString(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						System.out.println("name : " + name + ", ID : " + id);
						sb.append("\n Contact Name:" + name);
						Cursor pCur = cr
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = ?", new String[] { id },
										null);
						while (pCur.moveToNext()) {
							phone = pCur
									.getString(pCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							sb.append("\n Phone number:" + phone);
							System.out.println("phone" + phone);
						}
						s.setMobilenumber(phone);
						pCur.close();

						Cursor emailCur = cr
								.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Email.CONTACT_ID
												+ " = ?", new String[] { id },
										null);
						while (emailCur.moveToNext()) {
							emailContact = emailCur
									.getString(emailCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							emailType = emailCur
									.getString(emailCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
							sb.append("\nEmail:" + emailContact + "Email type:"
									+ emailType);
							System.out.println("Email " + emailContact
									+ " Email Type : " + emailType);

						}

						emailCur.close();
					}
					s.setEmail(emailContact);
					emailContact = null;
					// if (image_uri != null) {
					// System.out.println(Uri.parse(image_uri));
					// try {
					// bitmap = MediaStore.Images.Media
					// .getBitmap(getActivity().getContentResolver(),
					// Uri.parse(image_uri));
					// sb.append("\n Image in Bitmap:" + bitmap);
					// System.out.println(bitmap);
					//
					// } catch (FileNotFoundException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					//
					// }

					sb.append("\n........................................");
					msylphonecontacts.add(s);
				}

			}
			return msylphonecontacts;
		}

		@Override
		protected void onPostExecute(ArrayList<SYLPhoneContacts> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mPhoneContactsAdapter = new SYLPhoneContactsAdapter(getActivity(),
					R.layout.sylcontacts_eachrow, result, getActivity());

			mPhonecontactsListview.setAdapter(mPhoneContactsAdapter);
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

		mPhonecontactsListview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(
					com.webcamconsult.syl.fragments.RefreshableListView listView) {
				// TODO Auto-generated method stub
				Log.e("refresh", "refresh");
				refreshflag = true;
				SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
						getActivity());
				mdatamangaer.clearTable("contactpersons_detailsphonecontacts");
				loadphoneContactdata();

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

	private void loadcontactsFromPhone() {
		

	
		if(!refreshflag)
		{
			msylProgressDialog.show();
		}
		SYLFragmentChangeActivity activity = (SYLFragmentChangeActivity) getActivity();
		SYLPhoneContactsAsyncTask asynctask = new SYLPhoneContactsAsyncTask(
				activity);
		asynctask.mListener = SYLPhonecontactsFragment.this;
		asynctask.execute();
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("fragment on pause", "onpause");
	}

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
