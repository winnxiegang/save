package com.android.banana.commlib.liveScore.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import com.android.banana.commlib.R;

import java.util.List;

/**
 * Created by mrs on 2017/3/30.
 */

public class GsLiveMatchTextView extends android.support.v7.widget.AppCompatTextView {

    private String TAG = this.getClass().getSimpleName();
    private Paint mPaint, mBgPaint;
    private int mWinCol, mFaiedCol, mPeaceCol;//胜负平背景颜色

    private float mItemGap = 5;//字体之间的间隙 dp
    private float mItemConner = 2;//小方块的圆角 dp
    private float mItemRadiu = 20;//小方块宽高
    private float mPadding = 10;//离顶部 底部间距

    private List<String> score;

    public GsLiveMatchTextView(Context context) {
        this(context, null);
    }

    public GsLiveMatchTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(getTextSize());
        mPaint.setColor(getCurrentTextColor());

        //初始化小方块画笔
        mWinCol = context.getResources().getColor(R.color.liked_text_color);
        mFaiedCol = context.getResources().getColor(R.color.analysisLostColor);
        mPeaceCol = context.getResources().getColor(R.color.hall_of_fame);

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(mWinCol);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GsLiveMatchTextView);

        mItemGap = a.getDimension(R.styleable.GsLiveMatchTextView_item_gap, mItemGap);
        mItemGap = dp2px(context, mItemGap);  // to px

        mItemConner = a.getDimension(R.styleable.GsLiveMatchTextView_item_coner, mItemConner);
        mItemConner = dp2px(context, mItemConner);

        mItemRadiu = a.getDimension(R.styleable.GsLiveMatchTextView_item_radiu, mItemRadiu);
        mItemRadiu = dp2px(context, mItemRadiu);

        a.getDimension(R.styleable.GsLiveMatchTextView_item_padding, mPadding);
        mPadding = dp2px(context, mPadding);

        a.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (score == null || score.size() == 0) {
            setGravity(Gravity.CENTER);
            setText("暂无");
            return;
        }

        setGravity(Gravity.CENTER_HORIZONTAL);
        setText("近五场");
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        int len = score.size();
        //计算第一个小方块的起始位置
        float leftOffset = width / 2 - (len * mItemRadiu + (len - 1) * mItemGap) / 2;
        float topOffset = height / 2 - mItemRadiu / 2 + mPadding;

        RectF rectf = new RectF();
        Rect tvBound = new Rect();
        for (int i = 0; i < len; i++) {
            String str = score.get(i);
            mPaint.getTextBounds(str, 0, str.length(), tvBound);

            float left = leftOffset + i * mItemRadiu + i * mItemGap;
            rectf.set(left,
                    topOffset,
                    left + mItemRadiu,
                    topOffset + mItemRadiu
            );

            resetPaintColor(str);

            canvas.drawRoundRect(rectf, mItemConner, mItemConner, mBgPaint);
            canvas.drawText(str,
                    left + mItemRadiu / 2 - tvBound.width() / 2,
                    topOffset + mItemRadiu / 2 + tvBound.height() / 3,
                    mPaint);

            //Log.e(TAG, rectf.toString());
        }
    }

    private void resetPaintColor(String str) {
        if (TextUtils.equals(str, "胜"))
            mBgPaint.setColor(mWinCol);
        else if (TextUtils.equals(str, "负")) {
            mBgPaint.setColor(mFaiedCol);
        } else if (TextUtils.equals(str, "平")) {
            mBgPaint.setColor(mPeaceCol);
        } else {
            mBgPaint.setColor(mWinCol);
        }

    }

    public void setMatchScore(List<String> score) {
        this.score = score;
        invalidate();
    }

    private int dp2px(Context context, float dpvalue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpvalue * scale + 0.5f);
    }
}
