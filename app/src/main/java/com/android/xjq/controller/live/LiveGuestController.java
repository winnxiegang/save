package com.android.xjq.controller.live;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.groupchat.chat.SimpleChatActivity;
import com.android.banana.groupchat.groupchat.RewardActivity;
import com.android.banana.utils.DialogHelper;
import com.android.banana.utils.ListSelectDialog;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.GuestApplyActivity;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.login.LoginActivity;
import com.android.xjq.adapter.main.LiveGuestAdapter;
import com.android.xjq.adapter.main.LiveMedalAdapter;
import com.android.xjq.bean.live.main.RankedMedalInfoBean;
import com.android.xjq.bean.live.main.SeatInfoBean;
import com.android.xjq.dialog.live.PersonalInfoDialog;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.SwitchTabView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.banana.commlib.dialog.BaseDialogFragment.Theme.NORAML_THEME;

/**
 * Created by zhouyi on 2017/3/7.
 */

public class LiveGuestController extends BaseLiveController<LiveActivity> {
    private LiveGuestViewHolder mViewHolder;
    private LiveGuestAdapter liveGuestAdapter;
    private LiveMedalAdapter liveMedalAdapter;
    private List<SeatInfoBean.ChannelVipUserSimpleInfo> seatSimpleList = new ArrayList<>();
    private List<RankedMedalInfoBean.RankMedalTotalSimpleListBean> rankedMedalListData = new ArrayList<>();

    public LiveGuestController(LiveActivity context) {
        super(context);
    }

    private static final int TAB_SEAT = 0;
    private static final int TAB_MEDAL = 1;
    private int mCurSelectTab = TAB_SEAT;

    @Override
    public void init(View view) {
        if (mViewHolder == null) {
            mViewHolder = new LiveGuestViewHolder(view);
            setListener();
            init();
        }
    }

