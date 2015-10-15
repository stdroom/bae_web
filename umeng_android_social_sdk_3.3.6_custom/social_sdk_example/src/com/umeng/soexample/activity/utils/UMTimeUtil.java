package com.umeng.soexample.activity.utils;

import java.util.Locale;

/**   
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved 
 *
 * @Title: UMTimeUtil.java
 * @Package com.example.ummeidaplayer.utils
 * @Description: 
 *
 * @author Honghui He  
 * @version V1.0   
 */

public class UMTimeUtil {
    /**
     * @Title: calculateTime
     * @Description: 
     *      计算播放到position位置时对应的时间
     * 
     * @param usec 播放到的position，为毫秒
     * @return      
     * @throws
     */
    public static String calculateTime(int usec) {
        // 获得秒
        int seconds = usec / 1000;
        // 获得分钟数
        int minutes = seconds / 60;
        // 十位
        int tMinutes = minutes / 10;
        // 获得个位上的分钟数
        int oneMunite = minutes % 10;
        // 获取剩余的秒数
        int secs = seconds % 60;
        int tSec = secs / 10;
        int oneSec = secs % 10;
        String videoTimeStr = String.format(Locale.US,
                "%d%d:%d%d", tMinutes, oneMunite, tSec, oneSec);
        return videoTimeStr;
    }
}
