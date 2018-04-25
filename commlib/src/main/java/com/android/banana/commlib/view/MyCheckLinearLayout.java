package com.android.banana.commlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;


public class MyCheckLinearLayout extends LinearLayout {

    private boolean mIsChecked = false;

    public MyCheckLinearLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    @Override
    public void setOnClickListener(final OnClickListener l) {

        OnClickListener onClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIsChecked) {

                    mIsChecked = false;

                    setBackgroundResource(R.drawable.kjgg_time_selector_bg_normal);

                } else {

                    mIsChecked = true;

                    setBackgroundResource(R.drawable.kjgg_time_selector_checked);
                }

                l.onClick(v);

            }
        };
        super.setOnClickListener(onClickListener);

    }

    public boolean isChecked() {

        return mIsChecked;

    }

    public void setChecked(boolean isChecked) {

        mIsChecked = isChecked;

        setBackgroundResource(R.drawable.kjgg_time_selector_checked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int allChildMeasureWidth=0;
        for(int i=0;i<getChildCount();i++){
            allChildMeasureWidth+=getChildAt(i).getMeasuredWidth();
        }
        int width = getMeasuredWidth();
        int allChildWidth = width-allChildMeasureWidth;
        float maxTextWidth = 0;
        TextView maxWidthChild = null;
        for (int i = 0; i < getChildCount(); i++) {
            if(getChildAt(i).getVisibility()== View.GONE){
                continue;
            }
            if (getChildAt(i) instanceof ImageView) {
                allChildWidth += getChildAt(i).getMeasuredWidth();
            } else if (getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) getChildAt(i);
                float textViewWidth = getTextViewWidth(tv.getText().toString(), tv.getTextSize());
                if(textViewWidth>maxTextWidth){
                    maxTextWidth = textViewWidth;
                    maxWidthChild = tv;
                }
                allChildWidth+=textViewWidth;
            }
        }

        if(allChildWidth>width){
            if(maxWidthChild!=null){
                float textSize = maxWidthChild.getTextSize();
                allChildWidth-=getTextViewWidth(maxWidthChild.getText().toString(),textSize);
                String text = maxWidthChild.getText().toString();
                String dstText = null;
                for(int i=1;i<text.length();i++){
                    dstText = text.substring(0,text.length()-i)+"...";
                    allChildWidth+=getTextViewWidth(dstText,textSize);
                    if(allChildWidth<width){
                        maxWidthChild.setText(dstText);
                        break;
                    }
                    allChildWidth-=getTextViewWidth(dstText,textSize);
                }
            }
        }
        //requestLayout();
    }

    private float getTextViewWidth(String displayText, float textSize) {
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(textSize);
        float textWidth = mTextPaint.measureText(displayText);
        return textWidth;
    }
}
