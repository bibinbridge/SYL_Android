package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLFetchUserDetailsListener;
import com.webcamconsult.syl.model.SYLFetchUserDetailsResponseModel;
import com.webcamconsult.syl.modelmanager.SYLFetchUserProfileDetailsModelManager;

public class SYLFetchUserDetailsViewManager implements SYLFetchUserDetailsListener{

	public SYLFetchUserDetailsListener mFetchUserDetailsListener;
	
	public void fetchUserDetails(String accessToken,String timezone)
	{
		SYLFetchUserProfileDetailsModelManager mmodelmanager=new SYLFetchUserProfileDetailsModelManager();
		mmodelmanager.mFetchUserdetailsListener=SYLFetchUserDetailsViewManager.this;
		mmodelmanager.getUserDetails(accessToken,timezone);
	}
	
	
	
	
	@Override
	public void getUserDetailsFinish(
			SYLFetchUserDetailsResponseModel muserresponsemodel) {
		// TODO Auto-generated method stub
		mFetchUserDetailsListener.getUserDetailsFinish(muserresponsemodel);
	}

}
