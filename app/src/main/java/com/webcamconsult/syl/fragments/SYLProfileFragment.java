package com.webcamconsult.syl.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.activities.SYLMenuFragment;
import com.webcamconsult.syl.activities.SYLSignupActivity;
import com.webcamconsult.syl.activities.SYLVerificationCodeActivity;
import com.webcamconsult.syl.adapters.SYLCountrycodeadapter;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.SYLFetchUserDetailsListener;
import com.webcamconsult.syl.interfaces.SYLGoogleImageDownloadInterface;
import com.webcamconsult.syl.interfaces.SYLKeyboardcloseInterface;
import com.webcamconsult.syl.interfaces.SYLPasswordChangeListener;
import com.webcamconsult.syl.interfaces.SYLSignupListener;
import com.webcamconsult.syl.model.SYLFetchUserDetailsResponseModel;
import com.webcamconsult.syl.model.SYLPasswordChangeResponseModel;
import com.webcamconsult.syl.model.SYLSignupResponseModel;
import com.webcamconsult.syl.modelmanager.SYLProfileUpdateModelManager;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.GraphicsUtil;
import com.webcamconsult.syl.utilities.SYLGoogleImageDownload;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLFetchUserDetailsViewManager;
import com.webcamconsult.viewmanager.SYLPasswordChangeViewManager;
import com.webcamconsult.viewmanager.SYLProfileUpdateViewManager;

