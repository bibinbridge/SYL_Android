package com.webcamconsult.syl.modelmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.adapters.SYLPhoneContactsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.interfaces.SYLPhoneContactsListener;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.model.SYLPhoneContacts;

public class SYLPhoneContactsModelManager {
public	SYLPhoneContactsListener msylphonecontactslistener;
	public SYLFragmentChangeActivity mSylfragmentchangeActivity;
	ArrayList<SYLPhoneContacts> mphonecontacts;
	ArrayList<SYLPhoneContacts> msylphonecontacts;
public void getSYLPhoneContacts(SYLFragmentChangeActivity mSylfragmentchangeActivity)
{
	this.mSylfragmentchangeActivity=mSylfragmentchangeActivity;
	new Phonecontacts().execute();
//ArrayList<SYLPhoneContacts> msylphonecontacts=getContacts1();

//msylphonecontactslistener.getPhoneContactsFinish(msylphonecontacts);
}
private ArrayList<SYLPhoneContacts> getContacts1() {
	 mphonecontacts=new ArrayList<SYLPhoneContacts>();
String email="";
String phonenumber="";
	Cursor cursor =

	this.mSylfragmentchangeActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
			null, null, null);

	if (cursor != null && cursor.getCount() > 0) {
		while (cursor.moveToNext()) {
			SYLPhoneContacts phonecontacts=new SYLPhoneContacts();
		
			long id = cursor.getLong(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));

			// get firstName & lastName
			Cursor nameCur =this.mSylfragmentchangeActivity. getContentResolver().query(
					ContactsContract.Data.CONTENT_URI,
					null,
					ContactsContract.Data.MIMETYPE + " = ? AND "
							+ StructuredName.CONTACT_ID + " = ?",
					new String[] { StructuredName.CONTENT_ITEM_TYPE,
							Long.valueOf(id).toString() }, null);
			if (nameCur != null) {
				if (nameCur.moveToFirst()) {

					String displayName = nameCur
							.getString(nameCur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				
					if (displayName == null)
						displayName = "";
					Log.e("Name", displayName);
					//String name=displayName.trim();
phonecontacts.setName(" Ashok");
					// String firstName =
					// nameCur.getString(nameCur.getColumnIndex(StructuredName.GIVEN_NAME));
					// if (firstName == null) firstName = "";
					// Log.d("--> ",
					// firstName.length()>0?firstName:displayName);
					//
					// String middleName =
					// nameCur.getString(nameCur.getColumnIndex(StructuredName.MIDDLE_NAME));
					// if (middleName == null) middleName = "";
					//
					// String lastName =
					// nameCur.getString(nameCur.getColumnIndex(StructuredName.FAMILY_NAME));
					// if (lastName == null) lastName = "";
					//
					// lastName = middleName +
					// (middleName.length()>0?" ":"") + lastName;
					//
					// Log.d("--> ", lastName);
				}
				nameCur.close();
			}
			   Cursor emailCur = this.mSylfragmentchangeActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{Long.toString(id)},null);
			   while (emailCur.moveToNext()) { 
               email = emailCur.getString( emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                   Log.e("Email2",email);
               } 
				phonecontacts.setEmail(email);   
			   emailCur.close();
			   Cursor phones =this.mSylfragmentchangeActivity. getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{Long.toString(id)}, null);
			   while (phones.moveToNext())
			   {

				   phonenumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				   if(phonenumber==null){phonenumber="";}
Log.e("phone number",phonenumber);
			   }
			   phonecontacts.setMobilenumber(phonenumber);
			   phones.close();
			   Bitmap photo = null;

			try {
				InputStream inputStream = ContactsContract.Contacts
						.openContactPhotoInputStream(
					this.mSylfragmentchangeActivity.			getContentResolver(),
								ContentUris
										.withAppendedId(
												ContactsContract.Contacts.CONTENT_URI,
												Long.valueOf(id)));
				Uri uri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI,
						Long.valueOf(id));
		
String uriString=uri.toString();
if(uriString==null){uriString="";}
phonecontacts.setProfileimageurl(uriString);

Log.e("URI String",uriString);
				if (inputStream != null) {
					photo = BitmapFactory.decodeStream(inputStream);
				}

				if (inputStream != null)
					inputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			mphonecontacts.add(phonecontacts);
		}
	
	}

	if (cursor != null) {
		cursor.close();
	}
	insertPhoneContacts(this.mSylfragmentchangeActivity,mphonecontacts);
