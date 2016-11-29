package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLSignupListener;
import com.webcamconsult.syl.model.SYLSignupResponseModel;
import com.webcamconsult.syl.modelmanager.SYLSignupModelManager;

import android.content.Context;

public class SYLSignupviewManager implements SYLSignupListener {
	
	public SYLSignupListener msylsignuplistener;

public void doSignup(Context c,String name,String email, String password, String facetimeid,String skypeid,String facebookid,String googleid,String mobilenumber,String countrycode,String timezone,String devicetoken,String latitude,String longitude,String isProfileimageupload,String imagepath)

{
	SYLSignupModelManager msignupmodelmanager=new SYLSignupModelManager();
	msignupmodelmanager.msylsignuplistener=SYLSignupviewManager.this;
	msignupmodelmanager.doSignup(name,email,password,facetimeid,skypeid,facebookid,googleid,mobilenumber,countrycode,timezone,devicetoken,latitude,longitude,isProfileimageupload,imagepath);

}

@Override
public void onDidFinished(SYLSignupResponseModel msylsignupresponsemodel) {
	// TODO Auto-generated method stub
	msylsignuplistener.onDidFinished(msylsignupresponsemodel);
}


}
