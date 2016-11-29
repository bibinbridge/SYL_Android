package com.webcamconsult.syl.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.adapters.SYLPrioritySpinnerAdapter;
import com.webcamconsult.syl.interfaces.SYLPrioritySelectListener;

public class SYLPrioritySelectDialogFragment extends DialogFragment{
	ListView lv;
	SYLPrioritySelectListener  mSelectListener;
	String optionnumber;
		  @Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
		        View v = inflater.inflate(R.layout.syl_priorityselectlist, container, false);
		        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		        lv=(ListView)v.findViewById(R.id.list);
		        return v;
		  }
	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		
		Bundle c=getArguments();
		Log.e("Value",c.getString("test"));
		optionnumber=c.getString("optionnumber");
		ArrayList<String> medium1ArrayList=c.getStringArrayList("testarraylist");
		ArrayList<Integer> imagearraylist=c.getIntegerArrayList("imagearraylist");
		ArrayList<String> medium2ArrayList=new ArrayList<String>();
		int total_images2[] = { R.drawable.skype_largeicon,
				R.drawable.opentok_icon,R.drawable.mobile_largeicon  };
		ArrayList<Integer> k=new ArrayList<Integer>();
		k.add(R.drawable.skype_largeicon);
		k.add(R.drawable.opentok_icon);
		k.add(R.drawable.mobile_largeicon);
		String[] items = new String[] {"SKype", "opentok", "mobile"};





	new SYLPrioritySpinnerAdapter(getActivity(), R.layout.spinner_priorityrow,total_images2,medium1ArrayList,imagearraylist);

		 lv.setAdapter(new SYLPrioritySpinnerAdapter(getActivity(), R.layout.spinner_priorityrow,total_images2,medium1ArrayList,imagearraylist));
	lv.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			   String selectedoption = ((TextView) view.findViewById(R.id.txt_priorityname)).getText().toString();
			Log.e("position",""+position);
		
			mSelectListener.onPrioritySelectFinish(selectedoption,optionnumber);
			getDialog().dismiss();
		}
	});
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mSelectListener = (SYLPrioritySelectListener) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mSelectListener = null;
	}

}
