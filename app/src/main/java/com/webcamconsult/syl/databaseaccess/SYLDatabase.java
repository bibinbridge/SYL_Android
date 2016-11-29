package com.webcamconsult.syl.databaseaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SYLDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "syl_database";
	Context context;
	/** The Constant DATABASE_VERSION. */
	private static final int DATABASE_VERSION = 24;
	public SYLDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
	//		db.execSQL("CREATE TABLE appointment_details(appointment_id VARCHAR(20),appointment_topic VARCHAR(200),appointment_description VARCHAR(400),user_id VARCHAR(20),firstname VARCHAR(500),mobilenumber VARCHAR(500),skypeid VARCHAR(500),date VARCHAR(500),time  VARCHAR(500), profile_image VARCHAR(500) , appointment_type  VARCHAR(500)   )       ");
			
			db.execSQL("CREATE TABLE appointment_details(appointment_id VARCHAR(20),appointment_topic VARCHAR(200),appointment_description VARCHAR(400), priority1 VARCHAR(200), priority2  VARCHAR(200),   priority3   VARCHAR(200),    preferreddate1   VARCHAR(200),    preferredtime1   VARCHAR(200),  optiondate1   VARCHAR(200),    optiontime1   VARCHAR(200),    optiondate2   VARCHAR(200),     optiontime2   VARCHAR(200),      user_id  VARCHAR(20),firstname VARCHAR(500),mobilenumber VARCHAR(500), facetimeid VARCHAR(500), hangoutid VARCHAR(500),  skypeid VARCHAR(500), profile_image VARCHAR(500) , email VARCHAR(500) ,appointment_type  VARCHAR(500),organizer_id  VARCHAR(20) ,organizer_profileimage  VARCHAR(500),organizer_email  VARCHAR(200) ,organizer_name  VARCHAR(400),organizer_facetimeid  VARCHAR(400) ,organizer_skypeid  VARCHAR(400),organizer_mobile  VARCHAR(400), organizer_hangoutid  VARCHAR(400)   )       ");
			
			
			db.execSQL("CREATE TABLE contactpersons_details(user_id VARCHAR(20), name VARCHAR(500)  ,email VARCHAR(500),  profile_image VARCHAR(500) , position_marker  VARCHAR(500) , contact_type  VARCHAR(500)  )    ");
		
			db.execSQL("CREATE TABLE contactpersons_detailssyl(user_id VARCHAR(20), name VARCHAR(500)  ,email VARCHAR(500),  profile_image VARCHAR(500) , position_marker  VARCHAR(500) , contact_type  VARCHAR(500)  )    ");
		
			db.execSQL("CREATE TABLE contactpersons_detailsfacebook(user_id VARCHAR(20), name VARCHAR(500)  ,email VARCHAR(500),  profile_image VARCHAR(500) , position_marker  VARCHAR(500) , contact_type  VARCHAR(500)  )    ");
			
			db.execSQL("CREATE TABLE contactpersons_detailsgmail(serialno VARCHAR(20),user_id VARCHAR(20), mobile VARCHAR(500)  ,importedfrom VARCHAR(500),  name VARCHAR(500) , position_marker  VARCHAR(500) , email  VARCHAR(500), facetimeid  VARCHAR(500), facebookid  VARCHAR(500), googleid  VARCHAR(500)  )    ");
			
		//	db.execSQL("CREATE TABLE contactpersons_detailsgmail(user_id VARCHAR(20), name VARCHAR(500)  ,email VARCHAR(500),  profile_image VARCHAR(500) , position_marker  VARCHAR(500) , contact_type  VARCHAR(500)  )    ");
			db.execSQL("CREATE TABLE appointments_calendar(event_id VARCHAR(100), appointment_id VARCHAR(100)   )    ");
			
			db.execSQL("CREATE TABLE contactpersons_detailsphonecontacts(ID INTEGER PRIMARY KEY   AUTOINCREMENT,name VARCHAR(500)  ,email VARCHAR(500),  profile_image VARCHAR(500) , position_marker  VARCHAR(500) ,  mobilenumber  VARCHAR(800) , contact_type  VARCHAR(500) , image BLOB )    ");

			db.execSQL("CREATE TABLE contactpersons_detailsphonecontacts2(name VARCHAR(500),  profile_image VARCHAR(500),position_marker  VARCHAR(500) ,contactid VARCHAR(500) ) ");

			db.execSQL("CREATE TABLE appointmenthistory_details(appointment_id VARCHAR(20),appointment_topic VARCHAR(200),appointment_description VARCHAR(400), priority1 VARCHAR(200), priority2  VARCHAR(200),   priority3   VARCHAR(200),    preferreddate1   VARCHAR(200),    preferredtime1   VARCHAR(200),  optiondate1   VARCHAR(200),    optiontime1   VARCHAR(200),    optiondate2   VARCHAR(200),     optiontime2   VARCHAR(200),      user_id  VARCHAR(20),firstname VARCHAR(500),mobilenumber VARCHAR(500), facetimeid VARCHAR(500), hangoutid VARCHAR(500),  skypeid VARCHAR(500), profile_image VARCHAR(500) , email VARCHAR(500) ,appointment_type  VARCHAR(500),organizer_id  VARCHAR(20) ,organizer_profileimage  VARCHAR(500),organizer_email  VARCHAR(200) ,organizer_name  VARCHAR(400),organizer_facetimeid  VARCHAR(400) ,organizer_skypeid  VARCHAR(400),organizer_mobile  VARCHAR(400), organizer_hangoutid  VARCHAR(400)   )       ");

		}
		catch(Exception e)
		{
			
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	
		db.execSQL("DROP TABLE IF EXISTS " + "appointment_details");
		onCreate(db);
	}

}
