package com.webcamconsult.syl.model;

import android.graphics.Bitmap;

public class SYLPhoneContacts {
	int id;
public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
String name;
String profileimageurl;
String mobilenumber;
String email;
Bitmap bitmap;
public Bitmap getBitmap() {
	return bitmap;
}
public void setBitmap(Bitmap bitmap) {
	this.bitmap = bitmap;
}
public String getPositiommark() {
	return positiommark;
}
public void setPositiommark(String positiommark) {
	this.positiommark = positiommark;
}
String positiommark;
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getProfileimageurl() {
	return profileimageurl;
}
public void setProfileimageurl(String profileimageurl) {
	this.profileimageurl = profileimageurl;
}
public String getMobilenumber() {
	return mobilenumber;
}
public void setMobilenumber(String mobilenumber) {
	this.mobilenumber = mobilenumber;
}
}
