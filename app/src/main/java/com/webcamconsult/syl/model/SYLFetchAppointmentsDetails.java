package com.webcamconsult.syl.model;

public class SYLFetchAppointmentsDetails {
int	appointmentID;
String topic;
String description;
String appointmentPriority1;
String appointmentPriority2;
String appointmentPriority3;
String listingType;
String appointmentDate1;
String appointmentTime1;
String appointmentDate2;
String appointmentTime2;
String appointmentDate3;
String appointmentTime3;
String verificationGuid;
SYLOrganizer organizer;
SYLParticipant participant;

public String getListingType() {
	return listingType;
}
public void setListingType(String listingType) {
	this.listingType = listingType;
}
public String getAppointmentPriority1() {
	return appointmentPriority1;
}
public void setAppointmentPriority1(String appointmentPriority1) {
	this.appointmentPriority1 = appointmentPriority1;
}
public String getAppointmentPriority2() {
	return appointmentPriority2;
}
public void setAppointmentPriority2(String appointmentPriority2) {
	this.appointmentPriority2 = appointmentPriority2;
}
public String getAppointmentPriority3() {
	return appointmentPriority3;
}
public void setAppointmentPriority3(String appointmentPriority3) {
	this.appointmentPriority3 = appointmentPriority3;
}

public int getAppointmentID() {
	return appointmentID;
}
public void setAppointmentID(int appointmentID) {
	this.appointmentID = appointmentID;
}
public String getTopic() {
	return topic;
}
public void setTopic(String topic) {
	this.topic = topic;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getAppointmentDate1() {
	return appointmentDate1;
}
public void setAppointmentDate1(String appointmentDate1) {
	this.appointmentDate1 = appointmentDate1;
}
public String getAppointmentTime1() {
	return appointmentTime1;
}
public void setAppointmentTime1(String appointmentTime1) {
	this.appointmentTime1 = appointmentTime1;
}
public String getAppointmentDate2() {
	return appointmentDate2;
}
public void setAppointmentDate2(String appointmentDate2) {
	this.appointmentDate2 = appointmentDate2;
}
public String getAppointmentTime2() {
	return appointmentTime2;
}
public void setAppointmentTime2(String appointmentTime2) {
	this.appointmentTime2 = appointmentTime2;
}
public String getAppointmentDate3() {
	return appointmentDate3;
}
public void setAppointmentDate3(String appointmentDate3) {
	this.appointmentDate3 = appointmentDate3;
}
public String getAppointmentTime3() {
	return appointmentTime3;
}
public void setAppointmentTime3(String appointmentTime3) {
	this.appointmentTime3 = appointmentTime3;
}
public String getVerificationGuid() {
	return verificationGuid;
}
public void setVerificationGuid(String verificationGuid) {
	this.verificationGuid = verificationGuid;
}
public SYLOrganizer getOrganizer() {
	return organizer;
}
public void setOrganizer(SYLOrganizer organizer) {
	this.organizer = organizer;
}
public SYLParticipant getParticipant() {
	return participant;
}
public void setParticipant(SYLParticipant participant) {
	this.participant = participant;
}


}
