package com.android.banana.groupchat.view.baselist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.bet.BetGiftPop;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.PageIndicatorView;
import com.android.banana.commlib.view.PercentProgressView;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.library.Utils.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by qiaomu on 2017/11/6.
 */

public class TopicRecyclerView2 extends RecyclerView {

    private static final String TAG = "TopicRecyclerView";
    private static final String BASKET_BALL_RACE_TYPE = "BASKETBALL";
    private static final String FOOTBALL_BALL_RACE_TYPE = "FOOTBALL";
    private static final String OPTION_RQSF = "RQSF";
    private static final String OPTION_RFSF = "RFSF";
    private static final String GUEST_WIN = "GUEST_WIN";
    private static final String HOME_WIN = "HOME_WIN";
    private static final String SALE_PROGRESSING = "PROGRESSING";
    private static final long ANIM_DURATION = 500L;
    private MyLinearLayoutManager mManager;
    private TopicAdapter mAdapter;
    private int mBannerHeight;
    private int mMinHeight;
    private boolean isExpand = true;
    private RecyclerView mRecyclerView;
    private PageIndicatorView mIndicatorView;
    private int newState = RecyclerView.SCROLL_STATE_IDLE;
    private GroupChatTopic1.RaceIdAndGameBoard mGameBoard;

    public TopicRecyclerView2(Context context) {
        this(context, null);
    }

