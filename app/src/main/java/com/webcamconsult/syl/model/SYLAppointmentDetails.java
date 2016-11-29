package com.webcamconsult.syl.model;

public class SYLAppointmentDetails {

int user_id;
String email;
String firstname;
String lastname;
String profile_image;
String time;
String date;



public int getUser_id() {
	return user_id;
}
public void setUser_id(int user_id) {
	this.user_id = user_id;
}
public String getProfile_image() {
	return profile_image;
}
public void setProfile_image(String profile_image) {
	this.profile_image = profile_image;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getFirstname() {
	return firstname;
}
public void setFirstname(String firstname) {
	this.firstname = firstname;
}
public String getLastname() {
	return lastname;
}
public void setLastname(String lastname) {
	this.lastname = lastname;
}

public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}






}
