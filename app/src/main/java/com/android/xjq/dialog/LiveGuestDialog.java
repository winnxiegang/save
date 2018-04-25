package com.android.xjq.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.GuestApplyActivity;
import com.android.xjq.activity.login.LoginActivity;
import com.android.xjq.adapter.main.LiveGuestAdapter;
import com.android.xjq.adapter.main.LiveMedalAdapter;
import com.android.xjq.bean.live.main.RankedMedalInfoBean;
import com.android.xjq.bean.live.main.SeatInfoBean;
import com.android.xjq.dialog.base.DialogBase;
import com.android.xjq.dialog.live.PersonalInfoDialog;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.SwitchTabView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.FANS_RANK_MEDAL_TOTAL_QUERY;

/**
 * Created by lingjiu on 2017/4/10.
 */

public class LiveGuestDialog extends DialogBase implements OnHttpResponseListener {
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.switch_tab)
    SwitchTabView switchTabView;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @BindView(R.id.emptyIv)
    ImageView emptyIv;
    @BindView(R.id.emptyTv)
    TextView emptyTv;
    @BindView(R.id.bottom_seat_relLay)
    RelativeLayout bottomSeatLay;
    @BindView(R.id.bottom_divider)
    View bottomDivider;
    private List<RankedMedalInfoBean.RankMedalTotalSimpleListBean> rankedMedalListData = new ArrayList<>();
    private LiveMedalAdapter liveMedalAdapter;
    private HttpRequestHelper httpRequestHelper;
    private String channelId;
    private LiveGuestAdapter liveGuestAdapter;
    private List<SeatInfoBean.ChannelVipUserSimpleInfo> seatSimpleList = new ArrayList<>();

    @OnClick(R.id.applyGuestBtn)
    public void applyGuest() {
        if (LoginInfoHelper.getInstance().getUserId() == null) {
            LoginActivity.startLoginActivity((Activity) context, false);
            return;
        }
        GuestApplyActivity.startGuestApplyActivity((Activity) context);
    }

    public LiveGuestDialog(Context context, String channelId) {
        super(context, R.layout.dialog_guest_live, R.style.dialog_base, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.channelId = channelId;
        ButterKnife.bind(this, rootView);
        init();
        requestSeatData();
    }

    private void requestSeatData() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.VIP_SEAT_LIST_QUERY, true);
        map.put("channelAreaId", channelId);
        httpRequestHelper.startRequest(map, false);
    }

    private void requestMedalData() {
        XjqRequestContainer map2 = new XjqRequestContainer(FANS_RANK_MEDAL_TOTAL_QUERY, true);
        map2.put("service", "FANS_RANK_MEDAL_TOTAL_QUERY");
        map2.put("channelId", channelId);
        map2.put("authedUserId", LoginInfoHelper.getInstance().getUserId());
        httpRequestHelper.startRequest(map2, false);
    }

    private void init() {
        emptyLayout.setVisibility(View.GONE);
        emptyIv.setImageResource(R.drawable.icon_no_content_about_match_schedule_detail);
        httpRequestHelper = new HttpRequestHelper(context, this);
        liveGuestAdapter = new LiveGuestAdapter(context, seatSimpleList);
        liveMedalAdapter = new LiveMedalAdapter(context, rankedMedalListData);
        listView.setAdapter(liveGuestAdapter);
        switchTabView.setCheckedTab(SwitchTabView.TAB_LEFT);
        switchTabView.setOnSwitchTabListener(new SwitchTabView.IonSwitchTab() {
            @Override
            public void onSwitchLeftTab() {
                requestSeatData();
                listView.setAdapter(liveGuestAdapter);
                emptyLayout.setVisibility(View.GONE);
                bottomSeatLay.setVisibility(View.VISIBLE);
                bottomDivider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSwitchRightTab() {
                requestMedalData();
                listView.setAdapter(liveMedalAdapter);
                bottomSeatLay.setVisibility(View.GONE);
                bottomDivider.setVisibility(View.GONE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //非龙椅,vip座位有人坐上才可以私聊,且不可以与自己私聊
                if (seatSimpleList.get(position) != null &&
                        seatSimpleList.get(position).getUserId() != null &&
                        !LoginInfoHelper.getInstance().getUserId().equals(seatSimpleList.get(position).getUserId())) {
                    String userId = seatSimpleList.get(position).getUserId();
                    new PersonalInfoDialog(context, userId).show();
                }
            }
        });
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case VIP_SEAT_LIST_QUERY:
                responseSeatQuerySuccess(jsonObject);
                break;
            case FANS_RANK_MEDAL_TOTAL_QUERY:
                responseRankMedalQuerySuccess(jsonObject);
                break;
        }

    }

    private void responseRankMedalQuerySuccess(JSONObject jsonObject) {
        RankedMedalInfoBean rankedMedalInfoBean = new Gson().fromJson(jsonObject.toString(), RankedMedalInfoBean.class);
        rankedMedalListData.clear();
        if (rankedMedalInfoBean.getRankMedalTotalSimpleList() != null && rankedMedalInfoBean.getRankMedalTotalSimpleList().size() > 0) {
            rankedMedalInfoBean.operate(context);
            rankedMedalListData.addAll(rankedMedalInfoBean.getRankMedalTotalSimpleList());
            liveMedalAdapter.notifyDataSetChanged();
            emptyLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            if (rankedMedalInfoBean.isAnchorHasFanMedal()) {
                emptyTv.setText("无人上榜");
            } else {
                emptyTv.setText("主播尚未开通粉丝勋章");
            }
        }
    }

    private void responseSeatQuerySuccess(JSONObject jsonObject) {
        SeatInfoBean seatInfoBean = new Gson().fromJson(jsonObject.toString(), SeatInfoBean.class);
        seatInfoBean.operatorData();
        seatSimpleList.clear();
        if (seatInfoBean.getSeatSitterSimpleList() != null && seatInfoBean.getSeatSitterSimpleList().size() > 0) {
            seatSimpleList.addAll(seatInfoBean.getSeatSitterSimpleList());
        }
        liveGuestAdapter.notifyDataSetChanged();
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {

    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }
}
