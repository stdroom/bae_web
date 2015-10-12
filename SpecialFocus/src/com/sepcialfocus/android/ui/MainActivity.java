/**
 * ������: SpecialFocus
 * �ļ���: MainActivity.java
 * ����: com.sepcialfocus.android.ui
 * ����: 2015-9-1����9:40:41
 * Copyright (c) 2015, ����С����ӽ����Ƽ����޹�˾ All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.spot.SpotManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.MD5Utils;
import com.mike.aframe.utils.PreferenceHelper;
import com.sepcialfocus.android.BaseApplication;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.NavBean;
import com.sepcialfocus.android.configs.AppConfig;
import com.sepcialfocus.android.configs.AppConstant;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.ui.adapter.ArticleFragmentPagerAdapter;
import com.sepcialfocus.android.ui.adapter.MainPagerAdapter;
import com.sepcialfocus.android.ui.article.ArticleFragment;
import com.sepcialfocus.android.ui.article.MainFragment;
import com.sepcialfocus.android.ui.settting.DragSortMenuActivity;
import com.sepcialfocus.android.ui.settting.MineActivity;
import com.sepcialfocus.android.utils.UpdateManager;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * ����: MainActivity <br/>
 * ����: 程序入口. <br/>
 * ����: 2015-9-1 ����9:40:41 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class MainActivity extends BaseFragmentActivity
	implements OnPageChangeListener,View.OnClickListener,View.OnTouchListener,GestureDetector.OnGestureListener{
	
	private HorizontalScrollView mHorizontalScrollView ;
	private LinearLayout mLinearLayout;
	private int mScreenWidth;
	
	private int currentFragmentIndex;
	private boolean isEnd;
	
	private ArrayList<NavBean> mUrlsList = null;
	private ViewPager mFragmentViewPager = null;
	private ArticleFragmentPagerAdapter mFragmentPagerAdapter = null;
	private ArrayList<Fragment> mFragmentList;
	
	private RelativeLayout mDragSoftImg;
	
	private KJDB mKJDb;

	private long exitTime = 0;
	private ImageView mJumpMineImg;
	
	private GestureDetector detector = null;
	
	int mDragImgWidth = 0;
	int mDragImgHeight = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		// 友盟发送策略
		MobclickAgent.updateOnlineConfig(this);
		SpotManager.getInstance(this).loadSpotAds();
		// 插屏出现动画效果，0:ANIM_NONE为无动画，1:ANIM_SIMPLE为简单动画效果，2:ANIM_ADVANCE为高级动画效果
		SpotManager.getInstance(this).setAnimationType(
				SpotManager.ANIM_ADVANCE);
		// 设置插屏动画的横竖屏展示方式，如果设置了横屏，则在有广告资源的情况下会是优先使用横屏图。
		SpotManager.getInstance(this).setSpotOrientation(
				SpotManager.ORIENTATION_PORTRAIT);
		mFragmentList = new ArrayList<Fragment>();
		mKJDb = KJDB.create(this);
		initView();
		initMenu();
		initFragment();
		UpdateManager.getUpdateManager().checkAppUpdate(this, false);
		// 获取在线参数
		OnlineConfigAgent.getInstance().updateOnlineConfig(this);
		detector = new GestureDetector(this, this);
		setDector();
	}
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		AppConfig.imgFlag = PreferenceHelper.readBoolean(this, AppConstant.FLAG_IMG, AppConstant.FLAG_IMG, true);
		AppConfig.windowFlag = PreferenceHelper.readBoolean(this, AppConstant.FLAG_WINDOW, AppConstant.FLAG_WINDOW, true);
		if(AppConfig.windowFlag){
			mDragSoftImg.setVisibility(View.VISIBLE);
		}else{
			mDragSoftImg.setVisibility(View.GONE);
		}
	}




	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initMenu();
		initFragment();
	}



	protected void initView(){
		mJumpMineImg = (ImageView)findViewById(R.id.jump_mine_img);
		mJumpMineImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,MineActivity.class);
				startActivity(intent);
			}
		});
		mDragSoftImg = (RelativeLayout)findViewById(R.id.drag_soft_img);
		mDragSoftImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,DragSortMenuActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("key", mUrlsList);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mDragSoftImg.setOnTouchListener(this);
		mFragmentViewPager = (ViewPager)findViewById(R.id.fragment_viewpager);
		mFragmentPagerAdapter = new ArticleFragmentPagerAdapter(
				getSupportFragmentManager());
		mFragmentViewPager.setOffscreenPageLimit(1);
		mFragmentViewPager.setAdapter(mFragmentPagerAdapter);
		mFragmentViewPager.setOnPageChangeListener(this);
		
	}
	
	private void initFragment(){
		mFragmentList.clear();
		int length = mUrlsList.size();
		for(int i = 1 ; i <= length ; i++){
			Bundle bundle = new Bundle();
			bundle.putString("key", URLs.HOST+mUrlsList.get(i-1).getMenuUrl());
			if(i == 1){
				Fragment fragment = new MainFragment();
				fragment.setArguments(bundle);
				mFragmentList.add(fragment);
			}else{
				Fragment fragment = new ArticleFragment();
				fragment.setArguments(bundle);
				mFragmentList.add(fragment);
			}
		}
		mFragmentPagerAdapter.setFragments(mFragmentList);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_DRAGGING) {
			isEnd = false;
		} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
			isEnd = true;
			if (mFragmentViewPager.getCurrentItem() == currentFragmentIndex) {
				// 未跳入下一个页面
//				mImageView.clearAnimation();
//				Animation animation = null;
//				// 恢复位置
//				animation = new TranslateAnimation(endPosition, currentFragmentIndex * item_width, 0, 0);
//				animation.setFillAfter(true);
//				animation.setDuration(1);
//				mImageView.startAnimation(animation);
				mHorizontalScrollView.invalidate();
//				endPosition = currentFragmentIndex * item_width;
			}
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int arg2) {
		if(!isEnd){
//			if(currentFragmentIndex == position){
//				endPosition = item_width * currentFragmentIndex + 
//						(int)(item_width * positionOffset);
//			}
//			if(currentFragmentIndex == position+1){
//				endPosition = item_width * currentFragmentIndex - 
//						(int)(item_width * (1-positionOffset));
//			}
			
//			Animation mAnimation = new TranslateAnimation(beginPosition, endPosition, 0, 0);
//			mAnimation.setFillAfter(true);
//			mAnimation.setDuration(0);
//			mImageView.startAnimation(mAnimation);
			mHorizontalScrollView.invalidate();
//			beginPosition = endPosition;
		}
	}

	@Override
	public void onPageSelected(int position) {
//		Animation animation = new TranslateAnimation(endPosition, position* item_width, 0, 0);
//		
//		beginPosition = position * item_width;
		RelativeLayout rel = (RelativeLayout)mLinearLayout.getChildAt(currentFragmentIndex);
		rel.getChildAt(1).setVisibility(View.INVISIBLE);
		((TextView)rel.getChildAt(0)).setTextColor(getResources().getColor(R.color.title_color));
		currentFragmentIndex = position;
		RelativeLayout rel2 = (RelativeLayout)mLinearLayout.getChildAt(currentFragmentIndex);
		rel2.getChildAt(1).setVisibility(View.VISIBLE);
		((TextView)rel2.getChildAt(0)).setTextColor(getResources().getColor(R.color.text_color_green));
//		if (animation != null) {
//			animation.setFillAfter(true);
//			animation.setDuration(0);
//			mImageView.startAnimation(animation);
		int scrollX = 0;
		for(int i = 0 ;i < currentFragmentIndex;i++){
			scrollX += mLinearLayout.getChildAt(i).getWidth();
		}
			mHorizontalScrollView.smoothScrollTo(scrollX, 0);
//		}
	}
	

	@SuppressWarnings("unchecked")
	private ArrayList<NavBean> getMenuList(){
		List<NavBean> list  = mKJDb.findAllByWhere(NavBean.class, "show = \'1\'");
		if(list!=null && list.size()>0){
			return (ArrayList<NavBean>) list;
		}else{
			list = new ArrayList<NavBean>();
		}
		String[] menuName = getResources().getStringArray(R.array.menu_str);
		String[] menuUrl = getResources().getStringArray(R.array.menu_url);
		for(int i = 0 ; i<menuName.length ; i++){
			NavBean bean = new NavBean();
			bean.setMd5(MD5Utils.md5(menuName[i]+menuUrl[i]));
			bean.setMenu(menuName[i]);
			bean.setMenuUrl(menuUrl[i]);
			if(i<6){
				bean.setShow("1");
				list.add(bean);
			}else{
				bean.setShow("0");
			}
			bean.setCategory(1);
			mKJDb.save(bean);
		}
		return (ArrayList<NavBean>)list;
	}
	
	private void initMenu(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_view);
		mLinearLayout = (LinearLayout) findViewById(R.id.hsv_content);
		mLinearLayout.removeAllViews();
		mUrlsList = getMenuList();
		int length = mUrlsList.size();
		for (int i = 0 ; i < length ; i++) {
			RelativeLayout layout = new RelativeLayout(this);
			ImageView mImageView = new ImageView(this);
			TextView view = new TextView(this);
			view.setText(mUrlsList.get(i).getMenu());
			view.setTextSize(getResources().getDimension(R.dimen.title_size));
			view.setGravity(Gravity.CENTER);
			if(i==0){
				view.setTextColor(getResources().getColor(R.color.text_color_green));
			}else{
				view.setTextColor(getResources().getColor(R.color.title_color));
			}
			int itemWidth = (int) (view.getPaint().measureText(mUrlsList.get(i).getMenu())+getResources().getDimension(R.dimen.title_add_width));
			RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			layout.addView(view,0, params);
			mImageView.setBackgroundColor(getResources().getColor(R.color.text_color_green));
			RelativeLayout.LayoutParams params2 =  new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.title_img_height));
			params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layout.addView(mImageView,1,params2);
			if(i==0){
				mImageView.setVisibility(View.VISIBLE);
			}else{
				mImageView.setVisibility(View.INVISIBLE);
			}
			mLinearLayout.addView(layout, itemWidth, (int)getResources().getDimension(R.dimen.title_height));
			layout.setOnClickListener(this);
			layout.setTag(i);
		}
	}
	
	

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onClick(View v) {
		mFragmentViewPager.setCurrentItem((Integer)v.getTag());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/** 一个鄙人感觉不错的退出体验*/
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(this, 
	            		getResources().getString(R.string.common_back_str),
	            		Toast.LENGTH_SHORT)
	            		.show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
		return super.onKeyDown(keyCode, event);
	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId() == R.id.drag_soft_img){
			detector.onTouchEvent(event);
		}
		return false;
	}


	@Override
	public boolean onDown(MotionEvent e) {
		mDragImgHeight = mDragSoftImg.getHeight();
		mDragImgWidth = mDragSoftImg.getWidth();
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		
	}



	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
