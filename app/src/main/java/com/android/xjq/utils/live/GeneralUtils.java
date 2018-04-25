package com.android.xjq.utils.live;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by lingjiu on 2017/3/24.
 */

public class GeneralUtils {

    public static int dpToPx(Context context, float dpVal) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public static int pxToDp(Context context, float pxVal){

        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pxVal, context.getResources().getDisplayMetrics());
    }
}
