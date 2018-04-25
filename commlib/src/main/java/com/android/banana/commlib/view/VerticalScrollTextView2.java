package com.android.banana.commlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.banana.commlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/18.
 */

public class VerticalScrollTextView2 extends ViewSwitcher implements ViewSwitcher.ViewFactory {

    private float mHeight;
    private Context mContext;
    private Animation mInAnim;
    private Animation mOutAnim;
    private List<IGeneralNotice> list = new ArrayList<>();
    private static final int SCROLL_SPEED = 3 * 1000;
    private boolean isFirst = true;
    private int index;
    private int color;

    public interface IGeneralNotice {
        CharSequence getText();
    }

    public void addMsg(List<? extends IGeneralNotice> notices) {
        if (notices == null || notices.size() == 0) return;
        for (IGeneralNotice notice : notices) {
            if (!list.contains(notice)) {
                list.add(notice);
            }
        }
        if (isFirst) {
            start();
            setVisibility(View.VISIBLE);
        }

        isFirst = false;
    }

    public VerticalScrollTextView2(Context context) {
        this(context, null);
    }

    public VerticalScrollTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.autoScroll);
        mHeight = a.getDimension(R.styleable.autoScroll_textSize, 12);
        color = a.getColor(R.styleable.autoScroll_tv_textColor, Color.BLACK);
        a.recycle();
        mContext = context;
        init();
    }

    private void init() {
        //千万别忘这句话
        setFactory(this);
        mOutAnim = createAnim(true);
        mInAnim = createAnim(false);
        mOutAnim.setFillAfter(false);
        setOutAnimation(mOutAnim);
        setInAnimation(mInAnim);

    }

    public void startAwayAnim() {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        setAnimation(animation);
        startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getChildAt(getDisplayedChild()).setVisibility(View.GONE);
                VerticalScrollTextView2.this.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //子view进入/退出的动画
    private AnimationSet createAnim(boolean outAnim) {
        AnimationSet set = new AnimationSet(false);
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, outAnim ? 0f : 1f, Animation.RELATIVE_TO_PARENT, outAnim ? -1f : 0f);
        AlphaAnimation aa = new AlphaAnimation(outAnim ? 1f : 0f, outAnim ? 0f : 1f);
        if (!outAnim) {
            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        set.setDuration(400);
        set.addAnimation(ta);
        set.addAnimation(aa);
        return set;
    }

    public void setData(CharSequence sequence) {
        TextView nextView = (TextView) getNextView();
        nextView.setText(sequence);
        if (!isVisibility()) {
            setVisibility(View.VISIBLE);
        }
        if (getAlpha() != 1.0f)
            setAlpha(1.0f);
        if (list.size() > 1) showNext();
    }

    public void start() {
        index = 0;
        this.removeCallbacks(runnable);
        postDelayed(runnable, 50);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (list == null || list.size() == 0) {
                startAwayAnim();
                return;
            }
            if (index >= list.size() - 1)
                index = 0;
            setData(list.get(index).getText());
            index++;
            postDelayed(this, SCROLL_SPEED);
        }
    };

    public boolean isVisibility() {
        return this.getVisibility() == View.VISIBLE ? true : false;
    }

    public void stopScroll() {
        index = 0;
        removeCallbacks(runnable);
    }

    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        t.setTextSize(mHeight);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setMaxLines(1);
        return t;
    }
}
