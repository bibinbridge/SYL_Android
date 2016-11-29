package com.webcamconsult.syl.model;

public class SYLSignupResponseModel {
	boolean success;
	SignupResponse signupResponse;
	SYLSignupError error;
	EmailSendResponse emailSendResponse;

	public SYLSignupError getError() {
		return error;
	}

	public void setError(SYLSignupError error) {
		this.error = error;
	}

	public EmailSendResponse getEmailSendResponse() {
		return emailSendResponse;
	}

	public void setEmailSendResponse(EmailSendResponse emailSendResponse) {
		this.emailSendResponse = emailSendResponse;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public SignupResponse getSignupResponse() {
		return signupResponse;
	}

	public void setSignupResponse(SignupResponse signupResponse) {
		this.signupResponse = signupResponse;
	}
}
