package com.webcamconsult.syl.providers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SYLSaveValues {
	public static String SYL_USERNAME="syl_username";
	public static String USER_IMAGESOURCE="user_imagesource";
	public static String PROFILE_IMAGEURL="profile_imageurl";
	public static String GOOGLE_ACCESSTOKEN="google_accesstoken";
	public static String SYL_ACCESSTOKEN="syl_accesstoken";
	public static String GOOGLE_REFRESHTOKEN="google_refreshtoken";
	public static String SYL_EMAIL="syl_email";
	public static String SYL_USERID="syl_userid";
	public static String GCM_REGISTRATIONID="gcm_registartionid";
	public static String SETTINGS_NOTIFICATIONSTATUS="settings_notificationstatus";
	public static String SYL_MOBILENUMBER="mobilenumber";
	public static String SYL_FIRSTTIMELOGIN="syl_firsttimelogin";
	public static String SYL_ISFACEBOOKCONNECTED="facebook_connected";

	public static String SYL_HIDEGUIDEDNAVIGATION="syl_hideguidednavigation";
	public static String SYL_HIDEGUIDEDNAVIGATIONCONTACTS="syl_hideguidednavigationcontacts";
	public static String SYL_HIDEGUIDEDNAVIGATIONNEW="syl_hideguidednavigationnew";
	public static String SYL_SKYPEID="syl_skypeid";
	public static String SYL_FACETIMEID="syl_facetime";
	public static String SYL_HANGOUTID="syl_hangoutid";
	public static String SYL_COUNTRYCODE="syl_countrycode";
	public static void setSylCountrycode(String countrycode,Context mContext)
	{
		final  SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_COUNTRYCODE, countrycode);
		editor.commit();
	}

	public static String getSylCountrycode(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_COUNTRYCODE,"");

	}
	public static void setSylFacetimeid(String facetimeid,Context mContext)
	{
		final  SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_FACETIMEID, facetimeid);
		editor.commit();
	}

	public static String getSylFacetimeid(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_FACETIMEID,"");

	}


	public static void setSylHangoutid(String hangoutid,Context mContext)
	{
		final  SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_HANGOUTID, hangoutid);
		editor.commit();
	}

	public static String getSYLHangoutid(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_HANGOUTID,"");

	}
	public static void setSYLusername(String username,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_USERNAME, username);
		editor.commit();
	}

	public static String getSYLusername(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_USERNAME,"");

	}



	public static void setUserImageSource(String userimagesource,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(USER_IMAGESOURCE, userimagesource);
		editor.commit();
	}

	public static String getProfileImageurl(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(PROFILE_IMAGEURL,"");
	}

	public static void setProfileImageurl(String profileimageurl,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(PROFILE_IMAGEURL, profileimageurl);
		editor.commit();
	}

	public static String getUserImageSource(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(USER_IMAGESOURCE,"");

	}
	public static void setGoogleToken(String googletoken,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(GOOGLE_ACCESSTOKEN, googletoken);
		editor.commit();
	}
	public static String getGoogleToken(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(GOOGLE_ACCESSTOKEN,"");

	}
	public static void setGoogleRefreshToken(String googlerefreshtoken,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(GOOGLE_REFRESHTOKEN, googlerefreshtoken);
		editor.commit();
	}
	public static String getGoogleRefreshToken(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(GOOGLE_REFRESHTOKEN,"");

	}

	public static void setSYLaccessToken(String syltoken,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_ACCESSTOKEN, syltoken);
		editor.commit();
	}
	public static String  getSYLaccessToken(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_ACCESSTOKEN,"");

	}


	public static void setSYLEmailAddress(String syltoken,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_EMAIL, syltoken);
		editor.commit();
	}
	public static String  getSYLEmailAddress(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_EMAIL,"");

	}


	public static void setSYLUserID(String userid,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_USERID, userid);
		editor.commit();
	}
	public static String  getSYLUserID(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_USERID,"");

	}

	public static void setGCMRegistrationid(String gcmregistrationid,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(GCM_REGISTRATIONID, gcmregistrationid);
		editor.commit();
	}
	public static String  getGCMRegistrationid(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(GCM_REGISTRATIONID,"");

	}
	public static void setNotificationstatus(String notificationstatus,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SETTINGS_NOTIFICATIONSTATUS,notificationstatus);
		editor.commit();
	}

	public static String getNotificationstatus(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SETTINGS_NOTIFICATIONSTATUS,"");

	}

	public static void setSYLMobileNumber(String mobilenumber,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_MOBILENUMBER, mobilenumber);
		editor.commit();
	}
	public static String  getSYLMobileNumber(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_MOBILENUMBER,"");

	}
	public static void setSYLFirsttimeLogin(String firsttimeloginflag,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_FIRSTTIMELOGIN, firsttimeloginflag);
		editor.commit();
	}
	public static String  getSYLFirsttimeLogin(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_FIRSTTIMELOGIN,"");

	}
	public static void setFacebookConnected(String facebookconnected,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_ISFACEBOOKCONNECTED, facebookconnected);
		editor.commit();
	}
	public static String  getFacebookConnected(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_ISFACEBOOKCONNECTED,"");

	}
	public static void setSylHideguidednavigation(String syl_hideguidednavigation ,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_HIDEGUIDEDNAVIGATION, syl_hideguidednavigation);
		editor.commit();
	}
	public static String  getSylHideguidednavigation(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_HIDEGUIDEDNAVIGATION,"");

	}
	public static void setSylHideguidednavigationContacts(String syl_hideguidednavigationcontacts ,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_HIDEGUIDEDNAVIGATIONCONTACTS, syl_hideguidednavigationcontacts);
		editor.commit();
	}
	public static String  getSylHideguidednavigationContacts(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_HIDEGUIDEDNAVIGATIONCONTACTS,"");

	}
	public static void setSylHideguidednavigationnewappointment(String syl_hideguidednavigationnew ,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_HIDEGUIDEDNAVIGATIONNEW, syl_hideguidednavigationnew);
		editor.commit();
	}
	public static String  getSylHideguidednavigationnewappointment(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_HIDEGUIDEDNAVIGATIONNEW,"");

	}
	public static void setSylSkypeID(String syl_skypeid ,Context mContext)
	{
		final 	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor=preferences.edit();
		editor.putString(SYL_SKYPEID, syl_skypeid);
		editor.commit();
	}
	public static String  getSylSkypeID(Context mContext)
	{
		final SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(SYL_SKYPEID,"");

	}


}
