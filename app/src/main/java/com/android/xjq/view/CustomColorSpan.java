package com.android.xjq.view;

import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

import com.android.xjq.XjqApplication;
import com.android.xjq.R;

/**
 * Created by zhouyi on 2017/4/26.
 */

public class CustomColorSpan extends ForegroundColorSpan {
    public CustomColorSpan(@ColorInt int color) {
        super(color);
    }

    public CustomColorSpan(Parcel src) {
        super(src);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setTextSize(XjqApplication.getContext().getResources().getDimension(R.dimen.sp16));
    }
}
