package com.android.xjq.controller.live;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.banana.commlib.view.PageIndicatorView;
import com.android.banana.view.BadgeView;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.LiveCommentAdapter2;
import com.android.xjq.adapter.live.LiveCommentMultiType;
import com.android.xjq.bean.LivePkBean;
import com.android.xjq.bean.gamePK.PkGameBoarInfoBean;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.main.PrizeCoreInfoBean;
import com.android.xjq.model.PrizeItemType;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.xjq.view.expandtv.MarqueeTextView;
import com.android.xjq.view.expandtv.VerticalScrollTextView;
import com.android.xjq.view.recyclerview.PKRecyclerView;
import com.android.xjq.view.recyclerview.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouyi on 2017/3/7.
 */

public class LiveMainController extends BaseLiveController<LiveActivity> {
    private ViewHolder mViewHolder;
    private ArrayList<LiveCommentBean> mList = new ArrayList<>();
    private LiveCommentAdapter2 mAdapter;
    private boolean mKeyBoardIsShow;
    private boolean isVisBottom = true;

    public LiveMainController(LiveActivity context) {
        super(context);
    }

    @Override
    public void init(View view) {
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder(view);
            setListener();
        }

        mViewHolder.countdownTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                if (mViewHolder == null) {
                    return;
                }
                if (countdownTime < 5 * 60 * 1000) {
                    mViewHolder.countdownTv.setVisibility(View.VISIBLE);
                    mViewHolder.countdownTv.setText(stringFormat(countdownTime));
                }
            }

            @Override
            public void countdownEnd() {
                if (mViewHolder == null) {
                    return;
                }
                //context.getLiveHttpRequestController().getPrizeCoreInfo();
                mViewHolder.countdownTv.setVisibility(View.GONE);
            }
        });

        mViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LiveCommentAdapter2(context, mList, 0, new LiveCommentMultiType());
        mViewHolder.recyclerView.setAdapter(mAdapter);
        mViewHolder.recyclerView.setFocusable(false);
        mViewHolder.recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {

            @Override
            public void isVisBottom(boolean isVisibility) {
                isVisBottom = isVisibility;
                if (isVisibility) {
                    mViewHolder.backBottomTv.setVisibility(View.GONE);
                }
            }
        });
    }

    private CharSequence stringFormat(long time) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(SpannableStringHelper.changeTextColor("距离开奖还有:", Color.WHITE));
        ssb.append(SpannableStringHelper.changeTextColor(TimeUtils.timeFormat(time), context.getResources().getColor(R.color.light_yellow)));
        return ssb;
    }

    private void setListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewHolder.recordScreenIv.setVisibility(View.VISIBLE);
        }

        mViewHolder.recordScreenIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getLivePortraitController().recordViewIsVisibility()) return;
                context.applyScreenRecordPermission();
            }
        });

        mViewHolder.pkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showPKView();
            }
        });

        mViewHolder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showMoreFunctionView();
            }
        });

        mViewHolder.inputCommentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showComment();
            }
        });
        mViewHolder.giftIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showGift(false);
