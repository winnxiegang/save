package com.android.banana.commlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.banana.commlib.R;

/**
 * Created by lingjiu on 2017/6/6.
 */

public class PercentProgressView extends View {
    private static String TAG = PercentProgressView.class.getName();
    private Paint mPaint;
    private Paint textPaint;
    private int mHeight;
    private int mWidth;
    private int[] mColors = {Color.parseColor("#fd6969"), Color.parseColor("#70a8f3")};//进度条颜色（渐变色的2个点）
    private int backgroundColor = Color.parseColor("#dddddd");//进度条默认颜色
    private int textColor = Color.GRAY;//文本颜色
    private float mRatio;//比例
    private long hostProgressValue = 0;
    private long guestProgressValue = 0;
    private long totalProgressValue = 0;
    private static final int TOTAL_PAINT_TIMES = 50;
    private int mPaintTimes;
    private static final int DRAWABLE_PADDING = 50;

    public PercentProgressView(Context context) {
        this(context, null);
    }

    public PercentProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获得我们所定义的自定义样式属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomProgressView_hostProgressColor) {
                // 主队进度条颜色，默认设置为红色
                mColors[0] = a.getColor(attr, Color.RED);
            } else if (attr == R.styleable.CustomProgressView_guestProgressColor) {
                // 客队进度条颜色，默认设置为蓝色
                mColors[1] = a.getColor(attr, Color.BLUE);
            } else if (attr == R.styleable.CustomProgressView_backgroundColor2) {
                // 进度条默认颜色，默认设置为灰色
                backgroundColor = a.getColor(attr, Color.GRAY);
            } else if (attr == R.styleable.CustomProgressView_textColor) {
                // 文字颜色，默认设置为灰色
                textColor = a.getColor(attr, Color.GRAY);
            }
        }
        a.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.WHITE);
    }

    public long getHostProgressValue() {
        return hostProgressValue;
    }

    public long getGuestProgressValue() {
        return guestProgressValue;
    }

    public void setProgressValue(long hostValue, long guestValue, boolean animEnable) {
        this.hostProgressValue = hostValue;
        this.guestProgressValue = guestValue;
        this.totalProgressValue = hostValue + guestValue;
        enableAnim(animEnable);
        invalidate();
    }

   public void setProgressValue(long hostValue, long guestValue) {
       setProgressValue(hostValue,guestValue,false);
   }

    public void setColors(int[] colors) {
        mColors = colors;
    }

    //是否开启动画
    public void enableAnim(boolean animEnable) {
        mPaintTimes = animEnable ? 0 : TOTAL_PAINT_TIMES - 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
                width = specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                width = getPaddingLeft() + getPaddingRight();
                break;
        }

        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                height = width / 10;
                break;
            case MeasureSpec.AT_MOST:
                height = width / 10;
                break;
            case MeasureSpec.EXACTLY:
                height = specSize;
                break;
        }

        Log.i(TAG, "log: w=" + width + " h=" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mRatio = Math.min((float) mWidth / 300, (float) mHeight / 30);
        mPaint.setStrokeWidth(mRatio * 20);//线宽
        textPaint.setStrokeWidth(1);
        textPaint.setTextSize(mRatio * 18);
        Log.i(TAG, "w=" + w + " mWidth=" + mWidth + " mHeight=" + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaintTimes < TOTAL_PAINT_TIMES) {
            mPaintTimes++;
        }
        float hostProgressValue = this.hostProgressValue * ((float) mPaintTimes / TOTAL_PAINT_TIMES);
        float hostSection = ((float) hostProgressValue) / totalProgressValue;
        float guestProgressValue = this.guestProgressValue * ((float) mPaintTimes / TOTAL_PAINT_TIMES);
        float guestSection = ((float)guestProgressValue) / totalProgressValue;
        float offsetLength = mRatio * 10;//前后留空
        float offsetSpace = mHeight / 2;
        float halfTotalProgressWidth = mWidth/2 - offsetLength;

        mPaint.setShader(null);
        mPaint.setColor(backgroundColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //画出进度条底部背景
        canvas.drawLine(offsetLength, offsetSpace, mWidth - offsetLength, offsetSpace, mPaint);
     /*   LinearGradient gradient = new LinearGradient(0, 0, halfTotalProgressWidth, 0, mColors, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);*/
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float baseY = mHeight / 2 + fontHeight / 2 - fontMetrics.bottom;
        //画出2边主队和客队的比值
        mPaint.setColor( mColors[1]);
        canvas.drawLine(mWidth/2, offsetSpace, mWidth/2 + guestSection * halfTotalProgressWidth, offsetSpace, mPaint);
        mPaint.setColor( mColors[0]);
        canvas.drawLine(mWidth/2, offsetSpace, mWidth/2 - hostSection * halfTotalProgressWidth, offsetSpace, mPaint);
        Bitmap ctrIcon = BitmapFactory.decodeResource(getResources(), R.drawable.progress_bar_ctr_icon);
        canvas.drawBitmap(ctrIcon, mWidth/2 - ctrIcon.getWidth()/2, offsetSpace - ctrIcon.getHeight()/2, mPaint);
        String hostValue = String.valueOf((int)hostProgressValue);
        canvas.drawText(hostValue, mWidth/2 - textPaint.measureText(hostValue) - DRAWABLE_PADDING, baseY, textPaint);
        String guestValue = String.valueOf((int)guestProgressValue);
        canvas.drawText(guestValue, mWidth/2 + DRAWABLE_PADDING, baseY, textPaint);

        if (mPaintTimes < TOTAL_PAINT_TIMES) {
            invalidate();
        }
    }

}
