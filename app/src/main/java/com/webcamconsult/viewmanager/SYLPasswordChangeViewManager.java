package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLPasswordChangeListener;
import com.webcamconsult.syl.model.SYLPasswordChangeResponseModel;
import com.webcamconsult.syl.modelmanager.SYLPasswordChangeModelmanager;

public class SYLPasswordChangeViewManager implements SYLPasswordChangeListener{
public SYLPasswordChangeListener mPasswordChangeListener;
public void doPasswordChange(String userid,String sylaccesstoken,String oldpassword,String newpassword,String timezone)
{
	SYLPasswordChangeModelmanager mmodelmanager=new SYLPasswordChangeModelmanager();
	mmodelmanager.mSylpasswordchangelistener=SYLPasswordChangeViewManager.this;
	mmodelmanager.doPasswordChange(userid, sylaccesstoken, oldpassword, newpassword,timezone);
	
}
@Override
public void finishPasswordChange(
		SYLPasswordChangeResponseModel mPasswordchangeresponsemodel) {
	// TODO Auto-generated method stub
	mPasswordChangeListener.finishPasswordChange(mPasswordchangeresponsemodel);
}
}
