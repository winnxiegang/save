package com.android.banana.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.android.banana.R;
import com.android.library.Utils.LibAppUtil;


/**
 * Created by qiaomu on 2017/5/27.
 */

public class WithRedTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;
    private String member = "0人";
    private int padding = 10;
    private final float textHeight;
    private int bgColor;
    private int iconRes = R.drawable.icon_with_red;
    private Bitmap bitmap;
    private int bitWidth;
    private float strWidth;

    public WithRedTextView(Context context) {
        this(context, null);
    }

    public WithRedTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WithRedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        padding = LibAppUtil.dip2px(context, padding);
        bgColor = ContextCompat.getColor(context, R.color.with_red);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAlpha(10);
        mPaint.setTextSize(getTextSize());

        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        textHeight = metrics.descent - metrics.ascent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (TextUtils.equals(member, "0人")) return;
        strWidth = mPaint.measureText(member);
        bitmap = BitmapFactory.decodeResource(getResources(), iconRes);
        if (bitmap == null)
            return;
        bitWidth = bitmap.getWidth();
        int bitHeight = bitmap.getHeight();

        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (strWidth + bitWidth + 2 * padding), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(bitHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float top = (getHeight() - textHeight) / 2;

        if (!TextUtils.isEmpty(member)) {
            mPaint.setColor(bgColor);

            Path path = new Path();
            path.moveTo(bitWidth / 2, top);
            path.addArc(new RectF(0, top, bitWidth, getHeight() - top), -90, 180);
            path.lineTo(getWidth() - top, getHeight() - top);
            path.lineTo(getWidth() - top, top);
            path.addOval(new RectF(getWidth() - 3 * top, top, getWidth(), getHeight() - top), Path.Direction.CCW);
            path.close();

            canvas.drawPath(path, mPaint);
            mPaint.setColor(Color.WHITE);
            mPaint.setAlpha(255);
            float left = (getWidth() - bitWidth) / 2 - strWidth / 2;
            canvas.drawText(member, left + bitWidth, getBaseline(), mPaint);
        }

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
        }

    }

    public void setMember(int member, @DrawableRes int iconRes) {

        if (member <= 0) {
            return;
        }

        this.member = member + "人";
        if (iconRes != 0)
            this.iconRes = iconRes;

        setVisibility(VISIBLE);
        requestLayout();

    }
}
