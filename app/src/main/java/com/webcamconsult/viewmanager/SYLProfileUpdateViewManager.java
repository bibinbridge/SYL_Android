package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLSignupListener;
import com.webcamconsult.syl.model.SYLSignupResponseModel;
import com.webcamconsult.syl.modelmanager.SYLProfileUpdateModelManager;

public class SYLProfileUpdateViewManager implements SYLSignupListener{
public SYLSignupListener mSylSignupListener;

public void doProfileUpdate(String name,String email,String facetimeid,String skypeid,String facebookid,String googleid,String mobilenumber,String countrycode,String timezone,String devicetoken,String latitude,String longitude,String isprofileimageuploaded,String file_path,String accesstoken)
{
	SYLProfileUpdateModelManager mProfileupdatemanager=new SYLProfileUpdateModelManager();
	mProfileupdatemanager.msylSignupListener=SYLProfileUpdateViewManager.this;
	mProfileupdatemanager.doProfileUpdate(name, email, facetimeid, skypeid, facebookid, googleid, mobilenumber, countrycode, timezone, devicetoken, latitude, longitude, isprofileimageuploaded, file_path, accesstoken);
}
	@Override
	public void onDidFinished(SYLSignupResponseModel msylsignupresponsemodel) {
		// TODO Auto-generated method stub
		mSylSignupListener.onDidFinished(msylsignupresponsemodel);
	}

}
