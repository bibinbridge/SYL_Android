package com.webcamconsult.syl.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.utilities.SYLUtil;

/**
 * Created by Sandeep on 8/18/2015.
 */
public class SYLFinishedAppointmentDetailActivity extends Activity {
    BroadcastReceiver br;
    EditText edittext_mobNo, edittext_skypeId, edittext_priorityone,mHangoutEditText,mFacetimeEdittext;
    String name;
    TextView textview_name, textview_appointmenttopic,
            textview_appointmentdetailsdescription;
    EditText  textview_prioritydate;
    public ImageLoader imageLoader;
    String profile_image;
    ImageView imageview_clientImage;
    String date1;
    String hangoutid,facetimeid;
    String priority1;
    String topic;
    String mobile;
    String skypeid;
    String description;
    TextView mtxtview_appointmentheading;
    ImageView img_backarrow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.finishedappointmentslayout);
        mHangoutEditText=(EditText)findViewById(R.id.etxt_googlehangout);
        mFacetimeEdittext=(EditText)findViewById(R.id.etxt_facetimeid);
        edittext_mobNo = (EditText) findViewById(R.id.etmobNo);
        edittext_skypeId = (EditText) findViewById(R.id.etskypeId);
        edittext_priorityone = (EditText) findViewById(R.id.etpriorityone);
        textview_name = (TextView) findViewById(R.id.tvname);
        imageview_clientImage = (ImageView) findViewById(R.id.ImClientImage);
        textview_appointmenttopic = (TextView) findViewById(R.id.tvAppointmentTopic);
        mtxtview_appointmentheading = (TextView) findViewById(R.id.tvAppointmentHeading);
        textview_appointmentdetailsdescription = (TextView) findViewById(R.id.tvAppointmentDetailsdescription);
        textview_prioritydate = (EditText) findViewById(R.id.tvprioritydate);
        img_backarrow = (ImageView) findViewById(R.id.img_backarrow);
getBundleValues();
        setValues();
    }
   private void  getBundleValues()
   {

       Bundle extras = getIntent().getExtras();
       profile_image = extras.getString("profile_image");
       name = extras.getString("name");
       date1 = extras.getString("date1");
       priority1 = extras.getString("priority1");
       topic = extras.getString("topic");
       description = extras.getString("description");
       hangoutid=extras.getString("hangoutid");
       mobile = extras.getString("mobile");
       skypeid = extras.getString("skypeid");
       facetimeid=extras.getString("facetimeid");
   }
private void setValues()
{

    if(name.length()>16)
    {
        name= name.substring(0, Math.min(name.length(), 12))+"...";
    }
    textview_name.setText(name);
    imageLoader = new ImageLoader(SYLFinishedAppointmentDetailActivity.this);
    imageLoader.DisplayImage(profile_image, imageview_clientImage);
    textview_name.setText(name);
    mtxtview_appointmentheading.setText(topic);
    textview_appointmentdetailsdescription.setText(Html.fromHtml("<html><strong>Description:</strong>" + description + "</html>"));
    edittext_mobNo.setText(mobile);
    edittext_skypeId.setText(skypeid);
    edittext_priorityone.setText("priority 1 is " + " " + priority1);
    setImageforPriorityone(priority1);
    textview_prioritydate.setText("preferred date and time " + "\n" + date1
            + "");

    if(!hangoutid.equals(""))
    {
        mHangoutEditText.setText(hangoutid);
    }
    else {
        mHangoutEditText.setText("not available");
    }
    if(skypeid.equals("not available"))
    {
        if(SYLUtil.isTablet(SYLFinishedAppointmentDetailActivity.this)) {
            edittext_skypeId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_skypeicon, 0,0, 0);
        }
        else {
            edittext_skypeId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.skypeicon2, 0, 0, 0);
        }
    }
    if(mobile.equals("not available"))
    {
        if(SYLUtil.isTablet(SYLFinishedAppointmentDetailActivity.this)) {
            edittext_mobNo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tablet_mobileicon, 0,0, 0);
        }
        else {
            edittext_mobNo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobileicon2, 0, 0, 0);
        }
    }



    mFacetimeEdittext.setText(facetimeid);
    img_backarrow.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            navigatetoAppointmentsListingScreen();
        }
    });
    edittext_mobNo.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Log.e("clicked mobile number", "mobile number");
            calltoMobilenumber(edittext_mobNo.getText().toString());
        }
    });
    edittext_skypeId.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(!skypeid.equals("not available"))
            callviaSkype(edittext_skypeId.getText().toString());
        }
    });
}
    private void callviaSkype(String skypeid){

        if(	SYLUtil.appInstalledOrNot("com.skype.raider", SYLFinishedAppointmentDetailActivity.this))
        {
            Intent skypeVideo = new Intent("android.intent.action.VIEW");
            skypeVideo.setData(Uri.parse("skype:" + skypeid + "?call&video=true"));
            startActivity(skypeVideo);
        }
        else {
            SYLUtil.buildAlertMessage(SYLFinishedAppointmentDetailActivity.this, "Please install Skype to enable this functionality");
        }
    }
    private void calltoMobilenumber(String mobilenumber)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + mobilenumber));
        startActivity(intent);
    }
    private void navigatetoAppointmentsListingScreen() {

        if(getIntent().getStringExtra("intentfrom").equals("futureappointment")) {
            Intent intent = new Intent(SYLFinishedAppointmentDetailActivity.this,
                    SYLFragmentChangeActivity.class);
            intent.putExtra("fragmentvalue", "newappointment");
            intent.putExtra("needrefresh", "false");
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(SYLFinishedAppointmentDetailActivity.this,
                    SYLHistoryAppointmentsActivity.class);

            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

 navigatetoAppointmentsListingScreen();
    }
    private void	setImageforPriorityone(String priority)
    {
        int imgResource;
        if(SYLUtil.isTablet(SYLFinishedAppointmentDetailActivity.this))
        {
            imgResource=R.drawable.tablet_chooseicon;
        }
        else{
            imgResource=R.drawable.chooseicon;
        }



        switch (priority) {
            case "Skype":
                if(SYLUtil.isTablet(SYLFinishedAppointmentDetailActivity.this))
                {
                    imgResource =R.drawable.tablet_skypeicon;
                }
                else{
                    imgResource =R.drawable.skypeicon2;
                }

                break;
            case "Mobile":
                if(SYLUtil.isTablet(SYLFinishedAppointmentDetailActivity.this)) {
                    imgResource=	R.drawable.tablet_mobileicon;
                }
                else{
                    imgResource=	R.drawable.mobileicon2;
                }

                break;
            case "SYL Videocall(in beta)":
                if(SYLUtil.isTablet(SYLFinishedAppointmentDetailActivity.this))
                {
                    imgResource=	R.drawable.tablet_opentokicon;
                }
                else{
                    imgResource=	R.drawable.opentok_icon2;
                }

                break;
            case "Hangout":
                if(SYLUtil.isTablet(SYLFinishedAppointmentDetailActivity.this))
                {
                    imgResource=R.drawable.tablet_hangouticon;
                }
                else{
                    imgResource=R.drawable.googlehangouticon;
                }

                break;
            case "Facetime":
                if(SYLUtil.isTablet(SYLFinishedAppointmentDetailActivity.this))
                {
                    imgResource=R.drawable.tablet_facetimeicon;
                }
                else{
                    imgResource=R.drawable.facetime_icon2;
                }

                break;
            default:
                imgResource=R.drawable.chooseicon;
                break;
        }
        edittext_priorityone.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
    }

}
