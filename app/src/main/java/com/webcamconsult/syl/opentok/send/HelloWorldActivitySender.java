package com.webcamconsult.syl.opentok.send;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLConfirmedRequestAppointmentActivity;
import com.webcamconsult.syl.activities.SYLOpentokcallSenderActivity;
import com.webcamconsult.syl.activities.SYLSigninActivity;
import com.webcamconsult.syl.activities.SYLTodaysAppointmentDetailActivity;
import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLHistoryAppointmentsdatamanager;
import com.webcamconsult.syl.fragments.SYLAppointmentsFragment;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLChangeconferenceListener;
import com.webcamconsult.syl.interfaces.SYLFetchSessionStatusListener;
import com.webcamconsult.syl.model.SYLChangeConferenceResponeModel;
import com.webcamconsult.syl.model.SYLFetchAppointmentsDetails;
import com.webcamconsult.syl.model.SYLFetchSessionAppointmentResponseModel;
import com.webcamconsult.syl.opentok.ClearNotificationService;
import com.webcamconsult.syl.opentok.SYLOpentokAcceptRejectActivity;
import com.webcamconsult.syl.opentok.send.UIActivity2.CounterClass;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLChangeOpentokStatusViewmanager;
import com.webcamconsult.viewmanager.SYLFetchSessionStatusViewManager;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;


/**
 * This application demonstrates the basic workflow for getting started with the
 * OpenTok 2.0 Android SDK. For more information, see the README.md file in the
 * samples directory.
 */
public class HelloWorldActivitySender extends Activity implements
        Session.SessionListener, Publisher.PublisherListener,
        Subscriber.VideoListener,	SYLFetchSessionStatusListener ,Session.ConnectionListener, SYLChangeconferenceListener{

    private static final String LOGTAG = "demo-hello-world";
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ArrayList<Stream> mStreams;
    private Handler mHandler = new Handler();

    private RelativeLayout mPublisherViewContainer;
    private RelativeLayout mSubscriberViewContainer;
    private RelativeLayout testViewContainer;
    // Spinning wheel for loading subscriber view
    private ProgressBar mLoadingSub;

    private boolean resumeHasRun = false;

    private boolean mIsBound = false;
    private NotificationCompat.Builder mNotifyBuilder;
    private NotificationManager mNotificationManager;
    private ServiceConnection mConnection;
RelativeLayout mCallRelativeLayout;
TextView mNameTextView;
public  CounterClass timer ;
ImageView mUserImageView;
String sessionid, token, apikey, appointmentid, profileimage_url;
ProgressDialog msylProgressDialog;
    MediaPlayer mediaPlayer;
    Timer checkingcallstatusTimer;
    private int checkingcallstatuscounter=0;
private boolean medialpayerflag=true;
BroadcastReceiver callendBroadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOGTAG, "ONCREATE");
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.CALLEND");
        callendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                finish();
            }
        };
        registerReceiver(
                callendBroadcastReceiver, intentFilter);










        setContentView(com.webcamconsult.syl.R.layout.helloworld_senderlayout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mediaPlayer = new MediaPlayer();


    	mNameTextView=(TextView)findViewById(R.id.txt_name);
    	mUserImageView=(ImageView)findViewById(R.id.row_icon);
        mCallRelativeLayout=(RelativeLayout)findViewById(R.id.rel_call);
    	sessionid = getIntent().getExtras().getString("sessionid");
		token = getIntent().getExtras().getString("token");
		apikey = getIntent().getExtras().getString("apikey");
		appointmentid = getIntent().getExtras().getString("appointmentid");
		mNameTextView.setText(getIntent().getExtras().getString("name"));
		profileimage_url=getIntent().getExtras().getString("profileimage_url");
		ImageLoader	imageLoader = new ImageLoader(HelloWorldActivitySender.this);
		imageLoader.DisplayImage(profileimage_url, mUserImageView);
		msylProgressDialog = new ProgressDialog(HelloWorldActivitySender.this);
		msylProgressDialog.setMessage("Please wait...");
        msylProgressDialog.setCancelable(false);
        msylProgressDialog.setCanceledOnTouchOutside(false);
		msylProgressDialog.show();
        checkingcallstatusTimer=new Timer();


        
        
/*

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
*/
        mPublisherViewContainer = (RelativeLayout) findViewById(R.id.publisherview);
        mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberview);
      testViewContainer= (RelativeLayout) findViewById(R.id.testView);
        mCallRelativeLayout.setVisibility(View.VISIBLE);
        mPublisherViewContainer.setVisibility(View.GONE);
        mSubscriberViewContainer.setVisibility(View.GONE);
        testViewContainer.setVisibility(View.GONE);
        mLoadingSub = (ProgressBar) findViewById(R.id.loadingSpinner);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mStreams = new ArrayList<Stream>();

