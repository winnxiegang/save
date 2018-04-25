package com.android.banana.commlib.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.library.Utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2017/11/27.
 * <p>
 * 无限轮播图
 */

public class BannerView extends FrameLayout {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private PageIndicatorView mIndicatorView;
    private BannerAdapter mBannerAdapter;
    private TextView mBannerTipTv;
    private List<String> mListUrls = new ArrayList<>();
    private List<String> mListTips = new ArrayList<>();

    private int tipHeight = (int) dp2px(30);//提示语高度
    private int tipBgStartColor = Color.parseColor("#0D000000");//提示语背景色
    private int tipBgEndColor = Color.parseColor("#1A000000");//提示语背景色
    private int tipColor = Color.WHITE;//提示语的颜色
    private int tipSize = 16;//提示语的字体大小

    //这里实现的效果是指示器在提示语上方,并且在右侧
    //上下位置修改getLayoutParams() 和这个值 修改gravity来实现
    private int indicatorVerticalMargin = tipHeight;
    //左右位置可以通过getLayoutParams() 和这个值 修改gravity来实现
    private int indicatorHorizontalMargin = (int) dp2px(10);
    private final int indicatorSelectColor = Color.parseColor("#f63f3f");
    private final int indicatorUnSelectColor = Color.parseColor("#e0e0e0");

    private boolean infinite = true;//无限轮播
    private int interval = 3000;//切换间隔时间
    private Handler mHandler = new Handler();
    private int firstVisiblePosition = 0;
    private Drawable mDrawable;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //滑动列表
    private void initRecyclerView() {
        if (mRecyclerView != null)
            return;
        mRecyclerView = new RecyclerView(getContext());
        mBannerAdapter = new BannerAdapter(getContext(), mListUrls, mListTips);
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mRecyclerView.setAdapter(mBannerAdapter);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        addView(mRecyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition() % mListUrls.size();
                    setBannerIndicatorAndTipView();
                }
            }
        });
    }

    //提示语
    private void initBannerTipView() {
        if (mBannerTipTv != null || mListTips == null || mListTips.size() == 0)
            return;
        mBannerTipTv = new TextView(getContext());
        mBannerTipTv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        mBannerTipTv.setTextColor(tipColor);
        mBannerTipTv.setSingleLine(true);
        mBannerTipTv.setEllipsize(TextUtils.TruncateAt.END);
        // GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{tipBgStartColor, tipBgEndColor});
        mBannerTipTv.setBackground(mDrawable);
        mBannerTipTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, tipSize);
        LayoutParams params = new LayoutParams(-1, tipHeight);
        params.gravity = Gravity.BOTTOM;
        mBannerTipTv.setLayoutParams(params);
        addView(mBannerTipTv);
    }

    //指示器
    private void initIndicatorView() {
        if (mIndicatorView != null)
            return;
        mIndicatorView = new PageIndicatorView(getContext());
        LayoutParams params1 = new LayoutParams(-2, -2);
        params1.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        params1.rightMargin = indicatorHorizontalMargin;
        params1.bottomMargin = indicatorVerticalMargin;
        mIndicatorView.setLayoutParams(params1);

        mIndicatorView.setIndicatorColor(indicatorSelectColor, indicatorUnSelectColor);
        mIndicatorView.initIndicator(mListTips.size());
        addView(mIndicatorView);
    }

    public void setBannerUrls(List<String> urls, List<String> titleList) {

        if (urls == null || urls.size() == 0) {
            if (mRecyclerView != null)
                mRecyclerView.setAdapter(null);
            if (mIndicatorView != null)
                mIndicatorView.clear();

            mRecyclerView = null;
            mIndicatorView = null;
            mBannerTipTv = null;
            mBannerAdapter = null;
            this.mListUrls.clear();
            this.setVisibility(GONE);
            return;
        }

        if (!isDiff(urls))
            return;

        mListUrls.clear();
        mListUrls.addAll(urls);
        if (titleList != null) {
            mListTips.clear();
            mListTips.addAll(titleList);
        }

        initRecyclerView();
        initBannerTipView();
        initIndicatorView();

        mBannerAdapter.notifyDataSetChanged();
        onResume();
    }

    public PageIndicatorView getIndicatorView() {
        return mIndicatorView;
    }

    public TextView getBannerTipTv() {
        return mBannerTipTv;
    }

    public void setTipBackgroundDrawable(Drawable drawable) {
        mDrawable = drawable;
        if (mBannerTipTv == null)
            return;
        mBannerTipTv.setBackground(mDrawable);
    }

    //无限轮播适配器
    private class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {
        private Context mContext;
        private List<String> mUrls;
        private List<String> mTitleTips;

        public BannerAdapter(Context context, List<String> urls, List<String> listTips) {
            mContext = context;
            mUrls = urls;
            mTitleTips = listTips;
        }

        @Override
        public BannerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new RecyclerView.LayoutParams(-1, (int) dp2px(150)));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new BannerHolder(imageView);
        }

        @Override
        public void onBindViewHolder(BannerHolder holder, final int position) {
            final int realPosition = position % mUrls.size();
            String url = mUrls.get(realPosition);
            PicUtils.load(mContext, ((ImageView) holder.itemView), url, R.drawable.bg_banner_placeholder);

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onBannerClick(realPosition);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mUrls == null ? 0 : (mUrls.size() == 1 ? 1 : infinite ? Integer.MAX_VALUE : mUrls.size());
        }

        class BannerHolder extends RecyclerView.ViewHolder {

            public BannerHolder(View itemView) {
                super(itemView);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onStop();
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            sendEmptyMessageDelayed4Loop();
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean isDiff(List<String> newUrls) {
        if (mListUrls == null || mListUrls.size() == 0)
            return true;
        if (newUrls == null || newUrls.size() == 0)
            return true;
        if (mListUrls.size() != newUrls.size())
            return true;

        boolean diff = false;
        for (int i = 0; i < mListUrls.size(); i++) {
            for (int j = 0; j < newUrls.size(); j++) {
                if (!TextUtils.equals(mListUrls.get(i), newUrls.get(j))) {
                    diff = true;
                    break;
                }
            }
        }
        return diff;
    }

    private float dp2px(int dpValue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (float) (metrics.density * dpValue + 0.5);
    }

    private float sp2px(int spValue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (float) (metrics.scaledDensity * spValue + 0.5);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mHandler != null) {
            if (visibility == VISIBLE) {
                sendEmptyMessageDelayed4Loop();
            } else {
                onStop();
            }
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int nextPosition = firstVisiblePosition + 1;
            mRecyclerView.smoothScrollToPosition(nextPosition);
            sendEmptyMessageDelayed4Loop();
        }
    };

    private void setBannerIndicatorAndTipView() {
        LogUtils.e("setBannerIndicatorAndTipView", firstVisiblePosition + "--");
        if (mIndicatorView != null) {
            mIndicatorView.setSelectedPage(firstVisiblePosition);
        }
        if (mBannerTipTv != null && mListTips != null && firstVisiblePosition < mListTips.size()) {
            mBannerTipTv.setText(mListTips.get(firstVisiblePosition));
        }
    }

    public void onResume() {
        sendEmptyMessageDelayed4Loop();
    }

    public void onStop() {
        mHandler.removeCallbacksAndMessages(null);
    }

    void sendEmptyMessageDelayed4Loop() {
        onStop();
        if (mListUrls.size() > 1)
            mHandler.postDelayed(mRunnable, interval);
    }

    private onClickListener mListener;


    public interface onClickListener {
        void onBannerClick(int position);
    }

    public void setOnBannerClickListener(onClickListener listener) {
        mListener = listener;
    }
}
