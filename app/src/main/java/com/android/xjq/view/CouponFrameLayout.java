package com.android.xjq.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.jjx.sdk.utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.draw.DrawIssueEntity;
import com.android.xjq.bean.draw.IssueStatusType;
import com.android.xjq.bean.draw.LiveDrawInfoEntity;
import com.android.xjq.dialog.live.LimitHandSpeedDialog;
import com.android.xjq.model.draw.DrawActivityType;
import com.android.xjq.model.draw.DrawStatusType;

/**
 * Created by lingjiu on 2016/11/1 13:38.
 */
public class CouponFrameLayout extends FrameLayout {

    private View addView;

    private TextView mTextView;
    private ImageView mImageView;

    private boolean isAddView;
    private AnimationDrawable animationDrawable;
    private ViewGroup couponLayout;

    private boolean isShakeAnim = true;
    /**
     * 悬浮控件点击监听
     */
    private FloatingLayoutClickListener floatingLayoutClickListener;
    private CountdownTextView luckyDrawTv;
    private CountdownTextView limitSpeedTv;
    private AnimatorSet mShowDrawAnim, mHideDrawAnim;

    public void setFloatingLayoutClickListener(FloatingLayoutClickListener floatingLayoutClickListener) {
        this.floatingLayoutClickListener = floatingLayoutClickListener;
    }

    public interface FloatingLayoutClickListener {
        void onClickListener();
    }

    /**
     * 当前悬浮框的状态
     */
    public enum LayoutState {
        HIDE, SHOW
    }

    private LayoutState currentState = LayoutState.HIDE;

    private AnimatorSet mShowAnimatorSet, mHideAnimatorSet;
    //
    private Handler mHandler = new Handler();

    public LayoutState getCurrentState() {
        return currentState;
    }

