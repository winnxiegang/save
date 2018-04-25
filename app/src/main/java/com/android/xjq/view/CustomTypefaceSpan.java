package com.android.xjq.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by zhouyi on 2017/4/14.
 */

public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;

    private String color;


    public CustomTypefaceSpan(String family, Typeface type, String color) {
        super(family);
        newType = type;
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType, color);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType, color);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf, String color) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setColor(Color.parseColor(color));

        paint.setTypeface(tf);
    }


}