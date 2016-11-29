package com.webcamconsult.syl.sylphonecontacts;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLRequestnewappointmentActivity;
import com.webcamconsult.syl.adapters.SYLPhoneContactsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.model.SYLFetchAppointmentsResponseModel;
import com.webcamconsult.syl.model.SYLPhoneContacts;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout;
import com.webcamconsult.syl.utilities.SYLUtil;

import java.util.ArrayList;

/**
 * Created by Sandeep on 7/8/2015.
 */
public class SYLPhoneContactsFragment2 extends  Fragment implements
        SwipeRefreshLayout.OnRefreshListener,LoaderManager.LoaderCallbacks<Cursor> {
    private ListView  mPhonecontactsListview;
    private SwipeRefreshLayout swipeView;
    private SYLPhoneContactsAdapter2 mSylPhoneContactsAdapter;
    EditText mFavouritesEdittext;
    ArrayList<SYLPhoneContacts2> mPhoneContactpersonsdetails_databasearraylist;
    public boolean refreshflag = false;
    public boolean searchfilterflag = false;
    public String searchfiltervalue="";
    String userid = null;
    String name = null;
    String email = null;
    String mobilenumber;
    Bundle detailsbundle;
    String intentfrom;
    ImageView newButton;
    boolean loaderfinishflag=false;
    private boolean progressdialogflag=true;
    ProgressDialog msylProgressDialog;
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
        Bundle bundle_values = getArguments();
        detailsbundle=bundle_values.getBundle("detailsbundle");
        intentfrom = bundle_values.getString("intentfrom");
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
        msylProgressDialog = new ProgressDialog(getActivity());
        msylProgressDialog.setCancelable(false);
        msylProgressDialog.setCanceledOnTouchOutside(false);
        msylProgressDialog.setMessage("Please wait...");
        checkPhoneLocaldatabaseandstartLoader();

        setswipeviewProperties();

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
                ArrayList<SYLPhoneContacts2> mContactpersonsdetails_databasearraylist2 = datamanager
                        .getphoneContactswithvalue2(searchfiltervalue);
             if( mContactpersonsdetails_databasearraylist2.size()>0)
                {
                    mPhoneContactpersonsdetails_databasearraylist.clear();
                    mPhoneContactpersonsdetails_databasearraylist
                            .addAll(mContactpersonsdetails_databasearraylist2);
                    mSylPhoneContactsAdapter.notifyDataSetChanged();
                }
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
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

String usermobilenumber= SYLSaveValues.getSYLMobileNumber(getActivity());
                        String useremail=SYLSaveValues.getSYLEmailAddress(getActivity());
                        TextView txtview_firstname = (TextView) view
                                .findViewById(R.id.txt_firstname);
                        name=txtview_firstname.getText().toString();
                        TextView txtview_contactid = (TextView) view
                                .findViewById(R.id.txt_userid);
                        userid=txtview_contactid.getText().toString();
                        String[] getEmailorMobile=getEmailorMobile(txtview_contactid.getText().toString());
                        String phonecontact_mobilenumber=getEmailorMobile[0].toString();
                        String phonecontact_email=getEmailorMobile[1].toString();
                        email=phonecontact_email;
                        mobilenumber=phonecontact_mobilenumber;
if(mobilenumber.equals("")&&email.equals(""))
{
    SYLUtil.buildAlertMessage(getActivity(),"Please add mobile number or email to this contact and try again");
    return;
}
if(!mobilenumber.equals(""))
{
   if(mobilenumber.equals(usermobilenumber))
   {
       SYLUtil.buildAlertMessage(getActivity(),"You can't schedule an appointment with this user");
       return;
   }

}
 if(!email.equals(""))
 {
if(email.equals(useremail))
{
    SYLUtil.buildAlertMessage(getActivity(),"You can't schedule an appointment with this user");
    return;
}
 }

 if(!mobilenumber.equals("") || !email.equals("")   )
 {

     SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
             getActivity().getBaseContext());
     if (searchfilterflag) {

         datamanager
                 .updatePositionmarkerphonecontactsFalse2();
         datamanager
                 .updatePositionmarkerphoneContacts2(userid);
         mPhoneContactpersonsdetails_databasearraylist
                 .clear();
         ArrayList<SYLPhoneContacts2> mfacebookContactpersonsdetails_databasearraylist1 = datamanager
                 .getphoneContactswithvalue2(searchfiltervalue);
         mPhoneContactpersonsdetails_databasearraylist
                 .addAll(mfacebookContactpersonsdetails_databasearraylist1);
         mSylPhoneContactsAdapter.notifyDataSetChanged();

     }

     else{
         datamanager
                 .updatePositionmarkerphonecontactsFalse2();
         datamanager
                 .updatePositionmarkerphoneContacts2(userid);
         mPhoneContactpersonsdetails_databasearraylist
                 .clear();
         ArrayList<SYLPhoneContacts2> mfacebookContactpersonsdetails_databasearraylist1 = datamanager
                 .getphoneContactswithvalue2(searchfiltervalue);
         mPhoneContactpersonsdetails_databasearraylist
                 .addAll(mfacebookContactpersonsdetails_databasearraylist1);
         mSylPhoneContactsAdapter.notifyDataSetChanged();
     }
     if (intentfrom.equals("newappointmentimportcontact")) {
         Log.e("equest new appointment",
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

                    }
                });










    }
  private void  checkPhoneLocaldatabaseandstartLoader()
  {

      new AsyncTask<Void, Void, Boolean>() {
          protected void onPreExecute() {
              // Pre Code
              msylProgressDialog = new ProgressDialog(getActivity());
              msylProgressDialog.setCancelable(false);
              msylProgressDialog.setCanceledOnTouchOutside(false);
              msylProgressDialog.setMessage("Please wait...");
              msylProgressDialog.show();
          }

          protected Boolean doInBackground(Void... unused) {
             Boolean flag=false;

              if(checkphoneContactsLocaldata())
              {
                  flag=true;
                  SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
                          getActivity().getBaseContext());
                  datamanager.updatePositionmarkerphonecontactsFalse2();

                  mPhoneContactpersonsdetails_databasearraylist = datamanager
                          .getPhoneContacts2(getActivity().getBaseContext());


                 // loadPhoneContactsfromlocaldata();
              }
              else{
                  //  if(progressdialogflag){

flag=false;

              }
return flag;
          }

          protected void onPostExecute(Boolean flag) {

if(flag)
{
    if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
        msylProgressDialog.dismiss();
    }
    mSylPhoneContactsAdapter = new SYLPhoneContactsAdapter2(getActivity(),
            R.layout.sylcontacts_eachrow,
            mPhoneContactpersonsdetails_databasearraylist, getActivity());

    mPhonecontactsListview.setAdapter(mSylPhoneContactsAdapter);
}
              else {
    getActivity().getSupportLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null, SYLPhoneContactsFragment2.this);
}
          }
      }.execute();




