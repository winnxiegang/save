package com.android.banana.commlib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;

/**
 * Created by ajiao on 2018\2\24 0024.
 */

public class TimeLineMarkView extends LinearLayout{
    private TextView topLine;
    private TextView bottomLine;
    private TextView dot;

    public TimeLineMarkView(Context context) {
        super(context);
        init(context);
    }

    public TimeLineMarkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeLineMarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.widget_time_line_mark, this);
        topLine = (TextView)findViewById(R.id.topLine);
        bottomLine = (TextView)findViewById(R.id.bottomLine);
        dot = (TextView)findViewById(R.id.tvDot);
    }

    public void hideDot() {
        dot.setVisibility(View.GONE);
    }
    public void showDot() {
        dot.setVisibility(View.VISIBLE);
    }
    public void hideTopLine() {
        topLine.setVisibility(View.GONE);
    }
    public void showTopLine() {
        topLine.setVisibility(View.VISIBLE);
    }
    public void hideBottomLine() {
        bottomLine.setVisibility(View.GONE);
    }
    public void showBottomLine() {
        bottomLine.setVisibility(View.VISIBLE);
    }

}
