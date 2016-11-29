package com.webcamconsult.viewmanager;


import android.content.Context;
import android.os.AsyncTask;

import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.interfaces.SYLAppointmentListner;
import com.webcamconsult.syl.interfaces.SYLFetchAppointmentDetailsListener;
import com.webcamconsult.syl.model.SYLAppointmentDetailsAll;
import com.webcamconsult.syl.model.SYLFetchAppointmentsResponseModel;
import com.webcamconsult.syl.modelmanager.SYLAppointmentsModelManager;

public class SYLAppointmentsManager implements SYLAppointmentListner ,SYLFetchAppointmentDetailsListener
{
	Context mContext;

	public SYLAppointmentListner sYLAppointmentListner;
	public SYLFetchAppointmentDetailsListener mFetchAppointmentsListener;

	public void getAppointments(String method,Context c,String accesstoken,String userid,String gmttimezone) 
	{
		SYLAppointmentsModelManager sYLAppointmentsModelManager = new SYLAppointmentsModelManager();
	//	sYLAppointmentsModelManager.sYLAppointmentListner=SYLAppointmentsManager.this;
		sYLAppointmentsModelManager.mfetchAppointmentsListener=SYLAppointmentsManager.this;
		sYLAppointmentsModelManager.getAppointments(method,accesstoken,userid,gmttimezone);
		mContext=c;
		
	}

	



	@Override
	public void onDidFinished(SYLAppointmentDetailsAll sYLAppointmentsDetailsall) 
	{
		sYLAppointmentListner.onDidFinished(sYLAppointmentsDetailsall);
		SYLAppointmentdetailsdatamanager  datamanager = new SYLAppointmentdetailsdatamanager(mContext);
	
//		datamanager.insertappointmentsetails(sYLAppointmentsDetailsall.getRequestReceived(),"RequestReceived");
//		datamanager.insertappointmentsetails(sYLAppointmentsDetailsall.getConfirmedAppointments(),"ConfirmedAppointments");
//		datamanager.insertappointmentsetails(sYLAppointmentsDetailsall.getRequestSend(),"RequestSend");
//		datamanager.insertappointmentsetails(sYLAppointmentsDetailsall.getCancelledRequest(),"CancelledRequest");
		
//		ArrayList<SYLAppointmentDetails>  requestreceived_arraylist=datamanager.getAllAppointmentDetails("RequestReceived");
//		ArrayList<SYLAppointmentDetails>  confirmedappointment_arraylist=datamanager.getAllAppointmentDetails("ConfirmedAppointments");
//		ArrayList<SYLAppointmentDetails>  requestsend_arraylist=datamanager.getAllAppointmentDetails("RequestSend");
//		ArrayList<SYLAppointmentDetails>  cancelled_arraylist=datamanager.getAllAppointmentDetails("CancelledRequest");
	}





	@Override
	public void getAppointmentDetailsFinish(
			SYLFetchAppointmentsResponseModel mFetchappointmentsResponseModel) {
		// TODO Auto-generated method stub
		new AsyncTask<SYLFetchAppointmentsResponseModel, Void, SYLFetchAppointmentsResponseModel>() {
			protected void onPreExecute() {
				// Pre Code
			}
			protected SYLFetchAppointmentsResponseModel doInBackground(SYLFetchAppointmentsResponseModel... unused) {
				try {
					// Background Code
					SYLFetchAppointmentsResponseModel mFetchappointmentsResponseModel = unused[0];
					SYLAppointmentdetailsdatamanager datamanager = new SYLAppointmentdetailsdatamanager(mContext);
					datamanager.clearTable("appointment_details");
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
				mFetchAppointmentsListener.getAppointmentDetailsFinish(flag);
			}
		}.execute(mFetchappointmentsResponseModel);
	}
}