/*
    if(checkphoneContactsLocaldata())
    {
        loadPhoneContactsfromlocaldata();
    }
     else{
      //  if(progressdialogflag){


        getActivity().getSupportLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null, SYLPhoneContactsFragment2.this);
    }
*/

  }
    private boolean checkphoneContactsLocaldata() {
        SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
                getActivity().getBaseContext());

        mPhoneContactpersonsdetails_databasearraylist = datamanager
                .getPhoneContacts2(getActivity().getBaseContext());
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
datamanager.updatePositionmarkerphonecontactsFalse2();

        mPhoneContactpersonsdetails_databasearraylist = datamanager
                .getPhoneContacts2(getActivity().getBaseContext());

        mSylPhoneContactsAdapter = new SYLPhoneContactsAdapter2(getActivity(),
                R.layout.sylcontacts_eachrow,
                mPhoneContactpersonsdetails_databasearraylist, getActivity());

        mPhonecontactsListview.setAdapter(mSylPhoneContactsAdapter);



    }








    @Override
    public void onRefresh() {
        loaderfinishflag=false;
        msylProgressDialog = new ProgressDialog(getActivity());
        msylProgressDialog.setCancelable(false);
        msylProgressDialog.setCanceledOnTouchOutside(false);
        msylProgressDialog.setMessage("Please wait...");
        msylProgressDialog.show();
        getActivity().getSupportLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null, SYLPhoneContactsFragment2.this);
     /*   swipeView.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeView.setRefreshing(true);
                handler.sendEmptyMessage(0);
            }
        }, 1000); */
    }
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mFavouritesEdittext.setText("");

            getActivity().getSupportLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null, SYLPhoneContactsFragment2.this);
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri contentUri;
        contentUri = ContactsQuery.CONTENT_URI;
        return new CursorLoader(getActivity(),
                contentUri,
                ContactsQuery.PROJECTION,
                ContactsQuery.SELECTION,
                null,
                ContactsQuery.SORT_ORDER);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(!loaderfinishflag) {
            new AsyncTask<Cursor, Void, Boolean>() {
                protected void onPreExecute() {
                    // Pre Code
                    loaderfinishflag = true;
                }

                protected Boolean doInBackground(Cursor... unused) {
                    // Background Code
                    Cursor cursor = unused[0];
                    Boolean flag = false;
                    try {

                        int j = 10;
                        int u = j + 9;
                        ArrayList<SYLPhoneContacts2> mphonecontacts = new ArrayList<SYLPhoneContacts2>();

                        while (cursor.moveToNext()) {
                            String contactid = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            SYLPhoneContacts2 phonecontacts = new SYLPhoneContacts2();
                            Log.e("name", name);
                            if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)) != null) {
                                String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                                if (photoUri != null || !photoUri.equals("")) {
                    /*    Uri uri = Uri.parse(photoUri);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getActivity().getContentResolver(),
                                        uri);
                        phonecontacts.setBitmap(bitmap); */
                                    phonecontacts.setProfileimageurl(photoUri);
                                } else {
                                    phonecontacts.setProfileimageurl("");
                                }
                            } else {
                                phonecontacts.setProfileimageurl("");
                                Log.e("Error", "error-e");
                            }
                            phonecontacts.setName(name);
                            phonecontacts.setContactid(contactid);

                            Log.e("name", name);

                            mphonecontacts.add(phonecontacts);
                        }
                        if (mphonecontacts.size() > 0) {
                            flag = true;
                            SYLContactPersonsdatamanager m = new SYLContactPersonsdatamanager(getActivity().getBaseContext());
                            m.insertPhoneContacts2(mphonecontacts);

                            mPhoneContactpersonsdetails_databasearraylist = m.getPhoneContacts2(getActivity().getBaseContext());


                        }


                    } catch (Exception e) {
                        flag = false;
                    }


                    return flag;
                }

                protected void onPostExecute(Boolean flag) {
                    // Post Code
                    //   Toast.makeText(getActivity(), "finished", Toast.LENGTH_LONG).show();
                    try {
                        if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
                            msylProgressDialog.dismiss();
                        }
                        mSylPhoneContactsAdapter = new SYLPhoneContactsAdapter2(getActivity(),
                                R.layout.sylphonecontacts_eachrow,
                                mPhoneContactpersonsdetails_databasearraylist, getActivity());

                        mPhonecontactsListview.setAdapter(mSylPhoneContactsAdapter);
                        swipeView.setRefreshing(false);
                    } catch (Exception e) {

                    }
                }
            }.execute(cursor);

        }







