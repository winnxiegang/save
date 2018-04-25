package com.android.banana.commlib.loadmore;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.banana.commlib.R;


/**
 * 自定义样式加载更多
 * <p>
 * Created by lingjiu on 17-3-31.
 */
public class CommonLoadMoreView extends LoadMoreView implements View.OnClickListener {

    private ViewFlipper vPages;
    private LinearLayout vLoading;
    private TextView vRetry;
    private TextView vEnd;
    private TextView expandTv;

    private OnRetryClickListener mListener;

    public CommonLoadMoreView(Context context) {
        this(context, null);
    }

    public CommonLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommonLoadMoreView);
        // 获取初始化显示哪一个view
        int initShowView = array.getInt(R.styleable.CommonLoadMoreView_init_show_view, 0);
        initShowView = initShowView > 3 ? 0 : initShowView;
        array.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.default_load_more, this, true);
        /*FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        setLayoutParams(layoutParams);*/
        vPages = (ViewFlipper) findViewById(R.id.default_loadmore_flipper);
        vLoading = (LinearLayout) findViewById(R.id.default_loadmore_loading);
        vRetry = (TextView) findViewById(R.id.default_loadmore_retry);
        vEnd = (TextView) findViewById(R.id.default_loadmore_end);
        expandTv = ((TextView) findViewById(R.id.default_expand_more));
        vRetry.setOnClickListener(this);
        vPages.setDisplayedChild(initShowView);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.default_loadmore_retry) {
            if (mListener != null) {
                showLoading();
                mListener.OnRetryClick();
            }
        }
    }

    public void showLoading() {
        vPages.setDisplayedChild(0);
    }

    public void showRetry() {
        vPages.setDisplayedChild(1);
    }

    public void showEnd() {
        vPages.setDisplayedChild(2);
    }

    public void showExpandMore() {
        vPages.setDisplayedChild(3);
    }

    public boolean isLoadMoreEnable() {
        return vPages.getDisplayedChild() == 0;
    }

    @Override
    public void setOnRetryClickListener(OnRetryClickListener listener) {
        mListener = listener;
    }

}
