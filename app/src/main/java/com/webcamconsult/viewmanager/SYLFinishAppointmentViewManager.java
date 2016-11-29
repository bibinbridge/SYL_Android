package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLCancelAppointmentListener;
import com.webcamconsult.syl.interfaces.SYLFinishedAppointmentListener;
import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.model.SYLFinishAppointmentResponseModel;
import com.webcamconsult.syl.modelmanager.SYLCancelAppointmentModelManager;
import com.webcamconsult.syl.modelmanager.SYLFinishAppointmentModelManager;

public class SYLFinishAppointmentViewManager implements SYLFinishedAppointmentListener{
public SYLFinishedAppointmentListener  mFinishAppointmentListener;
public void doFinishAppointment(String accesstoken, String appointmentid,String timezone)
{
	SYLFinishAppointmentModelManager mfinishappointmentmodelmanager=new SYLFinishAppointmentModelManager();
	mfinishappointmentmodelmanager.mFinishedAppointmentListener= SYLFinishAppointmentViewManager.this;
	mfinishappointmentmodelmanager.doFinishAppoitment(accesstoken, appointmentid, timezone);
	}

	@Override
	public void finishAppointment(SYLFinishAppointmentResponseModel mFinishappointmentresponsemodel) {
	mFinishAppointmentListener.finishAppointment(mFinishappointmentresponsemodel);
	}
}
