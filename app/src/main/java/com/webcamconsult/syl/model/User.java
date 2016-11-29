package com.webcamconsult.syl.model;

public class User {

	int userId;
String name;
String email;
String facetimeId;
String skypeId;
String facebookId;
String googleId;
String mobile;
String profileImage;
String countryCode;
String timeZone;
boolean isFacebookConnected;
boolean  isGmailConnected;
String latitude;
String longitude;
String accessToken;	
boolean isVerified;

    	
    	public boolean isVerified() {
	return isVerified;
}
public void setVerified(boolean isVerified) {
	this.isVerified = isVerified;
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
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public boolean isFacebookConnected() {
		return isFacebookConnected;
	}
	public void setFacebookConnected(boolean isFacebookConnected) {
		this.isFacebookConnected = isFacebookConnected;
	}
	public boolean isGmailConnected() {
		return isGmailConnected;
	}
	public void setGmailConnected(boolean isGmailConnected) {
		this.isGmailConnected = isGmailConnected;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
