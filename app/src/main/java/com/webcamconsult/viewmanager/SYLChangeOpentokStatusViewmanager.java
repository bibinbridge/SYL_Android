package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLChangeconferenceListener;
import com.webcamconsult.syl.model.SYLChangeConferenceResponeModel;
import com.webcamconsult.syl.modelmanager.SYLChangeOpentokstatusModelManager;

public class SYLChangeOpentokStatusViewmanager implements SYLChangeconferenceListener {
public SYLChangeconferenceListener mConferenceChangeListener;

public void changeOpentokStatus(String statuscode,String timezone,String appointmentid)
{
	SYLChangeOpentokstatusModelManager mchangeopentokstatusmodelmanager=new SYLChangeOpentokstatusModelManager();
	mchangeopentokstatusmodelmanager.mChangeConferenceListener=SYLChangeOpentokStatusViewmanager.this;
	mchangeopentokstatusmodelmanager.changeOpentokStatus(statuscode, timezone, appointmentid);
}
	@Override
	public void finishConferenceStstus(
			SYLChangeConferenceResponeModel msylChangeConferencemodel) {
		// TODO Auto-generated method stub
		mConferenceChangeListener.finishConferenceStstus(msylChangeConferencemodel);
	}

}
