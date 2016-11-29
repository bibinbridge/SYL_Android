package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLSendVerificationcodeListener;
import com.webcamconsult.syl.model.SYLVerificationcodeResponseModel;
import com.webcamconsult.syl.modelmanager.SYLSendVerifivationcodemodelmanager;

public class SYLSendVerificationcodeViewmanager implements
		SYLSendVerificationcodeListener {
	public SYLSendVerificationcodeListener msylsendverificationlistener;

	public void sendVerifivationcode(String verificationcode, String email,
			String accesstoken,String timezone) {
		SYLSendVerifivationcodemodelmanager mmodelmanager = new SYLSendVerifivationcodemodelmanager();
		mmodelmanager.msendverificationcodelistener=SYLSendVerificationcodeViewmanager.this;
		mmodelmanager
				.sendverificationcode(verificationcode, email, accesstoken,timezone);
	}

	@Override
	public void onDidFinished(
			SYLVerificationcodeResponseModel msylverificationcoderesponse) {
		// TODO Auto-generated method stub
		msylsendverificationlistener
				.onDidFinished(msylverificationcoderesponse);
	}
}
