package com.webcamconsult.viewmanager;
import android.content.Context;
import android.os.AsyncTask;

import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLHistoryAppointmentsdatamanager;
import com.webcamconsult.syl.interfaces.SYLFetchHistoryAppointmentsListener;
import com.webcamconsult.syl.model.SYLFetchAppointmentsResponseModel;
import com.webcamconsult.syl.modelmanager.SYLAppointmentsModelManager;
import com.webcamconsult.syl.modelmanager.SYLHistoryAppointmentsModelManager;

/**
 * Created by Sandeep on 11/24/2015.
 */
public class SYLHistoryAppointmentsViewManager implements SYLFetchHistoryAppointmentsListener {
    public SYLFetchHistoryAppointmentsListener mFetchhistoryAppointmentsListener;
    Context mContext;
public void getHistoryAppointments(String method,Context c,String accesstoken,String userid,String gmttimezone)
{
    SYLHistoryAppointmentsModelManager sylhistoryAppointmentsModelManager = new SYLHistoryAppointmentsModelManager();
    //	sYLAppointmentsModelManager.sYLAppointmentListner=SYLAppointmentsManager.this;
    sylhistoryAppointmentsModelManager.mFetchhistoryAppointmentsListener=SYLHistoryAppointmentsViewManager.this;
    sylhistoryAppointmentsModelManager.geHistorytAppointments(method,accesstoken,userid,gmttimezone);
    mContext=c;

}

    @Override
    public void getHistoryAppointmentDetailsFinish(SYLFetchAppointmentsResponseModel mFetchappointmentsResponseModel) {

        new AsyncTask<SYLFetchAppointmentsResponseModel, Void, SYLFetchAppointmentsResponseModel>() {
            protected void onPreExecute() {
                // Pre Code
            }
            protected SYLFetchAppointmentsResponseModel doInBackground(SYLFetchAppointmentsResponseModel... unused) {
                try {
                    // Background Code
                    SYLFetchAppointmentsResponseModel mFetchappointmentsResponseModel = unused[0];
                    SYLHistoryAppointmentsdatamanager datamanager = new SYLHistoryAppointmentsdatamanager(mContext);
                    datamanager.clearTable("appointmenthistory_details");
                    datamanager.insertappointmentsetails(mFetchappointmentsResponseModel.getTodaysAppointments(), "TODAYS CONFIRMED");
                    datamanager.insertappointmentsetails(mFetchappointmentsResponseModel.getRequestReceived(), "REQUEST RECEIVED");
                    datamanager.insertappointmentsetails(mFetchappointmentsResponseModel.getRequestSend(), "REQUEST SENT");
                    datamanager.insertappointmentsetails(mFetchappointmentsResponseModel.getConfirmedAppointments(), "OTHER CONFIRMED");
                    datamanager.insertappointmentsetails(mFetchappointmentsResponseModel.getCancelledRequest(), "CANCELLED");
                    datamanager.insertappointmentsetails(mFetchappointmentsResponseModel.getFinishedAppointments(), "FINISHED");

                    return mFetchappointmentsResponseModel;
                }
                catch (Exception e)
                {
                    return null;
                }
            }
            protected void onPostExecute(SYLFetchAppointmentsResponseModel flag) {
                // Post Code
                mFetchhistoryAppointmentsListener.getHistoryAppointmentDetailsFinish(flag);
            }
        }.execute(mFetchappointmentsResponseModel);














    }
}
