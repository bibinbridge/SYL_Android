package com.webcamconsult.syl.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.interfaces.SYLResendverificationListener;
import com.webcamconsult.syl.interfaces.SYLSendVerificationcodeListener;
import com.webcamconsult.syl.interfaces.SYLVerifyMobileNumberListner;
import com.webcamconsult.syl.model.SYLResendVerificationResponsemodel;
import com.webcamconsult.syl.model.SYLVerificationcodeResponseModel;
import com.webcamconsult.syl.model.SYLVerifyMobileDetails;
import com.webcamconsult.syl.modelmanager.SYLSendVerifivationcodemodelmanager;
import com.webcamconsult.syl.modelmanager.SYLresendVerificationcodeModelManager;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLResendVerificationcodeViewManager;
import com.webcamconsult.viewmanager.SYLSendVerificationcodeViewmanager;

import java.lang.ref.WeakReference;

public class SYLVerificationCodeActivity extends FragmentActivity implements
		SYLSendVerificationcodeListener, SYLResendverificationListener {

	Button btn_submit, btn_resend;
	EditText edittxt_verifyMobile;
	Context mContext;
	String method = "post";
	ImageView imageview_backarrow;
	TextView mverifynumberTextView;
	String verificationcode, email, sylaccesstoken,timezone;
	ProgressDialog msylProgressDialog;
	static	String verificationcodeshowing="false";
	static	String verificationcodemessage;
	static	String resendverificationcodeshowing="false";
	static	String resendverificationcodemessage;
	static WeakReference<ProgressDialog> Dialog;
	static boolean waiting = false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.syl_verificationcodelayout);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mContext = this;
if(icicle!=null)
{
	if(icicle.getString("verificationcodeshowing").equals("true"))
	{
		verificationcodeshowing="true";
		verificationcodemessage=icicle.getString("verificationcodemessage");
		buildAlertMessage(SYLVerificationCodeActivity.this,verificationcodemessage);
	}
	if(icicle.getString("resendverificationcodeshowing").equals("true"))
	{
		resendverificationcodeshowing="true";
		verificationcodemessage=icicle.getString("resendverificationcodemessage");
		showresendAlertMessage(SYLVerificationCodeActivity.this, resendverificationcodemessage);
	}
	restoreProgress(icicle);
}
		initActivity();

	}

	/* Initializing activity class */
	private void initActivity() {
		sylaccesstoken=getIntent().getStringExtra("accesstoken");
		Typeface font = Typeface.createFromAsset(this.getAssets(),// Setting the
				// font for
				// the
				// TextView
				"fonts/myriadproregular.OTF");
		edittxt_verifyMobile = (EditText) findViewById(R.id.etVerifyMobile);
		mverifynumberTextView = (TextView) findViewById(R.id.txt_heading);
		edittxt_verifyMobile.setTypeface(font);
		btn_submit = (Button) findViewById(R.id.btnSubmit);
		btn_resend = (Button) findViewById(R.id.btnResend);
		btn_submit.setTypeface(font);
		btn_resend.setTypeface(font);
		mverifynumberTextView.setTypeface(font);
		imageview_backarrow = (ImageView) findViewById(R.id.img_backarrow);
		/*No need to show backarrow after when user navigated from profile update*/
if(getIntent().getStringExtra("intentfrom").equals("profile"))
{
	imageview_backarrow.setVisibility(View.INVISIBLE);
}

		imageview_backarrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				navigateToSignupScreen();
			}
		});

	}

	private void navigateToSignupScreen() {
		Intent signuintent=null;
		String intentfrom=getIntent().getStringExtra("intentfrom");
		if(intentfrom.equals("signup"))
		{
signuintent=new Intent(SYLVerificationCodeActivity.this,SYLSignupActivity.class);
		}
		else if(intentfrom.equals("signin"))
		{
			signuintent=new Intent(SYLVerificationCodeActivity.this,SYLSigninActivity.class);
		}


		startActivity(signuintent);
		finish();
	}

	public void sendVerificationcode(View v) {
		hideKeyboard();
		if(validateVerification()) {
			if (SYLUtil.isNetworkAvailable(SYLVerificationCodeActivity.this)) {
				waiting = true;

				msylProgressDialog = new ProgressDialog(SYLVerificationCodeActivity.this);
				msylProgressDialog.setMessage("Please wait...");
				msylProgressDialog.setCancelable(false);
				msylProgressDialog.setCanceledOnTouchOutside(false);
				msylProgressDialog.show();
				Dialog = new WeakReference<ProgressDialog>(msylProgressDialog);
				verificationcode = edittxt_verifyMobile.getText().toString();

				email = SYLSaveValues
						.getSYLEmailAddress(SYLVerificationCodeActivity.this);
				timezone = SYLUtil.getTimeGMTZone(SYLVerificationCodeActivity.this);
				SYLSendVerificationcodeViewmanager mmodelmanager = new SYLSendVerificationcodeViewmanager();
				mmodelmanager.msylsendverificationlistener = SYLVerificationCodeActivity.this;
				mmodelmanager.sendVerifivationcode(verificationcode, email,
						sylaccesstoken, timezone);
				Log.e(verificationcode, sylaccesstoken);
			}
			else {
				SYLUtil.buildAlertMessage(SYLVerificationCodeActivity.this,
						getString(R.string.network_alertmessage));
			}
		}
	}
	private boolean validateVerification(){
		boolean valid = true;
	if (TextUtils.isEmpty(edittxt_verifyMobile.getText().toString())) {
		valid = false;
		edittxt_verifyMobile.requestFocus();
		edittxt_verifyMobile.setError("Please enter a valid verification code");

	}
	return valid;
	}
	protected void SuccessAlert(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@SuppressLint("NewApi")
							public void onClick(final DialogInterface dialog,
									final int id) {
								// mbtnSubmit.setBackground(getResources().getDrawable(R.drawable.submitbutton));

								Intent intent = new Intent(
										getApplicationContext(),
										SYLFragmentChangeActivity.class);
								intent.putExtra("fragmentvalue",
										"newappointment");
								intent.putExtra("needrefresh",
										"true");
								startActivity(intent);
								finish();
								dialog.cancel();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();

	}

	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

		}

	}

	public void resendVerificationcode(View v) {
		if (SYLUtil.isNetworkAvailable(SYLVerificationCodeActivity.this)) {
			waiting = true;
			msylProgressDialog = new ProgressDialog(SYLVerificationCodeActivity.this);
			msylProgressDialog.setMessage("Please wait...");
			msylProgressDialog.show();
			Dialog = new WeakReference<ProgressDialog>(msylProgressDialog);

String 	gmttimezone=SYLUtil.getTimeGMTZone(SYLVerificationCodeActivity.this);
	SYLResendVerificationcodeViewManager mmanager=new SYLResendVerificationcodeViewManager();
	mmanager.doresendVerificationcode(sylaccesstoken,gmttimezone);
	mmanager.msylresendverificationlistener=SYLVerificationCodeActivity.this;
		}
	}

	@Override
	public void onDidFinished(
			SYLVerificationcodeResponseModel msylverificationcoderesponse) {
		// TODO Auto-generated method stub
		try {
			waiting = false;
			if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
				msylProgressDialog.dismiss();
			}
			if (msylverificationcoderesponse != null) {
				Log.e("verification", "" + msylverificationcoderesponse.isVerified());

				if (msylverificationcoderesponse.isVerified()) {
					String accesstoken = getIntent().getStringExtra("accesstoken");
					SYLSaveValues.setSYLaccessToken(accesstoken, SYLVerificationCodeActivity.this);
					verificationcodeshowing="true";
					verificationcodemessage="Your verification code has been verified";
					buildAlertMessage(SYLVerificationCodeActivity.this, "Your verification code has been verified");
				} else {
					resendverificationcodeshowing="true";
					resendverificationcodemessage="Please check your verification code and try again";
					showresendAlertMessage(SYLVerificationCodeActivity.this, "Please check your verification code and try again");
				}
			}
		} catch (Exception e) {
			resendverificationcodeshowing="true";
			resendverificationcodemessage="Unexpected server error occured";
			showresendAlertMessage(SYLVerificationCodeActivity.this, "Unexpected server error occured");
		}
	}
