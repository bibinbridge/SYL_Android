package com.webcamconsult.syl.model;

public class SYLFetchUserDetailsResponseModel {
boolean success;
SignupResponse user;
	SYLSignupError error;



public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public SignupResponse getUser() {
	return user;
}
public void setUser(SignupResponse user) {
	this.user = user;
}
	public SYLSignupError getError() {
		return error;
	}

	public void setError(SYLSignupError error) {
		this.error = error;
	}
}
