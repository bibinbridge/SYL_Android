package com.webcamconsult.syl.interfaces;

import com.webcamconsult.syl.model.SYLCancelAppointmentModel;
import com.webcamconsult.syl.model.SYLFinishAppointmentResponseModel;

public interface SYLFinishedAppointmentListener {
void finishAppointment(SYLFinishAppointmentResponseModel mFinishappointmentresponsemodel);
}
