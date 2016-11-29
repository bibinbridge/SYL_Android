package com.webcamconsult.syl.utilities;

public class SYLGoogleContacts {
//	public static String CLIENT_ID = "333175490446-a9bq9iapmgrvb0i6bo5evdctqrvk9v4i.apps.googleusercontent.com";
    // Use your own client id
 
  //  public static String CLIENT_SECRET = "hhh_vwv3jDo-6P1S8UQ-OOHY";
    // Use your own client secret
 
	//public static String CLIENT_ID = "946597396463-enaauh97146keb3a2g5fr8gptoqtaprb.apps.googleusercontent.com";
    // Use your own client id
 
   // public static String CLIENT_SECRET = "3aSUt_dmLofD4fIgdmFEMCuW";


    public static String CLIENT_ID = "451016043221-rtqsa8j4tr2u25ckuopkjdjehavnkij0.apps.googleusercontent.com";
    // Use your own client id

    public static String CLIENT_SECRET = "5Rlb-dpOjVC0iUfpRqABdx8d";
    
    
    public static String REDIRECT_URI = "http://localhost";
    public static String GRANT_TYPE = "authorization_code";
    public static String TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    public static String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    //public static String OAUTH_SCOPE = "https://www.googleapis.com/auth/contacts.readonly";
    public static String OAUTH_SCOPE="https://www.google.com/m8/feeds/contacts/default/full";
    public static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full";
    public static final int MAX_NB_CONTACTS = 1000;
    public static final String APP = "";
}
