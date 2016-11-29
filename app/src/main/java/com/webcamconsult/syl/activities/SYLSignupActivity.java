package com.webcamconsult.syl.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.adapters.SYLCountrycodeadapter;

import com.webcamconsult.syl.interfaces.SYLGoogleImageDownloadInterface;

import com.webcamconsult.syl.interfaces.SYLSignupListener;
import com.webcamconsult.syl.model.SYLSignupResponseModel;
import com.webcamconsult.syl.modelmanager.SYLSignupModelManager;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.utilities.GraphicsUtil;
import com.webcamconsult.syl.utilities.SYLGoogleImageDownload;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLSignupviewManager;

public class SYLSignupActivity extends FragmentActivity implements
		SYLGoogleImageDownloadInterface, SYLSignupListener {
	public static final String TAG = "SYLSplashScreenActivity";
	ImageView picView;
	private TextView txtview_countrycode;
	String[] countries_list;
	public ImageView muserimageview;
	private static int RESULT_LOAD_IMAGE = 1;
	String profileimagepathlocal = "";
	String profileimagepathgallery;
	ImageView imageview_backarrow;
	Bitmap camerabitmap;
	boolean cameraflag = false;
	// keep track of camera capture intent
	final int CAMERA_CAPTURE = 1;
	final int GALLERY_CAPTURE = 3;
	public Bitmap cameraBitmap;
	private Uri picUri;
	private Bitmap googleimagebitmap;
	private EditText mNameEdittText, mEmailEditText, mPasswordEditText,
			mConfirmPasswordEditText, mSkypeidEditText, mHangoutEditText,
			mMobilenumberEditText,mFacetimeEditText;
	TextView mTextviewCountrycode;
	Button mSignupButton;
	String name, email, password, confirmpassword, skypeid, hangoutid,
			countrycode, mobilenumber;
	String deviceuid, facebookid, facetimeid, latitude, longitude,
			isprofileimageupload, timezone;
	String profileimagepath = null;
	ProgressDialog msylProgressDialog;
	public String signupindicator;
private boolean IsCountryCodeshowing=false;
	int popup_open;
	int orientation=0;
	public Bitmap correctedBitMap;
	public String accesstoken;
	String progressdialogflag="false";
	static WeakReference<ProgressDialog> Dialog;
	static boolean waiting = false;
	static ProgressDialog mProgressDialog;
static	String verificationcodeshowing="false";
static	String verificationcodemessage,verificationcodeindicator;
	LinearLayout mCountrycodeLinearLayout;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.syl_signuplayout);
		picView = (ImageView) findViewById(R.id.img_userimage);
		initializeViews();

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// muserimageview = (ImageView) findViewById(R.id.imageView1);
		if (icicle != null) {

			cameraBitmap = (Bitmap) icicle.getParcelable("bitmap");
			orientation=(int)icicle.getInt("orientation");




			if (cameraBitmap != null) {
				profileimagepath=icicle.getString("profileimagepath");
				Log.e("profileimagepath-landscpe", profileimagepath);
				GraphicsUtil graphicUtil = new GraphicsUtil();

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
				}
				if(correctedBitMap!= null)
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




				// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
				/*picView.setImageBitmap(graphicUtil.getCircleBitmap(
						cameraBitmap, 16)); */
			}