    public TopicRecyclerView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopicRecyclerView2(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mMinHeight = getResources().getDimensionPixelSize(R.dimen.topic_min_height);
        mManager = new MyLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(mManager);
        setItemAnimator(null);
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(this);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                super.onScrollStateChanged(recyclerView, state);
                newState = state;
                if (state == RecyclerView.SCROLL_STATE_IDLE && mIndicatorView != null)
                    mIndicatorView.setSelectedPage(mManager.findFirstCompletelyVisibleItemPosition());
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mBannerHeight == 0 && t != b && b != 0) {
            mBannerHeight = b - t;
        }
    }

    /*设置主题*/
    public void setTopic(GroupChatTopic1 chatTopic) {
        if (chatTopic == null || chatTopic.gameBoardList == null || chatTopic.gameBoardList.size() == 0)
            return;
        setVisibility(VISIBLE);
        if (mAdapter == null) {
            mAdapter = new TopicAdapter(getContext(), chatTopic);
            setAdapter(mAdapter);
            if (mIndicatorView != null) {
                mIndicatorView.initIndicator(chatTopic.gameBoardList.size());
                mIndicatorView.setSelectedPage(0);
                mIndicatorView.setVisibility(VISIBLE);
            }
        } else {
            int firstPosition = mManager.findFirstCompletelyVisibleItemPosition();
            if (mAdapter.getItemCount() != chatTopic.gameBoardList.size()) {
                if (mIndicatorView != null) {
                    mIndicatorView.initIndicator(chatTopic.gameBoardList.size());
                    mIndicatorView.setSelectedPage(0);
                    mIndicatorView.setVisibility(VISIBLE);
                }
                mAdapter.updateTopic(chatTopic);
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.updateTopic(chatTopic);
                mAdapter.notifyItemChanged(mManager.findFirstCompletelyVisibleItemPosition());
            }
        }
    }

    /*当前是否有可助威的局*/
    public boolean isGameValidate() {
        return mAdapter == null ? false : mAdapter.isGameValidate();
    }

    public void clear() {
        setAdapter(null);
        mAdapter = null;
        isExpand = true;
        setVisibility(GONE);
        if (mIndicatorView != null) {
            mIndicatorView.setVisibility(GONE);
        }
    }

    private class MyLinearLayoutManager extends LinearLayoutManager {

        private boolean mCanScroll = true;

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean canScrollHorizontally() {
            return mCanScroll;
        }

        public void setSanScrollHorizontally(boolean canScroll) {
            mCanScroll = canScroll;
        }
    }


    private class TopicAdapter extends RecyclerView.Adapter<com.android.banana.pullrecycler.multisupport.ViewHolder> {
        private Context mContext;
        private Map<String,JczqDataBean> basketballRaceMap;
        private List<GroupChatTopic1.RaceIdAndGameBoard> gameBoardList;
        private Map<String, JczqDataBean> footballRaceMap;
        private int selectPos = -1;
        private boolean isHomeSelect;

        public TopicAdapter(Context context, GroupChatTopic1 topic) {
            mContext = context;
            updateTopic(topic);
        }

        public void updateTopic(GroupChatTopic1 newTopic) {
            if (newTopic == null)
                return;
            this.basketballRaceMap = newTopic.basketballRaceMap;
            this.gameBoardList = newTopic.gameBoardList;
            this.footballRaceMap = newTopic.footballRaceMap;
        }

        @Override
        public com.android.banana.pullrecycler.multisupport.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.vp_item_game_view2, parent, false);
            return new com.android.banana.pullrecycler.multisupport.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(com.android.banana.pullrecycler.multisupport.ViewHolder holder, int position) {
            GroupChatTopic1.RaceIdAndGameBoard gameBoard = gameBoardList.get(position);
            if (TextUtils.equals(gameBoard.raceType, BASKET_BALL_RACE_TYPE)) {

                JczqDataBean basketball = basketballRaceMap.get(gameBoard.raceId);
                setUpTopic(holder, gameBoard, basketball);

            } else if (TextUtils.equals(gameBoard.raceType, FOOTBALL_BALL_RACE_TYPE)) {
                JczqDataBean basketball = footballRaceMap.get(gameBoard.raceId);
                setUpTopic(holder, gameBoard, basketball);
            }

        }

        /**
         * WAIT("未开始"),
         * <p>
         * PROGRESSING("进行中"),
         * <p>
         * PASUE("暂停"),
         * <p>
         * FINISH("结束"),
         * <p>
         * CANCEL("取消"),
         */
        private void setUpTopic(final com.android.banana.pullrecycler.multisupport.ViewHolder holder, final GroupChatTopic1.RaceIdAndGameBoard gameBoard,JczqDataBean basketball) {

            if (gameBoard == null || basketball == null)
                return;
            mGameBoard = gameBoard;
            GroupChatTopic1.RaceIdAndGameBoard.RaceStageTypeBean raceStageType = mGameBoard.raceStageType;
            final String optionGroup = mGameBoard.optionGroup;
            NormalObject saleStatus = mGameBoard.saleStatus;
            boolean showCheerTv = TextUtils.equals(saleStatus.getName(), SALE_PROGRESSING);
            holder.setViewVisibility(R.id.host_zhuwei, showCheerTv ? VISIBLE : GONE);
            holder.setViewVisibility(R.id.guest_zhuwei, showCheerTv ? VISIBLE : GONE);
            holder.setText(R.id.match_type_txt, basketball.matchName);
            String plate = mGameBoard.plate > 0 ? "+" + mGameBoard.plate : "" + mGameBoard.plate;
            String danWei = TextUtils.equals(OPTION_RQSF, optionGroup) ? "球" : "分";
            String hostTeamName = basketball.homeTeamName.length() > 6 ? basketball.homeTeamName.substring(0,6) : basketball.homeTeamName;
            String guestTeamName = basketball.guestTeamName.length() > 6 ? basketball.guestTeamName.substring(0,6) : basketball.guestTeamName;
            String homeSupportName = basketball.homeTeamName.length() > 4 ? basketball.homeTeamName.substring(0,4) + "..." : basketball.homeTeamName;
            String content = "<font color='#FFFFFF'>" + homeSupportName + "</font>"
                    + "<font color='#009900'>" + plate + "</font>" + "<font color='#FFFFFF'>" + danWei + "</font>"
                    + "<font color='#FFFFFF'>， 你支持哪支球队？</font>";
            holder.setText(R.id.support_txt, Html.fromHtml(content));
            String optionRule = TextUtils.equals(OPTION_RQSF, optionGroup) ? mContext.getString(R.string.rang_qiu) : mContext.getString(R.string.rang_fen);
            holder.setText(R.id.changci_txt, "[" + raceStageType.name + "]" + "比分");
            holder.setText(R.id.host_name_txt, hostTeamName);
            holder.setText(R.id.guest_name_txt, guestTeamName);
            holder.setText(R.id.match_time_txt, TimeUtils.formatStringDate(basketball.gmtStart, "MM-dd HH:mm"));
            holder.setText(R.id.chnangci_rangfei_txt, "[" + raceStageType.name + "]" + optionRule + plate);
            setUpProgressbar(holder, mGameBoard, 0, 0);
            final TextView checkedTvLeft = holder.getView(R.id.host_zhuwei);
            final TextView checkedTvRight = holder.getView(R.id.guest_zhuwei);
            checkedTvLeft.setSelected(selectPos == holder.getAdapterPosition() && isHomeSelect);
            checkedTvRight.setSelected(selectPos == holder.getAdapterPosition() && !isHomeSelect);
            holder.setOnClickListener(R.id.host_zhuwei, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedTvLeft.setSelected(true);
                    checkedTvRight.setSelected(false);
                    selectPos = holder.getAdapterPosition();
                    isHomeSelect = true;
                    showBetGiftPop(holder, mGameBoard, optionGroup, BetGiftPop.OptionCode.HOME_WIN);
                }
            });

            holder.setOnClickListener(R.id.guest_zhuwei, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedTvLeft.setSelected(false);
                    checkedTvRight.setSelected(true);
                    selectPos = holder.getAdapterPosition();
                    isHomeSelect = false;
                    showBetGiftPop(holder, mGameBoard, optionGroup, BetGiftPop.OptionCode.GUEST_WIN);
                }
            });

            View.OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAnimation(holder);
                }
            };
            holder.setOnClickListener(R.id.folder_img, onClickListener);
        }

        private void showBetGiftPop(final com.android.banana.pullrecycler.multisupport.ViewHolder holder, final GroupChatTopic1.RaceIdAndGameBoard gameBoard, String optionGroup, final String optionCode) {
            final boolean isHome = TextUtils.equals(optionCode, HOME_WIN);
            CharSequence teamName = isHome ? ((TextView) holder.getView(R.id.host_name_txt)).getText() : ((TextView) holder.getView(R.id.guest_name_txt)).getText();
            BetGiftPop betGiftPop = new BetGiftPop(mContext,
                    gameBoard.gmtCreate,
                    gameBoard.raceId,
                    gameBoard.raceType,
                    gameBoard.gameBoardId,
                    optionCode,
                    optionGroup);
            betGiftPop.show();
            betGiftPop.setTeamName(String.valueOf(teamName));
            betGiftPop.setOnSendBetGiftSuccess(new BetGiftPop.IonSendBetGiftSuccess() {
                @Override
                public void onSendBetGiftSuccess(double price) {
                    double homePrice = isHome ? price : 0;
                    double guestPrice = !isHome ? price : 0;
                    LogUtils.e(TAG, "homePrice=" + homePrice + "  guestPrice=" + guestPrice);
                    setUpProgressbar(holder, mGameBoard, homePrice, guestPrice);
                }
            });
        }

        private void setUpProgressbar(com.android.banana.pullrecycler.multisupport.ViewHolder holder, GroupChatTopic1.RaceIdAndGameBoard gameBoard, double addPriceHome, double addPriceGuest) {
            try {
                PercentProgressView progressBar = holder.getView(R.id.percent_progress_bar);
                List<GroupChatTopic1.Thermodynamic> entries = gameBoard.gameBoardOptionEntries;
                if (entries == null || entries.size() == 0)
                    return;
                double totalFee = 0d;
                double homeTotalFee = 0d, homeTotalPaidFee = 0d;
                double guestTotalFee = 0d, guestTotalPaidFee = 0d;
                for (int i = 0; i < entries.size(); i++) {
                    GroupChatTopic1.Thermodynamic thermodynamic = entries.get(i);
                    String optionCode = thermodynamic.optionCode;
                    NormalObject saleStatus = gameBoard.saleStatus;
                    boolean showCheerTv = TextUtils.equals(saleStatus.getName(), SALE_PROGRESSING);
                    if (TextUtils.equals(optionCode, GUEST_WIN)) {
                        guestTotalFee = thermodynamic.totalFee + addPriceGuest;
                        totalFee += guestTotalFee;
                        guestTotalPaidFee += thermodynamic.totalPaidFee + addPriceGuest;
                        thermodynamic.totalPaidFee = guestTotalPaidFee;
                        holder.setViewVisibility(R.id.guest_coin_value, showCheerTv && guestTotalPaidFee != 0 ? VISIBLE : GONE)
                                .setText(R.id.guest_coin_value, String.valueOf((int) thermodynamic.totalPaidFee));
                    } else if (TextUtils.equals(optionCode, HOME_WIN)) {
                        homeTotalFee = thermodynamic.totalFee + addPriceHome;
                        totalFee += homeTotalFee;
                        homeTotalPaidFee += thermodynamic.totalPaidFee + addPriceHome;
                        thermodynamic.totalPaidFee = homeTotalPaidFee;
                        holder.setViewVisibility(R.id.host_coin_value, showCheerTv && homeTotalPaidFee != 0 ? VISIBLE : GONE)
                                .setText(R.id.host_coin_value, String.valueOf((int) thermodynamic.totalPaidFee));
                    }
                }
                LogUtils.e(TAG, "homePrice=" + homeTotalFee + "  guestPrice=" + guestTotalFee);
                progressBar.setProgressValue((int) homeTotalFee, (int) guestTotalFee, false);
            } catch (Exception e) {

            }
        }

        @Override
        public int getItemCount() {
            return gameBoardList == null ? 0 : gameBoardList.size();
        }

        public boolean isGameValidate() {
            if (gameBoardList == null || gameBoardList.size() == 0)
                return false;
            for (GroupChatTopic1.RaceIdAndGameBoard gameBoard : gameBoardList) {
                NormalObject saleStatus = gameBoard.saleStatus;
                boolean showCheer = TextUtils.equals(saleStatus.getName(), SALE_PROGRESSING);
                if (showCheer) {
                    return true;
                }
            }
            return false;
        }
    }

    private float distance;

    private void startAnimation(final com.android.banana.pullrecycler.multisupport.ViewHolder holder) {
        distance = 0;
        MarginLayoutParams params = (MarginLayoutParams) holder.getView(R.id.content_lay).getLayoutParams();
        View contentLayout = holder.getView(R.id.content_lay);
        final Drawable alphaBackGround = contentLayout.getBackground();
        if (mRecyclerView != null)
            mRecyclerView.stopScroll();

        View optionLayout = holder.getView(R.id.top_lay);
        int fromHeight = isExpand ? mBannerHeight : mMinHeight;
        int targetHeight = isExpand ? mMinHeight : mBannerHeight;
        float translateY_option = optionLayout.getHeight() * 0.3f;
        optionLayout.animate().translationY((isExpand ? -1 : 0) * translateY_option).setDuration(ANIM_DURATION).start();

        int translateX_right = holder.getView(R.id.folder_img).getLeft() - 10;
        View guestName = holder.getView(R.id.guest_name_txt);
        guestName.animate().translationX((isExpand ? 1 : 0) * (translateX_right - guestName.getLeft() - guestName.getWidth()))
                .setDuration(ANIM_DURATION).start();
        View vsTxt = holder.getView(R.id.vs_txt);
        vsTxt.animate().translationX((isExpand ? 1 : 0) * (translateX_right - vsTxt.getLeft() - vsTxt.getWidth() - guestName.getWidth()))
                .setDuration(ANIM_DURATION).start();
        View hostName = holder.getView(R.id.host_name_txt);
        hostName.animate().translationX((isExpand ? 1 : 0) * (translateX_right - hostName.getLeft() - hostName.getWidth() - vsTxt.getWidth() - guestName.getWidth()))
                .setDuration(ANIM_DURATION).start();

        ValueAnimator animator = ValueAnimator.ofInt(fromHeight, targetHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                RecyclerView.LayoutParams params = (LayoutParams) holder.itemView.getLayoutParams();
                params.height = height;
                holder.itemView.setLayoutParams(params);

                if (alphaBackGround != null) {
                    float fraction = animation.getAnimatedFraction();
                    alphaBackGround.setAlpha(isExpand ? (int) ((1 - fraction) * 255) : (int) (255 * fraction));
                }

                if (mRecyclerView != null) {
                    float fraction = animation.getAnimatedFraction();
                    float curDistance = fraction * (mBannerHeight - mMinHeight);
                    mRecyclerView.scrollBy(0, (int) ((isExpand ? 1 : -1) * (curDistance - distance)));
                    distance = curDistance;
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isExpand = !isExpand;
                mManager.setSanScrollHorizontally(isExpand);
                holder.setViewVisibility(R.id.match_type_txt, isExpand ? VISIBLE : INVISIBLE);
                holder.setViewVisibility(R.id.match_time_txt, isExpand ? VISIBLE : INVISIBLE);
                holder.setViewVisibility(R.id.chnangci_rangfei_txt, isExpand ? INVISIBLE : VISIBLE);
                int resId = isExpand ? R.drawable.arrow_up : R.drawable.arrow_down;
                holder.setBackgroundRes(R.id.folder_img, resId);
                if (mIndicatorView != null)
                    mIndicatorView.setVisibility(isExpand ? VISIBLE : GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mIndicatorView != null)
                    mIndicatorView.setVisibility(GONE);
                int resId = isExpand ? R.drawable.arrow_up : R.drawable.arrow_down;
                holder.setBackgroundRes(R.id.folder_img, resId);
                holder.setViewVisibility(R.id.match_type_txt, isExpand ? VISIBLE : INVISIBLE);
                holder.setViewVisibility(R.id.match_time_txt, isExpand ? VISIBLE : INVISIBLE);
                holder.setViewVisibility(R.id.chnangci_rangfei_txt, isExpand ? INVISIBLE : VISIBLE);
                holder.setViewVisibility(R.id.textView28, INVISIBLE);
                holder.setViewVisibility(R.id.textView32, INVISIBLE);
            }
        });
        animator.setDuration(ANIM_DURATION);
        animator.start();
    }

    public void attachToIndicatorView(PageIndicatorView indicatorView) {
        mIndicatorView = indicatorView;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public boolean isExpand() {
        return isExpand;
    }
}
