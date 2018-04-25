package com.android.banana.commlib.view.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lingjiu on 2017/11/1.
 */
public class DownLoadBar extends View {
    /**
     * 进度条画笔
     */
    private Paint mPaintStroke;
    /**
     * 进度条背景
     */
    private Paint mPaintFill;
    /**
     * 进度条所在矩形
     */
    private RectF mRectF, mInternalRectF, mStopRectF, mStartRectF;
    //中心点坐标
    private int mCenterX, mCenterY;

    /**
     * 当前进度
     */
    private long mCurrentProgress = 0;
    /**
     * 进度最大值,可以为时间
     */
    private long maxProgress = 60 * 1000;
    /**
     * 是否正在加载
     */
    private boolean isLoading = true;
    /**
     * 控件宽高
     */
    private int minSide;
    /**
     * 内边框
     */
    private static float padding = 20.0f;
    /**
     * 文字进度画笔
     */
    private Paint mPaintText;
    /**
     * 字体大小
     */
    private static final int FONT_SIZE = 40;
    /**
     * 动画延迟
     */
    private static int PROGRESS_DELAY = 5;
    /**
     * 点击监听器
     */
    private ClickListener listener;
    /**
     * 是否加载完成
     */
    private boolean isComplete = false;
    /**
     * 文字y坐标
     */
    private int textY;

    //中心圆背景颜色
    private int centerCircleColor;

    //进度条颜色
    private int mLoadingBarColor;

    //中心圆的距外边的距离
    private float mDistance = 20f;

    private Runnable countDownRunnable;

    private ProgressListener progressListener;

    public DownLoadBar(Context context) {
        super(context);
        init();

    }

    public DownLoadBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DownLoadBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLoadingBarColor = Color.parseColor("#FF7E00");
        centerCircleColor = Color.parseColor("#99000000");

        mPaintStroke = new Paint();
        mPaintStroke.setAntiAlias(true);
        mPaintStroke.setColor(mLoadingBarColor);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setStrokeWidth(12.0f);

        mPaintFill = new Paint();
        mPaintFill.setAntiAlias(true);
        mPaintFill.setColor(Color.WHITE);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setStrokeWidth(13.0f);

        mPaintText = new Paint();
        mPaintText.setTextSize(FONT_SIZE);
        mPaintText.setColor(mLoadingBarColor);
        mPaintText.setTextAlign(Paint.Align.CENTER);

        mRectF = new RectF();
        mInternalRectF = new RectF();
        mStopRectF = new RectF();
        mStartRectF = new RectF();

        countDownRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isLoading || mCurrentProgress >= maxProgress) {
                    if (progressListener != null)
                        progressListener.pause(mCurrentProgress, maxProgress);
                    return;
                }
                mCurrentProgress += 100;
                if (progressListener != null)
                    progressListener.progressing(mCurrentProgress, maxProgress);
                postInvalidate();
                postDelayed(countDownRunnable, 100);
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        minSide = width < height ? width : height;
        this.setMeasuredDimension(width, height);
        mCenterX = minSide / 2;
        mCenterY = minSide / 2;
        mRectF.set(padding, padding, minSide - padding, minSide - padding);
        mInternalRectF.set(padding + 6.5f, padding + 6.5f, minSide - padding - 6.5f, minSide - padding - 6.5f);
        mStopRectF.set(mInternalRectF.left + mInternalRectF.width() / 4, mInternalRectF.top + mInternalRectF.width() / 4,
                mInternalRectF.right - mInternalRectF.width() / 4, mInternalRectF.bottom - mInternalRectF.width() / 4);
        mStartRectF.set(mInternalRectF.left + mDistance, mInternalRectF.top + mDistance,
                mInternalRectF.right - mDistance, mInternalRectF.bottom - mDistance);
        Paint.FontMetricsInt fontMetrics = mPaintText.getFontMetricsInt();
        textY = (minSide - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintStroke.setColor(Color.WHITE);
        canvas.drawArc(mRectF, 0, 360, false, mPaintStroke);
        mPaintFill.setColor(centerCircleColor);
        canvas.drawArc(mInternalRectF, 0, 360, false, mPaintFill);

        if (isLoading) {
            if (mCurrentProgress == 0) {
                mPaintFill.setColor(mLoadingBarColor);
                canvas.drawArc(mStartRectF, 0, 360, false, mPaintFill);
            } else if (mCurrentProgress <= maxProgress) {
                //canvas.drawText(mCurrentProgress + "%", mCenterX, textY, mPaintText);
                mPaintFill.setColor(mLoadingBarColor);
                mPaintStroke.setColor(mLoadingBarColor);
                canvas.drawArc(mRectF, -90, calculateProgress(), false, mPaintStroke);
                canvas.drawRect(mStopRectF, mPaintFill);
            }
        } else {
            //暂停
            mPaintFill.setColor(mLoadingBarColor);
            mPaintStroke.setColor(mLoadingBarColor);
            canvas.drawRect(mStopRectF, mPaintFill);
            canvas.drawArc(mRectF, 0, calculateProgress(), false, mPaintStroke);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
           /* case MotionEvent.ACTION_DOWN:
                return true;*/
            case MotionEvent.ACTION_UP:
                /*if (isComplete) {
                    return true;
                }
                if (listener != null) {
                    if (isLoading) {
                        listener.pause();
                    } else {
                        listener.restart();
                    }
                    isLoading = !isLoading;
                }
                postInvalidate();*/
                /*isStart = !isStart;
                controllCountDown(isStart);*/

                break;

        }
        return super.onTouchEvent(event);
    }

    public void controlProgress(boolean isLoading) {
        this.isLoading = isLoading;
        mCurrentProgress = 0;
        if (isLoading) {
            postDelayed(countDownRunnable, 100);
        } else {
            removeCallbacks(countDownRunnable);
        }
    }

    public void startCountDown() {
        isLoading = true;
        postDelayed(countDownRunnable, 100);
    }

    public void stopCountDown() {
        isLoading = false;
        removeCallbacks(countDownRunnable);
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setCurrentProgress(int mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
        postInvalidate();
    }

    private float calculateProgress() {
        return (360 * mCurrentProgress) / maxProgress;
    }


}
