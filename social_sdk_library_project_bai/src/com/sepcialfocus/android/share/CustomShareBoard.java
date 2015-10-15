/**
 * 工程名: IeltsCode
 * 文件名: CustomShareBoard.java
 * 包名: com.xiaoma.ieltstone.widgets
 * 日期: 2015-6-16上午11:55:17
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.sepcialfocus.android.configs.AppConstant;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.CircleShareContent;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.WeiXinShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMWXHandler;
import com.umeng_social_sdk_res_lib.R;

/**
 * 类名: CustomShareBoard <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-6-16 上午11:55:17 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

    private UMSocialService mController;
    private Activity mActivity;
    private LinearLayout wechat,friends,sina,qq,qzone,renren;
    
    private boolean isSetShareFlag = false;
    public CustomShareBoard(Activity activity) {
        super(activity);
        this.mActivity = activity;
        mController = UMServiceFactory.getUMSocialService(AppConstant.DESCRIPTOR,RequestType.SOCIAL);
        initView(activity);
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.share_uipopupwindow, null);
        wechat=(LinearLayout) rootView.findViewById(R.id.ll_wechat);
		friends=(LinearLayout) rootView.findViewById(R.id.ll_friends);
		sina=(LinearLayout) rootView.findViewById(R.id.ll_sina);
		qq=(LinearLayout) rootView.findViewById(R.id.ll_qq);
		qzone=(LinearLayout) rootView.findViewById(R.id.ll_qzone);
		renren=(LinearLayout) rootView.findViewById(R.id.ll_renren);
		
		wechat.setOnClickListener(this);
		friends.setOnClickListener(this);
		sina.setOnClickListener(this);
		qq.setOnClickListener(this);
		qzone.setOnClickListener(this);
		renren.setOnClickListener(this);
		
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
//        setAnimationStyle(R.style.AnimBottom);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.ll_wechat){
        // 微信分享
        	performShare(SHARE_MEDIA.WEIXIN);
        }else if(id == R.id.ll_friends){
        	// 朋友圈分享
        	performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
        }else if(id == R.id.ll_sina){
        	performShare(SHARE_MEDIA.SINA);
        }else if(id == R.id.ll_qq){
        	performShare(SHARE_MEDIA.QQ);
        }else if(id == R.id.ll_qzone){
        	performShare(SHARE_MEDIA.QZONE);
        }else if(id == R.id.ll_renren){
        	performShare(SHARE_MEDIA.RENREN);
        }
        dismiss();
    }

    private void performShare(SHARE_MEDIA platform) {
    	mController.getConfig().closeToast();
        mController.postShare(mActivity, platform, new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
            	String showText = "";
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享成功";
                    Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
                } else {
//                    showText = "分享失败";
                }
//                Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
    
    public final String getString(int resId) {
        return mActivity.getResources().getString(resId);
    }
    
    
    
    public void setShareContent(String shareContent,String shareTitle,
    		String shareTargetUrl,String shareTargetIcon){
    	
    	// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		 mController.getConfig().supportQQPlatform(mActivity, AppConstant.QQ_APP_ID,
				 shareTargetUrl);
		mController.getConfig().setSsoHandler(
				new QZoneSsoHandler(mActivity, AppConstant.QQ_APP_ID));
		UMWXHandler circleHandler = mController.getConfig()
				.supportWXCirclePlatform(mActivity, AppConstant.WEIXIN_APP_ID,shareTargetUrl);
		circleHandler.setCircleTitle(shareTitle);
		// 添加微信平台
		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(
				mActivity, AppConstant.WEIXIN_APP_ID, shareTargetUrl);
		wxHandler.setWXTitle(shareTitle);
		mController.setShareContent(shareContent);
		
		UMImage localImage = new UMImage(mActivity, R.drawable.icon);
		UMImage urlImage = new UMImage(mActivity,
				shareTargetIcon);
		
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(shareContent);
		weixinContent.setTitle(shareTitle);
		weixinContent.setTargetUrl(shareTargetUrl);
		 weixinContent.setShareImage(localImage);
		// weixinContent.setShareMusic(uMusic);
//		weixinContent.setShareVideo(video);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareContent);
		circleMedia.setTitle(shareTitle);
		 circleMedia.setShareImage(localImage);
		// circleMedia.setShareMusic(uMusic);
//		circleMedia.setShareVideo(video);
		mController.setShareMedia(circleMedia);

		// 设置新浪分享内容
		mController.setShareMedia(new SinaShareContent(new UMImage(
				mActivity,
				shareTargetIcon)));

		// 设置renren分享内容
		RenrenShareContent renrenShareContent = new RenrenShareContent();
		renrenShareContent.setShareContent(shareContent);
		UMImage image = new UMImage(mActivity,
				BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.icon));
		image.setTitle(shareTitle);
		image.setThumb(shareTargetIcon);
		renrenShareContent.setShareImage(image);
		renrenShareContent.setAppWebSite(shareTargetUrl);
		mController.setShareMedia(renrenShareContent);

		UMImage qzoneImage = new UMImage(mActivity,
				shareTargetIcon);
		qzoneImage
				.setTargetUrl(shareTargetIcon);

		UMImage mx2Image = new UMImage(
				mActivity,
				/* new File("/mnt/sdcard/bigimage.jpg") */shareTargetIcon);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(shareContent);
		qzone.setTargetUrl(shareTargetUrl);
		qzone.setTitle(shareTitle);
		qzone.setShareImage(qzoneImage);
		// qzone.setAppWebSite("http://www.umeng.com/social");
		mController.setShareMedia(qzone);


		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(shareContent);
		qqShareContent.setTitle(shareTitle);
		qqShareContent.setTargetUrl(shareTargetUrl);
		mController.setShareMedia(qqShareContent);


		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent(shareContent);
		// 设置tencent分享内容
		mController.setShareMedia(tencent);

		SinaShareContent sinaContent = new SinaShareContent(urlImage);
		sinaContent.setShareContent(shareContent);
		mController.setShareMedia(sinaContent);

		mController.setShareMedia(new UMImage(mActivity,
				 R.drawable.icon));

		mController.getConfig().closeSinaSSo();
    	
    }
}

