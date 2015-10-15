package com.sepcialfocus.android.ui;

import java.util.HashMap;
import java.util.logging.Logger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mike.aframe.MKLog;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.configs.AppConstant;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

public class WelcomeActivity extends BaseFragmentActivity{
	
	View splash;
	RelativeLayout splashLayout;
	String platform = "";
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.splash_activity);
		
		
		mHandler.sendEmptyMessageDelayed(0x01, 500);
		
	}
	

public static String getDeviceInfo(Context context) {
    try{
      org.json.JSONObject json = new org.json.JSONObject();
      android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
          .getSystemService(Context.TELEPHONY_SERVICE);

      String device_id = tm.getDeviceId();

      android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

      String mac = wifi.getConnectionInfo().getMacAddress();
      json.put("mac", mac);

      if( !"".equals(device_id) ){
        device_id = mac;
      }

      if( !"".equals(device_id) ){
        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
      }

      json.put("device_id", device_id);

      return json.toString();
    }catch(Exception e){
      e.printStackTrace();
    }
  return null;
}
                

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Intent intent2 = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent2);
			finish();
		}
		
	};

}
