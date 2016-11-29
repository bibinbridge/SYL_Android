package com.webcamconsult.syl.databaseaccess;

import java.util.ArrayList;

import com.webcamconsult.syl.model.GmailContacts;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;
import com.webcamconsult.syl.networkoperations.GetAccessToken;
import com.webcamconsult.syl.utilities.SYLGoogleContacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SYLGmailFacebookContactsManager {
	SYLDatabase syldatabase;
	/** The context. */
	Context context;
int k=0;
	public SYLGmailFacebookContactsManager() {
		// TODO Auto-generated constructor stub
		this.context = context;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(context);

	}

	public SYLGmailFacebookContactsManager(Context context) {
		this.context = context;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(context);
	}

	public void insertFacebookContacts(
			ArrayList<SYLContactPersonsDetails> contactpersonsdetails) {
		if (contactpersonsdetails != null && contactpersonsdetails.size() > 0) {
			clearTable("contactpersons_detailsfacebook");
			for (SYLContactPersonsDetails syl_contactpersonsdetails : contactpersonsdetails) {
		
				this.insertfacebookContactPersonsDetail(
						syl_contactpersonsdetails, this.context);

			}
		}

	}

	public void insertGmailContacts(
			ArrayList<GmailContacts> contactpersonsdetails) {
		if (contactpersonsdetails != null && contactpersonsdetails.size() > 0) {
			clearTable("contactpersons_detailsgmail");
			for (GmailContacts  syl_contactpersonsdetails : contactpersonsdetails) {
				this.insertGmailContactPersonsDetail(syl_contactpersonsdetails,
						this.context);

			}
		}

	}

	private void insertfacebookContactPersonsDetail(
			SYLContactPersonsDetails mContactpersonsdetails, Context c) {
		SQLiteDatabase db=null;
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);

		 db = syldatabase.getWritableDatabase();
			ContentValues values;
			values = new ContentValues();
			values.put("user_id", mContactpersonsdetails.getUserId());
			values.put("name", mContactpersonsdetails.getName());
			values.put("email", mContactpersonsdetails.getEmail());
			values.put("profile_image",
					mContactpersonsdetails.getProfileImage());
			values.put("position_marker", "false");
			values.put("contact_type", "facebookcontacts");

			db.insert("contactpersons_detailsfacebook", null, values);
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
finally {
	db.close();
}
	}

	private void insertGmailContactPersonsDetail(
			GmailContacts mGmailContacts, Context c) {
		SQLiteDatabase db=null;
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);

	 db = syldatabase.getWritableDatabase();
			ContentValues values;
			values = new ContentValues();
			values.put("serialno", mGmailContacts.getSerialNo());
			values.put("user_id", mGmailContacts.getUserId());
			
			values.put("mobile", mGmailContacts.getMobile());
			values.put("importedfrom", mGmailContacts.getImportedFrom());
			values.put("name", mGmailContacts.getName());
	
			values.put("email", mGmailContacts.getEmail());
			values.put("facetimeid", mGmailContacts.getFacetimeId());
		
			values.put("facebookid", mGmailContacts.getFacebookId());
		
			values.put("googleid", mGmailContacts.getGoogleId());
			values.put("position_marker", "false");
			
	

			db.insert("contactpersons_detailsgmail", null, values);
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
finally {
	db.close();
	}
	}

	public ArrayList<SYLContactPersonsDetails> getFacebookContacts() {
		String query;
		SQLiteDatabase db=null;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLContactPersonsDetails> syl_contactpersonsdetailsarraylist = new ArrayList<SYLContactPersonsDetails>();
		Cursor cursor = null;
 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_detailsfacebook";

			if (cursor == null)
				cursor = db.rawQuery(query, null);

			while (cursor.moveToNext()) {
				SYLContactPersonsDetails contactpersondetail = new SYLContactPersonsDetails();
				contactpersondetail.setName(cursor.getString(cursor
						.getColumnIndex("name")));

				String user_id = cursor.getString(cursor
						.getColumnIndex("user_id"));
				contactpersondetail.setUserId(Integer.parseInt(user_id));

				contactpersondetail.setEmail(cursor.getString(cursor
						.getColumnIndex("email")));
				contactpersondetail.setProfileImage(cursor.getString(cursor
						.getColumnIndex("profile_image")));
				contactpersondetail.setPositionmark(cursor.getString(cursor
						.getColumnIndex("position_marker")));

				syl_contactpersonsdetailsarraylist.add(contactpersondetail);

			}
		} catch (Exception e) {

		}
		finally {
			db.close();
		}
		cursor.close();
		return syl_contactpersonsdetailsarraylist;
	}

	public ArrayList<GmailContacts> getGmailContacts() {
		String query;
		SQLiteDatabase db=null;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(context);
		ArrayList<GmailContacts> syl_contactpersonsdetailsarraylist = new ArrayList<GmailContacts>();
		Cursor cursor = null;
	
	 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_detailsgmail";

			if (cursor == null)
				cursor = db.rawQuery(query, null);

			while (cursor.moveToNext()) {
				GmailContacts contactpersondetail = new GmailContacts();
				String serialno = cursor.getString(cursor
						.getColumnIndex("serialno"));
				contactpersondetail.setSerialNo(Integer.parseInt(serialno));
				String user_id = cursor.getString(cursor
						.getColumnIndex("user_id"));
int userid=Integer.parseInt(user_id);
				
			
				contactpersondetail.setUserId(userid);
				contactpersondetail.setMobile(cursor.getString(cursor
						.getColumnIndex("mobile")));
				contactpersondetail.setImportedFrom(cursor.getString(cursor
						.getColumnIndex("importedfrom")));
				contactpersondetail.setName(cursor.getString(cursor
						.getColumnIndex("name")));
			
				
		

				contactpersondetail.setEmail(cursor.getString(cursor
						.getColumnIndex("email")));
				contactpersondetail.setFacetimeId(cursor.getString(cursor
						.getColumnIndex("facetimeid")));
				contactpersondetail.setFacebookId(cursor.getString(cursor
						.getColumnIndex("facebookid")));
				contactpersondetail.setGoogleId(cursor.getString(cursor
						.getColumnIndex("googleid")));
				contactpersondetail.setPositionmark(cursor.getString(cursor
						.getColumnIndex("position_marker")));

				syl_contactpersonsdetailsarraylist.add(contactpersondetail);

			}
		} catch (Exception e) {

		}
		finally{
			db.close();
		}
		cursor.close();
		return syl_contactpersonsdetailsarraylist;
	}

	public void clearTable(String tablename) {
		SQLiteDatabase db=null;
		if (syldatabase != null) {
			try {
		 db = syldatabase.getWritableDatabase();
				db.delete(tablename, null, null);
				db.close();
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
finally {
	db.close();
}
		}

	}

	public void updatePositionmarkerFacebookcontacts(String userid) {
		SQLiteDatabase db=null;
		
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

	 db = syldatabase.getWritableDatabase();

			String mSelectionClause = "user_id" + " = ? ";
			String[] mSelectionArgs = { userid };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "true");
			int k = db.update("contactpersons_detailsfacebook", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.close();
		}
	}

	public void updatePositionmarkerGmailcontacts(String userid) {
		SQLiteDatabase db=null;
		
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

			 db = syldatabase.getWritableDatabase();

			String mSelectionClause = "serialno" + " = ? ";
			String[] mSelectionArgs = { userid };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "true");
			int k = db.update("contactpersons_detailsgmail", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.close();
		}
	}

	public void updatePositionmarkerfacebookcontactsFalse() {
		SQLiteDatabase db=null; ;
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

		 db = syldatabase.getWritableDatabase();

			String mSelectionClause = "position_marker" + " = ? ";
			String[] mSelectionArgs = { "true" };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "false");
			int k = db.update("contactpersons_detailsfacebook", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.close();
		}
	}

	public void updatePositionmarkergmailcontactsFalse() {
		SQLiteDatabase db=null;
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

	 db = syldatabase.getWritableDatabase();

			String mSelectionClause = "position_marker" + " = ? ";
			String[] mSelectionArgs = { "true" };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "false");
			int k = db.update("contactpersons_detailsgmail", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally { db.close();         }
	}

	public ArrayList<SYLContactPersonsDetails> getFacebookContactswithvalue(
			String filtervalue) {
		String query;
		SQLiteDatabase db=null;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLContactPersonsDetails> syl_contactpersonsdetailsarraylist = new ArrayList<SYLContactPersonsDetails>();
		Cursor cursor = null;
 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_detailsfacebook where name LIKE '"+"%"
					+ filtervalue + "%" + "' ";

			if (cursor == null)
				cursor = db.rawQuery(query, null);

			while (cursor.moveToNext()) {
				SYLContactPersonsDetails contactpersondetail = new SYLContactPersonsDetails();
				contactpersondetail.setName(cursor.getString(cursor
						.getColumnIndex("name")));

				String user_id = cursor.getString(cursor
						.getColumnIndex("user_id"));
				contactpersondetail.setUserId(Integer.parseInt(user_id));

				contactpersondetail.setEmail(cursor.getString(cursor
						.getColumnIndex("email")));
				contactpersondetail.setProfileImage(cursor.getString(cursor
						.getColumnIndex("profile_image")));
				contactpersondetail.setPositionmark(cursor.getString(cursor
						.getColumnIndex("position_marker")));

				syl_contactpersonsdetailsarraylist.add(contactpersondetail);

			}
		} catch (Exception e) {

		}
		finally {
			db.close();
		}
		cursor.close();
		return syl_contactpersonsdetailsarraylist;
	}

	public ArrayList<GmailContacts> getGmailContactswithvalue(
			String filtervalue) {
		String query;
		SQLiteDatabase db=null ;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<GmailContacts> syl_contactpersonsdetailsarraylist = new ArrayList<GmailContacts>();
		Cursor cursor = null;
		 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_detailsgmail where email LIKE '"+"%"
					+ filtervalue + "%" + "' ";

			if (cursor == null)
				cursor = db.rawQuery(query, null);

			while (cursor.moveToNext()) {
			
				
				GmailContacts contactpersondetail = new GmailContacts();
				String serialno = cursor.getString(cursor
						.getColumnIndex("serialno"));
				contactpersondetail.setSerialNo(Integer.parseInt(serialno));
				String user_id = cursor.getString(cursor
						.getColumnIndex("user_id"));
				contactpersondetail.setUserId(Integer.parseInt(user_id));
				
				
				contactpersondetail.setName(cursor.getString(cursor
						.getColumnIndex("name")));

		

				contactpersondetail.setEmail(cursor.getString(cursor
						.getColumnIndex("email")));
			
				contactpersondetail.setPositionmark(cursor.getString(cursor
						.getColumnIndex("position_marker")));

				syl_contactpersonsdetailsarraylist.add(contactpersondetail);

			}
		} catch (Exception e) {

		}
		finally{
			db.close();
		}
		cursor.close();
		return syl_contactpersonsdetailsarraylist;
	}
}
