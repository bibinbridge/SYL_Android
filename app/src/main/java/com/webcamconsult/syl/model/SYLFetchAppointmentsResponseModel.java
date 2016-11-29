package com.webcamconsult.syl.model;

import java.util.ArrayList;

public class SYLFetchAppointmentsResponseModel {
	boolean success;
	ArrayList<SYLFetchAppointmentsDetails> todaysAppointments;
	ArrayList<SYLFetchAppointmentsDetails> requestReceived;
	ArrayList<SYLFetchAppointmentsDetails> confirmedAppointments;
	ArrayList<SYLFetchAppointmentsDetails> requestSend;
	ArrayList<SYLFetchAppointmentsDetails> cancelledRequest;
	ArrayList<SYLFetchAppointmentsDetails> finishedAppointments;
	public ArrayList<SYLFetchAppointmentsDetails> getFinishedAppointments() {
		return finishedAppointments;
	}

	public void setFinishedAppointments(ArrayList<SYLFetchAppointmentsDetails> finishedAppointments) {
		this.finishedAppointments = finishedAppointments;
	}


	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public ArrayList<SYLFetchAppointmentsDetails> getTodaysAppointments() {
		return todaysAppointments;
	}
	public void setTodaysAppointments(
			ArrayList<SYLFetchAppointmentsDetails> todaysAppointments) {
		this.todaysAppointments = todaysAppointments;
	}
	public ArrayList<SYLFetchAppointmentsDetails> getRequestReceived() {
		return requestReceived;
	}
	public void setRequestReceived(
			ArrayList<SYLFetchAppointmentsDetails> requestReceived) {
		this.requestReceived = requestReceived;
	}
	public ArrayList<SYLFetchAppointmentsDetails> getConfirmedAppointments() {
		return confirmedAppointments;
	}
	public void setConfirmedAppointments(
			ArrayList<SYLFetchAppointmentsDetails> confirmedAppointments) {
		this.confirmedAppointments = confirmedAppointments;
	}
	public ArrayList<SYLFetchAppointmentsDetails> getRequestSend() {
		return requestSend;
	}
	public void setRequestSend(ArrayList<SYLFetchAppointmentsDetails> requestSend) {
		this.requestSend = requestSend;
	}
	public ArrayList<SYLFetchAppointmentsDetails> getCancelledRequest() {
		return cancelledRequest;
	}
	public void setCancelledRequest(
			ArrayList<SYLFetchAppointmentsDetails> cancelledRequest) {
		this.cancelledRequest = cancelledRequest;
	}
}