private void buildAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
												final int id) {

								dialog.cancel();
								SYLSaveValues.setSYLFirsttimeLogin("signedin",SYLVerificationCodeActivity.this);
								Intent intent = new Intent(getApplicationContext(),
										SYLFragmentChangeActivity.class);
								intent.putExtra("fragmentvalue", "newappointment");
								intent.putExtra("needrefresh",
										"true");
								startActivity(intent);
								finish();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}
	private void showresendAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
												final int id) {
								resendverificationcodeshowing="false";
								dialog.cancel();

							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}
	@Override
	public void onresendverificationFinish(
			SYLResendVerificationResponsemodel mresendverificationresponsemodel) {
		// TODO Auto-generated method stub
		waiting = false;
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}

		if (mresendverificationresponsemodel.isSuccess()) {

			String email = SYLSaveValues
					.getSYLEmailAddress(SYLVerificationCodeActivity.this);
			resendverificationcodeshowing="true";
			resendverificationcodemessage="Verification code is successfully send to " + email;
			showresendAlertMessage(SYLVerificationCodeActivity.this,
					"Verification code is successfully send to " + email);
		} else {
			resendverificationcodeshowing="true";
			resendverificationcodemessage="Verificationcode sending failed";
			showresendAlertMessage(SYLVerificationCodeActivity.this,
					"Verificationcode sending failed");
		}
	}
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
//	super.onBackPressed();
	if(getIntent().getStringExtra("intentfrom").equals("signup")) {
		navigateToSignupScreen();
	}
	else if(getIntent().getStringExtra("intentfrom").equals("profile")) {
		finish();
	} else {
		finish();
	}
}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("verificationcodeshowing", verificationcodeshowing);
		outState.putString("verificationcodemessage", verificationcodemessage);
		outState.putString("resendverificationcodeshowing", resendverificationcodeshowing);
		outState.putString("resendverificationcodemessage", resendverificationcodemessage);
		outState.putBoolean("waiting", waiting);
	}

	private void restoreProgress(Bundle savedInstanceState) {
		waiting = savedInstanceState.getBoolean("waiting");

		if (waiting) {
			if (Dialog != null) {
				ProgressDialog refresher = (ProgressDialog) Dialog.get();
				refresher.dismiss();
				msylProgressDialog = new ProgressDialog(SYLVerificationCodeActivity.this);
				msylProgressDialog.setMessage("Please wait...");
				msylProgressDialog.show();
				Dialog = new WeakReference<ProgressDialog>(msylProgressDialog);
			}
		}

	}


	private void hideKeyboard()
	{
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
