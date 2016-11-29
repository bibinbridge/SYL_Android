package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLDeleteHistoryAppoitmentsListener;
import com.webcamconsult.syl.model.SYLAppointmentRemoveResponseModel;
import com.webcamconsult.syl.modelmanager.SYLDeleteHistoryAppointmentsModelmanager;

/**
 * Created by Sandeep on 11/27/2015.
 */
public class SYLDeleteHistoryAppointmentsViewmanager implements SYLDeleteHistoryAppoitmentsListener {
public SYLDeleteHistoryAppoitmentsListener mDeleteHistoryAppointmentsListener;
public void deleteHistoryAppointments(String accesstoken,String timezone)
{
    SYLDeleteHistoryAppointmentsModelmanager mmodelmanager=new SYLDeleteHistoryAppointmentsModelmanager();
    mmodelmanager.mdeleteHistoryAppointmentsListener=SYLDeleteHistoryAppointmentsViewmanager.this;
    mmodelmanager.deleteHistoryAppointments(accesstoken,timezone);
}



    public void deleteHistoryAppointmentsFinish(SYLAppointmentRemoveResponseModel msylappointmentremoveResponseModel)
    {
    mDeleteHistoryAppointmentsListener.deleteHistoryAppointmentsFinish(msylappointmentremoveResponseModel);
    }
}
