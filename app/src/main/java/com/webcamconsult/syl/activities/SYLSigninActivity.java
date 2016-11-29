package com.webcamconsult.syl.activities;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.interfaces.SYLSigninListener;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.modelmanager.SYLSigninModelManager;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.SYLApplicationConstants;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLFacebookSigninViewManager;
import com.webcamconsult.viewmanager.SYLSigninViewManager;

public class SYLSigninActivity extends FragmentActivity implements
		SYLSigninListener {
	private TextView txtview_newuser, txtview_forgotpassword;
	private Button btn_signin, btn_signup;
	private EditText mEmailEdittExt, mPasswordEdittExt;
	public boolean isFetching = true;
	GoogleCloudMessaging gcmObj;
	Context applicationContext;
	String regId = "";
	ProgressDialog msylProgressDialog;
	static WeakReference<ProgressDialog> Dialog;
	String email, password;
String accesstoken;
	CallbackManager callbackManager;
	static boolean waiting = false;


	static	String alertmessageshowingverification="false";
	static	String alertmessageverification;
	static	String alertmessageshowing="false";
	static	String alertmessage="false";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	facebookInitialize();

		setContentView(R.layout.syl_signinlayout);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		if(icicle!=null)
		{
			restoreProgress(icicle);
			if(icicle.getString("alertmessageshowingverification").equals("true"))
			{
				alertmessageshowingverification="true";
				alertmessageverification=icicle.getString("alertmessageverification");
				sylverifivcationAlertDialog(alertmessageverification);
			}
			else if(icicle.getString("alertmessageshowing").equals("true"))
			{
				alertmessageshowing="true";
				alertmessage=icicle.getString("alertmessage");
				buildAlertMessage(SYLSigninActivity.this,alertmessage);
			}

		}

		applicationContext = getApplicationContext();

		// getRegistrationId();

		// performFacebookLogin();

		txtview_newuser = (TextView) findViewById(R.id.txt_newuser);
		txtview_forgotpassword = (TextView) findViewById(R.id.txt_forgotpassword);
		mEmailEdittExt = (EditText) findViewById(R.id.etxt_email);
		mPasswordEdittExt = (EditText) findViewById(R.id.etxt_password);
		btn_signin = (Button) findViewById(R.id.btn_signin);
		btn_signup = (Button) findViewById(R.id.btn_signup);
		Typeface font = Typeface.createFromAsset(this.getAssets(),
				"fonts/myriadproregular.OTF");
		txtview_newuser.setTypeface(font);
		mEmailEdittExt.setTypeface(font);
		mPasswordEdittExt.setTypeface(font);
		txtview_forgotpassword.setTypeface(font);
		btn_signin.setTypeface(font);
		btn_signup.setTypeface(font);
		txtview_forgotpassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				navigatetoForgotpasswordScreen();
			}
		});
	}

	public void userSignin(View v) {
		hideKeyboard();
		if (validate()) {
			if (SYLUtil.isNetworkAvailable(SYLSigninActivity.this)) {
				waiting = true;
				msylProgressDialog = new ProgressDialog(SYLSigninActivity.this);
				msylProgressDialog.setMessage("Please wait...");
				msylProgressDialog.setCancelable(false);
				msylProgressDialog.setCanceledOnTouchOutside(false);
				msylProgressDialog.show();
				Dialog = new WeakReference<ProgressDialog>(msylProgressDialog);
				email = mEmailEdittExt.getText().toString().trim();
				password = mPasswordEdittExt.getText().toString();
				String gmttimezone=SYLUtil.getTimeGMTZone(SYLSigninActivity.this);
				String deviceUID=SYLUtil.getUID(SYLSigninActivity.this);
				SYLSigninViewManager mviewmanager = new SYLSigninViewManager();
				mviewmanager.msylsigninlistener = SYLSigninActivity.this;
				mviewmanager.doUserSignin(email, password,gmttimezone,deviceUID);
			} else {
				SYLUtil.buildAlertMessage(SYLSigninActivity.this,
						getString(R.string.network_alertmessage));
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
	public void navigatetoSignupScreen(View v) {
		Intent signinIntent = new Intent(SYLSigninActivity.this,
				SYLSignupActivity.class);
		SYLSigninActivity.this.startActivity(signinIntent);
		finish();

	}

	public void navigatetoForgotpasswordScreen() {
		Intent signinIntent = new Intent(SYLSigninActivity.this,
				SYLForgotpasswordActivity.class);
		SYLSigninActivity.this.startActivity(signinIntent);

	}
/*
	private void performFacebookLogin() {

		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {

					// make request to the /me API
					Request.newMeRequest(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										Log.e("Success-Facebook", user.getName());
									}
								}
							}).executeAsync();
				}
				else {
					Log.e("Success-Facebook","session closed");
				}
			}
		});
	}
*/
	private boolean validate() {
		boolean valid = true;

		if (TextUtils.isEmpty(mPasswordEdittExt.getText().toString())) {
			valid = false;
			mPasswordEdittExt.requestFocus();
			mPasswordEdittExt.setError("Please enter a valid password");

		}
		if (!SYLUtil.validateEmail(mEmailEdittExt)) {
			valid = false;

			mEmailEdittExt.requestFocus();
			mEmailEdittExt.setError("Please enter a valid email");
			// mEmailEdit.setError(getString(R.string.validate_username,R.drawable.excailm));
			mEmailEdittExt.requestFocus();
		}

		return valid;

	}

	private void getRegistrationId() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcmObj == null) {
						gcmObj = GoogleCloudMessaging
								.getInstance(applicationContext);
					}
					regId = gcmObj
							.register(SYLApplicationConstants.GOOGLE_PROJ_ID);
					msg = "Registration ID :" + regId;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				if (!TextUtils.isEmpty(regId)) {
					// Store RegId created by GCM Server in SharedPref
					// storeRegIdinSharedPref(applicationContext, regId,
					// emailID);
					Toast.makeText(
							applicationContext,
							"Registered with GCM Server successfully.\n\n"
									+ msg, Toast.LENGTH_SHORT).show();
					Log.e("registration ID", msg);
				} else {
					Toast.makeText(
							applicationContext,
							"Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
									+ msg, Toast.LENGTH_LONG).show();
				}
			}
		}.execute(null, null, null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
*/
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDidFinished(SYLSigninResponseModel msylsigninresponsemodel) {
		// TODO Auto-generated method stub
		try {
			waiting = false;
			if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
				msylProgressDialog.dismiss();
			}
			if (msylsigninresponsemodel != null) {
				if (msylsigninresponsemodel.isSuccess()) {
					String userid = Integer.toString(msylsigninresponsemodel.getUser().getUserId());
					SYLSaveValues.setSYLUserID(userid, SYLSigninActivity.this);
					accesstoken = msylsigninresponsemodel.getUser().getAccessToken();
					SYLSaveValues.setSYLaccessToken(accesstoken, SYLSigninActivity.this);
					SYLSaveValues.setProfileImageurl(msylsigninresponsemodel.getUser().getProfileImage(), SYLSigninActivity.this);
					SYLSaveValues.setSYLusername(msylsigninresponsemodel.getUser().getName(), SYLSigninActivity.this);
					SYLSaveValues.setSYLEmailAddress(msylsigninresponsemodel.getUser().getEmail(), SYLSigninActivity.this);
					SYLSaveValues.setSYLMobileNumber(msylsigninresponsemodel.getUser().getMobile(), SYLSigninActivity.this);
					SYLSaveValues.setFacebookConnected(Boolean.toString(msylsigninresponsemodel.getUser().isFacebookConnected()), SYLSigninActivity.this);
					SYLSaveValues.setSylSkypeID(msylsigninresponsemodel.getUser().getSkypeId(), SYLSigninActivity.this);
					SYLSaveValues.setSylHangoutid(msylsigninresponsemodel.getUser().getGoogleId(), SYLSigninActivity.this);
					SYLSaveValues.setSylFacetimeid(msylsigninresponsemodel.getUser().getFacetimeId(), SYLSigninActivity.this);
					SYLSaveValues.setSylCountrycode(msylsigninresponsemodel.getUser().getCountryCode(), SYLSigninActivity.this);
					if (msylsigninresponsemodel.getUser().isVerified()) {
						SYLSaveValues.setSYLFirsttimeLogin("signedin", SYLSigninActivity.this);
						Intent signinIntent = new Intent(SYLSigninActivity.this,
								SYLFragmentChangeActivity.class);
						signinIntent.putExtra("fragmentvalue", "newappointment");
						signinIntent.putExtra("needrefresh", "true");
						SYLSigninActivity.this.startActivity(signinIntent);
						waiting = false;//We use waiting flag for showing progressdialog while screen rotates
						// .We set this flag as false when navigate to appointment screen, other wise it will show progress dialog automatically
						//when signout user
						finish();
					} else {
						alertmessageshowingverification = "true";
						alertmessageverification = msylsigninresponsemodel.getUser().getEmail();
						sylverifivcationAlertDialog(msylsigninresponsemodel.getUser().getEmail());
					}
				} else {
					alertmessageshowing = "true";
					alertmessage = msylsigninresponsemodel.getError().getErrorDetail();
					buildAlertMessage(SYLSigninActivity.this,
							msylsigninresponsemodel.getError().getErrorDetail());
				}
			} else {
				alertmessageshowing = "true";
				alertmessage = "Unexpected Signin Error Occured";
				buildAlertMessage(SYLSigninActivity.this,
						"Unexpected Signin Error Occured");
			}
		}
		catch (Exception e)
		{

		}
	}

	private void sylverifivcationAlertDialog(String emailid) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
		.setCancelable(false)
		.setMessage("Please enter the verification code which we have send to your " + emailid + " to complete the login process");
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent signinIntent = new Intent(
								SYLSigninActivity.this,
								SYLVerificationCodeActivity.class);
