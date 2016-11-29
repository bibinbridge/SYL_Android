package com.webcamconsult.syl.modelmanager;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.databaseaccess.SYLContactPersonsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLGmailFacebookContactsManager;
import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.interfaces.SYLGmailContactsListener;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.model.SYLGmailContactResponsemodel;
import com.webcamconsult.syl.model.SYLSigninResponseModel;
import com.webcamconsult.syl.networkoperations.AsyncWebHandler;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLContactsModelManager {
	public SYLContactsListener mContactsListener;
	public SYLGmailContactsListener sylgmailcontactlistener;

	Context mContext;
public String gmailAccesstoken,refreshtoken;
String userid,timezone,accesstoken;

	public void getFavouriteContactpersons(String method, Context mContext,String userid,String timezone,String accesstoken) 
	{
		// String contactsresponse =
		// "{\"success\":\"true\",\"SYLContactPersonsDetails\":[{\"user_id\":11,\"email\":\"john@gmail.com\",\"first_name\":\"Amal\",\"profileimage_url\":\"http://lh5.ggpht.com/_mrb7w4gF8Ds/TCpetKSqM1I/AAAAAAAAD2c/Qef6Gsqf12Y/s144-c/_DSC4374%20copy.jpg\"},{\"user_id\":12,\"email\":\"john@gmail.com\",\"first_name\":\"Praveen\",\"profileimage_url\":\"http://lh6.ggpht.com/_TQY-Nm7P7Jc/TBpjA0ks2MI/AAAAAAAABcI/J6ViH98_poM/s144-c/IMG_6517.jpg\"},{\"user_id\":13,\"email\":\"john@gmail.com\",\"first_name\":\"Ajay\",\"profileimage_url\":\"http://lh6.ggpht.com/_qvCl2efjxy0/TCIVI-TkuGI/AAAAAAAAOUY/vbk9MURsv48/s144-c/DSC_0844.JPG\"},{\"user_id\":14,\"email\":\"john@gmail.com\",\"first_name\":\"Mridhul\",\"profileimage_url\":\"http://lh4.ggpht.com/_XjNwVI0kmW8/TCOwNtzGheI/AAAAAAAAC84/SxFJhG7Scgo/s144-c/0014.jpg\"},{\"user_id\":15,\"email\":\"john@gmail.com\",\"first_name\":\"Dheeraj\",\"profileimage_url\":\"http://lh3.ggpht.com/_0YSlK3HfZDQ/TCExCG1Zc3I/AAAAAAAAX1w/9oCH47V6uIQ/s144-c/3138923889_a7fa89cf94_o.jpg\"},{\"user_id\":16,\"email\":\"john@gmail.com\",\"first_name\":\"Anildas\",\"profileimage_url\":\"http://lh6.ggpht.com/_0YSlK3HfZDQ/TCEx16nJqpI/AAAAAAAAX1c/R5Vkzb8l7yo/s144-c/4235400281_34d87a1e0a_o.jpg\"},{\"user_id\":17,\"email\":\"john@gmail.com\",\"first_name\":\"Krishnadas\",\"profileimage_url\":\"http://lh5.ggpht.com/_mrb7w4gF8Ds/TCpetKSqM1I/AAAAAAAAD2c/Qef6Gsqf12Y/s144-c/_DSC4374%20copy.jpg\"},{\"user_id\":18,\"email\":\"john@gmail.com\",\"first_name\":\"Vijay\",\"profileimage_url\":\"http://lh5.ggpht.com/_loGyjar4MMI/S-InWuHkR9I/AAAAAAAADJE/wD-XdmF7yUQ/s144-c/Colorado%20River%20Sunset.jpg\"}]}";
		// SYLContacts mSYLcontacts = null;
		//
		// //
		//
		// Gson gson = new Gson();
		// // JsonReader reader = new JsonReader(new StringReader(result));
		// // reader.setLenient(true);
		// try {
		// mSYLcontacts = gson.fromJson(contactsresponse, SYLContacts.class);
		// if (mSYLcontacts != null) {
		// insertSYLFavouritesContact(c, mSYLcontacts);
		// }
		//
		// Log.e("Response", "Response");
		// } catch (Exception e) {
		// Log.e("error", e.getMessage());
		// }
		// mContactsListener.onDidFinishGetContacts(mSYLcontacts);

		this.mContext = mContext;

this.accesstoken=accesstoken;
this.userid=userid;
this.timezone=timezone;
		new AsyncWebHandler() {

			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException {
				try {

					// TODO Auto-generated method stub
					HttpPost httppost = new HttpPost(
							(SYLUtil.SYL_BASEURL+"api/User/FetchFavoriteContacts"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");

					JSONObject holder = new JSONObject();
					holder.put("userId", Integer.parseInt(SYLContactsModelManager.this.userid));
					holder.put("accessToken",
							SYLContactsModelManager.this.accesstoken);
					holder.put("timeZone",SYLContactsModelManager.this.timezone);
					// passes the results to a string builder/entity
					StringEntity se = new StringEntity(holder.toString());
					httppost.setEntity(se);

					return httppost;

					// httppost.setEntity(new
					// UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					Log.e("Error", "Error");
				}

				return null;

			}

			@Override
			public void onResponse(String result) {
				// TODO Auto-generated method stub

				String contactsresponse = result;
				SYLContacts mSYLcontacts = null;
				Gson gson = new Gson();
				;
				try {
					Log.e("favourites-result", result);
					mSYLcontacts = gson.fromJson(contactsresponse,
							SYLContacts.class);
					if (mSYLcontacts != null) {
						insertSYLFavouritesContact(
								SYLContactsModelManager.this.mContext,
								mSYLcontacts);
					}

				} catch (Exception e) {

				}
				mContactsListener.onDidFinishGetContacts(mSYLcontacts);

			}

		}.execute("post");

	}

	public void getSYLContactpersons(String method, Context c,String userid,String timezone,String accesstoken) {
		/*
		 * String contactsresponse =
		 * "{\"success\":\"true\",\"SYLContactPersonsDetails\":[{\"user_id\":11,\"email\":\"john@gmail.com\",\"first_name\":\"Adithya Ramesh\",\"profileimage_url\":\"http://lh3.ggpht.com/_rfAz5DWHZYs/S9cLAeKuueI/AAAAAAAAeYU/E19G8DOlJRo/s144-c/DSC_4397_8_9_tonemapped2.jpg\"},{\"user_id\":12,\"email\":\"john@gmail.com\",\"first_name\":\"Rahul\",\"profileimage_url\":\"http://lh4.ggpht.com/_TQY-Nm7P7Jc/TBpi6rKfFII/AAAAAAAABbg/79FOc0Dbq0c/s144-c/david_lee_sakura.jpg\"},{\"user_id\":13,\"email\":\"john@gmail.com\",\"first_name\":\"Prakash\",\"profileimage_url\":\"http://lh5.ggpht.com/_6_dLVKawGJA/SMwq86HlAqI/AAAAAAAAG5U/q1gDNkmE5hI/s144-c/mobius-glow.jpg\"},{\"user_id\":14,\"email\":\"john@gmail.com\",\"first_name\":\"Mridhul\",\"profileimage_url\":\"http://lh3.ggpht.com/_QFsB_q7HFlo/TCItc19Jw3I/AAAAAAAAFs4/nPfiz5VGENk/s144-c/4551649039_852be0a952_o.jpg\"},{\"user_id\":15,\"email\":\"john@gmail.com\",\"first_name\":\"Gopal\",\"profileimage_url\":\"http://lh4.ggpht.com/_XjNwVI0kmW8/TCOwNtzGheI/AAAAAAAAC84/SxFJhG7Scgo/s144-c/0014.jpg\"},{\"user_id\":16,\"email\":\"john@gmail.com\",\"first_name\":\"Anildas\",\"profileimage_url\":\"http://lh6.ggpht.com/_0YSlK3HfZDQ/TCEx16nJqpI/AAAAAAAAX1c/R5Vkzb8l7yo/s144-c/4235400281_34d87a1e0a_o.jpg\"},{\"user_id\":17,\"email\":\"john@gmail.com\",\"first_name\":\"Krishnadas\",\"profileimage_url\":\"http://farm4.staticflickr.com/3773/9049175264_b0ea30fa75_s.jpg\"},{\"user_id\":18,\"email\":\"john@gmail.com\",\"first_name\":\"Rejin\",\"profileimage_url\":\"http://lh3.ggpht.com/_TQY-Nm7P7Jc/TBpi8EJ4eDI/AAAAAAAABb0/AZ8Cw1GCaIs/s144-c/Hokkaido%20Swans.jpg\"}]}"
		 * ; SYLContacts mSYLcontacts = null;
		 * 
		 * //
		 * 
		 * Gson gson = new Gson(); // JsonReader reader = new JsonReader(new
		 * StringReader(result)); // reader.setLenient(true); try { mSYLcontacts
		 * = gson.fromJson(contactsresponse, SYLContacts.class);
		 * if(mSYLcontacts!=null) { insertSYLContactsyl(c,mSYLcontacts); }
		 * Log.e("Response", "Response"); } catch (Exception e) { Log.e("error",
		 * e.getMessage()); }
		 * mContactsListener.onDidFinishGetContacts(mSYLcontacts);
		 */
		this.mContext = c;
		this.userid=userid;
		this.timezone=timezone;
		this.accesstoken=accesstoken;

		new AsyncWebHandler() {

			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException {
				try {

					// TODO Auto-generated method stub
					HttpPost httppost = new HttpPost(
							(SYLUtil.SYL_BASEURL+"api/User/FetchSYLContacts"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");

					JSONObject holder = new JSONObject();
					holder.put("userId",Integer.parseInt(SYLContactsModelManager.this.userid));
					holder.put("accessToken",
							SYLContactsModelManager.this.accesstoken);
					holder.put("timeZone",
							SYLContactsModelManager.this.timezone);
					// passes the results to a string builder/entity
					StringEntity se = new StringEntity(holder.toString());
					httppost.setEntity(se);

					return httppost;

					// httppost.setEntity(new
					// UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					Log.e("Error", "Error");
				}

				return null;

			}

			@Override
			public void onResponse(String result) {
				// TODO Auto-generated method stub
				Log.e("sylcontact-result", result);
				String contactsresponse = result;
				SYLContacts mSYLcontacts = null;
				Gson gson = new Gson();
				;
				try {
					mSYLcontacts = gson.fromJson(contactsresponse,
							SYLContacts.class);
					if (mSYLcontacts != null) {
			insertSYLContactsyl(mContext, mSYLcontacts);
					}

				} catch (Exception e) {

				}
				mContactsListener.onDidFinishGetContacts(mSYLcontacts);

			}

		}.execute("post");
	}

	public void getFacebookContacts(String facebookaccesstoken, String method,
			Context mContext) {
		String contactsresponse = "{\"success\":\"true\",\"SYLContactPersonsDetails\":[{\"user_id\":1,\"email\":\"john@gmail.com\",\"first_name\":\"Shankar\",\"profileimage_url\":\"http://lh3.ggpht.com/_1aZMSFkxSJI/TCIjB6od89I/AAAAAAAACHM/CLWrkH0ziII/s144-c/079.jpg\"},{\"user_id\":2,\"email\":\"john@gmail.com\",\"first_name\":\"Sreeraj\",\"profileimage_url\":\"http://lh3.ggpht.com/_M3slUPpIgmk/SlbnavqG1cI/AAAAAAAACvo/z6-CnXGma7E/s144-c/mf_003.jpg\"},{\"user_id\":3,\"email\":\"john@gmail.com\",\"first_name\":\"Sreejith\",\"profileimage_url\":\"http://lh5.ggpht.com/_6_dLVKawGJA/SMwq86HlAqI/AAAAAAAAG5U/q1gDNkmE5hI/s144-c/mobius-glow.jpg\"},{\"user_id\":4,\"email\":\"john@gmail.com\",\"first_name\":\"Mridhul\",\"profileimage_url\":\"http://lh4.ggpht.com/_loGyjar4MMI/S-InQvd_3hI/AAAAAAAADIw/dHvCFWfyHxQ/s144-c/Rainbokeh.jpg\"},{\"user_id\":5,\"email\":\"john@gmail.com\",\"first_name\":\"Gopal\",\"profileimage_url\":\"http://lh4.ggpht.com/_XjNwVI0kmW8/TCOwNtzGheI/AAAAAAAAC84/SxFJhG7Scgo/s144-c/0014.jpg\"},{\"user_id\":6,\"email\":\"john@gmail.com\",\"first_name\":\"Nikhil\",\"profileimage_url\":\"http://lh6.ggpht.com/_0YSlK3HfZDQ/TCEx16nJqpI/AAAAAAAAX1c/R5Vkzb8l7yo/s144-c/4235400281_34d87a1e0a_o.jpg\"},{\"user_id\":7,\"email\":\"john@gmail.com\",\"first_name\":\"Krishnadas\",\"profileimage_url\":\"http://lh5.ggpht.com/_mrb7w4gF8Ds/TCpetKSqM1I/AAAAAAAAD2c/Qef6Gsqf12Y/s144-c/_DSC4374%20copy.jpg\"},{\"user_id\":8,\"email\":\"john@gmail.com\",\"first_name\":\"Mohandas\",\"profileimage_url\":\"http://lh6.ggpht.com/_iGI-XCxGLew/S-iYQWBEG-I/AAAAAAAACB8/JuFti4elptE/s144-c/norvig-polar-bear.jpg\"}]}";
		SYLContacts mSYLcontacts = null;

		//

		Gson gson = new Gson();
		// JsonReader reader = new JsonReader(new StringReader(result));
		// reader.setLenient(true);
		try {
			mSYLcontacts = gson.fromJson(contactsresponse, SYLContacts.class);
			if (mSYLcontacts != null) {
				insertSYLfacebookContacts(mContext, mSYLcontacts);
			}
			Log.e("Response", "Response");
		} catch (Exception e) {
			Log.e("error", e.getMessage());
		}
		mContactsListener.onDidFinishGetContacts(mSYLcontacts);
	}

	public void getGmailContacts(String userId,String SYLAccessToken,String gmailaccesstoken, String refreshtoken,String method,
			Context mContext,String timezone) {
	this.accesstoken=SYLAccessToken;
	this.gmailAccesstoken=gmailaccesstoken;
	this.refreshtoken=refreshtoken;
this.userid=userId;
this.mContext=mContext;
this.timezone=timezone;
		new AsyncWebHandler() {

			@Override
			public HttpUriRequest postHttpRequestMethod()
					throws UnsupportedEncodingException {
				try {

					// TODO Auto-generated method stub
					HttpPost httppost = new HttpPost(
							(SYLUtil.SYL_BASEURL+"api/Contacts/FetchGoogleContacts"));
					httppost.addHeader("www-request-type", "SYL2WAPP07459842");
					httppost.addHeader("www-request-api-version", "1.0");
					httppost.addHeader("Content-Type", "application/json");

					JSONObject holder = new JSONObject();
					holder.put("userId",Integer.parseInt(SYLContactsModelManager.this.userid));
					holder.put("accessToken",
							SYLContactsModelManager.this.accesstoken);
					holder.put("shareableToken",
							gmailAccesstoken);
					holder.put("refreshToken",
							SYLContactsModelManager.this.refreshtoken);
					holder.put("timeZone",
							SYLContactsModelManager.this.timezone);
					// passes the results to a string builder/entity
					Log.e("Gmail contasct request",holder.toString());
					StringEntity se = new StringEntity(holder.toString());
					httppost.setEntity(se);

					return httppost;

					// httppost.setEntity(new
					// UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					Log.e("Error", "Error");
				}

				return null;

			}

			@Override
			public void onResponse(String result) {
				// TODO Auto-generated method stub
				Log.e("sylgmailcontact-result", result);
				String contactsresponse = result;
				SYLGmailContactResponsemodel mResponseModel = null;
				Gson gson = new Gson();
				;
				try {
					mResponseModel = gson.fromJson(contactsresponse,
							SYLGmailContactResponsemodel.class);
					if (mResponseModel != null) {

//insertSYLgmailContacts(SYLContactsModelManager.this.mContext, mResponseModel);


					}

				} catch (Exception e) {

				}
				sylgmailcontactlistener.finishGmailContacts(mResponseModel);

			}

		}.execute("post");
		
		
		
		
		
		
		
		
		
		
		
		
	}

	private void insertSYLFavouritesContact(Context mContext,
			SYLContacts msylcontacts) {
		
		SYLContactPersonsdatamanager mdatamangaer = new SYLContactPersonsdatamanager(
				mContext);
		mdatamangaer.clearTable("contactpersons_details");
		SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
				mContext);
		datamanager.insertFavouritesContacts(msylcontacts.getUsers());

	}

	private void insertSYLContactsyl(Context mContext, SYLContacts msylcontacts) {
		SYLContactPersonsdatamanager datamanager = new SYLContactPersonsdatamanager(
				mContext);
		datamanager.insertsylContacts(msylcontacts.getUsers());

	}

	private void insertSYLfacebookContacts(Context mContext,
			SYLContacts msylcontacts) {
		SYLGmailFacebookContactsManager datamanager = new SYLGmailFacebookContactsManager(
				mContext);
		datamanager.insertFacebookContacts(msylcontacts.getUsers());

	}

	private void insertSYLgmailContacts(Context mContext,
			SYLGmailContactResponsemodel mResponSeModel) {
		SYLGmailFacebookContactsManager mdatamangaer = new SYLGmailFacebookContactsManager(
				mContext);
		mdatamangaer.clearTable("contactpersons_detailsgmail");
		
		
		
		
		SYLGmailFacebookContactsManager datamanager = new SYLGmailFacebookContactsManager(
				mContext);
		datamanager.insertGmailContacts(mResponSeModel.getGoogleContacts());

	}
}
