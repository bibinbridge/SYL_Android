package com.webcamconsult.viewmanager;

import android.content.Context;

import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.interfaces.SYLGmailContactsListener;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.modelmanager.SYLContactsModelManager;

public class SYLContactsViewManager implements SYLContactsListener{
public SYLContactsListener mContactsListener;
public SYLGmailContactsListener mGmailContactListener;
public Context mContext;
	public  void getContacts(String method, Context mContext,String userid,String timezone,String accesstoken)
{
		this.mContext=mContext;
	SYLContactsModelManager mContacteManager=new SYLContactsModelManager();
	mContacteManager.mContactsListener=SYLContactsViewManager.this;
	mContacteManager.getFavouriteContactpersons("post",this.mContext,userid,timezone,accesstoken);
}

@Override
public void onDidFinishGetContacts(SYLContacts sylcontacts) {
	// TODO Auto-generated method stub
	if(sylcontacts!=null) {
	mContactsListener.onDidFinishGetContacts(sylcontacts);
	}
}
public  void getSYLContacts(String method, Context mContext,String userid,String timezone,String accesstoken)
{
	this.mContext=mContext;
SYLContactsModelManager mContacteManager=new SYLContactsModelManager();
mContacteManager.mContactsListener=SYLContactsViewManager.this;
mContacteManager.getSYLContactpersons("POST",mContext,userid,timezone,accesstoken);
}
}
