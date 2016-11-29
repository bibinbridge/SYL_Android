package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLCreatenewAppointmentListener;
import com.webcamconsult.syl.model.SYLCreateNewAppointmentResponse;
import com.webcamconsult.syl.modelmanager.SYLCreateNewAppointmentModelManager;

public class SYLCreateNewAppointmentViewManager implements
		SYLCreatenewAppointmentListener {

	public SYLCreatenewAppointmentListener msylcreatenewappointmentlistener;

	public void createnewAppointment(int userid, String importedfrom,String fullname,String mobile,String email,String topic,
			String description, String appointmentPriority1,
			String appointmentPriority2, String appointmentPriority3,
			String appointmentDate1, String appointmentDate2,
			String appointmentDate3, String timeZone, String accessToken,String participantuserid,String appointmentid,String url) {
		SYLCreateNewAppointmentModelManager mmodelmanager = new SYLCreateNewAppointmentModelManager();
		mmodelmanager.msylcreatenewappointmentlistener=SYLCreateNewAppointmentViewManager.this;
		mmodelmanager.createnewAppointment(userid, importedfrom,fullname,mobile,email,topic, description,
				appointmentPriority1, appointmentPriority2,
				appointmentPriority3, appointmentDate1, appointmentDate2,
				appointmentDate3, timeZone, accessToken,participantuserid,appointmentid,url);

	}

	@Override
	public void getCreatenewAppointmentFinish(
			SYLCreateNewAppointmentResponse msylcreatenewappointmentresponse) {
		// TODO Auto-generated method stub
		msylcreatenewappointmentlistener
				.getCreatenewAppointmentFinish(msylcreatenewappointmentresponse);
	}
}
