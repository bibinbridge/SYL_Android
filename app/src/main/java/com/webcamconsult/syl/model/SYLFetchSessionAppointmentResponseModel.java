package com.webcamconsult.syl.model;

public class SYLFetchSessionAppointmentResponseModel {
boolean success;
String sessionStatusCode;
SYLSignupError error;
public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public String getSessionStatusCode() {
	return sessionStatusCode;
}
public void setSessionStatusCode(String sessionStatusCode) {
	this.sessionStatusCode = sessionStatusCode;
}
public SYLSignupError getError() {
	return error;
}
public void setError(SYLSignupError error) {
	this.error = error;
}
}
