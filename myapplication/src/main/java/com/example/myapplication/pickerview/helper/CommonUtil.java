package com.example.myapplication.pickerview.helper;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by 孙伟 on 2017/8/18.
 */

public class CommonUtil {
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }
}
