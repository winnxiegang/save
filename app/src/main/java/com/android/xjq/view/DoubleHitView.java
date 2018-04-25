package com.android.xjq.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.bean.live.GiftBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyi on 2017/4/10.
 */

public class DoubleHitView extends RelativeLayout {

    private ViewHolder[] mViewHolder = new ViewHolder[2];

    enum ShowStatusEnum {

        INTO,//横幅进入

        SHOWING,//正在显示数字

        LEAVE//横幅离开

    }

    private boolean init = false;

    private boolean detach = false;

    private GiftFinishLeaveListener listener;

    private List<GiftBean> mList = new ArrayList<>();


    public interface GiftFinishLeaveListener {
        public void onLeaveFinish();
    }

    public DoubleHitView(Context context) {
        super(context);
        init();
    }

    public DoubleHitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoubleHitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DoubleHitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }

    public void setGiftFinishListener(GiftFinishLeaveListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < 5; i++) {
            View view = createView();
            mViewHolder[i] = new ViewHolder(view);
            addView(view);
        }
    }

    private View createView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_heng_fu, null);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!init) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int measuredWidth = child.getMeasuredWidth();
                int measuredHeight = child.getMeasuredHeight();
                int left = 0;
                int top = i * measuredHeight;
                int right = left + measuredWidth;
                int bottom = top + measuredHeight;
                child.layout(left, top, right, bottom);
                mViewHolder[i].hengfuLayout.setTranslationX(-measuredWidth);
            }
            init = true;
        }

    }

    public void showDoubleHit(GiftBean bean) {
        if (isShow()) {
            mList.add(bean);
        } else {
            createAnimation(bean);
        }
       /* for(GiftBean bean1:mList){
            Log.e("id="+bean1.getId(),"count="+bean1.getAllDoubleHitCount());
        }*/
    }

    private void createAnimation(GiftBean bean) {
        for (ViewHolder holder : mViewHolder) {
            if (!holder.isShowAnimation) {
                holder.bean = bean;
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse("http://img-giftcore.huohongshe.net/images/gift/effect/publicChat/dabaojian.gif"))
                        .setAutoPlayAnimations(true)
                        .build();
                holder.draweeView.setController(draweeController);
                holder.countTv.setText(bean.getId() + "");
                createIntoAnimation(holder).start();
                break;
            }
        }
    }

    public boolean isShow() {

        for (ViewHolder holder : mViewHolder) {
            if (holder == null) {
                return true;
            }
            if (!holder.isShowAnimation) {
                return false;
            }
        }
        return true;
    }

    //进入动画
    private ObjectAnimator createIntoAnimation(final ViewHolder view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view.hengfuLayout, "translationX", -getMeasuredWidth(), 0);
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.isShowAnimation = true;

                view.showStatusEnum = ShowStatusEnum.INTO;

                view.bean.setShowing(true);

                setText(view);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (detach) {
                    return;
                }
                ObjectAnimator scaleXAnimator = createScaleXAnimator(view);
                ObjectAnimator scaleYAnimator = createScaleYAnimator(view);
                scaleXAnimator.start();
                scaleYAnimator.start();
            }


            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        view.animator = objectAnimator;

        return objectAnimator;
    }

    //离开动画
    private ObjectAnimator createLeaveAnimation(final ViewHolder view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view.hengfuLayout, "translationX", 0, getMeasuredWidth());
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (detach) {
                    return;
                }
                view.isShowAnimation = false;
                view.bean.setShowing(false);
                //显示下一条数据
                if (view.bean.getCurrentShowCount() != view.bean.getAllDoubleHitCount()) {
                    createAnimation(view.bean);
                } else {
                    if (mList.size() > 0) {
                        GiftBean giftBean = mList.remove(0);
                        createAnimation(giftBean);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        view.animator = objectAnimator;
        return objectAnimator;
    }

    //x轴放大动画
    private ObjectAnimator createScaleXAnimator(final ViewHolder view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view.numberTv, "scaleX", 1, 2);
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.showStatusEnum = ShowStatusEnum.SHOWING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (detach) {
                    return;
                }
                changeDoubleHitNumber(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        view.animator = objectAnimator;
        return objectAnimator;
    }

    //y轴放大动画
    private ObjectAnimator createScaleYAnimator(ViewHolder view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view.numberTv, "scaleY", 1, 2);
        objectAnimator.setDuration(500);
        return objectAnimator;
    }

    //x轴缩小动画
    private ObjectAnimator createZoomInXAnimator(final ViewHolder view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view.numberTv, "scaleX", 2, 1);
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.showStatusEnum = ShowStatusEnum.LEAVE;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (detach) {
                    return;
                }
                createLeaveAnimation(view).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        view.animator = objectAnimator;
        return objectAnimator;
    }

    //y轴缩小动画
    private ObjectAnimator createZoomInYAnimator(ViewHolder view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view.numberTv, "scaleY", 2, 1);
        objectAnimator.setDuration(500);
        view.animator = objectAnimator;
        return objectAnimator;
    }

    //改变礼物背景
    private void changeBackground() {

    }

    //显示礼物数,从小到大
    private void showGiftNumber() {

    }


    private int MAX_COUNT = 50;

    //显示连击数
    private void changeDoubleHitNumber(final ViewHolder viewHolder) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (detach) {
                    return;
                }
                if (viewHolder.bean.getCurrentShowCount() < viewHolder.bean.getAllDoubleHitCount()) {
                    int count = viewHolder.bean.getCurrentShowCount();
                    count++;
                    viewHolder.bean.setCurrentShowCount(count);
                    setText(viewHolder);
                    postDelayed(this, 40);
                } else {
                    createZoomInXAnimator(viewHolder).start();
                    createZoomInYAnimator(viewHolder).start();
                    return;
                }
            }
        }, 40);
    }

    private void setText(ViewHolder holder) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(String.valueOf(holder.bean.getCurrentShowCount()));
        char[] chars = String.valueOf(holder.bean.getCurrentShowCount()).toCharArray();
        int[] resId = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            resId[i] = getContext().getResources().getIdentifier("yellow_num_" + chars[i], "mipmap", getContext().getPackageName());
        }
        for (int i = 0; i < chars.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId[i]);
            ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
            ssb.setSpan(imageSpan, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.numberTv.setText(ssb);
    }

    private void setText(ViewHolder holder, int count) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(String.valueOf(count));
        char[] chars = String.valueOf(count).toCharArray();
        int[] resId = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            resId[i] = getContext().getResources().getIdentifier("yellow_num_" + chars[i], "mipmap", getContext().getPackageName());
        }
        for (int i = 0; i < chars.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId[i]);
            ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
            ssb.setSpan(imageSpan, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.numberTv.setText(ssb);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (ViewHolder holder : mViewHolder) {
            if (holder == null || holder.animator == null) {
                continue;
            }
            holder.animator.cancel();
        }
        detach = true;

    }

    public static class ViewHolder {

        public View rootView;

        public SimpleDraweeView draweeView;

        public TextView numberTv;

        public TextView countTv;

        public LinearLayout hengfuLayout;

        public boolean isShowAnimation;

        public ShowStatusEnum showStatusEnum;

        public GiftBean bean;

        public int count = 0;

        private ObjectAnimator animator;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.numberTv = (TextView) rootView.findViewById(R.id.numberTv);
            //  this.countTv = (TextView) rootView.findViewById(R.id.countTv);
            this.hengfuLayout = (LinearLayout) rootView.findViewById(R.id.hengfuLayout);
            //draweeView = (SimpleDraweeView) rootView.findViewById(R.id.draweeView);
        }

    }
}
