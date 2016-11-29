package com.webcamconsult.syl.model;

public class SYLOpentokconfigureResponse {
SYLConferenceResponse conferenceResponse;
boolean success;
SYLSignupError error;
public SYLSignupError getError() {
	return error;
}
public void setError(SYLSignupError error) {
	this.error = error;
}
public SYLConferenceResponse getConferenceResponse() {
	return conferenceResponse;
}
public void setConferenceResponse(SYLConferenceResponse conferenceResponse) {
	this.conferenceResponse = conferenceResponse;
}
public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
}
