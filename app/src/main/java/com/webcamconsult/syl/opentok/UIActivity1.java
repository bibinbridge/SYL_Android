package com.webcamconsult.syl.opentok;

import android.app.Activity;
import android.os.Bundle;

import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Session.SessionListener;
import com.opentok.android.Stream;
import com.webcamconsult.syl.R;

public class UIActivity1 extends  Activity implements SessionListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.requestsend_detail);
	}
	@Override
	public void onConnected(Session arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected(Session arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Session arg0, OpentokError arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStreamDropped(Session arg0, Stream arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStreamReceived(Session arg0, Stream arg1) {
		// TODO Auto-generated method stub
		
	}

}