public class SYLProfileFragment extends Fragment implements
		SYLGoogleImageDownloadInterface, SYLPasswordChangeListener,
		SYLFetchUserDetailsListener,SYLSignupListener {
	ListView listView;
	EditText musernameEdittext, mEmailEditText, mSkypeEditText,
			mHangoutEditText, mMobilenumberEdittext;
	EditText mPasswordEditText, mconfirmPasswordEditText,
			mConfirmnewpasswordEditText;
	EditText mEdittextFacetimeId;
	TextView mCountrycodeTextView;
	ProgressDialog msylProgressDialog;
	String[] countries_list;
	ImageView picView,picview1;
	final int CAMERA_CAPTURE = 1;
	final int GALLERY_CAPTURE = 3;
	private Uri picUri;
	String profileimagepathlocal = "";
	String profileimagepathgallery;
	boolean cameraflag = false;
	public Bitmap cameraBitmap;
	Bitmap camerabitmap;
	Button mBtn_passwordchange, mbtnUserUpdate;
String  profileimageeditflag="false";
String profileimagepath;
public boolean editflag=false;
Button mButton;
	int orientation=0;
	public Bitmap correctedBitMap;
	public SYLProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_myprofile, null);
		
		musernameEdittext = (EditText) view.findViewById(R.id.etxt_username);
		mbtnUserUpdate = (Button) view.findViewById(R.id.btn_userupdate);
		mEmailEditText = (EditText) view.findViewById(R.id.etxt_email);
		mSkypeEditText = (EditText) view.findViewById(R.id.etxt_skypeid);
	mEdittextFacetimeId=	(EditText) view.findViewById(R.id.etxt_facetimeid);
		mHangoutEditText = (EditText) view.findViewById(R.id.etxt_hangouticon);
		mBtn_passwordchange = (Button) view
				.findViewById(R.id.btn_changepassword);
		mMobilenumberEdittext = (EditText) view
				.findViewById(R.id.etxt_mobilenumber);
		mCountrycodeTextView = (TextView) view
				.findViewById(R.id.txt_countrycode);
		mPasswordEditText = (EditText) view.findViewById(R.id.etxt_password);
		mconfirmPasswordEditText = (EditText) view
				.findViewById(R.id.etxt_confirmpassword);
		mConfirmnewpasswordEditText = (EditText) view
				.findViewById(R.id.etxt_confirmnewpassword);
	//	picView = (ImageView) view.findViewById(R.id.img_userimage);
		picview1 = (ImageView) view.findViewById(R.id.row_icon);
		musernameEdittext.setEnabled(false);
		mEmailEditText.setEnabled(false);
		mEdittextFacetimeId.setEnabled(false);
		mSkypeEditText.setEnabled(false);
		mHangoutEditText.setEnabled(false);
		mMobilenumberEdittext.setEnabled(false);
		mCountrycodeTextView.setEnabled(false);
		mPasswordEditText.setEnabled(false);
		mconfirmPasswordEditText.setEnabled(false);
		mConfirmnewpasswordEditText.setEnabled(false);

		picview1.setEnabled(false);
		setFonts();
		getProfileDetails();
		// String[] listViewItems = new String[] { "Symbian", "iOS",
		// "Windows Phone", "Blackberry", "Meego", "Symbian", "Blackberry",
		// "Meego", "Symbian", "Blackberry", "Meego", "Symbian"};
		// listView = (ListView)
		// view.findViewById(R.id.fragment_listview_listview);
		// listView.setAdapter(new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, listViewItems));

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);


			Log.e("saved-instance", "-->" + savedInstanceState);
			FragmentActivity i = getActivity();
			SlidingFragmentActivity k = (SlidingFragmentActivity) i;

			k.getSupportActionBar().setCustomView(R.layout.action_custom_profile);

			View v = k.getSupportActionBar().getCustomView();
			// View v=getS
			TextView mhead = (TextView) v.findViewById(R.id.mytext);
			mButton = (Button) v.findViewById(R.id.btnnew);
			mhead.setText("My Profile");
			Log.e("onactivity create", "onactivitycreate");
			mButton.setText("Edit");


			mButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("edit pressed", "edit pressed");
					editflag = !editflag;
					setFieldEditFlag(editflag);
					//	picview1.setEnabled(true);

				}
			});
			mBtn_passwordchange.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doPasswordChange();
				}
			});
			mCountrycodeTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showCountrycode();
				}
			});
			picview1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("open gallery", "opencamera");

					openImagechooseAlertDialog();
				}
			});
			picview1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("open gallery", "opencamera");

					openImagechooseAlertDialog();
				}
			});
			mbtnUserUpdate.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					updateUserDetails();
				}
			});



	}

	private void setFieldEditFlag(boolean flag)
	{
		musernameEdittext.setEnabled(flag);

		mEdittextFacetimeId.setEnabled(flag);
		mSkypeEditText.setEnabled(flag);
		mHangoutEditText.setEnabled(flag);
mEmailEditText.setEnabled(flag);
		mCountrycodeTextView.setEnabled(flag);
		mPasswordEditText.setEnabled(flag);
		mconfirmPasswordEditText.setEnabled(flag);
		mConfirmnewpasswordEditText.setEnabled(flag);
		mMobilenumberEdittext.setEnabled(flag);

		picview1.setEnabled(flag);
		if(flag)
		mButton.setText("Done");
		else {
			mButton.setText("Edit");
		}
		boolean facebookflag= Boolean.parseBoolean(SYLSaveValues.getFacebookConnected(getActivity()));
		if(facebookflag)
		{
			mPasswordEditText.setEnabled(false);
			mconfirmPasswordEditText.setEnabled(false);
			mConfirmnewpasswordEditText.setEnabled(false);

		}
	}
	
	
	
	
	private void setFonts() {
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/myriadproregular.OTF");
		mPasswordEditText.setTypeface(font);
		mconfirmPasswordEditText.setTypeface(font);
		mConfirmnewpasswordEditText.setTypeface(font);
		mEmailEditText.setTypeface(font);
		musernameEdittext.setTypeface(font);
		mSkypeEditText.setTypeface(font);
		mHangoutEditText.setTypeface(font);
		mCountrycodeTextView.setTypeface(font);
		mMobilenumberEdittext.setTypeface(font);
mEdittextFacetimeId.setTypeface(font);
	}

	private void showCountrycode() {
		countries_list = getResources().getStringArray(R.array.countryCodes);

		new AlertDialog.Builder(getActivity())

				.setAdapter(
						new SYLCountrycodeadapter(getActivity(),
								R.layout.syl_spinnereachitem, countries_list),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								mCountrycodeTextView
										.setText(countries_list[which]
												.toString());
								mCountrycodeTextView
										.setTextColor(getResources().getColor(
												R.color.white));
								dialog.dismiss();
							}
						}).create().show();

	}

	private void openImagechooseAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setMessage("Select any one of options");
		alertDialogBuilder.setPositiveButton("Open Camera",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

						openCamera();
					}
				});
		alertDialogBuilder.setNegativeButton("Open Gallery",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						openGallery();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void openCamera() {

		try {
			// use standard intent to capture an image
			Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// we will handle the returned data in onActivityResult
			startActivityForResult(captureIntent, CAMERA_CAPTURE);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support capturing images!";
			Toast toast = Toast.makeText(getActivity(), errorMessage,
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	public void openGallery() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		SYLProfileFragment.this.startActivityForResult(i, GALLERY_CAPTURE);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("on success", "on success");
		Bitmap s = null;

		if (resultCode == getActivity().RESULT_OK) {
			// user is returning from capturing an image using the camera
			if (requestCode == CAMERA_CAPTURE) {
				Log.e("camera", "camera");
				picUri = data.getData();
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				savetoaFileLocation(thePic);
				thePic = getResizedBitmap(thePic, 200, 200);
				cameraBitmap = thePic;
				// ImageView picView = (ImageView)
				// findViewById(R.id.img_userimage);
				// display the returned cropped image
				profileimageeditflag="true";
				GraphicsUtil graphicUtil = new GraphicsUtil();
				// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
				
				picview1.setImageBitmap(graphicUtil.getCircleBitmap(thePic, 16));
				//picview1.setImageBitmap(cameraBitmap);
			} else if (requestCode == GALLERY_CAPTURE) {




				Uri selectedImage = data.getData();

				String stringUri;
				stringUri = selectedImage.toString();
				if ((stringUri != "") || stringUri != null) {

					String selectedImagePath = getPath(selectedImage);
try {
	ExifInterface exifInterface = new ExifInterface(selectedImagePath);
	orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

}
catch (Exception e){}



					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getActivity().getContentResolver().query(
							selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String selectedimage_path = cursor.getString(columnIndex);
					Log.e("selected path",selectedimage_path);
					cursor.close();
					if (selectedimage_path.startsWith("https://")) {
						getBiymapFromGoogleLocation(selectedimage_path);
						return;
					} else {
						s = getBitmapFromFile(selectedimage_path);
					}
					s = getResizedBitmap(s, 200, 200);
					cameraBitmap = s;

					switch(orientation) {
						case ExifInterface.ORIENTATION_ROTATE_90:
							correctedBitMap = rotateImage(cameraBitmap, 90);
							break;

						case ExifInterface.ORIENTATION_ROTATE_180:
							correctedBitMap = rotateImage(cameraBitmap, 180);
							break;

						case ExifInterface.ORIENTATION_ROTATE_270:
							correctedBitMap = rotateImage(cameraBitmap, 270);
							break;
						default:
							correctedBitMap=cameraBitmap;
							break;
					}










					// ImageView picView = (ImageView)
					// findViewById(R.id.img_userimage);
					// display the returned cropped image
profileimageeditflag="true";
					GraphicsUtil graphicUtil = new GraphicsUtil();


	/*				if(correctedBitMap!= null)
					{
						picView.setImageBitmap(graphicUtil.getCircleBitmap(
								correctedBitMap, 16));
					}
					else
					{
						// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
						picView.setImageBitmap(graphicUtil.getCircleBitmap(
								cameraBitmap, 16));
					}

*/

					if (correctedBitMap != null)
					{
						picview1.setImageBitmap(graphicUtil.getCircleBitmap(correctedBitMap, 16));
						savetoaFileLocation(correctedBitMap);
					}
					else {
						picview1.setImageBitmap(graphicUtil.getCircleBitmap(s, 16));
						savetoaFileLocation(s);
					}



					// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
			
				//	picview1.setImageBitmap(graphicUtil.getCircleBitmap(s, 16));
				//	picview1.setImageBitmap(cameraBitmap);
				//	savetoaFileLocation(s);
				}
			}

		}
	}

	private Bitmap getBitmapFromFile(String filepath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
		return bitmap;

	}
	private String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor =getActivity(). getContentResolver().query(uri, projection, null, null, null);

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}
	private Bitmap rotateImage(Bitmap source, float angle) {

		Bitmap bitmap = null;
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		try {
			bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
					matrix, true);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	private void savetoaFileLocation(Bitmap bmp) {

		FileOutputStream out = null;
		try {


			String filepath = Environment.getExternalStorageDirectory()
					.toString();

			File wallpaperDirectory = new File(filepath + "/ProfileImages/");
			// have the object build the directory structure, if needed.
			wallpaperDirectory.mkdirs();
			File outputFile = new File(wallpaperDirectory, "flower.png");
			out = new FileOutputStream(outputFile);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your
																// Bitmap
																// instance
			// PNG is a lossless format, the compression factor (100) is ignored

			profileimagepath = filepath + "/ProfileImages/" + "flower.png";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;
	}

	private Bitmap circle_image(String file_path) {

		Bitmap mIcon11 = null;
		InputStream in = null;
		File f = new File(file_path);
		try {

			final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
			in = new FileInputStream(f);
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			Log.d("TAG", "scale = " + scale + ", orig-width: " + o.outWidth
					+ ", orig-height: " + o.outHeight);

			Bitmap b = null;
			in = new FileInputStream(f);

			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int width = b.getHeight();
				int height = b.getWidth();
				Log.d("TAG", "1th scale operation dimenions - width: " + width
						+ ",height: " + height);

				double y = Math.sqrt(IMAGE_MAX_SIZE
						/ (((double) width) / height));
				double x = (y / height) * width;

				// Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
				// (int) y, true);
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, 150, 150,
						true);
				b.recycle();
				b = scaledBitmap;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			Log.d("TAG", "bitmap size - width: " + b.getWidth() + ", height: "
					+ b.getHeight());

			/*
			 * 
			 * try { String path =
			 * Environment.getExternalStorageDirectory().toString
			 * ()+"/LazyList/a.png"; FileOutputStream out = new
			 * FileOutputStream(path); b.compress(Bitmap.CompressFormat.PNG, 90,
			 * out); out.close(); } catch (Exception e) { e.printStackTrace(); }
			 */

			return b;
		} catch (IOException e) {
			Log.e("TAG", e.getMessage(), e);
			return null;
		}

	}



	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		String filepath;
		String imagesource;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			imagesource = urls[1];
			Bitmap mIcon11 = null;
			Bitmap scaledBitmap = null;
			InputStream in = null;
			try {
				if (imagesource.equals("URL")) {
					in = new java.net.URL(urldisplay).openStream();
				} else if (imagesource.equals("gallery")) {
					in = new FileInputStream(urldisplay);
				}

				mIcon11 = BitmapFactory.decodeStream(in);
				scaledBitmap = Bitmap.createScaledBitmap(mIcon11, 150, 150,
						false);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

			try {

				// now attach the OutputStream to the file object, instead of a
				// String representation

				filepath = Environment.getExternalStorageDirectory().toString();
				profileimagepathlocal = filepath + "/LazyImages/flower.png";
				File wallpaperDirectory = new File(filepath + "/LazyImages/");
				// have the object build the directory structure, if needed.
				wallpaperDirectory.mkdirs();
				File outputFile = new File(wallpaperDirectory, "flower.png");
				FileOutputStream out = new FileOutputStream(outputFile);
				scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return scaledBitmap;
		}

		protected void onPostExecute(Bitmap result) {
			// bmImage.setImageBitmap(result);

			Bitmap b = circle_image(profileimagepathlocal);

			if (cameraflag) {
				Matrix matrix = new Matrix();

				matrix.postRotate(90);

				Bitmap rotatedBitmap = Bitmap.createBitmap(b, 0, 0,
						b.getWidth(), b.getHeight(), matrix, true);
				// muserimageview.setImageBitmap(rotatedBitmap);
				camerabitmap = rotatedBitmap;
				SYLSaveValues.setUserImageSource("camera", getActivity());
			} else {
				SYLSaveValues.setUserImageSource("gallery", getActivity());
				// muserimageview.setImageBitmap(b);
				camerabitmap = b;
			}
		}

	}

	private void getBiymapFromGoogleLocation(String googleimagepath) {
		SYLGoogleImageDownload downloadtask = new SYLGoogleImageDownload(
				getActivity());

		downloadtask.mGoogleimagedownloadlistener = SYLProfileFragment.this;

		downloadtask.execute(googleimagepath);

	}

	@Override
	public void onImageDownloadFinish(Bitmap googleimagebitmap) {
		// TODO Auto-generated method stub

	}

	private void doPasswordChange() {


		boolean facebookflag= Boolean.parseBoolean(SYLSaveValues.getFacebookConnected(getActivity()));
		if(facebookflag) {
			SYLUtil.buildAlertMessage(getActivity(),"You are logged in as Facebook user. So you can't change password");
			return;
		}
		if(editflag) {
			if (validatePasswordFields()) {
				if (SYLUtil.isNetworkAvailable(getActivity())) {

					String sylaccesstoken = SYLSaveValues
							.getSYLaccessToken(getActivity());
					String userid = SYLSaveValues.getSYLUserID(getActivity());
					String oldpassword = mPasswordEditText.getText().toString();
					String newpassword = mconfirmPasswordEditText.getText()
							.toString();
					String confirmnewpassword = mConfirmnewpasswordEditText
							.getText().toString();

					if (validatePasswords()) {

						if (newpassword.equals(confirmnewpassword)) {
							msylProgressDialog = new ProgressDialog(getActivity());
							msylProgressDialog.setMessage("Please wait...");
							msylProgressDialog.setCancelable(false);
							msylProgressDialog.setCanceledOnTouchOutside(false);
							msylProgressDialog.show();
							String timezone = SYLUtil.getTimeGMTZone(getActivity());
							SYLPasswordChangeViewManager mViewmanager = new SYLPasswordChangeViewManager();
							mViewmanager.mPasswordChangeListener = SYLProfileFragment.this;
							mViewmanager.doPasswordChange(userid, sylaccesstoken,
									oldpassword, newpassword, timezone);
						} else {
							Toast.makeText(getActivity(),
									"Password not seems to be matched",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getActivity(),
								"current password and new password seems as same.Please try a new password",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		else {
			SYLUtil.buildAlertMessage(getActivity(),"Pleases enable profile edit mode");
		}
	}
private boolean validatePasswords()
{
boolean flag=true;
	String currentpassword=mPasswordEditText.getText().toString();
	String newpassword=mconfirmPasswordEditText.getText()
			.toString();
	if(currentpassword.equals(newpassword))
	{
		flag=false;
	}
return flag;
}
	@Override
	public void finishPasswordChange(
			SYLPasswordChangeResponseModel mPasswordchangeresponsemodel) {
		// TODO Auto-generated method stub
mConfirmnewpasswordEditText.clearFocus();
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		try {
			if (mPasswordchangeresponsemodel != null) {
				if (mPasswordchangeresponsemodel.isSuccess()) {
					SYLUtil.buildAlertMessage(getActivity(),
							"Changed password successfully");
				} else {
					SYLUtil.buildAlertMessage(getActivity(),
							mPasswordchangeresponsemodel.getError().getErrorDetail());
				}
			}
		} catch (Exception e) {
			SYLUtil.buildAlertMessage(getActivity(),
					"Unexpected server error happened");
		}
	}

	private boolean validatePasswordFields() {
		boolean valid = true;

			if (TextUtils.isEmpty(mPasswordEditText.getText().toString())) {
				valid = false;
				mPasswordEditText.requestFocus();
				mPasswordEditText.setError("Please enter a valid password");

			}
			if (TextUtils.isEmpty(mconfirmPasswordEditText.getText().toString())) {
				valid = false;
				mconfirmPasswordEditText.requestFocus();
				mconfirmPasswordEditText.setError("Please enter a valid password");

			}


		return valid;
	}

	private void updateUserDetails() {
		hideKeyboard();
if(editflag){
String		facebookid = "";
String 		facetimeid = mEdittextFacetimeId.getText().toString();
String 		latitude = "0.0";
String 	longitude = "0.0";
String countrycode=mCountrycodeTextView.getText().toString();
String name=	musernameEdittext.getText().toString();
String  email=mEmailEditText.getText().toString();
String skypeid=mSkypeEditText.getText().toString();
String hangout=mHangoutEditText.getText().toString();
String mobilenumber=mMobilenumberEdittext.getText().toString();
String accesstoken=SYLSaveValues.getSYLaccessToken(getActivity());
String timezone=SYLUtil.getTimeGMTZone(getActivity());
String deviceUID=SYLUtil.getUID(getActivity());
if(profileimageeditflag.equals("false"))
{
profileimagepath="";
}
if(validate()) {

	if (SYLUtil.isNetworkAvailable(getActivity())) {
		msylProgressDialog = new ProgressDialog(getActivity());
		msylProgressDialog.setMessage("Please wait...");
		msylProgressDialog.show();
		SYLProfileUpdateViewManager mviewmanager=new SYLProfileUpdateViewManager();
		mviewmanager.mSylSignupListener=SYLProfileFragment.this;
		mviewmanager.	doProfileUpdate(name, email, facetimeid, skypeid, facebookid, hangout, mobilenumber, countrycode, timezone, deviceUID, latitude, longitude, profileimageeditflag, profileimagepath, accesstoken);	
	}
	
	}
}
else {
	SYLUtil.buildAlertMessage(getActivity(), "please enable profile edit mode");
}
	}
	private void getProfileDetails() {
		if(SYLUtil.isNetworkAvailable(getActivity())) {
			msylProgressDialog = new ProgressDialog(getActivity());
			msylProgressDialog.setMessage("Please wait...");
			msylProgressDialog.setCancelable(false);
			msylProgressDialog.setCanceledOnTouchOutside(false);
			msylProgressDialog.show();
			String accessToken = SYLSaveValues.getSYLaccessToken(getActivity());
			String GMTTImezone = SYLUtil.getTimeGMTZone(getActivity());
			SYLFetchUserDetailsViewManager mViewmanager = new SYLFetchUserDetailsViewManager();
			mViewmanager.mFetchUserDetailsListener = SYLProfileFragment.this;
			mViewmanager.fetchUserDetails(accessToken, GMTTImezone);
		}
		else {
			musernameEdittext.setText(SYLSaveValues.getSYLusername(getActivity()));
			mEmailEditText.setText(SYLSaveValues.getSYLEmailAddress(getActivity()));
			mSkypeEditText.setText(SYLSaveValues.getSylSkypeID(getActivity()));
			mHangoutEditText.setText(SYLSaveValues.getSYLHangoutid(getActivity()));
			mEdittextFacetimeId.setText(SYLSaveValues.getSylFacetimeid(getActivity()));
			mCountrycodeTextView.setText(SYLSaveValues.getSylCountrycode(getActivity()));
			mMobilenumberEdittext.setText(SYLSaveValues.getSYLMobileNumber(getActivity()));
			ImageLoader imageLoader = new ImageLoader(getActivity());
			imageLoader.DisplayImage(SYLSaveValues.getProfileImageurl(getActivity()),picview1);
		}

	}

	@Override
	public void getUserDetailsFinish(
			SYLFetchUserDetailsResponseModel muserresponsemodel) {
		// TODO Auto-generated method stub

		try {
			if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
				msylProgressDialog.dismiss();
			}
			if (muserresponsemodel != null) {
				if (muserresponsemodel.isSuccess()) {

					ImageLoader imageLoader = new ImageLoader(getActivity());
					musernameEdittext.setText(muserresponsemodel.getUser().getName());
					mEmailEditText.setText(muserresponsemodel.getUser().getEmail());
					if (muserresponsemodel.getUser().getSkypeId().equals("")) {
						mSkypeEditText.setText("not available");
					} else {
						mSkypeEditText.setText(muserresponsemodel.getUser().getSkypeId());
					}
					if (muserresponsemodel.getUser().getGoogleId().equals("")) {
						mHangoutEditText.setText("not available");
					} else {
						mHangoutEditText.setText(muserresponsemodel.getUser().getGoogleId());
					}

					mCountrycodeTextView.setText(muserresponsemodel.getUser().getCountryCode());
					mMobilenumberEdittext.setText(muserresponsemodel.getUser().getMobile());
					if (muserresponsemodel.getUser().getFacetimeId().equals("")) {
						mEdittextFacetimeId.setText("not available");
					} else {
						mEdittextFacetimeId.setText(muserresponsemodel.getUser().getFacetimeId());
					}


					imageLoader.DisplayImage(muserresponsemodel.getUser().getProfileImage(), picview1);

					SYLSaveValues.setSYLusername(muserresponsemodel.getUser().getName(), getActivity());
					SYLSaveValues.setProfileImageurl(muserresponsemodel.getUser().getProfileImage(), getActivity());

					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.menu_frame, new SYLMenuFragment())
							.commit();


				} else {
					SYLUtil.buildAlertMessage(getActivity(), muserresponsemodel.getError().getErrorDetail());
				}
			}
		}
		catch (Exception e)
		{
			SYLUtil.buildAlertMessage(getActivity(), "Unexpected server error occured");
		}
	}

	@Override
	public void onDidFinished(SYLSignupResponseModel msylsignupresponsemodel) {
		// TODO Auto-generated method stub
		if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
			msylProgressDialog.dismiss();
		}
		try {
		if(msylsignupresponsemodel!=null)
		{
			if(msylsignupresponsemodel.isSuccess())
			
			{
				SYLSaveValues.setSYLusername("", getActivity());
				SYLSaveValues.setProfileImageurl("", getActivity());
				SYLSaveValues.setSYLusername(msylsignupresponsemodel.getSignupResponse().getName(), getActivity());
				SYLSaveValues.setProfileImageurl(msylsignupresponsemodel.getSignupResponse().getProfileImage(), getActivity());
SYLSaveValues.setSYLEmailAddress(msylsignupresponsemodel.getSignupResponse().getEmail(),getActivity());

			    getActivity().		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new SYLMenuFragment())
				.commit();
				if(msylsignupresponsemodel.getSignupResponse().isVerified())
				{
					SYLUtil.buildAlertMessage(getActivity(),"Profile updated successfully");
				}
			else{
					buildAlertMessage(getActivity(), "Your profile Updated with your changed email.Please enter the verification code which we have send to your" + msylsignupresponsemodel.getSignupResponse().getEmail()+"to complete the validation process");
				}
				
				
			}
			else {
				SYLUtil.buildAlertMessage(getActivity(),msylsignupresponsemodel.getError().getErrorDetail());
			}
		}
		else {
			SYLUtil.buildAlertMessage(getActivity(),"Unexpected server error happened");
		}
		}
	catch(Exception e)	
	{
		SYLUtil.buildAlertMessage(getActivity(),"Unexpected server error happened");
	}
	}
	private boolean validate() {
		boolean valid = true;

		if (TextUtils.isEmpty(musernameEdittext.getText().toString())) {
			valid = false;
			musernameEdittext.requestFocus();
			musernameEdittext.setError("Please enter a valid name");

		}
		if (!SYLUtil.validateEmail(mEmailEditText)) {
			valid = false;

			mEmailEditText.requestFocus();
			mEmailEditText.setError("Please enter a valid email");
			// mEmailEdit.setError(getString(R.string.validate_username,R.drawable.excailm));
			mEmailEditText.requestFocus();
		}
		if (TextUtils.isEmpty(mMobilenumberEdittext.getText().toString())) {
			valid = false;
			mMobilenumberEdittext.requestFocus();
			mMobilenumberEdittext.setError("Please enter a valid contactnumber");

		}

		if (TextUtils.isEmpty(mCountrycodeTextView.getText().toString())) {
			valid = false;
	Toast.makeText(getActivity(), "Please select a country code",Toast.LENGTH_LONG).show();

		}
	

		return valid;

	}
	public void hideKeyboard()
	{
		View view =getActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	public  void buildAlertMessage(Context context, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
												final int id) {
navigatetoVerificationCodeScreen();
								dialog.cancel();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}
	private void navigatetoVerificationCodeScreen()
	{
		Intent verificationIntent = new Intent(
				getActivity(),
				SYLVerificationCodeActivity.class);
		verificationIntent.putExtra("intentfrom", "profile");
		verificationIntent.putExtra("accesstoken", SYLSaveValues.getSYLaccessToken(getActivity()));
		startActivity(verificationIntent);
		getActivity().finish();
	}
}