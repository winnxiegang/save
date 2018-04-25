package com.android.xjq.view.expandtv;

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
import android.util.Log;

import com.android.xjq.R;

/**
 * Created by lingjiu on 2017/9/28.
 */

public class CustomMedalDrawable extends Drawable {

    private Paint mPaint;
    private Bitmap bmp;
    private RectF rectF;
    private String drawText;
    private Paint textPaint;
    private int marginBottom;
    private int marginLeft;

    public CustomMedalDrawable(String drawText, int resId, Resources resources) {
        if (resId == 0) resId = R.drawable.icon_fans_medal_lv1;
        this.bmp = BitmapFactory.decodeResource(resources, resId);
        this.drawText = drawText;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        //textPaint.setTextSize(resources.getDimensionPixelSize(R.dimen.sp10));
        marginBottom = resources.getDimensionPixelOffset(R.dimen.dp3);
        marginLeft = resources.getDimensionPixelOffset(R.dimen.dp1Point5);
    }


    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        canvas.drawBitmap(bmp, rect, rect, mPaint);
        textPaint.setTextSize(rect.height() * 2 / 3);
        float textWidth = textPaint.measureText(drawText);
        Log.e("draw", "rect.height =" + rect.height() + " rect.width =" + rect.width() + " textWidth=" + textWidth);
       /* textPaint.setTextSize(rect.height() * 2 / 3);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;*/
        //设定文字距底部一定的距离
        float baseY = rect.height() - marginBottom;
        //Log.e("draw", fontMetrics.top + "=====" + fontMetrics.bottom + "=====" + marginBottom);
        canvas.drawText(drawText, (rect.width() * 2 / 3 - textWidth) / 3, baseY, textPaint);
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