    /**
     * @param context
     */
    public CouponFrameLayout(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CouponFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CouponFrameLayout(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        addView = LayoutInflater.from(context).inflate(R.layout.view_floating_coupon_layout,
                null);
        LayoutParams params = new LayoutParams(-2, -2);
        params.gravity = Gravity.RIGHT | Gravity.BOTTOM | Gravity.CENTER_VERTICAL;
        params.bottomMargin = (int) DimensionUtils.dpToPx(60, context);
        addView.setLayoutParams(params);

        mTextView = (TextView) addView.findViewById(R.id.tv);
        mImageView = (ImageView) addView.findViewById(R.id.iv);
        couponLayout = ((ViewGroup) addView.findViewById(R.id.couponLayout));
        couponLayout.setVisibility(View.GONE);
        animationDrawable = (AnimationDrawable) mImageView.getBackground();
        luckyDrawTv = (CountdownTextView) addView.findViewById(R.id.luckyDrawTv);
        limitSpeedTv = (CountdownTextView) addView.findViewById(R.id.limitSpeedTv);
        luckyDrawTv.setVisibility(View.GONE);
        limitSpeedTv.setVisibility(View.GONE);
        setAnimator();

        couponLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isShow = !isShow;
                if (floatingLayoutClickListener != null) {
                    floatingLayoutClickListener.onClickListener();
                }
            }
        });

        limitSpeedTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new LimitHandSpeedDialog(context, handSpeedIssueBean.id, handSpeedIssueBean.drawStatus.getName(), timeDiff).show();
            }
        });

        luckyDrawTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LiveActivity) context).showLuckyDrawView();
              /*  new LuckyDrawDialog(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, luckyDrawIssueBean.id,
                        luckyDrawIssueBean.gmtEndParticipate).show();*/
            }
        });
    }

    private boolean isShow = true;
    private long timeDiff;
    private DrawIssueEntity.IssueSimpleBean handSpeedIssueBean;
    private DrawIssueEntity.IssueSimpleBean luckyDrawIssueBean;

    /**
     * 设置红包个数
     *
     * @param num
     */
    public void setCouponCount(int num) {
        if (num <= 0) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(String.valueOf(num));
        }
    }


    private int getTextViewWidth() {
        return luckyDrawTv.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 设置是否跳动
     * @param isShakeAnim
     */
    public void setShakeAnim(boolean isShakeAnim) {
        this.isShakeAnim = isShakeAnim;
    }

    //当前抽奖view是否已经显示
    private boolean isShowLimitView;
    private boolean isShowLuckyView;

    /**
     * 设置开奖状态
     */
    public void setDrawViewStatus(DrawIssueEntity.IssueSimpleBean issueSimpleBean, String nowDate, boolean isShow) {
        LogUtils.e("issueSimple", "isShowDrawView = " + isShowLimitView + "  isShow = " + isShow);
        if (issueSimpleBean == null) {
            hideHandSpeedView();
            hideLuckyDrawView();
            return;
        }
        if (TextUtils.equals(DrawActivityType.HAND_SPEED, issueSimpleBean.activityCode)) {
            this.handSpeedIssueBean = issueSimpleBean;
            this.timeDiff = TimeUtils.dateSubDate(issueSimpleBean.gmtStartParticipate, nowDate);
            if (!isShow)
                issueSimpleBean.drawStatus.setName(DrawStatusType.FINISH);
            controlLimitViewShow(limitSpeedTv, issueSimpleBean.drawStatus.getName(), timeDiff);
        } else if (TextUtils.equals(DrawActivityType.LUCKY_DRAW, issueSimpleBean.activityCode)) {
            this.luckyDrawIssueBean = issueSimpleBean;
            if (!isShow)
                issueSimpleBean.status.setName(DrawStatusType.FINISH);
            long timeDiff = TimeUtils.dateSubDate(issueSimpleBean.gmtStartDraw, nowDate);
            controlLuckyDrawViewShow(luckyDrawTv, issueSimpleBean.awardType, issueSimpleBean.status.getName(), timeDiff);
        }
    }

    public void setIsShowPrizedView(boolean isShow) {
        if (isShowLuckyView) {
            luckyDrawTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        if (isShowLimitView)
            limitSpeedTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void hideHandSpeedView() {
        if (isShowLimitView) {
            hideDrawView(limitSpeedTv);
            isShowLimitView = false;
            limitSpeedTv.cancel();
        }
    }

    private void hideLuckyDrawView() {
        if (isShowLuckyView) {
            hideDrawView(luckyDrawTv);
            isShowLuckyView = false;
            limitSpeedTv.cancel();
        }
    }

    private void controlLuckyDrawViewShow(final CountdownTextView tv, String awardType, String drawStatus, long timeDiff) {
        if (TextUtils.equals(drawStatus, IssueStatusType.FINISH) && isShowLuckyView) {
            tv.setVisibility(View.VISIBLE);
            isShowLuckyView = false;
            hideDrawView(tv);
            tv.cancel();
        } else if (TextUtils.equals(drawStatus, IssueStatusType.DRAWING)) {
            tv.setVisibility(View.VISIBLE);
            isShowLuckyView = true;
            tv.cancel();
            tv.setText("抽奖进行中>");
            if (!isShowLuckyView) showDrawView(tv);
        } else if (TextUtils.equals(drawStatus, IssueStatusType.NORMAL) && !isShowLuckyView) {
            tv.setVisibility(View.VISIBLE);
            isShowLuckyView = true;
            showDrawView(tv);
            if (TextUtils.equals(awardType, LiveDrawInfoEntity.AwardType.BY_TIME)) {
                tv.start(timeDiff, getContext().getString(R.string.activity_count_down));
            } else {
                tv.setText("可参与");
            }
        }

        tv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
            }

            @Override
            public void countdownEnd() {
                tv.setText("抽奖进行中>");
                luckyDrawIssueBean.drawStatus.setName(DrawStatusType.WAIT_DRAW);
            }
        });
    }

    private void controlLimitViewShow(final CountdownTextView tv, String drawStatus, long timeDiff) {
        LogUtils.e("issueSimple", "isShowDrawView = " + isShowLimitView + "  drawStatus = " + drawStatus + "   timeDiff=" + timeDiff);
        if (TextUtils.equals(drawStatus, DrawStatusType.FINISH) && isShowLimitView) {
            tv.setVisibility(View.VISIBLE);
            isShowLimitView = false;
            hideDrawView(tv);
            tv.cancel();
        } else if (TextUtils.equals(drawStatus, DrawStatusType.WAIT_DRAW) && !isShowLimitView) {
            tv.setVisibility(View.VISIBLE);
            isShowLimitView = true;
            showDrawView(tv);
            tv.cancel();
            tv.setText("抽奖进行中>");
        } else if (TextUtils.equals(drawStatus, DrawStatusType.INIT) && !isShowLimitView) {
            tv.setVisibility(View.VISIBLE);
            isShowLimitView = true;
            showDrawView(tv);
            tv.start(timeDiff, getContext().getString(R.string.activity_count_down));
        }

        tv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                CouponFrameLayout.this.timeDiff = countdownTime;
            }

            @Override
            public void countdownEnd() {
                tv.setText("抽奖进行中>");
                handSpeedIssueBean.drawStatus.setName(DrawStatusType.WAIT_DRAW);
            }
        });
    }


    private void showDrawView(TextView textView) {
        mShowDrawAnim = new AnimatorSet();
        Animator[] showAnimator = new Animator[2];
        showAnimator[0] = ObjectAnimator.ofFloat(textView, "translationX",
                200, 0.0f);

        showAnimator[1] = ObjectAnimator.ofFloat(textView, "alpha",
                new float[]{0.5f, 1.0f});
        mShowDrawAnim.playTogether(showAnimator);
        mShowDrawAnim.setDuration(500);
        mShowDrawAnim.start();
    }

    private void hideDrawView(final TextView textView) {
        mHideDrawAnim = new AnimatorSet();
        Animator[] hideAnimator = new Animator[2];
        hideAnimator[0] = ObjectAnimator.ofFloat(textView, "translationX",
                0.0f, 200);

        hideAnimator[1] = ObjectAnimator.ofFloat(textView, "alpha",
                new float[]{1f, 0.5f});
        mHideDrawAnim.playTogether(hideAnimator);
        mHideDrawAnim.setDuration(500);
        mHideDrawAnim.start();
        mHideDrawAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setVisibility(View.GONE);
            }
        });
    }


    private void setAnimator() {
        mShowAnimatorSet = new AnimatorSet();
        Animator[] showAnimator = new Animator[2];
        showAnimator[0] = ObjectAnimator.ofFloat(couponLayout, "translationX",
                DimensionUtils.dpToPx(38, getContext()), 0.0f);

        showAnimator[1] = ObjectAnimator.ofFloat(couponLayout, "alpha",
                new float[]{0.5f, 1.0f});
        mShowAnimatorSet.playTogether(showAnimator);
        mShowAnimatorSet.setDuration(500);

        mHideAnimatorSet = new AnimatorSet();
        Animator[] hideAnimator = new Animator[2];
        hideAnimator[0] = ObjectAnimator.ofFloat(couponLayout, "translationX",
                0.0f, DimensionUtils.dpToPx(60, getContext()));
        hideAnimator[1] = ObjectAnimator.ofFloat(couponLayout, "alpha",
                new float[]{1.0f, 0.5F});
        mHideAnimatorSet.playTogether(hideAnimator);
        mHideAnimatorSet.setDuration(500);
    }

    //记录两次显示的时间,如果太短则不执行显示动画
    private long preShowTime;
    private long currentShowTime;

    public void showAnimator() {
        if (!isAddView) {
            return;
        }
        currentShowTime = SystemClock.uptimeMillis();
        //延时一秒执行
        if (LayoutState.HIDE.equals(currentState) && currentShowTime - preShowTime > 1500) {
            preShowTime = SystemClock.uptimeMillis();
            mHandler.postDelayed(showRunnable, 1000);
        }
    }

    private Runnable showRunnable = new Runnable() {
        @Override
        public void run() {
            couponLayout.setVisibility(View.VISIBLE);
            mShowAnimatorSet.start();
            mShowAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    currentState = LayoutState.SHOW;
                    if (isShakeAnim) {
                        animationDrawable.start();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addView(addView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeView(addView);
    }

    public void hideAnimator() {
        if (!isAddView) {
            return;
        }
        mHandler.removeCallbacks(showRunnable);
        mShowAnimatorSet.cancel();
        if (LayoutState.SHOW.equals(currentState)) {
            currentState = LayoutState.HIDE;
            mHideAnimatorSet.start();

            mHideAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    animationDrawable.selectDrawable(0);

                    animationDrawable.stop();
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    public void setCouponViewVisibility(int visibility) {
        addView.setVisibility(visibility);
    }

    public void sendMessage() {
        isAddView = true;

        if (animationDrawable.isRunning()) {

            animationDrawable.selectDrawable(0);

            animationDrawable.stop();
        }
        if (LayoutState.SHOW.equals(currentState) && isShakeAnim) {

            animationDrawable.start();
        } else {
            showAnimator();
        }

    }

}