if(icicle.getString("countrycodeshowing").equals("true"))
{
	IsCountryCodeshowing=true;
	showCountrycode();
	}

			if(icicle.getString("verificationcodeshowing").equals("true"))
			{
				verificationcodeshowing="true";
				verificationcodemessage=icicle.getString("verificationcodemessage");
				verificationcodeindicator=icicle.getString("verificationcodeindicator");
				signupindicator=verificationcodeindicator;
				showAlertDialog(icicle.getString("verificationcodemessage"), icicle.getString("verificationcodeindicator"));
			}

			restoreProgress(icicle);
		}
		picView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openImagechooseAletDialog();
			}
		});
		txtview_countrycode = (TextView) findViewById(R.id.txt_countrycode);

		imageview_backarrow = (ImageView) findViewById(R.id.img_backarrow);

		mCountrycodeLinearLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* This method is for selecting the sountry code */
				IsCountryCodeshowing=true;
				hideKeyboard();
				showCountrycode();
			}
		});
		imageview_backarrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				navigateToSignupScreen();
			}
		});

	}

	private void initializeViews() {
		Typeface font = Typeface.createFromAsset(this.getAssets(),
				"fonts/myriadproregular.OTF");
		mNameEdittText = (EditText) findViewById(R.id.etxt_username);
		mEmailEditText = (EditText) findViewById(R.id.etxt_email);
		mPasswordEditText = (EditText) findViewById(R.id.etxt_password);
		mConfirmPasswordEditText = (EditText) findViewById(R.id.etxt_confirmpassword);
		mSkypeidEditText = (EditText) findViewById(R.id.etxt_skypeid);
		mHangoutEditText = (EditText) findViewById(R.id.etxt_hangouticon);
		mFacetimeEditText=(EditText)findViewById(R.id.etxt_facetimeid);
		mTextviewCountrycode = (TextView) findViewById(R.id.txt_countrycode);
		mMobilenumberEditText = (EditText) findViewById(R.id.etxt_mobilenumber);
		mSignupButton = (Button) findViewById(R.id.btn_signup);
		mCountrycodeLinearLayout=(LinearLayout)findViewById(R.id.etxt_countrycode);
		mNameEdittText.setTypeface(font);
		mEmailEditText.setTypeface(font);
		mPasswordEditText.setTypeface(font);
		mConfirmPasswordEditText.setTypeface(font);
		mSkypeidEditText.setTypeface(font);
		mHangoutEditText.setTypeface(font);
		mTextviewCountrycode.setTypeface(font);
		mMobilenumberEditText.setTypeface(font);
		mSignupButton.setTypeface(font);
		/*
		mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if(!b) {
					if (!SYLUtil.validateEmail(mEmailEditText)) {


						mEmailEditText.setError("Please enter a valid email");
						// mEmailEdit.setError(getString(R.string.validate_username,R.drawable.excailm));
						//mEmailEditText.requestFocus();
					} else {
						mEmailEditText.setError(null);
					}
				}
			}
		});
*/

	}
