package com.webcamconsult.syl.model;

public class SYLCreateNewAppointmentResponse {
boolean success;
int appointmentID;
String verificationGuid;
SYLSignupError error;
public SYLSignupError getError() {
	return error;
}
public void setError(SYLSignupError error) {
	this.error = error;
}
public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public int getAppointmentID() {
	return appointmentID;
}
public void setAppointmentID(int appointmentID) {
	this.appointmentID = appointmentID;
}
public String getVerificationGuid() {
	return verificationGuid;
}
public void setVerificationGuid(String verificationGuid) {
	this.verificationGuid = verificationGuid;
}














}
