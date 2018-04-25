package com.android.banana.commlib.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by lingjiu on 2017/9/11.
 */

public class DimensionUtils {
    public static float spToPx(float sp, Context context) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics()));
    }

    public static float dpToPx(float dp, Context context) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static float pxToDp(float dp, Context context) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, context.getResources().getDisplayMetrics()));
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();//屏幕高度
        return screenHeight;
    }


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        return width;
    }
}
