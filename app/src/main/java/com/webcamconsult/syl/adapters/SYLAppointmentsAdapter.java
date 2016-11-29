package com.webcamconsult.syl.adapters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.fragments.SYLAppointmentsiniFragment;
import com.webcamconsult.syl.imageloader.ImageLoader;
import com.webcamconsult.syl.interfaces.ProgressDialogFinishListener;

public class SYLAppointmentsAdapter extends BaseAdapter {

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
public	ProgressDialogFinishListener mProgressdialogFinishListener;
	Context mcontext;
	Bitmap roundedbitmapimage;
	String url,arrowindicator;
	String temp;
	ImageView image;

	private ArrayList<String> mData = new ArrayList<String>();

	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

	public ImageLoader imageLoader;

	private LayoutInflater mInflater;

	public SYLAppointmentsAdapter(Context context) {
if(context!=null){

	mcontext=context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}
	}

	public void addItem(final String item) {
		mData.add(item);
		// mdate.add(date);
		// mtime.add(time);
		notifyDataSetChanged();
	}

	public void addSectionHeaderItem(final String item) {
		mData.add(item);
		// mdate.add(item);
		// mtime.add(item);
		sectionHeader.add(mData.size() - 1);
		notifyDataSetChanged();

	}

	@Override
	public int getItemViewType(int position) {
		return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getCount() {
		Log.e("getcount","getcount");
		return mData.size();
	}

	@Override
	public Object getItem(int position) {

		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
Log.e("getview method","getview method");

		ViewHolder holder = null;

		int rowType = getItemViewType(position);
		String listLevel = null;
		if (convertView == null) {
			holder = new ViewHolder();
			switch (rowType) {
			case TYPE_ITEM:
				listLevel = "TYPE_ITEM";
				convertView = mInflater.inflate(
						R.layout.fragment_appointmentsrw, parent, false);
				holder.textView = (TextView) convertView
						.findViewById(R.id.name);
				holder.textViewdate = (TextView) convertView
						.findViewById(R.id.date);
				holder.textViewtime = (TextView) convertView
						.findViewById(R.id.time);
				holder.mlastname = (TextView) convertView
						.findViewById(R.id.lastname);
				holder.row_icon = (ImageView) convertView
						.findViewById(R.id.row_icon);

				holder.mtvuserid = (TextView) convertView
						.findViewById(R.id.tvuserid);
				holder.mtextviewappointmenttype = (TextView) convertView
						.findViewById(R.id.tvappointmenttype);
				holder.arrow_indicator=(ImageView)convertView.findViewById(R.id.img_arrow);

				break;
			case TYPE_SEPARATOR:

				convertView = mInflater.inflate(R.layout.header, parent, false);
				holder.textView = (TextView) convertView
						.findViewById(R.id.textSeparator);

				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String h = mData.get(position);
		if ((h.equals("Request Received") || h.equals("Confirmed Appointments")
				|| h.equals("Today's Appointments") || h.equals("Request Sent") || h
					.equals("Cancelled Request") || h.equals("Finished Appointments")       )) {
			holder.textView.setText(h);

		} else {
			imageLoader = new ImageLoader(mcontext);

			// imageLoaderone= new ImageLoaderone(mcontext);

			String j[] = h.split("_");
			holder.textView.setText(j[0]);
	//		holder.mlastname.setText(j[1]);
			holder.textViewdate.setText(j[2]);
			holder.textViewtime.setText(j[3] );

			url = (j[4]);

			holder.mtextviewappointmenttype.setText(j[5]);
			holder.mtvuserid.setText(j[6]);
			arrowindicator=j[7];
			image = holder.row_icon;
			// getScaledbitmap();
			// MyTask task = new MyTask();
			// task.execute(url);

			// int rounded_value = 120;
			//
			// DisplayImageOptions options = new
			// DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).displayer(new
			// RoundedBitmapDisplayer(rounded_value)).build();
			//
		
if(!url.equals("")){

			//imageLoader.DisplayImage(url, image);
	Picasso.with(mcontext)
			.load(url)
			.transform(new CircleTransform())
.placeholder(R.drawable.syl_nouserimage)
					// .resize(R.dimen.roundimage_width,R.dimen.roundimage_height)
			.into(image);
}
else {
image.setImageResource(R.drawable.syl_nouserimage);
}
			if(arrowindicator.equals("createdbyuser"))
			{
		holder.arrow_indicator.setImageResource(R.drawable.appointment_uparrow);
			}
			else{
				holder.arrow_indicator.setImageResource(R.drawable.appointment_downarrow);
			}
			// imageLoaderone.DisplayCircleImage(url, image);

		}

		return convertView;

	}

	public static class ViewHolder {
		public TextView textView;
		public TextView textViewdate;
		public TextView textViewtime;
		public ImageView row_icon;
		public ImageView arrow_indicator;
		public TextView mlastname;
		public TextView mtvuserid;
		public TextView mtextviewappointmenttype;

	}

	public Bitmap getScaledbitmap() {
		String localpath = "/storage/emulated/0/LazyList/826302068";
		String filepath = "";
		String profileimagepathlocal = "";
		Bitmap mIcon11 = null;
		Bitmap scaledBitmap = null;
		InputStream in = null;
		try {

			in = new FileInputStream(localpath);

			mIcon11 = BitmapFactory.decodeStream(in);
			scaledBitmap = Bitmap.createScaledBitmap(mIcon11, 150, 150, false);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		try {

			// now attach the OutputStream to the file object, instead of a
			// String representation

			filepath = Environment.getExternalStorageDirectory().toString();
			profileimagepathlocal = filepath + "/LazyImages1/flower1.png";
			File wallpaperDirectory = new File(filepath + "/LazyImages1/");
			// have the object build the directory structure, if needed.
			wallpaperDirectory.mkdirs();
			File outputFile = new File(wallpaperDirectory, "flower1.png");
			FileOutputStream out = new FileOutputStream(outputFile);
			scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Bitmap b = circle_image(profileimagepathlocal);
		image.setImageBitmap(b);

		return null;

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

}
