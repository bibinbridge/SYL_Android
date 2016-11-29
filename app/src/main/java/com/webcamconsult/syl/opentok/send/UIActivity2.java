package com.webcamconsult.syl.opentok.send;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLOpentokcallSenderActivity;
import com.webcamconsult.syl.activities.SYLSigninActivity;
import com.webcamconsult.syl.activities.SYLOpentokcallSenderActivity.CounterClass;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLFetchSessionStatusListener;
import com.webcamconsult.syl.model.SYLFetchSessionAppointmentResponseModel;
import com.webcamconsult.syl.opentok.AudioLevelView;
import com.webcamconsult.syl.opentok.ClearNotificationService;
import com.webcamconsult.syl.opentok.PublisherControlFragment;
import com.webcamconsult.syl.opentok.UIActivity;
import com.webcamconsult.syl.opentok.ClearNotificationService.ClearBinder;
import com.webcamconsult.syl.opentok.OpenTokConfig;
import com.webcamconsult.syl.opentok.send.PublisherControlFragment2.PublisherCallbacks;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLFetchSessionStatusViewManager;

public class UIActivity2 extends Activity implements
		SYLFetchSessionStatusListener, Session.SessionListener,
		Session.ArchiveListener, Session.StreamPropertiesListener,
		Publisher.PublisherListener, Subscriber.VideoListener,
		Subscriber.SubscriberListener,
		SubscriberControlFragment2.SubscriberCallbacks,
		PublisherControlFragment2.PublisherCallbacks,Session.ConnectionListener {
	String sessionid, token, apikey, appointmentid, profileimage_url;
	ImageView mUserImageView;
	TextView mNameTextView;
	ProgressDialog msylProgressDialog;
	private static final String LOGTAG = "demo-UI";
	private static final int ANIMATION_DURATION = 3000;

	private Session mSession;
	private Publisher mPublisher;
	private Subscriber mSubscriber;
	private ArrayList<Stream> mStreams = new ArrayList<Stream>();
	private Handler mHandler = new Handler();

	private boolean mSubscriberAudioOnly = false;
	private boolean archiving = false;
	private boolean resumeHasRun = false;

	// View related variables
	private RelativeLayout mPublisherViewContainer;
	private RelativeLayout mSubscriberViewContainer;
	private RelativeLayout mSubscriberAudioOnlyView;

	// Fragments
	private SubscriberControlFragment2 mSubscriberFragment;
	private PublisherControlFragment2 mPublisherFragment;
	private PublisherStatusFragment2 mPublisherStatusFragment;
	private SubscriberQualityFragment2 mSubscriberQualityFragment;
	private FragmentTransaction mFragmentTransaction;

	// Spinning wheel for loading subscriber view
	private ProgressBar mLoadingSub;
public  CounterClass timer ;
	private AudioLevelView mAudioLevelView;

	private SubscriberQualityFragment2.CongestionLevel congestion = SubscriberQualityFragment2.CongestionLevel.Low;

	private boolean mIsBound = false;
	private NotificationCompat.Builder mNotifyBuilder;
	private NotificationManager mNotificationManager;
	private ServiceConnection mConnection;
Bundle b;
RelativeLayout mCallRelativeLayout;
RelativeLayout mFragmentsubcontainer;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
b=savedInstanceState;
		setContentView(R.layout.layout_ui_activitysender);
mCallRelativeLayout=(RelativeLayout)findViewById(R.id.rel_test);
mPublisherViewContainer = (RelativeLayout) findViewById(R.id.publisherView);
mPublisherViewContainer.setVisibility(View.GONE);


//		mUserImageView = (ImageView) findViewById(R.id.row_icon);
	//	mNameTextView = (TextView) findViewById(R.id.txt_name);
		sessionid = getIntent().getExtras().getString("sessionid");
		token = getIntent().getExtras().getString("token");
		apikey = getIntent().getExtras().getString("apikey");
		appointmentid = getIntent().getExtras().getString("appointmentid");
		profileimage_url = getIntent().getExtras()
				.getString("profileimage_url");
	//	mNameTextView.setText(getIntent().getExtras().getString("name"));
		ImageLoader imageLoader = new ImageLoader(UIActivity2.this);
		//imageLoader.DisplayImage(profileimage_url, mUserImageView);
		
	 	loadInterfaceandsessionStart();
	     if (savedInstanceState == null) {
	            mFragmentTransaction = getFragmentManager().beginTransaction();
	            initSubscriberFragment();
	            initPublisherFragment();
	            initPublisherStatusFragment();
	            initSubscriberQualityFragment();
	            mFragmentTransaction.commitAllowingStateLoss();
	        }
			msylProgressDialog = new ProgressDialog(UIActivity2.this);
			msylProgressDialog.setMessage("Please wait...");
			msylProgressDialog.show();
		sessionConnect();
	

 timer = new CounterClass(10000, 1000);
	
	}

	public class CounterClass extends CountDownTimer {
		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			Log.e("timer finished", "timer finished");
			loadCameraView();
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

			Log.e("time", hms);
		}
	}

	private void fetchSessionStatusForAppointment() {
		String accesstoken = SYLSaveValues.getSYLaccessToken(UIActivity2.this);
		String timezone = SYLUtil.getTimeGMTZone(UIActivity2.this);
		SYLFetchSessionStatusViewManager mViewmanager = new SYLFetchSessionStatusViewManager();
		mViewmanager.msylFetchSessionStatusListener = UIActivity2.this;
		mViewmanager.fetchSessionStatus(appointmentid, accesstoken, timezone);
	}

	@Override
	public void finishFetchSessionAppointmentStatus(
			SYLFetchSessionAppointmentResponseModel mSylFetchsessionappointmentresponsemodel) {
		// TODO Auto-generated method stub
		if (mSylFetchsessionappointmentresponsemodel != null) {
			if (mSylFetchsessionappointmentresponsemodel.isSuccess()) {
			
			
		
			
			} 
			else 
			{
				SYLUtil.buildAlertMessage(UIActivity2.this,
						mSylFetchsessionappointmentresponsemodel.getError()
								.getErrorTitle());
			}
		} else {
			SYLUtil.buildAlertMessage(UIActivity2.this,
					"Opentok error happened");
		}
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
            mSession.setArchiveListener(this);
            mSession.setStreamPropertiesListener(this);
            mSession.setConnectionListener(this);
            mSession.connect(token);
            
        }
       
     
    }
	public void loadInterfaceandsessionStart() {
		//setContentView(R.layout.layout_ui_activity);

		mLoadingSub = (ProgressBar) findViewById(R.id.loadingSpinner);

	//	mPublisherViewContainer = (RelativeLayout) findViewById(R.id.publisherView);
		mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberView);
		mSubscriberAudioOnlyView = (RelativeLayout) findViewById(R.id.audioOnlyView);

		// Initialize
		mAudioLevelView = (AudioLevelView) findViewById(R.id.subscribermeter);
		mAudioLevelView.setIcons(BitmapFactory.decodeResource(getResources(),
				R.drawable.headset));
		// Attach running video views
		if (mPublisher != null) {
			attachPublisherView(mPublisher);
		}

		// show subscriber status
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mSubscriber != null) {
					attachSubscriberView(mSubscriber);

					if (mSubscriberAudioOnly) {
						mSubscriber.getView().setVisibility(View.GONE);
						setAudioOnlyView(true);
						congestion = SubscriberQualityFragment2.CongestionLevel.High;
					}
				}
			}
		}, 0);

		loadFragments();
	}

	public void loadFragments() {
		// show subscriber status
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mSubscriber != null) {
					mSubscriberFragment.showSubscriberWidget(true);
					mSubscriberFragment.initSubscriberUI();

					if (congestion != SubscriberQualityFragment2.CongestionLevel.Low) {
						mSubscriberQualityFragment.setCongestion(congestion);
						mSubscriberQualityFragment.showSubscriberWidget(true);
					}
				}
			}
		}, 0);

		// show publisher status
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mPublisher != null) {
					mPublisherFragment.showPublisherWidget(true);
					mPublisherFragment.initPublisherUI();

					if (archiving) {
						mPublisherStatusFragment.updateArchivingUI(true);
						setPubViewMargins();
					}
				}
			}
		}, 0);

	}

	public void initSubscriberFragment() {
		mSubscriberFragment = new SubscriberControlFragment2();

		getFragmentManager().beginTransaction()
				.add(R.id.fragment_sub_container, mSubscriberFragment).
		
				commit();
	}

	public void initPublisherFragment() {
		mPublisherFragment = new PublisherControlFragment2();
		getFragmentManager().beginTransaction()
				.add(R.id.fragment_pub_container, mPublisherFragment).commit();
	}

	public void initPublisherStatusFragment() {
		mPublisherStatusFragment = new PublisherStatusFragment2();
		getFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_pub_status_container,
						mPublisherStatusFragment).commit();
	}

	public void initSubscriberQualityFragment() {
		mSubscriberQualityFragment = new SubscriberQualityFragment2();
		getFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_sub_quality_container,
						mSubscriberQualityFragment).commit();
	}

	public void setPubViewMargins() {
		RelativeLayout.LayoutParams pubLayoutParams = (LayoutParams) mPublisherViewContainer
				.getLayoutParams();
		int bottomMargin = 0;
		boolean controlBarVisible = mPublisherFragment
				.isMPublisherWidgetVisible();
		boolean statusBarVisible = mPublisherStatusFragment
				.isMPubStatusWidgetVisible();
		RelativeLayout.LayoutParams pubControlLayoutParams = (LayoutParams) mPublisherFragment
				.getMPublisherContainer().getLayoutParams();
		RelativeLayout.LayoutParams pubStatusLayoutParams = (LayoutParams) mPublisherStatusFragment
				.getMPubStatusContainer().getLayoutParams();

		// setting margins for publisher view on portrait orientation
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (statusBarVisible && archiving) {
				// height of publisher control bar + height of publisher status
				// bar + 20 px
				bottomMargin = pubControlLayoutParams.height
						+ pubStatusLayoutParams.height + dpToPx(20);
			} else {
				if (controlBarVisible) {
					// height of publisher control bar + 20 px
					bottomMargin = pubControlLayoutParams.height + dpToPx(20);
				} else {
					bottomMargin = dpToPx(20);
				}
			}
		}

		// setting margins for publisher view on landscape orientation
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (statusBarVisible && archiving) {
				bottomMargin = pubStatusLayoutParams.height + dpToPx(20);
			} else {
				bottomMargin = dpToPx(20);
			}
		}

		pubLayoutParams.bottomMargin = bottomMargin;
		pubLayoutParams.leftMargin = dpToPx(20);

		mPublisherViewContainer.setLayoutParams(pubLayoutParams);

		if (mSubscriber != null) {
			if (mSubscriberAudioOnly) {
				RelativeLayout.LayoutParams subLayoutParams = (LayoutParams) mSubscriberAudioOnlyView
						.getLayoutParams();
				int subBottomMargin = 0;
				subBottomMargin = pubLayoutParams.bottomMargin;
				subLayoutParams.bottomMargin = subBottomMargin;
				mSubscriberAudioOnlyView.setLayoutParams(subLayoutParams);
			}

			setSubQualityMargins();
		}
	}

	public int dpToPx(int dp) {
		double screenDensity = getResources().getDisplayMetrics().density;
		return (int) (screenDensity * (double) dp);
	}

	private void attachPublisherView(Publisher publisher) {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		mPublisherViewContainer.addView(publisher.getView(), layoutParams);
		mPublisherViewContainer.setDrawingCacheEnabled(true);
		publisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
				BaseVideoRenderer.STYLE_VIDEO_FILL);
		publisher.getView().setOnClickListener(onViewClick);
	}

	private void setAudioOnlyView(boolean audioOnlyEnabled) {
		mSubscriberAudioOnly = audioOnlyEnabled;

		if (audioOnlyEnabled) {
			mSubscriber.getView().setVisibility(View.GONE);
			mSubscriberAudioOnlyView.setVisibility(View.VISIBLE);
			mSubscriberAudioOnlyView.setOnClickListener(onViewClick);

			// Audio only text for subscriber
			TextView subStatusText = (TextView) findViewById(R.id.subscriberName);
			subStatusText.setText(R.string.audioOnly);
			AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setDuration(ANIMATION_DURATION);
			subStatusText.startAnimation(aa);

			mSubscriber
					.setAudioLevelListener(new SubscriberKit.AudioLevelListener() {
						@Override
						public void onAudioLevelUpdated(
								SubscriberKit subscriber, float audioLevel) {
							mAudioLevelView.setMeterValue(audioLevel);
						}
					});
		} else {
			if (!mSubscriberAudioOnly) {
				mSubscriber.getView().setVisibility(View.VISIBLE);
				mSubscriberAudioOnlyView.setVisibility(View.GONE);

				mSubscriber.setAudioLevelListener(null);
			}
		}
	}

	private OnClickListener onViewClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			boolean visible = false;

			if (mPublisher != null) {
				// check visibility of bars
				if (!mPublisherFragment.isMPublisherWidgetVisible()) {
					visible = true;
				}
				mPublisherFragment.publisherClick();
				if (archiving) {
					mPublisherStatusFragment.publisherClick();
				}
				setPubViewMargins();
				if (mSubscriber != null) {
					mSubscriberFragment.showSubscriberWidget(visible);
					mSubscriberFragment.initSubscriberUI();
				}
			}
		}
	};

	public void setSubQualityMargins() {
		RelativeLayout.LayoutParams subQualityLayoutParams = (LayoutParams) mSubscriberQualityFragment
				.getSubQualityContainer().getLayoutParams();
		boolean pubControlBarVisible = mPublisherFragment
				.isMPublisherWidgetVisible();
		boolean pubStatusBarVisible = mPublisherStatusFragment
				.isMPubStatusWidgetVisible();
		RelativeLayout.LayoutParams pubControlLayoutParams = (LayoutParams) mPublisherFragment
				.getMPublisherContainer().getLayoutParams();
		RelativeLayout.LayoutParams pubStatusLayoutParams = (LayoutParams) mPublisherStatusFragment
				.getMPubStatusContainer().getLayoutParams();
		RelativeLayout.LayoutParams audioMeterLayoutParams = (LayoutParams) mAudioLevelView
				.getLayoutParams();

		int bottomMargin = 0;

		// control pub fragment
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (pubControlBarVisible) {
				bottomMargin = pubControlLayoutParams.height + dpToPx(10);
			}
			if (pubStatusBarVisible && archiving) {
				bottomMargin = pubStatusLayoutParams.height + dpToPx(10);
			}
			if (bottomMargin == 0) {
				bottomMargin = dpToPx(10);
			}
			subQualityLayoutParams.rightMargin = dpToPx(10);
		}

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (!pubControlBarVisible) {
				subQualityLayoutParams.rightMargin = dpToPx(10);
				bottomMargin = dpToPx(10);
				audioMeterLayoutParams.rightMargin = 0;
				mAudioLevelView.setLayoutParams(audioMeterLayoutParams);

			} else {
				subQualityLayoutParams.rightMargin = pubControlLayoutParams.width;
				bottomMargin = dpToPx(10);
				audioMeterLayoutParams.rightMargin = pubControlLayoutParams.width;
			}
			if (pubStatusBarVisible && archiving) {
				bottomMargin = pubStatusLayoutParams.height + dpToPx(10);
			}
			mAudioLevelView.setLayoutParams(audioMeterLayoutParams);
		}

		subQualityLayoutParams.bottomMargin = bottomMargin;

		mSubscriberQualityFragment.getSubQualityContainer().setLayoutParams(
				subQualityLayoutParams);

	}

	private void attachSubscriberView(Subscriber subscriber) {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		mSubscriberViewContainer.removeView(mSubscriber.getView());
		mSubscriberViewContainer.addView(subscriber.getView(), layoutParams);
		subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
				BaseVideoRenderer.STYLE_VIDEO_FILL);
		subscriber.getView().setOnClickListener(onViewClick);
	}

	@Override
	public void onConnected(Session session) {
		// TODO Auto-generated method stub
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		Log.i(LOGTAG, "Connected to the session.");

timer.start();

	}
