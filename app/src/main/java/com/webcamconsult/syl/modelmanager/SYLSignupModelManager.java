package com.webcamconsult.syl.modelmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.webcamconsult.syl.interfaces.SYLSignupListener;
import com.webcamconsult.syl.model.SYLSignupError;
import com.webcamconsult.syl.model.SYLSignupResponseModel;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLSignupModelManager {

	public SYLSignupResponseModel msylsignupresponsemodel;
	public SYLSignupListener msylsignuplistener;
	String name, email, password, facetimeid, skypeid, facebookid, googleid,
			mobilenumber, countrycode, timezone, devicetoken, latitude,
			longitude, isprofileimageuploaded, file_path;
	SYLSignupError msylsignuperror;
	public void doSignup(String name, String email, String password,
			String facetimeid, String skypeid, String facebookid,
			String googleid, String mobilenumber, String countrycode,
			String timeZone, String devicetoken, String latitude,
			String longitude, String isProfileimageUploaded, String file_path) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.facetimeid = facetimeid;
		this.skypeid = skypeid;
		this.facebookid = facebookid;
		this.googleid = googleid;
		this.mobilenumber = mobilenumber;
		this.countrycode = countrycode;
		this.timezone = timeZone;
		this.devicetoken = devicetoken;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isprofileimageuploaded = isProfileimageUploaded;
		this.file_path = file_path;

		new SignupTask().execute(SYLUtil.SYL_BASEURL+"api/user/create");
	}

	class SignupTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
String signupurl;
			try {
signupurl=params[0].toString();
				HttpClient httpClient = new DefaultHttpClient();

				HttpPost postRequest = new HttpPost(
						signupurl);
				postRequest.addHeader("www-request-type", "SYL2MAPP07459842");
				postRequest.addHeader("www-request-api-version", "1.0");
				
				InputStream is;

				StringBody namebody = new StringBody(name);
				StringBody emailbody = new StringBody(email);
				StringBody passwordbody = new StringBody(password);
				StringBody facetimeidbody = new StringBody(facetimeid);
				StringBody skypeidbody = new StringBody(skypeid);
				StringBody facebookidbody = new StringBody("");
				StringBody googleidbody = new StringBody(googleid);
				StringBody mobilenumberbody = new StringBody(mobilenumber);
				StringBody countrycodebody = new StringBody(countrycode);
				StringBody timezonebody = new StringBody(timezone);
				StringBody devicetokenbody = new StringBody(devicetoken);
				StringBody devicetypebody = new StringBody("ANDROID");
				StringBody latitudebody = new StringBody(latitude);
				StringBody longitudebody = new StringBody(longitude);
				StringBody isprofileimageuploadbody = new StringBody(
						isprofileimageuploaded);
				MultipartEntity multipartContent = new MultipartEntity();

				File f = new File(file_path);
				multipartContent.addPart("file", new FileBody(f));

				// multipartContent.addPart("image", isb);
				multipartContent.addPart("name", namebody);
				multipartContent.addPart("email", emailbody);
				multipartContent.addPart("password", passwordbody);
				multipartContent.addPart("facetimeId", facetimeidbody);
				multipartContent.addPart("skypeId", skypeidbody);
				multipartContent.addPart("facebookId", facebookidbody);
				multipartContent.addPart("googleId", googleidbody);
				multipartContent.addPart("mobile", mobilenumberbody);
				multipartContent.addPart("countryCode", countrycodebody);
				multipartContent.addPart("timeZone", timezonebody);
				multipartContent.addPart("deviceUid", devicetokenbody);
				multipartContent.addPart("deviceType",devicetypebody);
				multipartContent.addPart("latitude", latitudebody);
				multipartContent.addPart("longitude", longitudebody);
				multipartContent.addPart("isProfileImageUploaded",
						isprofileimageuploadbody);
				postRequest.setEntity(multipartContent);
				HttpResponse response = httpClient.execute(postRequest);
				// response.getEntity().getContent().close();
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				String contentAsString = getString(is);
				Log.e("response signup", contentAsString);
				return contentAsString;
			} catch (Exception e) {
				return "error_message";
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

	
			try {
				Log.e("Signupresponse", result);
				Gson gson = new Gson();
				msylsignupresponsemodel = gson.fromJson(result,
						SYLSignupResponseModel.class);

			} catch (Exception e) {
				msylsignupresponsemodel = null;
			}
			msylsignuplistener.onDidFinished(msylsignupresponsemodel);
		}
		

		}
		public String getString(InputStream is) throws IOException {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}

			return writer.toString();
		}

	}