//		RelativeLayout.LayoutParams params = (LayoutParams) mDragSoftImg.getLayoutParams();
//		params.setMargins((int)(mDragSoftImg.getLeft()+distanceX), (int)mDragSoftImg.getTop(), 
//				(int)(mDragSoftImg.getLeft()+distanceX+mDragImgWidth), (int)(mDragSoftImg.getTop()+mDragImgHeight));
//		mDragSoftImg.setLayoutParams(params);
//		mDragSoftImg.requestLayout();
		return false;
	}



	@Override
	public void onLongPress(MotionEvent e) {
		
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
			float velocityY) {
//		if (event1.getRawX() > event2.getRawX()) {
//		    Toast.makeText(this, "swipe left",Toast.LENGTH_SHORT).show();
//		   } else {
//		   Toast.makeText(this, "swipe right",Toast.LENGTH_SHORT).show();
//		   }
		return true;
	}
	
	private void setDector(){
		detector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() { 
           //短快的点击算一次单击 
            @Override 
            public boolean onSingleTapConfirmed(MotionEvent e) { 
//            	Toast.makeText(MainActivity.this, "单击",Toast.LENGTH_SHORT).show();
                return false; 
            } 
            //双击时产生一次 
            @Override 
            public boolean onDoubleTap(MotionEvent e) { 
//            	Toast.makeText(MainActivity.this, "双击",Toast.LENGTH_SHORT).show();
                return false; 
            } 
          //双击时产生两次 
            @Override 
            public boolean onDoubleTapEvent(MotionEvent e) { 
                return false; 
            } 
        }); 
	}
	
	
}

