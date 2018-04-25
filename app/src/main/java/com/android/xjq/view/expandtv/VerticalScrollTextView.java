package com.android.xjq.view.expandtv;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.xjq.R;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lingjiu on 2016/10/24 17:39.
 */
public class VerticalScrollTextView extends ViewSwitcher implements ViewSwitcher.ViewFactory {

    private Context mContext;
    private Animation mInAnim;
    private Animation mOutAnim;
    private List<LiveCommentBean> list = new ArrayList<>();
    //保留已经显示的近20条数据
    private List<LiveCommentBean> alreadyShowList = new ArrayList<>();
    private static final int ScrollSpeed = 800;

    //新加消息与最近30条做对比,如果是同一组连击并且练击数较小,则将其过滤掉
    public boolean addMsg(LiveCommentBean bean) {
        if (alreadyShowList != null && alreadyShowList.size() > 0) {
            LiveCommentBean.ContentBean.BodyBean body = bean.getContent().getBody();
            for (LiveCommentBean liveCommentBean : alreadyShowList) {
                LiveCommentBean.ContentBean.BodyBean body2 = liveCommentBean.getContent().getBody();
                if (body2.getGiftConfigId().equals(body.getGiftConfigId()) &&
                        body2.getDoubleHitGroupNo().equals(body.getDoubleHitGroupNo()) &&
                        Integer.valueOf(body2.getDoubleHit()) > Integer.valueOf(body.getDoubleHit())) {
                    return false;
                }
            }
        }
        list.add(bean);

        if (alreadyShowList.size() > 30) {
            alreadyShowList.remove(0);
        }
        alreadyShowList.add(bean);
        return true;
    }

    public VerticalScrollTextView(Context context) {
        this(context, null);

    }

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
       /* TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.autoScroll);
        mHeight = a.getDimension(R.styleable.autoScroll_textSize, 14);
        a.recycle();*/
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
                VerticalScrollTextView.this.setVisibility(View.GONE);
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
                    list.remove(0);
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

    public void setData(LiveCommentBean liveCommentBean) {
        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
        View nextView = getNextView();
        ((TextView) nextView.findViewById(R.id.nameTv)).setText(liveCommentBean.getSenderName());
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("送出");
        ssb.append(SpannableStringHelper.changeTextColor(body.getTotalCount() + "个", Color.parseColor("#fe6969")));
        ssb.append(body.getGiftConfigName());
        ((TextView) nextView.findViewById(R.id.descTv)).setText(ssb);
        ImageView iv = (ImageView) nextView.findViewById(R.id.iv);
        TextView doubleHitTv = (TextView) nextView.findViewById(R.id.doubleHitTv);
        if (!TextUtils.isEmpty(body.getGiftImageUrl()))
            Picasso.with(mContext).load(body.getGiftImageUrl()).into(iv);
        if (Integer.valueOf(body.getDoubleHit()) > 1) {
            doubleHitTv.setVisibility(View.VISIBLE);
            doubleHitTv.setText(body.getDoubleHit() + "连击");
        } else {
            doubleHitTv.setVisibility(View.GONE);
        }

        nextView.setVisibility(View.VISIBLE);
        if (!isVisibility()) {
            setVisibility(View.VISIBLE);
        }
        if (getAlpha() != 1.0f)
            setAlpha(1.0f);
        showNext();
    }

    public void start() {
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
            setData(list.get(0));
            postDelayed(this, ScrollSpeed);
        }
    };

    public boolean isVisibility() {
        return this.getVisibility() == View.VISIBLE ? true : false;
    }

    public void stopScroll() {
        removeCallbacks(runnable);
    }

    @Override
    public View makeView() {
        View view = View.inflate(getContext(), R.layout.dynamic_item_gift, null);
        return view;
    }


}
