package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLNotificationListener;
import com.webcamconsult.syl.model.SYLNotificationResponseModel;
import com.webcamconsult.syl.modelmanager.SYLNotificationStatusModelmanager;

public class SYLNotificationViewManager implements SYLNotificationListener{
public SYLNotificationListener mNotificationListener;
	public void dosylNotificationChange(String userid,boolean isActivated,String deviceUID,String timezone)
	{
		
		SYLNotificationStatusModelmanager mmodelmanager=new SYLNotificationStatusModelmanager();
		mmodelmanager.mNotificationListener=SYLNotificationViewManager.this;
		mmodelmanager.doNotifivationChange(userid, isActivated, deviceUID,timezone);
	}
	
	@Override
	public void finishsylNotificationStatus(
			SYLNotificationResponseModel mNotificationResponseModel) {
		// TODO Auto-generated method stub
		mNotificationListener.finishsylNotificationStatus(mNotificationResponseModel);
	}

}
