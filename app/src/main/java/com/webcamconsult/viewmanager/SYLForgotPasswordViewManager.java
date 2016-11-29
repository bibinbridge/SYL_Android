package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLForgotpasswordListener;
import com.webcamconsult.syl.model.SYLForgotPasswordResponseModel;
import com.webcamconsult.syl.modelmanager.SYLForgetPasswordModelManager;

public class SYLForgotPasswordViewManager implements SYLForgotpasswordListener {
public SYLForgotpasswordListener mforgotpasswordlistener;
public void doForgotPassword(String forgotpasswordemailaddress,String gmttimezone)
{
	SYLForgetPasswordModelManager mmodelmanager=new SYLForgetPasswordModelManager();
	mmodelmanager.mforgetpasswordlistener=SYLForgotPasswordViewManager.this;
	mmodelmanager.doForgetPassword(forgotpasswordemailaddress,gmttimezone);
	
}
@Override
public void onFinishForgotPassword(
		SYLForgotPasswordResponseModel msylforgotpasswordresponsemodel) {
	// TODO Auto-generated method stub
	mforgotpasswordlistener.onFinishForgotPassword(msylforgotpasswordresponsemodel);
}
}
