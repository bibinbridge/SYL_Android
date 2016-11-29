package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLAppointmentDetailsListner;
import com.webcamconsult.syl.model.SYLAppointmentsDescription;
import com.webcamconsult.syl.modelmanager.SYLAppointmentsDetailsModelManager;



public class SYLAppointmentsDetailsManager implements SYLAppointmentDetailsListner
{
	
	public SYLAppointmentDetailsListner sYLAppointmentDetailsListner;

	public void getAppointments(String method, String userID, String listclick) 
	{
		SYLAppointmentsDetailsModelManager sYLAppointmentsDetailsModelManager = new SYLAppointmentsDetailsModelManager();
		sYLAppointmentsDetailsModelManager.sYLAppointmentDetailsListner=SYLAppointmentsDetailsManager.this;
		sYLAppointmentsDetailsModelManager.getAppointments(method,userID,listclick);
		
	}

	@Override
	public void onDidFinished(
			SYLAppointmentsDescription sYLAppointmentsDescription)
	{
		sYLAppointmentDetailsListner.onDidFinished(sYLAppointmentsDescription);
		
	}
	
	

}
