package com.android.xjq.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.xjq.R;

/**
 * 可以双向拖动的seekbar
 * 用于视频剪辑
 * <p>
 * Created by lingjiu on 2018/3/3.
 */

public class MySeekBar extends View {

    private static final String TAG = MySeekBar.class.getName();
    private int currentX, currentX2;//进度条左右位置
    private Bitmap bitmap1, bitmap2;
    private Paint paint, textPaint;
    private int mScollBarWidth, mScollBarHeight;  //控件宽度=滑动条宽度+滑动块宽度
    private int progressTop, progressBootom;//滑块顶部与底部高
    private int offset;//控件的偏移量
    private int progressBegin, progressEnd;
    private OnSeekBarChangeListener mBarChangeListener;
    private RectF mRect, mBeginRect, mEndRect;
    private int mWidth, mHeight;
    private int thumbHeight, thumbWidth;
    private String timeText = "00:00";
    private int progressBgColor, selectedBgColor, textColor;
    private int textLocation;
    private int type;

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MySeekBar, 0, 0);
        textLocation = array.getInt(R.styleable.MySeekBar_text_location, 0);
        type = array.getInt(R.styleable.MySeekBar_type, 0);
        textColor = array.getColor(R.styleable.MySeekBar_time_text_color, ContextCompat.getColor(context, R.color.colorTextG2));
        array.recycle();
        selectedBgColor = ContextCompat.getColor(context, R.color.light_yellow7);
        progressBgColor = ContextCompat.getColor(context, R.color.alpha_black);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        thumbHeight = 80;
        if (type == 0) {
            bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_seekbar_begin_thumb);
            bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_seekbar_end_thumb);
            thumbWidth = bitmap1.getWidth() * thumbHeight / bitmap1.getHeight();
            //bitmap1,bitmap2大小一致
        }

        mRect = new RectF();
        mBeginRect = new RectF();
        mEndRect = new RectF();
    }

    //默认执行，计算view的宽高,在onDraw()之前
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = resolveSizeAndState(widthMeasureSpec);
        mHeight = 150;
        setMeasuredDimension(mWidth, mHeight);
        Log.i("MySeekBar", "mWidth" + mWidth + " mHeight=" + mHeight + "  thumbWidth=" + thumbWidth + " thumbHeight=" + thumbHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        offset = 30;
        mScollBarWidth = mWidth - offset;
        // mScollBarHeight = mHeight / 2;
       /* thumbHeight = mHeight / 2;
        thumbWidth = mHeight / 2;*/
        currentX = offset;
        currentX2 = mWidth - offset;
        progressTop = thumbHeight;
        progressBootom = mHeight;
        //初始位置
        progressBegin = 0;
        progressEnd = 100;

        textPaint.setTextSize((progressBootom - progressTop) * 2 / 3);

    }

    private int resolveSizeAndState(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = specSize;
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                result = specSize;
        }
        return result;
    }


    @interface TouchLocation {
        int touchLeftThumb = -1;
        int touchNoThumb = 0;
        int touchRightThumb = 1;
    }

    private int touchLocation = TouchLocation.touchNoThumb;

    private long preCallBackTime;

    //处理手势
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (type == 1)
            return false;

        int x = (int) event.getX();

        Log.i("MySeekBar", "event.getAction=" + event.getAction() + " ++++++++  " + judgeTouchThumb(event.getX(), event.getY()));

        Log.i("MySeekBar", "x=" + x + "   y=" + event.getY() + "   mLeftRect = " + mBeginRect.toString());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //没有触摸到滑块不处理
                touchLocation = judgeTouchThumb(event.getX(), event.getY());
                if (touchLocation == TouchLocation.touchNoThumb) return false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchLocation == TouchLocation.touchLeftThumb) {
                    if (x < offset) {
                        currentX = offset;
                    } else if (x >= currentX2 - 10) {
                        currentX = currentX2 - 10;
                    } else {
                        currentX = x;
                    }
                    //左滑块进度
                    progressBegin = (currentX - offset) * 100 / mScollBarWidth;
                    //Log.e("tag","currentX"+currentX);
                    //Log.e("tag","mScollBarWidth"+mScollBarWidth);
                    //Log.e("tag","progressBegin"+progressBegin);
                    long currentTime = System.currentTimeMillis();
                    if (mBarChangeListener != null && currentTime - preCallBackTime > 500) {
                        preCallBackTime = currentTime;
                        mBarChangeListener.onProgressChanged(this, true, progressBegin, progressEnd);
                    }
                } else {
                    if (x > mScollBarWidth) {
                        currentX2 = mScollBarWidth;
                    } else if (x <= currentX + 10) {
                        currentX2 = currentX + 10;
                    } else {
                        currentX2 = x;
                    }
                    //右滑块进度
                    progressEnd = (currentX2) * 100 / mScollBarWidth;

                    long currentTime = System.currentTimeMillis();
                    if (mBarChangeListener != null && currentTime - preCallBackTime > 500) {
                        mBarChangeListener.onProgressChanged(this, false, progressBegin, progressEnd);
                        preCallBackTime = currentTime;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // invalidate();
                break;
        }
        return true;
    }

    private int judgeTouchThumb(float x, float y) {
        return mBeginRect.contains(x, y) ? TouchLocation.touchLeftThumb :
                (mEndRect.contains(x, y) ? TouchLocation.touchRightThumb : TouchLocation.touchNoThumb);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        mRect.set(offset, progressTop, mWidth - offset, progressBootom);
        paint.setColor(progressBgColor);
        canvas.drawRect(mRect, paint);
        //绘制前景
        mRect.set(currentX, progressTop, currentX2, progressBootom);
        paint.setColor(selectedBgColor);
        canvas.drawRect(mRect, paint);

        if (type == 0) {
            //绘制左滑块
            mBeginRect.set(currentX - thumbWidth / 2, 0, currentX + thumbWidth / 2, thumbHeight);
            canvas.drawBitmap(bitmap1, null, mBeginRect, paint);
            //绘制右滑块、getPaddingRight()设置paddingRight,其他同理
            mEndRect.set(currentX2 - thumbWidth / 2, 0, currentX2 + thumbWidth / 2, thumbHeight);
            canvas.drawBitmap(bitmap2, null, mEndRect, paint);
        }

        //绘制文字
        float textWidth = textPaint.measureText(timeText);
        float textLeft = 0;
        if (textLocation == 0) {
            textLeft = mWidth / 2 - textWidth / 2;
        } else {
            textLeft = mWidth - textWidth - 15 - offset;
        }
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float baseY = (progressBootom - progressTop) / 2 + fontHeight / 2 - fontMetrics.bottom;
        canvas.drawText(timeText, textLeft, thumbHeight + baseY, textPaint);
        //invalidate();
    }


    public void setProgress(int currentProgress) {
        currentX2 = currentProgress;
        invalidate();
    }

    public void startCountDown(final long totalTime) {
        new CountDownTimer(totalTime, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentX2 = (int) ((mWidth - offset) * (totalTime - millisUntilFinished) / totalTime);
                setProgress(currentX2);
            }

            @Override
            public void onFinish() {
                currentX2 = mWidth - offset;
                setProgress(currentX2);
            }
        }.start();
    }

    //设置总时长
    public void setTotalTime(long totalTime) {
        timeText = com.android.banana.commlib.utils.TimeUtils.timeFormat(totalTime);
        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
        this.mBarChangeListener = mListener;
    }

    //回调函数，在滑动时实时调用，改变输入框的值
    public interface OnSeekBarChangeListener {
        //滑动前
        public void onProgressBefore();

        //滑动时
        public void onProgressChanged(MySeekBar seekBar, boolean isTouchLeft, int progressLow,
                                      int progressHigh);

        //滑动后
        public void onProgressAfter();
    }
}