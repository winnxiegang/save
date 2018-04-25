package com.android.xjq.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;

/**
 * Created by ajiao on 2018\3\2 0002.
 */

public class FooterTabView extends LinearLayout {

    private FrameLayout leftTab;
    private FrameLayout rightTab;
    private TextView leftTxt;
    private TextView rightTxt;
    private onTabClick mListener;

    public FooterTabView(Context context) {
        super(context);
        init(context);
    }

    public FooterTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FooterTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_footer_tabs, this);
        leftTab = (FrameLayout) findViewById(R.id.foot_ball_lay);
        rightTab = (FrameLayout)findViewById(R.id.basket_ball_lay);
        leftTxt = (TextView)findViewById(R.id.foot_ball_txt);
        rightTxt = (TextView)findViewById(R.id.basket_ball_txt);
        initDefaultUI();
        leftTab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                leftTab.setSelected(true);
                leftTxt.setSelected(true);
                rightTab.setSelected(false);
                rightTxt.setSelected(false);
                if (mListener != null) {
                    mListener.onLeftTabClick();
                }
            }
        });
        rightTab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rightTab.setSelected(true);
                rightTxt.setSelected(true);
                leftTab.setSelected(false);
                leftTxt.setSelected(false);
                if (mListener != null) {
                    mListener.onRightTabClick();
                }
            }
        });
    }

    private void initDefaultUI() {
        leftTab.setSelected(true);
        leftTxt.setSelected(true);
        rightTab.setSelected(false);
        rightTxt.setSelected(false);
    }

    public interface onTabClick {
        public void onLeftTabClick();
        public void onRightTabClick();
    }

    public void setOnTabClickListener(onTabClick listener) {
        mListener = listener;
    }
}
