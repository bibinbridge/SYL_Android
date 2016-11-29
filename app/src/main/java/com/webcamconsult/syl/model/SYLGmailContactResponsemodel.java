package com.webcamconsult.syl.model;

import java.util.ArrayList;

public class SYLGmailContactResponsemodel {
boolean success;
ArrayList<GmailContacts> googleContacts;
	SYLSignupError error;
public boolean isSuccess() {
	return success;
}
public ArrayList<GmailContacts> getGoogleContacts() {
	return googleContacts;
}
public void setGoogleContacts(ArrayList<GmailContacts> googleContacts) {
	this.googleContacts = googleContacts;
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
