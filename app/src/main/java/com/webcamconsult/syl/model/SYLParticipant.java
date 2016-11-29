package com.webcamconsult.syl.model;

public class SYLParticipant {
int userId;
String name;
String email;
String profileImage;
String facetimeId;
String skypeId;
String mobile;
String hangoutId;
public String getHangoutId() {
	return hangoutId;
}
public void setHangoutId(String hangoutId) {
	this.hangoutId = hangoutId;
}
public String getFacetimeId() {
	return facetimeId;
}
public void setFacetimeId(String facetimeId) {
	this.facetimeId = facetimeId;
}
public String getSkypeId() {
	return skypeId;
}
public void setSkypeId(String skypeId) {
	this.skypeId = skypeId;
}
public String getMobile() {
	return mobile;
}
public void setMobile(String mobile) {
	this.mobile = mobile;
}
public int getUserId() {
	return userId;
}
public void setUserId(int userId) {
	this.userId = userId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getProfileImage() {
	return profileImage;
}
public void setProfileImage(String profileImage) {
	this.profileImage = profileImage;
}
}
