package com.umeng.soexample.activity;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;

import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.soexample.socialize.fragments.CustomPlatformFragment;
import com.umeng.ui.BaseSinglePaneActivity;

public class CustomPlatformActivity extends BaseSinglePaneActivity{
	
	/**
	 * just for weixin callback.
	 */
	private IWXAPI api;
	
	@Override
	protected Fragment onCreatePane() {
		CustomPlatformFragment cp  = new CustomPlatformFragment();
		api = WXAPIFactory.createWXAPI(this, "wx92e468cd3f047396");
		return cp;
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (api != null) {// for weixin call back
			setIntent(intent);
			api.handleIntent(intent, new IWXAPIEventHandler() {
				@Override
				public void onResp(BaseResp arg0) {
					SendAuth.Resp resp = (SendAuth.Resp) arg0;
				}

				@Override
				public void onReq(BaseReq arg0) {}
			});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		String result = "null";
		try {
			Bundle b = data.getExtras();
			Set<String> keySet = b.keySet();
			if(keySet.size() > 0)
				result = "result size:"+keySet.size();
			for(String key : keySet){
				Object object = b.get(key);
				Log.d("TestData", "Result:"+key+"   "+object.toString());
			}
		}
		catch (Exception e) {

		}
		Log.d("TestData", "onActivityResult   " + requestCode + "   " + resultCode + "   " + result);
		
	    UMSocialService controller = UMServiceFactory.getUMSocialService(DESCRIPTOR,
							RequestType.SOCIAL);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode) ;
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}

}
