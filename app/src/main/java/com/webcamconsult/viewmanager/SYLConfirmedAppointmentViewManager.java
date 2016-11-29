package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLConfirmAppointmentListener;
import com.webcamconsult.syl.model.SYLConfirmRequestResponseModel;
import com.webcamconsult.syl.modelmanager.SYLConfirmedAppointmentModelManager;

public class SYLConfirmedAppointmentViewManager  implements SYLConfirmAppointmentListener {
	public SYLConfirmAppointmentListener mSylConfirmAppointmentListener;
public void doconfirmAppointment(String appointmentid,String accesstoken,boolean flag,String datetime,String selectedoption,String timezone,int selectedoptionindex,int selecteddateindex)
{
	SYLConfirmedAppointmentModelManager mmodelmanager=new SYLConfirmedAppointmentModelManager();
	mmodelmanager.mSylConfirmAppointmentListener=SYLConfirmedAppointmentViewManager.this;
	mmodelmanager.doConfirmAppointment(appointmentid, accesstoken, flag, datetime, selectedoption,timezone,selectedoptionindex,selecteddateindex);

}

@Override
public void getConfirmAppointmentFinish(
		SYLConfirmRequestResponseModel mconfirmrequestResponseModel) {
	// TODO Auto-generated method stub
	mSylConfirmAppointmentListener.getConfirmAppointmentFinish(mconfirmrequestResponseModel);
}
}
