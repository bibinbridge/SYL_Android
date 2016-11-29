package com.webcamconsult.syl.gcm;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.interfaces.SYLOpentoksessionFinishListener;
import com.webcamconsult.syl.opentok.send.HelloWorldActivityReceiver;

public class NotificationReceiverActivity extends Activity {
    TextView alertContent;
    Button btn_ok;
public SYLOpentoksessionFinishListener mOpentokFinishListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("notification activity", "notification activity");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notification_dialog);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        alertContent= (TextView)findViewById(R.id.alertContent) ;
        btn_ok= (Button)findViewById(R.id.okbtn) ;
        String msg=getIntent().getExtras().getString("message");
        alertContent.setText(msg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                navigatetoAppointmentsList();

            }
        });
    }

private void navigatetoAppointmentsList()
{

    Intent signinIntent = new Intent(NotificationReceiverActivity.this,
            SYLFragmentChangeActivity.class);

    signinIntent.putExtra("fragmentvalue", "newappointment");
    signinIntent.putExtra("needrefresh", "true");
    //signinIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    NotificationReceiverActivity.this.startActivity(signinIntent);
    Intent broadcastIntent = new Intent();
    broadcastIntent.setAction("com.package.CALLEND");
    sendBroadcast(broadcastIntent);
    finish();

  //  finish();
}
 public void cancel(View v)
 {
     NotificationReceiverActivity.this.finish();
 }

}
