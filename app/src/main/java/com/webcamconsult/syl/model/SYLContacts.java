package com.webcamconsult.syl.model;

import java.util.ArrayList;

public class SYLContacts {
boolean success;
ArrayList<SYLContactPersonsDetails> users;
	SYLSignupError error;



public boolean getSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public ArrayList<SYLContactPersonsDetails> getUsers() {
	return users;
}
public void setUsers(ArrayList<SYLContactPersonsDetails> users) {
	this.users = users;
}
	public SYLSignupError getError() {
		return error;
	}

	public void setError(SYLSignupError error) {
		this.error = error;
	}

}
