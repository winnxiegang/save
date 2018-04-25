package com.android.xjq.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.xjq.R;


/**
 * Created by lingjiu on 2017/6/6.
 */

public class CustomProgressView extends View {
    private static String TAG = CustomProgressView.class.getName();
    private Paint mPaint;
    private Paint textPaint;
    private int mHeight;
    private int mWidth;
    private int[] mColors = {Color.parseColor("#fc8d82"), Color.parseColor("#e21b07")};//进度条颜色（渐变色的2个点）
    private int backgroundColor = Color.parseColor("#dddddd");//进度条默认颜色
    private int textColor = Color.GRAY;//文本颜色
    private float mRatio;//比例
    private long progressValue = 0;//当前进度值
    private long totalProgressValue = 0;//最大值
    private static final int TOTAL_PAINT_TIMES = 50;
    private int mPaintTimes;

    public CustomProgressView(Context context) {
        this(context, null);
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获得我们所定义的自定义样式属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgressView_startColor:
                    // 渐变色之起始颜色，默认设置为红色
                    mColors[0] = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomProgressView_endColor:
                    // 渐变色之结束颜色，默认设置为品红
                    mColors[1] = a.getColor(attr, Color.MAGENTA);
                    break;
                case R.styleable.CustomProgressView_backgroundColor:
                    // 进度条默认颜色，默认设置为灰色
                    backgroundColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.CustomProgressView_textColor:
                    // 文字颜色，默认设置为灰色
                    textColor = a.getColor(attr, Color.GRAY);
                    break;
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

    public void setProgressValue(long progressValue, long totalProgressValue, boolean animEnable) {
        this.progressValue = progressValue;
        if (progressValue > totalProgressValue) {
            totalProgressValue = progressValue;
        }
        this.totalProgressValue = totalProgressValue;
        invalidate();
        Log.i("customView", "log: progressValue=" + progressValue +
                ", this.progressValue =" + this.progressValue +", totalProgressValue = " + totalProgressValue);
        enableAnim(animEnable);
        invalidate();
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

        float progressValue = this.progressValue * ((float) mPaintTimes / TOTAL_PAINT_TIMES);

        float offsetLength = mRatio * 10;//前后留空
        float offsetSpace = mHeight / 2;
        float progressTotalWidth = mWidth - 2 * offsetLength;
        float section = ((float) progressValue) / totalProgressValue;
        if (section > 1)
            section = 1;
        mPaint.setShader(null);
        mPaint.setColor(backgroundColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //画出进度条底部背景
        canvas.drawLine(offsetLength, offsetSpace, mWidth - offsetLength, offsetSpace, mPaint);
        LinearGradient gradient = new LinearGradient(0, 0, progressTotalWidth, 0, mColors, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
        //画出当前渐变进度,前半段用ROUND类型画(有圆弧),后半段用SQUARE类型画(无圆弧)
        canvas.drawLine(offsetLength, offsetSpace, offsetLength + section * progressTotalWidth /*/ 2*/, offsetSpace, mPaint);
       /* if (progressValue == totalProgressValue) {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
        } else {
            mPaint.setStrokeCap(Paint.Cap.BUTT);
        }
        canvas.drawLine(offsetLength + section * progressTotalWidth / 2, offsetSpace, offsetLength + section * progressTotalWidth, offsetSpace, mPaint);*/
       /* //计算坐标使文字居中
       Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float  fontHeight = fontMetrics.bottom - fontMetrics.top;
        float baseY =  mHeight/2 + fontHeight/2 - fontMetrics.bottom;
        canvas.drawText(""+progressValue+"%", progressWidth+2*offsetWidth, baseY, mPaint);//略微偏下，baseline*/
        //画文字
        String text = (int) this.progressValue + "/" + (totalProgressValue == this.progressValue ?
                totalProgressValue == 0 ? 0 : "-" : totalProgressValue);
        float textWidth = textPaint.measureText(text);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float baseY = mHeight / 2 + fontHeight / 2 - fontMetrics.bottom;
        canvas.drawText(text, mWidth / 2 - textWidth / 2, baseY, textPaint);

        if (mPaintTimes < TOTAL_PAINT_TIMES) {
            invalidate();
        }
    }
}
