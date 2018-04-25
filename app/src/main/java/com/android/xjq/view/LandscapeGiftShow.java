package com.android.xjq.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.live.DoubleHitShowBean;
import com.android.xjq.bean.live.GiftViewShowBean;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.utils.WebpUtils;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouyi on 2017/4/13.
 */

public class LandscapeGiftShow extends RelativeLayout {

    private GiftShowViewHolder[] mGiftHolder = new GiftShowViewHolder[2];

    private DoubleShowViewHolder[] mDoubleHolder = new DoubleShowViewHolder[2];

    private List<GiftViewShowBean> mGiftList = new ArrayList<>();

    private List<DoubleHitShowBean> mDoubleList = new ArrayList<>();

    private List<LiveCommentBean> mImperialList = new ArrayList<>();

    private long bannerIntervalSeconds;

    private PathAnimView sendGiftSuccessAnimView;

    //连击显示条数(最少一条,最多两条);横幅显示时间
    public void setBaseConfig(int showCount, String bannerIntervalSeconds) {
        if (showCount == 1) {
            mDoubleHolder[1] = null;
        }
        this.bannerIntervalSeconds = Long.valueOf(bannerIntervalSeconds);
    }

    public enum DoubleHitShowStatusEnum {
        WAIT,

        SHOWING,

        FINISH
    }

    private boolean mDetach;

    private Handler mHandler = new Handler();

    //初始化
    private boolean mInit = true;

    private boolean mPortrait = true;

    private Typeface mRoomNumberFont = null;

    private Typeface mGiftNumberFont = null;

    private boolean mDoubleHitHide = false;

    //是否显示键盘，如果显示键盘，需要将连击的界面向上移动。
    private boolean mShowKeyBoard = false;


    public LandscapeGiftShow(Context context) {
        super(context);
        init();
    }

    public LandscapeGiftShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LandscapeGiftShow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LandscapeGiftShow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mRoomNumberFont = Typeface.createFromAsset(getContext().getAssets(), "hhs_room_num.ttf");

        mGiftNumberFont = Typeface.createFromAsset(getContext().getAssets(), "hhs_gift_num.ttf");
        for (int i = 0; i < mGiftHolder.length; i++) {
            View giftShow = createGiftShow();
            mGiftHolder[i] = new GiftShowViewHolder(giftShow);
            addView(giftShow);
        }
        for (int i = 0; i < mDoubleHolder.length; i++) {
            View doubleShow = createDoubleShow();
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            RelativeLayout.LayoutParams llps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            addView(doubleShow);
            mDoubleHolder[i] = new DoubleShowViewHolder(doubleShow);
            mDoubleHolder[i].rootView.setLayoutParams(llps);
        }

