package com.webcamconsult.syl.databaseaccess;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.webcamconsult.syl.sylphonecontacts.SYLPhoneContacts2;
import com.webcamconsult.syl.model.SYLContactPersonsDetails;
import com.webcamconsult.syl.model.SYLPhoneContacts;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLContactPersonsdatamanager {
	SYLDatabase syldatabase;
	/** The context. */
	Context context;

	public SYLContactPersonsdatamanager() {
		this.context = context;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(context);
	}

	public SYLContactPersonsdatamanager(Context context) {
		this.context = context;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(context);
	}

	public void insertFavouritesContacts(
			ArrayList<SYLContactPersonsDetails> contactpersonsdetails) {
		if (contactpersonsdetails != null && contactpersonsdetails.size() > 0) {
			clearTable("contactpersons_details");
			for (SYLContactPersonsDetails syl_contactpersonsdetails : contactpersonsdetails) {
				this.insertfavouritesContactPersonsDetail(
						syl_contactpersonsdetails, this.context);

			}
		}

	}

	public void insertsylContacts(
			ArrayList<SYLContactPersonsDetails> contactpersonsdetails) {
		if (contactpersonsdetails != null && contactpersonsdetails.size() > 0) {
			clearTable("contactpersons_detailssyl");
			for (SYLContactPersonsDetails syl_contactpersonsdetails : contactpersonsdetails) {
				this.insertsylContactPersonsDetail(syl_contactpersonsdetails,
						this.context);

			}
		}

	}

	private void insertfavouritesContactPersonsDetail(
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
			values.put("contact_type", "favourites");

			db.insert("contactpersons_details", null, values);
			//db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
finally {
	db.close();
}
	}

	private void insertsylContactPersonsDetail(
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
			values.put("contact_type", "syl");

			db.insert("contactpersons_detailssyl", null, values);
			//db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
finally {
	db.close();
}
	}

	public ArrayList<SYLContactPersonsDetails> getFavouritesContacts() {
		String query;
		SQLiteDatabase db ;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLContactPersonsDetails> syl_contactpersonsdetailsarraylist = new ArrayList<SYLContactPersonsDetails>();
		Cursor cursor = null;
 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_details";

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
			cursor.close();
			db.close();
		}

		return syl_contactpersonsdetailsarraylist;
	}
	
	public ArrayList<SYLContactPersonsDetails> getsylContacts() {
		String query;
		SQLiteDatabase db;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLContactPersonsDetails> syl_contactpersonsdetailsarraylist = new ArrayList<SYLContactPersonsDetails>();
		Cursor cursor = null;
	 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_detailssyl ";

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
			cursor.close();
			db.close();
		}
	
		return syl_contactpersonsdetailsarraylist;
	}
	public  void clearTable(String tablename) {
		SQLiteDatabase db=null ;
		if (syldatabase != null) {
			try {
				 db = syldatabase.getWritableDatabase();
				db.delete(tablename, null, null);
				//db.close();
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
finally {
	//db.close();
}
		}

	}

	public void updatePositionmarker(String userid) {
		SQLiteDatabase db=null;;
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

		 db = syldatabase.getWritableDatabase();

			String mSelectionClause = "user_id" + " = ? ";
			String[] mSelectionArgs = { userid };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "true");
			int k = db.update("contactpersons_details", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			
			db.close();
		}
	}
	public void updatePositionmarkersylContacts(String userid) {
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

			SQLiteDatabase db = syldatabase.getWritableDatabase();

			String mSelectionClause = "user_id" + " = ? ";
			String[] mSelectionArgs = { userid };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "true");
			int k = db.update("contactpersons_detailssyl", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void updatePositionmarkerFalse() {
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

			SQLiteDatabase db = syldatabase.getWritableDatabase();

			String mSelectionClause = "position_marker" + " = ? ";
			String[] mSelectionArgs = { "true" };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "false");
			int k = db.update("contactpersons_details", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void updatePositionmarkersylcontactsFalse() {
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

			SQLiteDatabase db = syldatabase.getWritableDatabase();

			String mSelectionClause = "position_marker" + " = ? ";
			String[] mSelectionArgs = { "true" };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "false");
			int k = db.update("contactpersons_detailssyl", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<SYLContactPersonsDetails> getFavouritesContactswithvalue(
			String filtervalue) {
		String query;
		SQLiteDatabase db=null;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLContactPersonsDetails> syl_contactpersonsdetailsarraylist = new ArrayList<SYLContactPersonsDetails>();
		Cursor cursor = null;
	 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_details where name LIKE '" +"%"
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
			cursor.close();
			db.close();
		}

		return syl_contactpersonsdetailsarraylist;
	}
	public ArrayList<SYLContactPersonsDetails> getsylContactswithvalue(
			String filtervalue) {
		String query;
		SQLiteDatabase db=null;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLContactPersonsDetails> syl_contactpersonsdetailsarraylist = new ArrayList<SYLContactPersonsDetails>();
		Cursor cursor = null;
	 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_detailssyl where name LIKE '"+"%"
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
			cursor.close();
			db.close();
		}
	
		return syl_contactpersonsdetailsarraylist;
	}
	public void insertPhoneContacts(
			ArrayList<SYLPhoneContacts> msylphonecontacts) {
		if (msylphonecontacts != null && msylphonecontacts.size() > 0) {
			clearTable("contactpersons_detailsphonecontacts");
			for (SYLPhoneContacts syl_phonecontacts : msylphonecontacts) {
				this.insertphoneContactPersonsDetail(
						syl_phonecontacts, this.context);

			}
		}

	}
	private void insertphoneContactPersonsDetail(
			SYLPhoneContacts msylphonecontats, Context c) {
		SQLiteDatabase db=null;
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);

			 db = syldatabase.getWritableDatabase();
			ContentValues values;
			values = new ContentValues();
		
			values.put("name", msylphonecontats.getName());
			values.put("email", msylphonecontats.getEmail());
			values.put("profile_image",
					msylphonecontats.getProfileimageurl());
			values.put("position_marker", "false");
			values.put("contact_type", "phonecontacts");
			 
			values.put("mobilenumber",msylphonecontats.getMobilenumber());
			values.put("image",SYLUtil.getBytes(msylphonecontats.getBitmap()));
			db.insert("contactpersons_detailsphonecontacts", null, values);
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
finally {
	db.close();
}
	}
	public ArrayList<SYLPhoneContacts> getPhoneContacts() {
		String query;
		SQLiteDatabase db =null;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLPhoneContacts> syl_contactpersonsdetailsarraylist = new ArrayList<SYLPhoneContacts>();
		Cursor cursor = null;
		 db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_detailsphonecontacts ORDER BY name COLLATE NOCASE ASC";

			if (cursor == null)
				cursor = db.rawQuery(query, null);

			while (cursor.moveToNext()) {
				SYLPhoneContacts  contactpersondetail = new SYLPhoneContacts();
				contactpersondetail.setName(cursor.getString(cursor
						.getColumnIndex("name")));

				String user_id = cursor.getString(cursor
						.getColumnIndex("ID"));
				contactpersondetail.setId(Integer.parseInt(user_id));

				contactpersondetail.setEmail(cursor.getString(cursor
						.getColumnIndex("email")));
				contactpersondetail.setProfileimageurl(cursor.getString(cursor
						.getColumnIndex("profile_image")));
				contactpersondetail.setPositiommark(cursor.getString(cursor
						.getColumnIndex("position_marker")));
				contactpersondetail.setMobilenumber(cursor.getString(cursor
						.getColumnIndex("mobilenumber")));
				byte[] image=cursor.getBlob(cursor.getColumnIndex("image"));
				if(image!=null){
				contactpersondetail.setBitmap(SYLUtil.getImage(image));}
				syl_contactpersonsdetailsarraylist.add(contactpersondetail);

			}
		} catch (Exception e) {
Log.e("Exception", e.getMessage());
		}
		finally {
			cursor.close();
			db.close();
		}

		return syl_contactpersonsdetailsarraylist;
	}
	public ArrayList<SYLPhoneContacts> getphoneContactswithvalue(
			String filtervalue) {
		String query;
		SQLiteDatabase db;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLPhoneContacts> syl_contactpersonsdetailsarraylist = new ArrayList<SYLPhoneContacts>();
		Cursor cursor = null;
 db = syldatabase.getReadableDatabase();
		try {
		
			
			query = "select * from contactpersons_detailsphonecontacts where name LIKE  '"+"%"
					+ filtervalue + "%" + "'  order by name COLLATE NOCASE    ASC                 ";
				
		 
			if (cursor == null)
				cursor = db.rawQuery(query, null);

			while (cursor.moveToNext()) {
				SYLPhoneContacts contactpersondetail = new SYLPhoneContacts();
				contactpersondetail.setName(cursor.getString(cursor
						.getColumnIndex("name")));

				String user_id = cursor.getString(cursor
						.getColumnIndex("ID"));
				contactpersondetail.setId(Integer.parseInt(user_id));

				contactpersondetail.setEmail(cursor.getString(cursor
						.getColumnIndex("email")));
				contactpersondetail.setProfileimageurl(cursor.getString(cursor
						.getColumnIndex("profile_image")));
				contactpersondetail.setPositiommark(cursor.getString(cursor
						.getColumnIndex("position_marker")));
				contactpersondetail.setMobilenumber(cursor.getString(cursor
						.getColumnIndex("mobilenumber")));
				byte[] image=cursor.getBlob(cursor.getColumnIndex("image"));
				if(image!=null){
				contactpersondetail.setBitmap(SYLUtil.getImage(image));}
				syl_contactpersonsdetailsarraylist.add(contactpersondetail);

			}
		} catch (Exception e) {

		}
		finally {
			cursor.close();
			db.close();
		}

		return syl_contactpersonsdetailsarraylist;
	}
	public void updatePositionmarkerphonecontactsFalse() {
		
		SQLiteDatabase db=null;;
		try {
		
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

		 db = syldatabase.getWritableDatabase();

			String mSelectionClause = "position_marker" + " = ? ";
			String[] mSelectionArgs = { "true" };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "false");
			int k = db.update("contactpersons_detailsphonecontacts", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			//db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		db.close();
		}
	}
	
	public void updatePositionmarkerphoneContacts(String userid) {
		SQLiteDatabase db=null;
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

	 db = syldatabase.getWritableDatabase();

			String mSelectionClause = "ID" + " = ? ";
			String[] mSelectionArgs = { userid };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "true");
			int k = db.update("contactpersons_detailsphonecontacts", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			//db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.close();
		}
	}
	
	public void deletefavoritesContactwithuserid(String userid)
	{
		SQLiteDatabase db=null;

		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			 db = syldatabase.getWritableDatabase();
				String mSelectionClause = "user_id" + " = ? ";
				String[] mSelectionArgs = {userid};
			
		int		mRowsDeleted=db.delete("contactpersons_details", mSelectionClause, mSelectionArgs);
		Log.e("rows updated", "" + mRowsDeleted);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.close();
		}
	}
	public void insertPhoneContacts2(
			ArrayList<SYLPhoneContacts2> msylphonecontacts) {
		SQLiteDatabase db = null;
		try {

			String sql = "INSERT INTO " + "contactpersons_detailsphonecontacts2" + " VALUES (?,?,?,?);";




			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);

			db = syldatabase.getWritableDatabase();
			SQLiteStatement statement = db.compileStatement(sql);
			db.beginTransaction();

			if (msylphonecontacts != null && msylphonecontacts.size() > 0) {
				clearTable("contactpersons_detailsphonecontacts2");
				ContentValues[] v = new ContentValues[msylphonecontacts.size()];
				int i = 0;
				for (SYLPhoneContacts2 syl_phonecontacts : msylphonecontacts) {

					statement.clearBindings();
					statement.bindString(1, syl_phonecontacts.getName());
					statement.bindString(2, syl_phonecontacts.getProfileimageurl());
					statement.bindString(3, "false");
					statement.bindString(4, syl_phonecontacts.getContactid());


					statement.execute();

				}
				db.setTransactionSuccessful();
				db.endTransaction();


			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
		finally {

		}

	}
	public ArrayList<SYLPhoneContacts2> getPhoneContacts2(Context c) {
		String query;
		SQLiteDatabase db = null;

		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);

		ArrayList<SYLPhoneContacts2> syl_contactpersonsdetailsarraylist = new ArrayList<SYLPhoneContacts2>();
		Cursor cursor = null;
		db = syldatabase.getReadableDatabase();
		try {

			query = "select * from contactpersons_detailsphonecontacts2 order by name COLLATE NOCASE    ASC ";

			if (cursor == null)
				cursor = db.rawQuery(query, null);
			while (cursor.moveToNext()) {
				SYLPhoneContacts2 contactpersondetail = new SYLPhoneContacts2();
				String name=cursor.getString(cursor
						.getColumnIndex("name"));
				String photouri=cursor.getString(cursor
						.getColumnIndex("profile_image"));
				contactpersondetail.setName(cursor.getString(cursor
						.getColumnIndex("name")));
				contactpersondetail.setProfileimageurl(cursor.getString(cursor
						.getColumnIndex("profile_image")));
				contactpersondetail.setPositiommark(cursor.getString(cursor
						.getColumnIndex("position_marker")));
				contactpersondetail.setContactid(cursor.getString(cursor
						.getColumnIndex("contactid")));

				syl_contactpersonsdetailsarraylist.add(contactpersondetail);

			}
		}
		catch (Exception e)
		{
			Log.e("Error",e.getMessage());
		}
		return syl_contactpersonsdetailsarraylist;
	}
	public ArrayList<SYLPhoneContacts2> getphoneContactswithvalue2(
			String filtervalue) {
		String query;
		SQLiteDatabase db;
		if (syldatabase == null)
			syldatabase = new SYLDatabase(this.context);
		ArrayList<SYLPhoneContacts2> syl_contactpersonsdetailsarraylist = new ArrayList<SYLPhoneContacts2>();
		Cursor cursor = null;
		db = syldatabase.getReadableDatabase();
		try {


			query = "select * from contactpersons_detailsphonecontacts2 where name LIKE  '"+"%"
					+ filtervalue + "%" + "'  order by name COLLATE NOCASE    ASC                 ";


			if (cursor == null)
				cursor = db.rawQuery(query, null);

			while (cursor.moveToNext()) {
				SYLPhoneContacts2 contactpersondetail = new SYLPhoneContacts2();
				String name=cursor.getString(cursor
						.getColumnIndex("name"));
				String photouri=cursor.getString(cursor
						.getColumnIndex("profile_image"));
				contactpersondetail.setName(cursor.getString(cursor
						.getColumnIndex("name")));

				contactpersondetail.setProfileimageurl(cursor.getString(cursor
						.getColumnIndex("profile_image")));
				contactpersondetail.setPositiommark(cursor.getString(cursor
						.getColumnIndex("position_marker")));
				contactpersondetail.setContactid(cursor.getString(cursor
						.getColumnIndex("contactid")));

				syl_contactpersonsdetailsarraylist.add(contactpersondetail);

			}
		} catch (Exception e) {

		}
		finally {
			cursor.close();
			db.close();
		}

		return syl_contactpersonsdetailsarraylist;
	}
	public void updatePositionmarkerphonecontactsFalse2() {

		SQLiteDatabase db=null;;
		try {

			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

			db = syldatabase.getWritableDatabase();

			String mSelectionClause = "position_marker" + " = ? ";
			String[] mSelectionArgs = { "true" };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "false");
			int k = db.update("contactpersons_detailsphonecontacts2", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			//db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.close();
		}
	}
	public void updatePositionmarkerphoneContacts2(String userid) {
		SQLiteDatabase db=null;
		try {
			if (syldatabase == null)
				syldatabase = new SYLDatabase(context);
			ContentValues mUpdateValues = new ContentValues();

			db = syldatabase.getWritableDatabase();

			String mSelectionClause = "contactid" + " = ? ";
			String[] mSelectionArgs = { userid };
			int mRowsUpdated = 0;
			mUpdateValues.put("position_marker", "true");
			int k = db.update("contactpersons_detailsphonecontacts2", mUpdateValues,
					mSelectionClause, mSelectionArgs);
			//db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			db.close();
		}
	}
	
}
