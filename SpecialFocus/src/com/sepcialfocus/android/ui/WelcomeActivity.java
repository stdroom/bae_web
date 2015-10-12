package com.sepcialfocus.android.ui;

import java.util.HashMap;
import java.util.logging.Logger;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SplashView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class WelcomeActivity extends BaseFragmentActivity{
	
	SplashView splashView;
	View splash;
	RelativeLayout splashLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		AdManager.getInstance(this).init(AppConstant.YOUMI_APPID, AppConstant.YOUMI_APPSECRET, false);
		// 第二个参数传入目标activity，或者传入null，改为setIntent传入跳转的intent
		splashView = new SplashView(this, null);
		// 设置是否显示倒数
		splashView.setShowReciprocal(true);
		// 隐藏关闭按钮
		splashView.hideCloseBtn(true);

		Intent intent = new Intent(this, MainActivity.class);
		splashView.setIntent(intent);
		splashView.setIsJumpTargetWhenFail(true);

		splash = splashView.getSplashView();
		setContentView(R.layout.splash_activity);
		splashLayout = ((RelativeLayout) findViewById(R.id.splashview));
		splashLayout.setVisibility(View.GONE);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
		params.addRule(RelativeLayout.ABOVE, R.id.cutline);
		splashLayout.addView(splash, params);
//		MKLog.d("shit",getDeviceInfo(this));
		findViewById(R.id.myRelativeLayout).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		SpotManager.getInstance(this).showSplashSpotAds(this, splashView,
				new SpotDialogListener() {

					@Override
					public void onShowSuccess() {
						splashLayout.setVisibility(View.VISIBLE);
						splashLayout.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.pic_enter_anim_alpha));
						MKLog.d("youmisdk", "展示成功");
						MobclickAgent.onEvent(WelcomeActivity.this,"welcome_ad", 
								new HashMap<String,String>().put("首页广告","展示成功"));
					}

					@Override
					public void onShowFailed() {
						MKLog.d("youmisdk", "展示失败");
						MobclickAgent.onEvent(WelcomeActivity.this,"welcome_ad", 
								new HashMap<String,String>().put("首页广告","展示失败"));
					}

					@Override
					public void onSpotClosed() {
						MKLog.d("youmisdk", "展示关闭");
					}

					@Override
					public void onSpotClick() {
						MobclickAgent.onEvent(WelcomeActivity.this,"welcome_ad", 
								new HashMap<String,String>().put("首页广告","点击"));
						MKLog.i("YoumiAdDemo", "插屏点击");
					}
				});
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
                  

}
