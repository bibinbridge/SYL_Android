package com.webcamconsult.syl.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;

public class SYLContactsFragment extends Fragment {
	CheckBox checkBox1;
	CheckBox checkBox2;
	TextView mTitletxtview;
	private FragmentTabHost mTabHost;
	private static final String TAB1_FAVOURITES = "tab_favourites";
	private static final String TAB2_SYL = "tab_syl";
	private static final String TAB3_FACEBOOK = "tab_facebook";
	private static final String TAB4_GOOGLE = "tab_google";
	private static final String TAB5_PHONE = "tab_phone";
	View view;
	Bundle c;

	public SYLContactsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.contacts_main, null);
		mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		// mTabHost = new FragmentTabHost(getActivity());
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);
		Bundle b=getArguments();
		String intentfrom=b.getString("intentfrom");
		Bundle detailsbundle=b.getBundle("detailsbundle");

		
		Log.e("intentfrom-sylcontactsfragment",intentfrom);
		c=new Bundle();
c.putString("intentfrom", intentfrom);
c.putBundle("detailsbundle", detailsbundle);
		//
		//
		// mTabHost.addTab(setIndicator(getActivity(),mTabHost.newTabSpec(TAB_2_TAG),
		// R.drawable.tab_sylselector,"SYL",R.drawable.syl_normal),Tab2Container.class,null);
		// //
		// mTabHost.addTab(setIndicator(getActivity(),mTabHost.newTabSpec(TAB_2_TAG),
		// R.drawable.tab_sylselector,"SYL",R.drawable.syl_normal),Tab2Container.class,null);
		// mTabHost.addTab(setIndicator(getActivity(),mTabHost.newTabSpec(TAB_3_TAG),
		// R.drawable.tab_selector,"SYL",R.drawable.google_normal),Tab2Container.class,null);
		// mTabHost.addTab(setIndicator(getActivity(),mTabHost.newTabSpec(TAB_2_TAG),
		// R.drawable.tab_selector,"SYL",R.drawable.google_normal),Tab2Container.class,null);
		// mTabHost.addTab(setIndicator(getActivity(),mTabHost.newTabSpec(TAB_2_TAG),
		// R.drawable.tab_selector,"SYL",R.drawable.google_normal),Tab2Container.class,null);

		mTabHost.addTab(
				mTabHost.newTabSpec(TAB1_FAVOURITES).setIndicator(
						"Recent",
						getResources().getDrawable(
								R.drawable.tab_favouriteselector)),
				SYLFavouritesTabContainer.class,c);

		mTabHost.addTab(
				mTabHost.newTabSpec(TAB2_SYL).setIndicator("SYL",
						getResources().getDrawable(R.drawable.tab_sylselector)),
				SYLContactsTabContainer.class, c);
/*
		mTabHost.addTab(
				mTabHost.newTabSpec(TAB3_FACEBOOK).setIndicator(
						"Facebook",
						getActivity().getResources().getDrawable(
								R.drawable.tab_facebookselector)),
				SYLFacebookTabContainer.class, c);
*/
		mTabHost.addTab(
				mTabHost.newTabSpec(TAB4_GOOGLE).setIndicator("Google",
						getResources().getDrawable(R.drawable.tab_selector)),
				SYLGoogleContactsContainer.class, c);
		mTabHost.addTab(
				mTabHost.newTabSpec(TAB5_PHONE).setIndicator(
						"Phone",
						getResources()
								.getDrawable(R.drawable.tab_phoneselector)),
				SYLPhoneContactsContainer.class,c);
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(getResources().getColor(R.color.white));
			mTabHost.getTabWidget().setStripEnabled(false);
			TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title); // Unselected Tabs
			tv.setTextColor(Color.parseColor("#acabab"));
		}
		mTabHost.getTabWidget().setCurrentTab(0);
		TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0)
				.findViewById(android.R.id.title); // Unselected Tabs
		tv.setTextColor(Color.parseColor("#683d9e"));
		mTabHost.getTabWidget().getChildAt(0).getLayoutParams().width = 40;

		mTabHost.getTabWidget().setDividerDrawable(
				getResources().getDrawable(R.drawable.line));
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
					// unselected
					TextView tv = (TextView) mTabHost.getTabWidget()
							.getChildAt(i).findViewById(android.R.id.title); // Unselected
																				// Tabs
					tv.setTextColor(Color.parseColor("#acabab"));
					
				}

				// selected
				TextView tv = (TextView) mTabHost.getCurrentTabView()
						.findViewById(android.R.id.title); // for Selected Tab
				tv.setTextColor(Color.parseColor("#683d9e"));
			}
		});
		//
		// mTabHost.getTabWidget().setDividerDrawable(
		// getResources().getDrawable(R.drawable.line));
		//
		// for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
		// mTabHost.getTabWidget().getChildAt(i)
		// .setBackgroundColor(getResources().getColor(R.color.white));
		// mTabHost.getTabWidget().setStripEnabled(false);
		// TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
		// .findViewById(android.R.id.title); // Unselected Tabs
		// tv.setTextColor(Color.parseColor("#eb8064"));
		// }

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Bundle b=getArguments();
		String intentfrom=b.getString("intentfrom");
		FragmentActivity i = getActivity();
		SlidingFragmentActivity k = (SlidingFragmentActivity) i;
		k.getSupportActionBar().setCustomView(R.layout.actionbardetails_ini);
		View v = k.getSupportActionBar().getCustomView();
		// View v=getS
		TextView mhead = (TextView) v.findViewById(R.id.mytext);
		ImageView btnNewButton = (ImageView) v.findViewById(R.id.btnnew);
	//	btnNewButton.setVisibility(View.VISIBLE);
		mhead.setText("Contacts");


	}


}
