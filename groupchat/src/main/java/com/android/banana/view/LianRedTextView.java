package com.android.banana.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.android.banana.R;
import com.android.library.Utils.LibAppUtil;


/**
 * Created by qiaomu on 2017/5/26.
 */

public class LianRedTextView extends android.support.v7.widget.AppCompatTextView {
    private Paint mPaint;

    private String ellipsizeStr = "连红";//后缀
    private float textHeight;
    private float textBaseLine;

    private int padding = 2;//文字与四周间隔
    private int max = 30;//超过这个数就显示+号了
    private int lianRedNum = 40;
    private int redColor;

    public LianRedTextView(Context context) {
        super(context);
        init(context);
    }

    public LianRedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LianRedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        redColor = ContextCompat.getColor(context, R.color.main_red);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(redColor);
        mPaint.setTextSize(getTextSize());

        padding = LibAppUtil.dip2px(context, padding);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();

        textBaseLine = (metrics.bottom + metrics.top) / 2 - metrics.top;
        textHeight = metrics.descent - metrics.ascent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float measureText = mPaint.measureText(lianRedNum + ellipsizeStr) + 4 * padding;//连红数字左右间距，连红汉字前后间距
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) measureText, MeasureSpec.EXACTLY);
        float height = textHeight;
        if (lianRedNum > max) {
            /*如果连红大于30那么要算上+号的高度  这里给定2 * padding 的高度*/
            height += 2 * padding;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        setBackgroundColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (lianRedNum < 2)
            return;
        String numStr = "";
        if (lianRedNum > max) {
            numStr = String.valueOf(max);
        } else {
            numStr = String.valueOf(lianRedNum);
        }

        float numWidth = mPaint.measureText(numStr);
        //数字 ：一位数和两位数的宽度不同对待，看起来比较和谐
        float right;
        if (lianRedNum < 10) {
            right = 2 * numWidth;
        } else {
            right = 1.3f * numWidth + padding;
        }
        mPaint.setStyle(Paint.Style.FILL);
        /*绘制红色方块*/
        canvas.drawRect(0, 0, right, getHeight(), mPaint);

        float baseLine = getHeight() / 2 + textBaseLine / 2;
        mPaint.setColor(Color.WHITE);
        /*绘制连红数字*/
        if (lianRedNum <= max) {
            canvas.drawText(numStr, right / 2 - numWidth / 2, baseLine, mPaint);
        } else {
            /*绘制+号*/
            float plusWidth = mPaint.measureText("+");
            canvas.drawText(numStr, (right - numWidth - plusWidth) / 2, baseLine, mPaint);
            canvas.drawText("+", (right + numWidth - plusWidth) / 2, textBaseLine, mPaint);
        }
        /*绘制连红 两个字*/
        mPaint.setColor(redColor);
        canvas.drawText(ellipsizeStr, right + padding / 2, baseLine, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    public void setLianRedNum(int lianRedNum) {
        this.lianRedNum = lianRedNum;
        if (lianRedNum < 2)
            this.setVisibility(GONE);
        else {
            this.setVisibility(VISIBLE);
            invalidate();
        }
    }
}