mPublisherViewContainer.setVisibility(View.GONE);
mSubscriberViewContainer.setVisibility(View.GONE);
        sessionConnect();
        timer = new CounterClass(25000, 1000);
        TimerTask k=new TimerTask() {
            @Override
            public void run() {
                checkingcallstatuscounter++;
                Log.e("check call status","check call status");
                fetchSessionStatusForAppointment();
            }
        };
        checkingcallstatusTimer.scheduleAtFixedRate(k, 3000, 3 * 1000);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSession != null) {
            mSession.onPause();

            if (mSubscriber != null) {
                mSubscriberViewContainer.removeView(mSubscriber.getView());
            }
        }

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(this.getTitle())
                .setContentText(getResources().getString(R.string.notification))
                .setSmallIcon(R.drawable.ic_launcher).setOngoing(true);

        Intent notificationIntent = new Intent(this, HelloWorldActivitySender.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        mNotifyBuilder.setContentIntent(intent);
        if (mConnection == null) {
            mConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className, IBinder binder) {
                    ((ClearNotificationService.ClearBinder) binder).service.startService(new Intent(HelloWorldActivitySender.this, ClearNotificationService.class));
                  //  NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                  //  mNotificationManager.notify(ClearNotificationService.NOTIFICATION_ID, mNotifyBuilder.build());
                }

                @Override
                public void onServiceDisconnected(ComponentName className) {
                    mConnection = null;
                }

            };
        }

        if (!mIsBound) {
            bindService(new Intent(HelloWorldActivitySender.this,
                            ClearNotificationService.class), mConnection,
                    Context.BIND_AUTO_CREATE);
            mIsBound = true;
            startService(notificationIntent);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }

        if (!resumeHasRun) {
            resumeHasRun = true;
            return;
        } else {
            if (mSession != null) {
                mSession.onResume();
            }
        }
      //  mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);

        reloadInterface();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }

        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        if (isFinishing()) {
           // mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
            if (mSession != null) {
                mSession.disconnect();
            }
        }
    }

    @Override
    public void onDestroy() {
       // mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }

        if (mSession != null) {
            mSession.disconnect();
        }

        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mSession != null) {
            mSession.disconnect();
        }
        Log.e("opentok disconnect", "call change conference");
        mediaPlayer.stop();
        timer.cancel();
        changeConferenceStatuscode("FINISHED");
        if(getIntent().getExtras().getString("intentfrom").equals("todaysappointment"))
        {
            navigatetoTodaysAppointmentDetail(appointmentid);
        }
        else {
            navigatetoConfirmedAppointmentDetailScreen(appointmentid);
        }
    }

    public void reloadInterface() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSubscriber != null) {
                    attachSubscriberView(mSubscriber);
                }
            }
        }, 500);
    }

    private void sessionConnect() {
        if (mSession == null) {
            mSession = new Session(HelloWorldActivitySender.this,
                    apikey,sessionid);
            mSession.setSessionListener(this);
            mSession.setConnectionListener(this);
            mSession.connect(token);
      
        }
    }

    @Override
    public void onConnected(Session session) {

		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
        if(medialpayerflag) {
            playReceiverRingtone();
        }
        Log.i(LOGTAG, "Connected to the session.");
        if (mPublisher == null) {
            mPublisher = new Publisher(HelloWorldActivitySender.this, "publisher");
            mPublisher.setPublisherListener(this);
            attachPublisherView(mPublisher);
            attachTestView();
            mSession.publish(mPublisher);
           // timer.start();
        }
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOGTAG, "Disconnected from the session.");
        if (mPublisher != null) {
            mPublisherViewContainer.removeView(mPublisher.getView());
        }

        if (mSubscriber != null) {
            mSubscriberViewContainer.removeView(mSubscriber.getView());
        }

        mPublisher = null;
        mSubscriber = null;
        mStreams.clear();
        mSession = null;
    }

    private void subscribeToStream(Stream stream) {
        mSubscriber = new Subscriber(HelloWorldActivitySender.this, stream);
        mSubscriber.setVideoListener(this);
        mSession.subscribe(mSubscriber);

        if (mSubscriber.getSubscribeToVideo()) {
            // start loading spinning
           mLoadingSub.setVisibility(View.VISIBLE);
        }
    }

    private void unsubscribeFromStream(Stream stream) {
        mStreams.remove(stream);
        if (mSubscriber.getStream().equals(stream)) {
            mSubscriberViewContainer.removeView(mSubscriber.getView());
            mSubscriber = null;
            if (!mStreams.isEmpty()) {
                subscribeToStream(mStreams.get(0));
            }
        }
    }

    private void attachSubscriberView(Subscriber subscriber) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels, getResources()
                .getDisplayMetrics().heightPixels);
        mSubscriberViewContainer.removeView(mSubscriber.getView());
        mSubscriberViewContainer.addView(mSubscriber.getView(), layoutParams);
        subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
    }

    private void attachPublisherView(Publisher publisher) {
        mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                320, 240);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                RelativeLayout.TRUE);
        layoutParams.bottomMargin = dpToPx(8);
        layoutParams.rightMargin = dpToPx(8);
        mPublisherViewContainer.addView(mPublisher.getView(), layoutParams);
    }
    private void attachTestView()
    {
        Button btnTag = new Button(this);
        btnTag.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
        btnTag.setText("End call");
        btnTag.setTextSize(14);
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkingcallstatusTimer.cancel();
                if (mSession != null) {
                    mSession.disconnect();
                    changeConferenceStatuscode("FINISHED");
                }
                Log.e("opentok disconnect", "call change conference");
                timer.cancel();
                Log.e("helloworldactivity", getIntent().getExtras().getString("intentfrom"));


                if (getIntent().getExtras().getString("intentfrom").equals("todaysappointment")) {

                    navigatetoTodaysAppointmentDetail(appointmentid);
                } else {
                    navigatetoConfirmedAppointmentDetailScreen(appointmentid);
                }

            }
        });

     btnTag.setBackgroundResource(R.drawable.selector_rejectcall);
      //  btnTag.setBackground(getResources().getDrawable(R.drawable.selector_rejectcall));
        btnTag.setTextColor(getResources().getColor(R.color.white));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int buttonwidth= (int) Math.round(width*.24);
        int buttonheight=(int)Math.round(height*.07);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(buttonwidth,buttonheight);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                RelativeLayout.TRUE);
        layoutParams.bottomMargin = dpToPx(8);
        layoutParams.rightMargin = dpToPx(8);
        testViewContainer.addView(btnTag, layoutParams);
    }
    @Override
    public void onError(Session session, OpentokError exception) {
        Log.i(LOGTAG, "Session exception: " + exception.getMessage());
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        if (!HelloWorldopentokconfig.SUBSCRIBE_TO_SELF) {
            mStreams.add(stream);
            if (mSubscriber == null) {
                subscribeToStream(stream);
            }
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if (!HelloWorldopentokconfig.SUBSCRIBE_TO_SELF) {
            if (mSubscriber != null) {
                unsubscribeFromStream(stream);
            }
        }
    }

    @Override
    public void onStreamCreated(PublisherKit publisher, Stream stream) {
        if (HelloWorldopentokconfig.SUBSCRIBE_TO_SELF) {
            mStreams.add(stream);
            if (mSubscriber == null) {
                subscribeToStream(stream);
            }
        }
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisher, Stream stream) {
        if ((HelloWorldopentokconfig.SUBSCRIBE_TO_SELF && mSubscriber != null)) {
            unsubscribeFromStream(stream);
        }
    }

    @Override
    public void onError(PublisherKit publisher, OpentokError exception) {
        Log.i(LOGTAG, "Publisher exception: " + exception.getMessage());
    }

    @Override
    public void onVideoDataReceived(SubscriberKit subscriber) {
        Log.i(LOGTAG, "First frame received");
        Log.e("firstframe", "First frame received");
        // stop loading spinning
        mLoadingSub.setVisibility(View.GONE);
        attachSubscriberView(mSubscriber);
    }

    /**
     * Converts dp to real pixels, according to the screen density.
     *
     * @param dp A number of density-independent pixels.
     * @return The equivalent number of real pixels.
     */
    private int dpToPx(int dp) {
        double screenDensity = this.getResources().getDisplayMetrics().density;
        return (int) (screenDensity * (double) dp);
    }

    @Override
    public void onVideoDisabled(SubscriberKit subscriber, String reason) {
        Log.i(LOGTAG,
                "Video disabled:" + reason);
    }

    @Override
    public void onVideoEnabled(SubscriberKit subscriber, String reason) {
        Log.i(LOGTAG, "Video enabled:" + reason);
    }

    @Override
    public void onVideoDisableWarning(SubscriberKit subscriber) {
        Log.i(LOGTAG, "Video may be disabled soon due to network quality degradation. Add UI handling here.");
    }

    @Override
    public void onVideoDisableWarningLifted(SubscriberKit subscriber) {
        Log.i(LOGTAG, "Video may no longer be disabled as stream quality improved. Add UI handling here.");
    }
	public class CounterClass extends CountDownTimer {
		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			Log.e("timer finished", "timer finished");
            mediaPlayer.stop();
			fetchSessionStatusForAppointment();
		}

		@Override
		public void onTick(long millisUntilFinished) {

			long millis = millisUntilFinished;
			String hms = String.format(
					"%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(millis),
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis)));
			System.out.println(hms);

			Log.e("time-sender", hms);
		}
	}
	
	private void fetchSessionStatusForAppointment() {
		String accesstoken = SYLSaveValues.getSYLaccessToken(HelloWorldActivitySender.this);
		String timezone = SYLUtil.getTimeGMTZone(HelloWorldActivitySender.this);
		SYLFetchSessionStatusViewManager mViewmanager = new SYLFetchSessionStatusViewManager();
		mViewmanager.msylFetchSessionStatusListener = HelloWorldActivitySender.this;
		mViewmanager.fetchSessionStatus(appointmentid, accesstoken, timezone);
	}

	@Override
	public void finishFetchSessionAppointmentStatus(
			SYLFetchSessionAppointmentResponseModel mSylFetchsessionappointmentresponsemodel) {
		// TODO Auto-generated method stub
try {
    if (mSylFetchsessionappointmentresponsemodel != null) {
        if (mSylFetchsessionappointmentresponsemodel.isSuccess()) {
            if (mSylFetchsessionappointmentresponsemodel.getSessionStatusCode().equals("INCALL")) {
                if (checkingcallstatuscounter < 8) {
                    mCallRelativeLayout.setVisibility(View.GONE);


                    mPublisherViewContainer.setVisibility(View.VISIBLE);
                    mSubscriberViewContainer.setVisibility(View.VISIBLE);
                    mLoadingSub.setVisibility(View.VISIBLE);
                    // mSubscriberViewContainer.setBackground(getResources().getDrawable(R.drawable.appbackground));
                    testViewContainer.setVisibility(View.VISIBLE);

                    checkingcallstatusTimer.cancel();
                    medialpayerflag = false;
                    mediaPlayer.stop();
                }
            } else if (mSylFetchsessionappointmentresponsemodel.getSessionStatusCode().equals("REJECTED")) {
                if (checkingcallstatuscounter < 8) {
                    mediaPlayer.stop();
                    medialpayerflag = false;
                    showAlertMessage(HelloWorldActivitySender.this,
                            "SYL user rejected the call");
                    checkingcallstatusTimer.cancel();
                    mediaPlayer.stop();
                }
            } else if (mSylFetchsessionappointmentresponsemodel.getSessionStatusCode().equals("UNANSWERED")) {
                if (checkingcallstatuscounter > 8) {

                    showAlertMessage(HelloWorldActivitySender.this,
                            "SYL user unanswered the call");
                    checkingcallstatusTimer.cancel();
                    mediaPlayer.stop();
                }
            } else if (mSylFetchsessionappointmentresponsemodel.getSessionStatusCode().equals("ACTIVE")) {
                if (checkingcallstatuscounter > 8) {
                    Log.e("unanswered", "unanswered");
                    changeConferenceStatuscode("UNANSWERED");
                    checkingcallstatusTimer.cancel();
                    mediaPlayer.stop();
                    showAlertMessage(HelloWorldActivitySender.this,
                            "SYL user unanswered the call");
                }

            } else if (mSylFetchsessionappointmentresponsemodel.getSessionStatusCode().equals("FINISHED")) {
                checkingcallstatusTimer.cancel();
                try {
                    showAlertMessage(HelloWorldActivitySender.this,
                            "SYL user ended the call");
                } catch (Exception e) {
                }
            }
        } else {
            SYLUtil.buildAlertMessage(HelloWorldActivitySender.this,
                    mSylFetchsessionappointmentresponsemodel.getError()
                            .getErrorTitle());
        }
    } else {
        SYLUtil.buildAlertMessage(HelloWorldActivitySender.this,
                "Opentok error happened");
    }
}
catch (Exception e)
{
    e.printStackTrace();
}

	}
	
	
    public void onEndCall(View v) {
        checkingcallstatusTimer.cancel();
        if (mSession != null) {
            mSession.disconnect();
        }
Log.e("opentok disconnect", "call change conference");
        mediaPlayer.stop();
timer.cancel();
changeConferenceStatuscode("FINISHED");
        if(getIntent().getExtras().getString("intentfrom").equals("todaysappointment"))
        {
            navigatetoTodaysAppointmentDetail(appointmentid);
        }
        else {
            navigatetoConfirmedAppointmentDetailScreen(appointmentid);
        }

    }

	@Override
	public void onConnectionCreated(Session arg0, Connection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionDestroyed(Session arg0, Connection arg1) {
		// TODO Auto-generated method stub
		Log.e("Connection destroyed", "Connection Destroyed");
        mediaPlayer.stop();
        showAlertMessage(HelloWorldActivitySender.this, "SYL user ended the call");
	}
	private void changeConferenceStatuscode(String statuscode)
	{
		String appointmentid=getIntent().getExtras().getString("appointmentid");
		String timezone=	SYLUtil
				.getTimeGMTZone(HelloWorldActivitySender.this);
		String status=statuscode;
		SYLChangeOpentokStatusViewmanager mChangeOpentokstausviewmanager=new SYLChangeOpentokStatusViewmanager();
mChangeOpentokstausviewmanager.mConferenceChangeListener=HelloWorldActivitySender.this;
		mChangeOpentokstausviewmanager.changeOpentokStatus(status, timezone, this.appointmentid);
	}
	private void  showAlertMessage(Context context, String message) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog,
                                                    final int id) {

                                    dialog.cancel();
                                    if (getIntent().getExtras().getString("intentfrom").equals("todaysappointment")) {
                                        navigatetoTodaysAppointmentDetail(appointmentid);
                                    } else {
                                        navigatetoConfirmedAppointmentDetailScreen(appointmentid);
                                    }
                                }
                            });

            final AlertDialog alert = builder.create();
            alert.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

	}
	protected void navigatetoConfirmedAppointmentDetailScreen(String appointmentid) {
        SYLFetchAppointmentsDetails mfetchappointmentdetails=null;
try {
     mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);

    if(getIntent().getStringExtra("intentname").equals("futureappointment")) {
        mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);
    }
    else
    {
        mfetchappointmentdetails = getHistoryAppointmentusingID(appointmentid);
    }





    Intent intent = new Intent(
            HelloWorldActivitySender.this,
            SYLConfirmedRequestAppointmentActivity.class);
    String curr_userid = SYLSaveValues.getSYLUserID(HelloWorldActivitySender.this);
    String organizer_id = Integer.toString(mfetchappointmentdetails
            .getOrganizer().getUserId());
    String participnat_id = Integer.toString(mfetchappointmentdetails
            .getParticipant().getUserId());
    if (curr_userid.equals(organizer_id)) {
        intent.putExtra("name", mfetchappointmentdetails.getParticipant()
                .getName());
        int participantid = mfetchappointmentdetails.getParticipant()
                .getUserId();
        intent.putExtra("participantid", Integer.toString(participantid));
        intent.putExtra("profile_image", mfetchappointmentdetails
                .getParticipant().getProfileImage());

        if (mfetchappointmentdetails
                .getParticipant().getHangoutId().equals("")) {
            intent.putExtra("hangoutid", "not available");
        } else {
            intent.putExtra("hangoutid", mfetchappointmentdetails
                    .getParticipant().getHangoutId());
        }
        if (mfetchappointmentdetails.getParticipant().getSkypeId().equals("")) {
            intent.putExtra("skypeid", "not available");
        } else {
            intent.putExtra("skypeid", mfetchappointmentdetails.getParticipant().getSkypeId());
        }
        if (mfetchappointmentdetails.getParticipant().getFacetimeId().equals("")) {
            intent.putExtra("facetimeid", "not available");
        } else {
            intent.putExtra("facetimeid", mfetchappointmentdetails.getParticipant().getFacetimeId());
        }

    } else {
        intent.putExtra("name", mfetchappointmentdetails.getOrganizer()
                .getName());
        int participantid = mfetchappointmentdetails.getOrganizer()
                .getUserId();
        intent.putExtra("participantid", Integer.toString(participantid));
        intent.putExtra("profile_image", mfetchappointmentdetails
                .getOrganizer().getProfileImage());
        intent.putExtra("hangoutid", mfetchappointmentdetails
                .getOrganizer().getHangoutId());
        if (mfetchappointmentdetails
                .getOrganizer().getHangoutId().equals("")) {
            intent.putExtra("hangoutid", "not available");
        } else {
            intent.putExtra("hangoutid", mfetchappointmentdetails
                    .getOrganizer().getHangoutId());
        }
        if (mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("")) {
            intent.putExtra("facetimeid", "not available");
        } else {
            intent.putExtra("facetimeid", mfetchappointmentdetails.getOrganizer().getFacetimeId());
        }
        if (mfetchappointmentdetails.getOrganizer().getSkypeId().equals("")) {
            intent.putExtra("skypeid", "not available");
        } else {
            intent.putExtra("skypeid", mfetchappointmentdetails.getOrganizer().getSkypeId());
        }

    }

    intent.putExtra("topic", mfetchappointmentdetails.getTopic());
    intent.putExtra("description",
            mfetchappointmentdetails.getDescription());
    intent.putExtra("mobile", mfetchappointmentdetails.getParticipant()
            .getMobile());

    intent.putExtra("priority1",
            mfetchappointmentdetails.getAppointmentPriority1());
    intent.putExtra("priority2",
            mfetchappointmentdetails.getAppointmentPriority2());
    intent.putExtra("priority3",
            mfetchappointmentdetails.getAppointmentPriority3());
    intent.putExtra("appointmentid", appointmentid);
    String date1 = mfetchappointmentdetails.getAppointmentDate1();
    String time1 = mfetchappointmentdetails.getAppointmentTime1();
    String date2 = mfetchappointmentdetails.getAppointmentDate2();
    String time2 = mfetchappointmentdetails.getAppointmentTime2();
    String date3 = mfetchappointmentdetails.getAppointmentDate3();
    String time3 = mfetchappointmentdetails.getAppointmentTime3();
    intent.putExtra("date1", date1 + " " + time1);

    if (date2.equals("") || time2.equals("")) {
        intent.putExtra("date2", " ");
    } else {
        intent.putExtra("date2", date2 + " " + time2);
    }
    if (date3.equals("") || time3.equals("")) {
        intent.putExtra("date3", " ");
    } else {
        intent.putExtra("date3", date3 + " " + time3);
    }
    intent.putExtra("intentfrom",getIntent().getStringExtra("intentname"));
    startActivity(intent);
    finish();
}
catch (Exception e)
{

}

    }
    private void navigatetoTodaysAppointmentDetail(String appointmentid) {
        SYLFetchAppointmentsDetails mfetchappointmentdetails=null;
        try {
            if(getIntent().getStringExtra("intentname").equals("futureappointment")) {
                mfetchappointmentdetails = getAppointmentDetailsFromDB(appointmentid);
            }
            else
            {
                mfetchappointmentdetails = getHistoryAppointmentusingID(appointmentid);
            }

    Intent intent = new Intent(HelloWorldActivitySender.this,

            SYLTodaysAppointmentDetailActivity.class);
    String curr_userid = SYLSaveValues.getSYLUserID(HelloWorldActivitySender.this);
    String organizer_id = Integer.toString(mfetchappointmentdetails.getOrganizer().getUserId());
    String participnat_id = Integer.toString(mfetchappointmentdetails.getParticipant().getUserId());
    if (curr_userid.equals(organizer_id)) {
        intent.putExtra("name", mfetchappointmentdetails
                .getParticipant().getName());
        int participantid = mfetchappointmentdetails
                .getParticipant().getUserId();
        intent.putExtra("participantid",
                Integer.toString(participantid));
        intent.putExtra("profile_image",
                mfetchappointmentdetails.getParticipant()
                        .getProfileImage());

        if (mfetchappointmentdetails.getParticipant()
                .getFacetimeId().equals("")) {
            intent.putExtra("facetimeid",
                    "not available");
        } else {
            intent.putExtra("facetimeid",
                    mfetchappointmentdetails.getParticipant()
                            .getFacetimeId());
        }
        if (mfetchappointmentdetails.getParticipant()
                .getSkypeId().equals("")) {
            intent.putExtra("skypeid",
                    "not available");
        } else {
            intent.putExtra("skypeid",
                    mfetchappointmentdetails.getParticipant()
                            .getSkypeId());
        }

        intent.putExtra("mobile",
                mfetchappointmentdetails.getParticipant()
                        .getMobile());
        if (mfetchappointmentdetails.getParticipant()
                .getHangoutId().equals("")) {
            intent.putExtra("hangoutid",
                    "not available");
        } else {
            intent.putExtra("hangoutid",
                    mfetchappointmentdetails.getParticipant()
                            .getHangoutId());
        }

    } else {
        intent.putExtra("name", mfetchappointmentdetails
                .getOrganizer().getName());
        int participantid = mfetchappointmentdetails
                .getOrganizer().getUserId();
        intent.putExtra("participantid",
                Integer.toString(participantid));
        intent.putExtra("profile_image",
                mfetchappointmentdetails.getOrganizer().getProfileImage());


        if (mfetchappointmentdetails.getOrganizer()
                .getFacetimeId().equals("")) {
            intent.putExtra("facetimeid",
                    "not available");
        } else {
            intent.putExtra("facetimeid",
                    mfetchappointmentdetails.getOrganizer()
                            .getFacetimeId());
        }
        if (mfetchappointmentdetails.getOrganizer()
                .getSkypeId().equals("")) {
            intent.putExtra("skypeid",
                    "not available");
        } else {
            intent.putExtra("skypeid",
                    mfetchappointmentdetails.getOrganizer()
                            .getSkypeId());
        }

        intent.putExtra("mobile",
                mfetchappointmentdetails.getOrganizer()
                        .getMobile());

        if (mfetchappointmentdetails.getOrganizer()
                .getHangoutId().equals("")) {
            intent.putExtra("hangoutid",
                    "not available");
        } else {
            intent.putExtra("hangoutid",
                    mfetchappointmentdetails.getOrganizer()
                            .getHangoutId());
        }

    }

    intent.putExtra("topic",
            mfetchappointmentdetails.getTopic());
    intent.putExtra("description",
            mfetchappointmentdetails.getDescription());


    intent.putExtra("priority1",
            mfetchappointmentdetails.getAppointmentPriority1());
    intent.putExtra("priority2",
            mfetchappointmentdetails.getAppointmentPriority2());
    intent.putExtra("priority3",
            mfetchappointmentdetails.getAppointmentPriority3());
    intent.putExtra("appointmentid",
            appointmentid);

    String date1 = mfetchappointmentdetails
            .getAppointmentDate1();
    String time1 = mfetchappointmentdetails
            .getAppointmentTime1();
    String date2 = mfetchappointmentdetails
            .getAppointmentDate2();
    String time2 = mfetchappointmentdetails
            .getAppointmentTime2();
    String date3 = mfetchappointmentdetails
            .getAppointmentDate3();
    String time3 = mfetchappointmentdetails
            .getAppointmentTime3();
    intent.putExtra("date1", date1 + " " + time1);
    intent.putExtra("date2", date2 + " " + time2);
    intent.putExtra("date3", date3 + " " + time3);
intent.putExtra("intentfrom",getIntent().getStringExtra("intentname"));
    startActivity(intent);
    finish();
}
catch (Exception e)
{

}

    }
	private SYLFetchAppointmentsDetails getAppointmentDetailsFromDB(String appointmentid) {
		SYLFetchAppointmentsDetails mfetchappointmentdetailsobject = null;
		SYLAppointmentdetailsdatamanager datamanager = new SYLAppointmentdetailsdatamanager(
				HelloWorldActivitySender.this);
		mfetchappointmentdetailsobject = datamanager
				.getAllAppointmentFetchDetails("TodaysAppointments",appointmentid);
		return mfetchappointmentdetailsobject;
	}
    private SYLFetchAppointmentsDetails getHistoryAppointmentusingID(
            String appointmentid) {
        SYLFetchAppointmentsDetails mfetchappointmentdetailsobject = null;
        SYLHistoryAppointmentsdatamanager datamanager = new SYLHistoryAppointmentsdatamanager(
                HelloWorldActivitySender.this);
        mfetchappointmentdetailsobject = datamanager
                .getHistoryAppointmentFetchDetails("TodaysAppointments",
                        appointmentid);
        return mfetchappointmentdetailsobject;
    }
	@Override
	public void finishConferenceStstus(
			SYLChangeConferenceResponeModel msylChangeConferencemodel) {
		// TODO Auto-generated method stub
		Log.e("conference status finished","conference status finished");
	}
    private void playReceiverRingtone()
    {
        try {



            String packagename=getPackageName();

            Uri notificationsoundpath = Uri.parse("android.resource://" + packagename + "/" + R.raw.sylopentok_callertone_high);

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setDataSource(HelloWorldActivitySender.this, notificationsoundpath);

            mediaPlayer.prepare(); // might take long! (for buffering, etc)

            mediaPlayer.start();
          /*  int maxVolume = 50;
            float log1=(float)(Math.log(maxVolume-1)/Math.log(maxVolume));
            mediaPlayer.setVolume(1,log1);
*/
            mediaPlayer.setLooping(true);

        }
        catch (Exception e)
        {
            Log.e("Media player error",e.getMessage());
        }
    }
}
