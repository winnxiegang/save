package com.android.banana.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;


/**
 * Created by mrs on 2017/4/11.
 */

public class AnimationUtils {
    public static void animateScale(final View view, float scalex, float scaley, int delay, boolean visible) {
        animate(view, 0, 0, scalex, scaley, 500, delay, visible?0:8);
    }

    public static void animateScale(final View view, float scalex, float scaley, int duration, int delay, boolean visible) {
        animate(view, 0, 0, scalex, scaley, duration, delay, visible?0:8);
    }

    public static void animateTranslate(final View view, float x, float y, int delay, final boolean visible) {
        animate(view, x, y, 1, 1, 500, delay, visible?0:8);
    }

    public static void animateTranslate(final View view, float x, float y, int duration, int delay, final boolean visible) {
        animate(view, x, y, 1, 1, duration, delay, visible?0:8);
    }


    public static void animate(final View view, float x, float y, float scalex, float scaley, int duration, int delay, final int  visiblity) {
        view.animate().translationX(x).translationY(y).scaleX(scalex).scaleY(scaley).setDuration(duration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(visiblity);
            }
        }).setStartDelay(delay).start();
    }

//    /**
//     * 竞猜直播页面的弹幕动画
//     *
//     * @param animView
//     */
//    public static void startAnimationLeft2Right(TextView animView) {
//        animView.setX(LibAppUtil.dip2px(animView.getContext(), 18));
//        TranslateAnimation transAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,
//                -0.5f,
//                TranslateAnimation.RELATIVE_TO_SELF,
//                0f,
//                TranslateAnimation.RELATIVE_TO_SELF,
//                0f,
//                TranslateAnimation.RELATIVE_TO_SELF,
//                0f);
//        transAnimation.setInterpolator(new FastOutSlowInInterpolator());
//        transAnimation.setDuration(1500);
//        transAnimation.setFillAfter(true);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
//        alphaAnimation.setDuration(1500);
//        alphaAnimation.setFillAfter(true);
//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(transAnimation);
//        set.addAnimation(alphaAnimation);
//        set.setFillAfter(true);
//        animView.startAnimation(set);
//        animView.setBackgroundDrawable(ContextCompat.getDrawable(animView.getContext(), R.drawable.icon_gsdanmu_left));
//        animView.setVisibility(View.VISIBLE);
//    }

//    /**
//     * 竞猜直播页面的弹幕动画
//     *
//     * @param animView
//     */
//    public static void startAnimationRight2Left(final TextView animView) {
//        animView.setVisibility(View.VISIBLE);
//        int w = animView.getWidth();
//        animView.setX(LibAppUtil.getScreenWidth(animView.getContext()) - LibAppUtil.dip2px(animView.getContext(), 207));
//        TranslateAnimation transAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,
//                0.5f,
//                TranslateAnimation.RELATIVE_TO_SELF,
//                0f,
//                TranslateAnimation.RELATIVE_TO_SELF,
//                0,
//                TranslateAnimation.RELATIVE_TO_SELF,
//                0);
//        transAnimation.setInterpolator(new FastOutSlowInInterpolator());
//        transAnimation.setDuration(1500);
//        transAnimation.setFillAfter(true);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
//        alphaAnimation.setDuration(1500);
//        alphaAnimation.setFillAfter(true);
//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(transAnimation);
//        set.addAnimation(alphaAnimation);
//        set.setFillAfter(true);
//        animView.startAnimation(set);
//        animView.setBackgroundDrawable(ContextCompat.getDrawable(animView.getContext(), R.drawable.icon_gsdanmu_right));
//
//    }

    public static void doLiveScoreBtAnimation(View animView) {
        Animation animation = animView.getAnimation();
        if (animation != null)
            return;
        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(800);
        TranslateAnimation downTranslate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0.15f);
        downTranslate.setRepeatMode(Animation.REVERSE);
        downTranslate.setRepeatCount(Animation.INFINITE);
        downTranslate.setDuration(800);


        AnimationSet set = new AnimationSet(true);
        set.addAnimation(rotate);
        set.addAnimation(downTranslate);
        set.setInterpolator(new LinearInterpolator());
        set.setFillAfter(true);
        set.setRepeatCount(Animation.INFINITE);
        animView.startAnimation(set);
    }


    public static void doLiveScoreFtAnimation(View animView) {
        Animation animation = animView.getAnimation();
        if (animation != null)
            return;
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(800);
        animView.startAnimation(rotate);

    }

    public static long clearAnimation(View animView) {
        long startTime = 0;
        Animation animation = animView.getAnimation();
        if (animation != null) {
            startTime = animation.getStartTime();
            animation.cancel();
            animView.clearAnimation();
        }
        return startTime;
    }
}
