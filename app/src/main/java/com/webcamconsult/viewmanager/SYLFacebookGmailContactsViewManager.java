package com.webcamconsult.viewmanager;

import android.content.Context;
import android.os.AsyncTask;

import com.webcamconsult.syl.databaseaccess.SYLGmailFacebookContactsManager;
import com.webcamconsult.syl.interfaces.SYLContactsListener;
import com.webcamconsult.syl.interfaces.SYLGmailContactsListener;
import com.webcamconsult.syl.model.SYLContacts;
import com.webcamconsult.syl.model.SYLGmailContactResponsemodel;
import com.webcamconsult.syl.modelmanager.SYLContactsModelManager;

public class SYLFacebookGmailContactsViewManager implements SYLContactsListener,SYLGmailContactsListener{
public SYLContactsListener mContactsListener;
public SYLGmailContactsListener mGmailContactsListener;
Context mContext;
	public  void getFacebookContacts(String accesstoken,Context mContext)
{
		this.mContext=mContext;
	SYLContactsModelManager mContacteManager=new SYLContactsModelManager();
	mContacteManager.mContactsListener=SYLFacebookGmailContactsViewManager.this;
	mContacteManager.getFacebookContacts(accesstoken,"POST",mContext);
}

@Override
public void onDidFinishGetContacts(SYLContacts sylcontacts) {
	// TODO Auto-generated method stub
	mContactsListener.onDidFinishGetContacts(sylcontacts);
}
public  void getGmailContacts(String userid,String accesstoken,String gmailtoken,String refreshtoken,Context mContext,String timezone)
{
	this.mContext=mContext;
SYLContactsModelManager mContacteManager=new SYLContactsModelManager();
mContacteManager.sylgmailcontactlistener=SYLFacebookGmailContactsViewManager.this;
mContacteManager.getGmailContacts(userid, accesstoken, gmailtoken, refreshtoken, "POST", this.mContext, timezone);
}

@Override
public void finishGmailContacts(
		SYLGmailContactResponsemodel mGmailContactresponsemodel) {
	// TODO Auto-generated method stub
	new AsyncTask<SYLGmailContactResponsemodel, Void, SYLGmailContactResponsemodel>() {
		protected void onPreExecute() {
			// Pre Code
		}
		protected SYLGmailContactResponsemodel doInBackground(SYLGmailContactResponsemodel... gmailcontactresponsemodel) {
			// Background Code
			SYLGmailContactResponsemodel  mgmmailContactResponseModel=gmailcontactresponsemodel[0];
			insertSYLgmailContacts(SYLFacebookGmailContactsViewManager.this.mContext, mgmmailContactResponseModel);

			return mgmmailContactResponseModel;
		}
		protected void onPostExecute(SYLGmailContactResponsemodel mGmailContactresponsemodel) {
			// Post Code
			mGmailContactsListener.finishGmailContacts(mGmailContactresponsemodel);
		}
	}.execute(mGmailContactresponsemodel);
}

	private void insertSYLgmailContacts(Context mContext,
										SYLGmailContactResponsemodel mResponSeModel) {
		SYLGmailFacebookContactsManager mdatamangaer = new SYLGmailFacebookContactsManager(
				mContext);
		mdatamangaer.clearTable("contactpersons_detailsgmail");




		SYLGmailFacebookContactsManager datamanager = new SYLGmailFacebookContactsManager(
				mContext);
		datamanager.insertGmailContacts(mResponSeModel.getGoogleContacts());

	}










}
