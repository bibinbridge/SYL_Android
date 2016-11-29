package com.webcamconsult.syl.gcm;

import com.webcamconsult.syl.activities.SYLSignupActivity;
import com.webcamconsult.syl.opentok.UIActivity;
import com.webcamconsult.syl.opentok.UIActivity1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class CallBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e("onreceive","on receive");
		 Toast.makeText(context, "Action:", Toast.LENGTH_SHORT).show();
		 Intent acceptrejectintent=new Intent(context,com.webcamconsult.syl.opentok.SYLOpentokAcceptRejectActivity.class);
		 acceptrejectintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 acceptrejectintent.putExtra("sessionid", intent.getExtras().getString("sessionid"));
		 acceptrejectintent.putExtra("token",  intent.getExtras().getString("token"));
		 acceptrejectintent.putExtra("apikey",  intent.getExtras().getString("apikey"));
		 acceptrejectintent.putExtra("name",  intent.getExtras().getString("name"));
		 acceptrejectintent.putExtra("profileimageurl",  intent.getExtras().getString("profileimageurl"));
		 acceptrejectintent.putExtra("appointmentid",  intent.getExtras().getString("appointmentid"));
		 context.startActivity(acceptrejectintent);
		 
	}

}
