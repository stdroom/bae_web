
package com.umeng.soexample.activity.beans;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: VideoState.java
 * @Package com.example.umscrshotdemo.beans
 * @Description: 视频状态类
 * @author Honghui He
 * @version V1.0
 */

public class VideoState {

    /**
     * 
     */
    private int mPosition = 0;
    /**
     * 
     */
    private boolean isPlaying = false;
    private String mVideoPath = "";

    /**
     * @Title: VideoState
     * @Description: VideoState Constructor
     */
    public VideoState() {
    }

    /**
     * @Title: VideoState
     * @Description: VideoState Constructor
     * @param position
     * @param playing
     */
    public VideoState(int position, boolean playing, String path) {
        mPosition = position;
        isPlaying = playing;
        mVideoPath = path;
    }

    /**
     * 获取 mPosition
     * 
     * @return 返回 mPosition
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * 设置 mPosition
     * 
     * @param 对mPosition进行赋值
     */
    public void setPosition(int position) {
        this.mPosition = position;
    }

    /**
     * 获取 isPlaying
     * 
     * @return 返回 isPlaying
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 设置 isPlaying
     * 
     * @param 对isPlaying进行赋值
     */
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    /**
     * 获取 mVideoPath
     * 
     * @return 返回 mVideoPath
     */
    public String getVideoPath() {
        return mVideoPath;
    }

    /**
     * 设置 mVideoPath
     * 
     * @param 对mVideoPath进行赋值
     */
    public void setVideoPath(String path) {
        this.mVideoPath = path;
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: toString
     * @Description:
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VideoState [mPosition=" + mPosition + ", isPlaying=" + isPlaying + ", mVideoPath="
                + mVideoPath + "]";
    }

}