return mphonecontacts;
}











private void insertPhoneContacts(Context mContext,
		ArrayList<SYLPhoneContacts> msylphonecontacts) {
	SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
			mContext);
	datamanager.insertPhoneContacts(msylphonecontacts);
			
	
}

class Phonecontacts extends AsyncTask<Void, Void, ArrayList<SYLPhoneContacts>>
{

	@Override
	protected ArrayList<SYLPhoneContacts> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		msylphonecontacts=new ArrayList<SYLPhoneContacts>();
		  StringBuffer sb = new StringBuffer();
		  sb.append("......Contact Details.....");
		  ContentResolver cr = mSylfragmentchangeActivity.getContentResolver();
		  Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
		    null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
		  String phone = null;
		  String emailContact = null;
		  String emailType = null;
		  String image_uri = "";
		  Bitmap bitmap = null;
		  if (cur.getCount() > 0) {
		   while (cur.moveToNext()) {
			   SYLPhoneContacts s=new SYLPhoneContacts();
		    String id = cur.getString(cur
		      .getColumnIndex(ContactsContract.Contacts._ID));
		    String name = cur
		      .getString(cur
		        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		    if(name!=null){
		    String trimname=name.trim();
s.setName(trimname);}
		    image_uri = cur
		      .getString(cur
		        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
		    s.setProfileimageurl(image_uri);
		    image_uri=null;
		    if (Integer
		      .parseInt(cur.getString(cur
		        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		 //    System.out.println("name : " + name + ", ID : " + id);
		     sb.append("\n Contact Name:" + name);
		     Cursor pCur = cr.query(
		       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		       null,
		       ContactsContract.CommonDataKinds.Phone.CONTACT_ID
		         + " = ?", new String[] { id }, null);
		     while (pCur.moveToNext()) {
		      phone = pCur
		        .getString(pCur
		          .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		      sb.append("\n Phone number:" + phone);
		  //    System.out.println("phone" + phone);
		     }
		     s.setMobilenumber(phone);
		     pCur.close();

		     Cursor emailCur = cr.query(
		       ContactsContract.CommonDataKinds.Email.CONTENT_URI,
		       null,
		       ContactsContract.CommonDataKinds.Email.CONTACT_ID
		         + " = ?", new String[] { id }, null);
		     while (emailCur.moveToNext()) {
		      emailContact = emailCur
		        .getString(emailCur
		          .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		      emailType = emailCur
		        .getString(emailCur
		          .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
		      sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
		//      System.out.println("Email " + emailContact + " Email Type : " + emailType);

		     }


		     emailCur.close();
		    }
		    s.setEmail(emailContact);  
		    emailContact=null;
//		    if (image_uri != null) {
//		     System.out.println(Uri.parse(image_uri));
//		     try {
//		      bitmap = MediaStore.Images.Media
//		        .getBitmap(getActivity().getContentResolver(),
//		          Uri.parse(image_uri));
//		      sb.append("\n Image in Bitmap:" + bitmap);
//		      System.out.println(bitmap);
//
//		     } catch (FileNotFoundException e) {
//		      // TODO Auto-generated catch block
//		      e.printStackTrace();
//		     } catch (IOException e) {
//		      // TODO Auto-generated catch block
//		      e.printStackTrace();
//		     }
//
//		    }
		   
		    
		    sb.append("\n........................................");
		    msylphonecontacts.add(s);
		   }

	
		  }
		  return msylphonecontacts;
	}
@Override
protected void onPostExecute(ArrayList<SYLPhoneContacts> result) {
	// TODO Auto-generated method stub
	super.onPostExecute(result);
//	mPhoneContactsAdapter = new SYLPhoneContactsAdapter(getActivity(),
//			R.layout.sylcontacts_eachrow,
//			result, getActivity());
//
//	mPhonecontactsListview.setAdapter(mPhoneContactsAdapter);
	insertPhoneContacts(mSylfragmentchangeActivity,result);
	msylphonecontactslistener.getPhoneContactsFinish(result);
}

	

}

}
