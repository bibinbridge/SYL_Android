package com.webcamconsult.viewmanager;

import android.content.Context;

import com.webcamconsult.syl.interfaces.SYLVerifyMobileNumberListner;
import com.webcamconsult.syl.model.SYLVerifyMobileDetails;
import com.webcamconsult.syl.modelmanager.SYLVerifyMobileNumberModelManager;



public class SYLVerifyMobileNumberManager implements SYLVerifyMobileNumberListner
{
	public SYLVerifyMobileNumberListner iSYLVerifyMobileNumberListner;

	public void verifyMobileNumber(Context mContext, int userid,
			String verifycode, String mobilenumber, String method) 
	{
		
		SYLVerifyMobileNumberModelManager sYLVerifyMobileNumberModelManager = new SYLVerifyMobileNumberModelManager();
		sYLVerifyMobileNumberModelManager.iSYLVerifyMobileNumberListner=SYLVerifyMobileNumberManager.this;
		sYLVerifyMobileNumberModelManager.verifyMobileNumber(mContext,userid,verifycode,mobilenumber,method);
	}

	@Override
	public void onDidFinished(SYLVerifyMobileDetails sYLVerifyMobileDetails) 
	{
		iSYLVerifyMobileNumberListner.onDidFinished(sYLVerifyMobileDetails);
		
	}


}
