package com.android.xjq.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.xjq.R;

/**
 * Created by ajiao on 2017/10/9 0009.
 */

public class SwitchTabView extends FrameLayout{
    private TextView mLeftText;
    private TextView mRightText;
    private IonSwitchTab mListener;
    public static final int TAB_LEFT = 0;
    public static final int TAB_RIGHT = 1;
    public SwitchTabView(Context context) {
        super(context);
        init(context);
    }

    public SwitchTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public SwitchTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_switch_tab, null);
        addView(view);
        setListener(view);
    }

    private void setListener(View view) {
        mLeftText = (TextView)view.findViewById(R.id.switch_left_txt);
        mRightText = (TextView)view.findViewById(R.id.switch_right_txt);
        mLeftText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                highLightLeft();
                if (mListener != null) {
                    mListener.onSwitchLeftTab();
                }
            }
        });
        mRightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                highLightRight();
                if (mListener != null) {
                    mListener.onSwitchRightTab();
                }

            }
        });
    }

    private void highLightLeft () {
        mLeftText.setBackgroundResource(R.drawable.shape_solid_left_half_radius);
        mRightText.setBackgroundResource(R.drawable.shape_stroke_right_half_radius);
        mRightText.setTextColor(getResources().getColor(R.color.gold_deep));
        mLeftText.setTextColor(getResources().getColor(R.color.white));
    }
    private void highLightRight() {
        mLeftText.setBackgroundResource(R.drawable.shape_stroke_left_half_radius);
        mRightText.setBackgroundResource(R.drawable.shape_solid_right_half_radius);
        mRightText.setTextColor(getResources().getColor(R.color.white));
        mLeftText.setTextColor(getResources().getColor(R.color.gold_deep));
    }

    public void setCheckedTab(int tab) {
        if (tab == TAB_LEFT) {
            highLightLeft();
        } else if (tab == TAB_RIGHT) {
            highLightRight();
        }
    }

    public interface IonSwitchTab {
        public void onSwitchLeftTab();
        public void onSwitchRightTab();
    }

    public void setOnSwitchTabListener(IonSwitchTab listener) {
       this.mListener =  listener;
    }
}
