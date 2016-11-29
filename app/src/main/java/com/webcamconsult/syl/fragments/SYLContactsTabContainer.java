package com.webcamconsult.syl.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webcamconsult.syl.R;

public class SYLContactsTabContainer extends SYLBaseContainerFragment {
	private boolean IsViewInited;
	Bundle bundlevalues;
	Bundle detailsBundle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	return inflater.inflate(R.layout.container_fragment, null);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	if (!IsViewInited) {
	IsViewInited = true;
	bundlevalues=getArguments();
	detailsBundle=bundlevalues.getBundle("detailsbundle");
	initView();
	  }
	}
	private void initView() {
		/*
		Bundle bundle_values=new Bundle();
		bundle_values.putString("intentfrom", bundlevalues.getString("intentfrom"));
		SYLFragment mFragment=new SYLFragment();
		mFragment.setArguments(bundle_values);
	replaceFragment(mFragment, false);
	*/
	
	
	Bundle bundle_values=new Bundle();
	bundle_values.putString("intentfrom", bundlevalues.getString("intentfrom"));
	bundle_values.putBundle("detailsbundle", detailsBundle);
	SYLUsersFragment1 mFragment=new SYLUsersFragment1();
	mFragment.setArguments(bundle_values);
	
replaceFragment(mFragment, false);
	
	
	}

}