//                context.getGiftController().addGift();
            }
        });
        mViewHolder.backBottomTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisBottom = true;
                mViewHolder.backBottomTv.setVisibility(View.GONE);
                mViewHolder.recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

        mViewHolder.closePKIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.closePKIv.setVisibility(View.GONE);
                mViewHolder.pkRecyclerView.setVisibility(View.GONE);
            }
        });

    }

    public void addMessage(LiveCommentBean bean) {
        if (bean.isShowFloatingLayer()) {
            showGiftFloatingLayer(bean);
            return;
        }
        //如果礼物同一组连击里面,大的连击消息先到了，那么小的消息就废弃掉不要显示
        if (bean.isRuleEffect() && mAdapter.judgeIsFilterSameGroupGift(bean)) return;
        if (mList.size() >= 100) {
            mAdapter.notifyItemRemoved(0);
            mList.remove(0);
        }
        mList.add(bean);
        if (!context.isLandscape() && !mKeyBoardIsShow) {
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
            LogUtils.e(TAG, "isVisBottom=" + isVisBottom + "");
            if (isVisBottom) {
                mViewHolder.recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                mViewHolder.backBottomTv.setVisibility(View.GONE);
            } else if ((mViewHolder.recyclerView.getLayoutManager()).getItemCount() >
                    (mViewHolder.recyclerView.getLayoutManager()).getChildCount()) {
                //当前底部不可见,消息大于一屏,有新消息来,则显示回到底部文字
                mViewHolder.backBottomTv.setVisibility(View.VISIBLE);
            }
//            notifyData();
        }
    }

    private void showGiftFloatingLayer(final LiveCommentBean bean) {
        boolean isAddSuccess = mViewHolder.verticalTv.addMsg(bean);
        if (isAddSuccess && !mViewHolder.verticalTv.isVisibility()) {
            mViewHolder.verticalTv.start();
        }
    }

    public void showPkData(LivePkBean livePkBean) {
        if (mViewHolder.matchThemeLayout == null) return;
        if (livePkBean != null && livePkBean.pkGameBoard != null) {
            mViewHolder.matchThemeLayout.setVisibility(View.VISIBLE);
            List<PkGameBoarInfoBean> pkGameBoarInfoBeans = new ArrayList<>();
            pkGameBoarInfoBeans.add(livePkBean.pkGameBoard);
            mViewHolder.pkRecyclerView.setPkData(pkGameBoarInfoBeans, livePkBean.multipleList);
          /* FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mViewHolder.recyclerView.getLayoutParams();
            params.topMargin = LibAppUtil.dip2px(context, 45);*/
        } else {
            mViewHolder.matchThemeLayout.setVisibility(View.GONE);
           /* FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mViewHolder.recyclerView.getLayoutParams();
            params.topMargin = 0;*/
        }
    }

    /**
     * 切换为竖屏
     */
    public void changePortrait() {
        notifyData();
    }

    private void notifyData() {
        if (mList == null || mList.size() == 0) {
            return;
        }
        mAdapter.notifyDataSetChanged();
        mViewHolder.recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
//        mViewHolder.recyclerView.setSelection(mList.size() - 1);
    }


    @Override
    public void setView() {
        //context.getHttpController().getPrizeCoreInfo();
    }

    @Override
    public void onHiddenChanged(boolean isHide) {
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewHolder.countdownTv.cancel();
        mViewHolder.marqueeTv.stopScroll();
        mViewHolder.verticalTv.stopScroll();
        mViewHolder = null;
    }

    /**
     * 计算时间倒计时
     */
    public void prizeInfoQuery(PrizeCoreInfoBean prizeCoreInfoBean) {
        String currentTime = TimeUtils.getCurrentTime();
        long countDownTime = 0;
        List<PrizeCoreInfoBean.IssueListBean> issueList = prizeCoreInfoBean.getIssueList();
        if (issueList != null && issueList.size() > 0) {
            for (PrizeCoreInfoBean.IssueListBean issueListBean : issueList) {
                if (TimeUtils.dateSubDate(issueListBean.getGmtStartPrize(), currentTime) > 0) {

                    long diff = TimeUtils.dateSubDate(issueListBean.getGmtStartPrize(), currentTime);
                    if (countDownTime == 0 || diff > 0 && countDownTime - diff > 0) {
                        countDownTime = diff;
                    }
                } else if (TimeUtils.dateSubDate(issueListBean.getGmtStopPrize(), currentTime) > 0) {
                    long diff = TimeUtils.dateSubDate(issueListBean.getGmtStopPrize(), currentTime);
                    if (countDownTime == 0 || countDownTime - diff > 0 && diff > 0) {
                        countDownTime = diff;
                    }
                }
            }
        }
        mViewHolder.countdownTv.start(countDownTime);
    }

    public void showFastFunction() {
        mViewHolder.pkView.setAlpha(1);
        mViewHolder.moreIv.setAlpha(1f);
        TranslateAnimation transAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -3, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        transAnim.setDuration(2 * 1000);
        mViewHolder.moreIv.startAnimation(transAnim);
        mViewHolder.pkView.startAnimation(transAnim);
        if (mViewHolder.recordScreenIv.getVisibility() == View.VISIBLE) {
            mViewHolder.recordScreenIv.setAlpha(1f);
            mViewHolder.recordScreenIv.startAnimation(transAnim);
        }
    }

    /**
     * 爆奖池获奖名单
     */
    public void prizeCoreNoticeShow(LiveCommentBean liveCommentBean) {
        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
        if (body != null) {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append("恭喜");
            ssb.append(SpannableStringHelper.changeTextColor(body.getLoginName(), context.getResources().getColor(R.color.light_yellow)));
            ssb.append("获得");
            String prizeType = PrizeItemType.safeValueOf(body.getPrizeItemType()).getMessage();
            ssb.append(SpannableStringHelper.changeTextColor(prizeType, context.getResources().getColor(R.color.light_yellow)));
            ssb.append("(" + body.getPrizeAmount() + "金币)!赠送礼物就有机会获得超值大奖!");
            if (mViewHolder.marqueeTv.isVisibility()) {
                mViewHolder.marqueeTv.addMessage(ssb);
                return;
            }
            mViewHolder.marqueeTv.setVisibility(View.VISIBLE);
            mViewHolder.marqueeTv.setText(ssb);
            mViewHolder.marqueeTv.startScrollShow();
        }
    }

    /**
     * 爆奖池获奖名单
     */
    public void prizeCoreNoticeShow(CharSequence prizeRecordInfo) {
        if (mViewHolder.marqueeTv.isVisibility()) {
            mViewHolder.marqueeTv.addMessage(prizeRecordInfo);
            return;
        }
        mViewHolder.marqueeTv.setVisibility(View.VISIBLE);
        mViewHolder.marqueeTv.setText(prizeRecordInfo);
        mViewHolder.marqueeTv.startScrollShow();
    }

    public void onKeyBoardStateChange(boolean keyBoardIsShow) {
        mKeyBoardIsShow = keyBoardIsShow;
        //如果键盘收起
        if (!context.isLandscape()) {
            if (!mKeyBoardIsShow) {
                notifyData();
                mViewHolder.recyclerView.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.recyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void showPkView() {
        mViewHolder.closePKIv.setVisibility(View.VISIBLE);
        mViewHolder.pkRecyclerView.showView();
    }

    public void setPkRedView(int processingPkGameBoardCount) {
        mViewHolder.badgeView.setBadgeNum(processingPkGameBoardCount);
    }


    static class ViewHolder {
        @BindView(R.id.moreIv)
        ImageView moreIv;
        @BindView(R.id.countdownTv)
        CountdownTextView countdownTv;
        @BindView(R.id.marqueeTv)
        MarqueeTextView marqueeTv;
        @BindView(R.id.inputCommentTv)
        TextView inputCommentTv;
        @BindView(R.id.giftIv)
        ImageView giftIv;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.verticalTv)
        VerticalScrollTextView verticalTv;
        @BindView(R.id.backBottomTv)
        TextView backBottomTv;
        @BindView(R.id.pkRecyclerView)
        PKRecyclerView pkRecyclerView;
        @BindView(R.id.matchThemeLayout)
        FrameLayout matchThemeLayout;
        @BindView(R.id.indicatorView)
        PageIndicatorView indicatorView;
        @BindView(R.id.recordScreenIv)
        ImageView recordScreenIv;
        @BindView(R.id.pkIv)
        ImageView pkIv;
        @BindView(R.id.badgeView)
        BadgeView badgeView;
        @BindView(R.id.closePKIv)
        ImageView closePKIv;
        @BindView(R.id.pkView)
        FrameLayout pkView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