private void loadCameraView()
{
	mCallRelativeLayout.setVisibility(View.INVISIBLE);
	mPublisherViewContainer.setVisibility(View.VISIBLE);
	if (mPublisher == null) {
		mPublisher = new Publisher(this, "Publisher");
		mPublisher.setPublisherListener(this);
		attachPublisherView(mPublisher);
		attachSubscriberView(mSubscriber);
		mSession.publish(mPublisher);
	}
	}
	@Override
	public void onDisconnected(Session session) {
		// TODO Auto-generated method stub
		if (mPublisher != null) {
			mPublisherViewContainer.removeView(mPublisher.getRenderer()
					.getView());
		}

		if (mSubscriber != null) {
			mSubscriberViewContainer.removeView(mSubscriber.getRenderer()
					.getView());
		}

		mPublisher = null;
		mSubscriber = null;
		mStreams.clear();
		mSession = null;
	}

	@Override
	public void onError(Session session, OpentokError exception) {
		// TODO Auto-generated method stub
		Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStreamDropped(Session session, Stream stream) {
		// TODO Auto-generated method stub
		mStreams.remove(stream);
		if (!OpenTokConfig.SUBSCRIBE_TO_SELF) {
			if (mSubscriber != null
					&& mSubscriber.getStream().getStreamId()
							.equals(stream.getStreamId())) {
				mSubscriberViewContainer.removeView(mSubscriber.getView());
				mSubscriber = null;
				findViewById(R.id.avatar).setVisibility(View.GONE);
				mSubscriberAudioOnly = false;
				if (!mStreams.isEmpty()) {
					subscribeToStream(mStreams.get(0));
				}
			}
		}
	}

	@Override
	public void onStreamReceived(Session session, Stream stream) {
		// TODO Auto-generated method stub
		if (!OpenTokConfig.SUBSCRIBE_TO_SELF) {
			mStreams.add(stream);
			if (mSubscriber == null) {
				subscribeToStream(stream);
			}
		}
	}

	private void subscribeToStream(Stream stream) {
		mSubscriber = new Subscriber(this, stream);
		mSubscriber.setSubscriberListener(this);
		mSubscriber.setVideoListener(this);
		mSession.subscribe(mSubscriber);

		if (mSubscriber.getSubscribeToVideo()) {
			// start loading spinning
			mLoadingSub.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onMutePublisher() {
		// TODO Auto-generated method stub
		if (mPublisher != null) {
			mPublisher.setPublishAudio(!mPublisher.getPublishAudio());
		}
	}

	@Override
	public void onSwapCamera() {
		// TODO Auto-generated method stub
		if (mPublisher != null) {
			mPublisher.swapCamera();
		}
	}

	@Override
	public void onEndCall() {
		// TODO Auto-generated method stub
		if (mSession != null) {
			mSession.disconnect();
		}
		Log.e("opentok disconnect", "call change conference");
		finish();
	}

	@Override
	public void onMuteSubscriber() {
		// TODO Auto-generated method stub
		if (mSession != null) {
			mSession.disconnect();
		}
		Log.e("opentok disconnect", "call change conference");
		finish();
	}

	@Override
	public void onConnected(SubscriberKit subscriber) {
		// TODO Auto-generated method stub
	Log.e("subscriber connected","subscriber connected");

	mLoadingSub.setVisibility(View.GONE);
	mSubscriberFragment.showSubscriberWidget(true);
	mSubscriberFragment.initSubscriberUI();
	}



	

	
	@Override
	public void onDisconnected(SubscriberKit subscriber) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Subscriber disconnected.");
	}

	@Override
	public void onError(SubscriberKit subscriber, OpentokError exception) {
		// TODO Auto-generated method stub
		Log.e(LOGTAG, "Subscriber exception: " + exception.getMessage());
	}

	@Override
	public void onVideoDataReceived(SubscriberKit subscriber) {
		// TODO Auto-generated method stub
		mLoadingSub.setVisibility(View.GONE);
		attachSubscriberView(mSubscriber);
	}

	@Override
	public void onVideoDisableWarning(SubscriberKit subscriber) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG,
				"Video may be disabled soon due to network quality degradation. Add UI handling here.");
		mSubscriberQualityFragment
				.setCongestion(SubscriberQualityFragment2.CongestionLevel.Mid);
		congestion = SubscriberQualityFragment2.CongestionLevel.Mid;
		setSubQualityMargins();
		mSubscriberQualityFragment.showSubscriberWidget(true);
	}

	@Override
	public void onVideoDisableWarningLifted(SubscriberKit arg0) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG,
				"Video may no longer be disabled as stream quality improved. Add UI handling here.");
		mSubscriberQualityFragment
				.setCongestion(SubscriberQualityFragment2.CongestionLevel.Low);
		congestion = SubscriberQualityFragment2.CongestionLevel.Low;
		mSubscriberQualityFragment.showSubscriberWidget(false);
	}

	@Override
	public void onVideoDisabled(SubscriberKit subscriber, String reason) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Video disabled:" + reason);
		if (mSubscriber == subscriber) {
			setAudioOnlyView(true);
		}

		if (reason.equals("quality")) {
			mSubscriberQualityFragment
					.setCongestion(SubscriberQualityFragment2.CongestionLevel.High);
			congestion = SubscriberQualityFragment2.CongestionLevel.High;
			setSubQualityMargins();
			mSubscriberQualityFragment.showSubscriberWidget(true);
		}
	}

	@Override
	public void onVideoEnabled(SubscriberKit subscriber, String reason) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Video enabled:" + reason);
		if (mSubscriber == subscriber) {
			setAudioOnlyView(false);
		}
		if (reason.equals("quality")) {
			mSubscriberQualityFragment
					.setCongestion(SubscriberQualityFragment2.CongestionLevel.Low);
			congestion = SubscriberQualityFragment2.CongestionLevel.Low;
			mSubscriberQualityFragment.showSubscriberWidget(false);
		}
	}

	@Override
	public void onError(PublisherKit publisherkit, OpentokError exception) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Publisher exception: " + exception.getMessage());
	}

	@Override
	public void onStreamCreated(PublisherKit publisher, Stream stream) {
		// TODO Auto-generated method stub
		if (OpenTokConfig.SUBSCRIBE_TO_SELF) {
			mStreams.add(stream);
			if (mSubscriber == null) {
				subscribeToStream(stream);
			}
		}
		mPublisherFragment.showPublisherWidget(true);
		mPublisherFragment.initPublisherUI();
		mPublisherStatusFragment.showPubStatusWidget(true);
		mPublisherStatusFragment.initPubStatusUI();
	}

	@Override
	public void onStreamDestroyed(PublisherKit publisher, Stream stream) {
		// TODO Auto-generated method stub
		Log.e("publisher stream destrotyed", "publisher stream destroyed");
		if (OpenTokConfig.SUBSCRIBE_TO_SELF && mSubscriber != null) {
			unsubscriberFromStream(stream);
		}
	}

	@Override
	public void onStreamHasAudioChanged(Session arg0, Stream arg1, boolean arg2) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Stream audio changed");
	}

	@Override
	public void onStreamHasVideoChanged(Session arg0, Stream arg1, boolean arg2) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Stream video changed");
	}

	@Override
	public void onStreamVideoDimensionsChanged(Session arg0, Stream arg1,
			int arg2, int arg3) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Stream video dimensions changed");
	}

	@Override
	public void onArchiveStarted(Session session, String id, String name) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Archiving starts");
		mPublisherFragment.showPublisherWidget(false);

		archiving = true;
		mPublisherStatusFragment.updateArchivingUI(true);
		mPublisherFragment.showPublisherWidget(true);
		mPublisherFragment.initPublisherUI();
		setPubViewMargins();

		if (mSubscriber != null) {
			mSubscriberFragment.showSubscriberWidget(true);
		}
	}

	@Override
	public void onArchiveStopped(Session session, String id) {
		// TODO Auto-generated method stub
		Log.i(LOGTAG, "Archiving stops");
		archiving = false;

		mPublisherStatusFragment.updateArchivingUI(false);
		setPubViewMargins();

		if (mSubscriber != null) {
			setSubQualityMargins();
		}
	}

	private void unsubscriberFromStream(Stream stream) {
		mStreams.remove(stream);
		if (mSubscriber.getStream().equals(stream)) {
			mSubscriberViewContainer.removeView(mSubscriber.getView());
			mSubscriber = null;
			if (!mStreams.isEmpty()) {
				subscribeToStream(mStreams.get(0));
			}
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

        Intent notificationIntent = new Intent(this, UIActivity2.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        mNotifyBuilder.setContentIntent(intent);
        if (mConnection == null) {
            mConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className, IBinder binder) {
                    ((ClearBinder) binder).service.startService(new Intent(UIActivity2.this, ClearNotificationService.class));
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
            Log.d(LOGTAG, "mISBOUND GOT CALLED");
            bindService(new Intent(UIActivity2.this,
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
    
    
    public void reloadInterface() {
        mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mSubscriber != null) {
					attachSubscriberView(mSubscriber);
					if (mSubscriberAudioOnly) {
						mSubscriber.getView().setVisibility(View.GONE);
						setAudioOnlyView(true);
						congestion = SubscriberQualityFragment2.CongestionLevel.High;
					}
				}
			}
		}, 500);

        loadFragments();
    }
    
    
    public Handler getmHandler() {
        return mHandler;
    }
    
    public Publisher getmPublisher() {
        return mPublisher;
    }

    public Subscriber getmSubscriber() {
        return mSubscriber;
    }
    
   public void endSession(View v) 
   {
	   onEndCall();
	 //  timer.cancel();
   }

@Override
public void onConnectionCreated(Session arg0, Connection arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnectionDestroyed(Session arg0, Connection arg1) {
	// TODO Auto-generated method stub
	Log.e("Connection destroyed","connection destroyes");
	SYLUtil.buildAlertMessage(UIActivity2.this, "Caller cancelled the call");
}
	@Override
	public void onStreamVideoTypeChanged(Session session, Stream stream,
										 Stream.StreamVideoType videoType) {
		Log.i(LOGTAG, "Stream video type changed");
	}
}