package com.webcamconsult.syl.model;

public class SYLSigninResponseModel {
boolean success;
User user;
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
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
}
