package com.webcamconsult.syl.model;

public class SYLFetchActiveSessionResponeModel {
SYLConferenceResponse  conferenceResponse;
boolean success;
boolean isActiveSessionPresent;
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
public boolean isActiveSessionPresent() {
	return isActiveSessionPresent;
}
public void setActiveSessionPresent(boolean isActiveSessionPresent) {
	this.isActiveSessionPresent = isActiveSessionPresent;
}
}
