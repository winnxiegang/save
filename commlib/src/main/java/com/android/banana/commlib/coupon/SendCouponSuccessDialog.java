package com.android.banana.commlib.coupon;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.banana.commlib.R;
import com.android.library.Utils.LogUtils;


/**
 * Created by zhouyi on 2016/6/25 14:50.
 */
public class SendCouponSuccessDialog {

    private Context context;

    PopupWindow mPopupWindow;

    View view;
    private ImageView imageView;

    private int duration;

    public SendCouponSuccessDialog(Context context) {
        this.context = context;
    }

    public void show(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initPopupWindow();
                start();
                mPopupWindow.showAsDropDown(view);
            }
        }, 285);
    }

    private void initPopupWindow() {
        view = getPopupWindowView();

        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDismissListener != null) {

                    onDismissListener.onDismissListener();
                }
            }
        });

    }

    private View getPopupWindowView() {

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_send_coupon_success, null);

        imageView = (ImageView) view.findViewById(R.id.imageView);

        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.anim_send_coupon_success));

        return view;
    }

    public AnimatorSet createAnim(boolean outAnim) {
        //设置X轴平移量
        int endX = context.getResources().getDisplayMetrics().widthPixels - imageView.getMeasuredWidth();
        //设置Y轴平移量
        int endY = (context.getResources().getDisplayMetrics().heightPixels - imageView.getMeasuredHeight()) / 2;

        LogUtils.e("createAnim", "widthPixels =" + context.getResources().getDisplayMetrics().widthPixels +
                " height= " + context.getResources().getDisplayMetrics().heightPixels +
                "  endY=" + imageView.getMeasuredWidth() +
                "  " + imageView.getMeasuredHeight());

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageView, "scaleX",
                outAnim ? 1f : 0.2f, outAnim ? 0.2f : 1f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageView, "scaleY",
                outAnim ? 1.0f : 0.2f, outAnim ? 0.2f : 1.0f);

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(imageView, "Alpha",
                outAnim ? 1.0f : 0.0f, outAnim ? 0.0f : 1.0f);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(imageView, "translationX",
                outAnim ? -endX / 2 : imageView.getMeasuredWidth(), outAnim ? imageView.getMeasuredWidth() : -endX / 2);

        ObjectAnimator anim5 = ObjectAnimator.ofFloat(imageView, "translationY",
                outAnim ? -endY : imageView.getMeasuredHeight() / 2, outAnim ? imageView.getMeasuredHeight() / 2 : -endY);

        AnimatorSet set = new AnimatorSet();
//        set.setInterpolator(new LinearInterpolator());

        set.setDuration(800);

        set.playTogether(anim1, anim2, anim3, anim4, anim5);

        return set;
    }

    private void start() {

        imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        AnimatorSet inAnim = createAnim(false);

        final AnimatorSet outAnim = createAnim(true);

        final AnimationDrawable ad = (AnimationDrawable) imageView.getDrawable();

        ad.selectDrawable(0);

        duration = 0;

        for (int i = 0; i < ad.getNumberOfFrames(); i++) {

            duration += ad.getDuration(i);
        }

        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (outAnim.equals(animation)) {
                    imageView.setImageDrawable(null);
                    mPopupWindow.dismiss();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            outAnim.start();
                        }
                    }, duration);
                    ad.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        inAnim.addListener(animatorListener);
        outAnim.addListener(animatorListener);
        inAnim.start();
    }

    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismissListener();
    }

}
