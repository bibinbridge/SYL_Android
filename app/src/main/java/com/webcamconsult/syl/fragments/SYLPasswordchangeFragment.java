package com.webcamconsult.syl.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.webcamconsult.syl.R;



public class SYLPasswordchangeFragment extends Fragment {
    CheckBox checkBox1;
    CheckBox checkBox2;
    TextView mTitletxtview;
    public SYLPasswordchangeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passwordchange, null);
    

        
        return view;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		FragmentActivity i=		getActivity();
		SlidingFragmentActivity k=(SlidingFragmentActivity)i;
		View v=k.getSupportActionBar().getCustomView();
			 //    View v=getS
			     TextView mhead=(TextView)v.findViewById(R.id.mytext);
			     mhead.setText("Change Password");
			   Button mnewButton=(Button)v.findViewById(R.id.btnnew);
			   mnewButton.setVisibility(View.INVISIBLE);
			   


	}


	
	
}