signinIntent.putExtra("intentfrom","signin");
						alertmessageshowing="false";
						alertmessageshowingverification="false";
						signinIntent.putExtra("accesstoken",accesstoken);
						SYLSigninActivity.this.startActivity(signinIntent);
						finish();

						arg0.dismiss();

					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
private void	facebookInitialize()
{
	FacebookSdk.sdkInitialize(this.getApplicationContext());

	callbackManager = CallbackManager.Factory.create();

	LoginManager.getInstance().registerCallback(callbackManager,
			new FacebookCallback<LoginResult>() {
				@Override
				public void onSuccess(LoginResult loginResult) {
					Log.e("Success", "Login");
					String accesstoken = loginResult.getAccessToken().toString();
					String token = loginResult.getAccessToken().getToken();
					Log.e("acess token", token);
					getUserDetailsAfterFbSignin(token);
				}

				@Override
				public void onCancel() {
					Toast.makeText(SYLSigninActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onError(FacebookException exception) {
					Toast.makeText(SYLSigninActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
				}
			});
}

public void doFacebookLogin(View v)
{
	LoginManager.getInstance().logInWithReadPermissions(SYLSigninActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
}
private void getUserDetailsAfterFbSignin(String fbaccesstoken)
{
	msylProgressDialog = new ProgressDialog(SYLSigninActivity.this);
	msylProgressDialog.setMessage("Please wait...");
	msylProgressDialog.setCancelable(false);
	msylProgressDialog.setCanceledOnTouchOutside(false);
	msylProgressDialog.show();
	String gmttimezone=SYLUtil.getTimeGMTZone(SYLSigninActivity.this);
	String deviceUID=SYLUtil.getUID(SYLSigninActivity.this);
	SYLFacebookSigninViewManager mViewmanager=new SYLFacebookSigninViewManager();
	mViewmanager.msylsigninlistener=SYLSigninActivity.this;
	mViewmanager.doFacebookSignin(fbaccesstoken, deviceUID, "android", gmttimezone);
}

	private void restoreProgress(Bundle savedInstanceState) {
		waiting = savedInstanceState.getBoolean("waiting");

		if (waiting) {
			if (Dialog != null) {
				ProgressDialog refresher = (ProgressDialog) Dialog.get();
				refresher.dismiss();
				msylProgressDialog = new ProgressDialog(SYLSigninActivity.this);
				msylProgressDialog.setMessage("Please wait...");
				msylProgressDialog.show();
				Dialog = new WeakReference<ProgressDialog>(msylProgressDialog);
			}
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean("waiting", waiting);
		outState.putString("alertmessageshowingverification", alertmessageshowingverification);
		outState.putString("alertmessageverification", alertmessageverification);
		outState.putString("alertmessageshowing", alertmessageshowing);
		outState.putString("alertmessage", alertmessage);
	}

	private void buildAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
												final int id) {
alertmessageshowing="false";
								dialog.cancel();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}

}
