package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLCancelAppointmentListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.modelmanager.SYLCancelAppointmentModelManager;

public class SYLCancelAppointmentViewManager implements SYLCancelAppointmentListener{
public SYLCancelAppointmentListener mCancelAppointmentListener;
public void doCancelAppointment(String accesstoken, String appointmentid,String timezone)
{
	SYLCancelAppointmentModelManager mcancelappointmentmodelmanager=new SYLCancelAppointmentModelManager();
	mcancelappointmentmodelmanager.mCancelAppointmentListener= SYLCancelAppointmentViewManager.this;
	mcancelappointmentmodelmanager.doCancelAppointment(accesstoken, appointmentid,timezone);
	}
@Override
public void finishCancelAppointment(
		SYLCancelAppointmentModel mcancelappointmentmodel) {
	// TODO Auto-generated method stub
	mCancelAppointmentListener.finishCancelAppointment(mcancelappointmentmodel);
}
}
