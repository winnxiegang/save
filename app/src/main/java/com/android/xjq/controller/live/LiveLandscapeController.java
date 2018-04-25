package com.android.xjq.controller.live;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.emoji.EmojUtils;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.LiveHeaderGuestAdapter;
import com.android.xjq.bean.live.AnchorUserInfoClientSimple;
import com.android.xjq.bean.live.ChannelUserBean;
import com.android.xjq.bean.live.EnterRoomBean;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.controller.GestureDetectorController;
import com.android.xjq.dialog.LiveGuestDialog;
import com.android.xjq.dialog.live.PersonalInfoDialog;
import com.android.xjq.dialog.popupwindow.DropHostPop;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.model.live.LiveCommentMessageTypeEnum;
import com.android.xjq.utils.GiftMsgUtils;
import com.android.xjq.utils.SocialTools;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by zhouyi on 2017/3/7.
 */

public class LiveLandscapeController extends BaseLiveController<LiveActivity> {

    private LandscapeViewHolder mViewHolder;
    private DanmakuContext mContext;
    private BaseDanmakuParser mParser;
    private boolean mInitDanmu = false;
    private boolean mPause = false;
    private GiftMsgUtils giftMsgUtils;
    private List<ChannelUserBean> mUserInfoList = new ArrayList<>();
    private List<AnchorUserInfoClientSimple> anchorUserInfoList = new ArrayList<>();
    //主主播信息
    public AnchorUserInfoClientSimple firstAnchorUserInfo;
    // 是否关闭视频
    private boolean isCloseVideo;
    // 手势识别,控制功能快捷点的显示与隐藏
    private GestureDetectorController gestureDetectorController;
    private LiveHeaderGuestAdapter headerGuestAdapter;

    public LiveLandscapeController(LiveActivity context) {
        super(context);
    }

    @Override
    public void init(View view) {
        if (mViewHolder == null) {
            mViewHolder = new LandscapeViewHolder(view);
            giftMsgUtils = new GiftMsgUtils();
            setListener();
        }
    }

    private void setListener() {
        //定时取礼物消息
        giftMsgUtils.setTimerSendReceiver(new GiftMsgUtils.TimerSendReceiver() {
            @Override
            public void pullMsg(LiveCommentBean bean) {
                createImageDownload(bean);
            }
        });

        mViewHolder.dropDownIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anchorUserInfoList != null && anchorUserInfoList.size() > 1) {
                    DropHostPop dropHostPop = new DropHostPop(context);
                    dropHostPop.showPop(anchorUserInfoList, mViewHolder.anchorInfoLayout);
                }
            }
        });

        mViewHolder.hostIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstAnchorUserInfo == null) return;
                new PersonalInfoDialog(context, firstAnchorUserInfo.userId, new PersonalInfoDialog.Callback() {
                    @Override
                    public void focusStatusChanged(String userId, boolean isFocus) {
                        firstAnchorUserInfo.focus = isFocus;
                        if (mViewHolder.focusTv != null)
                            mViewHolder.focusTv.setText(firstAnchorUserInfo.focus ? "取消" : "关注");
                        context.getLivePortraitController().firstAnchorUserInfo.focus = isFocus;
                    }
                }).show();
            }
        });

        mViewHolder.liveHostInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstAnchorUserInfo == null) return;
                if (firstAnchorUserInfo.focus) {
                    SocialTools.cancelAttention(firstAnchorUserInfo.userId, "FOLLOWCANCEL", onSocialCallback);
                } else {
                    SocialTools.payAttention(firstAnchorUserInfo.userId, onSocialCallback);
                }
            }
        });

        mViewHolder.danmuIv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mViewHolder.mDanmuView == null) return;
                if (isChecked) {
                    mViewHolder.mDanmuView.setVisibility(View.VISIBLE);
                    mViewHolder.mDanmuView.resume();
                } else {
                    mViewHolder.mDanmuView.pause();
                    mViewHolder.mDanmuView.removeAllDanmakus(true);
                    mViewHolder.mDanmuView.setVisibility(View.GONE);
                }
            }
        });

        mViewHolder.sendDanmuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showComment();
            }
        });

        gestureDetectorController = new GestureDetectorController(context, mViewHolder.functionView, mViewHolder.operateLayout);

        mViewHolder.floatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    gestureDetectorController.setMotionEventUp();
                }
                gestureDetectorController.setTouchEvent(event);

                return true;
            }
        });

        mViewHolder.landscapeBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.changeScreenOrientation();
            }
        });

        mViewHolder.giftIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showGift(true);
            }
        });

        mViewHolder.showPeopleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showAudience(true);
            }
        });

        mViewHolder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showMoreFunctionView();
            }
        });

        mViewHolder.guestIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveGuestDialog dialog = new LiveGuestDialog(context, context.getChannelId());
                dialog.show();
            }
        });

        mViewHolder.guestRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        headerGuestAdapter = new LiveHeaderGuestAdapter(context, mUserInfoList);
        mViewHolder.guestRecyclerView.setAdapter(headerGuestAdapter);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);
        mViewHolder.guestRecyclerView.setLayoutAnimation(controller);
        //着色
        Drawable mutate = ContextCompat.getDrawable(context, R.drawable.ic_close_x).mutate();
        DrawableCompat.setTint(mutate, Color.WHITE);
    }

    private void initDanmu() {
        mContext = DanmakuContext.create();
        if (mViewHolder.mDanmuView != null) {
            // 设置最大显示行数
            final HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
            maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
            // 设置是否禁止重叠
            HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
            overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_LR, false);
            mViewHolder.mDanmuView.enableDanmakuDrawingCache(true);
            mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_SHADOW, 5)//设置描边样式
