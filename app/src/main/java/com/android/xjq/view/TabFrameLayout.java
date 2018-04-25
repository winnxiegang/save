package com.android.xjq.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.DimensionUtils;
import com.android.xjq.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by qiaomu on 2017/9/20.
 */

public class TabFrameLayout extends FrameLayout {
    public static int TAB_STATUS_NORMAL = 0;
    public static int TAB_STATUS_CLICK_SCROLL = 1;

    private String mDefaultTabText;
    private int mTabTextColorRes = R.color.text_selector_tab_text;
    private int mDefaultTabIcRes;
    private int mDefaultExtraTabIcRes;
    private int mTabTextSize = 13;
    private int mTabIcSize;
    private int mExtraTabIcSize;

    private boolean mTabExtraEnable;

    private ImageView mTabIv, extraTabIv;
    private TextView mTabTv;


    private int mLoadedCompeletedCount;//用于验证 下载动态图标时是否两种状态下的图都下载完成了
    private StateListDrawable mListDrawable;//用于承载两张动态图
    private LinearLayout containerLayout;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mRadius;
    private boolean mVisible = false;

    public void setTabExtraEnable(boolean mTabExtraEnable) {
        this.mTabExtraEnable = mTabExtraEnable;
        if (mTabExtraEnable && getChildCount() == 1)
            addView(getExtraTabView(getContext()));

    }

    public TabFrameLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TabFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabFrameLayout);

        mDefaultTabText = ta.getString(R.styleable.TabFrameLayout_tab_text);
        mTabTextSize = ta.getDimensionPixelSize(R.styleable.TabFrameLayout_tab_text_size, mTabTextSize);
        mTabTextColorRes = ta.getResourceId(R.styleable.TabFrameLayout_tab_text_color, mTabTextColorRes);

        mDefaultTabIcRes = ta.getResourceId(R.styleable.TabFrameLayout_tab_ic, 0);
        mTabIcSize = ta.getDimensionPixelSize(R.styleable.TabFrameLayout_tab_ic_size, mTabIcSize);

        mDefaultExtraTabIcRes = ta.getResourceId(R.styleable.TabFrameLayout_tab_extra_ic, R.drawable.icon_extra_tab_view);
        mTabExtraEnable = ta.getBoolean(R.styleable.TabFrameLayout_tab_extra_ic_enable, mTabExtraEnable);

        ta.recycle();

        mTabIcSize = (int) DimensionUtils.dpToPx(25, context);
        mExtraTabIcSize = (int) DimensionUtils.dpToPx(40, context);
        mRadius = (int) DimensionUtils.dpToPx(3, context);
        mPaint.setColor(Color.RED);
        addTabView(context);
    }

    private void addTabView(Context context) {
        containerLayout = new LinearLayout(context);
        containerLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setGravity(Gravity.CENTER);

        mTabIv = new ImageView(context);
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(-2, -2);
        params.height = (int) DimensionUtils.dpToPx(24, getContext());
        params.width = (int) DimensionUtils.dpToPx(24, getContext());
        mTabIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mTabIv.setAdjustViewBounds(true);
        mTabIv.setLayoutParams(params);
        if (mDefaultTabIcRes > 0)
            mTabIv.setImageDrawable(ContextCompat.getDrawable(context, mDefaultTabIcRes));

        mTabTv = new TextView(context);
        LinearLayoutCompat.LayoutParams imgParams = new LinearLayoutCompat.LayoutParams(-2, -2);
        mTabTv.setLayoutParams(imgParams);
        mTabTv.setTextSize(mTabTextSize);
        ColorStateList stateList = ContextCompat.getColorStateList(context, mTabTextColorRes);
        mTabTv.setTextColor(stateList);
        mTabTv.setText(mDefaultTabText);

        containerLayout.addView(mTabIv);
        containerLayout.addView(mTabTv);

        addView(containerLayout);

        if (mTabExtraEnable)
            addView(getExtraTabView(context));
    }

    private ImageView getExtraTabView(Context context) {
        extraTabIv = new ImageView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mExtraTabIcSize, mExtraTabIcSize);
        params.gravity = Gravity.CENTER;
        extraTabIv.setLayoutParams(params);
        extraTabIv.setAlpha(0.0f);
        if (mDefaultTabIcRes > 0)
            extraTabIv.setImageDrawable(ContextCompat.getDrawable(context, mDefaultExtraTabIcRes));
        return extraTabIv;
    }

    public void setImageResource(int imageResource) {
        mDefaultTabIcRes = imageResource;
        if (mTabIv != null) mTabIv.setImageResource(mDefaultTabIcRes);
    }

    public void setText(String text) {
        mDefaultTabText = text;
        if (mTabTv != null) mTabTv.setText(mDefaultTabText);
    }

    public void setUrl(String normalUrl, String pressedUrl) {
        mLoadedCompeletedCount = 0;
        mListDrawable = new StateListDrawable();
        Picasso.with(getContext().getApplicationContext()).load(normalUrl).into(normalTarget);
        Picasso.with(getContext().getApplicationContext()).load(pressedUrl).into(pressedTarget);
    }


    private BitmapDrawable normalDrawble;
    private BitmapDrawable pressedDrawable;
    private final Target normalTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mLoadedCompeletedCount++;
            normalDrawble = new BitmapDrawable(bitmap);
            if (mLoadedCompeletedCount >= 2) {
                updateStateListDrawable();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    private final Target pressedTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mLoadedCompeletedCount++;
            pressedDrawable = new BitmapDrawable(bitmap);
            if (mLoadedCompeletedCount >= 2) {
                updateStateListDrawable();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    private void updateStateListDrawable() {
        mListDrawable.addState(new int[]{android.R.attr.state_selected}, pressedDrawable);
        mListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        mListDrawable.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        mListDrawable.addState(new int[]{}, normalDrawble);
        mTabIv.setImageDrawable(mListDrawable);
    }

    /**
     * 改变tabView当前的状态
     * TAB_STATUS_NORMAL  正常状态
     * TAB_STATUS_CLICK_SCROLL 点击回到顶部
     *
     * @param currentStatus
     */
    public void changeTabViewStatus(int currentStatus) {
        if (!mTabExtraEnable) return;
        //extraTabIv
        containerLayout.clearAnimation();
        extraTabIv.clearAnimation();

        if (TAB_STATUS_NORMAL == currentStatus) {
            containerLayout.animate().alpha(1.0f).setDuration(1000).start();
            extraTabIv.animate().alpha(0.0f).setDuration(1000).start();
        } else if (TAB_STATUS_CLICK_SCROLL == currentStatus) {
            containerLayout.animate().alpha(0.0f).setDuration(1000).start();
            extraTabIv.animate().alpha(1.0f).setDuration(1000).start();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mVisible)
            canvas.drawCircle(mTabIv.getRight(), 2.5f * mRadius, mRadius, mPaint);
    }

    public void setNoticeVisible(boolean visible) {
        mVisible = visible;
        invalidate();
    }
}
