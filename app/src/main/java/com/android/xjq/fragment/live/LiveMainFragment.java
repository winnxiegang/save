package com.android.xjq.fragment.live;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.banana.commlib.view.PageIndicatorView;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.banana.groupchat.view.baselist.TopicRecyclerView2;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.LiveCommentAdapter2;
import com.android.xjq.adapter.live.LiveCommentMultiType;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.main.PrizeCoreInfoBean;
import com.android.xjq.fragment.BaseFragment;
import com.android.xjq.model.PrizeItemType;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.xjq.view.expandtv.MarqueeTextView;
import com.android.xjq.view.expandtv.VerticalScrollTextView;
import com.android.xjq.view.recyclerview.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lingjiu on 2018/2/25.
 */

public class LiveMainFragment extends BaseFragment {
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
    @BindView(R.id.topicRecyclerView)
    TopicRecyclerView2 topicRecyclerView;
    @BindView(R.id.matchThemeLayout)
    FrameLayout matchThemeLayout;
    @BindView(R.id.indicatorView)
    PageIndicatorView indicatorView;

    private ArrayList<LiveCommentBean> mList = new ArrayList<>();
    private LiveCommentAdapter2 mAdapter;
    private boolean mKeyBoardIsShow;
    private boolean isVisBottom = true;

    @Override
    protected void initData() {
        setListener();
        countdownTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                if (countdownTime < 5 * 60 * 1000) {
                    countdownTv.setVisibility(View.VISIBLE);
                    countdownTv.setText(stringFormat(countdownTime));
                }
            }
            @Override
            public void countdownEnd() {
                //((LiveActivity) activity).getLiveHttpRequestController().getPrizeCoreInfo();
                countdownTv.setVisibility(View.GONE);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new LiveCommentAdapter2(activity, mList, 0, new LiveCommentMultiType());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setFocusable(false);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {

            @Override
            public void isVisBottom(boolean isVisibility) {
                isVisBottom = isVisibility;
                if (isVisibility) {
                    backBottomTv.setVisibility(View.GONE);
                }
            }
        });
        topicRecyclerView.attachToIndicatorView(indicatorView);
    }

    private CharSequence stringFormat(long time) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(SpannableStringHelper.changeTextColor("距离开奖还有:", Color.WHITE));
        ssb.append(SpannableStringHelper.changeTextColor(TimeUtils.timeFormat(time), getContext().getResources().getColor(R.color.light_yellow)));
        return ssb;
    }

    private void setListener() {
        moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new LiveMoreFunctionDialog(activity).show();
            }
        });

        inputCommentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LiveActivity) activity).showComment();
            }
        });

        giftIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LiveActivity) activity).showGift(false);
//                context.getGiftController().addGift();
            }
        });
        backBottomTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisBottom = true;
                backBottomTv.setVisibility(View.GONE);
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
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
        if (!((LiveActivity) activity).isLandscape() && !mKeyBoardIsShow) {
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
            LogUtils.e(TAG, "isVisBottom=" + isVisBottom + "");
            if (isVisBottom) {
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                backBottomTv.setVisibility(View.GONE);
            } else if ((recyclerView.getLayoutManager()).getItemCount() >
                    (recyclerView.getLayoutManager()).getChildCount()) {
                //当前底部不可见,消息大于一屏,有新消息来,则显示回到底部文字
                backBottomTv.setVisibility(View.VISIBLE);
            }
//            notifyData();
        }
    }

    private void showGiftFloatingLayer(final LiveCommentBean bean) {
        boolean isAddSuccess = verticalTv.addMsg(bean);
        if (isAddSuccess && !verticalTv.isVisibility()) {
            verticalTv.start();
        }
    }

    public void showRaceTheme(GroupChatTopic1 groupChatTopic1) {
        if (matchThemeLayout == null) return;
        if (groupChatTopic1 != null && groupChatTopic1.gameBoardList.size() > 0) {
            matchThemeLayout.setVisibility(View.VISIBLE);
            topicRecyclerView.setTopic(groupChatTopic1);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) recyclerView.getLayoutParams();
            params.topMargin = LibAppUtil.dip2px(activity, 45);
        } else {
            matchThemeLayout.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) recyclerView.getLayoutParams();
            params.topMargin = 0;
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
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
//        mViewHolder.recyclerView.setSelection(mList.size() - 1);
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
        countdownTv.start(countDownTime);
    }

    /**
     * 爆奖池获奖名单
     */
    public void prizeCoreNoticeShow(LiveCommentBean liveCommentBean) {
        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
        if (body != null) {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append("恭喜");
            ssb.append(SpannableStringHelper.changeTextColor(body.getLoginName(), getContext().getResources().getColor(R.color.light_yellow)));
            ssb.append("获得");
            String prizeType = PrizeItemType.safeValueOf(body.getPrizeItemType()).getMessage();
            ssb.append(SpannableStringHelper.changeTextColor(prizeType, getContext().getResources().getColor(R.color.light_yellow)));
            ssb.append("(" + body.getPrizeAmount() + "金币)!赠送礼物就有机会获得超值大奖!");
            if (marqueeTv.isVisibility()) {
                marqueeTv.addMessage(ssb);
                return;
            }
            marqueeTv.setVisibility(View.VISIBLE);
            marqueeTv.setText(ssb);
            marqueeTv.startScrollShow();
        }
    }

    /**
     * 爆奖池获奖名单
     */
    public void prizeCoreNoticeShow(CharSequence prizeRecordInfo) {
        if (marqueeTv.isVisibility()) {
            marqueeTv.addMessage(prizeRecordInfo);
            return;
        }
        marqueeTv.setVisibility(View.VISIBLE);
        marqueeTv.setText(prizeRecordInfo);
        marqueeTv.startScrollShow();
    }

    public void onKeyBoardStateChange(boolean keyBoardIsShow) {
        mKeyBoardIsShow = keyBoardIsShow;
        //如果键盘收起
        if (!((LiveActivity) activity).isLandscape()) {
            if (!mKeyBoardIsShow) {
                notifyData();
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.item_live_comment, null);
    }
}