        sendGiftSuccessAnimView = new PathAnimView(getContext());
        sendGiftSuccessAnimView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        addView(sendGiftSuccessAnimView);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //if (sendGiftSuccessAnimView != null) removeView(sendGiftSuccessAnimView);
        mHandler.removeCallbacksAndMessages(null);
        mDetach = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void changeOrientation(boolean portrait) {

        if (sendGiftSuccessAnimView != null) {
            sendGiftSuccessAnimView.changeOrientation(portrait);
        }

        //如果当前是横屏且当前连击数不显示，当用户切为竖屏时，则需要显示连击数
        if (!portrait && mDoubleHitHide) {
            for (DoubleShowViewHolder holder : mDoubleHolder) {
                holder.rootView.setVisibility(View.VISIBLE);
            }
        } else if (portrait && mDoubleHitHide) {
            for (DoubleShowViewHolder holder : mDoubleHolder) {
                holder.rootView.setVisibility(View.GONE);
            }
        }

        for (DoubleShowViewHolder aMDoubleHolder : mDoubleHolder) {
            int width = 0;
            if (portrait) {
                width = LayoutParams.MATCH_PARENT;
            } else {
                width = aMDoubleHolder.doubleHitLayout.getMeasuredWidth();
            }
            LayoutParams llps = new LayoutParams(width, LayoutParams.WRAP_CONTENT);
            aMDoubleHolder.rootView.setLayoutParams(llps);
        }
        mPortrait = portrait;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mPortrait) {
            layoutPortrait();
        } else {
            layoutLandscape();
        }
        setViewGone();
    }

    private void layoutPortrait() {

        float videoHeight = getContext().getResources().getDimension(R.dimen.live_video_height);

        float giftBannerHeight = getContext().getResources().getDimension(R.dimen.gift_double_height);

        float firstHeight = videoHeight - giftBannerHeight;

        for (int i = 0; i < mGiftHolder.length; i++) {
            int measureHeight = mGiftHolder[i].rootView.getMeasuredHeight();
            int top = (int) (firstHeight + measureHeight * i);
            mGiftHolder[i].rootView.layout(0, top, getMeasuredWidth(), top + measureHeight);
        }

        if (mShowKeyBoard) {
            for (int i = 0; i < mDoubleHolder.length; i++) {
//                mDoubleHolder[i].rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int measuredWidth = mDoubleHolder[i].rootView.getMeasuredWidth();
                int measureHeight = mDoubleHolder[i].rootView.getMeasuredHeight();
                int top = (int) (/*firstHeight +*/ measureHeight * i);
                int left = getMeasuredWidth() - mDoubleHolder[i].doubleHitLayout.getMeasuredWidth();
//                mDoubleHolder[i].rootView.layout(0, top, measuredWidth, top + measureHeight);
                LogUtils.e("layoutPortrait", "measuredWidth=" + measuredWidth + " measureHeight=" + measureHeight + " getMeasuredWidth()=" + getMeasuredWidth());
                mDoubleHolder[i].rootView.layout(left, top, getMeasuredWidth() + left, top + measureHeight);
            }
        } else {
            for (int i = 0; i < mDoubleHolder.length; i++) {
                int measuredWidth = mDoubleHolder[i].rootView.getMeasuredWidth();
                int measureHeight = mDoubleHolder[i].rootView.getMeasuredHeight();
                int top = getMeasuredHeight() / 2 + measureHeight * i;
                mDoubleHolder[i].rootView.layout(0, top, measuredWidth, top + measureHeight);
            }
        }

    }

    public void showKeyBoard(boolean showKeyBoard) {
        mShowKeyBoard = showKeyBoard;
        requestLayout();
    }

    private void layoutLandscape() {
        layoutGiftShow();
        layoutDoubleShow();
    }

    //初始化横幅不显示
    private void setViewGone() {
        if (mInit) {
            for (DoubleShowViewHolder aMDoubleHolder : mDoubleHolder) {
                aMDoubleHolder.doubleHitLayout.setVisibility(View.GONE);
            }
            for (GiftShowViewHolder aMGiftHolder : mGiftHolder) {
                aMGiftHolder.bgLayout.setVisibility(View.GONE);
            }
            mInit = false;
        }
    }

    private void layoutGiftShow() {
        for (int i = 0; i < mGiftHolder.length; i++) {
            int measureHeight = mGiftHolder[i].rootView.getMeasuredHeight();
            int top = measureHeight * i;
            mGiftHolder[i].rootView.layout(0, top, getMeasuredWidth(), top + measureHeight);
        }
    }

    private void layoutDoubleShow() {
        for (int i = 0; i < mDoubleHolder.length; i++) {
            RelativeLayout.LayoutParams llps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDoubleHolder[i].rootView.setLayoutParams(llps);
            int measuredWidth = mDoubleHolder[i].rootView.getMeasuredWidth();
            int measureHeight = mDoubleHolder[i].rootView.getMeasuredHeight();
            int top = getMeasuredHeight() / 2 + measureHeight * i;
            mDoubleHolder[i].rootView.layout(0, top, measuredWidth, top + measureHeight);
            LogUtils.e("layoutDoubleShow", "measuredWidth = " + measuredWidth + "measureHeight=" + measureHeight);
        }
    }

    private View createDoubleShow() {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_double_show, null);
    }

    private View createGiftShow() {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_gift_show, null);
    }

    private View createImperialEdictShow() {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_imperial_edict_show, null);
    }

    public void showGift(GiftViewShowBean bean) {
        if (isHasSpaceToShowGift()) {
            createGiftShowAnimation(bean);
        } else {
            mGiftList.add(bean);
        }
    }

    public void showImperialEdict(LiveCommentBean liveCommentBean) {
       /* if (mImperialEdictHolder.showing) {
            mImperialList.add(liveCommentBean);
        } else {

        }*/

        View imperialEdictShow = createImperialEdictShow();
        int margin = DimensionUtils.getScreenHeight(getContext()) / 5;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = margin;
        imperialEdictShow.setLayoutParams(params);
        imperialEdictShow.setVisibility(View.GONE);
        addView(imperialEdictShow);

        //圣旨这边不用排队,来一条出来一条,距底边1/5个屏幕高度
        createImperialShowAnimation(imperialEdictShow, liveCommentBean);
    }

    private void createImperialShowAnimation(final View imperialEdictShow, LiveCommentBean liveCommentBean) {
        TextView messageTv = (TextView) imperialEdictShow.findViewById(R.id.messageTv);
        final int textColor = ContextCompat.getColor(getContext(), R.color.height_yellow);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString ss = SpannableStringHelper.changeTextColor(HhsUtils.splitString(liveCommentBean.getSenderName(), 10), textColor);
        ssb.append(ss);
        ssb.append("在");
        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
        final long secondsTime = (long) (Double.valueOf(body.getSecondsTime()) * 1000);
        final String channelAreaId = body.getChannelAreaId();
        SpannableString channelName = SpannableStringHelper.changeTextColor(HhsUtils.splitString(body.getChannelAreaName(), 10), textColor);
        ssb.append(channelName);
        ssb.append("节目中降下一道圣旨,");
        SpannableString clickSpan = new SpannableString("快来接旨领红包!");
        messageTv.setLinksClickable(true);
        clickSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (getContext() instanceof LiveActivity) {
                    ((LiveActivity) getContext()).switchChannel(channelAreaId);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文本的颜色
                ds.setColor(textColor);
                //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
                ds.setUnderlineText(true);
            }
        }, 0, clickSpan.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ssb.append(clickSpan);
        messageTv.setText(ssb);
        messageTv.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        //mImperialEdictHolder.messageTv.setHighlightColor(Color.TRANSPARENT);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imperialEdictShow,
                "translationX", getMeasuredWidth(), 0);
        objectAnimator.setDuration(2 * 1000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                createImperialLeaveAnimation(imperialEdictShow, secondsTime);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                imperialEdictShow.setVisibility(View.VISIBLE);
            }
        });
        objectAnimator.start();
    }

    private void createImperialLeaveAnimation(final View imperialEdictShow, long secondsTime) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imperialEdictShow,
                        "translationX", 0, -getMeasuredWidth());
                objectAnimator.setDuration(2 * 1000);
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeView(imperialEdictShow);
                    }
                });
                objectAnimator.start();
            }
        }, secondsTime);

    }


    /**
     * 是否有空间去显示礼物
     *
     * @return
     */
    private boolean isHasSpaceToShowGift() {
        for (GiftShowViewHolder aMGiftHolder : mGiftHolder) {
            if (!aMGiftHolder.showing) {
                return true;
            }
        }
        return false;
    }

    private void createGiftShowAnimation(GiftViewShowBean bean) {
        for (GiftShowViewHolder holder : mGiftHolder) {
            if (!holder.showing) {
                holder.bean = bean;
                LiveCommentBean.ContentBean.BodyBean body = bean.getLiveCommentBean().getContent().getBody();
                LogUtils.e("createDoubleHitAnimation", "body.isUseModelResource()=" + body.isUseModelResource() + "  " + body.getGiftImageUrl());
                if (body.isUseModelResource()) {
                    holder.giftIv.setVisibility(View.VISIBLE);
                    Picasso.with(getContext().getApplicationContext()).load(body.getGiftImageUrl()).into(holder.giftIv);
                } else {
                    holder.giftIv.setVisibility(View.GONE);
                }
                if (bean.getImageUrl() != null) {
                    holder.bgIv.setImageBitmap(getBitmap(holder.bean.isImageFromAsserts(), bean.getImageUrl().get(0)));
                }
                holder.bgIv.setAdjustViewBounds(true);
                holder.messageTv.setText(bean.getLeftMessage());
                holder.currentGiftCount = 0;
                holder.currentImageCount = 0;
//                holder.leave = false;
                holder.messageTv.setTextColor(Color.parseColor(bean.getTextColor()));
                showNumberText(holder, 1);
                createGiftShowIntoAnimation(holder).start();
                break;
            }
        }
    }

   /* private Bitmap getBitmap(String url) {
        if (url == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            InputStream open = getContext().getAssets().open(url);
            bitmap = BitmapFactory.decodeStream(open);
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }*/

    private Bitmap getBitmap(boolean isImageFromAsserts, String url) {
        if (url == null) {
            return null;
        }
        Bitmap bitmap = null;
        InputStream open = null;
        try {
            if (isImageFromAsserts) {
                open = getContext().getAssets().open(url);
            } else {
                open = new FileInputStream(new File(url));
            }
            byte[] data = WebpUtils.streamToBytes(open);
            bitmap = WebpUtils.webpToBitmap(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    //礼物进入动画
    private ObjectAnimator createGiftShowIntoAnimation(final GiftShowViewHolder view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view.bgLayout, "translationX", getMeasuredWidth(), 0);
        objectAnimator.setDuration(1000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.showing = true;
                view.bgLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mDetach) {
                    return;
                }
                view.starShowTime = System.currentTimeMillis();
                changeGiftShowBackground(view);
                showGiftNumber(view);
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

    //礼物离开动画
    private ObjectAnimator createGiftShowLeaveAnimation(final GiftShowViewHolder view) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view.bgLayout, "translationX", 0, -getMeasuredWidth());
        objectAnimator.setDuration(1000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mDetach) {
                    return;
                }
                view.bgLayout.setVisibility(View.GONE);
                view.showing = false;
                view.currentImageCount = 0;
                //显示下一条数据
                if (mGiftList.size() > 0) {
                    GiftViewShowBean remove = mGiftList.remove(0);
                    createGiftShowAnimation(remove);
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

    //改变礼物背景
    private void changeGiftShowBackground(final GiftShowViewHolder view) {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (view.bean.getImageUrl() != null && view.currentImageCount < view.bean.getImageUrl().size() /*&& !view.leave*/) {
                    String url = view.bean.getImageUrl().get(view.currentImageCount);
                    view.bgIv.setImageBitmap(getBitmap(view.bean.isImageFromAsserts(), url));
                    view.bgIv.setAdjustViewBounds(true);
                    view.currentImageCount++;
                    postDelayed(this, 20);
                } else {
                    giftShowLeaveAnimation(view);
                    return;
                }
            }
        };

        mHandler.postDelayed(runnable, 20);

    }

    //礼物显示离开动画
    private void giftShowLeaveAnimation(final GiftShowViewHolder view) {
        long changeBgTime = System.currentTimeMillis() - view.starShowTime;
        long stayShowTime = 100;
        if (bannerIntervalSeconds != 0 && bannerIntervalSeconds * 1000 > changeBgTime) {
            stayShowTime = bannerIntervalSeconds * 1000 - changeBgTime;
        }
        LogUtils.e("onFinish", "changeBgTime = " + changeBgTime + "stayShowTime = " + stayShowTime);
        if (view.currentGiftCount == view.bean.getTotalGiftCount() &&
                (view.bean.getImageUrl() == null || view.currentImageCount == view.bean.getImageUrl().size())) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    createGiftShowLeaveAnimation(view).start();
                }
            }, stayShowTime);
        }
    }


    //显示礼物数,从小到大
    private void showGiftNumber(final GiftShowViewHolder holder) {
        NumberCalculate numberCalculate = new NumberCalculate();
        final long startTime = System.currentTimeMillis();
        numberCalculate.startCalculate(mHandler, holder.bean.getTotalGiftCount(), new NumberCalculate.OnCalculateListener() {
            @Override
            public void onCalculate(String number) {
                showNumberText(holder, Integer.parseInt(number));
            }

            @Override
            public void onFinish(String number) {
                long endTime = System.currentTimeMillis();
                LogUtils.e("onFinish", "  =" + (endTime - startTime));
                showNumberText(holder, Integer.parseInt(number));
                holder.currentGiftCount = Integer.valueOf(number);
                giftShowLeaveAnimation(holder);
            }
        });
    }

    private void showNumberText(final GiftShowViewHolder holder, int currentGiftCount) {

        if (holder.bean.getRoomNumber() != null) {
            setPlatformGiftTextShow(holder, currentGiftCount);
        } else {
            setChannelGiftTextShow(holder, currentGiftCount);
        }

    }

    private void setPlatformGiftTextShow(final GiftShowViewHolder holder, int currentGiftCount) {

        GiftViewShowBean bean = holder.bean;

        String sendName = bean.getSendName().length() > 4 ? bean.getSendName().substring(0, 4) + "..." : bean.getSendName();
        String roomNumber = "在 " + bean.getRoomNumber();
        String receiveName = " 直播间送出";
        String giftName = "个" + HhsUtils.splitString(bean.getGiftName(), 8);

        String left = sendName + roomNumber + receiveName;

        SpannableStringBuilder ssb = new SpannableStringBuilder(sendName + roomNumber + receiveName + currentGiftCount + giftName);

        //设置房间号Span
        CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan("", mRoomNumberFont, bean.getRoomNumberColor());
        ssb.setSpan(customTypefaceSpan, sendName.length() + 1, sendName.length() + roomNumber.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置活动的数字Span
       /* char[] chars = String.valueOf(currentGiftCount).toCharArray();
        int[] resId = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            resId[i] = getContext().getResources().getIdentifier("yellow_num_" + chars[i], "mipmap", getContext().getPackageName());
        }
        for (int i = 0; i < chars.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId[i]);
            ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
            ssb.setSpan(imageSpan, i + left.length(), left.length() + i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*/

       /* ssb.setSpan(new CustomColorSpan(Color.parseColor(holder.messageParamBean.getGiftNumberColor())),
                left.length(),
                left.length() + String.valueOf(currentGiftCount).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        //设置礼物数
        ssb.setSpan(new CustomTypefaceSpan("", mGiftNumberFont, bean.getRoomNumberColor()),
                left.length(),
                left.length() + String.valueOf(currentGiftCount).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.messageTv.setText(ssb);

    }

    private void setChannelGiftTextShow(final GiftShowViewHolder holder, int currentGiftCount) {
        String left = holder.bean.getSendName().length() > 8 ? holder.bean.getSendName().substring(0, 8) + "...送出" : holder.bean.getSendName() + "送出";
        String giftName = "个" + holder.bean.getGiftName();
        SpannableStringBuilder ssb = new SpannableStringBuilder(left + currentGiftCount + giftName);
        /*char[] chars = String.valueOf(currentGiftCount).toCharArray();
        int[] resId = new int[chars.length];

        for (int i = 0; i < chars.length; i++) {
            resId[i] = getContext().getResources().getIdentifier("yellow_num_" + chars[i], "mipmap", getContext().getPackageName());
        }

        for (int i = 0; i < chars.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId[i]);
            ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
            ssb.setSpan(imageSpan, i + left.length(), left.length() + i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*/
        ssb.setSpan(new CustomColorSpan(Color.parseColor(holder.bean.getGiftNumberColor())),
                left.length(),
                left.length() + String.valueOf(currentGiftCount).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.messageTv.setText(ssb);
    }

    /**
     * 隐藏连击数显示,连击数只显示在公屏，
     * 如果用户滑到其他界面，则需要隐藏
     */
    public void hideDoubleHitShow() {
        mDoubleHitHide = true;
        for (DoubleShowViewHolder holder : mDoubleHolder) {
            holder.rootView.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏连击数显示,连击数只显示在公屏，
     * 如果用户滑到其他界面，则需要隐藏
     */
    public void showDoubleHitShow() {
        mDoubleHitHide = false;
        for (DoubleShowViewHolder holder : mDoubleHolder) {
            holder.rootView.setVisibility(View.VISIBLE);
        }
    }


    public void showDoubleHit(DoubleHitShowBean bean) {
        if (isHasSpaceToShowDoubleHit()) {
            createDoubleHitAnimation(bean);
        } else {
            mDoubleList.add(bean);
        }
    }

    /**
     * 礼物赠送成功动画
     */
    public void showSendGiftSuccessAnim(String imgUrl, int number) {
        if (sendGiftSuccessAnimView == null) {
           /* sendGiftSuccessAnimView = new PathAnimView(getContext());
            addView(sendGiftSuccessAnimView);*/
            sendGiftSuccessAnimView.sendGiftSuccess(imgUrl, number);
        }

    }

    private void createDoubleHitAnimation(DoubleHitShowBean bean) {
        for (DoubleShowViewHolder holder : mDoubleHolder) {
            if (!holder.showing) {
                holder.bean = bean;
                LiveCommentBean.ContentBean.BodyBean body = bean.getLiveCommentBean().getContent().getBody();
                if (body.isUseModelResource()) {
                    holder.giftIv.setVisibility(View.VISIBLE);
                    Picasso.with(getContext().getApplicationContext()).load(body.getGiftImageUrl()).into(holder.giftIv);
                } else {
                    holder.giftIv.setVisibility(View.GONE);
                }
                holder.bgIv.setImageBitmap(getBitmap(bean.isImageFromAssert(), bean.getImageUrl()));
                holder.bgIv.setAdjustViewBounds(true);
                holder.userNameTv.setTextColor(Color.parseColor(bean.getGiftNormalColor()));
                holder.messageTv.setTextColor(Color.parseColor(bean.getGiftNormalColor()));
                String sendName = HhsUtils.splitString(bean.getLiveCommentBean().getSenderName(), 12);
                holder.userNameTv.setText(sendName);
                setDoubleHitGiftNumber(holder, bean);
                createDoubleHitIntoAnimation(holder);
                break;
            }
        }
    }

    private void setDoubleHitGiftNumber(DoubleShowViewHolder holder, DoubleHitShowBean bean) {

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        String leftMessage = "送出";
        ssb.append(leftMessage);
        SpannableString giftCount = SpannableStringHelper.changeTextColor(String.valueOf(bean.getTotalGiftCount()),
                Color.parseColor(bean.getGiftCountColor()));
        ssb.append(giftCount);

        String rightMessage = "个" + bean.getGiftName();
        ssb.append(rightMessage);

        holder.messageTv.setText(ssb);

    }

    /**
     * 是否有空间去显示连击
     *
     * @return
     */
    private boolean isHasSpaceToShowDoubleHit() {
        for (DoubleShowViewHolder holder : mDoubleHolder) {
            if (!holder.showing) {
                return true;
            }
        }
        return false;
    }

    //连击进入动画
    private void createDoubleHitIntoAnimation(final DoubleShowViewHolder holder) {
        final ObjectAnimator objectAnimator;
        if (mShowKeyBoard && mPortrait) {
            objectAnimator = ObjectAnimator.ofFloat(holder.doubleHitLayout, "translationX", getMeasuredWidth(), 0);
        } else {
            objectAnimator = ObjectAnimator.ofFloat(holder.doubleHitLayout, "translationX", -holder.doubleHitLayout.getMeasuredWidth(), 0);
        }

        LogUtils.e("createDoubleHitIntoAnimation", "" + holder.rootView.getMeasuredWidth() + "  " + getMeasuredWidth() + "  " + holder.doubleHitLayout.getMeasuredWidth());
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.bean.setShowStatusEnum(DoubleHitShowStatusEnum.SHOWING);
                holder.showing = true;
                holder.bean.setShowing(true);
                holder.doubleHitLayout.setVisibility(View.VISIBLE);
                setDoubleHitText(holder.doubleHitTv, holder.bean.getCurrentShowCount());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                changeDoubleHitNumber(holder);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.start();
    }

    //连击离开动画
    private void createDoubleHitLeaveAnimation(final DoubleShowViewHolder holder) {

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(holder.doubleHitLayout, "translationY", 0, -getResources().getDimension(R.dimen.dp60));

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(holder.doubleHitLayout, "alpha", 1, 0);

        AnimatorSet animSet = new AnimatorSet();

        animSet.playTogether(objectAnimator1, objectAnimator2);

        animSet.setDuration(500);

        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                holder.doubleHitLayout.setVisibility(View.GONE);
                holder.bean.setShowStatusEnum(DoubleHitShowStatusEnum.FINISH);
                holder.showing = false;
                holder.bean.setShowing(false);
                if (mShowKeyBoard) {
                    holder.doubleHitLayout.setTranslationX(getMeasuredWidth());
                } else {
                    holder.doubleHitLayout.setTranslationX(-holder.doubleHitLayout.getMeasuredWidth());
                }
                holder.doubleHitLayout.setTranslationY(0);
                holder.doubleHitLayout.setAlpha(1.0f);
                holder.bgIv.setImageBitmap(null);
                if (holder.bean.getAllDoubleHitCount() <= holder.bean.getCurrentShowCount()) {
                    if (mDoubleList.size() > 0) {
                        DoubleHitShowBean remove = mDoubleList.remove(0);
                        createDoubleHitAnimation(remove);
                    }
                } else {
                    createDoubleHitAnimation(holder.bean);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animSet.start();

    }

    private void changeDoubleHitNumber(final DoubleShowViewHolder holder) {
        createDoubleHitScaleAnimation(holder);
    }

    private void createDoubleHitScaleAnimation(DoubleShowViewHolder holder) {

        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.doubleHitTv, "scaleX", 2, 1);

        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.doubleHitTv, "scaleY", 2, 1);

        AnimatorSet set = new AnimatorSet();

        set.playTogether(scaleX, scaleY);

        set.addListener(createDoubleHitScaleListener(holder));

        set.setDuration(400);

        set.start();

    }

    private Animator.AnimatorListener createDoubleHitScaleListener(final DoubleShowViewHolder holder) {

        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (holder.bean.getCurrentShowCount() < holder.bean.getAllDoubleHitCount()) {
                    int currentShowCount = holder.bean.getCurrentShowCount();
                    currentShowCount++;
                    holder.bean.setCurrentShowCount(currentShowCount);
                    //getCurrentShowAllDoubleHit不等于0说明在活动内,如果当前区间内连击完成之后就消失掉
                    if (holder.bean.getCurrentShowAllDoubleHit() != 0 && currentShowCount > holder.bean.getCurrentShowAllDoubleHit()) {
                        holder.bean.setCurrentShowCount(currentShowCount > holder.bean.getNextShowCount() ? currentShowCount : holder.bean.getNextShowCount());
                        holder.bean.setCurrentShowAllDoubleHit(holder.bean.getAllDoubleHitCount());
                        createDoubleHitLeaveAnimation(holder);
                        return;
                    }
                    setDoubleHitText(holder.doubleHitTv, holder.bean.getCurrentShowCount());
                    createDoubleHitScaleAnimation(holder);
                } else {
                    holder.bean.setCurrentShowCount(holder.bean.getCurrentShowCount() + 1);
                    createDoubleHitLeaveAnimation(holder);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }


    private void setDoubleHitText(TextView tv, int count) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(String.valueOf(count));
        char[] chars = String.valueOf(count).toCharArray();
        int[] resId = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            resId[i] = getResources().getIdentifier("double_hit_" + chars[i], "mipmap", getContext().getPackageName());
        }
        for (int i = 0; i < chars.length; i++) {
           /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId[i]);
            ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
            ssb.setSpan(imageSpan, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId[i]);
            Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            int width = (int) (mDrawable.getIntrinsicWidth() * 1.2);
            int height = (int) (mDrawable.getIntrinsicHeight() * 1.2);
            mDrawable.setBounds(0, 0, width, height);
            ImageSpan imageSpan = new ImageSpan(mDrawable);
            ssb.setSpan(imageSpan, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        tv.setText(ssb);
    }

    public static class GiftShowViewHolder {

        public View rootView;
        public TextView messageTv;
        public FrameLayout bgLayout;
        public ImageView bgIv;
        public boolean showing = false;
        public ObjectAnimator animator;
        public GiftViewShowBean bean;
        public int currentImageCount = 0;
        public int currentGiftCount = 0;
        //开始切换背景的时间
        public long starShowTime;
        public ImageView giftIv;

        //当前是否已经结束显示了，如果已经结束显示，就不去变换背景图
//        public boolean leave = false;

        public GiftShowViewHolder(View rootView) {
            this.rootView = rootView;
            this.messageTv = (TextView) rootView.findViewById(R.id.messageTv);
            this.bgLayout = (FrameLayout) rootView.findViewById(R.id.bgLayout);
            this.bgIv = (ImageView) rootView.findViewById(R.id.backgroundIv);
            this.giftIv = ((ImageView) rootView.findViewById(R.id.giftIv));
        }

    }

    class DoubleShowViewHolder {
        public View rootView;
        public TextView userNameTv;
        public TextView messageTv;
        public TextView doubleHitTv;
        public RelativeLayout doubleHitLayout;
        public ImageView bgIv;
        public ImageView giftIv;
        public boolean showing;

        public DoubleHitShowBean bean;

        public DoubleShowViewHolder(View rootView) {
            this.rootView = rootView;
            this.userNameTv = (TextView) rootView.findViewById(R.id.userNameTv);
            this.messageTv = (TextView) rootView.findViewById(R.id.messageTv);
            this.doubleHitTv = (TextView) rootView.findViewById(R.id.doubleHitTv);
            this.doubleHitLayout = (RelativeLayout) rootView.findViewById(R.id.doubleHitLayout);
            this.bgIv = (ImageView) rootView.findViewById(R.id.bgIv);
            this.giftIv = ((ImageView) rootView.findViewById(R.id.giftIv));
        }
    }

    class ImperialEdictViewHolder {
        public View rootView;
        public TextView messageTv;
        public FrameLayout bgLayout;
        public boolean showing;

        public LiveCommentBean liveCommentBean;

        public ImperialEdictViewHolder(View rootView) {
            this.rootView = rootView;
            this.bgLayout = (FrameLayout) rootView.findViewById(R.id.bgLayout);
            this.messageTv = (TextView) rootView.findViewById(R.id.messageTv);
        }
    }


    //负责改变礼物数变换
    static class NumberCalculate {

        int i;

        int addValue;

        long delayedTime;

        public interface OnCalculateListener {

            public void onCalculate(String number);

            public void onFinish(String number);

        }

        public void startCalculate(final Handler handler, final int count, final OnCalculateListener listener) {

            if (count > 100) {
                int j = Math.round((long) count / 100f);
                if (j == 0) {
                    addValue = 1;
                } else if (j % 10 == 0) {
                    addValue = j + 1;
                } else {
                    addValue = j;
                }
                addValue *= 3;
                delayedTime = 20;
            } else {
                addValue = 1;
                if (count > 50) {
                    addValue = 2;
                }
                delayedTime = 40;
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (i >= count) {
                        i = count;
                        listener.onFinish(String.valueOf(count));
                        return;
                    }

                    i += addValue;

                    listener.onCalculate(String.valueOf(i));

                    handler.postDelayed(this, delayedTime);
                }
            }, delayedTime);
        }

    }
}
