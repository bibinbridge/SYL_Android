package com.webcamconsult.syl.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;
import com.webcamconsult.syl.fragments.SYLListFragment;
import com.webcamconsult.syl.fragments.SYLPhonecontactsFragment;
import com.webcamconsult.syl.utilities.SYLUtil;

public class SYLBaseActivity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected ListFragment mFrag;

	public SYLBaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}
	public SYLBaseActivity() {
	
	}
    
    //Activity firsh calls here
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);
		
		//setContentView(R.layout.menu_frame);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
	
	 
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new SYLListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	      //  getSupportActionBar().setCustomView(customNav);
		getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.navigationdrawer_menu));
	        getSupportActionBar().setDisplayShowCustomEnabled(true);
	        getSupportActionBar().setDisplayShowTitleEnabled(false);
	        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_frame));

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			toggle();
			return true;
		case R.id.github:
			SYLUtil.goToGitHub(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		ActionBar actionBar = getSupportActionBar();
		// 1) Inflate your menu, if you have also need actions
		getSupportMenuInflater().inflate(R.menu.main, menu);
		// 2) Set your display to custom next
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// 3) Do any other config to the action bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		// 4) Now set your custom view
//		actionBar.setCustomView(R.layout.action_custom1);
//		View v=actionBar.getCustomView();
//		TextView mybuzzoek=(TextView)v.findViewById(R.id.mytext);
//		Button mnewButton=(Button)v.findViewById(R.id.btnnew);
//		if(getIntent().getStringExtra("fragmentvalue").equals("favourites"))
//		{
//			mnewButton.setVisibility(View.VISIBLE);
//		}
//		else if(getIntent().getStringExtra("fragmentvalue").equals("favouritesimport"))
//		{
//			mnewButton.setVisibility(View.INVISIBLE);
//		}
//		mnewButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//	
//				 Intent intent=new Intent(SYLBaseActivity.this,SYLRequestnewappointmentActivity.class);
//					if(getIntent().getStringExtra("fragmentvalue").equals("favourites"))
//					{
//				 intent.putExtra("intentvalue","favourites");
//				
//					}
//					else if(getIntent().getStringExtra("fragmentvalue").equals("favouritesimport"))
//					{
//						 intent.putExtra("intentvalue","favouritesimport");
//					
//					}
//					else if(	(getIntent().getStringExtra("fragmentvalue").equals("newappointment"))){
//						 intent.putExtra("intentvalue","appointments");
//					}
//			
//startActivity(intent);
//finish();
//
//			}
//		});
//		Typeface signupfont=Typeface.createFromAsset(getAssets(),"fonts/MuseoSans-300.otf"); 
//		mybuzzoek.setTypeface(signupfont);
//		if(getIntent().getStringExtra("fragmentvalue").equals("favourites"))
//		{
//		mybuzzoek.setText("Favourites");
//		}
//		else if(getIntent().getStringExtra("fragmentvalue").equals("favouritesimport"))
//		{
//			mybuzzoek.setText("Favourites");
//		}
//		else {
//			mybuzzoek.setText("Appointments");
//		}

		return true;
	}
	public void finishActivity()
	{
		this.finish();
	}
}
