package com.webcamconsult.syl.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			ComponentName comp = new ComponentName(context.getPackageName(),
					SYLGCMNotificationIntentService.class.getName());
			startWakefulService(context, (intent.setComponent(comp)));
			//setResultCode(Activity.RESULT_OK);
		}
		catch (Exception e)
		{

		}
	}

}
