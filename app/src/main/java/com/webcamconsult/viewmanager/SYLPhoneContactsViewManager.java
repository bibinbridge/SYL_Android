package com.webcamconsult.viewmanager;

import java.util.ArrayList;

import com.webcamconsult.syl.activities.SYLFragmentChangeActivity;
import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.interfaces.SYLPhoneContactsListener;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.model.SYLPhoneContacts;
import com.webcamconsult.syl.modelmanager.SYLContactsModelManager;
import com.webcamconsult.syl.modelmanager.SYLPhoneContactsModelManager;

public class SYLPhoneContactsViewManager implements SYLPhoneContactsListener{

public SYLFragmentChangeActivity mFragmentchangeactivity;
public SYLPhoneContactsListener mPhoneContactsListener;
	public  void getphoneContacts(SYLFragmentChangeActivity mFragmentChangeActivity)
{
		this.mFragmentchangeactivity=mFragmentChangeActivity;
	SYLPhoneContactsModelManager mContacteManager=new SYLPhoneContactsModelManager();
	mContacteManager.msylphonecontactslistener=SYLPhoneContactsViewManager.this;
	mContacteManager.getSYLPhoneContacts(mFragmentChangeActivity);
}
	@Override
	public void getPhoneContactsFinish(
			ArrayList<SYLPhoneContacts> sylphonecontacts) {
		// TODO Auto-generated method stub
		mPhoneContactsListener.getPhoneContactsFinish(sylphonecontacts);
	}



}
