package com.android.xjq.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;


/**
 * Created by ajiao on 2018\2\5 0005.
 */

public class OverlapTabWithDrawableView extends LinearLayout {

    private IonSwitchTab mListener;
    private TextView leftTxt;
    private TextView rightTxt;
    private LinearLayout leftTabLay;
    private LinearLayout rightTabLay;

    public OverlapTabWithDrawableView(Context context) {
        super(context);
        init(context);
    }

    public OverlapTabWithDrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OverlapTabWithDrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_overlap_tab, this);
        leftTabLay = (LinearLayout)findViewById(R.id.left_tab_lay);
        rightTabLay = (LinearLayout)findViewById(R.id.right_tab_lay);
        leftTxt = (TextView)findViewById(R.id.left_txt);
        rightTxt = (TextView)findViewById(R.id.right_txt);
        leftTabLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSwitchLeftTab();
                leftTabLay.setBackground(getResources().getDrawable(R.drawable.shape_widget_overal_tab_h));
                leftTxt.setTextColor(getResources().getColor(R.color.main_red));
                rightTabLay.setBackground(getResources().getDrawable(R.drawable.shape_widget_overal_tab_n));
                rightTxt.setTextColor(getResources().getColor(R.color.white));
                Drawable drawableLeft = context.getResources().getDrawable(R.drawable.foot_ball_h);
                drawableLeft.setBounds(0,0,drawableLeft.getMinimumWidth(),drawableLeft.getMinimumHeight());
                leftTxt.setCompoundDrawables(drawableLeft, null, null, null);
                Drawable drawableRight = context.getResources().getDrawable(R.drawable.basket_ball_n);
                drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
                rightTxt.setCompoundDrawables(drawableRight, null, null, null);
            }
        });
        rightTabLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSwitchRightTab();
                rightTabLay.setBackground(getResources().getDrawable(R.drawable.shape_widget_overal_tab_h));
                rightTxt.setTextColor(getResources().getColor(R.color.main_red));
                leftTabLay.setBackground(getResources().getDrawable(R.drawable.shape_widget_overal_tab_n));
                leftTxt.setTextColor(getResources().getColor(R.color.white));
                Drawable drawableLeft = context.getResources().getDrawable(R.drawable.foot_ball_n);
                drawableLeft.setBounds(0,0,drawableLeft.getMinimumWidth(),drawableLeft.getMinimumHeight());
                leftTxt.setCompoundDrawables(drawableLeft, null, null, null);
                Drawable drawableRight = context.getResources().getDrawable(R.drawable.basket_ball_h);
                drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
                rightTxt.setCompoundDrawables(drawableRight, null, null, null);
            }
        });
        //LayoutInflater.from(context).inflate(R.layout.widget_overlap_tab, this);
        //TextView leftTab = (TextView)findViewById(R.id.)
        /*TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OverlapTab);
        Drawable bg = a.getDrawable(R.styleable.OverlapTab_OverlapTab_bg);
        int textColor = a.getColor(R.styleable.OverlapTab_OverlapTab_text_color, getResources().getColor(R.color.white));
        String leftText = a.getString(R.styleable.OverlapTab_OverlapTab_left_text);
        String rightText = a.getString(R.styleable.OverlapTab_OverlapTab_right_text);
        Drawable leftIcon = a.getDrawable(R.styleable.OverlapTab_OverlapTab_left_icon);
        Drawable rightIcon = a.getDrawable(R.styleable.OverlapTab_OverlapTab_right_icon);
        float cornerRadius = a.getDimension(R.styleable.OverlapTab_OverlapTab_corner_radius, getResources().getDimension(R.dimen.dp30));

        TextView leftTab = new TextView(context);
        leftTab.setText(leftText);
        leftTab.setCompoundDrawablePadding(10);
        leftTab.setCompoundDrawables(leftIcon,null, null, null);
        leftTab.setTextColor(textColor);
        leftTab.setBackground(bg);
        addView(leftTab);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = (int)cornerRadius;
        TextView rightTab = new TextView(context);
        rightTab.setText(rightText);
        rightTab.setCompoundDrawablePadding(10);
        rightTab.setCompoundDrawables(rightIcon,null, null, null);
        rightTab.setTextColor(textColor);
        rightTab.setBackground(bg);
        addView(rightTab, params);*/
        //a.recycle();
    }

    public interface IonSwitchTab {
        public void onSwitchLeftTab();
        public void onSwitchRightTab();
    }

    public void setOnSwitchTabListener(IonSwitchTab listener) {
        mListener = listener;
    }


}