private void hideKeyboard()
{
	View view = this.getCurrentFocus();
	if (view != null) {
		InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
	public void doUserSignup(View v) {

		facebookid = "";
		facetimeid = mFacetimeEditText.getText().toString();
		latitude = "0.0";
		longitude = "0.0";
		isprofileimageupload = "true";

		name = mNameEdittText.getText().toString();
		email = mEmailEditText.getText().toString();
		password = mPasswordEditText.getText().toString();
		confirmpassword = mConfirmPasswordEditText.getText().toString();
		skypeid = mSkypeidEditText.getText().toString();
		hangoutid = mHangoutEditText.getText().toString();
		mobilenumber = mMobilenumberEditText.getText().toString();
		deviceuid = SYLUtil.getUID(getBaseContext());
		timezone = SYLUtil.getTimeGMTZone(SYLSignupActivity.this);
		countrycode = txtview_countrycode.getText().toString();
		if(validate()) {
		if (SYLUtil.isNetworkAvailable(SYLSignupActivity.this)) {
			waiting = true;
			mProgressDialog = new ProgressDialog(SYLSignupActivity.this);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.show();
			Dialog = new WeakReference<ProgressDialog>(mProgressDialog);


			SYLSignupviewManager msignupviewmanager = new SYLSignupviewManager();
			msignupviewmanager.msylsignuplistener = SYLSignupActivity.this;
			msignupviewmanager.doSignup(SYLSignupActivity.this, name, email,
					password, facetimeid, skypeid, facebookid, hangoutid,
					mobilenumber, countrycode, timezone, deviceuid, latitude,
					longitude, isprofileimageupload, profileimagepath);

		} else {
			SYLUtil.buildAlertMessage(SYLSignupActivity.this,
					getString(R.string.network_alertmessage));
		}
		}
	}

	private void showCountrycode() {
		countries_list = getResources().getStringArray(R.array.countryCodes);

		new AlertDialog.Builder(SYLSignupActivity.this)
.setCancelable(false)
				.setAdapter(
						new SYLCountrycodeadapter(SYLSignupActivity.this,
								R.layout.syl_spinnereachitem, countries_list),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								txtview_countrycode
										.setText(countries_list[which]
												.toString());
								txtview_countrycode.setTextColor(getResources()
										.getColor(R.color.white));
								IsCountryCodeshowing=false;
								dialog.dismiss();
							}
						}).create().show();

	}

	private boolean validate() {
		boolean valid = true;

		if (TextUtils.isEmpty(mNameEdittText.getText().toString())) {
			valid = false;
			mNameEdittText.requestFocus();
			mNameEdittText.setError("Please fill the name field");

		}
		if (!SYLUtil.validateEmail(mEmailEditText)) {
			valid = false;

			mEmailEditText.requestFocus();
			mEmailEditText.setError("Please enter a valid email");
			// mEmailEdit.setError(getString(R.string.validate_username,R.drawable.excailm));
			//mEmailEditText.requestFocus();
		}
		if (TextUtils.isEmpty(mPasswordEditText.getText().toString())) {
			valid = false;
			mPasswordEditText.requestFocus();
			mPasswordEditText.setError("Please enter a valid password");

		}
		if (TextUtils.isEmpty(txtview_countrycode.getText().toString())) {
			valid = false;
	Toast.makeText(SYLSignupActivity.this, "Please fill the country code field",Toast.LENGTH_LONG).show();

		}
		if(! password.equals(confirmpassword)          )
		{
			valid = false;
			Toast.makeText(SYLSignupActivity.this, "Password must be same",Toast.LENGTH_LONG).show();
		}
		if(profileimagepath==null)
		{
			Toast.makeText(SYLSignupActivity.this, "Please add your image",Toast.LENGTH_LONG).show();
			valid=false;
		}
		if(TextUtils.isEmpty(mMobilenumberEditText.getText().toString()))
		{
			valid=false;
			mMobilenumberEditText.requestFocus();
			mMobilenumberEditText.setError("Please fill the mobilenumber field");
		}
		return valid;

	}

	private void navigateToSignupScreen() {
		Intent signuintent = new Intent(SYLSignupActivity.this,
				SYLSigninActivity.class);
		startActivity(signuintent);
		finish();

	}

	public void openImageChooser(View v) {
		// Intent i = new Intent(Intent.ACTION_PICK,
		// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// startActivityForResult(i, 1);

		// openCamera();
		openImagechooseAletDialog();

	}

	public void openGallery() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, GALLERY_CAPTURE);

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

	// private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	// ImageView bmImage;
	// String filepath;
	// String imagesource;
	//
	// public DownloadImageTask(ImageView bmImage) {
	// this.bmImage = bmImage;
	// }
	//
	// protected Bitmap doInBackground(String... urls) {
	// String urldisplay = urls[0];
	// imagesource = urls[1];
	// Bitmap mIcon11 = null;
	// Bitmap scaledBitmap = null;
	// InputStream in = null;
	// try {
	// if (imagesource.equals("URL")) {
	// in = new java.net.URL(urldisplay).openStream();
	// } else if (imagesource.equals("gallery")) {
	// in = new FileInputStream(urldisplay);
	// }
	//
	// mIcon11 = BitmapFactory.decodeStream(in);
	// scaledBitmap = Bitmap.createScaledBitmap(mIcon11, 150, 150,
	// false);
	// } catch (Exception e) {
	// Log.e("Error", e.getMessage());
	// e.printStackTrace();
	// }
	//
	// try {
	//
	// // now attach the OutputStream to the file object, instead of a
	// // String representation
	//
	// filepath = Environment.getExternalStorageDirectory().toString();
	// profileimagepathlocal = filepath + "/LazyImages/flower.png";
	// File wallpaperDirectory = new File(filepath + "/LazyImages/");
	// // have the object build the directory structure, if needed.
	// wallpaperDirectory.mkdirs();
	// File outputFile = new File(wallpaperDirectory, "flower.png");
	// FileOutputStream out = new FileOutputStream(outputFile);
	// scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
	// out.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return scaledBitmap;
	// }
	//
	// protected void onPostExecute(Bitmap result) {
	// // bmImage.setImageBitmap(result);
	//
	// Bitmap b = circle_image(profileimagepathlocal);
	//
	// if (cameraflag) {
	// Matrix matrix = new Matrix();
	//
	// matrix.postRotate(90);
	//
	// Bitmap rotatedBitmap = Bitmap.createBitmap(b, 0, 0,
	// b.getWidth(), b.getHeight(), matrix, true);
	// muserimageview.setImageBitmap(rotatedBitmap);
	// camerabitmap = rotatedBitmap;
	// SYLSaveValues.setUserImageSource("camera",
	// SYLSignupActivity.this);
	// } else {
	// SYLSaveValues.setUserImageSource("gallery",
	// SYLSignupActivity.this);
	// muserimageview.setImageBitmap(b);
	// camerabitmap = b;
	// }
	// }
	//
	// }

	public void openCamera() {

		try {
			// use standard intent to capture an image
			Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// we will handle the returned data in onActivityResult
			startActivityForResult(captureIntent, CAMERA_CAPTURE);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support capturing images!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	private void openImagechooseAletDialog() {
		popup_open=1;

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("Select any one of options");
		alertDialogBuilder.setPositiveButton("Open Camera",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						popup_open=0;
						openCamera();

					}
				});
		alertDialogBuilder.setNegativeButton("Open Gallery",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						popup_open=0;
						openGallery();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bitmap s = null;
		if (resultCode == RESULT_OK) {
			// user is returning from capturing an image using the camera
			if (requestCode == CAMERA_CAPTURE) {
				int orientation=0;
				// get the Uri for the captured image
				picUri = data.getData();
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				savetoaFileLocation(thePic);
				thePic = getResizedBitmap(thePic, 200, 200);
				cameraBitmap = thePic;
				ImageView picView = (ImageView) findViewById(R.id.img_userimage);
				// display the returned cropped image

				GraphicsUtil graphicUtil = new GraphicsUtil();
				// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
				picView.setImageBitmap(graphicUtil.getCircleBitmap(thePic, 16));
				// carry out the crop operation
				// performCrop();
			}
			// user is returning from cropping the image

			else if (requestCode == GALLERY_CAPTURE) {
				Uri selectedImage = data.getData();
				String stringUri;
				stringUri = selectedImage.toString();
				if ((stringUri != "") || stringUri != null) {

					String selectedImagePath = getPath(selectedImage);
					try{
						ExifInterface exifInterface = new ExifInterface(selectedImagePath);
						orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
					}
					catch(Exception e)
					{

					}











					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String selectedimage_path = cursor.getString(columnIndex);
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




					ImageView picView = (ImageView) findViewById(R.id.img_userimage);
					// display the returned cropped image

					GraphicsUtil graphicUtil = new GraphicsUtil();
					// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
					if (correctedBitMap != null)
					{
						picView.setImageBitmap(graphicUtil.getCircleBitmap(correctedBitMap, 16));
						savetoaFileLocation(correctedBitMap);
					}
					else {
						picView.setImageBitmap(graphicUtil.getCircleBitmap(s, 16));
						savetoaFileLocation(s);
					}




					//picView.setImageBitmap(graphicUtil.getCircleBitmap(s, 16));
					//savetoaFileLocation(s);

				}

			}

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("bitmap", cameraBitmap);
		outState.putInt("orientation", orientation);
		outState.putString("profileimagepath", profileimagepath);
		outState.putInt("popup_value", popup_open);
		outState.putBoolean("waiting", waiting);
		outState.putString("verificationcodeshowing", verificationcodeshowing);
		outState.putString("verificationcodemessage", verificationcodemessage);
		Log.e("onsaveint-flag",verificationcodeshowing);
		outState.putString("verificationcodeindicator", verificationcodeindicator);

	if(IsCountryCodeshowing)
	{
		outState.putString("countrycodeshowing", "true");
	}
	else {
		outState.putString("countrycodeshowing", "false");
	}
		Log.e("success","success");
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if(savedInstanceState.getInt("popup_value")==1)
			openImagechooseAletDialog();
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

	private Bitmap getBitmapFromFile(String filepath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
		return bitmap;

	}

	@Override
	public void onImageDownloadFinish(Bitmap googleimagebitmap) {
		if (googleimagebitmap != null) {
			Bitmap s = getResizedBitmap(googleimagebitmap, 200, 200);
			cameraBitmap = s;
			ImageView picView = (ImageView) findViewById(R.id.img_userimage);
			// display the returned cropped image

			GraphicsUtil graphicUtil = new GraphicsUtil();
			// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
			picView.setImageBitmap(graphicUtil.getCircleBitmap(s, 16));
			savetoaFileLocation(s);
		} else {
			SYLUtil.buildAlertMessage(SYLSignupActivity.this, getResources()
					.getString(R.string.imagepath_alertmessage));
		}
	}

	private void getBiymapFromGoogleLocation(String googleimagepath) {
		SYLGoogleImageDownload downloadtask = new SYLGoogleImageDownload(
				getBaseContext());

		downloadtask.mGoogleimagedownloadlistener = SYLSignupActivity.this;

		downloadtask.execute(googleimagepath);

	}

	@Override
	public void onDidFinished(SYLSignupResponseModel msylsignupresponsemodel) {
		// TODO Auto-generated method stub
		waiting = false;
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}

		if (msylsignupresponsemodel != null) {
			if (msylsignupresponsemodel.isSuccess()) {
				signupindicator = "success";
				String useremail = msylsignupresponsemodel.getSignupResponse()
						.getEmail();
				String userid =Integer.toString(msylsignupresponsemodel.getSignupResponse()
						.getUserId());
				verificationcodeshowing="true";
				showAlertDialog(
						"You have successfully registered in SYL app. A verification code is send to "
								+ useremail, "success");
				verificationcodemessage="You have successfully registered in SYL app. A verification code is send to "+useremail;
				verificationcodeindicator=signupindicator;


accesstoken=msylsignupresponsemodel
		.getSignupResponse().getAccessToken();
				SYLSaveValues
						.setSYLEmailAddress(msylsignupresponsemodel
										.getSignupResponse().getEmail(),
								SYLSignupActivity.this);
	SYLSaveValues.setSYLUserID(userid, SYLSignupActivity.this);
	SYLSaveValues.setProfileImageurl(msylsignupresponsemodel.getSignupResponse().getProfileImage(), SYLSignupActivity.this);
				SYLSaveValues.setSYLMobileNumber(msylsignupresponsemodel.getSignupResponse().getMobile(), SYLSignupActivity.this);
	SYLSaveValues.setSYLusername(msylsignupresponsemodel.getSignupResponse().getName(), SYLSignupActivity.this);
				Log.e("success", msylsignupresponsemodel.getSignupResponse()
						.getAccessToken());

			} else {
				signupindicator = "fail";
				verificationcodeshowing="true";//this variable is using to show the alert message when screen rotates
				verificationcodemessage=msylsignupresponsemodel.getError()
						.getErrorDetail();//this variable is using to show the alert message when screen rotates
				verificationcodeindicator=signupindicator;
				showAlertDialog(msylsignupresponsemodel.getError()
						.getErrorDetail(), "fail");
			}
		} else {
			signupindicator="fail";
			verificationcodeindicator=signupindicator;

			verificationcodeshowing="true";//this variable is using to show the alert message when screen rotates
			verificationcodemessage="SYL Server Error Occured";//this variable is using to show the alert message when screen rotates
			showAlertDialog("SYL Server Error Occured", "fail");

		}
	}

	private void showAlertDialog(String message, String indicator) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface di, int arg1) {
						if (signupindicator.equals("success")) {
							verificationcodeshowing="false";
							Intent signinIntent = new Intent(
									SYLSignupActivity.this,
									SYLVerificationCodeActivity.class);
							signinIntent.putExtra("intentfrom", "signup");
							signinIntent.putExtra("accesstoken", accesstoken);
							SYLSignupActivity.this.startActivity(signinIntent);
							finish();
						}
						else  if(signupindicator.equals("fail"))
						{
							verificationcodeshowing="false";
						}
						di.cancel();

					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();



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

	private String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,null);

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}
	private void restoreProgress(Bundle savedInstanceState) {
		waiting = savedInstanceState.getBoolean("waiting");

		if (waiting) {
			if (Dialog != null) {
				ProgressDialog refresher = (ProgressDialog) Dialog.get();
				refresher.dismiss();
				mProgressDialog = new ProgressDialog(SYLSignupActivity.this);
				mProgressDialog.setMessage("Please wait...");
				mProgressDialog.show();
				Dialog = new WeakReference<ProgressDialog>(mProgressDialog);
			}
		}

	}


}


	
	
	
	

