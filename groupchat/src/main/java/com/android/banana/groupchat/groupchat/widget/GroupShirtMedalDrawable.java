package com.android.banana.groupchat.groupchat.widget;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.android.banana.R;

/**
 * Created by lingjiu on 2017/12/11.
 */

public class GroupShirtMedalDrawable extends Drawable {
    private Paint mPaint;
    private Bitmap bmp;
    private RectF rectF;
    private String drawText;
    private Paint textPaint;
    private int marginBottom;

    public GroupShirtMedalDrawable(String drawText, Resources resources) {
        this.bmp = BitmapFactory.decodeResource(resources, R.drawable.icon_group_shirt_with_txt_bg);
        this.drawText = drawText;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        //textPaint.setTextSize(resources.getDimensionPixelSize(R.dimen.sp10));
        marginBottom = resources.getDimensionPixelOffset(R.dimen.dp4);
    }


    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        canvas.drawBitmap(bmp, rect, rect, mPaint);
        Log.e("draw", rect.height() + "=====" + rect.width() + "  " + getIntrinsicWidth() + " " + getIntrinsicHeight());
        if (TextUtils.isEmpty(drawText)) return;
        //绘制文字
        textPaint.setTextSize(rect.height()/ 2);
        float textWidth = textPaint.measureText(drawText);
        //textPaint.setTextSize(rect.height() * 2 / 3);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        //设定文字距底部一定的距离
        float baseY = rect.height() / 2 + fontHeight / 3;
        //Log.e("draw", fontMetrics.top + "=====" + fontMetrics.bottom + "=====" + marginBottom);
        canvas.drawText(drawText, rect.width() * 3 / 4 - textWidth * 2 / 3, baseY, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    // getIntrinsicWidth、getIntrinsicHeight主要是为了在View使用wrap_content的时候，提供一下尺寸
    @Override
    public int getIntrinsicHeight() {
        return bmp.getHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return bmp.getWidth();
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }


}
