package com.webcamconsult.syl.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLCaneledRequestDetailedActivity extends Activity {
    public ImageLoader imageLoader;
    Context mContext;
    ImageView mUserImageView;
    String profile_image;
    ImageView img_backarrow;
    TextView mtxtview_topic, mtxtview_description;
    EditText metxtetmobNo, metxtskypeid;
    EditText etxtprefdateone, etxtprefdate2;
    TextView mtxtview_prefdate3;
    EditText mEtxtPriorityone, mEtxtPrioritytwo;
    TextView mtxtviewpriority3, mNameTextView;
    EditText mHangoutEditText,mFacetimeEdittext,edittext_chooseContacts,edittext_choosedate;
    String hangoutid, name;
    BroadcastReceiver br;

    String appointmentid,topic,description,priority1,priority2,priority3,date1,date2,date3,mobile,skypeid,facetimeid,participantid;
    Boolean choosecontactcclicked = false;
    Boolean choosedatecclicked = false;
    RelativeLayout relativelay_contactsdropdown, relativelay_datedown;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                finish();
            }
        };
        registerReceiver(
                br, intentFilter);


        setContentView(R.layout.cancel_requestdetaillayout);
        mtxtview_topic = (TextView) findViewById(R.id.tvAppointmentHeading);
        mtxtview_description = (TextView) findViewById(R.id.tvAppointmentDetailsdescription);
        mHangoutEditText = (EditText) findViewById(R.id.etxt_googlehangout);
        mFacetimeEdittext = (EditText) findViewById(R.id.etxt_facetimeid);
        metxtetmobNo = (EditText) findViewById(R.id.etmobNo);
        metxtskypeid = (EditText) findViewById(R.id.etskypeId);
        mEtxtPriorityone = (EditText) findViewById(R.id.etxt_priority1);
        mEtxtPrioritytwo = (EditText) findViewById(R.id.etxt_priority2);
        mtxtviewpriority3 = (TextView) findViewById(R.id.txtview_priority3);
        etxtprefdateone = (EditText) findViewById(R.id.etprefDateandtimeone);
        etxtprefdate2 = (EditText) findViewById(R.id.etprefDateandtimetwo);
        mtxtview_prefdate3 = (TextView) findViewById(R.id.tvprefDateandtimethree);
        relativelay_contactsdropdown = (RelativeLayout) findViewById(R.id.RlContactsDropdown);
        edittext_chooseContacts = (EditText) findViewById(R.id.etchooseContacts);
        mNameTextView = (TextView) findViewById(R.id.tvname);
        edittext_choosedate = (EditText) findViewById(R.id.etchoosedate);
        Bundle extras = getIntent().getExtras();
        appointmentid=extras.getString("appointmentid");
        participantid=extras.getString("participantid");
        topic=extras.getString("topic");
        description=extras.getString("description");
        priority1=extras.getString("priority1");
        priority2=extras.getString("priority2");
        priority3=extras.getString("priority3");
     date1=   extras.getString("date1");
      date2=  extras.getString("date2");
        date3= extras.getString("date3");
        mobile=extras.getString("mobile");
        skypeid=extras.getString("skypeid");
        facetimeid=extras.getString("facetimeid");
        profile_image = extras.getString("profile_image");
        name = extras.getString("name");

        if (name.length() > 16) {
            name = name.substring(0, Math.min(name.length(), 12)) + "...";
        }
        mNameTextView.setText(name);
        mtxtview_topic.setText(extras.getString("topic"));
        metxtetmobNo.setText(extras.getString("mobile"));
        metxtskypeid.setText(extras.getString("skypeid"));
        mFacetimeEdittext.setText(extras.getString("facetimeid"));
        relativelay_datedown = (RelativeLayout) findViewById(R.id.RlDatedown);
        hangoutid = extras.getString("hangoutid");

            mHangoutEditText.setText(hangoutid);

        String description = extras.getString("description");

        mtxtview_description.setText(Html.fromHtml("<html><strong>Description:</strong>" + description + "</html>"));
        mEtxtPriorityone.setText("Priority1 is " + extras.getString("priority1"));
        Drawable priorityoneimage = addPriorityImage(extras.getString("priority1"));
        Drawable prioritytwoimage = addPriorityImage(extras.getString("priority2"));
        Drawable prioritythreeimage = addPriorityImage(extras.getString("priority3"));
        mEtxtPriorityone
                .setCompoundDrawablesWithIntrinsicBounds(priorityoneimage, null,
                        null, null);
        mEtxtPrioritytwo.setText("Priority2 is " + extras.getString("priority2"));
        mEtxtPrioritytwo
                .setCompoundDrawablesWithIntrinsicBounds(prioritytwoimage, null,
                        null, null);
        String priority3 = extras.getString("priority3");
        if (priority3.equals("")) {
            priority3 = "not given";
        } else {
            priority3 = extras.getString("priority3");
        }
        mtxtviewpriority3.setText("Priority3 is " + priority3);
        mtxtviewpriority3
                .setCompoundDrawablesWithIntrinsicBounds(prioritythreeimage, null,
                        null, null);
        etxtprefdateone.setText("Preferred date and time " + extras.getString("date1"));
        etxtprefdate2.setText("Preferred date and time " + extras.getString("date2"));
        String date3 = extras.getString("date3");
        if (date3.equals("")) {
            date3 = "not given";
        } else {
            date3 = extras.getString("date3");
        }
        mtxtview_prefdate3.setText("Preferred date and time " + date3);

        mUserImageView = (ImageView) findViewById(R.id.ImClientImage);
        img_backarrow = (ImageView) findViewById(R.id.img_backarrow);

        mContext = this;
        imageLoader = new ImageLoader(mContext);
        imageLoader.DisplayImage(profile_image, mUserImageView);
        img_backarrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                navigatetoAppointmentsListingScreen();
            }
        });
        metxtskypeid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                callviaSkype(metxtskypeid.getText().toString());
            }
        });
        metxtetmobNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                calltoMobilenumber(metxtetmobNo.getText().toString());
            }
        });
        if(skypeid.equals("not available"))
        {
            if(SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this)) {
                metxtskypeid.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_skypeicon, 0, 0, 0);
            }
            else {
                metxtskypeid.setCompoundDrawablesWithIntrinsicBounds(R.drawable.skypeicon2, 0, 0, 0);
            }
        }
        if(mobile.equals("not available"))
        {
            if(SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this)) {
                metxtetmobNo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_mobileicon, 0, 0, 0);
            }
            else {
                metxtetmobNo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobileicon2, 0, 0, 0);
            }
        }
        edittext_chooseContacts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!choosecontactcclicked) {
                    relativelay_contactsdropdown.setVisibility(View.VISIBLE);
                    if(SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this))
                    {
                        edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.uparrow, 0);
                    }
                    else {

                        edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.uparrow, 0);
                    }

                    choosecontactcclicked = true;
                } else {
                    relativelay_contactsdropdown.setVisibility(View.GONE);
                    if(SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this))
                    {
                        edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.downarrow, 0);
                    }
                    else {
                        edittext_chooseContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.downarrow, 0);
                    }

                    choosecontactcclicked = false;
                }

            }
        });
        edittext_choosedate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!choosedatecclicked) {
                    relativelay_datedown.setVisibility(View.VISIBLE);
                    if(SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this))
                    {
                        edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.uparrow, 0);
                    }
                    else{
                        edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.uparrow, 0);
                    }

                    choosedatecclicked = true;
                } else {
                    relativelay_datedown.setVisibility(View.GONE);
                    if(SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this))
                    {
                        edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_chooseicon, 0, R.drawable.downarrow, 0);
                    }
                    else{
                        edittext_choosedate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chooseicon, 0, R.drawable.downarrow, 0);
                    }

                    choosedatecclicked = false;
                }

            }
        });






    }

    /*This method navigates the screen to Appointments Listing*/
    private void navigatetoAppointmentsListingScreen() {
        if(getIntent().getStringExtra("intentfrom").equals("futureappointment")) {
            Intent intent = new Intent(SYLCaneledRequestDetailedActivity.this,
                    SYLFragmentChangeActivity.class);
            intent.putExtra("fragmentvalue", "newappointment");
            intent.putExtra("needrefresh", "false");
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(SYLCaneledRequestDetailedActivity.this,
                    SYLHistoryAppointmentsActivity.class);
            startActivity(intent);
            finish();
        }


    }

    /*This method is calling when user clicks the hardware backbutton*/
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //	super.onBackPressed();
        navigatetoAppointmentsListingScreen();
    }

    private void calltoMobilenumber(String mobilenumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + mobilenumber));
        startActivity(intent);
    }

    private void callviaSkype(String skypeid) {

        if (SYLUtil.appInstalledOrNot("com.skype.raider", SYLCaneledRequestDetailedActivity.this)) {
            Intent skypeVideo = new Intent("android.intent.action.VIEW");
            skypeVideo.setData(Uri.parse("skype:" + skypeid + "?call&video=true"));
            startActivity(skypeVideo);
        } else {
            SYLUtil.buildAlertMessage(SYLCaneledRequestDetailedActivity.this, "Please install Skype to enable this functionality");
        }
    }
    public void navigatetoReschuduleScreen(View v) {
        Intent rescheduleintent = new Intent(
                SYLCaneledRequestDetailedActivity.this,
                SYLRescheduleappointmentActivity.class);
        rescheduleintent.putExtra("name", name);
        rescheduleintent.putExtra("appointmentid", appointmentid);
        rescheduleintent.putExtra("participantid",participantid );
        rescheduleintent.putExtra("topic", topic);
        rescheduleintent.putExtra("description", description);
        rescheduleintent.putExtra("priority1", priority1);

        if(priority2.equals("not available")){

            rescheduleintent.putExtra("priority2", "Choose priority 2");
        }
        else {
            rescheduleintent.putExtra("priority2", priority2);
        }
        if(priority3.equals("not available")){

            rescheduleintent.putExtra("priority3", "Choose priority 3");
        }
        else {
            rescheduleintent.putExtra("priority3", priority3);
        }
        rescheduleintent.putExtra("date1", date1);

        rescheduleintent.putExtra("date2", date2);
        rescheduleintent.putExtra("date3", date3);
        rescheduleintent.putExtra("hangoutid",hangoutid);
        rescheduleintent.putExtra("mobile",mobile);
        rescheduleintent.putExtra("skypeid",skypeid);
        rescheduleintent.putExtra("facetimeid",facetimeid);
        rescheduleintent.putExtra("profile_image",profile_image);

        rescheduleintent.putExtra("intentfrom", "cancelled");
        rescheduleintent.putExtra("intentname", getIntent().getStringExtra("intentfrom"));
        startActivity(rescheduleintent);
        finish();
    }
    private Drawable addPriorityImage(String priority) {

        Drawable dr = null;
        switch (priority) {
            case "Skype":
                if (SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this)) {
                    dr = getResources().getDrawable(R.drawable.tablet_skypeicon);
                } else {
                    dr = getResources().getDrawable(R.drawable.skypeicon2);
                }

                break;
            case "SYL Videocall(in beta)":
                if (SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this)) {
                    dr = getResources().getDrawable(R.drawable.tablet_opentokicon);
                } else {
                    dr = getResources().getDrawable(R.drawable.opentok_icon2);
                }

                break;
            case "Mobile":
                if (SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this)) {
                    dr = getResources().getDrawable(R.drawable.tablet_mobileicon);
                } else {
                    dr = getResources().getDrawable(R.drawable.mobileicon2);
                }

                break;
            case "Facetime":
                if (SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this)) {
                    dr = getResources().getDrawable(R.drawable.tablet_facetimeicon);
                } else {
                    dr = getResources().getDrawable(R.drawable.facetime_icon2);
                }

                break;
            case "Hangout":
                if (SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this)) {
                    dr = getResources().getDrawable(R.drawable.tablet_hangouticon);
                } else {
                    dr = getResources().getDrawable(R.drawable.googlehangouticon);
                }

                break;
            default:
                if (SYLUtil.isTablet(SYLCaneledRequestDetailedActivity.this)) {
                    dr = getResources().getDrawable(R.drawable.tablet_chooseicon);
                } else {
                    dr = getResources().getDrawable(R.drawable.chooseicon);
                }

                break;
        }
        return dr;

    }


}