//                    .alignBottom(true)
                    .preventOverlapping(overlappingEnablePair)
                    .setScrollSpeedFactor(1.2f)//设置弹幕速度
                    .setDuplicateMergingEnabled(false)//是否启用合并重复弹幕
                    .setCacheStuffer(new SpannedCacheStuffer(), new BaseCacheStuffer.Proxy() {

                        @Override
                        public void prepareDrawing(BaseDanmaku danmaku, boolean fromWorkerThread) {

                        }

                        @Override
                        public void releaseResource(BaseDanmaku danmaku) {
                            if (danmaku.text instanceof SpannableStringBuilder) {
                                SpannableStringBuilder ssb = (SpannableStringBuilder) danmaku.text;
                                ImageSpan[] spans = ssb.getSpans(0, ssb.length(), ImageSpan.class);
                                for (ImageSpan span : spans) {
                                    span.getDrawable().setCallback(null);
                                }
                                ssb.clearSpans();
                            }
                        }
                    });
            mParser = createParser();
            mViewHolder.mDanmuView.prepare(mParser, mContext);
        }
    }

    public void createImageDownload(final LiveCommentBean bean) {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                String sendName = bean.getSenderName();

                String totalCount = bean.getContent().getBody().getTotalCount();
                String giftConfigName = bean.getContent().getBody().getGiftConfigName();
                String text = sendName + "送出" + totalCount + "个" + giftConfigName;

                SpannableStringBuilder ssb = new SpannableStringBuilder(text);
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#f63f3f")), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.append("[图片]");
                BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
                bitmapDrawable.setBounds(0, 0,
                        (int) context.getResources().getDimension(R.dimen.dp28),
                        (int) context.getResources().getDimension(R.dimen.dp28));
                ImageSpan span = new ImageSpan(bitmapDrawable);

                ssb.setSpan(span, text.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                addDanmaku(ssb, getDanMuPriority(bean), false);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(context).load(bean.getContent().getBody().getGiftImageUrl()).into(target);
    }


    public void controlLiveFunctionHide(boolean isHideFunction) {
        mViewHolder.floatLayout.setVisibility(isHideFunction ? View.GONE : View.VISIBLE);
    }

    public void changePeopleCountTv(long userCount) {
        mViewHolder.peopleNumTv.setText(String.valueOf(userCount));
    }

    SocialTools.onSocialCallback onSocialCallback = new SocialTools.onSocialCallback() {
        @Override
        public void onResponse(JSONObject response, boolean success) {
            if (success) {
                firstAnchorUserInfo.focus = !firstAnchorUserInfo.focus;
                if (mViewHolder.focusTv != null)
                    mViewHolder.focusTv.setText(firstAnchorUserInfo.focus ? "取消" : "关注");
            } else {
                try {
                    if (context != null)
                        context.operateErrorResponseMessage(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void setAnchorView(EnterRoomBean enterRoomBean) {
        List<ChannelUserBean> channelAreaUserInfoList = enterRoomBean.getChannelAreaUserInfoList();
        if (channelAreaUserInfoList != null && channelAreaUserInfoList.size() != 0) {
            mUserInfoList.clear();
            mUserInfoList.addAll(channelAreaUserInfoList);
            headerGuestAdapter.notifyDataSetChanged();
            //mViewHolder.guestRecyclerView.scheduleLayoutAnimation();
        }
        AnchorUserInfoClientSimple firstAnchorUserInfo = enterRoomBean.getFirstAnchorUserInfo();
        anchorUserInfoList = enterRoomBean.getAnchorUserInfoList();
        this.firstAnchorUserInfo = firstAnchorUserInfo;
        if (this.firstAnchorUserInfo != null) {
            //主主播
            PicUtils.load(context.getApplicationContext(), mViewHolder.hostIv,
                    this.firstAnchorUserInfo.userLogoUrl, R.drawable.user_default_logo);
            mViewHolder.hostNameTv.setText(this.firstAnchorUserInfo.userName);
            mViewHolder.fansTv.setText(String.format(context.getString(R.string.fans_number),
                    "" + this.firstAnchorUserInfo.followMyUserCount));
        }
        //getMainController().showFastFunction();
    }

    public void hide() {
        //mViewHolder.mDanmuView.setVisibility(View.GONE);
        //mViewHolder.floatLayout.setVisibility(View.GONE);
        mViewHolder.liveLandscapeLayout.setVisibility(View.GONE);
        onPause();
    }

    public void show() {
        //mViewHolder.floatLayout.setVisibility(View.VISIBLE);
        //mViewHolder.mDanmuView.setVisibility(View.VISIBLE);
        mViewHolder.liveLandscapeLayout.setVisibility(View.VISIBLE);
        if (CurLiveInfo.getHostAvator() != null) {
            //Picasso.with(context).load(Uri.parse(CurLiveInfo.getHostAvator())).into(mViewHolder.portraitIv);
        }
        if (CurLiveInfo.getHostName() != null) {
            //mViewHolder.portraitName.setText(CurLiveInfo.getHostName());
        }
        if (!mInitDanmu) {
            mViewHolder.mDanmuView = (DanmakuView) mViewHolder.liveLandscapeLayout.findViewById(R.id.sv_danmaku);
            initDanmu();
            mInitDanmu = true;
        }
        onResume();
    }

    @Override
    public void onResume() {
        if (mViewHolder.mDanmuView != null && mViewHolder.mDanmuView.isPrepared() && mViewHolder.mDanmuView.isPaused()) {
            mViewHolder.mDanmuView.resume();
        }
        if (giftMsgUtils != null) {
            giftMsgUtils.onResume();
        }
    }

    public void onDestroy() {
        if (mViewHolder.mDanmuView != null) {
            // dont forget release!
            mViewHolder.mDanmuView.release();
            mViewHolder.mDanmuView = null;
        }
        super.onDestroy();
        gestureDetectorController.onDestroy();
        mViewHolder = null;
        if (giftMsgUtils != null) giftMsgUtils.onDestroy();
    }

    @Override
    public void onPause() {
        mPause = true;
        if (mViewHolder.mDanmuView != null && mViewHolder.mDanmuView.isPrepared()) {
            mViewHolder.mDanmuView.removeAllDanmakus(true);
            mViewHolder.mDanmuView.pause();
        }
        if (giftMsgUtils != null) giftMsgUtils.onPause();
    }

    private BaseDanmakuParser createParser() {
        return new BaseDanmakuParser() {
            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        };
    }

    private void addDanmaku(CharSequence text, byte priority, boolean isStroke) {
        if (mContext == null) {
            return;
        }
        //初始化弹幕类型
        BaseDanmaku danmaku;
        int textSize;
        if (isStroke) {
            danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
            if (mParser.getDisplayer() != null) {
                textSize = (int) (25f * (mParser.getDisplayer().getDensity() - 0.6f));
            } else {
                textSize = HhsUtils.spToPx(context, 16);
            }
        } else {
            danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SPECIAL);
            mContext.mDanmakuFactory.fillTranslationData(danmaku, LibAppUtil.getScreenWidth(context),
                    LibAppUtil.getScreenHeight(context) * 3 / 4, -LibAppUtil.getScreenWidth(context) / 2, LibAppUtil.getScreenHeight(context) * 3 / 4,
                    5000, 200, 1.0f, 1.0f);
            danmaku.duration = new Duration(5000);
            if (mParser.getDisplayer() != null) {
                textSize = (int) (18f * (mParser.getDisplayer().getDensity() - 0.6f));
            } else {
                textSize = HhsUtils.spToPx(context, 14);
            }
        }

        if (danmaku == null || mViewHolder.mDanmuView == null) {
            return;
        }
        if (mViewHolder.mDanmuView.isPaused() && !mPause) {//由于首次进来横屏弹幕为Pause状态，需要将其启动
            onResume();
        }

        danmaku.text = text;
        danmaku.tag = isStroke;
        danmaku.padding = 10;
        danmaku.priority = priority;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = true;
        danmaku.setTime(mViewHolder.mDanmuView.getCurrentTime() + 1200);
        danmaku.textSize = textSize;
        danmaku.textColor = Color.WHITE;
        if (isStroke) {
            danmaku.textShadowColor = Color.BLACK;
        } else {
            // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
            danmaku.textShadowColor = 0;
        }


        mViewHolder.mDanmuView.addDanmaku(danmaku);
    }

    /**
     * @param size 图标大小
     * @param text
     * @return
     */
    private SpannableStringBuilder formatText(int size, String text) {

        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        int i = text.indexOf("[");
        while (i != -1) {
            int j = text.indexOf("]", i + 1);
            if (j != -1) {
                String emojName = text.substring(i + 1, j);
                //获取资源id
                int resId = context.getResources().getIdentifier("emoj_" + emojName, "drawable", context.getPackageName());
                if (resId == 0) {
                    emojName = EmojUtils.getReplaceDrawableName(emojName);
                    resId = context.getResources().getIdentifier("emoj_" + emojName, "drawable", context.getPackageName());
                }
                if (resId != 0) {
                    ssb.setSpan(getEmojImageSpan(size, resId), i, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }
            i = text.indexOf("[", i + 1);
        }
        return ssb;
    }

    private ImageSpan getEmojImageSpan(int size, int resId) {

        Drawable d = context.getResources().getDrawable(resId);

        d.setBounds(0, 0, size, size);

        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);

        return span;

    }


    public void showMessage(LiveCommentBean bean) {
        if (bean.getContent().getText() == null) {
            switch (LiveCommentMessageTypeEnum.safeValueOf(bean.getType())) {
                case SPECIAL_EFFECT_ENTER_NOTICE://特殊人物进场
                    String senderName = bean.getSenderName();
                    SpannableStringBuilder ssb = new SpannableStringBuilder("欢迎 ");
                    ssb.append(SpannableStringHelper.changeTextColor(senderName, Color.parseColor("#fe6969")));
                    ssb.append(" 入场");
                    addDanmaku(ssb, (byte) 0, true);
                    break;
                case GIFTCORE_GIFT_GIVE_TEXT://发送礼物
                    giftMsgUtils.addMsg(bean);
                    break;
            }
        } else {
            addDanmaku(formatText((int) context.getResources().getDimension(R.dimen.sp14), bean.getContent().getText()),
                    getDanMuPriority(bean), true);
        }
    }

    private byte getDanMuPriority(LiveCommentBean bean) {
        if (bean.getSenderId() != null &&
                bean.getSenderId().equals(LoginInfoHelper.getInstance().getUserId())) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public void setView() {

    }

    public void pushStreamStatusChange(Boolean pushStreamStatus) {
      /*  if (pushStreamStatus) {

            mViewHolder.anchorOfflineLayout.setVisibility(View.GONE);
        } else {

            mViewHolder.anchorOfflineLayout.setVisibility(View.VISIBLE);
        }*/
    }


    static class LandscapeViewHolder {
        @BindView(R.id.landscapeBackIv)
        ImageView landscapeBackIv;
        @BindView(R.id.peopleNumTv)
        TextView peopleNumTv;
        @BindView(R.id.showPeopleLayout)
        LinearLayout showPeopleLayout;
        @BindView(R.id.definationTv)
        TextView definationTv;
        @BindView(R.id.moreIv)
        ImageView moreIv;
        @BindView(R.id.guestIv)
        ImageView guestIv;
        @BindView(R.id.sendDanmuIv)
        ImageView sendDanmuIv;
        @BindView(R.id.danmuIv)
        CheckBox danmuIv;
        @BindView(R.id.giftIv)
        ImageView giftIv;
        @BindView(R.id.liveLandscapeLayout)
        FrameLayout liveLandscapeLayout;
        DanmakuView mDanmuView;
        @BindView(R.id.bottomButtonLayout)
        View bottomButtonLayout;
        @BindView(R.id.functionView)
        RelativeLayout functionView;
        @BindView(R.id.floatLayout)
        FrameLayout floatLayout;
        @BindView(R.id.operateLayout)
        RelativeLayout operateLayout;
        //顶部主播以及嘉宾
        @BindView(R.id.guestRecyclerView)
        RecyclerView guestRecyclerView;
        @BindView(R.id.hostIv)
        ImageView hostIv;
        @BindView(R.id.hostNameTv)
        TextView hostNameTv;
        @BindView(R.id.anchorInfoLayout)
        RelativeLayout anchorInfoLayout;
        @BindView(R.id.fansTv)
        TextView fansTv;
        @BindView(R.id.dropDownIv)
        ImageView dropDownIv;
        @BindView(R.id.focusTv)
        TextView focusTv;
        @BindView(R.id.liveHostInfoLayout)
        LinearLayout liveHostInfoLayout;

        LandscapeViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
