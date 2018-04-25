package com.android.banana.groupchat.view.baselist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.bet.BetGiftPop;
import com.android.banana.commlib.game.ShowPopListSelectorView;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.PageIndicatorView;
import com.android.banana.commlib.view.PercentProgressView;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.android.banana.commlib.bet.BetUrlEnum.PURCHASE_BASKETBALL_NORMAL;
import static com.android.banana.commlib.bet.BetUrlEnum.PURCHASE_FOOTBALL_NORMAL;

/**
 * Created by qiaomu on 2017/11/6.
 */

public class TopicRecyclerView3 extends RecyclerView implements IHttpResponseListener {
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

    //赛事信息
    private JczqDataBean matchDataBean;
    //可选个数修改
    private List<Integer> multipleList;
    private WrapperHttpHelper httpHelper;
    private Animation btnAnim;

    public TopicRecyclerView3(Context context) {
        this(context, null);
    }

    public TopicRecyclerView3(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopicRecyclerView3(Context context, AttributeSet attrs, int defStyle) {
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
                if (state == RecyclerView.SCROLL_STATE_IDLE && mIndicatorView != null) {
                    int position = mManager.findFirstCompletelyVisibleItemPosition();

                    mIndicatorView.setSelectedPage(position);
                }

            }
        });
        httpHelper = new WrapperHttpHelper(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        if (mBannerHeight == 0 && t != b && b != 0) {
//            mBannerHeight = b - t;
//        }
    }

    public void setMatch(JczqDataBean matchDataBean) {
        this.matchDataBean = matchDataBean;
    }

    /*设置主题*/
    public void setTopic(GroupChatTopic1 chatTopic) {
        if (chatTopic == null || chatTopic.gameBoardList == null || chatTopic.gameBoardList.size() == 0)
            return;

        btnAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rocket_button_click);
        multipleList = chatTopic.multipleList;
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

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        try {
            int position = (int) request.getTag();
            ToastUtil.showLong(getContext().getApplicationContext(), getContext().getString(R.string.send_success));
            JSONObject jsonObject = (JSONObject) o;
            double totalFee = 0;
            String option = null;
            if (jsonObject.has("totalFee")) {
                totalFee = jsonObject.getDouble("totalFee");
            }
            if (jsonObject.has("option")) {
                option = jsonObject.getString("option");
            }
            GroupChatTopic1.RaceIdAndGameBoard item = mAdapter.getItem(position);
            if (TextUtils.equals("HOME_WIN", option)) {
                item.leftGameBoardOptionEntry.totalFee += totalFee;
                item.leftGameBoardOptionEntry.totalPaidFee += totalFee;
            } else {
                item.rightGameBoardOptionEntry.totalFee += totalFee;
                item.rightGameBoardOptionEntry.totalPaidFee += totalFee;
            }
            mAdapter.notifyItemChanged(position);
            //setUpProgressbar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        Context context = getContext();
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
        } else if (context instanceof ContextThemeWrapper) {
            Context baseContext = ((ContextThemeWrapper) context).getBaseContext();
            ((BaseActivity) baseContext).operateErrorResponseMessage(jsonObject);
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


    private class TopicAdapter extends Adapter<com.android.banana.pullrecycler.multisupport.ViewHolder> {
        private Context mContext;
        private Map<String, JczqDataBean> basketballRaceMap;
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_cheer_betting_game_group1, parent, false);
            return new com.android.banana.pullrecycler.multisupport.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(com.android.banana.pullrecycler.multisupport.ViewHolder holder, int position) {
            GroupChatTopic1.RaceIdAndGameBoard gameBoard = gameBoardList.get(position);
            setUpTopic(holder, gameBoard, position);
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
        private void setUpTopic(final com.android.banana.pullrecycler.multisupport.ViewHolder holder, final GroupChatTopic1.RaceIdAndGameBoard gameBoard, final int position) {

            final JczqDataBean matchDataBean = getMatchDataBeanFromMap(gameBoard);
            if (matchDataBean == null)
                return;

            boolean isFootball = !TextUtils.equals(matchDataBean.raceType.getName(), BASKET_BALL_RACE_TYPE);
            holder.setBackgroundRes(R.id.group_main_lay, isFootball ? R.drawable.item_football_ball_bg : R.drawable.item_basket_ball_bg);
            setBackGround(isFootball);

            GroupChatTopic1.RaceIdAndGameBoard.RaceStageTypeBean raceStageType = gameBoard.raceStageType;
            final String optionGroup = gameBoard.optionGroup;
            NormalObject saleStatus = gameBoard.saleStatus;
            boolean showCheerTv = TextUtils.equals(saleStatus.getName(), SALE_PROGRESSING);
            holder.setViewVisibility(R.id.home_lay, showCheerTv ? VISIBLE : INVISIBLE);
            holder.setViewVisibility(R.id.guest_lay, showCheerTv ? VISIBLE : INVISIBLE);
            final ShowPopListSelectorView leftNumSelectorView = holder.getView(R.id.home_show_pop_list_selector_view);
            final ShowPopListSelectorView rightNumSelectorView = holder.getView(R.id.guest_show_pop_list_selector_view);
            leftNumSelectorView.setData(multipleList);
            rightNumSelectorView.setData(multipleList);

            final GroupChatTopic1.Thermodynamic leftOptionEntry = gameBoard.leftGameBoardOptionEntry;
            final GroupChatTopic1.Thermodynamic rightOptionEntry = gameBoard.rightGameBoardOptionEntry;
            if (showCheerTv) {
                holder.setImageByUrl(mContext, R.id.home_icon, leftOptionEntry.betFormImageUrl);
                holder.setImageByUrl(mContext, R.id.guest_icon, rightOptionEntry.betFormImageUrl);
            }
            holder.setText(R.id.match_type_txt, matchDataBean.matchName);
            String plate = gameBoard.plate > 0 ? "+" + gameBoard.plate : "" + gameBoard.plate;
            String danWei = TextUtils.equals(OPTION_RQSF, optionGroup) ? "球" : "分";
            String hostTeamName = matchDataBean.homeTeamName.length() > 6 ? matchDataBean.homeTeamName.substring(0, 6) : matchDataBean.homeTeamName;
            String guestTeamName = matchDataBean.guestTeamName.length() > 6 ? matchDataBean.guestTeamName.substring(0, 6) : matchDataBean.guestTeamName;

            String content = "<font color='#000'>" + hostTeamName + "[</font>"
                    + (Double.parseDouble(plate) > 0 ? "<font color='#FF2335'>" : "<font color='#00C10D'>") + plate + "</font>" + "<font color='#000'>]";
            //赛事信息绑定
            holder.setText(R.id.home_team_name, hostTeamName);
            holder.setText(R.id.guest_team_name, guestTeamName);

            holder.setText(R.id.left_per_money, leftOptionEntry.betFormSingleFee);
            holder.setText(R.id.right_per_money, rightOptionEntry.betFormSingleFee);
            //投注量
            setUpProgressbar(holder, gameBoard, 0, 0);

            holder.setText(R.id.match_name_and_time, matchDataBean.matchName +
                    TimeUtils.format(TimeUtils.LONG_DATEFORMAT, TimeUtils.MATCH_FORMAT, matchDataBean.getGmtStart()));
            //设置中间局的显示
            String dangWei = matchDataBean.isFootball() ? "球" : "分";
            String color = gameBoard.plate > 0 ? "#ff0000" : "#009900";
            String score = "[" + "<font color='" + color + "'>" + plate + "</font>" + "<font color='#505050'>" + dangWei + "]";
            String scoreAndChangciStr = score + " " + gameBoard.raceStageType.name + "比分";
            holder.setText(R.id.differ_score_changci, Html.fromHtml(scoreAndChangciStr));

            holder.setOnClickListener(R.id.home_icon, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.getView(R.id.home_lay).startAnimation(btnAnim);

                    presentCheerGift(matchDataBean, leftOptionEntry.boardId, leftOptionEntry.optionCode,
                            leftOptionEntry.betFormNo, matchDataBean.isFootball() ? "RQSF" : "RFSF", leftNumSelectorView.getCurrentNum(), position);
                }
            });

            holder.setOnClickListener(R.id.guest_icon, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.getView(R.id.guest_lay).startAnimation(btnAnim);
                    presentCheerGift(matchDataBean, rightOptionEntry.boardId, rightOptionEntry.optionCode,
                            rightOptionEntry.betFormNo, matchDataBean.isFootball() ? "RQSF" : "RFSF", rightNumSelectorView.getCurrentNum(), position);
                }
            });

        }

        private JczqDataBean getMatchDataBeanFromMap(GroupChatTopic1.RaceIdAndGameBoard gameBoard) {
            if (TextUtils.equals(gameBoard.raceType, BASKET_BALL_RACE_TYPE) && basketballRaceMap != null) {
                JczqDataBean matchDataBean = basketballRaceMap.get(gameBoard.raceId);
                matchDataBean.raceType = new NormalObject(gameBoard.raceType, "");
                return matchDataBean;
            }
            if (TextUtils.equals(gameBoard.raceType, FOOTBALL_BALL_RACE_TYPE) && footballRaceMap != null) {
                JczqDataBean matchDataBean = footballRaceMap.get(gameBoard.raceId);
                matchDataBean.raceType = new NormalObject(gameBoard.raceType, "");
                return matchDataBean;
            }
            return matchDataBean;
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
                    setUpProgressbar(holder, gameBoard, homePrice, guestPrice);
                }
            });
        }

        private void setUpProgressbar(com.android.banana.pullrecycler.multisupport.ViewHolder holder, GroupChatTopic1.RaceIdAndGameBoard gameBoard, double addPriceHome, double addPriceGuest) {
            try {
                PercentProgressView progressBar = holder.getView(R.id.customer_progress_bar);
                double totalFee = 0d;
                double homeTotalFee = 0d, homeTotalPaidFee = 0d;
                double guestTotalFee = 0d, guestTotalPaidFee = 0d;

                GroupChatTopic1.Thermodynamic leftGameBoardOptionEntry = gameBoard.leftGameBoardOptionEntry;
                GroupChatTopic1.Thermodynamic rightGameBoardOptionEntry = gameBoard.rightGameBoardOptionEntry;
                NormalObject saleStatus = gameBoard.saleStatus;
                boolean showCheerTv = TextUtils.equals(saleStatus.getName(), SALE_PROGRESSING);


                CharSequence guesttext = ((TextView) holder.getView(R.id.guest_gold_coin)).getText();
                guestTotalPaidFee = rightGameBoardOptionEntry.totalPaidFee > Double.valueOf(TextUtils.isEmpty(guesttext) ? "0" : guesttext.toString()) ?
                        rightGameBoardOptionEntry.totalPaidFee : Double.valueOf(TextUtils.isEmpty(guesttext) ? "0" : guesttext.toString());
                rightGameBoardOptionEntry.totalPaidFee = guestTotalPaidFee;
                holder.setViewVisibility(R.id.guest_gold_coin, showCheerTv && guestTotalPaidFee != 0 ? VISIBLE : INVISIBLE)
                        .setText(R.id.guest_gold_coin, new Money(rightGameBoardOptionEntry.totalPaidFee).toSimpleString());

                CharSequence hometext = ((TextView) holder.getView(R.id.home_gold_coin)).getText();
                homeTotalPaidFee = leftGameBoardOptionEntry.totalPaidFee > Double.valueOf(TextUtils.isEmpty(hometext) ? "0" : hometext.toString()) ?
                        leftGameBoardOptionEntry.totalPaidFee : Double.valueOf(TextUtils.isEmpty(hometext) ? "0" : hometext.toString());
                leftGameBoardOptionEntry.totalPaidFee = homeTotalPaidFee;
                holder.setViewVisibility(R.id.home_gold_coin, showCheerTv && homeTotalPaidFee != 0 ? VISIBLE : INVISIBLE)
                        .setText(R.id.home_gold_coin, new Money(leftGameBoardOptionEntry.totalPaidFee).toSimpleString());

                homeTotalFee = leftGameBoardOptionEntry.totalFee > progressBar.getHostProgressValue() ?
                        leftGameBoardOptionEntry.totalFee : progressBar.getHostProgressValue();
                guestTotalFee = rightGameBoardOptionEntry.totalFee > progressBar.getGuestProgressValue() ?
                        rightGameBoardOptionEntry.totalFee : progressBar.getGuestProgressValue();
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

        private GroupChatTopic1.RaceIdAndGameBoard getItem(int position) {
            return gameBoardList == null ? null : gameBoardList.get(position);
        }
    }

    private void presentCheerGift(JczqDataBean matchDataBean, String boardId, String optionCode, String payTypeNo, String playType, int currentNum, int position) {
        RequestFormBody map = null;
        if (matchDataBean.isFootball()) {
            map = new RequestFormBody(PURCHASE_FOOTBALL_NORMAL, true);
        } else {
            map = new RequestFormBody(PURCHASE_BASKETBALL_NORMAL, true);
        }
        map.put("frontType", "M_CLIENT");
        map.put("createrType", "USER_CREATE");
        map.put("payCoinType", "GOLD_COIN");
        map.put("payType", "GIFT");
        map.put("payTypeNo", payTypeNo);
        map.put("boardId", boardId);
        map.put("keyAndOptions", boardId + "@" + optionCode);
        map.put("playType", "CHEER");
        map.put("playSubType", playType);
        map.put("multiple", currentNum);
        map.setTag(position);
        //Log.i("yjj", "reqSendGift rate = " + rate);
        httpHelper.startRequest(map, true);
    }

    private float distance;

    public void startAnimation(final FrameLayout topicLayout, final LinearLayout folderLayout, final TextView scoreTv, final TextView teamNameTv, final ImageView expandIv) {

        final int position = mManager.findFirstCompletelyVisibleItemPosition();
        final GroupChatTopic1.RaceIdAndGameBoard mGameBoard = mAdapter.getItem(position);
        final JczqDataBean matchDataBean = mAdapter.getMatchDataBeanFromMap(mGameBoard);

        final com.android.banana.pullrecycler.multisupport.ViewHolder holder = (com.android.banana.pullrecycler.multisupport.ViewHolder) findViewHolderForLayoutPosition(position);
        if (holder == null)
            return;
        if (mRecyclerView != null)
            mRecyclerView.stopScroll();

        if (mBannerHeight == 0) {
            mBannerHeight = topicLayout.getHeight();
            mMinHeight = folderLayout.getHeight();
        }

        if (mIndicatorView != null)
            mIndicatorView.setVisibility(GONE);

        int fromHeight = isExpand ? mBannerHeight : mMinHeight;
        int targetHeight = isExpand ? mMinHeight : mBannerHeight;

        final Drawable alphaBackGround = ((ViewGroup) getParent()).getBackground();
        ValueAnimator animator = ValueAnimator.ofInt(fromHeight, targetHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) topicLayout.getLayoutParams();
                params.height = height;

                //下面requestLayout 的代码是因为 当前itemView 和下一个position对应的itemView  避免视图显示不出来
                //先折叠 在展开 ，去掉之后  后面的 就会显示不出来 我也不知道为啥
                topicLayout.setLayoutParams(params);
                holder.itemView.requestLayout();

                View nextItemView = mManager.findViewByPosition(position + 1);
                if (nextItemView != null) {
                    ViewHolder childViewHolder = getChildViewHolder(nextItemView);
                    if (childViewHolder != null)
                        childViewHolder.itemView.requestLayout();
                }

                View preItemView = mManager.findViewByPosition(position - 1);
                if (preItemView != null) {
                    ViewHolder childViewHolder = getChildViewHolder(preItemView);
                    if (childViewHolder != null)
                        childViewHolder.itemView.requestLayout();
                }


                float fraction = animation.getAnimatedFraction();

                if (alphaBackGround != null) {
                    alphaBackGround.setAlpha(isExpand ? (int) ((1 - fraction) * 255) : (int) (255 * fraction));
                }

                if (mRecyclerView != null) {
                    float curDistance = fraction * (mBannerHeight - mMinHeight);
                    mRecyclerView.scrollBy(0, (int) ((isExpand ? 1 : -1) * (curDistance - distance)));
                    distance = curDistance;
                }
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = !isExpand;
                mManager.setSanScrollHorizontally(isExpand);

                if (!isExpand) {
                    folderLayout.setBackgroundColor(Color.parseColor("#cfe9e9"));
                    GroupChatTopic1.RaceIdAndGameBoard.RaceStageTypeBean raceStageType = mGameBoard.raceStageType;
                    String plate = mGameBoard.plate > 0 ? "+" + mGameBoard.plate : "" + mGameBoard.plate;
                    final String optionGroup = mGameBoard.optionGroup;
                    String danWei = TextUtils.equals(OPTION_RQSF, optionGroup) ? "球" : "分";
                    String content = "<font color='#000'>" + "[" + raceStageType.name + "]比分" + "[</font><font color='#009900'>" + plate + "</font>" + "<font color='#000'>" + danWei + "]</font>";

                    scoreTv.setText(Html.fromHtml(content));
                    scoreTv.setVisibility(VISIBLE);

                    String hostTeamName = matchDataBean.homeTeamName.length() > 6 ? matchDataBean.homeTeamName.substring(0, 6) : matchDataBean.homeTeamName;
                    String guestTeamName = matchDataBean.guestTeamName.length() > 6 ? matchDataBean.guestTeamName.substring(0, 6) : matchDataBean.guestTeamName;
                    teamNameTv.setText(hostTeamName + "  VS  " + guestTeamName);
                    teamNameTv.setVisibility(VISIBLE);
                }

                if (mIndicatorView != null)
                    mIndicatorView.setVisibility(isExpand ? VISIBLE : GONE);

                int resId = isExpand ? R.drawable.arrow_up : R.drawable.arrow_down;
                expandIv.setImageResource(resId);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                if (!isExpand) {
                    folderLayout.setBackgroundColor(Color.TRANSPARENT);
                    scoreTv.setVisibility(GONE);
                    teamNameTv.setVisibility(GONE);
                }

                int resId = isExpand ? R.drawable.arrow_up : R.drawable.arrow_down;
                expandIv.setImageResource(resId);
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

    private void setBackGround(boolean isFootball) {
        ((ViewGroup) getParent()).setBackgroundResource(isFootball ? R.drawable.icon_header_cheer_ft_bg : R.drawable.icon_header_cheer_bt_bg);
    }
}
