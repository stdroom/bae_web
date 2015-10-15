
package com.umeng.soexample.activity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.umeng.soexample.activity.beans.VideoState;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: UMVideoStateUtil.java
 * @Package com.example.umscrshotdemo.utils
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class UMVideoStateUtil {
    /**
     * 
     */
    private static final String SP_NAME = UMVideoStateUtil.class.getSimpleName();
    private static final String POSITION_KEY = "position";
    private static final String ISPLAYING_KEY = "isplaying";

    /**
     * @Title: saveVideoState
     * @Description:
     * @param context
     * @throws
     */
    public static void saveVideoState(Context context, VideoState state) {
        if (state == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                SP_NAME, Context.MODE_PRIVATE);
        synchronized (sp) {
            int position = state.getPosition();
            boolean isPlaying = state.isPlaying();
            Editor edit = sp.edit();
            if (position > 0) {
                edit.putInt(POSITION_KEY, position);
            }
            edit.putBoolean(ISPLAYING_KEY, isPlaying);
            edit.commit();
            Log.d(SP_NAME, "### 状态保存. " + state.toString());
        }
    }

    /**
     * @Title: restoreVideoState
     * @Description: 读取状态
     * @param context
     * @return
     * @throws
     */
    public static VideoState restoreVideoState(Context context) {
        // 视频状态
        VideoState state = new VideoState();
        SharedPreferences sp = context.getSharedPreferences(
                SP_NAME, Context.MODE_PRIVATE);
        synchronized (sp) {
            state.setPosition(sp.getInt(POSITION_KEY, 0));
            state.setPlaying(sp.getBoolean(ISPLAYING_KEY, false));
            Log.d(SP_NAME, "### 恢复状态. " + state.toString());
        }
        return state;
    }

}
