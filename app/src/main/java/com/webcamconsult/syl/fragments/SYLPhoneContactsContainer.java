package com.webcamconsult.syl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.sylphonecontacts.SYLPhoneContactsFragment2;

public class SYLPhoneContactsContainer extends SYLBaseContainerFragment {
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
/*		SYLPhonecontactsFragment mFragment=new SYLPhonecontactsFragment();
		Bundle bundle_values=new Bundle();
		bundle_values.putString("intentfrom", bundlevalues.getString("intentfrom"));
		mFragment.setArguments(bundle_values);
	replaceFragment(mFragment, false);
	*/
/*
	SYLPhonecontactsFragment1 mFragment=new SYLPhonecontactsFragment1();
	Bundle bundle_values=new Bundle();
	bundle_values.putString("intentfrom", bundlevalues.getString("intentfrom"));
	bundle_values.putBundle("detailsbundle", detailsBundle);
	mFragment.setArguments(bundle_values);
replaceFragment(mFragment, false);
*/
		SYLPhoneContactsFragment2 mFragment=new SYLPhoneContactsFragment2();
		Bundle bundle_values=new Bundle();
		bundle_values.putString("intentfrom", bundlevalues.getString("intentfrom"));
		bundle_values.putBundle("detailsbundle", detailsBundle);
		mFragment.setArguments(bundle_values);
		replaceFragment(mFragment, false);

	
	}

}
