package com.webcamconsult.syl.opentok.send;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.activities.SYLSigninActivity;
import com.webcamconsult.syl.gcm.NotificationReceiverActivity;
import com.webcamconsult.syl.interfaces.SYLChangeconferenceListener;
import com.webcamconsult.syl.interfaces.SYLFetchSessionStatusListener;
import com.webcamconsult.syl.interfaces.SYLOpentoksessionFinishListener;
import com.webcamconsult.syl.model.SYLChangeConferenceResponeModel;
import com.webcamconsult.syl.model.SYLFetchSessionAppointmentResponseModel;
import com.webcamconsult.syl.opentok.ClearNotificationService;
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
public class HelloWorldActivityReceiver extends Activity implements
        Session.SessionListener, Publisher.PublisherListener,
        Subscriber.VideoListener ,Session.ConnectionListener,SYLChangeconferenceListener,SYLFetchSessionStatusListener ,SYLOpentoksessionFinishListener{

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
RelativeLayout mcallrelativelayout;
String appointmentid;
private String sessionid,token,apikey;
private  CounterClass timer ;
    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOGTAG, "ONCREATE");
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









        setContentView(com.webcamconsult.syl.R.layout.helloworld_receiverlayout);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        timer = new CounterClass(28000, 1000);
timer.start();
sessionid=getIntent().getStringExtra("sessionid");
token=getIntent().getStringExtra("token");
apikey=getIntent().getStringExtra("apikey");
appointmentid=getIntent().getStringExtra("appointmentid");
/*
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
*/
        mPublisherViewContainer = (RelativeLayout) findViewById(R.id.publisherview);
        mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberview);
        testViewContainer = (RelativeLayout) findViewById(R.id.testView);
        mLoadingSub = (ProgressBar) findViewById(R.id.loadingSpinner);
        mLoadingSub.setVisibility(View.VISIBLE);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mStreams = new ArrayList<Stream>();

        sessionConnect();
   
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
    public void onPause()

    {
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

        Intent notificationIntent = new Intent(this, HelloWorldActivityReceiver.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        mNotifyBuilder.setContentIntent(intent);
        if (mConnection == null) {
            mConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className, IBinder binder) {
                    ((ClearNotificationService.ClearBinder) binder).service.startService(new Intent(HelloWorldActivityReceiver.this, ClearNotificationService.class));
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotificationManager.notify(ClearNotificationService.NOTIFICATION_ID, mNotifyBuilder.build());
                }

                @Override
                public void onServiceDisconnected(ComponentName className) {
                    mConnection = null;
                }

            };
        }

        if (!mIsBound) {
            bindService(new Intent(HelloWorldActivityReceiver.this,
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
        mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);

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
            mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
            if (mSession != null) {
                mSession.disconnect();
            }
        }
    }

    @Override
    public void onDestroy() {
        mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
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
            changeConferenceStatuscode("FINISHED");
        }

        super.onBackPressed();
        timer.cancel();
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
            mSession = new Session(HelloWorldActivityReceiver.this,
                    apikey,sessionid);
            mSession.setSessionListener(this);
            mSession.setConnectionListener(this);
            mSession.connect(token);
      
        }
    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOGTAG, "Connected to the session.");
        if (mPublisher == null) {
            mPublisher = new Publisher(HelloWorldActivityReceiver.this, "publisher");
            mPublisher.setPublisherListener(this);
            attachPublisherView(mPublisher);
attachTestView();
            mSession.publish(mPublisher);

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
        mSubscriber = new Subscriber(HelloWorldActivityReceiver.this, stream);
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
                if (mSession != null) {
                    mSession.disconnect();
                    changeConferenceStatuscode("FINISHED");
                }
                Log.e("opentok disconnect", "call change conference");
                timer.cancel();
                finish();
            }
        });
        btnTag.setBackgroundResource(R.drawable.selector_rejectcall);
        btnTag.setTextColor(getResources().getColor(R.color.white));
        btnTag.setSingleLine(true);
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
		//	mcallrelativelayout.setVisibility(View.GONE);
	//		mPublisherViewContainer.setVisibility(View.VISIBLE);
		//	mSubscriberViewContainer.setVisibility(View.VISIBLE);
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

			Log.e(" Receiver-time", hms);
		}
		

		
		
		
		
		
	}
	@Override
	public void onConnectionCreated(Session arg0, Connection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionDestroyed(Session arg0, Connection arg1) {
		// TODO Auto-generated method stub
		Log.e("Connection destroyed","Connection Destroyed");
		timer.cancel();
        showAlertMessage(HelloWorldActivityReceiver.this, "User ended the call");
	}
    public void onEndCall(View v) {
        if (mSession != null) {
            mSession.disconnect();
            changeConferenceStatuscode("FINISHED");
        }
Log.e("opentok disconnect","call change conference");
timer.cancel();

        finish();
    }
    
	private void changeConferenceStatuscode(String statuscode)
	{
		String appointmentid=getIntent().getExtras().getString("appointmentid");
		String timezone=	SYLUtil
				.getTimeGMTZone(HelloWorldActivityReceiver.this);
		String status=statuscode;
		SYLChangeOpentokStatusViewmanager mChangeOpentokstausviewmanager=new SYLChangeOpentokStatusViewmanager();
mChangeOpentokstausviewmanager.mConferenceChangeListener=HelloWorldActivityReceiver.this;
		mChangeOpentokstausviewmanager.changeOpentokStatus(status, timezone, appointmentid);
	}
    
    
	private void  showAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(HelloWorldActivityReceiver.this);
		builder.setMessage(message)
				.setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {

                                dialog.cancel();
                                finish();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}
    
    private void navigatetoAppointmentsScreen() {
	Intent signinIntent = new Intent(HelloWorldActivityReceiver.this,
			SYLFragmentChangeActivity.class);
	signinIntent.putExtra("fragmentvalue", "newappointment");
startActivity(signinIntent);
	finish();
    
    }

	@Override
	public void finishConferenceStstus(
			SYLChangeConferenceResponeModel msylChangeConferencemodel) {
		// TODO Auto-generated method stub
		
	} 
	private void fetchSessionStatusForAppointment() {
		String accesstoken = SYLSaveValues.getSYLaccessToken(HelloWorldActivityReceiver.this);
		String timezone = SYLUtil.getTimeGMTZone(HelloWorldActivityReceiver.this);
		SYLFetchSessionStatusViewManager mViewmanager = new SYLFetchSessionStatusViewManager();
		mViewmanager.msylFetchSessionStatusListener = HelloWorldActivityReceiver.this;
		mViewmanager.fetchSessionStatus(appointmentid, accesstoken, timezone);
	}

	@Override
	public void finishFetchSessionAppointmentStatus(
			SYLFetchSessionAppointmentResponseModel mSylFetchsessionappointmentresponsemodel) {
		// TODO Auto-generated method stub
		if(mSylFetchsessionappointmentresponsemodel!=null)
		if(mSylFetchsessionappointmentresponsemodel.getSessionStatusCode().equals("FINISHED"))
		{
			showAlertMessage(HelloWorldActivityReceiver.this,"SYL user disconnected the call");
	}
	}
    public void finishopentokSession()
    {
        this.finish();
    }


}
