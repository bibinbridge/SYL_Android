package com.webcamconsult.syl.activities;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
/*
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
*/

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.fragments.SYLAppointmentsFragment;
import com.webcamconsult.syl.fragments.SYLAppointmentsiniFragment;

import com.webcamconsult.syl.fragments.SYLContactsFragment;
import com.webcamconsult.syl.gcm.SYLGCMNotificationIntentService;
import com.webcamconsult.syl.interfaces.SYLFacebookTokenListener;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLFragmentChangeActivity extends SYLBaseActivity {
    public SYLFacebookTokenListener mFacebooktokenListener;

    private Fragment mContent;
    public String socialmediatype = "";
    String fragment_value;
    private static final String TAG = "RetrieveAccessToken";
    private static final int REQ_SIGN_IN_REQUIRED = 55664;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    private String mAccountName;

    public SYLFragmentChangeActivity() {
        super(R.string.changing_fragments);
    }

    BroadcastReceiver br;

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


        String fragmenttag = null;
        // set the Above View
        // checkForLongRunning();
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fragment_value = getIntent().getStringExtra("fragmentvalue");
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        if (mContent == null) {

            mContent = new SYLAppointmentsiniFragment();

            if (fragment_value.equals("newappointment")) {
                mContent = new SYLAppointmentsiniFragment();

                fragmenttag = "appointments";
                Bundle refreshBundle=new Bundle();
                refreshBundle.putString("needrefresh", getIntent().getStringExtra("needrefresh"));
                mContent.setArguments(refreshBundle);
            } else if (fragment_value.equals("newappointmentimportcontact")) {
                mContent = new SYLContactsFragment();
                fragmenttag = "contacts";
                Bundle args = new Bundle();
                args.putString("intentfrom", "newappointmentimportcontact");
                Bundle detailsbundle = getIntent().getBundleExtra("detailsbundle");
                args.putBundle("detailsbundle", detailsbundle);
                mContent.setArguments(args);
            } else if (fragment_value.equals("menufragment")) {
                mContent = new SYLContactsFragment();
                fragmenttag = "contacts";
                Bundle args = new Bundle();
                args.putString("intentfrom", "menufragment");
                mContent.setArguments(args);
            }
        }
        // set the Above View
        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent, fragmenttag).commit();

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new SYLMenuFragment()).commit();

        // customize the SlidingMenu
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

    }

    /* Method For Handling Facebook Login */

    /*
    public void doFacebookLogin(String socialmediatype) {
        this.socialmediatype = socialmediatype;
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
                                        Log.e("Success", user.getName());
                                        Session s = Session.getActiveSession();
                                        Log.e("token", s.getAccessToken());
                                        Log.e("Name", user.getFirstName());
                                        mFacebooktokenListener
                                                .onfacebooktokenGetFinish(s
                                                        .getAccessToken());

                                    }
                                }
                            }).executeAsync();
                }
            }
        });

    }
*/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void switchContent(Fragment fragment, String identifier) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, identifier).commit();
        getSlidingMenu().showContent();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     /*   if (this.socialmediatype.equals("googleplus")) {
            new RetrieveTokenTask().execute(mAccountName);
        } else if (this.socialmediatype.equals("facebook")) {
            Session.getActiveSession().onActivityResult(
                    SYLFragmentChangeActivity.this, requestCode, resultCode,
                    data);
        } else {
            return;
        }
        */
    }

    /*
     * We need to startactivity again when using this method. So we implemented
     * AsyncTask here
     */
    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            String scopes = "oauth2:profile email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(),
                        accountName, scopes);


            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    public void getAccesToken(String socialmediatype) {
        this.socialmediatype = socialmediatype;
        mAccountName = SYLUtil.getAccountName(SYLFragmentChangeActivity.this);
        if (mAccountName == null) {
            SYLUtil.buildAlertMessage(SYLFragmentChangeActivity.this,
                    "Your device does not seems to have a valid Gmail Account");
        } else {
            new RetrieveTokenTask().execute(mAccountName);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SlidingMenu sm = getSlidingMenu();
            if (sm.isMenuShowing()) {
                Log.e("menu showing", "menu showing");
                sm.showContent(true);
                return true;
            } else {
                String h = mContent.getTag().toString();
                //       	Toast.makeText(getApplicationContext(),mContent.getTag().toString(), Toast.LENGTH_SHORT).show();
                if (mContent.getTag().toString().equals("contacts")) {
                    mContent = new SYLAppointmentsFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, mContent, "appointments").commit();
                } else if (mContent.getTag().toString().equals("profile") || mContent.getTag().toString().equals("settings")) {
                    mContent = new SYLAppointmentsFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, mContent, "appointments").commit();
                } else {
                    showExit("Are you sure want to exit from SYL app?");
                }
            }

        }
        return true;
    }

    private void showExit(String message) {

        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(
                this);
        mAlertDialog.setTitle(R.string.app_name);
        mAlertDialog.setIcon(getResources().getDrawable(R.drawable.syl_applaunchericon));

        mAlertDialog.setMessage(message);
        mAlertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        callFinishMethod();
//finish();

                    }
                });


        mAlertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        // mAlertDialog.setNegativeButton(getString(android.R.string.cancel),null);
        mAlertDialog.create().show();
    }

    private void callFinishMethod() {
     /*   Intent intent = new Intent(getApplicationContext(), SYLSigninActivity.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
*/
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
        sendBroadcast(broadcastIntent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }





}