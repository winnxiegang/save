package com.android.banana.commlib.liveScore.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.android.banana.commlib.R;
import com.android.banana.commlib.bean.liveScoreBean.PathAnimEventResultBean;
import com.android.banana.commlib.liveScore.livescoreEnum.AnimTypeEnum;
import com.android.banana.commlib.utils.LibAppUtil;

import java.util.List;


/**
 * Created by qiaomu on 2017/8/19.
 */

public class ThumbSeekBar extends AppCompatSeekBar implements SeekBar.OnSeekBarChangeListener {
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private LruCache<String, Bitmap> mMemoryCache;

    private final float imgWidth = LibAppUtil.dip2px(getContext(), 12);
    private float width, height, yOffset, lastX;
    private Paint mBitPaint;
    private Drawable mThumb;
    private boolean enableDrag = true;//是否允许拖动
    public boolean isInTouch = false;
    private int stopTrackingTouchIndex = 0;
    private long minActionTime;
    private float totalTime;
    public long homeAs = 0;


    private PathAnimEventResultBean.AnimEventBean lastHomeBean, lastGuestBean;

    private List<PathAnimEventResultBean.AnimEventBean> mAnimEventList;

    public ThumbSeekBar(Context context) {
        super(context);
        init();
    }

    public ThumbSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThumbSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        minActionTime = 0;
        totalTime = 0;
        mAnimEventList = null;
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);

        mMemoryCache = new LruCache<String, Bitmap>(maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        setOnSeekBarChangeListener(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        lastHomeBean = lastGuestBean = null;
        width = getWidth();
        height = getHeight();
        yOffset = LibAppUtil.dip2px(getContext(), 5);
        lastX = getPaddingStart() + getPaddingLeft();

        if (mAnimEventList == null || mAnimEventList.size() == 0)
            return;
        int size = mAnimEventList.size();
        for (int i = 0; i < size; i++) {
            PathAnimEventResultBean.AnimEventBean animEvent = mAnimEventList.get(i);
            drawImg(animEvent, canvas);
        }
    }

    void drawImg(PathAnimEventResultBean.AnimEventBean bean, Canvas canvas) {
        String atypeName = (bean != null && bean.actionType != null) ? bean.actionType.getName() : null;
        Bitmap bitmap = getBitmap(atypeName);
        if (bitmap != null) {
            float x = 0, y = 0;
            //求左上角坐标
            long raceT = getTime(bean.raceTime);
            x = raceT * width / totalTime;
            if (homeAs == bean.actionSubject) {
                y = 0;
                if (isNearLastOne(lastHomeBean, raceT)) {
                    long raceTLast = getTime(lastHomeBean.raceTime);
                    x = lastX + imgWidth / 2;
                }
                lastHomeBean = bean;
            } else {
                y = height / 2 + yOffset;
                if (isNearLastOne(lastGuestBean, raceT)) {
                    long raceTLast = getTime(lastGuestBean.raceTime);
                    x = lastX + imgWidth / 2;
                }
                lastGuestBean = bean;
            }
            lastX = x + getPaddingStart();
            canvas.drawBitmap(bitmap, lastX, y, mBitPaint);
        }

    }

    boolean isNearLastOne(PathAnimEventResultBean.AnimEventBean beanLast, long raceT) {
        if (beanLast != null) {
            long raceTLast = getTime(beanLast.raceTime);
            long deltaT = raceT - raceTLast;
            return (deltaT <= 60 * 1000);
        }
        return false;
    }


    void computeData() {
        if (mAnimEventList != null && mAnimEventList.size() > 0) {
            int size = mAnimEventList.size();
            PathAnimEventResultBean.AnimEventBean bean0 = mAnimEventList.get(0);
            PathAnimEventResultBean.AnimEventBean beanLast = mAnimEventList.get(size - 1);
            if (bean0 != null && beanLast != null) {
                long raceTime0 = getTime(bean0.raceTime);
                minActionTime = bean0.actionTime - raceTime0;
                totalTime = beanLast.actionTime - minActionTime;
            }
        }
    }

    long getTime(String mmss) {
        String[] arr = mmss.split(":");
        return Integer.parseInt(arr[1]) * 1000 + Long.parseLong(arr[0]) * 60 * 1000;
    }

    //乌龙  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event_oolong_small);
    private Bitmap getBitmap(String atypeName) {
        AnimTypeEnum typeEnum = AnimTypeEnum.safeValueOf(atypeName);
        Bitmap bitmap = mMemoryCache.get(atypeName);
        switch (typeEnum) {
            case GOAL:
                if (bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event_score_small);
                break;
            case STANDBY_FOR_PENALTY_SHOOTOUT:
            case PENALTY:
                if (bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event_penalty_small);
                break;
            case RED_CARD:
                if (bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event_red_card_small);
                break;
            case YELLOW_CARD:
                if (bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event_yellow_card_small);
                break;
            case SHOT_ON_TARGET:
                if (bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_shot_target);
                break;
            case SHOT_OFF_TARGET:
                if (bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_shot_wide);
                break;
            case SUBSTITUTION:
                if (bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event_change);
                break;
        }
        if (bitmap != null && mMemoryCache.get(atypeName) == null) {
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, (int) imgWidth, (int) imgWidth, true);
            if (bitmap2 != null) {
                mMemoryCache.put(atypeName, bitmap2);
                if (!bitmap.isRecycled() && bitmap != bitmap2)
                    bitmap.recycle();
                return bitmap2;
            }
        }
        return bitmap;
    }


    public void setPathAnimEventList(List<PathAnimEventResultBean.AnimEventBean> animEventList) {
        mAnimEventList = animEventList;
        if (mAnimEventList == null || mAnimEventList.size() == 0)
            return;
        computeData();
        setMax((int) totalTime);

        invalidate();
    }

    private PathAnimEventResultBean.AnimEventBean getNearestPoint() {
        if (mAnimEventList == null || mAnimEventList.size() == 0)
            return null;
        stopTrackingTouchIndex = -1;
        long time = (long) (getProgress() + minActionTime);
        for (PathAnimEventResultBean.AnimEventBean bean : mAnimEventList) {
            stopTrackingTouchIndex++;
            long indexProgress = getTime(bean.raceTime);
            if (bean.actionTime >= time) {
                return bean;
            }
        }
        return mAnimEventList.get(mAnimEventList.size() - 1);
    }

    @Override
    public void setThumb(Drawable thumb) {
        mThumb = thumb;
        super.setThumb(thumb);
    }


    public Drawable getThumb() {
        return mThumb;
    }

    public void setDragEnable(boolean enable) {
        this.enableDrag = enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isInTouch = true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isInTouch = false;
        }
        return enableDrag ? super.onTouchEvent(event) : false;
    }

    public boolean isInTouch() {
        return isInTouch;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        isInTouch = true & fromUser;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mListener != null)
            mListener.onStartTrackingTouch(seekBar);
        isInTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int max = seekBar.getMax();
        int progress = seekBar.getProgress();
        if (max != 0) {
            PathAnimEventResultBean.AnimEventBean bean = getNearestPoint();
            if (bean != null) {
                if (mListener != null)
                    mListener.onStopTrackingTouch(seekBar, bean, stopTrackingTouchIndex);
            }
        }
        isInTouch = false;
    }

    public void updateProgress(long actionTime) {
        setProgress((int) (actionTime - minActionTime));
    }

    public void setHomeTeamId(long homeAs) {
        this.homeAs = homeAs;
    }

    public interface OnThumbSeekBarChangeListener {
        void onStopTrackingTouch(SeekBar seekBar, PathAnimEventResultBean.AnimEventBean animEventBean, int stopTrackingTouchIndex);

        void onStartTrackingTouch(SeekBar seekBar);
    }

    private OnThumbSeekBarChangeListener mListener;

    public void setThumbOnSeekBarChangeListener(OnThumbSeekBarChangeListener listener) {
        this.mListener = listener;
    }
}

