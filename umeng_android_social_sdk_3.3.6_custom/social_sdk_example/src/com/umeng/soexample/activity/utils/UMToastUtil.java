
package com.umeng.soexample.activity.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: ToastUtil.java
 * @Package com.example.umscrshotdemo.utils
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class UMToastUtil {

    /**
     * @Title: showToast
     * @Description: toast 信息
     * @param act
     * @param text
     * @throws
     */
    public static void showToast(Activity act, String text) {
        if (act != null && !TextUtils.isEmpty(text) ) {
            Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
        }
    }
}
