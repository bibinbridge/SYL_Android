package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLFetchSessionStatusListener;
import com.webcamconsult.syl.model.SYLFetchSessionAppointmentResponseModel;
import com.webcamconsult.syl.modelmanager.SYLFetchSessionStatusModelManager;

public class SYLFetchSessionStatusViewManager implements SYLFetchSessionStatusListener{
public 	SYLFetchSessionStatusListener msylFetchSessionStatusListener;
	public void fetchSessionStatus(String appointmentid,String accesstoken,String timezone)
	{
		SYLFetchSessionStatusModelManager mSessionStatusModelManager=new SYLFetchSessionStatusModelManager();
		mSessionStatusModelManager.fetchSessionStatusForAppointment(appointmentid, accesstoken, timezone);
		mSessionStatusModelManager.mFetchSessionStatusListener=SYLFetchSessionStatusViewManager.this;
	}
	
	
	@Override
	public void finishFetchSessionAppointmentStatus(
			SYLFetchSessionAppointmentResponseModel mSylFetchsessionappointmentresponsemodel) {
		// TODO Auto-generated method stub
		msylFetchSessionStatusListener.finishFetchSessionAppointmentStatus(mSylFetchsessionappointmentresponsemodel);
	}

}
