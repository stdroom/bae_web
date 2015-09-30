/**
 * 工程名: MainActivity
 * 文件名: MineActivity.java
 * 包名: com.sepcialfocus.android.ui.settting
 * 日期: 2015-9-20上午10:55:33
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.settting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;

/**
 * 类名: MineActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-20 上午10:55:33 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class MineActivity extends BaseFragmentActivity implements View.OnClickListener{

	ImageView mBackImg;
	TextView mTitleTv;
	
	RelativeLayout mHistoryRl;
	RelativeLayout mAddColumnRl;
	RelativeLayout mFavorRl;
	RelativeLayout mFeedbackRl;
	RelativeLayout mSystemMsgRl;
	RelativeLayout mShareRl;
	RelativeLayout mAboutUsRl;
	RelativeLayout mSettingRl;
	
	Intent intent ;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_mine);
		initView();
	}
	
	

	@Override
	protected void initView() {
		mBackImg = (ImageView)findViewById(R.id.img_title_back);
		mBackImg.setOnClickListener(this);
		mTitleTv = (TextView)findViewById(R.id.tv_title);
		mTitleTv.setText("我的");
		
		mHistoryRl = (RelativeLayout)findViewById(R.id.mine_history_rl);
		mAddColumnRl = (RelativeLayout)findViewById(R.id.mine_columns_rl);
		mFavorRl = (RelativeLayout)findViewById(R.id.mine_favorite_rl);
		mFeedbackRl = (RelativeLayout)findViewById(R.id.mine_feedback_rl);
//		mSystemMsgRl = (RelativeLayout)findViewById(R.id.mine_msg_rl);
//		mShareRl = (RelativeLayout)findViewById(R.id.mine_share_rl);
//		mAboutUsRl = (RelativeLayout)findViewById(R.id.mine_about_us_rl);
//		mSettingRl = (RelativeLayout)findViewById(R.id.mine_setting_rl);
		
		mHistoryRl.setOnClickListener(this);
		mAddColumnRl.setOnClickListener(this);
		mFavorRl.setOnClickListener(this);
		mFeedbackRl.setOnClickListener(this);
//		mSystemMsgRl.setOnClickListener(this);
//		mShareRl.setOnClickListener(this);
//		mAboutUsRl.setOnClickListener(this);
//		mSettingRl.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.img_title_back:
			finish();
			break;
		case R.id.mine_history_rl:
			startActivity(new Intent(MineActivity.this,HistoryActivity.class));
			break;
		case R.id.mine_columns_rl:
			intent = new Intent(MineActivity.this,DragSortMenuActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_favorite_rl:
			intent = new Intent(MineActivity.this,HistoryActivity.class);
			intent.putExtra("key", false);
			startActivity(intent);
			break;
		case R.id.mine_feedback_rl:
			intent = new Intent(MineActivity.this,FeedbackActivity.class);
			startActivity(intent);
			break;
		}
		
	}
	
	

}

