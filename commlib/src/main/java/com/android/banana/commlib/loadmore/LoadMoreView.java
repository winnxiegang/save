package com.android.banana.commlib.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 加载更多view接口
 * <p>
 * Created by lingjiu on 17-3-31.
 */
public abstract class LoadMoreView extends FrameLayout {


    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void showLoading();

    public abstract void showRetry();

    public abstract void showEnd();

    public abstract boolean isLoadMoreEnable();

    public abstract void setOnRetryClickListener(OnRetryClickListener listener);
}
