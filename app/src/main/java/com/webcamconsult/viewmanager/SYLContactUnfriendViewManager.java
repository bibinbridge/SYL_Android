package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLContactUnfriendListener;
import com.webcamconsult.syl.model.SYLContactUnfriendResponseModel;
import com.webcamconsult.syl.modelmanager.SYLContactsUnfriendModelManager;

public class SYLContactUnfriendViewManager implements SYLContactUnfriendListener{
	public SYLContactUnfriendListener msylContactUnfriendListener;
public void doUnfriendsylContact(String sender_userid,String accesstoken,String unfriend_userid,String timezone)
{
	SYLContactsUnfriendModelManager mUnfriendModelManager=new SYLContactsUnfriendModelManager();
	mUnfriendModelManager.mContactUnfriendListener=SYLContactUnfriendViewManager.this;
	mUnfriendModelManager.doUnfriendsylContact(sender_userid, accesstoken, unfriend_userid, timezone);
}
	@Override
	public void finishUnfriendRequest(
			SYLContactUnfriendResponseModel mContactUnfriendResponseModel) {
		// TODO Auto-generated method stub
		msylContactUnfriendListener.finishUnfriendRequest(mContactUnfriendResponseModel);
	}

}
