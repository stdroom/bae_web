
package com.umeng.soexample.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.scrshot.adapter.UMVideoAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sensor.UMSensor.OnSensorListener;
import com.umeng.socialize.sensor.UMSensor.WhitchButton;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.soexample.R;
import com.umeng.soexample.activity.beans.VideoState;
import com.umeng.soexample.activity.utils.UMConstans;
import com.umeng.soexample.activity.utils.UMTimeUtil;
import com.umeng.soexample.activity.utils.UMToastUtil;
import com.umeng.soexample.activity.utils.UMVideoStateUtil;
import com.umeng.soexample.socialize.SocializeConfigDemo;

/**   
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved 
 *
 * @Title: VideoActivity.java
 * @Package com.example.umscrshotdemo
 * @Description: 
 *
 * @author Honghui He  
 * @version V1.0   
 */

/**
 * @ClassName: MainActivity
 * @Description: UMPlayer视频播放器，集成友盟social sdk
 * @author Honghui He
 */
public class VideoActivity extends Activity implements SurfaceHolder.Callback,
        OnClickListener {
    /**
     * 
     */
    private MediaPlayer mMediaPlayer = null;
    /**
     * 
     */
    private SurfaceView mSurfaceView = null;
    /**
     * 
     */
    private SurfaceHolder mSurfaceHolder = null;
    /**
     * 
     */
    private ImageView mPlayPauseImgView = null;
    /**
     * 
     */
    private View mQuitBtn = null;
    /**
     * 视频进度条
     */
    private SeekBar mVideoPrgBar = null;
    /**
     * 当前时间
     */
    private TextView mCurrentTextView = null;
    /**
     * 视频总时间
     */
    private TextView mTotalTimeTextView = null;
    /**
     * 
     */
    private View mProgBarView = null;
    /**
     * 视频路径, CD卡根目录添加一个video.mp4文件,即可播放视频
     */
    private final String mVideoPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "video.mp4";
    /**
     * 视频的总时长
     */
    private int mVideoDuration = 0;
    /**
     * 视频播放的位置
     */
    private int mMediaPosition = -1;
    /**
     * 视频状态是否为正在播放
     */
    private boolean isPlaying = false;
    /**
     * 
     */
    private String mPosKeyString = "media_position";
    /**
     * 
     */
    private String mMediaStatekey = "media_state";
    /**
     * umeng social 控制器
     */
    private UMSocialService mController = UMServiceFactory.getUMSocialService(
            UMConstans.DESCRIPTION, RequestType.SOCIAL);

    /**
     * 摇一摇控制器
     */
    private UMShakeService mShakeController = UMShakeServiceFactory
            .getShakeService(SocializeConfigDemo.DESCRIPTOR);

    /**
     * 隐藏按钮
     */
    private final int HIDE_BTN_MSG = 1;
    /**
     * 更新进度条
     */
    private final int UPDATE_PRGBAR_MSG = 2;
    /**
     * 定时器， 用于更新播放时间
     */
    private Timer mTimer = new Timer();
    /**
     * 更新播放时间的间隔为1秒
     */
    private final int UPDATE_INTERVAL = 1000;

    /**
     * 按钮、进度条等是否为可见状态
     */
    private boolean isViewsVisibility = true;
    /**
     * 
     */
    private boolean isNeedReplay = false;

    /**
     * (非 Javadoc)
     * 
     * @Title: onCreate
     * @Description:
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_activity);
        // 初始化UI控件
        initViews();
        // 配置sso
        configSocialSdk();
    }

    /**
     * @Title: initViews
     * @Description: 初始化视图
     * @throws
     */
    private void initViews() {
        mPlayPauseImgView = (ImageView) findViewById(R.id.playBtn);
        mQuitBtn = findViewById(R.id.quitBtn);
        mSurfaceView = (SurfaceView) findViewById(R.id.video_surfaceview);

        // SurfaceHolder是SurfaceView的控制接口
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        // SurfaceView点击事件
        mSurfaceView.setOnClickListener(this);
        mPlayPauseImgView.setOnClickListener(this);
        mQuitBtn.setOnClickListener(this);

        mVideoPrgBar = (SeekBar) findViewById(R.id.progBar);
        mCurrentTextView = (TextView) findViewById(R.id.currentTime);
        mTotalTimeTextView = (TextView) findViewById(R.id.totalTime);
        mProgBarView = findViewById(R.id.progbar_layout);

        // 必须在surface创建后才能设置MediaPlayer的setDisplay为SurfaceView的holder,否则不会显示图像
        mMediaPlayer = new MediaPlayer();

        mVideoPrgBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mMediaPosition = progress * mVideoDuration / 100;
                mMediaPlayer.seekTo(mMediaPosition);
                updateUI(mMediaPosition);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {

            }
        });
        // 设置定时器
        mTimer.schedule(mTimerTask, 0, UPDATE_INTERVAL);

    }

    /**
     * @Title: configUmengSSO
     * @Description: 配置友盟分享功能的sso授权
     * @throws
     */
    private void configSocialSdk() {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(
                new QZoneSsoHandler(VideoActivity.this));
        mController.getConfig().supportQQPlatform(VideoActivity.this,
                "http://www.umeng.com");
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx967daebe835fbeac";
        // 微信图文分享必须设置一个url
        String contentUrl = "http://www.umeng.com/social";
        // 添加微信平台
        mController.getConfig().supportWXPlatform(VideoActivity.this, appId,
                contentUrl);
        // 朋友圈
        mController.getConfig().supportWXCirclePlatform(VideoActivity.this,
                appId, contentUrl);
    }

    /**
     * @Title: resigterShakeSensor
     * @Description: 注册摇一摇监听器
     * @throws
     */
    private void registerShakeSensor() {
        if (mMediaPlayer == null) {
            return;
        }
        UMVideoAdapter videoAdapter = new UMVideoAdapter(mMediaPlayer,
                mVideoPath);
        // 配置平台
        List<SHARE_MEDIA> platforms = new ArrayList<SHARE_MEDIA>();
        platforms.add(SHARE_MEDIA.QZONE);
        platforms.add(SHARE_MEDIA.SINA);
        platforms.add(SHARE_MEDIA.WEIXIN);
        platforms.add(SHARE_MEDIA.WEIXIN_CIRCLE);
        platforms.add(SHARE_MEDIA.QQ);
        /*
         * mController.registerShakeListender(VideoActivity.this, videoAdapter,
         * platforms, new VideoSensorListener());
         */
        mShakeController.setShareContent("美好瞬间，摇摇分享——来自友盟社会化组件摇一摇分享");
        mShakeController.registerShakeListender(VideoActivity.this,
                videoAdapter, platforms, new VideoSensorListener());
    }

    /**
     * @ClassName: SensorListener
     * @Description: 监听器
     * @author Honghui He
     */
    private class VideoSensorListener implements OnSensorListener {

        @Override
        public void onActionComplete(SensorEvent event) {
            saveMediaState();
            if (mMediaPlayer != null && isPlaying) {
                mMediaPlayer.pause();
                isNeedReplay = true;
            }
            UMToastUtil.showToast(VideoActivity.this, "摇一摇");

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int eCode,
                SocializeEntity entity) {
            UMToastUtil.showToast(VideoActivity.this, "分享成功");
            isNeedReplay = false;
        }

        @Override
        public void onButtonClick(WhitchButton button) {
            if (button == WhitchButton.BUTTON_CANCEL) {
                UMToastUtil.showToast(VideoActivity.this, "取消分享");
            } else {
                isNeedReplay = true;
            }
            resumeMediaplayer();
        }
    }

    /**
     * 通过定时器和Handler来更新进度条
     */
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mMediaPlayer == null) {
                return;
            }
            if (mMediaPlayer.isPlaying() && mVideoPrgBar != null) {
                Message msg = Message.obtain(mHandler);
                msg.what = UPDATE_PRGBAR_MSG;
                msg.obj = mMediaPlayer.getCurrentPosition();
                mHandler.sendMessage(msg);
            }
        }
    };

    /**
     * 处理各种消息
     */
    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == HIDE_BTN_MSG && isViewsVisibility) {
                hideViews();
            } else if (msg.what == UPDATE_PRGBAR_MSG) {
                int millseconds = (Integer) msg.obj;
                // 更新UI
                updateUI(millseconds);
            }
        };
    };

    /**
     * @Title: updateUI
     * @Description: 更新播放进度条和时间
     * @param millseconds
     * @throws
     */
    private void updateUI(int millseconds) {
        float total = (float) mVideoDuration;
        int progress = (int) ((millseconds / total) * 100);
        mVideoPrgBar.setProgress(progress);
        String timeStr = UMTimeUtil.calculateTime(millseconds);
        mCurrentTextView.setText(timeStr);
    }

    /**
     * @Title: postHideButtonMsg
     * @Description: post隐藏播放、暂停等按钮的消息
     * @throws
     */
    private void postHideButtonMsg() {
        mHandler.removeMessages(HIDE_BTN_MSG);
        Message msg = Message.obtain(mHandler);
        msg.what = HIDE_BTN_MSG;
        mHandler.sendMessageDelayed(msg, 2000);
    }

    /**
     * @Title: hideButtons
     * @Description: 隐藏播放、暂停等按钮
     * @throws
     */
    private void hideViews() {
        Animation animation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.btn_click_alpha_anim);

        mProgBarView.startAnimation(animation);
        mProgBarView.setVisibility(View.INVISIBLE);

        mPlayPauseImgView.startAnimation(animation);
        mPlayPauseImgView.setVisibility(View.INVISIBLE);

        mQuitBtn.startAnimation(animation);
        mQuitBtn.setVisibility(View.INVISIBLE);

        isViewsVisibility = false;
    }

    /**
     * @Title: showButtons
     * @Description: 显示播放、暂停等按钮
     * @throws
     */
    private void showViews() {
        mProgBarView.setVisibility(View.VISIBLE);
        mPlayPauseImgView.setVisibility(View.VISIBLE);
        mQuitBtn.setVisibility(View.VISIBLE);
        isViewsVisibility = true;
        postHideButtonMsg();
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onClick
     * @Description: view点击事件
     * @param v
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v == mPlayPauseImgView) {
            mPlayPauseImgView.startAnimation(AnimationUtils.loadAnimation(
                    VideoActivity.this, R.anim.btn_click_scale_anim));
            if (isPlaying) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
            changePlayBtnBg();
            isPlaying = mMediaPlayer.isPlaying();
        } else if (v == mQuitBtn) {
            mQuitBtn.startAnimation(AnimationUtils.loadAnimation(
                    VideoActivity.this, R.anim.btn_click_scale_anim));
            saveMediaState();
            mMediaPlayer.pause();
            // 弹出确认框
            configQuit();
        } else if (v == mSurfaceView) {
            if (isViewsVisibility) {
                hideViews();
            } else {
                showViews();
            }
        }
        // 向mHandler post隐藏按钮的消息
        postHideButtonMsg();
    }

    /**
     * @Title: changePlayBtnBg
     * @Description:
     * @throws
     */
    private void changePlayBtnBg() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mPlayPauseImgView.setImageResource(R.drawable.gtk_media_pause);
            } else {
                mPlayPauseImgView.setImageResource(R.drawable.gtk_media_play);
            }
        }
    }

    /**
     * @Title: configQuit
     * @Description:
     * @throws
     */
    private void configQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                VideoActivity.this);
        builder.setTitle("退出?");
        AlertDialog dialog = builder.create();
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        resumeMediaplayer();
                    }
                });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog.show();
    }

    /**
     * @Title: initMediaplayer
     * @Description: 初始化MediaPlayer，在SurfaceView创建后调用
     * @throws
     */
    private void initMediaplayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(mSurfaceHolder);
        mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoDuration = mMediaPlayer.getDuration();
                String timeStr = UMTimeUtil.calculateTime(mVideoDuration);
                mTotalTimeTextView.setText(timeStr);
                mMediaPosition = mVideoState.getPosition();
                if (mMediaPosition > 0) {
                    mMediaPlayer.seekTo(mMediaPosition);
                }
                mMediaPlayer.start();
                mPlayPauseImgView.setImageResource(R.drawable.gtk_media_pause);
                isPlaying = true;
                postHideButtonMsg();
                isNeedReplay = false;
            }
        });
        // 设置显示视频显示在SurfaceView上
        try {
            mMediaPlayer.setDataSource(mVideoPath);
            if (mVideoPath.startsWith("http")) {
                mMediaPlayer.prepareAsync();
                Log.d("", "流媒体prepare");
            } else {
                mMediaPlayer.prepare();
            }
        } catch (Exception e) {
            Toast.makeText(VideoActivity.this,
                    "请确定您的sdcard包含video.mp4文件，或者直接修改mVideoPath來修改视频路径.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onCreateOptionsMenu
     * @Description:
     * @param menu
     * @return
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: surfaceChanged
     * @Description:
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder,
     *      int, int, int)
     */
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: surfaceCreated
     * @Description: SurfaceView创建
     * @param arg0
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.d("", "#### surfaceCreated");
        initMediaplayer();
        registerShakeSensor();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }

    /**
     * @Title: resumeMediaplayer
     * @Description: 重新播放
     * @throws
     */
    private void resumeMediaplayer() {
        if (mMediaPlayer != null && isPlaying) {
            mMediaPlayer.start();
        }
        changePlayBtnBg();
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onRestoreInstanceState
     * @Description: 回复当前状态
     * @param savedInstanceState
     * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mMediaPosition = savedInstanceState.getInt(mPosKeyString, 0);
        isPlaying = savedInstanceState.getBoolean(mMediaStatekey, false);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onSaveInstanceState
     * @Description:
     * @param outState
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveMediaState();
        outState.putInt(mPosKeyString, mMediaPosition);
        outState.putBoolean(mMediaStatekey, isPlaying);
        super.onSaveInstanceState(outState);
    }

    /**
     * @Title: saveMediaState
     * @Description: 保存当前状态
     * @throws
     */
    private void saveMediaState() {
        if (mMediaPlayer != null) {
            mMediaPosition = mMediaPlayer.getCurrentPosition();
            isPlaying = mMediaPlayer.isPlaying();
        }
        Log.d("", "#### mMediaPosition = " + mMediaPosition + ", isPlaying = "
                + isPlaying);
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onStop
     * @Description:
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
        mTimer.cancel();
        mHandler.removeMessages(HIDE_BTN_MSG);
        mHandler.removeMessages(UPDATE_PRGBAR_MSG);
        // 注销传感器
        mShakeController.unregisterShakeListener(VideoActivity.this);
        saveMediaState();
        // 状态
        VideoState state = new VideoState(mMediaPosition, isPlaying, mVideoPath);
        if (isNeedReplay) {
            state.setPlaying(isNeedReplay);
        }
        UMVideoStateUtil.saveVideoState(VideoActivity.this, state);
        super.onStop();
    }

    private VideoState mVideoState = new VideoState();

    /**
     * (非 Javadoc)
     * 
     * @Title: onResume
     * @Description:
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        Log.d("", "#### video onResume");
        mVideoState = UMVideoStateUtil.restoreVideoState(VideoActivity.this);
        int position = mVideoState.getPosition();
        if (position > 0) {
            mMediaPosition = position;
        }
        isNeedReplay = mVideoState.isPlaying();
        if (mMediaPlayer != null && isNeedReplay) {
            try {
                updateUI(position);
                mMediaPlayer.seekTo(position);
                mMediaPlayer.start();
                changePlayBtnBg();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        registerShakeSensor();
        showViews();
        super.onResume();
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onActivityResult
     * @Description: 在此必须回调,得到授权的结果
     * @param requestCode
     * @param resultCode
     * @param data
     * @see android.app.Activity#onActivityResult(int, int,
     *      android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        super.onDestroy();
    }

}
