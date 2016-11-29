package com.webcamconsult.syl.model;

import java.util.ArrayList;



public class SYLAppointmentDetailsAll {
	
	String success;
	ArrayList<SYLAppointmentDetails> TodaysAppointments;
	ArrayList<SYLAppointmentDetails> RequestReceived;
	ArrayList<SYLAppointmentDetails> ConfirmedAppointments;
	ArrayList<SYLAppointmentDetails> RequestSend;
	ArrayList<SYLAppointmentDetails> CancelledRequest;
	
	
	public ArrayList<SYLAppointmentDetails> getTodaysAppointments() {
		return TodaysAppointments;
	}
	public void setTodaysAppointments(
			ArrayList<SYLAppointmentDetails> todaysAppointments) {
		TodaysAppointments = todaysAppointments;
	}
	public ArrayList<SYLAppointmentDetails> getCancelledRequest() {
		return CancelledRequest;
	}
	public void setCancelledRequest(
			ArrayList<SYLAppointmentDetails> cancelledRequest) {
		CancelledRequest = cancelledRequest;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public ArrayList<SYLAppointmentDetails> getRequestReceived() {
		return RequestReceived;
	}
	public void setRequestReceived(ArrayList<SYLAppointmentDetails> requestReceived) {
		RequestReceived = requestReceived;
	}
	public ArrayList<SYLAppointmentDetails> getConfirmedAppointments() {
		return ConfirmedAppointments;
	}
	public void setConfirmedAppointments(
			ArrayList<SYLAppointmentDetails> confirmedAppointments) {
		ConfirmedAppointments = confirmedAppointments;
	}
	public ArrayList<SYLAppointmentDetails> getRequestSend() {
		return RequestSend;
	}
	public void setRequestSend(ArrayList<SYLAppointmentDetails> requestSend) {
		RequestSend = requestSend;
	}
	

	

}
