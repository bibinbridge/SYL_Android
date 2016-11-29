package com.webcamconsult.syl.interfaces;

import com.webcamconsult.syl.model.SYLFetchAppointmentsResponseModel;

/**
 * Created by Sandeep on 11/24/2015.
 */
public interface SYLFetchHistoryAppointmentsListener {
    void getHistoryAppointmentDetailsFinish(SYLFetchAppointmentsResponseModel mFetchappointmentsResponseModel);
}