//        try {
//            Toast.makeText(getActivity(), "finished", Toast.LENGTH_LONG).show();
//            int j = 10;
//            int u = j + 9;
//            ArrayList<SYLPhoneContacts2> mphonecontacts = new ArrayList<SYLPhoneContacts2>();
//
//            while (cursor.moveToNext()) {
//                String contactid = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                SYLPhoneContacts2 phonecontacts = new SYLPhoneContacts2();
//                Log.e("name", name);
//                if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)) != null) {
//                    String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
//                    if (photoUri != null || !photoUri.equals("")) {
//                    /*    Uri uri = Uri.parse(photoUri);
//                        Bitmap bitmap = MediaStore.Images.Media
//                                .getBitmap(getActivity().getContentResolver(),
//                                        uri);
//                        phonecontacts.setBitmap(bitmap); */
//                        phonecontacts.setProfileimageurl(photoUri);
//                    } else {
//                        phonecontacts.setProfileimageurl("");
//                    }
//                } else {
//                    phonecontacts.setProfileimageurl("");
//                    Log.e("Error", "error-e");
//                }
//                phonecontacts.setName(name);
//                phonecontacts.setContactid(contactid);
//
//                Log.e("name", name);
//
//                mphonecontacts.add(phonecontacts);
//            }
//            if (mphonecontacts.size() > 0) {
//                SYLContactPersonsdatamanager m = new SYLContactPersonsdatamanager(getActivity().getBaseContext());
//                m.insertPhoneContacts2(mphonecontacts);
//
//                mPhoneContactpersonsdetails_databasearraylist = m.getPhoneContacts2(getActivity().getBaseContext());
//                if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
//                    msylProgressDialog.dismiss();
//                }
//                mSylPhoneContactsAdapter = new SYLPhoneContactsAdapter2(getActivity(),
//                        R.layout.sylphonecontacts_eachrow,
//                        mPhoneContactpersonsdetails_databasearraylist, getActivity());
//
//                mPhonecontactsListview.setAdapter(mSylPhoneContactsAdapter);
//
//            }
//            else{
//
//                if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
//                    msylProgressDialog.dismiss();
//                }
//            }
//        }
//        catch (Exception e)
//        {
//Log.e("exception message",e.getMessage());
//        }


    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.e("phone contactsfragment2", "onstop");
        //mFavouritesEdittext.setText("");
        //mFavouritesEdittext.setHint("Search Google Contacts");
    }

    private String[] getEmailorMobile(String contactid) {
        String[] emailorMobile = new String[2];
        String phoneNumber;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;
        Cursor phoneCursor =getActivity(). getContentResolver().query(
                PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
                new String[]{contactid}, null);
        if (phoneCursor.getCount() > 0) {

            while (phoneCursor.moveToNext()) {
                if(phoneCursor.getString(phoneCursor
                        .getColumnIndex(NUMBER))!=null)
                {
                    phoneNumber = phoneCursor.getString(phoneCursor
                            .getColumnIndex(NUMBER));
                    emailorMobile[0] = phoneNumber.replaceAll("\\s+","");
                }
                else{
                    emailorMobile[0] = "";
                }

            }
        }
        else{
            emailorMobile[0] = "";
        }
        Cursor emailCursor =getActivity(). getContentResolver().query(
                EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?",
                new String[]{contactid}, null);
        if(emailCursor.getCount()>0)
        {
            while(emailCursor.moveToNext())
            {

                if( emailCursor.getString(emailCursor
                        .getColumnIndex(DATA))!=null)
                {
                    emailorMobile[1] = emailCursor.getString(emailCursor
                            .getColumnIndex(DATA));
                }
                else{
                    emailorMobile[1] = "";
                }


            }
        }
        else{
            emailorMobile[1] = "";
        }
        return emailorMobile;


    }















}