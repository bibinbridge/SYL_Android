package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLAppointmentRemoveListener;
import com.webcamconsult.syl.interfaces.SYLContactUnfriendListener;
import com.webcamconsult.syl.model.SYLAppointmentRemoveResponseModel;
import com.webcamconsult.syl.model.SYLContactUnfriendResponseModel;
import com.webcamconsult.syl.modelmanager.SYLAppointmentRemoveModelManager;
import com.webcamconsult.syl.modelmanager.SYLContactsUnfriendModelManager;

public class SYLAppointmentRemoveViewManager implements SYLAppointmentRemoveListener{
	public SYLAppointmentRemoveListener  msylappointmentremoveListener;
public void doRemoveAppointment(String appointmentid,String accesstoken,String timezone)
{
	SYLAppointmentRemoveModelManager  mremoveModelManager=new SYLAppointmentRemoveModelManager();
	mremoveModelManager.mAppointmentremoveListener=SYLAppointmentRemoveViewManager.this;
	mremoveModelManager.doRemoveAppointment(appointmentid, accesstoken, timezone);
}
@Override
public void finishAppointmentRemove(
		SYLAppointmentRemoveResponseModel msylappointmentremoveResponseModel) {
	// TODO Auto-generated method stub
	msylappointmentremoveListener.finishAppointmentRemove(msylappointmentremoveResponseModel);
	
}


}
