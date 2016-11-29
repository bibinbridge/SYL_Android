package com.webcamconsult.syl.activities;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.AudioEffect.OnEnableStatusChangeListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.Session;
import com.opentok.android.Session.SessionListener;
import com.opentok.android.Stream;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLFetchSessionStatusListener;
import com.webcamconsult.syl.model.SYLFetchSessionAppointmentResponseModel;
import com.webcamconsult.syl.opentok.UIActivity;
import com.webcamconsult.syl.opentok.send.UIActivity2;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLFetchSessionStatusViewManager;

public class SYLOpentokcallSenderActivity extends Activity implements SYLFetchSessionStatusListener, Session.SessionListener, Session.ConnectionListener {
    String sessionid, token, apikey, appointmentid, profileimage_url;
    ImageView mUserImageView;
    private Session mSession;
    TextView mNameTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sylconferencecallsend_layout);
        mUserImageView = (ImageView) findViewById(R.id.row_icon);
        mNameTextView = (TextView) findViewById(R.id.txt_name);
        sessionid = getIntent().getExtras().getString("sessionid");
        token = getIntent().getExtras().getString("token");
        apikey = getIntent().getExtras().getString("apikey");
        appointmentid = getIntent().getExtras().getString("appointmentid");
        profileimage_url = getIntent().getExtras().getString("profileimage_url");
        mNameTextView.setText(getIntent().getExtras().getString("name"));
        ImageLoader imageLoader = new ImageLoader(SYLOpentokcallSenderActivity.this);
        imageLoader.DisplayImage(profileimage_url, mUserImageView);
        final CounterClass timer = new CounterClass(30000, 1000);
//timer.start();
        sessionConnect();
    }


    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Log.e("timer finished", "timer finished");
            fetchSessionStatusForAppointment();
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);


            Log.e("time", hms);
        }
    }

    private void fetchSessionStatusForAppointment() {
        String accesstoken = SYLSaveValues.getSYLaccessToken(SYLOpentokcallSenderActivity.this);
        String timezone = SYLUtil
                .getTimeGMTZone(SYLOpentokcallSenderActivity.this);
        SYLFetchSessionStatusViewManager mViewmanager = new SYLFetchSessionStatusViewManager();
        mViewmanager.msylFetchSessionStatusListener = SYLOpentokcallSenderActivity.this;
        mViewmanager.fetchSessionStatus(appointmentid, accesstoken, timezone);
    }

    public void navigatetoOpenTokchatscreen() {
        Intent resultIntent = new Intent(SYLOpentokcallSenderActivity.this, UIActivity2.class);
        resultIntent.putExtra("sessionid", getIntent().getExtras().getString("sessionid"));
        resultIntent.putExtra("token", getIntent().getExtras().getString("token"));
        resultIntent.putExtra("apikey", getIntent().getExtras().getString("apikey"));


        startActivity(resultIntent);
        finish();
    }


    @Override
    public void finishFetchSessionAppointmentStatus(
            SYLFetchSessionAppointmentResponseModel mSylFetchsessionappointmentresponsemodel) {
        // TODO Auto-generated method stub
        if (mSylFetchsessionappointmentresponsemodel != null) {
            if (mSylFetchsessionappointmentresponsemodel.isSuccess()) {
                navigatetoOpenTokchatscreen();
            } else {
                SYLUtil.buildAlertMessage(SYLOpentokcallSenderActivity.this, mSylFetchsessionappointmentresponsemodel.getError().getErrorTitle());
            }
        } else {
            SYLUtil.buildAlertMessage(SYLOpentokcallSenderActivity.this, "Opentok error happened");
        }
    }


    public void endSession(View v)

    {

    }

    private void sessionConnect() {
 /*
     if (mSession == null) {
         mSession = new Session(this, OpenTokConfig.API_KEY,
                 OpenTokConfig.SESSION_ID);
         mSession.setSessionListener(this);
         mSession.setArchiveListener(this);
         mSession.setStreamPropertiesListener(this);
         mSession.connect(OpenTokConfig.TOKEN);
     }
  */


        if (mSession == null) {
            mSession = new Session(this, apikey,
                    sessionid);
            mSession.setSessionListener(this);

            mSession.setConnectionListener(this);
            mSession.connect(token);

        }


    }


    @Override
    public void onConnected(Session session) {
        // TODO Auto-generated method stub
        Log.e("LOGTAG", "Connected to the session.");
        if (mSession != null) {
            mSession.disconnect();

        }
        Log.e("opentok disconnect", "call change conference");

    }

    @Override
    public void onDisconnected(Session arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(Session arg0, OpentokError arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStreamDropped(Session arg0, Stream arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStreamReceived(Session arg0, Stream arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionCreated(Session arg0, Connection arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionDestroyed(Session arg0, Connection arg1) {
        // TODO Auto-generated method stub

    }

}
