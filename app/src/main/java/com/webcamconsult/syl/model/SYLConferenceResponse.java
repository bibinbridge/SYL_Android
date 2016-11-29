package com.webcamconsult.syl.model;

public class SYLConferenceResponse {
int	conferenceId;
String opentokSessionId;
String publisherToken;
String subscriberToken;
String sessionStatusCode;
String apiKey;
public String getApiKey() {
	return apiKey;
}
public void setApiKey(String apiKey) {
	this.apiKey = apiKey;
}
public int getConferenceId() {
	return conferenceId;
}
public void setConferenceId(int conferenceId) {
	this.conferenceId = conferenceId;
}
public String getOpentokSessionId() {
	return opentokSessionId;
}
public void setOpentokSessionId(String opentokSessionId) {
	this.opentokSessionId = opentokSessionId;
}
public String getPublisherToken() {
	return publisherToken;
}
public void setPublisherToken(String publisherToken) {
	this.publisherToken = publisherToken;
}
public String getSubscriberToken() {
	return subscriberToken;
}
public void setSubscriberToken(String subscriberToken) {
	this.subscriberToken = subscriberToken;
}
public String getSessionStatusCode() {
	return sessionStatusCode;
}
public void setSessionStatusCode(String sessionStatusCode) {
	this.sessionStatusCode = sessionStatusCode;
}
}