    private void init() {
        mViewHolder.emptyIv.setImageResource(R.drawable.icon_my_channel_empty);
        liveGuestAdapter = new LiveGuestAdapter(context, seatSimpleList);
        liveMedalAdapter = new LiveMedalAdapter(context, rankedMedalListData);
        mViewHolder.listView.setAdapter(liveGuestAdapter);
        mViewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //非龙椅,vip座位有人坐上才可以私聊,且不可以与自己私聊
                if (mCurSelectTab == TAB_SEAT &&
                        seatSimpleList.get(position) != null &&
                        seatSimpleList.get(position).getUserId() != null &&
                        !LoginInfoHelper.getInstance().getUserId().equals(seatSimpleList.get(position).getUserId())) {
                    String userId = seatSimpleList.get(position).getUserId();
                    new PersonalInfoDialog(context, userId).show();
                }
            }
        });
        mViewHolder.switchTabView.setCheckedTab(SwitchTabView.TAB_LEFT);
        mCurSelectTab = TAB_SEAT;

        ((ImageView) mViewHolder.emptyLayout.findViewById(R.id.emptyIv)).setImageResource(R.drawable.icon_no_content_about_match_schedule_detail);
    }

    private void setListener() {
        mViewHolder.applyGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfoHelper.getInstance().getUserId() == null) {
                    LoginActivity.startLoginActivity(context, false);
                    return;
                }
                GuestApplyActivity.startGuestApplyActivity(context);
            }
        });
        mViewHolder.switchTabView.setOnSwitchTabListener(new SwitchTabView.IonSwitchTab() {
            @Override
            public void onSwitchLeftTab() {
                mCurSelectTab = TAB_SEAT;
                showSeatListView();
            }

            @Override
            public void onSwitchRightTab() {
                mCurSelectTab = TAB_MEDAL;
                showMedalListView();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean isHide) {
        if (!isHide) {
            if (mCurSelectTab == TAB_SEAT) {
                showSeatListView();
            } else if (mCurSelectTab == TAB_MEDAL) {
                showMedalListView();
            }
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    private void showMedalListView() {
        context.getLiveHttpRequestController().getRankedMedalList();
        mViewHolder.listView.setAdapter(liveMedalAdapter);
        // mViewHolder.bottomSeatLay.setVisibility(View.GONE);
        mViewHolder.bottomDivider.setVisibility(View.GONE);
    }

    private void showSeatListView() {
        context.getLiveHttpRequestController().getSeatInfoList(context.getChannelId());
        mViewHolder.listView.setAdapter(liveGuestAdapter);
        mViewHolder.emptyLayout.setVisibility(View.GONE);
        // mViewHolder.bottomSeatLay.setVisibility(View.VISIBLE);
        mViewHolder.bottomDivider.setVisibility(View.VISIBLE);
    }

    @Override
    public void setView() {

    }

    private void showMenuDialog(final String userId, final String userName) {
        DialogHelper.showListDialog(context.getSupportFragmentManager(), new ListSelectDialog.OnClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                switch (pos) {
                    case 0:
                        SimpleChatActivity.startSimpleChatActivity(context, userId, userName, null);
                        break;
                    case 1:
                        RewardActivity.startRewardActivity(context, userId, null);
                        break;
                }
            }
        }, NORAML_THEME, "私聊", "打赏");
    }

    @Override
    public void responseSuccessHttp(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case VIP_SEAT_LIST_QUERY:
                responseSeatQuerySuccess(jsonObject);
                break;
            case FANS_RANK_MEDAL_TOTAL_QUERY:
                responseRankMedalQuerySuccess(jsonObject);
                break;
        }
    }

    private void responseSeatQuerySuccess(JSONObject jsonObject) {
        SeatInfoBean seatInfoBean = new Gson().fromJson(jsonObject.toString(), SeatInfoBean.class);
        LogUtils.e("responseSuccessHttp", "seatInfoBean+" + seatInfoBean.toString());
        seatInfoBean.operatorData();
        seatSimpleList.clear();
        if (seatInfoBean.getSeatSitterSimpleList() != null && seatInfoBean.getSeatSitterSimpleList().size() > 0) {
            seatSimpleList.addAll(seatInfoBean.getSeatSitterSimpleList());
        }
        liveGuestAdapter.notifyDataSetChanged();
    }

    private void responseRankMedalQuerySuccess(JSONObject jsonObject) {
        RankedMedalInfoBean rankedMedalInfoBean = new Gson().fromJson(jsonObject.toString(), RankedMedalInfoBean.class);
        rankedMedalListData.clear();
        if (rankedMedalInfoBean.getRankMedalTotalSimpleList() != null && rankedMedalInfoBean.getRankMedalTotalSimpleList().size() > 0) {
            rankedMedalInfoBean.operate(context);
            rankedMedalListData.addAll(rankedMedalInfoBean.getRankMedalTotalSimpleList());
            liveMedalAdapter.notifyDataSetChanged();
            mViewHolder.emptyLayout.setVisibility(View.GONE);
        } else {
            mViewHolder.emptyLayout.setVisibility(View.VISIBLE);
            if (rankedMedalInfoBean.isAnchorHasFanMedal()) {
                mViewHolder.emptyTv.setText("无人上榜");
            } else {
                mViewHolder.emptyTv.setText("主播尚未开通粉丝勋章");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewHolder = null;
    }

    static class LiveGuestViewHolder {
        @BindView(R.id.listView)
        ListView listView;
        @BindView(R.id.applyGuestBtn)
        Button applyGuestBtn;
        @BindView(R.id.switch_tab)
        SwitchTabView switchTabView;
        @BindView(R.id.emptyLayout)
        LinearLayout emptyLayout;
        @BindView(R.id.emptyIv)
        ImageView emptyIv;
        @BindView(R.id.emptyTv)
        TextView emptyTv;
        @BindView(R.id.bottom_seat_lay)
        RelativeLayout bottomSeatLay;
        @BindView(R.id.bottom_diveder)
        View bottomDivider;

        public LiveGuestViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
