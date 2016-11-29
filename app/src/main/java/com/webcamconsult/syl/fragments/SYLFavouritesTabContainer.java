package com.webcamconsult.syl.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webcamconsult.syl.R;

public class SYLFavouritesTabContainer extends SYLBaseContainerFragment {

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
Log.e("favourites container-intentfrom",bundlevalues.getString("intentfrom"));
initView();
}
}

private void initView() {
/*	SYLFavouritesFragment mFragment=new SYLFavouritesFragment();
	Bundle bundle_values=new Bundle();
	bundle_values.putString("intentfrom", bundlevalues.getString("intentfrom"));
	mFragment.setArguments(bundle_values);
replaceFragment(mFragment, false);
*/
SYLFavouritesFragment1 mFragment=new SYLFavouritesFragment1();
Bundle bundle_values=new Bundle();
bundle_values.putString("intentfrom", bundlevalues.getString("intentfrom"));
bundle_values.putBundle("detailsbundle", detailsBundle);
mFragment.setArguments(bundle_values);
replaceFragment(mFragment, false);


}

}
