package com.webcamconsult.syl.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.interfaces.SYLForgotpasswordListener;
import com.webcamconsult.syl.model.SYLForgotPasswordResponseModel;
import com.webcamconsult.syl.modelmanager.SYLForgetPasswordModelManager;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLForgotPasswordViewManager;
import com.webcamconsult.viewmanager.SYLSignupviewManager;

public class SYLForgotpasswordActivity extends Activity implements
        SYLForgotpasswordListener {
    private TextView txtview_forgotpasswordheading;
    private Button btn_cancel, btn_reset;
    private EditText etxt_email;
    private ImageView img_backarrow;
    String forgotpasswordemailaddress;
    String responsemessage;
    ProgressDialog msylProgressDialog;
    int popup_open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syl_forgotpasswordlayout);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txtview_forgotpasswordheading = (TextView) findViewById(R.id.txt_heading);
        etxt_email = (EditText) findViewById(R.id.etxt_email);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        img_backarrow = (ImageView) findViewById(R.id.img_backarrow);
        Typeface font = Typeface.createFromAsset(this.getAssets(),
                "fonts/myriadproregular.OTF");
        txtview_forgotpasswordheading.setTypeface(font);
        btn_cancel.setTypeface(font);
        btn_reset.setTypeface(font);
        etxt_email.setTypeface(font);
        img_backarrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                navigatetoSigninScreen();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigatetoSigninScreen();
            }
        });
    }

    /* Method for navigating the screen to Signin Screen */
    private void navigatetoSigninScreen() {

        Intent signinIntent = new Intent(SYLForgotpasswordActivity.this,
                SYLSigninActivity.class);
        SYLForgotpasswordActivity.this.startActivity(signinIntent);
        finish();
    }

    public void doForgetPassword(View v) {
        hideKeyboard();

        if (validate()) {
            if (SYLUtil.isNetworkAvailable(SYLForgotpasswordActivity.this)) {
                msylProgressDialog = new ProgressDialog(SYLForgotpasswordActivity.this);
                msylProgressDialog.setMessage("Please wait...");
                msylProgressDialog.show();
                forgotpasswordemailaddress = etxt_email.getText().toString();
                String GMTtimezone = SYLUtil.getTimeGMTZone(SYLForgotpasswordActivity.this);
                SYLForgotPasswordViewManager mviewmanager = new SYLForgotPasswordViewManager();
                mviewmanager.mforgotpasswordlistener = SYLForgotpasswordActivity.this;
                mviewmanager.doForgotPassword(forgotpasswordemailaddress, GMTtimezone);
            } else {
                SYLUtil.buildAlertMessage(SYLForgotpasswordActivity.this,
                        "Please check your network connection");
            }
        }
    }

    @Override
    public void onFinishForgotPassword(
            SYLForgotPasswordResponseModel msylforgotpasswordresponsemodel) {
        // TODO Auto-generated method stub
        if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
            msylProgressDialog.dismiss();
        }
        if (msylforgotpasswordresponsemodel != null) {

            if (msylforgotpasswordresponsemodel.isSuccess()) {
                responsemessage = "Your new password successfully sent to "
                        + forgotpasswordemailaddress;
            } else {
                responsemessage = msylforgotpasswordresponsemodel.getError()
                        .getErrorDetail();
            }
        } else {
            responsemessage = "Unexpeted server error occured";
        }
        buildAlertMessage(SYLForgotpasswordActivity.this,
                responsemessage);
    }

    private void buildAlertMessage(Context context, final String message) {
        popup_open = 1;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                                if (message.contains("Your new password successfully sent to ")) {
                                    navigatetoSigninScreen();
                                    dialog.cancel();
                                    ;
                                }

                                dialog.cancel();
                                popup_open = 0;
                            }
                        });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean validate() {
        boolean valid = true;

        if (!SYLUtil.validateEmail(etxt_email)) {
            valid = false;

            etxt_email.requestFocus();
            etxt_email.setError("Please enter a valid email");
            // mEmailEdit.setError(getString(R.string.validate_username,R.drawable.excailm));
            etxt_email.requestFocus();
        }

        return valid;

    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("responsemessage_value", responsemessage);
        outState.putInt("popup_value", popup_open);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        responsemessage = savedInstanceState.getString("responsemessage_value");
        if (savedInstanceState.getInt("popup_value") == 1)
            buildAlertMessage(SYLForgotpasswordActivity.this,
                    savedInstanceState.getString("responsemessage_value"));
    }

}
