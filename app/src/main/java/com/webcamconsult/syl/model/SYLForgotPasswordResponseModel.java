package com.webcamconsult.syl.model;

public class SYLForgotPasswordResponseModel {
public SYLSignupError getError() {
		return error;
	}

	public void setError(SYLSignupError error) {
		this.error = error;
	}

boolean success;
SYLSignupError error;

public boolean isSuccess() {
	return success;
}

public void setSuccess(boolean success) {
	this.success = success;
}
}
