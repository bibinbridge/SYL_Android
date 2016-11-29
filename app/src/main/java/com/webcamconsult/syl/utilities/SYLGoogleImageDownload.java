package com.webcamconsult.syl.utilities;

import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.webcamconsult.syl.interfaces.SYLGoogleImageDownloadInterface;

public class SYLGoogleImageDownload extends AsyncTask<String, Void, Bitmap> {
public	SYLGoogleImageDownloadInterface mGoogleimagedownloadlistener;

	public SYLGoogleImageDownload(Context downloadContext)
	{

	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
		URL url = new URL(params[0].toString());
		Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		return image;
		}
		catch(Exception e)
		{
			return null;
		}
	}
@Override
protected void onPostExecute(Bitmap result) {
	// TODO Auto-generated method stub
	super.onPostExecute(result);
	mGoogleimagedownloadlistener.onImageDownloadFinish(result);
}
}
