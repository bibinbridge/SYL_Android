package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLFetchActiveSessionListener;
import com.webcamconsult.syl.model.SYLFetchActiveSessionResponeModel;
import com.webcamconsult.syl.modelmanager.SYLFetchActiveSessionModelManager;

public class SYLFetchActiveSessionViewManager implements SYLFetchActiveSessionListener{

	public SYLFetchActiveSessionListener mActiveSessionListener;
	
	public void fetchActiveSession(String appointmentid,String accesstoken,String timezone)
{
	SYLFetchActiveSessionModelManager mActivesessionmodelmamager=new SYLFetchActiveSessionModelManager();
	mActivesessionmodelmamager.mActiveSessionListener=SYLFetchActiveSessionViewManager.this;
	mActivesessionmodelmamager.fetchActiveSession(appointmentid, accesstoken, timezone);


}

@Override
public void fetchActiveSessionFinish(
		SYLFetchActiveSessionResponeModel mSessionResponseModel) {
	// TODO Auto-generated method stub
	mActiveSessionListener.fetchActiveSessionFinish(mSessionResponseModel);
}
}
