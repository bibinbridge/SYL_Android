package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLSigninListener;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.modelmanager.SYLSigninModelManager;

public class SYLSigninViewManager implements SYLSigninListener{

	public SYLSigninListener msylsigninlistener;
	public void doUserSignin(String email,String password,String gmttimezone,String deviceUID)
	{
		SYLSigninModelManager mmodelmanager=new SYLSigninModelManager();
		mmodelmanager.mSylSigninListener=SYLSigninViewManager.this;
		mmodelmanager.doUserSignin(email,password,gmttimezone,deviceUID);
	}
	
	
	
	
	
	
	@Override
	public void onDidFinished(SYLSigninResponseModel msylsigninresponsemodel) {
		// TODO Auto-generated method stub
		msylsigninlistener.onDidFinished(msylsigninresponsemodel);
	}

}
