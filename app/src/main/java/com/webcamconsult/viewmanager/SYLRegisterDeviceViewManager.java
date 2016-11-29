package com.webcamconsult.viewmanager;

import com.webcamconsult.syl.interfaces.SYLDeviceRegisterListener;
import com.webcamconsult.syl.model.SYLDeviceRegisterResponseModel;
import com.webcamconsult.syl.modelmanager.SYLDeviceRegisterModelManager;

import android.util.Log;

public class SYLRegisterDeviceViewManager {
	public SYLDeviceRegisterListener mDeviceRegisterListener;
public void sendregidtoServer(String appname,String appversion,String deviceUID,String regid,String devicename,String devicemodel,String deviceversion,boolean pushbadge,boolean pushalert,boolean pushsound,String devicetype,String timezone)
{
	
Log.e("send regid to server",regid);
SYLDeviceRegisterModelManager mmanager=new SYLDeviceRegisterModelManager();
mmanager.doRegisterDevice(appname, appversion, deviceUID, regid, devicename, devicemodel, deviceversion, pushbadge, pushalert, pushsound, devicetype,timezone);

}


}
