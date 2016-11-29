package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLSigninListener;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.modelmanager.SYLFacebookModelManager;
import com.webcamconsult.syl.modelmanager.SYLSigninModelManager;

/**
 * Created by Sandeep on 8/24/2015.
 */
public class SYLFacebookSigninViewManager implements SYLSigninListener {


    public SYLSigninListener msylsigninlistener;
    public void doFacebookSignin(String fbAccessToken,String deviceUid,String deviceType,String timeZone)
    {
        SYLFacebookModelManager mmodelmanager=new SYLFacebookModelManager();
        mmodelmanager.mSylSigninListener=SYLFacebookSigninViewManager.this;
        mmodelmanager.doFacebookSignin(fbAccessToken,deviceUid,deviceType,timeZone);

    }











    @Override
    public void onDidFinished(SYLSigninResponseModel msylsigninresponsemodel) {
        // TODO Auto-generated method stub
        msylsigninlistener.onDidFinished(msylsigninresponsemodel);
    }
}
