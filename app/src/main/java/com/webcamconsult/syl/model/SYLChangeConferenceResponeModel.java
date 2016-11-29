package com.webcamconsult.syl.model;

public class SYLChangeConferenceResponeModel {
boolean success;
SYLSignupError error;
boolean	isAlreadyInCall;

	public boolean isAlreadyInCall() {
		return isAlreadyInCall;
	}

	public void setIsAlreadyInCall(boolean isAlreadyInCall) {
		this.isAlreadyInCall = isAlreadyInCall;
	}

	public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public SYLSignupError getError() {
	return error;
}
public void setError(SYLSignupError error) {
	this.error = error;
}
}
