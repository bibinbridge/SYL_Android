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
import com.webcamconsult.syl.model.SYLSignupResponseModel;
import com.webcamconsult.syl.modelmanager.SYLSignupModelManager.SignupTask;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLProfileUpdateModelManager {
	public SYLSignupListener msylSignupListener;
	String name, email, facetimeid, skypeid, facebookid, googleid,
			mobilenumber, countrycode, timezone, devicetoken, latitude,
			longitude, isprofileimageuploaded, file_path,accesstoken;

	public void doProfileUpdate(String name,String email,String facetimeid,String skypeid,String facebookid,String googleid,String mobilenumber,String countrycode,String timezone,String devicetoken,String latitude,String longitude,String isprofileimageuploaded,String file_path,String accesstoken) {
		this.name=name;
		this.email=email;
		this.facetimeid=facetimeid;
		this.skypeid=skypeid;
		this.facebookid=facebookid;
		this.googleid=googleid;
		this.mobilenumber=mobilenumber;
		this.countrycode=countrycode;
		this.timezone=timezone;
		this.devicetoken=devicetoken;
		this.latitude=latitude;
		this.longitude=longitude;
		this.isprofileimageuploaded=isprofileimageuploaded;
		this.file_path=file_path;
		this.accesstoken=accesstoken;
		
		
		
		
		new UpdateProfileDetailsAsync()
				.execute(SYLUtil.SYL_BASEURL+"api/User/Update");

	}

	class UpdateProfileDetailsAsync extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String profileupdateurl;
			try {
				profileupdateurl = params[0].toString();
				HttpClient httpClient = new DefaultHttpClient();

				HttpPost postRequest = new HttpPost(profileupdateurl);
				postRequest.addHeader("www-request-type", "SYL2MAPP07459842");
				postRequest.addHeader("www-request-api-version", "1.0");

				InputStream is;

				StringBody namebody = new StringBody(name);
				StringBody emailbody = new StringBody(email);

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
				StringBody accesstokenbody = new StringBody(accesstoken);
				StringBody isprofileimageuploadbody = new StringBody(
						isprofileimageuploaded);
				MultipartEntity multipartContent = new MultipartEntity();
if(!file_path.equals("")){
				File profileimagefile = new File(file_path);
				multipartContent.addPart("file", new FileBody(profileimagefile));
}

				// multipartContent.addPart("image", isb);
				multipartContent.addPart("name", namebody);
				multipartContent.addPart("email", emailbody);

				multipartContent.addPart("facetimeId", facetimeidbody);
				multipartContent.addPart("skypeId", skypeidbody);
				multipartContent.addPart("facebookId", facebookidbody);
				multipartContent.addPart("googleId", googleidbody);
				multipartContent.addPart("mobile", mobilenumberbody);
				multipartContent.addPart("countryCode", countrycodebody);
				multipartContent.addPart("timeZone", timezonebody);
				multipartContent.addPart("deviceUid", devicetokenbody);
				multipartContent.addPart("deviceType", devicetypebody);
				multipartContent.addPart("latitude", latitudebody);
				multipartContent.addPart("longitude", longitudebody);
				multipartContent.addPart("accessToken", accesstokenbody);
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

			SYLSignupResponseModel msylSignupresponsemodel=null;
			try {
				Log.e("profileupdateresponse", result);
				Gson gson = new Gson();
				msylSignupresponsemodel = gson.fromJson(result,
						SYLSignupResponseModel.class);

			} catch (Exception e) {
msylSignupresponsemodel=null;
			}
msylSignupListener.onDidFinished(msylSignupresponsemodel);
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

}
