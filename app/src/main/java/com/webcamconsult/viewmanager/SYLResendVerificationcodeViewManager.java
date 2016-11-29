package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLResendverificationListener;
import com.webcamconsult.syl.model.SYLResendVerificationResponsemodel;
import com.webcamconsult.syl.modelmanager.SYLresendVerificationcodeModelManager;

public class SYLResendVerificationcodeViewManager implements SYLResendverificationListener {
public SYLResendverificationListener msylresendverificationlistener;
	
	public void doresendVerificationcode(String sylaccesstoken,String gmttimezone)
{
	SYLresendVerificationcodeModelManager mmodelmanager=new SYLresendVerificationcodeModelManager();
	mmodelmanager.mresendverificationlistener=SYLResendVerificationcodeViewManager.this;
	mmodelmanager.doResendverificationcode(sylaccesstoken,gmttimezone);
}

	@Override
	public void onresendverificationFinish(
			SYLResendVerificationResponsemodel mresendverificationresponsemodel) {
		// TODO Auto-generated method stub
		msylresendverificationlistener.onresendverificationFinish(mresendverificationresponsemodel);
	}
}
