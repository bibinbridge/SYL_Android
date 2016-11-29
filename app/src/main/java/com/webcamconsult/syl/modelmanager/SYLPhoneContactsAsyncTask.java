package com.webcamconsult.syl.modelmanager;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.interfaces.SYLPhoneContactsListener;
import com.webcamconsult.syl.model.SYLPhoneContacts;
import com.webcamconsult.syl.providers.SYLSaveValues;

public class SYLPhoneContactsAsyncTask extends AsyncTask<Void, Void, Void> {
	public SYLPhoneContactsListener mListener;
	SYLFragmentChangeActivity m;
	ArrayList<SYLPhoneContacts> mphonecontacts = new ArrayList<SYLPhoneContacts>();

	public SYLPhoneContactsAsyncTask(SYLFragmentChangeActivity m) {
		this.m = m;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		fetchContacts();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		mListener.getPhoneContactsFinish(mphonecontacts);

	}

	public void fetchContacts() {
String syluserphonenumber= SYLSaveValues.getSYLMobileNumber(this.m);
		String phoneNumber = null;
		String email = null;
		String photouri;
		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
		String PHOTO = ContactsContract.CommonDataKinds.Phone.PHOTO_URI;
		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String DATA = ContactsContract.CommonDataKinds.Email.DATA;

		StringBuffer output = new StringBuffer();

		ContentResolver contentResolver = m.getContentResolver();
String sortorder=DISPLAY_NAME+" ASC";
		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
				sortorder);

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String contact_id = cursor
						.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(DISPLAY_NAME));

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));

				if (hasPhoneNumber > 0) {

					output.append("\n First Name:" + name);
					SYLPhoneContacts phonecontacts = new SYLPhoneContacts();
				
					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(
							PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						try {
							phoneNumber = phoneCursor.getString(phoneCursor
									.getColumnIndex(NUMBER));

							phonecontacts.setName(name);

							phonecontacts.setMobilenumber(phoneNumber.replaceAll("\\s+", ""));
							photouri = phoneCursor.getString(phoneCursor
									.getColumnIndex(PHOTO));
							if(photouri!=null){
							Uri uri = Uri.parse(photouri);
							Bitmap bitmap = MediaStore.Images.Media
									.getBitmap(m.getContentResolver(),
											Uri.parse(photouri));
							Bitmap scaledBitmap = Bitmap.createScaledBitmap(
									bitmap, 150, 150, true);
							phonecontacts.setBitmap(scaledBitmap);
							phonecontacts.setProfileimageurl(photouri);
							output.append("\n Phone number:" + phoneNumber);
							}
					
					
						} catch (Exception e) {
						}
					}

					phoneCursor.close();

					// Query and loop for every email of the contact
					Cursor emailCursor = contentResolver.query(
							EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?",
							new String[] { contact_id }, null);
email="";
					while (emailCursor.moveToNext()) {

						email = emailCursor.getString(emailCursor
								.getColumnIndex(DATA));

						output.append("\nEmail:" + email);

					}
					phonecontacts.setEmail(email);
					emailCursor.close();
					if(!syluserphonenumber.equals(phoneNumber.replaceAll("\\s+",""))) {
					 //No need to add the Logged in user's own phone number
						mphonecontacts.add(phonecontacts);
					}

				}

				output.append("\n");
			}
			insertPhoneContacts(m, mphonecontacts);

		}
	}

	private void
	insertPhoneContacts(Context mContext,
			ArrayList<SYLPhoneContacts> msylphonecontacts) {
		SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
				mContext);
		datamanager.insertPhoneContacts(msylphonecontacts);

	}
}
