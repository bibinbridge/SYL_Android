package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLOpentokconfigureListener;
import com.webcamconsult.syl.model.SYLOpentokconfigureResponse;
import com.webcamconsult.syl.modelmanager.SYLOpentokModelManager;

public class SYLOpentokConfigureViewManager implements SYLOpentokconfigureListener {
	public SYLOpentokconfigureListener mOpentokconfigurelistener;
public void configureOpentok(String appointmentid,String userid,String accesstoken,String timezone)
{
	SYLOpentokModelManager opentokconfiguremodelmanager=new SYLOpentokModelManager();
	opentokconfiguremodelmanager.sylopentokconfigurelistener=SYLOpentokConfigureViewManager.this;
opentokconfiguremodelmanager.configureOpentok(appointmentid, userid, accesstoken, timezone);
	}

@Override
public void opentokconfigureFinish(
		SYLOpentokconfigureResponse mopentokConfigureresponse) {
	// TODO Auto-generated method stub
	mOpentokconfigurelistener.opentokconfigureFinish(mopentokConfigureresponse);
}
}
